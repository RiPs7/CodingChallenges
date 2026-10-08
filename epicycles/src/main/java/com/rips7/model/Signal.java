package com.rips7.model;

import java.util.ArrayList;
import java.util.List;

public record Signal(List<Double> originalSignal, List<Double> realTimeSamples) {

    public Signal(final List<Double> originalSignal) {
        this(originalSignal, new ArrayList<>());
    }

    public int originalSize() {
        return originalSignal.size();
    }

    public void addSample(final double value) {
        realTimeSamples.add(value);
    }
}
