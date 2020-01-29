package nl.elec332.nlda.tsit.sim.main.radar;

import com.google.common.collect.Sets;
import com.google.common.primitives.Doubles;
import nl.elec332.nlda.tsit.sim.math.Calculator;
import nl.elec332.nlda.tsit.sim.util.Constants;
import nl.elec332.nlda.tsit.sim.util.ObjectClassification;
import nl.elec332.nlda.tsit.sim.util.RadarMeasurement;
import org.jzy3d.maths.BoundingBox3d;
import org.jzy3d.maths.Coord3d;

import javax.vecmath.Vector3d;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Elec332 on 15-1-2020.
 */
public class ObjectTracker {

    public ObjectTracker() {
        this.trackedObjects = Sets.newHashSet();
        this.trackedObjects_ = Collections.unmodifiableSet(trackedObjects);
        this.hidden = Sets.newHashSet();
    }

    private final Set<TrackedObject> trackedObjects, trackedObjects_;
    private final Set<Integer> hidden;

    public Set<TrackedObject> getFilteredObjects() {
        return getTrackedObjects().stream().filter(obj -> !hidden.contains(obj.getId())).collect(Collectors.toSet());
    }

    public Set<TrackedObject> getTrackedObjects() {
        return trackedObjects_;
    }

    private void notifyHit(TrackedObject object) {
        this.hidden.add(object.getId());
        object.setObjectClassification(ObjectClassification.HIT);
    }

    public TrackedObject receiveMeasurement(final RadarMeasurement measurement) {
        TrackedObject ret;
        if (trackedObjects.isEmpty()) {
            ret = new TrackedObject(measurement);
            trackedObjects.add(ret);
            return ret;
        }

        Vector3d pos = Calculator.calculatePosition(measurement);
        Optional<TrackedObject> killed = this.trackedObjects.stream().filter(obj -> obj.getCurrentPosition().equals(pos)).findFirst();
        if (killed.isPresent()) {
            TrackedObject obj = killed.get();
            if (obj.getLocations().size() > 2) {
                if (!hidden.contains(obj.getId())) {
                    obj.addMeasurement(measurement);
                    obj.setObjectClassification(ObjectClassification.DOWN);
                    hidden.add(obj.getId());
                }
                return obj;
            }
        }

        Set<TrackedObject> trackedObjects = this.trackedObjects.stream()
                .filter(object -> object.getObjectClassification() != ObjectClassification.DOWN)
                .filter(obj -> Math.abs(obj.getSpeedDiff(measurement)) < Constants.MAX_ACCELERATION)
                .filter(obj -> obj.getSpeed(measurement).length() < Constants.MAX_SPEED)
                .collect(Collectors.toSet());
        if (trackedObjects.isEmpty()) {
            ret = new TrackedObject(measurement);
            this.trackedObjects.add(ret);
            return ret;
        }
        ret = trackedObjects.stream()
                .min((o1, o2) -> Doubles.compare(o1.getSpeedDiff(measurement), o2.getSpeedDiff(measurement)))
                .get();
        ret.addMeasurement(measurement);

        //checkHits();
        return ret;
    }

    private void checkHits() {
        for (TrackedObject object : trackedObjects) {
            Set<TrackedObject> inRange = trackedObjects.stream()
                    .filter(obj -> obj != object)
                    .filter(obj -> obj.getObjectClassification() != ObjectClassification.DOWN)
                    .filter(obj -> obj.getDistanceTo(object.getCurrentPosition()) - 10 <= object.getCurrentSpeed().length() + obj.getCurrentSpeed().length())
                    .collect(Collectors.toSet());
            if (!inRange.isEmpty()) {
                for (TrackedObject target : inRange) {
                    System.out.println("object " + object.getId() + " and " + target.getId() + " are within 10m of each other ");
                    for (int i = 0; i < 500; i++) {
                        Vector3d getPrevObj = object.getFuturePosition(-i/500.0);
                        Vector3d getPrevTarget = target.getFuturePosition(-i/500.0);
                        if (Calculator.distance(getPrevObj, getPrevTarget) < Constants.KILL_RANGE) {
                            notifyHit(object);
                            notifyHit(target);
                            System.out.println("And... it's a hit \\o/ ");
                            break;
                        }
                    }

//                    System.out.println("object " + object.getId() + " and " + target.getId() + " are within 10m of each other ");
//                    if (object.getBound().intersect(target.getBound())) {
//                        System.out.println("And boundingboxes intersect");
//                        Vector3d deltaVobject = object.getCurrentSpeed();
//                        Coord3d deltaCobject = new Coord3d(deltaVobject.x, deltaVobject.y, deltaVobject.z).div(Constants.HIT_SLICING);
//
//                        Vector3d deltaVtarget = target.getCurrentSpeed();
//                        Coord3d deltaCtarget = new Coord3d(deltaVtarget.x, deltaVtarget.y, deltaVtarget.z).div(Constants.HIT_SLICING);
//
//                        BoundingBox3d boundTarget = new BoundingBox3d(Arrays.asList(target.getCoord().sub(5), target.getCoord().add(5)));
//                        BoundingBox3d boundObject = new BoundingBox3d(Arrays.asList(object.getCoord().sub(5), object.getCoord().add(5)));
//                        for (int i = 0; i < 300; i++) {
//                            if (boundTarget.intersect(boundObject)) {
//                                notifyCrashed(object);
//                                notifyCrashed(target);
//                                i = 300;
//                                System.out.println("And... it's a hit \\o/ ");
//                            }
//                            boundTarget.shift(deltaCtarget);
//                            boundObject.shift(deltaCobject);
//                        }
//                    }
                }
            }
        }
    }
}
