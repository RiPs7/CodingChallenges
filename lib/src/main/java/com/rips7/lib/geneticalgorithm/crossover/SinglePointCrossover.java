package com.rips7.lib.geneticalgorithm.crossover;

import com.rips7.lib.geneticalgorithm.Chromosome;
import com.rips7.lib.geneticalgorithm.Gene;
import com.rips7.sketchkit.maths.Maths;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SinglePointCrossover implements CrossoverStrategy {

    private final double probability;

    public SinglePointCrossover(final double probability) {
        if (probability < 0 || probability > 1) {
            throw new IllegalArgumentException("Probability must be between 0 and 1");
        }
        this.probability = probability;
    }

    @Override
    public <T> List<Chromosome<T>> crossOver(final Chromosome<T> parent1, final Chromosome<T> parent2) {

        if (Math.random() >= probability) {
            return Arrays.asList(parent1, parent2);
        }

        final List<Gene<T>> genesA = parent1.dna().genes();
        final List<Gene<T>> genesB = parent2.dna().genes();

        final int length = genesA.size();

        int cutPoint = (int) Maths.random(length);

        final List<Gene<T>> childGenes1 = new ArrayList<>(length);
        final List<Gene<T>> childGenes2 = new ArrayList<>(length);

        for (int i = 0; i < length; i++) {
            if (i < cutPoint) {
                childGenes1.add(genesA.get(i));
                childGenes2.add(genesB.get(i));
            } else {
                childGenes1.add(genesB.get(i));
                childGenes2.add(genesA.get(i));
            }
        }

        return Arrays.asList(parent1.create(childGenes1), parent2.create(childGenes2));
    }
}