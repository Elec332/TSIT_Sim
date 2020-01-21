package nl.elec332.nlda.tsit.sim.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Simulator {

    public Simulator(String system) {
        try {
            pingSocket = new Socket("localhost", 9800);
            out = new PrintWriter(pingSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(pingSocket.getInputStream()));

            out.println("SYSTEM Base " + system);
            in.readLine();
            in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Socket pingSocket;
    private PrintWriter out;
    private BufferedReader in;

    public String writeLine(String command) {
        out.println(command);
        return readLine();
    }

    public String readLine() {
        try {
            return in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
