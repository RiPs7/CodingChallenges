package com.rips7.model;

import com.rips7.EpicyclesSketch;
import com.rips7.sketchkit.maths.Vector2D;

import java.util.List;

public record EpicycleChain(Vector2D origin, List<Epicycle> epicycles) {

    public void update(final double deltaTime) {
        for (final Epicycle epicycle : epicycles) {
            epicycle.update(deltaTime);
        }
    }

    public Vector2D endpoint() {
        Vector2D center = origin;
        for (final Epicycle epicycle : epicycles) {
            center = epicycle.endpoint(center);
        }
        return center;
    }

    public void draw(final EpicyclesSketch sketch) {
        Vector2D center = origin;
        for (final Epicycle epicycle : epicycles) {
            epicycle.draw(sketch, center);
            center = epicycle.endpoint(center);
        }
    }
}