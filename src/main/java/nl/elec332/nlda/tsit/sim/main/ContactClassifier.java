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
    private final List<Vector3d> friendlies;

    public void notifyFriendly(Vector3d position) {
        friendlies.add(position);
    }

    void updateObject(TrackedObject object) {
        if (Math.round(object.getDistanceTo(Constants.ZERO_POS)) == Constants.PROJECTILE_SPEED) {
            object.setObjectClassification(ObjectClassification.FRIENDLY);
            return;
        }
        Iterator<Vector3d> it = friendlies.iterator();
        while (it.hasNext()) {
            Vector3d friendly = it.next();
            if (object.getLocations().contains(friendly)) {
                it.remove();
                object.setObjectClassification(ObjectClassification.FRIENDLY);
                return;
            }
        }
        classify(object);
    }

    private void classify(TrackedObject object) {
        if (object.getObjectClassification() == ObjectClassification.DOWN || object.getObjectClassification() == ObjectClassification.FRIENDLY){
            return;
        }
        //TODO add boolean not changable
        if (object.getDistanceTo(Constants.ZERO_POS) < Constants.DEFAULT_ENEMY_RANGE) {
            object.setObjectClassification(ObjectClassification.HOSTILE);
        }
    }

}
