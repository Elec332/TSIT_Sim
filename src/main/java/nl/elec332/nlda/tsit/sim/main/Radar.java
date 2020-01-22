package nl.elec332.nlda.tsit.sim.main;

import com.google.common.collect.Lists;
import nl.elec332.nlda.tsit.sim.api.radar.IRadarView;
import nl.elec332.nlda.tsit.sim.gui.Gui2DRadarPlot;
import nl.elec332.nlda.tsit.sim.gui.Gui3DView;
import nl.elec332.nlda.tsit.sim.math.Calculator;
import nl.elec332.nlda.tsit.sim.simulation.ObjectTracker;
import nl.elec332.nlda.tsit.sim.simulation.TrackedObject;
import nl.elec332.nlda.tsit.sim.util.FileHelper;
import nl.elec332.nlda.tsit.sim.util.RadarMeasurement;
import nl.elec332.nlda.tsit.sim.util.Simulator;

import javax.vecmath.Vector3d;
import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by Elec332 on 21-1-2020.
 */
public class Radar {

    public Radar() {
        this.objectTracker = new ObjectTracker();
        this.views = Lists.newArrayList();
        Simulator gpsSim = new Simulator("1");
        this.location = Calculator.calculatePosition(FileHelper.parseMeasurement(gpsSim.readLine()));
    }

    public void addView(Function<Supplier<Collection<TrackedObject>>, IRadarView> viewerFactory) {
        IRadarView view = viewerFactory.apply(this.objectTracker::getTrackedObjects);
        view.show();
        views.add(view);
    }

    private final ObjectTracker objectTracker;
    private final Collection<IRadarView> views;
    private final Vector3d location;

    public void receiveMeasurement(RadarMeasurement measurement) {
        this.objectTracker.receiveMeasurement(measurement);
        updateRadarView();
    }

    public void updateRadarView() {
        views.forEach(IRadarView::updateTracks);
    }

}
