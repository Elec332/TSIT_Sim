package nl.elec332.nlda.tsit.sim.main;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import nl.elec332.nlda.tsit.sim.main.radar.TrackedObject;
import nl.elec332.nlda.tsit.sim.util.DialogHelper;
import nl.elec332.nlda.tsit.sim.util.ObjectType;

import javax.swing.*;
import java.awt.*;
import java.util.Set;
import java.util.Vector;

/**
 * Created by Elec332 on 28-1-2020
 */
public class Commander {

    public Commander(Platform platform) {
        this.platform = platform;
        this.clearReq = Sets.newConcurrentHashSet();
        this.editProps = Sets.newConcurrentHashSet();
    }

    private final Platform platform;
    private final Set<TrackedObject> clearReq, editProps;

    public void requestClearance(final TrackedObject object) {
        if (clearReq.add(object)) {
            new Thread(() -> {
                int result = JOptionPane.showConfirmDialog(null, "Permission to shoot down object ID: " + object.getId()
                        + " (" + object.getObjectTypeString() + ")");
                if (result == JOptionPane.YES_OPTION) {
                    object.setFireClearance(true);
                } else if (result == JOptionPane.NO_OPTION) {
                    object.setFireClearance(false);
                }
                clearReq.remove(object);
            }).start();
        }
    }

    public void editProperties(final TrackedObject object) {
        if (clearReq.contains(object)) {
            return;
        }
        if (editProps.add(object)) {
            new Thread(() -> {
                JPanel panel = new JPanel(new BorderLayout());
                JPanel type = new JPanel(new FlowLayout());
                JComboBox<ObjectType> types = new JComboBox<>(new Vector<>(Lists.newArrayList(ObjectType.values())));
                types.setSelectedItem(ObjectType.UNKNOWN);
                if (object.getTypes().size() == 0) {
                    types.setSelectedItem(object.getTypes().get(0));
                }
                types.addActionListener(a -> {
                    object.setObjectType((ObjectType) types.getSelectedItem());
                    object.lockType();
                });
                type.add(types);
                panel.add(type, BorderLayout.CENTER);
                JPanel t = new JPanel();
                JCheckBox perms = new JCheckBox("Permission to fire");
                perms.addActionListener(a -> {
                    object.setFireClearance(perms.isSelected());
                });
                t.add(perms);
                panel.add(t, BorderLayout.SOUTH);
                DialogHelper.askForInput(null, "Edit properties", panel);
                editProps.remove(object);
            }).start();
        }
    }

}
