package nl.elec332.nlda.tsit.sim.gui;

import com.google.common.collect.Lists;
import nl.elec332.nlda.tsit.sim.api.radar.IRadarView;
import nl.elec332.nlda.tsit.sim.main.radar.TrackedObject;
import org.jzy3d.chart.Chart;
import org.jzy3d.chart.ChartLauncher;
import org.jzy3d.chart.SwingChart;
import org.jzy3d.colors.Color;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.AbstractDrawable;
import org.jzy3d.plot3d.primitives.LineStrip;
import org.jzy3d.plot3d.primitives.Point;
import org.jzy3d.plot3d.rendering.canvas.Quality;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Created by Elec332 on 21-1-2020.
 */
public class Gui3DView implements IRadarView {

    public Gui3DView(Supplier<Collection<TrackedObject>> objectTracker) {
        this.chart = new SwingChart(Quality.Intermediate);
        this.height = 1000;
        this.minRange = 25000;
        this.objectTracker = objectTracker;
    }

    private final Chart chart;
    private final double minRange;
    private final Supplier<Collection<TrackedObject>> objectTracker;
    private final Collection<AbstractDrawable> drawers = Lists.newArrayList();

    private double height;

    @Override
    public void show() {
        new Thread(() -> {
            chart.add(new Point(new Coord3d(minRange, minRange, height), Color.WHITE, 0));
            chart.add(new Point(new Coord3d(-minRange, -minRange, 0), Color.WHITE, 0));
            chart.getView().setViewPoint(new Coord3d(-7 * Math.PI / 12, Math.PI / 9, Math.PI / 3));
            update(false);
            ChartLauncher.openChart(chart);

        }).start();
    }

    private void add(AbstractDrawable drawable) {
        chart.add(drawable, false);
        drawers.add(drawable);
    }

    @Override
    public void updateTracks() {
        if (chart.getScene() != null) {
            update(true);
        }
    }

    private synchronized void update(boolean redraw) {
        drawers.forEach(d -> chart.removeDrawable(d, false));
        drawers.clear();

        //height = objectTracker.get().stream().mapToDouble(obj -> obj.getCurrentPosition().z).max().orElse(height);


        for (TrackedObject trackedObject : objectTracker.get()) {
            if (trackedObject.getCurrentPosition().length() > minRange) {
                continue;
            }
            List<Coord3d> controlCoords = trackedObject.getLocations().stream().map(pos -> new Coord3d(pos.x, pos.y, pos.z)).collect(Collectors.toList());
            List<Point> controlPoints = new ArrayList<>();
            java.awt.Color realColor = trackedObject.getColor();
            Color color = new Color(realColor.getRed(), realColor.getGreen(), realColor.getBlue(), 255);
            for (Coord3d coord : controlCoords) {
                controlPoints.add(new Point(coord, color, 5.0f));
            }
            LineStrip line = new LineStrip(controlCoords);
            line.setWireframeColor(color);
            add(line);
            controlPoints.forEach(this::add);
        }

//        double max = 0, may = 0, mix = 1000000, miy = 1000000;
//        for (Coord3d c : chart.getScene().getGraph().getAll().stream()
//                .map(o -> o instanceof LineStrip ? (LineStrip) o : null)
//                .filter(Objects::nonNull)
//                .map(LineStrip::getPoints)
//                .flatMap(Collection::stream)
//                .map(Point::getCoord)
//                .collect(Collectors.toList())) {
//            if (max < c.x) {
//                max = c.x;
//            }
//            if (may < c.y) {
//                may = c.y;
//            }
//            if (c.x > mix) {
//                mix = c.x;
//            }
//            if (c.y > miy) {
//                miy = c.y;
//            }
//        }

        add(new Point(new Coord3d(5000, 5000, height), Color.WHITE, 5f));
        add(new Point(new Coord3d(-5000, -5000, 0), Color.WHITE, 5f));
        add(new Point(new Coord3d(0, 100, 0), Color.GREEN, 5f));
        add(new Point(new Coord3d(0, 0, 0), Color.GREEN, 5f));
        if (redraw) {
            chart.render();
        }
    }

}
