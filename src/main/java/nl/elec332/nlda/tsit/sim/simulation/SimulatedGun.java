package nl.elec332.nlda.tsit.sim.simulation;

import nl.elec332.nlda.tsit.sim.api.ISimulatedGun;

import java.io.IOException;

public class SimulatedGun extends AbstractSimulationSystem implements ISimulatedGun {

    public SimulatedGun(String host, int port, int gun) throws IOException {
        super(host, port, 3 + gun);
    }

    public void aim(int bearing, int elevation) {
        out.println("AIM " + bearing + " " + elevation);
    }

    public boolean fire() {
        try {
            out.println("FIRE");
            return in.readLine().equals("FIRED");
        } catch (Exception e) {
            return false;
        }
    }

}
