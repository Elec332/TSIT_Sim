package nl.elec332.nlda.tsit.sim.main;

import nl.elec332.nlda.tsit.sim.gui.Gui3DView;
import nl.elec332.nlda.tsit.sim.simulation.ObjectTracker;
import nl.elec332.nlda.tsit.sim.util.RadarMeasurement;

/**
 * Created by Elec332 on 21-1-2020.
 */
public class Radar {

    public Radar() {
        this.objectTracker = new ObjectTracker();
        this.view = new Gui3DView(this.objectTracker::getTrackedObjects);
        this.view.show();
    }

    private final ObjectTracker objectTracker;
    private final Gui3DView view;

    public void receiveMeasurement(RadarMeasurement measurement) {
        this.objectTracker.receiveMeasurement(measurement);
        updateRadarView();
    }

    public void updateRadarView() {
        view.update();
    }

}
