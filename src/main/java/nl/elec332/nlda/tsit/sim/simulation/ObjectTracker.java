package nl.elec332.nlda.tsit.sim.simulation;

import com.google.common.collect.Sets;
import com.google.common.primitives.Doubles;
import nl.elec332.nlda.tsit.sim.util.RadarMeasurement;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Elec332 on 15-1-2020.
 */
public class ObjectTracker {

    public ObjectTracker() {
        this.trackedObjects = Sets.newHashSet();
        this.trackedObjects_ = Collections.unmodifiableSet(trackedObjects);
    }

    private static final int MAX_ACCELERATION = 100;
    private static final int MAX_SPEED = 1000;

    private final Set<TrackedObject> trackedObjects, trackedObjects_;

    public Set<TrackedObject> getTrackedObjects() {
        return trackedObjects_;
    }

    public void receiveMeasurement(final RadarMeasurement measurement) {
        if (trackedObjects.isEmpty()) {
            trackedObjects.add(new TrackedObject(measurement));
            return;
        }
        Set<TrackedObject> trackedObjects = this.trackedObjects.stream()
                .filter(obj -> Math.abs(obj.getSpeedDiff()) < MAX_ACCELERATION)
                .filter(obj -> obj.getSpeed().length() < MAX_SPEED)
                .collect(Collectors.toSet());
        if (trackedObjects.isEmpty()) {
            this.trackedObjects.add(new TrackedObject(measurement));
            return;
        }
        trackedObjects.stream().min((o1, o2) -> Doubles.compare(o1.getSpeedDiff(), o2.getSpeedDiff())).get().addMeasurement(measurement);
    }

}
