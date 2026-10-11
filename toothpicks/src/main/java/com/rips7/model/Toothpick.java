package com.rips7.model;

import com.rips7.sketchkit.render.Sketch;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public record Toothpick(int ax, int ay, int bx, int by, boolean horizontal, AtomicBoolean isNew) {

    private static final int LEN = 63;

    public Toothpick(final int x, final int y, final boolean horizontal) {
        this(
            horizontal ? x - LEN / 2 : x,
            horizontal ? y : y - LEN / 2,
            horizontal ? x + LEN / 2 : x,
            horizontal ? y : y + LEN / 2,
            horizontal,
            new AtomicBoolean(true));
    }

    public boolean isNewToothpick() {
        return isNew.get();
    }

    public int minX() {
        return Math.min(ax, bx);
    }

    public int maxX() {
        return Math.max(ax, bx);
    }

    public List<Toothpick> createNewToothpicks(final List<Toothpick> toothpicks) {
        boolean availablePointA = true;
        boolean availablePointB = true;
        for (final Toothpick other : toothpicks) {
            if (!availablePointA && !availablePointB) break;
            if (other == this) continue;
            if (other.intersects(ax, ay)) {
                availablePointA = false;
            } else if (other.intersects(bx, by)) {
                availablePointB = false;
            }
        }
        final List<Toothpick> newToothpicks = new ArrayList<>();
        if (availablePointA) {
            newToothpicks.add(new Toothpick(ax, ay, !horizontal));
        }
        if (availablePointB) {
            newToothpicks.add(new Toothpick(bx, by, !horizontal));
        }
        isNew.set(false);
        return newToothpicks;
    }

    public void show(final Sketch s) {
        s.stroke(isNew.get() ? Color.RED : Color.BLACK);
        s.strokeWeight(2);
        s.line(ax, ay, bx, by);
    }

    private boolean intersects(final int x, final int y) {
        return (ax == x && ay == y) || (bx == x && by == y);
    }

}
