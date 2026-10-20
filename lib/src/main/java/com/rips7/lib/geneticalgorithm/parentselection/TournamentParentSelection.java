package com.rips7.lib.geneticalgorithm.parentselection;

import com.rips7.lib.geneticalgorithm.Chromosome;

import java.util.Comparator;
import java.util.Map;

public final class TournamentParentSelection implements ParentSelectionStrategy {

    private final int tournamentSize;

    public TournamentParentSelection(final int tournamentSize) {
        if (tournamentSize < 1) {
            throw new IllegalArgumentException("Tournament size must be positive");
        }
        this.tournamentSize = tournamentSize;
    }

    @Override
    public <T> Chromosome<T> pickParent(final Map<Chromosome<T>, Double> fitnessScores) {
        return fitnessScores.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(tournamentSize)
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Fitness scores must not be empty"));

    }
}