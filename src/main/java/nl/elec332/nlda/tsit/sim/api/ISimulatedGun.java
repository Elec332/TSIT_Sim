package nl.elec332.nlda.tsit.sim.api;

/**
 * Created by Elec332 on 22-1-2020
 */
public interface ISimulatedGun {

    void aim(double bearing, double elevation);

    boolean fire();

}
