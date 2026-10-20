package com.rips7.lib.geneticalgorithm.parentselection;

@SuppressWarnings("unused")
public enum ParentSelectionType {
    TOURNAMENT {
        @Override
        public ParentSelectionStrategy configure(final int parameter) {
            return new TournamentParentSelection(parameter);
        }
    },

    ROULETTE_WHEEL {
        @Override
        public ParentSelectionStrategy configure() {
            return new RouletteWheelParentSelection();
        }
    };

    public ParentSelectionStrategy configure(final int parameter) {
        throw new UnsupportedOperationException(this + " does not support configuration");
    }

    public ParentSelectionStrategy configure() {
        throw new UnsupportedOperationException(this + " requires configuration");
    }
}
