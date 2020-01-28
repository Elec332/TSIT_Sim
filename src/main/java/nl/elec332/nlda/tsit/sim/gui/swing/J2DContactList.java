package nl.elec332.nlda.tsit.sim.gui.swing;

import nl.elec332.nlda.tsit.sim.main.radar.TrackedObject;
import nl.elec332.nlda.tsit.sim.util.Constants;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;
import java.util.function.Supplier;

public class J2DContactList extends JList<String> {

    public J2DContactList(Supplier<Collection<TrackedObject>> objectTracker) {
        super();
        this.objectTracker = objectTracker;
        this.list = new DefaultListModel<>();
    }

    private final Supplier<Collection<TrackedObject>> objectTracker;
    private final DefaultListModel<String> list;

    @Override
    protected void paintComponent(Graphics g) {
        list.clear();
        for (TrackedObject object : objectTracker.get()) {
            String info = "<html>";
            info += ("ID: " + object.getId());
            info += ("<br />" + object.getObjectClassification().getName());

            info += ("<br />TYPE: " + object.getObjectTypeString());
            info += ("<br />ALT:  " + Constants.TWO_DIGITS_FORMAT.format(object.getCurrentPosition().z) + "m");
            info += ("<br />SPD:  " + Constants.TWO_DIGITS_FORMAT.format(object.getCurrentSpeed().length()) + "m/s");
            info += ("<br />DIST: " + Constants.TWO_DIGITS_FORMAT.format(object.getCurrentPosition().length()) + "m");
            info += "</html>";

            list.addElement(info);
        }
        super.setModel(list);
        super.paintComponent(g);
    }

}
