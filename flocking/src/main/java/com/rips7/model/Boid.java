package com.rips7.model;

import com.rips7.sketchkit.maths.Maths;
import com.rips7.sketchkit.maths.Vector2D;
import com.rips7.sketchkit.render.Sketch;

import java.util.List;
import java.util.stream.Stream;

public class Boid {
    private final double perceptionRadius;
    private final double maxForce;
    private final double maxDesiredSpeed;
    private final double maxSpeed;

    private Vector2D position;
    private Vector2D velocity;
    private Vector2D acceleration;

    public Boid(final double x, final double y) {
        this.position = Vector2D.of(x, y);
        this.velocity = Vector2D.random2D().setMag(Maths.random(1, 10));
        this.acceleration = Vector2D.ZERO;

        this.perceptionRadius = 500;
        this.maxForce = 1;
        this.maxDesiredSpeed = 5;
        this.maxSpeed = 10;
    }

    public Vector2D align(final List<Boid> boids) {
        final Vector2D desired = Vector2D.average(boids.stream().map(b -> b.velocity).toList());
        return desired.equals(Vector2D.ZERO) ? Vector2D.ZERO : steer(desired);
    }

    public Vector2D cohesion(final List<Boid> boids) {
        final Vector2D desired = Vector2D.average(boids.stream().map(b -> b.position).toList());
        return desired.equals(Vector2D.ZERO) ? Vector2D.ZERO : steer(desired.sub(position));
    }

    public Vector2D separation(final List<Boid> boids) {
        final Vector2D desired = Vector2D.average(boids.stream()
                .map(b -> position.sub(b.position).div(Vector2D.distanceSq(position, b.position)))
                .toList());
        return desired.equals(Vector2D.ZERO) ? Vector2D.ZERO : steer(desired);
    }

    public void flock(final List<Boid> boids) {
        final List<Boid> localBoids = boids.stream()
                .filter(b -> b != this && Vector2D.distance(b.position, position) <= perceptionRadius)
                .toList();
        acceleration = Stream.of(separation(localBoids), cohesion(localBoids), align(localBoids))
                .reduce(Vector2D.ZERO, Vector2D::add);
    }

    public void edges(final double width, final double height) {
        if (position.x() > width) {
            position = Vector2D.of(0, position.y());
        } else if (position.x() < 0) {
            position = Vector2D.of(width, position.y());
        }
        if (position.y() > height) {
            position = Vector2D.of(position.x(), 0);
        } else if (position.y() < 0) {
            position = Vector2D.of(position.x(), height);
        }
    }

    public void update() {
        position = position.add(velocity);
        velocity = velocity.add(acceleration).limit(maxSpeed);
        acceleration = Vector2D.ZERO;
    }

    public void show(final Sketch s) {
        s.noStroke();
        final float h = (float) (((velocity.heading() + Maths.TWO_PI) % Maths.TWO_PI) / Maths.TWO_PI);
        s.fill(s.hsbToRgb(h, 1.0f, 1.0f));
        s.triangle(position.x(), position.y(), 10, 15, velocity.heading());
    }

    private Vector2D steer(final Vector2D desired) {
        return desired.setMag(maxDesiredSpeed)
                .sub(velocity)
                .limit(maxForce);
    }
}
