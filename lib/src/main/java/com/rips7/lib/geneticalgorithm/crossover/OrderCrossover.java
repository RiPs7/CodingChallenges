package com.rips7.lib.geneticalgorithm.crossover;

import com.rips7.lib.geneticalgorithm.Chromosome;
import com.rips7.lib.geneticalgorithm.Gene;
import com.rips7.sketchkit.maths.Maths;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OrderCrossover implements CrossoverStrategy {

    private final double probability;

    public OrderCrossover(final double probability) {
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

        return Arrays.asList(
                parent1.create(orderCrossover(genesA, genesB)),
                parent2.create(orderCrossover(genesB, genesA)));
    }

    @SuppressWarnings("unchecked")
    private static <T> List<Gene<T>> orderCrossover(final List<Gene<T>> donor, final List<Gene<T>> filler) {
        final int length = donor.size();

        // Pick two cut points
        int start = (int) Maths.random(length);
        int end   = (int) Maths.random(length);
        if (start > end) {
            int tmp = start;
            start = end;
            end = tmp;
        }

        // Copy the segment from the donor into the child
        final Gene<T>[] child = new Gene[length];
        final Set<Gene<T>> inChild = new HashSet<>();
        for (int i = start; i <= end; i++) {
            child[i] = donor.get(i);
            inChild.add(donor.get(i));
        }

        // Fill remaining positions with the filler's genes, in order, wrapping around
        int insertAt = (end + 1) % length;
        int readFrom = (end + 1) % length;

        while (inChild.size() < length) {
            final Gene<T> gene = filler.get(readFrom);
            if (!inChild.contains(gene)) {
                child[insertAt] = gene;
                inChild.add(gene);
                insertAt = (insertAt + 1) % length;
            }
            readFrom = (readFrom + 1) % length;
        }

        return Arrays.asList(child);
    }
}