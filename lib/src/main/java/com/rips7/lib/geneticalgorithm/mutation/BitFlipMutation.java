package com.rips7.lib.geneticalgorithm.mutation;

import com.rips7.lib.geneticalgorithm.Chromosome;
import com.rips7.lib.geneticalgorithm.Gene;

import java.util.ArrayList;
import java.util.List;

public class BitFlipMutation implements MutationStrategy {
    private final double mutationRate;

    public BitFlipMutation(final double mutationRate) {
        this.mutationRate = mutationRate;
    }

    @Override
    public <T> Chromosome<T> mutate(final Chromosome<T> child) {
        final List<Gene<T>> genes = new ArrayList<>(child.dna().genes());
        boolean mutated = false;
        for (int i = 0; i < genes.size(); i++) {
            if (Math.random() < mutationRate) {
                genes.set(i, new Gene<>(flipBoolean(genes.get(i).value())));
                mutated = true;
            }
        }

        return mutated ? child.create(genes) : child;
    }

    private static <T> boolean parseBoolean(final T value) {
        return switch (value) {
            case Boolean b -> b;
            case Number n  -> n.intValue() == 1;
            case String s  -> Boolean.parseBoolean(s);
            case null      -> throw new IllegalArgumentException("Cannot parse null as boolean");
            default        -> throw new IllegalArgumentException("Cannot parse type %s as boolean".formatted(value.getClass().getSimpleName()));
        };
    }

    @SuppressWarnings("unchecked")
    private static <T> T flipBoolean(final T value) {
        final boolean flipped = !parseBoolean(value);
        return switch (value) {
            case Boolean ignored -> (T) Boolean.valueOf(flipped);
            case Integer ignored -> (T) Integer.valueOf(flipped ? 1 : 0);
            case Long ignored    -> (T) Long.valueOf(flipped ? 1L : 0L);
            case Double ignored  -> (T) Double.valueOf(flipped ? 1.0 : 0.0);
            case Float ignored   -> (T) Float.valueOf(flipped ? 1f : 0f);
            case String ignored  -> (T) String.valueOf(flipped);
            default              -> throw new IllegalArgumentException("Cannot flip type %s as boolean".formatted(value.getClass().getSimpleName()));
        };
    }
}

