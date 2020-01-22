package nl.elec332.nlda.tsit.sim.simulation;

import nl.elec332.nlda.tsit.sim.util.FileHelper;
import nl.elec332.nlda.tsit.sim.util.RadarMeasurement;

import java.io.IOException;

public class AirWarningRadar extends SimObject{

    public AirWarningRadar(String host, int port) throws IOException {
        super(host, port, 2);
    }

    public RadarMeasurement getMeasurement() {
        try {
            return FileHelper.parseMeasurement(in.readLine());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
