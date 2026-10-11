package com.rips7;

import com.rips7.model.Star;
import com.rips7.sketchkit.render.Sketch;

import java.awt.Color;
import java.util.stream.IntStream;

public class StarfieldSketch extends Sketch {

    private Star[] stars;

    public static void main(String[] args) {
        new StarfieldSketch();
    }

    public StarfieldSketch() {
        super("Starfield", 800, 800);
    }

    @Override
    public void setup() {
        stars = IntStream.range(0, (int) (2 * width()))
                .mapToObj(i -> new Star(this))
                .toArray(Star[]::new);
    }

    @Override
    public void draw() {
        background(Color.BLACK);

        translate(width() / 2, height() / 2);

        for (final Star star : stars) {
            star.update();
            star.show();
        }
    }
}
