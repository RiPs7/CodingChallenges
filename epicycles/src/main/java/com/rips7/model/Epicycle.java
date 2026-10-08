package com.rips7.model;

import com.rips7.EpicyclesSketch;
import com.rips7.sketchkit.maths.Maths;
import com.rips7.sketchkit.maths.Vector2D;

import java.awt.Color;

public final class Epicycle {

    private final double radius;
    private final double omega;
    private final double rotation;

    private double angle;

    public Epicycle(final double radius, final double phase, final double frequency, final double rotation) {
        this.radius = radius;
        this.omega = Maths.TWO_PI * frequency;
        this.angle = phase;
        this.rotation = rotation;
    }

    public void update(final double deltaTime) {
        angle += omega * deltaTime;
    }

    public Vector2D endpoint(final Vector2D center) {
        return new Vector2D(
                center.x() + radius * Math.cos(angle - Maths.HALF_PI + rotation),
                center.y() + radius * Math.sin(angle - Maths.HALF_PI + rotation));
    }

    public void draw(final EpicyclesSketch sketch, final Vector2D center) {
        sketch.noFill();
        sketch.stroke(Color.BLACK);

        sketch.circle(center.x(), center.y(), 2 * radius);

        final Vector2D endpoint = endpoint(center);

        sketch.line(center.x(), center.y(), endpoint.x(), endpoint.y());

        sketch.fill(Color.BLACK);
        sketch.circle(endpoint.x(), endpoint.y(), 10);
    }
}