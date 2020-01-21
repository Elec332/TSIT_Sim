package nl.elec332.nlda.tsit.sim.gui.swing;

import nl.elec332.nlda.tsit.sim.simulation.TrackedObject;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;
import java.util.function.Supplier;

/**
 * Created by Elec332 on 21-1-2020
 */
public class J2DRadarComponent extends JComponent {

    public J2DRadarComponent(Supplier<Collection<TrackedObject>> objectTracker) {
        this.objectTracker = objectTracker;
    }

    private final Supplier<Collection<TrackedObject>> objectTracker;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        //Todo: implement
    }

}
