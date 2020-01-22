package nl.elec332.nlda.tsit.sim.simulation;

import java.io.IOException;

public class Gun extends SimObject {

    public Gun(String host, int port, int system) throws IOException {
        super(host, port, system);
    }

    public void Aim(int bearing, int elevation) {
        out.println("AIM " + bearing + " " + elevation);
    }

    public boolean Fire() throws IOException {
        out.println("FIRE");
        return in.readLine() == "FIRED";
    }

}
