package com.rips7.lib.geneticalgorithm;

import com.rips7.lib.geneticalgorithm.crossover.CrossoverStrategy;
import com.rips7.lib.geneticalgorithm.mutation.MutationStrategy;
import com.rips7.lib.geneticalgorithm.parentselection.ParentSelectionStrategy;

import java.util.List;
import java.util.function.Supplier;

public class GeneticAlgorithm<T> {

    private Population<T> population;

    public GeneticAlgorithm(final int populationSize, final Supplier<List<T>> genesCreator,
                            final FitnessEvaluator<T> fitnessEvaluator, final ParentSelectionStrategy parentSelectionStrategy,
                            final CrossoverStrategy crossoverStrategy, final MutationStrategy mutationStrategy) {
        this.population = Population.of(populationSize, genesCreator, fitnessEvaluator, parentSelectionStrategy,
                crossoverStrategy, mutationStrategy);
    }

    public void nextEpoch() {
        population = population.nextPopulation();
    }
}
