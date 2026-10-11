package com.rips7;

import com.rips7.model.Toothpick;
import com.rips7.sketchkit.render.Sketch;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ToothpicksSketch extends Sketch {

    private static final int LIMIT = 26_130;

    private List<Toothpick> toothpicks;
    private List<Integer> numberSequence;

    private int viewMinX;
    private int viewMaxX;

    public static void main(String[] args) {
        new ToothpicksSketch();
    }

    public ToothpicksSketch() {
        super("Toothpicks", 600, 600);
    }

    @Override
    public void setup() {
        viewMinX = (int) (-width() / 2);
        viewMaxX = (int) (width() / 2);

        toothpicks = new ArrayList<>();
        numberSequence = new ArrayList<>();

        toothpicks.add(new Toothpick(0, 0, true));
    }

    @Override
    public void draw() {
        numberSequence.add(toothpicks.size());

        background(new Color(255, 255, 255));

        translate(width() / 2, height() / 2);

        for (final Toothpick toothpick : toothpicks) {
            viewMinX = Math.min(viewMinX, toothpick.minX());
            viewMaxX = Math.max(viewMaxX, toothpick.maxX());
        }

        scale((float) ((width() - 50) / (float) (viewMaxX - viewMinX)));

        toothpicks.forEach(toothpick -> toothpick.show(this));

        final List<Toothpick> newToothpicks = toothpicks.stream()
            .filter(Toothpick::isNewToothpick)
            .toList();
        final List<Toothpick> nextToothpicks = newToothpicks.stream()
            .flatMap(toothpick -> toothpick.createNewToothpicks(newToothpicks).stream())
            .toList();

        toothpicks.addAll(nextToothpicks);

        if (toothpicks.size() >= LIMIT) {
            noLoop();
            System.out.printf("Toothpick number sequence: %s%n", numberSequence.stream().map(String::valueOf).collect(Collectors.joining(",")));
        }
    }
}
