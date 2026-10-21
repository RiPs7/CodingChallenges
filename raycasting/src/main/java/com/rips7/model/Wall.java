package com.rips7.model;

import com.rips7.sketchkit.maths.Vector2D;
import com.rips7.sketchkit.render.Sketch;

import java.awt.Color;

public record Wall(Vector2D a, Vector2D b) {

    public Wall(final double x1, final double y1, final double x2, final double y2) {
        this(Vector2D.of(x1, y1), Vector2D.of(x2, y2));
    }

    public void show(final Sketch s) {
        s.stroke(Color.WHITE);
        s.line(a.x(), a.y(), b.x(), b.y());
    }
}
