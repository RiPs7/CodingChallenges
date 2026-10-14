package com.rips7;

import com.rips7.model.Perceptron;
import com.rips7.model.Point;
import com.rips7.model.TrainingDataset;
import com.rips7.sketchkit.render.Sketch;

import java.awt.Color;

public class PerceptronSketch extends Sketch {

    private Perceptron perceptron;
    private TrainingDataset trainingDataset;

    public static void main(String[] args) {
        new PerceptronSketch();
    }

    public PerceptronSketch() {
        super("Perceptron", 800, 800);
    }

    @Override
    public void setup() {
        perceptron = new Perceptron(2, 0.0001);
        trainingDataset = new TrainingDataset(200);
    }

    @Override
    public void draw() {
        background(Color.WHITE);

        perceptron.show(this);
        trainingDataset.show(this);

        noStroke();
        for (final Point point : trainingDataset.points()) {
            fill(perceptron.guess(point.asInput()) == point.label() ? Color.GREEN : Color.RED);
            circle(point.pixelX(this), point.pixelY(this), 16);
        }

        for (final Point point : trainingDataset.points()) {
            perceptron.train(point.asInput(), point.label());
        }
    }
}
