package nl.elec332.nlda.tsit.sim.util;

import javax.annotation.concurrent.Immutable;
import java.util.Objects;

/**
 * Created by Elec332 on 8-1-2020.
 */
@Immutable
public class RadarMeasurement {

    public RadarMeasurement(double time, double heading, double elevation, double distance) {
        this.time = time;
        this.heading = heading;
        this.elevation = elevation;
        this.distance = distance;
    }

    private final double time, heading, elevation, distance;

    public double getTime() {
        return time;
    }

    public double getHeading() {
        return heading;
    }

    public double getElevation() {
        return elevation;
    }

    public double getDistance() {
        return distance;
    }

    @Override
    public String toString() {
        return "Time: " + time + ", Heading: " + heading + ", Elevation:" + elevation + ", Distance: " + distance;
    }

    @Override
    public int hashCode() {
        return Objects.hash(time, heading, elevation, distance);
    }

}
