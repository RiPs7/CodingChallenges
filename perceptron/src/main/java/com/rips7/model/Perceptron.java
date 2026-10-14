package com.rips7.model;

import com.rips7.sketchkit.maths.Maths;
import com.rips7.sketchkit.render.Sketch;

import java.awt.Color;
import java.util.stream.IntStream;

public class Perceptron {
    private static final ActivationFunction ACTIVATION = ActivationFunction.SIGN;

    private final double[] weights;

    private final double learningRate;

    public Perceptron(final int n, final double learningRate) {
        this.weights = IntStream.range(0, n + 1)
                .mapToDouble(i -> Maths.random(-1, 1))
                .toArray();
        this.learningRate = learningRate;
    }

    public int guess(final double[] inputs) {
        return ACTIVATION.apply(dot(inputs, weights));
    }

    public void train(final double[] inputs, final int target) {
        final int guess = guess(inputs);
        final int error = target - guess;
        for (int i = 0; i < weights.length; i++) {
            weights[i] += error * inputs[i] * learningRate;
        }
    }

    public void show(final Sketch s) {
        final Point p1 = new Point(-1, y(-1));
        final Point p2 = new Point(1, y(1));
        s.strokeWeight(2);
        s.stroke(Color.BLUE);
        s.line(p1.pixelX(s), p1.pixelY(s), p2.pixelX(s), p2.pixelY(s));
    }

    private double dot(final double[] inputs, final double[] weights) {
        return IntStream.range(0, inputs.length)
                .mapToObj(i -> inputs[i] * weights[i])
                .reduce(Double::sum)
                .orElse(0.0);
    }

    private double y(final double x) {
        final double w0 = weights[0];
        final double w1 = weights[1];
        final double bias = weights[2];
        return - (bias / w1) - (w0 / w1) * x;
    }
}
