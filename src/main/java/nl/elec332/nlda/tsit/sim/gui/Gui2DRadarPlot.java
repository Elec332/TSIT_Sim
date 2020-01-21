package nl.elec332.nlda.tsit.sim.gui;

import nl.elec332.nlda.tsit.sim.api.radar.IRadarView;
import nl.elec332.nlda.tsit.sim.gui.swing.J2DRadarComponent;
import nl.elec332.nlda.tsit.sim.simulation.TrackedObject;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;
import java.util.function.Supplier;

/**
 * Created by Elec332 on 21-1-2020
 */
public class Gui2DRadarPlot implements IRadarView {

    public Gui2DRadarPlot(Supplier<Collection<TrackedObject>> objectTracker) {
        this.radarComponent = new J2DRadarComponent(objectTracker);
    }

    private final J2DRadarComponent radarComponent;

    @Override
    public void show() {
        JFrame frame = new JFrame("Simple plot");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(800, 600));
        frame.add(radarComponent);
        frame.setVisible(true);
    }

    @Override
    public void updateTracks() {
        radarComponent.repaint();
    }

}
