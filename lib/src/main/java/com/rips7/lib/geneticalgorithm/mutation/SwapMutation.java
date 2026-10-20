package com.rips7.lib.geneticalgorithm.mutation;

import com.rips7.lib.geneticalgorithm.Chromosome;
import com.rips7.lib.geneticalgorithm.Gene;

import java.util.ArrayList;
import java.util.List;

public class SwapMutation implements MutationStrategy {
    private final double mutationProbability; // Chance that the chromosome mutates at all

    public SwapMutation(final double mutationProbability) {
        this.mutationProbability = mutationProbability;
    }

    @Override
    public <T> Chromosome<T> mutate(final Chromosome<T> child) {
        if (Math.random() >= mutationProbability) {
            return child; // No mutation happens
        }

        final List<Gene<T>> genes = new ArrayList<>(child.dna().genes());
        final int size = genes.size();

        // Pick two distinct random indices and swap their positions
        final int indexA = (int) (Math.random() * size);
        final int indexB = (int) (Math.random() * size);

        final Gene<T> temp = genes.get(indexA);
        genes.set(indexA, genes.get(indexB));
        genes.set(indexB, temp);

        return child.create(genes.stream().toList());
    }

}
