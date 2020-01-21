package nl.elec332.nlda.tsit.sim.util;

import com.google.common.collect.Lists;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Elec332 on 21-1-2020.
 */
public class FileHelper {

    public static List<RadarMeasurement> openFile(File txt) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(txt)));
        return reader.lines().map(FileHelper::parseMeasurement).collect(Collectors.toList());
    }

    public static RadarMeasurement parseMeasurement(String line) {
        String[] split = line.split(" ");
        if (split.length != 4) {
            throw new RuntimeException();
        }
        return new RadarMeasurement(Double.parseDouble(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]));
    }

}
