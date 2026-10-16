package com.rips7;

import com.rips7.model.Dna;
import com.rips7.model.Vehicle;
import com.rips7.sketchkit.canvas.config.TextAlignH;
import com.rips7.sketchkit.canvas.config.TextAlignV;
import com.rips7.sketchkit.maths.Maths;
import com.rips7.sketchkit.maths.Vector2D;
import com.rips7.sketchkit.render.Sketch;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SteeringEvolutionSketch extends Sketch {

    private static final boolean DEBUG = false;

    private static final int STARTING_VEHICLES = 5;
    private static final int MAX_VEHICLES = 100;
    private static final int STARTING_FOODS = 20;
    private static final int STARTING_POISONS = 20;

    private static final double FOOD_RATE = 0.2;
    private static final double POISON_RATE = 0.2;

    private static final double STATS_PANEL_WIDTH = 170;
    private static final double STATS_PANEL_HEIGHT = 260;

    private ArrayList<Vehicle> vehicles;
    private ArrayList<Vector2D> foods;
    private ArrayList<Vector2D> poisons;

    private int maxVehicles;
    private AtomicReference<Stats> bestStats;

    public static void main(String[] args) {
        new SteeringEvolutionSketch();
    }

    public SteeringEvolutionSketch() {
        super("Steering Evolution", 1080, 720);
    }

    @Override
    public void setup() {
        vehicles = IntStream.range(0, STARTING_VEHICLES)
                .mapToObj(i -> new Vehicle(Maths.random(width()), Maths.random(height())))
                .collect(Collectors.toCollection(ArrayList::new));

        foods = IntStream.range(0, STARTING_FOODS)
                .mapToObj(i -> Vector2D.of(Maths.random(width()), Maths.random(height())))
                .collect(Collectors.toCollection(ArrayList::new));

        poisons = IntStream.range(0, STARTING_POISONS)
                .mapToObj(i -> Vector2D.of(Maths.random(width()), Maths.random(height())))
                .collect(Collectors.toCollection(ArrayList::new));

        maxVehicles = vehicles.size();
        bestStats = new AtomicReference<>(new Stats(0, Dna.empty()));
    }

    @Override
    public void draw() {
        background(new Color(51, 51, 51));

        if (vehicles.isEmpty()) {
            noLoop();
            fill(Color.RED);
            textSize(190);
            textAlign(TextAlignH.CENTER, TextAlignV.CENTER);
            text("FINISHED", width() / 2, height() / 2);
        }

        if (Maths.random(1) < FOOD_RATE) {
            foods.add(Vector2D.of(Maths.random(width()), Maths.random(height())));
        }
        if (Maths.random(1) < POISON_RATE) {
            poisons.add(Vector2D.of(Maths.random(width()), Maths.random(height())));
        }

        noStroke();
        fill(new Color(0, 255, 0));
        foods.forEach(food -> circle(food.x(), food.y(), 10));
        fill(new Color(255, 0, 0));
        poisons.forEach(poison -> circle(poison.x(), poison.y(), 10));

        final List<Vehicle> newVehicles = new ArrayList<>();
        vehicles.forEach(vehicle -> {
            vehicle.reproduce().ifPresent(newVehicles::add);
            vehicle.behaviors(width(), height(), foods, poisons);
            vehicle.update(this.deltaTime());
            vehicle.show(this, DEBUG);
            if (vehicle.age() > bestStats.get().age) {
                bestStats.set(new Stats(vehicle.age(), vehicle.dna()));
            }
        });
        vehicles.removeIf(Vehicle::dead);
        newVehicles.stream()
                .limit(MAX_VEHICLES - vehicles.size())
                .forEach(vehicles::add);

        if (vehicles.size() > maxVehicles) {
            maxVehicles = vehicles.size();
        }

        noStroke();
        fill(new Color(10, 10, 10, 200));
        rect(width() - STATS_PANEL_WIDTH, 0, STATS_PANEL_WIDTH, STATS_PANEL_HEIGHT);
        fill(Color.RED);
        textAlign(TextAlignH.LEFT, TextAlignV.CENTER);
        textSize(12);
        final double xOffset = width() - STATS_PANEL_WIDTH + 20;
        text("Max vehicles: %s".formatted(maxVehicles), xOffset, 20);
        text("Current vehicles: %s".formatted(vehicles.size()), xOffset, 40);
        bestStats.get().show(this, xOffset, 60);
    }

    private record Stats(long age, Dna dna) {

        @SuppressWarnings("SameParameterValue")
        private void show(final Sketch s, double xOffset, double yOffset) {
            s.text("Best DNA Stats:", xOffset, 20 + yOffset);
            s.text("Age: %ss".formatted(age), xOffset, 60 + yOffset);
            s.text(format("Food steer", dna.foodSteer()), xOffset, 100 + yOffset);
            s.text(format("Poison steer", dna.poisonSteer()), xOffset, 120 + yOffset);
            s.text(format("Max health", dna.maxHealth()), xOffset, 140 + yOffset);
            s.text(format("Perception", dna.perception()), xOffset, 160 + yOffset);
            s.text(formatPercentage("Reproduction %", dna.reproductionChance()), xOffset, 180 + yOffset);
        }

        private String format(final String label, final double value) {
            return "%s: %.2f".formatted(label, value);
        }

        @SuppressWarnings("SameParameterValue")
        private String formatPercentage(final String label, final double value) {
            return "%s: %.2f%%".formatted(label, value * 100.0);
        }
    }

}
