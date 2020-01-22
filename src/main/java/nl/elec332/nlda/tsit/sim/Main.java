package nl.elec332.nlda.tsit.sim;

import nl.elec332.nlda.tsit.sim.gui.Gui2DRadarPlot;
import nl.elec332.nlda.tsit.sim.gui.Gui3DView;
import nl.elec332.nlda.tsit.sim.main.Radar;
import nl.elec332.nlda.tsit.sim.math.Calculator;
import nl.elec332.nlda.tsit.sim.simulation.Simulator;
import nl.elec332.nlda.tsit.sim.util.FileHelper;
import nl.elec332.nlda.tsit.sim.util.RadarMeasurement;

public class Main {

    public static void main(String[] args) {


        Radar radar = new Radar();

        radar.addView(Gui3DView::new);
        radar.addView(Gui2DRadarPlot::new);

        RadarMeasurement data;
        while ((data = simulator.radar.getMeasurement()) != null) {
            radar.receiveMeasurement(data);
        }

    }

    private static Simulator simulator;


}
