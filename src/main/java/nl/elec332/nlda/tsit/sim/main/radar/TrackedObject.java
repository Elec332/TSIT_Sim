package nl.elec332.nlda.tsit.sim.main.radar;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import nl.elec332.nlda.tsit.sim.math.Calculator;
import nl.elec332.nlda.tsit.sim.util.Constants;
import nl.elec332.nlda.tsit.sim.util.ObjectClassification;
import nl.elec332.nlda.tsit.sim.util.ObjectType;
import nl.elec332.nlda.tsit.sim.util.RadarMeasurement;
import org.jzy3d.maths.BoundingBox3d;
import org.jzy3d.maths.Coord3d;

import javax.annotation.Nonnull;
import javax.vecmath.Vector3d;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

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
        this.types = Lists.newArrayList(ObjectType.UNKNOWN);
        this.typeLocked = false;
        this.fireClearance = null;
    }

    private static int idTracker = 0;

    private final int id;
    private final List<RadarMeasurement> measurements;
    private final List<Vector3d> locations;
    private final List<Vector3d> speeds;
    private Vector3d lastSpeed, lastPosition;
    private ObjectClassification objectType;
    private List<ObjectType> types;
    private boolean typeLocked;
    private Boolean fireClearance;
    private Color color;

    @Nonnull
    public ObjectClassification getObjectClassification() {
        return objectType;
    }

    public boolean isUnknown() {
        return types.size() == 1 && types.get(0) == ObjectType.UNKNOWN;
    }

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

    public List<ObjectType> getTypes() {
        return types;
    }

    public void setFireClearance(Boolean fireClearance) {
        this.fireClearance = fireClearance;
    }

    public Boolean hasFireClearance() {
        return fireClearance;
    }

    public String getObjectTypeString() {
        StringBuilder type = new StringBuilder();
        if (types.size() > 1) {
            type.append("Possibly ");
            type.append(types.get(0));
            for (int i = 1; i < types.size(); i++) {
                type.append("/");
                type.append(types.get(i));
            }
        } else {
            if (types.size() == 0) {
                type.append(ObjectType.UNKNOWN);
            } else {
                type.append(types.get(0));
            }
        }
        return type.toString();
    }

    public void setObjectType(ObjectType type) {
        setObjectType(Sets.newHashSet(type));
    }

    public void setObjectType(Set<ObjectType> types) {
        if (typeLocked) {
            return;
        }
        if (types.size() > 1) {
            types.remove(ObjectType.UNKNOWN);
        }
        this.types = ImmutableList.copyOf(types);
    }

    public void lockType() {
        this.typeLocked = true;
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

    public Vector3d getFuturePosition(double seconds) {
        Vector3d futurePosition = new Vector3d(getCurrentPosition());
        Vector3d speed = new Vector3d(getCurrentSpeed());
        speed.scale(seconds);
        futurePosition.add(speed);
        if (objectType == ObjectClassification.FRIENDLY && types.contains(ObjectType.MISSILE)) {
            futurePosition.z -= seconds*seconds*Constants.GRAVITY;
        }
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
