package nl.elec332.nlda.tsit.sim.main.radar;

import com.google.common.collect.Sets;
import com.google.common.primitives.Doubles;
import nl.elec332.nlda.tsit.sim.util.Constants;
import nl.elec332.nlda.tsit.sim.util.RadarMeasurement;
import org.jzy3d.maths.BoundingBox3d;
import org.jzy3d.maths.Coord3d;

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
    }



    private final Set<TrackedObject> trackedObjects, trackedObjects_;

    public Set<TrackedObject> getTrackedObjects() {
        return trackedObjects_;
    }

    public TrackedObject receiveMeasurement(final RadarMeasurement measurement) {
        TrackedObject ret;
        if (trackedObjects.isEmpty()) {
            ret = new TrackedObject(measurement);
            trackedObjects.add(ret);
            return ret;
        }
        //TODO: Voeg check toe of er al een snelheid is, want bij eerste measurement is snelheid 0, en als de tweede
        // measurement dan een snelheid van > max_acceleration heeft wordt deze als nieuw object gezien
        Set<TrackedObject> trackedObjects = this.trackedObjects.stream()
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
