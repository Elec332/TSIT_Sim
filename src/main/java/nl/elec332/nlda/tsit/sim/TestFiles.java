package nl.elec332.nlda.tsit.sim;

import com.google.common.collect.Lists;
import nl.elec332.nlda.tsit.sim.math.Calculator;
import nl.elec332.nlda.tsit.sim.util.Charts;
import nl.elec332.nlda.tsit.sim.util.RadarMeasurement;
import org.apache.commons.lang3.tuple.Pair;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jzy3d.chart.AWTChart;
import org.jzy3d.chart.Chart;
import org.jzy3d.chart.ChartLauncher;
import org.jzy3d.colors.Color;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.LineStrip;
import org.jzy3d.plot3d.primitives.Point;
import org.jzy3d.plot3d.rendering.canvas.Quality;

import java.io.*;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * Created by Elec332 on 8-1-2020.
 */
public class TestFiles {

    public static void main(String... args) throws IOException {
        String[] files = {"missile", "heli", "fighter", "airliner"};

        process(Arrays.stream(files).map(name -> {
            try {
                return Pair.of(name, openFile(new File("input/" + name + ".txt")));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList()));
    }

    private static List<RadarMeasurement> openFile(File txt) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(txt)));
        List<RadarMeasurement> ret = Lists.newArrayList();
        reader.lines().forEach(line -> {
            String[] split = line.split(" ");
            if (split.length != 4) {
                throw new RuntimeException();
            }
            ret.add(new RadarMeasurement(Double.parseDouble(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3])));
        });
        return ret;
    }

    private static void process(List<Pair<String, List<RadarMeasurement>>> measurements_) {
        drawLines(measurements_.stream().map(measurements -> (BiConsumer<Chart, XYSeriesCollection>) (chart, collection) -> {
            XYSeries data = new XYSeries(measurements.getLeft());
            measurements.getRight().stream()
                    .map(Calculator::calculatePosition)
                    .forEach(pos -> data.add(pos.x, pos.y));
            collection.addSeries(data);

            List<Coord3d> controlCoords = measurements.getRight().stream().map(Calculator::calculatePosition).map(pos -> new Coord3d(pos.x, pos.y, pos.z)).collect(Collectors.toList());
            List<org.jzy3d.plot3d.primitives.Point> controlPoints = new ArrayList<>();
            for (Coord3d coord : controlCoords) {
                controlPoints.add(new org.jzy3d.plot3d.primitives.Point(coord, Color.RED, 5.0f));
            }

            LineStrip line = new LineStrip(controlCoords);
            line.setWireframeColor(Color.BLACK);
            line.setWireframeDisplayed(false);
            line.setFaceDisplayed(false);
            chart.add(line);
            chart.add(controlPoints);
        }).collect(Collectors.toList()));
    }

    private static void drawLines(Collection<BiConsumer<Chart, XYSeriesCollection>> run) {
        Chart chart = new AWTChart(Quality.Intermediate);
        XYSeriesCollection coll = new XYSeriesCollection();
        run.forEach(c -> c.accept(chart, coll));
        JFreeChart chart_ = ChartFactory.createXYLineChart("Test", "x", "y", coll);
        Charts.showChart(chart_);

        double d = 0;
        for (Coord3d c : chart.getScene().getGraph().getAll().stream()
                .map(o -> o instanceof LineStrip ? (LineStrip) o : null)
                .filter(Objects::nonNull)
                .map(LineStrip::getPoints)
                .flatMap(Collection::stream)
                .map(Point::getCoord)
                .collect(Collectors.toList())) {
            if (d < c.x) {
                d = c.x;
            }
            if (d < c.y) {
                d = c.y;
            }
        }

        chart.add(new Point(new Coord3d(0, 0, 0), Color.GREEN, 5f));
        chart.add(new Point(new Coord3d(0, 1000, 0), Color.GREEN, 5f));
        chart.add(new Point(new Coord3d(d, d, 0), Color.WHITE, 0f));
        chart.getView().setViewPoint(new Coord3d(-7 * Math.PI / 12, Math.PI / 9, Math.PI / 3));

        ChartLauncher.openChart(chart);
    }

}
