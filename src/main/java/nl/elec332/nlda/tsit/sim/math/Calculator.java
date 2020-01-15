package nl.elec332.nlda.tsit.sim.math;

import nl.elec332.nlda.tsit.sim.util.RadarMeasurement;

import javax.vecmath.Vector3d;

/**
 * Created by Elec332 on 13-1-2020
 */
public class Calculator {

    public static final double GRAVITY = 9.80665;

    public static Vector3d calculatePosition(RadarMeasurement measurement) {
        double height = measurement.getDistance() * Math.sin(toRad(measurement.getElevation()));
        double ground = measurement.getDistance() * Math.cos(toRad(measurement.getElevation()));
        double forward = ground * Math.cos(toRad(measurement.getHeading()));
        double side = ground * Math.sin(toRad(measurement.getHeading()));
        return new Vector3d(side, forward, height);
    }

    public static Vector3d calculateAcceleration(RadarMeasurement measurement1, RadarMeasurement measurement2, RadarMeasurement measurement3) {
        double dt1 = measurement2.getTime() - measurement1.getTime();
        Vector3d s1 = diff(calculatePosition(measurement1), calculatePosition(measurement2), dt1);
        double dt2 = measurement3.getTime() - measurement2.getTime();
        Vector3d s2 = diff(calculatePosition(measurement2), calculatePosition(measurement3), dt2);
        return diff(s1, s2, dt2);
    }

    public static Vector3d calculateSpeed(RadarMeasurement measurement1, RadarMeasurement measurement2) {
        return diff(calculatePosition(measurement1), calculatePosition(measurement2), measurement2.getTime() - measurement1.getTime());
    }

    public static Vector3d diff(Vector3d posA, Vector3d posB, double td) {
        Vector3d diff = new Vector3d(posB);
        diff.sub(posA);
        diff.scale(1 / td);
        return diff;
    }


    private static double toRad(double deg) {
        return deg / 180 * Math.PI;
    }

}
