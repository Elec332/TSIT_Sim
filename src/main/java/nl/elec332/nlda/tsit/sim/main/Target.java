package nl.elec332.nlda.tsit.sim.main;

import nl.elec332.nlda.tsit.sim.main.radar.TrackedObject;

import javax.vecmath.Vector3d;

public class Target {

    public Target(Vector3d pos, TrackedObject target) {
        this.pos = pos;
        this.target = target;
        this.time = this.target.getCurrentTime();
    }

    private final double time;
    private Vector3d pos;
    private TrackedObject target, missile;

    public Vector3d getPos() {
        if (missile != null) {
            return missile.getCurrentPosition();
        }
        return pos;
    }

    public double getFireTime() {
        return time;
    }

    public boolean hasMissile() {
        return missile != null;
    }

    public TrackedObject getTarget() {
        return target;
    }

    public void setMissile(TrackedObject missile) {
        this.missile = missile;
    }

    public TrackedObject getMissile() {
        return missile;
    }

}
