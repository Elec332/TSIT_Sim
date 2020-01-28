package nl.elec332.nlda.tsit.sim.main.radar;

import com.google.common.collect.Sets;
import com.google.common.primitives.Doubles;
import nl.elec332.nlda.tsit.sim.math.Calculator;
import nl.elec332.nlda.tsit.sim.util.Constants;
import nl.elec332.nlda.tsit.sim.util.ObjectClassification;
import nl.elec332.nlda.tsit.sim.util.RadarMeasurement;

import javax.vecmath.Vector3d;
import java.util.*;
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

    public void notifyCrashed(int id) {
        this.hidden.add(id);
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
        return ret;
    }



}
