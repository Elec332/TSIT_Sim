package nl.elec332.nlda.tsit.sim.simulation;

import com.google.common.collect.Lists;
import nl.elec332.nlda.tsit.sim.math.Calculator;
import nl.elec332.nlda.tsit.sim.util.ObjectClassification;
import nl.elec332.nlda.tsit.sim.util.RadarMeasurement;

import javax.vecmath.Vector3d;
import java.awt.*;
import java.util.List;

/**
 * Created by Elec332 on 15-1-2020.
 */
public class TrackedObject {

    public TrackedObject(RadarMeasurement initialMeasurement) {
        this.measurements = Lists.newArrayList(initialMeasurement);
        this.locations = Lists.newArrayList(Calculator.calculatePosition(initialMeasurement));
        this.speeds = Lists.newArrayList();
        this.lastSpeed = new Vector3d(0, 0, 0);
    }

    private final List<RadarMeasurement> measurements;
    private final List<Vector3d> locations;
    private final List<Vector3d> speeds;
    private Vector3d lastSpeed;
    private ObjectClassification objectType;

    public ObjectClassification getObjectClassification() {
        return objectType;
    }

    public Color getColor() {
        return Color.GREEN;
    }

    public List<Vector3d> getLocations() {
        return locations;
    }

    public List<Vector3d> getSpeeds() {
        return speeds;
    }

    public void setObjectClassification(ObjectClassification objectType) {
        this.objectType = objectType;
    }

    public void addMeasurement(RadarMeasurement measurement) {
        measurements.add(measurement);
        locations.add(Calculator.calculatePosition(measurement));
        lastSpeed = Calculator.calculateSpeed(measurements.get(measurements.size() - 2), measurements.get(measurements.size() - 1));
        this.speeds.add(lastSpeed);
    }

    public Vector3d getSpeed(RadarMeasurement measurement) {
        return Calculator.calculateSpeed(measurements.get(measurements.size() - 1), measurement);
    }

    public double getSpeedDiff(RadarMeasurement measurement) {
        if (measurements.size() == 1) {
            return -1;
        }
        Vector3d retSpeed = getSpeed(measurement);
        retSpeed.sub(lastSpeed);
        return retSpeed.length();
    }

    public double getDistanceToLast(Vector3d pos) {
        return Math.sqrt(Calculator.calculatePosition(measurements.get(measurements.size() - 1)).dot(pos));
    }

}
