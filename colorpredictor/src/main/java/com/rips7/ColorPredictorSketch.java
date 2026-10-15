package com.rips7;

import com.rips7.lib.neuralnetwork.NeuralNetwork;
import com.rips7.sketchkit.canvas.config.TextAlignH;
import com.rips7.sketchkit.canvas.config.TextAlignV;
import com.rips7.sketchkit.render.Sketch;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import static com.rips7.sketchkit.maths.Maths.random;

public class ColorPredictorSketch extends Sketch {
    private enum Selection {
        BLACK(new double[][] {{ 1, 0 }}),
        WHITE(new double[][] {{ 0, 1 }});

        private final double[][] value;

        Selection(final double[][] value) {
            this.value = value;
        }
    }

    private NeuralNetwork neuralNetwork;
    private Color bgColor;
    private Map<Color, Selection> userSelections;

    public static void main(String[] args) {
        new ColorPredictorSketch();
    }

    public ColorPredictorSketch() {
        super("Color Predictor", 1200, 500);
    }

    @Override
    public void setup() {
        neuralNetwork = new NeuralNetwork(3, new int[] { 3 }, 2, 0.1);

        // Original training
        for (int i = 0; i < 10000; i++) {
            final Color color = randomColor();
            neuralNetwork.train(colorAsInput(color), trainingTargets(color));
        }

        bgColor = randomColor();
        userSelections = new HashMap<>();
    }

    @Override
    public void mousePressed() {
        bgColor = randomColor();

        // User feedback for further training
        userSelections.put(bgColor, mouseX() < width() / 2 ? Selection.BLACK : Selection.WHITE);
        for (int i = 0; i < 100; i++) {
            userSelections.forEach((color, selection) ->
                    neuralNetwork.train(colorAsInput(color), selectionAsTarget(selection)));
        }

        redraw();
    }

    @Override
    public void draw() {
        background(bgColor);

        noStroke();
        textSize(64);
        textAlign(TextAlignH.CENTER, TextAlignV.CENTER);
        fill(Color.BLACK);
        text("Black", width() / 4, height() / 3);
        fill(Color.WHITE);
        text("White", 3 * width() / 4, height() / 3);
        stroke(Color.BLACK);
        strokeWeight(4);
        line(width() / 2, 0, width() / 2, height());

        noStroke();
        final double predictionX = predict() == Selection.BLACK ? width() / 4 : 3 * width() / 4;
        circle(predictionX,  2 * height() / 3, 100);

        noLoop();
    }

    private Color randomColor() {
        return new Color((int) random(255), (int) random(255), (int) random(255));
    }

    private Selection predict() {
        final double[] prediction = neuralNetwork.predict(colorAsInput(bgColor))[0];
        return prediction[0] > prediction[1] ? Selection.BLACK : Selection.WHITE;
    }

    private double[][] trainingTargets(final Color color) {
        // Arbitrary classification: if r + g + b is over 381, pick black, else white
        return color.getRed() + color.getGreen() + color.getBlue() > 381 ? Selection.BLACK.value : Selection.WHITE.value;
    }

    private double[][] colorAsInput(final Color color) {
        return new double[][] {{ (double) color.getRed() / 255, (double) color.getGreen() / 255, (double) color.getBlue() / 255 }};
    }

    private double[][] selectionAsTarget(final Selection selection) {
        return selection.value;
    }
}
