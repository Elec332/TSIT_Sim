package nl.elec332.nlda.tsit.sim;

import nl.elec332.nlda.tsit.sim.gui.Gui2DRadarPlot;
import nl.elec332.nlda.tsit.sim.gui.Gui3DView;
import nl.elec332.nlda.tsit.sim.main.Platform;
import nl.elec332.nlda.tsit.sim.main.Radar;
import nl.elec332.nlda.tsit.sim.util.FileHelper;
import nl.elec332.nlda.tsit.sim.util.RadarMeasurement;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Elec332 on 21-1-2020.
 */
public class MultiFileTest {

    public static void main(String[] args) throws Exception {
        String[] files = {"missile", "heli", "fighter", "airliner"};

        List<RadarMeasurement> measurements = Arrays.stream(files).map(name -> {
            try {
                return FileHelper.openFile(new File("input/" + name + ".txt"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        })
                .flatMap(Collection::stream)
                .sorted(Comparator.comparing(RadarMeasurement::getTime))
                .collect(Collectors.toList());

        Platform platform = new Platform();
        Radar radar = platform.getRadar();
        radar.addView(Gui3DView::new);
        radar.addView(Gui2DRadarPlot::new);
        for (RadarMeasurement measurement : measurements) {
            radar.receiveMeasurement(measurement);
            Thread.sleep(50);
        }
    }

}
