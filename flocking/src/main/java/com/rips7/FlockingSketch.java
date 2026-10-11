package com.rips7;

import com.rips7.model.Boid;
import com.rips7.sketchkit.maths.Maths;
import com.rips7.sketchkit.render.Sketch;

import java.awt.Color;
import java.util.List;
import java.util.stream.IntStream;

public class FlockingSketch extends Sketch {

    private static final int SIZE = 300;

    private List<Boid> flock;

    public static void main(String[] args) {
        new FlockingSketch();
    }

    public FlockingSketch() {
        super("Flocking", 1600, 900);
    }

    @Override
    public void setup() {
        flock = IntStream.range(0, SIZE)
                .mapToObj(i -> new Boid(Maths.random(0, width()), Maths.random(0, height())))
                .toList();
    }

    @Override
    public void draw() {
        background(new Color(10, 10, 10));

        flock.forEach(boid -> {
            boid.edges(width(), height());
            boid.flock(flock);
            boid.update();
            boid.show(this);
        });
    }
}
