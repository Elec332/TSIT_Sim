package nl.elec332.nlda.tsit.sim.main;

import nl.elec332.nlda.tsit.sim.api.ISimulatedGun;
import nl.elec332.nlda.tsit.sim.util.Constants;

import javax.vecmath.Vector3d;

/**
 * Created by Elec332 on 22-1-2020
 */
public class Launcher {

    public Launcher(ISimulatedGun gun, Vector3d position, double speed) {
        this.gun = gun;
        this.position = position;
        this.speed = speed;
    }

    private final ISimulatedGun gun;
    private final double speed;
    private final Vector3d position;

    public void aim(double bearing, double elevation) {
        gun.aim(bearing, elevation);
    }

    public boolean fire()  {
        return gun.fire();
    }

    public double getFiringSpeed() {
        return this.speed;
    }

    public Vector3d getPosition() {
        return position;
    }

}
