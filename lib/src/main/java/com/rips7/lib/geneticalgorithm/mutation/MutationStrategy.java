package com.rips7.lib.geneticalgorithm.mutation;

import com.rips7.lib.geneticalgorithm.Chromosome;

public interface MutationStrategy {

    <T> Chromosome<T> mutate(final Chromosome<T> chromosome);
}
