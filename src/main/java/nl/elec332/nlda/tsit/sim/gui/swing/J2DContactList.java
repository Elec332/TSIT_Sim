package nl.elec332.nlda.tsit.sim.gui.swing;

import nl.elec332.nlda.tsit.sim.main.radar.TrackedObject;
import nl.elec332.nlda.tsit.sim.util.Constants;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Supplier;

public class J2DContactList extends JList {

    public J2DContactList(Supplier<Collection<TrackedObject>> objectTracker) {
        super();
        this.objectTracker = objectTracker;
    }

    private final Supplier<Collection<TrackedObject>> objectTracker;
    private DefaultListModel list = new DefaultListModel();

    @Override
    protected void paintComponent(Graphics g) {
        list.clear();
        for (TrackedObject object : objectTracker.get()) {
            String info = "<html>";
            info += ("ID: " + object.getId());
            info += ("<br />" + object.getObjectClassification().getName());
            info += ("<br />ALT:  " + Math.round(object.getCurrentPosition().z * 10)/10 + "m");
            info += ("<br />SPD:  " + Math.round(object.getCurrentSpeed().length() * 10)/10 + "m/s");
            info += ("<br />DIST: " + Math.round(object.getCurrentPosition().length() * 10)/10 + "m");
            info += "</html>";

            list.addElement(info);
        }
        super.setModel(list);

        super.paintComponent(g);
    }
}
