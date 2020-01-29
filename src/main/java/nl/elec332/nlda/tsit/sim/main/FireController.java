package nl.elec332.nlda.tsit.sim.main;

import com.google.common.collect.Lists;
import nl.elec332.nlda.tsit.sim.main.radar.TrackedObject;
import nl.elec332.nlda.tsit.sim.math.Calculator;
import nl.elec332.nlda.tsit.sim.util.Constants;
import nl.elec332.nlda.tsit.sim.util.ObjectClassification;
import org.jzy3d.maths.BoundingBox3d;
import org.jzy3d.maths.Coord3d;

import javax.vecmath.Vector3d;
import java.util.*;
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
                Target fired = fireAt(object);
                if (fired != null) {
                    targets.add(object);
                    platform.getClassifier().notifyFriendly(fired);
                }
            }
        }
        List<Target> fireds = Lists.newArrayList();
        boolean[] fired = new boolean[]{false};
        platform.getClassifier().getFriendlies(f -> {

            TrackedObject target = f.getTarget();

            if (target != object || fired[0]) {
                return;
            }
            fired[0] = true;
            if (Calculator.distance(target.getCurrentSpeed(),target.getSpeeds().get(target.getSpeeds().size()-2)) > Constants.FUZZY) {
                fireds.add(fireAt(target));
            }
        });

        fireds.stream().filter(Objects::nonNull).forEach(obj -> platform.getClassifier().notifyFriendly(obj));

    }



    public Target fireAt(TrackedObject object) {
        if (object.getCurrentSpeed().length() < 1) {
            return null;
        }
        System.out.println("FIRE@: " + object.getId());
        double tth = object.getDistanceTo(Constants.ZERO_POS) / Constants.PROJECTILE_SPEED;
        Vector3d fut = object.getFuturePosition(tth);
        double dist = 599, bearing = 0, elevation = 0;

        elevation = fut.z < 0 ? 0 : Math.atan((fut.z + Constants.GRAVITY * tth * tth) / Math.sqrt(fut.x * fut.x + fut.y * fut.y));
        for (int i = 0; i < 10; i++) {
            tth = Math.sqrt(fut.x * fut.x + fut.y * fut.y) / (Constants.PROJECTILE_SPEED * Math.cos(elevation));
            fut = object.getFuturePosition(tth);
            bearing = Math.atan(fut.x / fut.y);
            elevation = fut.z < 0 ? 0 : Math.atan((fut.z + Constants.GRAVITY * tth * tth) / Math.sqrt(fut.x * fut.x + fut.y * fut.y));

            Vector3d futProj = new Vector3d(Constants.PROJECTILE_SPEED * Math.sin(bearing) * Math.cos(elevation)
                    , Constants.PROJECTILE_SPEED * Math.cos(bearing) * Math.cos(elevation)
                    , Constants.PROJECTILE_SPEED * Math.sin(elevation) - Constants.GRAVITY * tth);
            futProj.scale(tth);
            dist = Calculator.distance(futProj, fut);
        }

        System.out.println("Target location in " + tth + "s is " + fut);

        bearing = Math.toDegrees(bearing);
        elevation = Math.toDegrees(elevation);

        if (fut.y < 0) {
            bearing = 180 + bearing;
        }
        for (int i = 0; i < Constants.NUMBER_OF_GUNS; i++) {
            Target fired = fire(i, bearing, elevation, object);
            if (fired != null) {
                return fired;
            }
        }
        return null;
    }

    private Target fire(int gun, double bearing, double elevation, TrackedObject target) {
        System.out.println("Firing at (bearing, elevation): (" + bearing + ", " + elevation + ")");
        Launcher launcher = platform.getGuns().get(gun);
        launcher.aim(bearing, elevation);
        boolean ret = launcher.fire();
        Target fired = null;
        if (ret) {
            bearing = Math.toRadians(bearing);
            elevation = Math.toRadians(elevation);
            Vector3d projLoc = new Vector3d(Math.sin(bearing) * Math.cos(elevation)
                    , Math.cos(bearing) * Math.cos(elevation)
                    , Math.sin(elevation) - Constants.GRAVITY / launcher.getFiringSpeed());
            projLoc.scale(launcher.getFiringSpeed());
            fired = new Target(projLoc, target);
        }
        return fired;
    }

}
