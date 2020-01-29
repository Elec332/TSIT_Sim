package nl.elec332.nlda.tsit.sim.main;

import com.google.common.collect.Lists;
import nl.elec332.nlda.tsit.sim.main.radar.TrackedObject;
import nl.elec332.nlda.tsit.sim.util.Constants;
import nl.elec332.nlda.tsit.sim.util.ObjectClassification;
import nl.elec332.nlda.tsit.sim.util.ObjectType;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Created by Elec332 on 22-1-2020
 */
public class ContactClassifier {

    public ContactClassifier(Platform platform) {
        this.friendlies = Lists.newArrayList();
        this.platform = platform;
        this.lock = new ReentrantLock(true);
    }

    private final Platform platform;
    private final List<Target> friendlies;
    private final ReentrantLock lock;

    public void getFriendlies(Consumer<Target> targetConsumer) {
        lock.lock();
        this.friendlies.forEach(targetConsumer);
        lock.unlock();
    }

    public void notifyFriendly(Target target) {
        lock.lock();
        friendlies.add(target);
        lock.unlock();
    }

    public void notifyFriendlyUnsafe(Target target) {
        friendlies.add(target);
    }

    void updateObject(TrackedObject object) {
        lock.lock();
        Iterator<Target> it = friendlies.iterator();
        while (it.hasNext()) {
            Target friendly = it.next();
            if (friendly.getTarget().getObjectClassification() == ObjectClassification.DOWN) {
                it.remove();
                continue;
            }
            if (friendly.hasMissile() && friendly.getMissile().getObjectClassification() == ObjectClassification.DOWN) {
                it.remove();
                continue;
            }
            System.out.println("Distance to target: " + friendly.getTarget().getDistanceTo(friendly.getPos()));
            if (!friendly.hasMissile() && object.getDistanceTo(friendly.getPos()) < 2) {
                object.setObjectClassification(ObjectClassification.FRIENDLY);
                friendly.setMissile(object);
                object.setObjectType(ObjectType.MISSILE);
                object.lockType();
                return;
            }
        }
        lock.unlock();
        classify(object);
    }

    private void classify(TrackedObject object) {
        Set<ObjectType> types = Arrays.stream(ObjectType.values()).filter(type -> {
            Boolean b = type.isPossibleTarget(object);
            return b != null && b;
        }).collect(Collectors.toSet());
        object.setObjectType(types);
        if (!object.getObjectClassification().getChangeable()) {
            return;
        }
        if (object.getObjectClassification() == ObjectClassification.UNKNOWN) {
            if (object.getTypes().contains(ObjectType.HELI)) {
                object.setObjectClassification(ObjectClassification.POSSIBLE_FRIENDLY);
            }
        }
        if (object.getDistanceTo(Constants.ZERO_POS) < Constants.DEFAULT_ENEMY_RANGE) {
            if (object.getTypes().contains(ObjectType.MISSILE) && object.getObjectClassification() == ObjectClassification.UNKNOWN) {
                object.setObjectClassification(ObjectClassification.HOSTILE);
            }
            if (object.getObjectClassification() == ObjectClassification.UNKNOWN) {
                object.setObjectClassification(ObjectClassification.HOSTILE);
            }
        }
    }


}
