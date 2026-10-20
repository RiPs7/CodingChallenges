package com.rips7;

import com.rips7.lib.geneticalgorithm.GeneticAlgorithm;
import com.rips7.lib.geneticalgorithm.crossover.Crossover;
import com.rips7.lib.geneticalgorithm.mutation.Mutation;
import com.rips7.lib.geneticalgorithm.parentselection.ParentSelection;
import com.rips7.sketchkit.canvas.renderer.Renderer;
import com.rips7.sketchkit.maths.Vector2D;
import com.rips7.sketchkit.render.Sketch;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import static com.rips7.sketchkit.maths.Maths.random;

public class TravellingSalespersonGASketch extends Sketch {

    private static final int CITIES_COUNT = 10;
    private static final int POPULATION_SIZE = 200;

    private List<Vector2D> cities;
    private GeneticAlgorithm<Integer> geneticAlgorithm;

    private double bestDistance = Double.MAX_VALUE;
    private List<Integer> bestOrder = IntStream.range(0, CITIES_COUNT).boxed().toList();

    public static void main(String[] args) {
        new TravellingSalespersonGASketch();
    }

    public TravellingSalespersonGASketch() {
        super("Travelling Salesperson - Genetic Algorithm", 800, 800);
    }

    @Override
    public void setup() {
        frameRate(1);
        cities = IntStream.range(0, CITIES_COUNT)
                .mapToObj(i -> Vector2D.of(random(width()), random(height())))
                .toList();

        geneticAlgorithm = new GeneticAlgorithm<>(
                POPULATION_SIZE,
                this::randomCitiesOrder,
                this::calculateFitness,
                ParentSelection.ROULETTE_WHEEL,
                Crossover.ORDER.configure(1),
                Mutation.SWAP.configure(1));
    }

    @Override
    public void draw() {
        background(Color.BLACK);

        noFill();
        stroke(Color.WHITE);
        strokeWeight(5);
        cities.forEach(c -> circle(c.x(), c.y(), 20));

        noFill();
        stroke(new Color(255, 0, 255));
        strokeWeight(2);
        final Renderer.ShapeBuilder shape = shape();
        bestOrder.stream()
                .map(cities::get)
                .forEach(c -> shape.vertex(c.x(), c.y()));
        shape.endShape();

        geneticAlgorithm.nextEpoch();
    }

    private List<Integer> randomCitiesOrder() {
        final List<Integer> initialOrder = IntStream.range(0, CITIES_COUNT).boxed().toList();
        final List<Integer> randomOrder = new ArrayList<>(initialOrder);
        Collections.shuffle(randomOrder);
        return randomOrder;
    }

    private double calculateFitness(final List<Integer> order) {
        final double citiesDistance = totalCitiesDistance(order);
        if (citiesDistance < bestDistance) {
            bestDistance = citiesDistance;
            bestOrder = order;
        }
        return 1 / (citiesDistance + 1);
    }

    private double totalCitiesDistance(final List<Integer> order) {
        return IntStream.range(0, order.size() - 1)
                .mapToDouble(i -> cities.get(order.get(i)).dist(cities.get(order.get(i + 1))))
                .sum();
    }
}
