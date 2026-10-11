package com.rips7.model;

import com.rips7.sketchkit.render.Sketch;

import java.awt.Color;
import java.util.concurrent.atomic.AtomicBoolean;

public record Cell(int row, int col, double w, boolean[] walls, AtomicBoolean visited) {

    private static final int TOP = 0;
    private static final int RIGHT = 1;
    private static final int BOTTOM = 2;
    private static final int LEFT = 3;

    public Cell(final int row, final int col, final double w) {
        this(row, col, w, new boolean[]{ true, true, true, true }, new AtomicBoolean(false));
    }

    public Cell visit() {
        visited.set(true);
        return this;
    }

    public boolean isVisited() {
        return visited.get();
    }

    public boolean isConnectedTo(final Cell other) {
        final int dRow = other.row - this.row;
        final int dCol = other.col - this.col;

        if (dRow == -1 && dCol == 0) return !walls[TOP];
        if (dRow ==  1 && dCol == 0) return !walls[BOTTOM];
        if (dRow == 0 && dCol ==  1) return !walls[RIGHT];
        if (dRow == 0 && dCol == -1) return !walls[LEFT];
        return false;   // not adjacent
    }

    public void removeWalls(final Cell other) {
        final int diffX = this.col - other.col;
        final int diffY = this.row - other.row;
        if (diffX == -1) { // 'this' is on the left of 'other'
            this.walls[RIGHT] = false;
            other.walls[LEFT] = false;
        } else if (diffX == 1) { // 'this' is on the right of 'other'
            this.walls[LEFT] = false;
            other.walls[RIGHT] = false;
        } else if (diffY == -1) { // 'this' is on top of 'other'
            this.walls[BOTTOM] = false;
            other.walls[TOP] = false;
        } else if (diffY == 1) { // 'this' is below 'other'
            this.walls[TOP] = false;
            other.walls[BOTTOM] = false;
        }
    }

    public void highlight(final Sketch s, final Color color) {
        final double x = col * w;
        final double y = row * w;

        s.noStroke();
        s.fill(color);
        s.rect(x, y, w, w);
    }

    public void show(final Sketch s) {
        final double x = col * w;
        final double y = row * w;

        s.stroke(Color.WHITE);
        if (walls[TOP])    s.line(x  , y  , x+w, y  );
        if (walls[RIGHT])  s.line(x+w, y  , x+w, y+w);
        if (walls[BOTTOM]) s.line(x+w, y+w, x  , y+w);
        if (walls[LEFT])   s.line(x  , y+w, x  , y  );

        if (isVisited()) {
            s.noStroke();
            s.fill(new Color(255, 0, 255, 100));
            s.rect(x, y, w, w);
        }
    }

}
