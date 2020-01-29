package nl.elec332.nlda.tsit.sim.main;

import com.google.common.collect.Lists;
import nl.elec332.nlda.tsit.sim.api.radar.IRadarView;
import nl.elec332.nlda.tsit.sim.main.radar.ObjectTracker;
import nl.elec332.nlda.tsit.sim.main.radar.TrackedObject;
import nl.elec332.nlda.tsit.sim.util.RadarMeasurement;

import java.util.Collection;
import java.util.function.BiFunction;
import java.util.function.Supplier;

/**
 * Created by Elec332 on 21-1-2020.
 */
public class Radar {

    Radar(Platform platform) {
        this.platform = platform;
        this.objectTracker = new ObjectTracker();
        this.views = Lists.newArrayList();
    }

    private final Platform platform;
    private final ObjectTracker objectTracker;
    private final Collection<IRadarView> views;

    public void addView(BiFunction<Supplier<Collection<TrackedObject>>, Commander, IRadarView> viewerFactory) {
        IRadarView view = viewerFactory.apply(this.objectTracker::getFilteredObjects, this.platform.getCommander());
        view.show();
        views.add(view);
    }

    public void addSimulatedRadar(Supplier<RadarMeasurement> measurementSupplier) {
        new Thread(() -> {
            RadarMeasurement m;
            while ((m = measurementSupplier.get()) != null) {
                receiveMeasurement(m);
            }
        }).start();
    }

    public synchronized void receiveMeasurement(RadarMeasurement measurement) {
        TrackedObject o = this.objectTracker.receiveMeasurement(measurement);
        this.platform.getClassifier().updateObject(o);
        this.platform.getFireController().updateObject(o);
        System.out.println(o.getId());
        System.out.println(o.getCurrentPosition());
        updateRadarView();
    }

    public void updateRadarView() {
        views.forEach(IRadarView::updateTracks);
    }

}
