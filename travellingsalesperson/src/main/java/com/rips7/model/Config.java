package com.rips7.model;

import com.rips7.sketchkit.canvas.renderer.Renderer;
import com.rips7.sketchkit.maths.Vector2D;
import com.rips7.sketchkit.render.Sketch;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

public record Config(double totalDistance, List<Integer> citiesIndices) {

    private static double totalDistance(final List<Vector2D> cities, final List<Integer> indices) {
        return IntStream.range(0, indices.size() - 1)
                .mapToDouble(i -> cities.get(indices.get(i)).dist(cities.get(indices.get(i + 1))))
                .sum();
    }

    public Config(final List<Vector2D> cities, final List<Integer> indices) {
        this(totalDistance(cities, indices), indices);
    }

    public Config nextConfig(final List<Vector2D> cities) {
        return new Config(cities, nextLexicographicOrder(new ArrayList<>(citiesIndices)));
    }

    public void show(final Sketch s, final List<Vector2D> cities, final Color color, final double strokeWeight) {
        s.noFill();
        s.stroke(color);
        s.strokeWeight(strokeWeight);
        final Renderer.ShapeBuilder shape = s.shape();
        citiesIndices.stream().map(cities::get).forEach(c -> shape.vertex(c.x(), c.y()));
        shape.endShape();
    }

    private List<Integer> nextLexicographicOrder(final List<Integer> values) {
        if (values.size() < 2) {
            throw new LastConfigException();
        }

        // Step 1: Find largest X such that values[X] < values[X+1]
        int X = -1;
        for (int x = values.size() - 2; x >= 0; x--) {
            if (values.get(x) < values.get(x + 1) ) {
                X = x;
                break;
            }
        }
        // If no such X exists, it's the last permutation
        if (X == -1) {
            throw new LastConfigException();
        }

        // Step 2: Find largest Y such that values[X] < values[Y]
        int Y = -1;
        for (int y = values.size() - 1; y >= 0; y--) {
            if (values.get(X) < values.get(y) ) {
                Y = y;
                break;
            }
        }

        // Step 3: Swap values[X] and values[Y]
        Collections.swap(values, X, Y);

        // Step 4: Reverse values[X+1 ...]
        Collections.reverse(values.subList(X + 1, values.size()));

        return values.stream().toList();
    }
}

