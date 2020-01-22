package nl.elec332.nlda.tsit.sim.simulation;

import com.google.common.collect.Lists;

import java.io.IOException;
import java.util.List;

public class  Simulator {

    public Simulator(String host, int port) throws IOException {
        for (int i = 3; i < 7; i++) {
            launchers.add(new Gun(host, port, i));
        }
        radar = new AirWarningRadar(host, port);
    }

    public List<Gun> launchers = Lists.newArrayList();
    public AirWarningRadar radar;

}
