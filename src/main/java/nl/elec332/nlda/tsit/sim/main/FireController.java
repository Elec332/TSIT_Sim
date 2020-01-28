package nl.elec332.nlda.tsit.sim.main;

import com.google.common.collect.Lists;
import nl.elec332.nlda.tsit.sim.main.radar.TrackedObject;
import nl.elec332.nlda.tsit.sim.math.Calculator;
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

        if (object.getObjectClassification() == ObjectClassification.HOSTILE && !targets.contains(object)) {
            if (object.getLocations().size() <= 3 && object.isUnknown()) {
                return;
            }
            Boolean clearance = object.hasFireClearance();
            if (clearance == null) {
                this.platform.getCommander().requestClearance(object);
                return;
            }
            if (clearance) {
                System.out.println(object);
                System.out.println(object.getCurrentPosition());
                if (fireAt(object)) {
                    targets.add(object);
                }
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
                    if (object.getBound().intersect(target.getBound())) {
                        System.out.println("And boundingboxes intersect");
                        Vector3d deltaVobject = object.getCurrentSpeed();
                        Coord3d deltaCobject = new Coord3d(deltaVobject.x, deltaVobject.y, deltaVobject.z).div(Constants.HIT_SLICING);

                        Vector3d deltaVtarget = target.getCurrentSpeed();
                        Coord3d deltaCtarget = new Coord3d(deltaVtarget.x, deltaVtarget.y, deltaVtarget.z).div(Constants.HIT_SLICING);

                        BoundingBox3d boundTarget = new BoundingBox3d(Arrays.asList(target.getCoord().sub(5), target.getCoord().add(5)));
                        BoundingBox3d boundObject = new BoundingBox3d(Arrays.asList(object.getCoord().sub(5), object.getCoord().add(5)));
                        for (int i = 0; i < 300; i++) {
                            if (boundTarget.intersect(boundObject)) {
                                this.platform.getRadar().notifyCrashed(object);
                                this.platform.getRadar().notifyCrashed(target);
                                i = 300;
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
        if (object.getCurrentSpeed().length() < 1) {
            return false;
        }
        System.out.println("FIRE@: " + object.getId());
        double tth = object.getDistanceTo(Constants.ZERO_POS) / Constants.PROJECTILE_SPEED;
        Vector3d fut = object.getFuturePosition(tth);
        double dist, bearing, elevation;
        elevation = fut.z < 0 ? 0 : Math.atan((fut.z + tth * Constants.GRAVITY) / Math.sqrt(fut.x * fut.x + fut.y * fut.y));
        dist = 500;
        for (int i = 0; i < 50 && dist > Constants.KILL_RANGE; i++) {
            tth = Calculator.distance(fut, Constants.ZERO_POS) / (Constants.PROJECTILE_SPEED * Math.cos(elevation/2));
            fut = object.getFuturePosition(tth);
            bearing = Math.atan(fut.x / fut.y);
            elevation = fut.z < 0 ? 0 : Math.atan((fut.z + Constants.GRAVITY * tth * tth) / Math.sqrt(fut.x * fut.x + fut.y * fut.y));

            Vector3d futProj = new Vector3d(Constants.PROJECTILE_SPEED * Math.sin(bearing) * Math.cos(elevation)
                    , Constants.PROJECTILE_SPEED * Math.cos(bearing) * Math.cos(elevation)
                    , Constants.PROJECTILE_SPEED * Math.sin(elevation) - Constants.GRAVITY * tth);
            futProj.scale(tth);
            dist = Calculator.distance(futProj, fut);
        }

        fut.add(new Vector3d(0,0, tth * Constants.GRAVITY));
        System.out.println("Target location in " + tth + "s is " + fut);
        System.out.println("Distance at impact will be " + dist + "m");
        bearing = Math.toDegrees(Math.atan(fut.x / fut.y)); //  o / a
        if (fut.y < 0) {
            bearing = 180 + bearing;
        }
        elevation = fut.z < 0 ? 0 : Math.toDegrees(Math.atan(fut.z / Math.sqrt(fut.x * fut.x + fut.y * fut.y)));

        for (int i = 0; i < Constants.NUMBER_OF_GUNS; i++) {
            if (fire(i, bearing, elevation, object)) {
                break;
            }
        }
        return true;
    }

    private boolean fire(int gun, double bearing, double elevation, TrackedObject target) {
        System.out.println("Firing at (bearing, elevation): (" + bearing + ", " + elevation + ")");
        Launcher launcher = platform.getGuns().get(gun);
        launcher.aim(bearing, elevation);
        boolean ret = launcher.fire();
        if (ret) {
            bearing = Math.toRadians(bearing);
            elevation = Math.toRadians(elevation);
            Vector3d projLoc = new Vector3d(Math.sin(bearing) * Math.cos(elevation)
                    , Math.cos(bearing) * Math.cos(elevation)
                    , Math.sin(elevation) - Constants.GRAVITY / launcher.getFiringSpeed());
            projLoc.scale(launcher.getFiringSpeed());
            platform.getClassifier().notifyFriendly(new Target(projLoc, target));
        }
        return ret;
    }

}
