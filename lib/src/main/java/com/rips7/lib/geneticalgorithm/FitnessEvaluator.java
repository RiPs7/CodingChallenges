package com.rips7.lib.geneticalgorithm;

import java.util.List;

@FunctionalInterface
public interface FitnessEvaluator<T> {

    double apply(final List<T> values);

}
