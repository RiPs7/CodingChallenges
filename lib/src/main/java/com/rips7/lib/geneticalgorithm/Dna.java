package com.rips7.lib.geneticalgorithm;

import java.util.List;
import java.util.function.Supplier;

public record Dna<T>(List<Gene<T>> genes) {

    public static <T> Dna<T> of(final Supplier<List<T>> genesCreator) {
        final List<Gene<T>> genes = genesCreator.get().stream()
                .map(Gene::new)
                .toList();
        return new Dna<>(genes);
    }
}
