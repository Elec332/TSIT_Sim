package nl.elec332.nlda.tsit.sim;

import nl.elec332.nlda.tsit.sim.gui.Gui2DRadarPlot;
import nl.elec332.nlda.tsit.sim.gui.Gui3DView;
import nl.elec332.nlda.tsit.sim.main.Radar;
import nl.elec332.nlda.tsit.sim.math.Calculator;
import nl.elec332.nlda.tsit.sim.util.FileHelper;
import nl.elec332.nlda.tsit.sim.util.Simulator;

public class Main {

    public static void main(String[] args) {
        simRadar = new Simulator("2");
        simWeapon = new Simulator("3");
        Simulator gpsSim = new Simulator("1");

        Radar radar = new Radar(Calculator.calculatePosition(FileHelper.parseMeasurement(gpsSim.readLine())));

        radar.addView(Gui3DView::new);
        radar.addView(Gui2DRadarPlot::new);

        String data;
        while ((data = simRadar.readLine()) != null) {
            radar.receiveMeasurement(FileHelper.parseMeasurement(data));
        }

    }

    private static Simulator simRadar;
    private static Simulator simWeapon;

}
