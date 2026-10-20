package com.rips7.lib.geneticalgorithm;

import com.rips7.lib.geneticalgorithm.crossover.CrossoverStrategy;
import com.rips7.lib.geneticalgorithm.mutation.MutationStrategy;
import com.rips7.lib.geneticalgorithm.parentselection.ParentSelectionStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public record Population<T>(int size, List<Chromosome<T>> chromosomes, ParentSelectionStrategy parentSelectionStrategy,
                            CrossoverStrategy crossoverStrategy, MutationStrategy mutationStrategy) {

    public static <T> Population<T> of(final int size, final Supplier<List<T>> genesCreator,
            final FitnessEvaluator<T> fitnessEvaluator, final ParentSelectionStrategy parentSelectionStrategy,
            final CrossoverStrategy crossoverStrategy, final MutationStrategy mutationStrategy) {
        final List<Chromosome<T>> chromosomes = IntStream.range(0, size)
                .mapToObj(i -> Chromosome.of(genesCreator, fitnessEvaluator))
                .toList();
        return new Population<>(size, chromosomes, parentSelectionStrategy, crossoverStrategy, mutationStrategy);
    }

    public Population<T> nextPopulation() {
        final Map<Chromosome<T>, Double> fitness = calculateFitness();

        final List<Chromosome<T>> nextPopulation = new ArrayList<>();
        while (nextPopulation.size() < size) {
            final Chromosome<T> parentA = parentSelectionStrategy.pickParent(fitness);
            final Chromosome<T> parentB = parentSelectionStrategy.pickParent(fitness);
            final List<Chromosome<T>> children = crossoverStrategy.crossOver(parentA, parentB);
            for (final Chromosome<T> child : children) {
                final Chromosome<T> mutatedChild = mutationStrategy.mutate(child);
                nextPopulation.add(mutatedChild);
            }
        }

        return new Population<>(size, nextPopulation, parentSelectionStrategy, crossoverStrategy, mutationStrategy);
    }

    public Map<Chromosome<T>, Double> calculateFitness() {
        final Map<Chromosome<T>, Double> fitnessScores = chromosomes.stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        Chromosome::calculateFitness,
                        (existingValue, newValue) -> existingValue));
        final Double sum = fitnessScores.values().stream()
                .mapToDouble(Double::doubleValue)
                .sum();
        fitnessScores.replaceAll((chromosome, fitness) -> sum / fitness);
        return fitnessScores;
    }
}
