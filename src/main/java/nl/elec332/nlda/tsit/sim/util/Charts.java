package nl.elec332.nlda.tsit.sim.util;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import javax.swing.*;

/**
 * Created by Elec332 on 13-1-2020
 */
public class Charts {

    public static void showChart(final JFreeChart chart) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame(chart.getTitle().getText());
            ChartPanel pan = new ChartPanel(chart);
            frame.setContentPane(pan);
            frame.setAlwaysOnTop(true);
            frame.pack();
            frame.setSize(600, 400);
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });

    }

}
