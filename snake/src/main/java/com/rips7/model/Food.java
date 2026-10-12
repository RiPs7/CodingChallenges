package com.rips7.model;

import com.rips7.sketchkit.maths.Vector2D;
import com.rips7.sketchkit.render.Sketch;

import java.awt.Color;

public record Food(Vector2D position, int size) {

    public Food(final int row, final int col, final int size) {
        this(Vector2D.of(row * size, col * size), size);
    }

    public void show(final Sketch s) {
        s.noStroke();
        s.fill(new Color(255, 0, 100));
        s.rect(position.x(), position.y(), size, size);
    }
}
