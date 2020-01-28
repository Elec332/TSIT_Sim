package nl.elec332.nlda.tsit.sim.main;

import com.google.common.collect.Lists;
import nl.elec332.nlda.tsit.sim.main.radar.TrackedObject;
import nl.elec332.nlda.tsit.sim.util.Constants;
import nl.elec332.nlda.tsit.sim.util.ObjectClassification;
import org.jzy3d.maths.BoundingBox3d;
import org.jzy3d.maths.Coord3d;

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
            System.out.println(object);
            System.out.println(object.getCurrentPosition());
            if (fireAt(object)) {
                targets.add(object);
            }
        }
        checkHits();
    }

    private void checkHits() {
        for (TrackedObject object : targets) {
            Set<TrackedObject> inRange = targets.stream()
                    .filter(obj -> obj != object)
                    .filter(obj -> obj.getObjectClassification() != ObjectClassification.DOWN)
                    .filter(obj -> obj.getDistanceTo(object.getCurrentPosition()) - 10 <= object.getCurrentSpeed().length() + obj.getCurrentSpeed().length())
                    .collect(Collectors.toSet());
            if (!inRange.isEmpty()) {
                for (TrackedObject target : inRange) {
                    System.out.println("object " + object.getId() + " and " + target.getId() + " are within 10m of each other ");
                    if (object.getBound().intersect(target.getBound())){
                        System.out.println("And boundingboxes intersect");
                        Vector3d deltaVobject = object.getCurrentSpeed();
                        Coord3d deltaCobject = new Coord3d(deltaVobject.x, deltaVobject.y, deltaVobject.z).div(Constants.HIT_SLICING);

                        Vector3d deltaVtarget = target.getCurrentSpeed();
                        Coord3d deltaCtarget = new Coord3d(deltaVtarget.x, deltaVtarget.y, deltaVtarget.z).div(Constants.HIT_SLICING);

                        BoundingBox3d boundTarget = new BoundingBox3d(Arrays.asList(target.getCoord().sub(5), target.getCoord().add(5)));
                        BoundingBox3d boundObject = new BoundingBox3d(Arrays.asList(object.getCoord().sub(5), object.getCoord().add(5)));
                        for (int i = 0; i < 300; i++) {
                            if (boundTarget.intersect(boundObject)){
                                object.hit();target.hit();i=300;
                                System.out.println("And... it's a hit \\o/ ");
                            }
                            boundTarget.shift(deltaCtarget);
                            boundObject.shift(deltaCobject);
                        }
                    }
                }
            }
        }
    }

    public boolean fireAt(TrackedObject object) {
        System.out.println("FIRE@: " + object.getId());
        if (object.getCurrentSpeed().length() < 1) {
            return false;
        }
        int tth = Constants.KILL_RANGE;
        Vector3d fut = object.getFuturePosition(tth);
        fut.add(new Vector3d(0,0, Constants.KILL_RANGE * Constants.GRAVITY));
        System.out.println("Target location in " + tth + "s is " + fut);
        double bearing = Math.toDegrees(fut.angle(new Vector3d(0, fut.y, fut.z)));
        double elevation = Math.toDegrees(fut.angle(new Vector3d(fut.x, fut.y, 0)));

        if (elevation < 0.75) {
            System.out.println("Target too low for hit (elv: " + elevation + ")");
            System.out.println(bearing);
            //System.out.println(fut.angle(new Vector3d(0, fut.y, fut.z)));
            return false;
        }

        for (int i = 0; i < Constants.NUMBER_OF_GUNS; i++) {
            if (fire(i, bearing, elevation, object)) {
                break;
            }
        }
        return true;
    }

    private boolean fire(int gun, double bearing, double elevation, TrackedObject target) {
        System.out.println("Firing at (bearing, elevation): (" + bearing + ", " + elevation + ")");
        bearing = Math.round(bearing);
        elevation = Math.round(elevation);
        Launcher launcher = platform.getGuns().get(gun);
        launcher.aim((int) bearing, (int) elevation);
        boolean ret = launcher.fire();
        if (ret) {
            bearing = Math.toRadians(bearing);
            elevation = Math.toRadians(elevation);
            Vector3d projLoc = new Vector3d(Math.sin(bearing) * Math.cos(elevation)
                    , Math.cos(bearing) * Math.cos(elevation)
                    , Math.sin(elevation) - Constants.GRAVITY / Constants.PROJECTILE_SPEED);
            projLoc.scale(Constants.PROJECTILE_SPEED);
            platform.getClassifier().notifyFriendly(new Target(projLoc, target));
        }
        return ret;
    }

}
