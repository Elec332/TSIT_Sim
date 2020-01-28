package nl.elec332.nlda.tsit.sim.simulation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class AbstractSimulationSystem {

    public AbstractSimulationSystem(String host, int port, int system) throws IOException {

        Socket pingSocket = new Socket(host, port);
        out = new PrintWriter(pingSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(pingSocket.getInputStream()));

        out.println("SYSTEM Base " + system);
        System.out.println("Starting object " + system + " : " + in.readLine() + " " + in.readLine());
    }

    protected PrintWriter out;
    protected BufferedReader in;

}
