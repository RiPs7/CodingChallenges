package com.rips7.model;

import com.rips7.sketchkit.maths.Maths;
import com.rips7.sketchkit.maths.Vector2D;
import com.rips7.sketchkit.render.Sketch;

import java.awt.Color;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class Vehicle {
    private static final int SIZE = 16;
    private static final double MAX_SPEED = 4;
    private static final double MAX_FORCE = 1;
    private static final double NUTRITION = 0.3;
    private static final double DAMAGE = 0.5;

    private final Dna dna;

    private Vector2D position;
    private Vector2D velocity;
    private Vector2D acceleration;

    private double health;
    private double age;

    public Vehicle(final double x, final double y) {
        this(x, y, new Dna());
    }

    public Vehicle(final double x, final double y, final Dna dna) {
        this.dna = dna;

        this.position = Vector2D.of(x, y);
        this.velocity = Vector2D.random2D();
        this.acceleration = Vector2D.ZERO;

        this.health = 1;
        this.age = 0;
    }

    public boolean dead() {
        return health <= 0;
    }

    public long age() {
        return (long) age;
    }

    public Dna dna() {
        return dna;
    }

    public Optional<Vehicle> reproduce() {
        if (Maths.random(1) < dna.reproductionChance()) {
            return Optional.of(new Vehicle(position.x(), position.y(), dna.copy()));
        }
        return Optional.empty();
    }

    public void behaviors(final double width, final double height, final List<Vector2D> foods, final List<Vector2D> poisons) {
        eat(foods, dna.foodSteer(), () -> health = Maths.constrain(health + NUTRITION, 0, dna.maxHealth()));
        eat(poisons, dna.poisonSteer(), () -> health = Maths.constrain(health - DAMAGE, 0, dna.maxHealth()));
        boundaries(width, height);
    }

    public void update(final double deltaTime) {
        health -= 0.005;
        age += deltaTime;
        velocity = velocity.add(acceleration).limit(MAX_SPEED);
        position = position.add(velocity);
        acceleration = acceleration.mult(0);
    }

    public void show(final Sketch s, final boolean debug) {
        s.noStroke();
        s.fill(lerpColor(Color.RED, Color.GREEN, health));
        s.triangle(position.x(), position.y(), SIZE, SIZE * 2, velocity.heading());
        dna.show(position, velocity.heading(), s, debug);
    }

    private void eat(final List<Vector2D> targets, final double forceWeight, final Runnable onEat) {
        targets.stream()
                .filter(target -> target.dist(position) <= dna.perception())
                .min(Comparator.comparingDouble(target -> target.distSq(position)))
                .ifPresent(target -> {
                    if (target.dist(position) < 5) {
                        targets.remove(target);
                        onEat.run();
                    } else {
                        seek(target, forceWeight);
                    }
                });
    }

    private void seek(final Vector2D target, final double forceWeight) {
        final Vector2D force = target.sub(position).setMag(MAX_SPEED).sub(velocity).limit(MAX_FORCE);
        applyForce(force.mult(forceWeight));
    }

    private void boundaries(final double width, final double height) {
        final Vector2D desired;
        if (position.x() < dna.perception()) desired = Vector2D.of(MAX_SPEED, velocity.y());
        else if (position.x() > width - dna.perception()) desired = Vector2D.of(-MAX_SPEED, velocity.y());
        else if (position.y() < dna.perception()) desired = Vector2D.of(velocity.x(), MAX_SPEED);
        else if (position.y() > height - dna.perception()) desired = Vector2D.of(velocity.x(), -MAX_SPEED);
        else return;
        final Vector2D force = desired.normalize().mult(MAX_SPEED).sub(velocity).limit(MAX_FORCE);
        applyForce(force);
    }

    private void applyForce(final Vector2D force) {
        acceleration = acceleration.add(force);
    }

    @SuppressWarnings("SameParameterValue")
    private static Color lerpColor(Color from, Color to, double t) {
        t = Math.clamp(t, 0.0, 1.0);
        return new Color(
                (int) Maths.lerp(from.getRed(),   to.getRed(),   t),
                (int) Maths.lerp(from.getGreen(), to.getGreen(), t),
                (int) Maths.lerp(from.getBlue(),  to.getBlue(),  t)
        );
    }

}
