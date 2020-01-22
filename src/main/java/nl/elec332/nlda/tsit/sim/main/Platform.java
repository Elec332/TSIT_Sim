package nl.elec332.nlda.tsit.sim.main;

import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;

/**
 * Created by Elec332 on 22-1-2020
 */
public class Platform {

    public Platform() {
        this.classifier = new ContactClassifier(this);
        this.radar = new Radar(this);
        this.fireController = new FireController(this);
        this.guns = Lists.newArrayList();
        this.guns_ = Collections.unmodifiableList(this.guns);
    }

    private final Radar radar;
    private final ContactClassifier classifier;
    private final FireController fireController;
    private final List<Launcher> guns, guns_;

    public Radar getRadar() {
        return radar;
    }

    public ContactClassifier getClassifier() {
        return classifier;
    }

    public FireController getFireController() {
        return fireController;
    }

    public void addGun(Launcher gun) {
        this.guns.add(gun);
    }

    public List<Launcher> getGuns() {
        return guns_;
    }

}
