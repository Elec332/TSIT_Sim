package nl.elec332.nlda.tsit.sim.simulation;

import com.google.common.collect.Lists;

import java.io.IOException;
import java.net.ConnectException;
import java.util.List;

public class Simulator {

    public Simulator(String host, int port) throws IOException {
        for (int i = 3; i < 7; i++) {
            launchers.add(new SimulatedGun(host, port, i));
        }
        radar = new SimulatedRadar(host, port);
    }

    public List<SimulatedGun> launchers = Lists.newArrayList();
    public SimulatedRadar radar;

}
