package com.rips7.lib.geneticalgorithm.parentselection;

@SuppressWarnings("unused")
public final class ParentSelection {

    public static final TournamentFactory TOURNAMENT = TournamentParentSelection::new;

    public static final ParentSelectionStrategy ROULETTE_WHEEL = new RouletteWheelParentSelection();

    private ParentSelection() {
    }

    @FunctionalInterface
    public interface TournamentFactory {
        ParentSelectionStrategy configure(final int tournamentSize);
    }
}
