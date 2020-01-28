package nl.elec332.nlda.tsit.sim.util;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Elec332 on 28-1-2020
 */
@SuppressWarnings("all")
public class DialogHelper {

    public static <T> T askForInput(Component parent, String title, T message) {
        return JOptionPane.showOptionDialog(parent, message, title, -1, -1, (Icon) null, (Object[]) null, (Object) null) == -1 ? null : message;
    }

}
