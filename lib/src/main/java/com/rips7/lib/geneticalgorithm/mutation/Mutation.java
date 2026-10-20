package com.rips7.lib.geneticalgorithm.mutation;

@SuppressWarnings("unused")
public final class Mutation {
    public static final Mutation.MutationFactory BIT_FLIP = BitFlipMutation::new;

    public static final Mutation.MutationFactory SWAP = SwapMutation::new;

    private Mutation() {
    }

    @FunctionalInterface
    public interface MutationFactory {
        MutationStrategy configure(final double probability);
    }

}
