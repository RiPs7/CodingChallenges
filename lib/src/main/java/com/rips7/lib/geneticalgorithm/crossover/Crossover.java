package com.rips7.lib.geneticalgorithm.crossover;

@SuppressWarnings("unused")
public final class Crossover {
    public static final Crossover.CrossoverFactory SINGLE_POINT = SinglePointCrossover::new;

    public static final Crossover.CrossoverFactory ORDER = OrderCrossover::new;

    public static final Crossover.CrossoverFactory UNIFORM = UniformCrossover::new;

    private Crossover() {
    }

    @FunctionalInterface
    public interface CrossoverFactory {
        CrossoverStrategy configure(final double probability);
    }

}
