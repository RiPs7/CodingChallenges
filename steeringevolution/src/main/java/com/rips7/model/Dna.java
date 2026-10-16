package com.rips7.model;

import com.rips7.sketchkit.maths.Maths;
import com.rips7.sketchkit.maths.Vector2D;
import com.rips7.sketchkit.render.Sketch;

import java.awt.Color;

public record Dna(double perception, double maxHealth, double foodSteer, double poisonSteer, double reproductionChance) {

    private static final double MIN_PERCEPTION = 10;
    private static final double MAX_PERCEPTION = 100;
    private static final double PERCEPTION_MUTATION_FACTOR = 10;

    private static final double MIN_MAX_HEALTH = 1;
    private static final double MAX_MAX_HEALTH = 2;
    private static final double MAX_HEALTH_MUTATION_FACTOR = 2;

    private static final double MIN_FOOD_STEER = -1;
    private static final double MAX_FOOD_STEER = 1;
    private static final double FOOD_STEER_MUTATION_FACTOR = 1;

    private static final double MIN_POISON_STEER = -1;
    private static final double MAX_POISON_STEER = 1;
    private static final double POISON_STEER_MUTATION_FACTOR = 1;

    private static final double MIN_REPRO_CHANCE = 0.001;
    private static final double MAX_REPRO_CHANCE = 0.005;
    private static final double REPRO_CHANCE_MUTATION_FACTOR = 1;

    private static final double MUTATION_RATE = 0.05;

    public Dna() {
        this(
                Maths.random(MIN_PERCEPTION, MAX_PERCEPTION),
                Maths.random(MIN_MAX_HEALTH, MAX_MAX_HEALTH),
                Maths.random(MIN_FOOD_STEER, MAX_FOOD_STEER),
                Maths.random(MIN_POISON_STEER, MAX_POISON_STEER),
                Maths.random(MIN_REPRO_CHANCE, MAX_REPRO_CHANCE));
    }

    public static Dna empty() {
        return new Dna(0, 0, 0, 0, 0);
    }

    public Dna copy() {
        return new Dna(
                mutate(perception, PERCEPTION_MUTATION_FACTOR),
                mutate(maxHealth, MAX_HEALTH_MUTATION_FACTOR),
                mutate(foodSteer, FOOD_STEER_MUTATION_FACTOR),
                mutate(poisonSteer, POISON_STEER_MUTATION_FACTOR),
                mutate(reproductionChance, REPRO_CHANCE_MUTATION_FACTOR));
    }

    public void show(final Vector2D pos, final double angle, final Sketch s, final boolean debug) {
        if (!debug) {
            return;
        }
        s.pushMatrix();
        s.translate(pos.x(), pos.y());
        s.noFill();
        s.stroke(Color.BLUE);
        s.circle(0, 0, perception * 2);
        s.rotate(angle);
        s.stroke(Color.GREEN);
        s.line(0, 0, foodSteer * 50, 0);
        s.stroke(Color.RED);
        s.line(0, 0, poisonSteer * 50, 0);
        s.popMatrix();
    }

    private double mutate(final double value, final double factor) {
        if (Maths.random(1) > MUTATION_RATE) return value;
        return value + Maths.random(-0.1 * factor, 0.1 * factor);
    }

}
