package nl.elec332.nlda.tsit.sim.util;

import com.google.common.collect.Sets;
import nl.elec332.nlda.tsit.sim.main.radar.TrackedObject;

import java.util.function.Predicate;

/**
 * Created by Elec332 on 28-1-2020
 */
public enum ObjectType {

    HELI(120, 0),
    AIRLINER(257, 120, obj -> obj.getCurrentPosition().z > 2000),
    FIGHTER(600, 100),
    MISSILE(3850, 201),
    UNKNOWN(0, 0);

    @SafeVarargs
    ObjectType(int maxSpeed, int minSpeed, Predicate<TrackedObject>... extras) {
        this.maxSpeed = maxSpeed;
        this.minSpeed = minSpeed;
        if (extras.length > 0) {
            Predicate<TrackedObject> p = extras[0];
            for (int i = 1; i < extras.length; i++) {
                p = p.and(extras[i]);
            }
            helper = p;
        } else {
            helper = meh -> true;
        }
    }

    private final Predicate<TrackedObject> helper;
    private final int maxSpeed, minSpeed;

    public Boolean isPossibleTarget(TrackedObject object) {
        if (this == UNKNOWN) {
            return true;
        }
        if (Sets.newHashSet(object.getLocations()).size() < 3) {
            return null;
        }
        double speed = object.getCurrentSpeed().length();
        if (speed > maxSpeed || minSpeed > speed) {
            return false;
        }
        return helper.test(object);
    }

}
