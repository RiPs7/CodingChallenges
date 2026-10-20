package com.rips7.lib.geneticalgorithm.parentselection;

import com.rips7.lib.geneticalgorithm.Chromosome;
import com.rips7.sketchkit.maths.Maths;

import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicReference;

public final class RouletteWheelParentSelection implements ParentSelectionStrategy {

    @Override
    public <T> Chromosome<T> pickParent(final Map<Chromosome<T>, Double> fitnessScores) {
        final NavigableMap<Double, Chromosome<T>> distribution = new TreeMap<>();
        final AtomicReference<Double> cumulative = new AtomicReference<>(0.0);
        fitnessScores.forEach((chromosome, fitness) -> {
            cumulative.set(cumulative.get() + fitness);
            distribution.put(cumulative.get(), chromosome);
        });
        return distribution.higherEntry(Maths.random(cumulative.get())).getValue();
    }
}