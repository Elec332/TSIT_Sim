package nl.elec332.nlda.tsit.sim.util;

import java.awt.*;

/**
 * Created by Elec332 on 15-1-2020.
 */
public enum ObjectClassification {

    HOSTILE("Hostile", Color.RED, true),
    POSSIBLE_HOSTILE("Poss. Hostile", Color.ORANGE, true),
    UNKNOWN("Unknown", Color.GRAY, true),
    NEUTRAL("Neutral", Color.WHITE, true),
    POSSIBLE_FRIENDLY("Poss. Friendly", Color.YELLOW, true),
    FRIENDLY("Friendly", Color.GREEN, false),
    HIT("Hit", Color.BLACK, false),
    DOWN("Down", Color.BLACK, false);

    ObjectClassification(String name, Color color, boolean changeable) {
        this.name = name;
        this.color = color;
        this.changeable = changeable;
    }

    private final String name;
    private final Color color;
    private final boolean changeable;

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

    public boolean getChangeable() {
        return changeable;
    }
}
