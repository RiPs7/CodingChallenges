package com.rips7.model;

import com.rips7.sketchkit.maths.Vector2D;
import com.rips7.sketchkit.render.Sketch;

import java.awt.Color;
import java.util.Optional;

public class Ray {
    private Vector2D dir;
    private Vector2D pos;

    public Ray(final Vector2D pos, final double angle) {
        this.pos = pos;
        this.dir = Vector2D.fromAngle(angle);
    }

    public void setAngle(final double angle) {
        dir = Vector2D.fromAngle(angle);
    }

    public void moveTo(final double x, final double y) {
        pos = Vector2D.of(x, y);
    }

    public Optional<Vector2D> cast(final Wall wall) {
        // https://en.wikipedia.org/wiki/Line%E2%80%93line_intersection
        final double x1 = wall.a().x();
        final double y1 = wall.a().y();
        final double x2 = wall.b().x();
        final double y2 = wall.b().y();
        final double x3 = pos.x();
        final double y3 = pos.y();
        final double x4 = pos.x() + dir.x();
        final double y4 = pos.y() + dir.y();

        final double den = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);
        if (den == 0) {
            return Optional.empty();
        }

        final double t = ((x1 - x3) * (y3 - y4) - (y1 - y3) * (x3 - x4)) / den;
        final double u = -((x1 - x2) * (y1 - y3) - (y1 - y2) * (x1 - x3)) / den;

        if (t <= 0 || t >= 1 || u <= 0) {
            return Optional.empty();
        }

        final Vector2D point = Vector2D.of(x1 + t * (x2 - x1), y1 + t * (y2 - y1));
        return Optional.of(point);
    }

    public void show(final Sketch s) {
        s.pushMatrix();
        s.stroke(new Color(255, 255, 255, 100));
        s.translate(pos.x(), pos.y());
        s.line(0, 0, dir.x() * 10, dir.y() * 10);
        s.popMatrix();
    }
}
