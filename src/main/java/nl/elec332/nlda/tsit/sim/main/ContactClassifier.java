package nl.elec332.nlda.tsit.sim.main;

import com.google.common.collect.Lists;
import nl.elec332.nlda.tsit.sim.main.radar.TrackedObject;
import nl.elec332.nlda.tsit.sim.util.Constants;
import nl.elec332.nlda.tsit.sim.util.ObjectClassification;

import javax.vecmath.Vector3d;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Elec332 on 22-1-2020
 */
public class ContactClassifier {

    public ContactClassifier(Platform platform) {
        this.friendlies = Lists.newArrayList();
        this.platform = platform;
    }

    private final Platform platform;
    private final List<Target> friendlies;

    public void notifyFriendly(Target target) {
        friendlies.add(target);
    }

    void updateObject(TrackedObject object) {

        Iterator<Target> it = friendlies.iterator();
        while (it.hasNext()) {
            Target friendly = it.next();
            System.out.println("Distance to target: " + friendly.getTarget().getDistanceTo(friendly.getPos()));
            if (object.getDistanceTo(friendly.getPos()) < 2) {
                object.setObjectClassification(ObjectClassification.FRIENDLY);
                return;
            }
        }
        classify(object);
    }

    private void classify(TrackedObject object) {
        if (!object.getObjectClassification().getChangeable()){
            return;
        }
        if (object.getDistanceTo(Constants.ZERO_POS) < Constants.DEFAULT_ENEMY_RANGE) {
            object.setObjectClassification(ObjectClassification.HOSTILE);
        }
    }

}
