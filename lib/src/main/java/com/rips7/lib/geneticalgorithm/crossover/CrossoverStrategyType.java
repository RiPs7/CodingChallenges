package com.rips7.lib.geneticalgorithm.crossover;

@SuppressWarnings("unused")
public enum CrossoverStrategyType {
    SINGLE_POINT {
        @Override
        public CrossoverStrategy configure(final double parameter) {
            return new SinglePointCrossover(parameter);
        }
    },

    ORDER {
        @Override
        public CrossoverStrategy configure(final double parameter) {
            return new SinglePointCrossover(parameter);
        }
    },

    UNIFORM {
        @Override
        public CrossoverStrategy configure(final double parameter) {
            return new UniformCrossover(parameter);
        }
    };

    public abstract CrossoverStrategy configure(final double parameter);
}
