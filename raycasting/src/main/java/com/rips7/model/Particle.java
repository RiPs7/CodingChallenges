package com.rips7.model;

import com.rips7.sketchkit.maths.Vector2D;
import com.rips7.sketchkit.render.Sketch;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.rips7.sketchkit.maths.Maths.radians;

public class Particle {
    private static final int VIEW_RANGE = 45;
    private static final double SPEED = 2.0;
    private static final double ANGLE_SPEED = 0.01;

    private final List<Ray> rays;

    private Vector2D pos;
    private double heading;

    public Particle(final double x, final double y) {
        pos = Vector2D.of(x, y);
        rays = new ArrayList<>();
        for (int i = -VIEW_RANGE / 2; i < VIEW_RANGE / 2; i += 1) {
            rays.add(new Ray(pos, radians(i)));
        }
    }

    public void rotate(final double a) {
        heading += a * ANGLE_SPEED;
        for (int i = 0; i < rays.size(); i++) {
            rays.get(i).setAngle(radians(i - VIEW_RANGE * 0.5) + heading);
        }
    }

    public void move(final double forward, final double right) {
        double len = Math.sqrt(forward * forward + right * right);
        if (len == 0) return;

        double nf = forward / len;
        double nr = right / len;

        double dx = (nf * Math.cos(heading) - nr * Math.sin(heading)) * SPEED;
        double dy = (nf * Math.sin(heading) + nr * Math.cos(heading)) * SPEED;

        pos = pos.add(Vector2D.of(dx, dy));
        rays.forEach(r -> r.moveTo(pos.x(), pos.y()));
    }

    public List<Double> lookAt(final Sketch s, final List<Wall> walls) {
        return rays.stream()
                .map(r -> walls.stream()
                        .map(r::cast)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .map(p -> Map.entry(p, p.dist(pos)))
                        .min(Comparator.comparingDouble(Map.Entry::getValue))
                        .map(pointAndDist -> {
                            final Vector2D point = pointAndDist.getKey();
                            s.line(pos.x(), pos.y(), point.x(), point.y());
                            return pointAndDist.getValue();
                        })
                        .orElse(Double.MAX_VALUE))
                .toList();
    }

    public void show(final Sketch s) {
        s.fill(Color.WHITE);
        s.circle(pos.x(), pos.y(), 4);
        rays.forEach(r -> r.show(s));
    }
}
