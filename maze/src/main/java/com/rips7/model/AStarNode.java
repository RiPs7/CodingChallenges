package com.rips7.model;

public class AStarNode {
    private final Cell cell;
    private double f;
    private double g;

    public AStarNode(final Cell cell) {
        this.cell = cell;
        this.f = Double.MAX_VALUE;
        this.g = Double.MAX_VALUE;
    }

    public Cell cell() {
        return this.cell;
    }

    public double f() {
        return this.f;
    }

    public void f(final double f) {
        this.f = f;
    }

    public double g() {
        return this.g;
    }

    public void g(final double g) {
        this.g = g;
    }
}
