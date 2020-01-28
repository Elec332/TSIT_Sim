package nl.elec332.nlda.tsit.sim.gui;

import nl.elec332.nlda.tsit.sim.api.radar.IRadarView;
import nl.elec332.nlda.tsit.sim.gui.swing.J2DContactList;
import nl.elec332.nlda.tsit.sim.gui.swing.J2DRadarComponent;
import nl.elec332.nlda.tsit.sim.main.Commander;
import nl.elec332.nlda.tsit.sim.main.radar.TrackedObject;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;
import java.util.function.Supplier;

/**
 * Created by Elec332 on 21-1-2020
 */
public class Gui2DRadarPlot implements IRadarView {

    public Gui2DRadarPlot(Supplier<Collection<TrackedObject>> objectTracker, Commander commander) {
        this.radarComponent = new J2DRadarComponent(objectTracker, commander);
        this.contactList = new J2DContactList(objectTracker);
        contactList.setLocation(800, 0);
    }

    private final J2DRadarComponent radarComponent;
    private final J2DContactList contactList;

    @Override
    public void show() {
        JFrame frame = new JFrame("Simple plot");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(radarComponent);
        frame.add(contactList);
        frame.setLayout(new GridLayout(1, 2));
        frame.setSize(1200, 600);
        frame.setVisible(true);
    }

    @Override
    public void updateTracks() {
        radarComponent.repaint();
        contactList.repaint();
    }

}
