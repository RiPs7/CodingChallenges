package com.rips7.model;

import com.rips7.sketchkit.maths.Maths;
import com.rips7.sketchkit.render.Sketch;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.IntStream;

public record TrainingDataset(Point[] points) {

    private static final double F_M = Maths.random(-1, 1);
    private static final double F_B = Maths.random(-1, 1);
    private static final Function<Double, Double> F = (x) -> F_M * x + F_B; // m * x + b

    public TrainingDataset(final int n) {
        this(createRandomPoints(n));
    }

    private static Point[] createRandomPoints(final int n) {
        return IntStream.range(0, n)
                .mapToObj(i -> new Point(Maths.random(-1, 1), Maths.random(-1, 1), F))
                .toArray(Point[]::new);
    }

    public void show(final Sketch s) {
        Arrays.stream(points).forEach(point -> point.show(s));
        final Point p1 = new Point(-1, F.apply(-1.0));
        final Point p2 = new Point(1, F.apply(1.0));
        s.line(p1.pixelX(s), p1.pixelY(s), p2.pixelX(s), p2.pixelY(s));
    }

}
