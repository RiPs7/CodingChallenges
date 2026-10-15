package com.rips7.model;

import com.rips7.maths.Matrix;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class NeuralNetwork {
    private static final Function<Double, Double> SIGMOID = x -> 1 / (1 + Math.exp(-x));
    private static final Function<Double, Double> D_SIGMOID = y -> y * (1 - y);

    private final Matrix[] weights;
    private final Matrix[] biases;

    private final double learningRate;
    private final List<Matrix> outputs;

    public NeuralNetwork(final int inputNodes, final int[] hiddenNodes, final int outputNodes, final double learningRate) {
        this.weights = new Matrix[hiddenNodes.length + 1];
        this.biases = new Matrix[hiddenNodes.length + 1];
        this.weights[0] = Matrix.random(inputNodes, hiddenNodes[0]);
        this.biases[0] = Matrix.mul(Matrix.random(1, hiddenNodes[0]), 0);
        for (int i = 0; i < hiddenNodes.length - 1; i++) {
            this.weights[i + 1] = Matrix.random(hiddenNodes[i], hiddenNodes[i + 1]);
            this.biases[i + 1] = Matrix.mul(Matrix.random(1, hiddenNodes[i + 1]), 0);
        }
        this.weights[this.weights.length - 1] = Matrix.random(hiddenNodes[hiddenNodes.length - 1], outputNodes);
        this.biases[this.biases.length - 1] = Matrix.mul(Matrix.random(1, outputNodes), 0);
        this.learningRate = learningRate;
        this.outputs = new ArrayList<>();
    }

    public double[][] predict(final double[][] inputs) {
        // 1. Clear the cached outputs
        outputs.clear();
        // 2. Start with outputs equal to the horizontal vector of the inputs
        Matrix outputs = Matrix.from(inputs);
        // 3. Iterate through the layers
        for (int i = 0; i < weights.length; i++) {
            // 4. Find the layer output and add it to the outputs matrix
            outputs = Matrix.addHorizontalVector(Matrix.product(outputs, weights[i]), biases[i]);
            // 5. Map it through the activation function
            outputs = Matrix.map(outputs, SIGMOID);
            // 6. Save the post-activation output of the layer
            this.outputs.add(outputs);
        }
        // 7. The result is a horizontal vector, so return it as an array
        return outputs.data();
    }


    public void train(final double[][] inputs, final double[][] targets) {
        // 1. Validate the batch dimensions.
        if (inputs.length == 0 || inputs.length != targets.length) {
            throw new IllegalArgumentException("Inputs and targets must have the same non-zero batch size");
        }

        final int batchSize = inputs.length;

        // 2. Convert the input and target batches into matrices.
        final Matrix input = Matrix.from(inputs);
        final Matrix target = Matrix.from(targets);

        // 3. Forward pass: predict() caches every layer's activations.
        predict(inputs);

        final Matrix output = outputs.getLast();

        // 4. Calculate the output layer's error gradient.
        //    delta = (prediction - target) * sigmoid'(prediction)
        Matrix delta = Matrix.mul(Matrix.sub(output, target), Matrix.map(output, D_SIGMOID));

        // 5. Work backwards through the network.
        for (int i = weights.length - 1; i >= 0; i--) {
            // Get the activations entering this layer's weights.
            // For the first layer, these are the original input rows.
            final Matrix previousActivation = i == 0 ? input : outputs.get(i - 1);

            // Calculate the previous layer's delta BEFORE updating weights.
            // We need the original weights to propagate the error correctly.
            Matrix previousDelta = null;

            if (i > 0) {
                // Propagate the delta backwards through the weights,
                // then multiply by the hidden layer's sigmoid derivative.
                final Matrix hiddenActivationDerivative = Matrix.map(outputs.get(i - 1), D_SIGMOID);

                previousDelta = Matrix.mul(Matrix.product(delta, Matrix.transpose(weights[i])), hiddenActivationDerivative);
            }

            // Calculate the weight gradient:
            // dL/dW = transpose(previousActivation) * delta / batchSize
            final Matrix gradient = Matrix.scale(Matrix.product(Matrix.transpose(previousActivation), delta), 1.0 / batchSize);

            // Average each column of delta to calculate the bias gradient.
            // This produces a single row matching the bias matrix dimensions.
            final Matrix biasGradient = columnMean(delta);

            // Gradient descent: W = W - learningRate * gradient.
            weights[i] = Matrix.sub(weights[i], Matrix.scale(gradient, learningRate));

            // Gradient descent: b = b - learningRate * biasGradient.
            biases[i] = Matrix.sub(biases[i], Matrix.scale(biasGradient, learningRate));

            // Continue backwards using the delta calculated with original weights.
            if (i > 0) {
                delta = previousDelta;
            }
        }
    }

    // Calculate the mean of each column, returning a 1 x cols row vector.
    private static Matrix columnMean(final Matrix matrix) {
        final int rows = matrix.rows();
        final int cols = matrix.cols();

        return new Matrix(1, cols, (i, j) -> {
            double sum = 0.0;
            for (int r = 0; r < rows; r++) {
                sum += matrix.data()[r][j];
            }
            return sum / rows;
        });
    }

}
