package com.rips7.lib.geneticalgorithm.mutation;

@SuppressWarnings("unused")
public enum MutationStrategyType {
    BIT_FLIP {
        @Override
        public MutationStrategy configure(final double parameter) {
            return new BitFlipMutation(parameter);
        }
    },

    SWAP {
        @Override
        public MutationStrategy configure(final double parameter) {
            return new SwapMutation(parameter);
        }
    };

    public abstract MutationStrategy configure(final double parameter);
}
