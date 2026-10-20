package com.rips7.lib.geneticalgorithm.parentselection;

import com.rips7.lib.geneticalgorithm.Chromosome;

import java.util.Map;

public interface ParentSelectionStrategy {

    <T> Chromosome<T> pickParent(final Map<Chromosome<T>, Double> fitnessScores);
}
