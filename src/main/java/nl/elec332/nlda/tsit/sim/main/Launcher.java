package nl.elec332.nlda.tsit.sim.main;

import nl.elec332.nlda.tsit.sim.api.ISimulatedGun;

import javax.vecmath.Vector3d;

/**
 * Created by Elec332 on 22-1-2020
 */
public class Launcher {

    public Launcher(ISimulatedGun gun, Vector3d position) {
        this.gun = gun;
        this.position = position;
        this.speed = 0; //todo
    }

    private final ISimulatedGun gun;
    private final double speed;
    private final Vector3d position;

    public void aim(int bearing, int elevation) {
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
