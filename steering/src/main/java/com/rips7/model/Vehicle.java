package com.rips7.model;

import com.rips7.sketchkit.maths.Maths;
import com.rips7.sketchkit.maths.Vector2D;
import com.rips7.sketchkit.render.Sketch;

import java.util.stream.Stream;

public class Vehicle {
    private static final int PERCEPTION_RANGE = 100;
    private static final int SIZE = 4;
    private static final double MAX_SPEED = 10;
    private static final double MAX_FORCE = 1;
    private static final double ARRIVE_WEIGHT = 1;
    private static final double FLEE_FACTOR = 5;

    private final float hue;
    private final Vector2D target;

    private Vector2D position;
    private Vector2D velocity;
    private Vector2D acceleration;

    public Vehicle(final Vector2D position, final Vector2D target) {
        this.hue = (float) Maths.random(0, 1);
        this.target = target;
        this.position = position;
        this.velocity = Vector2D.random2D();
        this.acceleration = Vector2D.ZERO;
    }

    public void update(final int mouseX, final int mouseY) {
        behaviours(Vector2D.of(mouseX, mouseY));

        position = position.add(velocity);
        velocity = velocity.add(acceleration);
        acceleration = acceleration.mult(0);
    }

    public void show(final Sketch s) {
        s.noStroke();
        s.fill(s.hsbToRgb(hue, 1.0f, 1.0f));
        s.circle(position.x(), position.y(), SIZE);
    }

    private void behaviours(final Vector2D mouse) {
        final Vector2D arrive = arrive(target).mult(ARRIVE_WEIGHT);
        final Vector2D flee = flee(mouse).mult(FLEE_FACTOR);
        Stream.of(arrive, flee).forEach(this::applyForce);
    }

    private Vector2D arrive(final Vector2D target) {
        final Vector2D desired = target.sub(position);
        final double dist = desired.mag();
        final double speed = dist < PERCEPTION_RANGE ? Maths.map(dist, 0, 100, 0, MAX_SPEED) : MAX_SPEED;
        final Vector2D adjustedDesired = desired.setMag(speed);
        return adjustedDesired.sub(velocity).limit(MAX_FORCE);
    }

    private Vector2D flee(final Vector2D target) {
        final Vector2D desired = target.sub(position);
        final double dist = desired.mag();
        if (dist > PERCEPTION_RANGE) {
            return Vector2D.ZERO;
        }
        return desired.setMag(MAX_SPEED).mult(-1).sub(velocity).limit(MAX_FORCE);
    }

    private void applyForce(final Vector2D force) {
        acceleration = acceleration.add(force);
    }
}
