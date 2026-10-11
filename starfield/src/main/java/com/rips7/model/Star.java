package com.rips7.model;

import com.rips7.sketchkit.maths.Maths;
import com.rips7.sketchkit.render.Sketch;

import java.awt.Color;

public final class Star {
    private static final int SPEED = 20;

    private final Sketch sketchRef;

    private double x;
    private double y;
    private double z;

    public Star(final Sketch s) {
        this.sketchRef = s;
        reset();
    }

    public void update() {
        z -= SPEED;
        if (z < 1) {
            reset();
        }
    }

    public void show() {
        sketchRef.fill(Color.WHITE);
        sketchRef.noStroke();

        final double sx = Maths.map(x / z, 0, 1, 0, sketchRef.width());
        final double sy = Maths.map(y / z, 0, 1, 0, sketchRef.width());
        final double sz = Maths.map(z, 0, sketchRef.width() / 2, 10, 0);
        sketchRef.circle(sx, sy, sz);
    }

    private void reset() {
        this.x = Maths.random(-sketchRef.width() / 2, sketchRef.width() / 2);
        this.y = Maths.random(-sketchRef.height() / 2, sketchRef.height() / 2);
        this.z = Maths.random(0, sketchRef.width() / 2);
    }
}
