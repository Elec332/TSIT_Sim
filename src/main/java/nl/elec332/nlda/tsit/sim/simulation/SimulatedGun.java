package nl.elec332.nlda.tsit.sim.simulation;

import nl.elec332.nlda.tsit.sim.api.ISimulatedGun;

import java.io.IOException;

public class SimulatedGun extends AbstractSimulationSystem implements ISimulatedGun {

    public SimulatedGun(String host, int port, int gun) throws IOException {
        super(host, port, 3 + gun);
    }

    public void aim(double bearing, double elevation) {
        out.println("AIM " + bearing + " " + elevation);
        String line;
        try {
            line = in.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(line);
    }

    public boolean fire() {
        try {
            out.println("FIRE");
            String line = in.readLine();
            System.out.println(line);
            return line.equals("FIRED");
        } catch (Exception e) {
            return false;
        }
    }

}
