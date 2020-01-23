package nl.elec332.nlda.tsit.sim.main.radar;

import com.google.common.collect.Lists;
import nl.elec332.nlda.tsit.sim.math.Calculator;
import nl.elec332.nlda.tsit.sim.util.ObjectClassification;
import nl.elec332.nlda.tsit.sim.util.RadarMeasurement;
import org.jzy3d.maths.BoundingBox3d;
import org.jzy3d.maths.Coord3d;

import javax.annotation.Nonnull;
import javax.vecmath.Vector3d;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Elec332 on 15-1-2020.
 */
public class TrackedObject {

    public TrackedObject(RadarMeasurement initialMeasurement) {
        this.measurements = Lists.newArrayList(initialMeasurement);
        this.lastPosition = Calculator.calculatePosition(initialMeasurement);
        this.locations = Lists.newArrayList(this.lastPosition);
        this.speeds = Lists.newArrayList();
        this.lastSpeed = new Vector3d(0, 0, 0);
        this.id = idTracker++;
        setObjectClassification(ObjectClassification.UNKNOWN);
    }

    private static  int idTracker = 0;

    private final int id;
    private final List<RadarMeasurement> measurements;
    private final List<Vector3d> locations;
    private final List<Vector3d> speeds;
    private Vector3d lastSpeed, lastPosition;
    private ObjectClassification objectType;
    private Color color;

    @Nonnull
    public ObjectClassification getObjectClassification() {
        return objectType;
    }

    public void hit() {objectType = ObjectClassification.DOWN;}

    public BoundingBox3d getBound() {
        Vector3d posV = getCurrentPosition();
        Vector3d speedV = getCurrentSpeed();
        Coord3d posC = new Coord3d(posV.x, posV.y, posV.z);
        Coord3d speedC = new Coord3d(speedV.x, speedV.y, speedV.z);

        return new BoundingBox3d(Arrays.asList(posC.sub(5), posC.add(speedC.add(5))));
    }

    public Coord3d getCoord() {
        return new Coord3d(lastPosition.x, lastPosition.y, lastPosition.z);
    }


    public int getId() {
        return id;
    }

    public Color getColor() {
        return color;
    }

    public List<Vector3d> getLocations() {
        return locations;
    }

    public List<Vector3d> getSpeeds() {
        return speeds;
    }

    public Vector3d getCurrentPosition() {
        return lastPosition;
    }

    public Vector3d getCurrentSpeed() {
        return lastSpeed;
    }

    public Vector3d getFuturePosition(int seconds) {
        Vector3d futurePosition = new Vector3d(lastPosition);
        Vector3d speed = new Vector3d(lastSpeed);
        speed.scale(seconds);
        futurePosition.add(speed);
        return futurePosition;
    }

    public void setObjectClassification(ObjectClassification objectType) {
        this.objectType = objectType;
        this.color = objectType.getColor();
    }

    public void addMeasurement(RadarMeasurement measurement) {
        measurements.add(measurement);
        this.lastPosition = Calculator.calculatePosition(measurement);
        locations.add(this.lastPosition);
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

    public double getDistanceTo(Vector3d pos) {
        return Calculator.distance(getCurrentPosition(), pos);
    }

}
