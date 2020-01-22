package nl.elec332.nlda.tsit.sim.util;

import java.awt.*;

/**
 * Created by Elec332 on 15-1-2020.
 */
public enum ObjectClassification {

    HOSTILE("Hostile", Color.RED),
    POSSIBLE_HOSTILE("Poss. Hostile", Color.ORANGE),
    UNKNOWN("Unknown", Color.GRAY),
    NEUTRAL("Neutral", Color.WHITE),
    POSSIBLE_FRIENDLY("Poss. Friendly", Color.YELLOW),
    FRIENDLY("Friendly", Color.GREEN);

    ObjectClassification(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    private final String name;
    private final Color color;

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

}
