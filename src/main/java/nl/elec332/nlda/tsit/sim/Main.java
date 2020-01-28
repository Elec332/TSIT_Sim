package nl.elec332.nlda.tsit.sim;

import nl.elec332.nlda.tsit.sim.gui.Gui2DRadarPlot;
import nl.elec332.nlda.tsit.sim.gui.Gui3DView;
import nl.elec332.nlda.tsit.sim.main.Launcher;
import nl.elec332.nlda.tsit.sim.main.Platform;
import nl.elec332.nlda.tsit.sim.main.Radar;
import nl.elec332.nlda.tsit.sim.simulation.Simulator;
import nl.elec332.nlda.tsit.sim.util.Constants;

import javax.vecmath.Vector3d;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        Simulator simulator;
        try {
            simulator = new Simulator("localhost", 9800);
        } catch (IOException e) {
            throw new RuntimeException("Failed to connect to server...", e);
        }

        Platform platform = new Platform();
        Radar radar = platform.getRadar();
        radar.addView((obj, com) -> new Gui3DView(obj));
        radar.addView(Gui2DRadarPlot::new);

        radar.addSimulatedRadar(simulator.radar::getMeasurement);
        simulator.launchers.forEach(g -> platform.addGun(new Launcher(g, new Vector3d(0, 0, 0), Constants.PROJECTILE_SPEED)));
    }

}
