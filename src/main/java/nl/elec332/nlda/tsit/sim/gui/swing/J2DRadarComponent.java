package nl.elec332.nlda.tsit.sim.gui.swing;

import com.google.common.collect.Lists;
import nl.elec332.nlda.tsit.sim.main.Commander;
import nl.elec332.nlda.tsit.sim.main.radar.TrackedObject;
import nl.elec332.nlda.tsit.sim.util.Constants;

import javax.swing.*;
import javax.vecmath.Vector3d;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

/**
 * Created by Elec332 on 21-1-2020
 */
public class J2DRadarComponent extends JComponent {

    public J2DRadarComponent(Supplier<Collection<TrackedObject>> objectTracker, Commander commander) {
        this.objectTracker = objectTracker;
        setMinimumSize(new Dimension(600, 600));
        setMaximumSize(new Dimension(600, 600));
        setSize(new Dimension(600, 600));
        setBackground(Color.BLACK);
        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {//Right mouse
                    for (TrackedObject object : objectTracker.get()) {
                        Vector3d pos = object.getCurrentPosition();
                        int x = getPositionX(pos.x);
                        int y = getPositionY(pos.y);
                        if (new Rectangle(x, y - 2 * textHeight, boxWidth, 2 * textHeight).contains(e.getPoint())) {
                            commander.editProperties(object);
                            return;
                        }
                    }
                }
            }

        });
    }

    private final Supplier<Collection<TrackedObject>> objectTracker;
    private static final int range = 50 * Constants.ONE_KILOMETER;
    private static final int circle = 6, boxWidth = 40, textHeight = 10;
    private double scaleX, scaleY, offsetX, offsetY;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(getBackground());
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.GREEN);
        double width = getWidth();
        double height = getHeight();

        scaleX = width / (range * 2);
        offsetX = width / 2;
        scaleY = height / (range * 2);
        offsetY = height / 2;

        int radX_ = (int) (Constants.DEFAULT_ENEMY_RANGE * scaleX);
        int radY_ = (int) (Constants.DEFAULT_ENEMY_RANGE * scaleY);

        for (int i = 1; i < 5; i++) {
            int radX = radX_ * i;
            int radY = radY_ * i;
            g.drawOval(getPositionX(0) - radX, getPositionY(0) - radY, radX * 2, radY * 2);
        }

        g.drawLine((int) offsetX, 0, (int) offsetX, (int) height);
        g.drawLine(0, (int) offsetY, (int) width, (int) offsetY);

        objectTracker.get().forEach(o -> drawObject(g, o));
        g.setColor(Color.WHITE);
        g.drawString("Objects: " + objectTracker.get().size(), 2, (int) (height - 2 - textHeight));
    }

    private void drawObject(Graphics g, TrackedObject object) {
        g.setColor(object.getColor());
        Vector3d pos = object.getCurrentPosition();
        int x = getPositionX(pos.x);
        int y = getPositionY(pos.y);
        pos = null;
        for (Vector3d oldPos : object.getLocations()) {
            if (pos == null) {
                pos = oldPos;
                continue;
            }
            g.drawLine(getPositionX(pos.x), getPositionY(pos.y), getPositionX(oldPos.x), getPositionY(oldPos.y));
            pos = oldPos;
        }
        g.fillOval(x - circle / 2, y - circle / 2, circle, circle);

        List<String> info = Lists.newArrayList();
        info.add("ID: " + object.getId());

        int boxHeight = textHeight * info.size() + 5;

        int xStart = x + circle;
        int yStart = y - circle - boxHeight;
        g.drawRect(xStart, yStart, boxWidth, boxHeight);
        xStart += 2;
        yStart += 2;

        for (int i = 1; i <= info.size(); i++) {
            g.drawString(info.get(i - 1), xStart, yStart + textHeight * i);
        }
    }

    private int getPositionX(double pos) {
        return (int) (offsetX + pos * scaleX);
    }

    private int getPositionY(double pos) {
        return (int) (offsetY - pos * scaleY);
    }

}
