package nl.elec332.nlda.tsit.sim.simulation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SimObject {

    public SimObject(String host, int port, int system) throws IOException {
        try {
            Socket pingSocket = new Socket(host, port);
            out = new PrintWriter(pingSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(pingSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        out.println("SYSTEM Base " + system);
        in.readLine();
    }

    protected PrintWriter out;
    protected BufferedReader in;

}
