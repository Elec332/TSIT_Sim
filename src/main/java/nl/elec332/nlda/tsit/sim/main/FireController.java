package nl.elec332.nlda.tsit.sim.main;

import com.google.common.collect.Lists;
import nl.elec332.nlda.tsit.sim.main.radar.TrackedObject;
import nl.elec332.nlda.tsit.sim.util.Constants;
import nl.elec332.nlda.tsit.sim.util.ObjectClassification;

import java.util.Comparator;
import java.util.List;

/**
 * Created by Elec332 on 22-1-2020
 */
public class FireController {

    FireController(Platform platform) {
        this.platform = platform;
        this.targets = Lists.newArrayList();
    }

    private final Platform platform;
    private final List<TrackedObject> targets;

    void updateObject(TrackedObject object) {
        TrackedObject obj = targets.stream().min(Comparator.comparing(o -> object.getDistanceTo(object.getCurrentPosition()))).orElse(null);
        if (obj != null && obj != object && obj.getDistanceTo(object.getCurrentPosition()) < Constants.KILL_RANGE) {
            targets.remove(obj);
            System.out.println("KILL: " + object.getId());
        }
        if (object.getObjectClassification() == ObjectClassification.HOSTILE && !targets.contains(object)) {
            fireAt(object);
            System.out.println(object);
            System.out.println(object.getCurrentPosition());
            targets.add(object);
        }
    }

    public void fireAt(TrackedObject object) {
        System.out.println("FIRE@: " + object.getId());
        //Calculate
    }

    private boolean fire(int gun) {
        Launcher launcher = platform.getGuns().get(gun);
        boolean ret = launcher.fire();
        if (ret) {
            platform.getClassifier().notifyFriendly(launcher.getPosition());
        }
        return ret;
    }

}
