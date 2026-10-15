package com.rips7;

import com.rips7.model.NeuralNetwork;
import com.rips7.sketchkit.render.Sketch;

import java.awt.Color;

public class NeuralNetworkSketch extends Sketch {

    private static final double[][] TRAINING_DATA_INPUT = new double[][] {{0, 0}, {0, 1}, {1, 0}, {1, 1}};
    private static final double[][] TRAINING_DATA_OUTPUT = new double[][] {{0}, {1}, {1}, {0}};

    private NeuralNetwork neuralNetwork;

    public static void main(String[] args) {
        new NeuralNetworkSketch();
    }

    public NeuralNetworkSketch() {
        super("Neural Network", 800, 800);
    }

    @Override
    public void setup() {
        neuralNetwork = new NeuralNetwork(2, new int[] { 3, 4 }, 1, 0.1);
    }

    @Override
    public void draw() {
        background(Color.BLACK);

        for (int i = 0; i < 500; i++) {
            neuralNetwork.train(TRAINING_DATA_INPUT, TRAINING_DATA_OUTPUT);
        }

        noStroke();
        final double resolution = 10;
        final int rows = (int) (height() / resolution);
        final int cols = (int) (width() / resolution);
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                final double x1 = row / (double) rows;
                final double x2 = col / (double) cols;
                final double y = neuralNetwork.predict(new double[][] {{ x1, x2 }})[0][0];
                final int value = (int) (y * 255);
                fill(new Color(value, value, value));
                rect(col * resolution, row * resolution, resolution, resolution);
            }
        }
    }
}
