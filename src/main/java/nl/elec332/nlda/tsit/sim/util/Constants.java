package nl.elec332.nlda.tsit.sim.util;

import javax.vecmath.Vector3d;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by Elec332 on 22-1-2020
 */
public class Constants {

    public static final int ONE_KILOMETER = 1000;
    public static final int DEFAULT_ENEMY_RANGE = 15 * ONE_KILOMETER;
    public static final int KILL_RANGE = 10;
    public static final int MAX_ACCELERATION = 500;
    public static final int MAX_SPEED = 1200;
    public static final int PROJECTILE_SPEED = 1050;
    public static final int HIT_SLICING = 300;
    public static final int NUMBER_OF_GUNS = 4;

    public static final double GRAVITY = 9.81 / 2; //The simulator is amazing....

    public static final Vector3d ZERO_POS = new Vector3d(0, 0, 0);

    public static final NumberFormat TWO_DIGITS_FORMAT = new DecimalFormat("#.##");

    public static final double FUZZY = 0.1;
    public static final int GUN_RANGE = 6 * ONE_KILOMETER;
}
