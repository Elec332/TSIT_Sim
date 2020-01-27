package nl.elec332.nlda.tsit.sim.main;

import nl.elec332.nlda.tsit.sim.main.radar.TrackedObject;

import javax.vecmath.Vector3d;

public class Target {

    private static Vector3d pos;
    private static TrackedObject target;

    public Target(Vector3d pos, TrackedObject target) {
        this.pos = pos;
        this.target = target;
    }

    public Vector3d getPos() {
        return pos;
    }

    public TrackedObject getTarget() {
        return target;
    }
}
