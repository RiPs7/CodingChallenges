package com.rips7.model;

import com.rips7.sketchkit.maths.Maths;
import com.rips7.sketchkit.render.Sketch;

import java.awt.Color;
import java.util.function.Function;

public record Point(double x, double y, int label) {

    private static int classify(final double x, final double y, final Function<Double, Double> classifier) {
        return classifier.apply(x) >= y ? 1 : -1;
    }

    public Point(final double x, final double y) {
        this(x, y, 0);
    }

    public Point(final double x, final double y, final Function<Double, Double> classifier) {
        this(x, y, classify(x, y, classifier));
    }

    public double[] asInput() {
        return new double[]{x, y, 1};
    }

    public double pixelX(final Sketch s) {
        return Maths.map(x, -1, 1, 0, s.width());
    }

    public double pixelY(final Sketch s) {
        return Maths.map(y, -1, 1, s.height(), 0);
    }

    public void show(final Sketch s) {
        s.stroke(Color.BLACK);
        s.fill(label == 1 ? Color.WHITE : Color.BLACK);
        s.circle(pixelX(s), pixelY(s), 32);
    }
}
