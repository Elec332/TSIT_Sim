package nl.elec332.nlda.tsit.sim.main;

import com.google.common.collect.Lists;
import nl.elec332.nlda.tsit.sim.main.radar.TrackedObject;
import nl.elec332.nlda.tsit.sim.util.Constants;
import nl.elec332.nlda.tsit.sim.util.ObjectClassification;
import org.jzy3d.maths.BoundingBox3d;
import org.jzy3d.maths.Coord3d;

import javax.vecmath.Tuple3d;
import javax.vecmath.Vector3d;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
        checkHits();
    }

    private void checkHits() {
        for (TrackedObject object : targets) {
            Set<TrackedObject> inRange = targets.stream()
                    .filter(obj -> obj != object)
                    .filter(obj -> obj.getObjectClassification() != ObjectClassification.DOWN)
                    .filter(obj -> obj.getDistanceTo(object.getCurrentPosition()) - 10 <= object.getCurrentSpeed().length())
                    .collect(Collectors.toSet());
            if (!inRange.isEmpty()) {
                for (TrackedObject target : inRange) {
                    if (object.getBound().contains(target.getBound())){
                        Vector3d deltaVobject = object.getCurrentSpeed();
                        Coord3d deltaCobject = new Coord3d(deltaVobject.x, deltaVobject.y, deltaVobject.z).div(Constants.HIT_SLICING);

                        Vector3d deltaVtarget = target.getCurrentSpeed();
                        Coord3d deltaCtarget = new Coord3d(deltaVtarget.x, deltaVtarget.y, deltaVtarget.z).div(Constants.HIT_SLICING);

                        BoundingBox3d boundTarget = new BoundingBox3d(Arrays.asList(target.getCoord().sub(5), target.getCoord().add(5)));
                        BoundingBox3d boundObject = new BoundingBox3d(Arrays.asList(object.getCoord().sub(5), object.getCoord().add(5)));
                        for (int i = 0; i < 300; i++) {
                            if (boundTarget.intersect(boundObject)){
                                object.hit();target.hit();i=300;
                                System.out.println("OBJECT HIT:");
                                System.out.println(object);
                                System.out.println(target);
                            }
                            boundTarget.add(deltaCtarget);
                            boundObject.add(deltaCobject);
                        }
                    }
                }
            }
        }
    }

    public void fireAt(TrackedObject object) {
        System.out.println("FIRE@: " + object.getId());
        Vector3d fut = object. getFuturePosition(Constants.KILL_RANGE);
        fut.add(new Vector3d(0,0, Constants.KILL_RANGE * Constants.GRAVITY));
        double bearing = fut.angle(new Vector3d(0, fut.y, fut.z)) * 180 / Math.PI;
        double elevation = fut.angle(new Vector3d(fut.x, fut.y, 0)) * 180 / Math.PI;

        platform.getGuns().get(0).aim((int)Math.round(bearing), (int)Math.round(elevation));
        fire(0);
    }

    private boolean fire(int gun) {
        Launcher launcher = platform.getGuns().get(gun);
        boolean ret = launcher.fire();
        if (ret) {
            //TODO platform.getClassifier().notifyFriendly(launcher.getPosition());
        }
        return ret;
    }

}
