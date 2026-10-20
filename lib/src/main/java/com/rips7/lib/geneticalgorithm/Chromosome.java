package com.rips7.lib.geneticalgorithm;

import java.util.List;
import java.util.function.Supplier;

public record Chromosome<T>(Dna<T> dna, FitnessEvaluator<T> fitnessEvaluator) {

    public static <T> Chromosome<T> of(final Supplier<List<T>> genesCreator, final FitnessEvaluator<T> fitnessEvaluator) {
        return new Chromosome<>(Dna.of(genesCreator), fitnessEvaluator);
    }

    public Chromosome<T> create(final List<Gene<T>> genes) {
        return new Chromosome<>(new Dna<>(genes), fitnessEvaluator);
    }

    public double calculateFitness() {
        final List<T> values = dna.genes().stream()
                .map(Gene::value)
                .toList();
        return fitnessEvaluator.apply(values);
    }
}
