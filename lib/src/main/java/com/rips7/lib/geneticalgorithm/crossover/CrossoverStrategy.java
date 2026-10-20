package com.rips7.lib.geneticalgorithm.crossover;

import com.rips7.lib.geneticalgorithm.Chromosome;

import java.util.List;

public interface CrossoverStrategy {

    <T> List<Chromosome<T>> crossOver(final Chromosome<T> parent1, final Chromosome<T> parent2);
}
