package com.rips7;

import com.rips7.model.Config;
import com.rips7.model.LastConfigException;
import com.rips7.sketchkit.canvas.config.TextAlignH;
import com.rips7.sketchkit.canvas.config.TextAlignV;
import com.rips7.sketchkit.maths.Vector2D;
import com.rips7.sketchkit.render.Sketch;

import java.awt.Color;
import java.util.List;
import java.util.stream.IntStream;

import static com.rips7.sketchkit.maths.Maths.random;

public class TravellingSalespersonSketch extends Sketch {

    private static final int CITIES_COUNT = 5;
    private static final int TOTAL_PERMUTATIONS;
    static {
        if (CITIES_COUNT < 2) {
            throw new RuntimeException("Too few cities");
        }
        TOTAL_PERMUTATIONS = factorial(CITIES_COUNT);
        if (CITIES_COUNT > 10) {
            throw new RuntimeException("Too many cities.. We don't want to check %s!=%s configurations".formatted(CITIES_COUNT, TOTAL_PERMUTATIONS));
        }
    }
    private static int factorial(final int n) {
        return n == 1 ? 1 : n * factorial(n - 1);
    }

    private List<Vector2D> cities;

    private Config config;
    private Config bestConfig;

    private int permutations;

    public static void main(String[] args) {
        new TravellingSalespersonSketch();
    }

    public TravellingSalespersonSketch() {
        super("Travelling Salesperson", 800, 800);
    }

    @Override
    public void setup() {
        cities = IntStream.range(0, CITIES_COUNT)
                .mapToObj(i -> Vector2D.of(random(width()), random(height())))
                .toList();

        config = new Config(cities, IntStream.range(0, CITIES_COUNT).boxed().toList());
        bestConfig = config;

        permutations = 0;
    }

    @Override
    public void draw() {
        background(Color.BLACK);

        noFill();
        stroke(Color.WHITE);
        strokeWeight(5);
        cities.forEach(c -> circle(c.x(), c.y(), 20));

        bestConfig.show(this, cities, new Color(200, 0, 200), 5);

        fill(Color.RED);
        textSize(26);
        textAlign(TextAlignH.LEFT, TextAlignV.CENTER);
        text("%.2f%% completed".formatted(config == null ? 100 : 100 * (permutations / (double) TOTAL_PERMUTATIONS)), 10, height() - 30);

        if (config == null) {
            return;
        }

        config.show(this, cities, Color.WHITE, 1);

        try {
            config = config.nextConfig(cities);
            permutations++;
            if (config.totalDistance() < bestConfig.totalDistance()) {
                bestConfig = config;
            }
        } catch (final LastConfigException e) {
            config = null;
        }
    }
}
