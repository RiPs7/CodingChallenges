package com.rips7.model;

import java.util.function.Function;

public enum ActivationFunction {
    SIGN(x -> x >= 0 ? 1 : -1);

    public final Function<Double, Integer> activation;

    ActivationFunction(final Function<Double, Integer> activation) {
        this.activation = activation;
    }

    public int apply(final double x) {
        return activation.apply(x);
    }
}
