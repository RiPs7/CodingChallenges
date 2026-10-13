package com.rips7.model;

import com.rips7.sketchkit.canvas.config.TextAlignH;
import com.rips7.sketchkit.canvas.config.TextAlignV;
import com.rips7.sketchkit.maths.Maths;
import com.rips7.sketchkit.maths.Vector2D;
import com.rips7.sketchkit.render.Sketch;

import java.awt.Color;

public final class Cell {
    private final int row;
    private final int col;
    private final double w;
    private final Vector2D position;
    private final boolean mine;
    private boolean revealed;
    private boolean flag;
    private int neighboringMines;

    public Cell(final int row, final int col, final double w, final double offset, final double mineProbability) {
        this.row = row;
        this.col = col;
        this.w = w;
        this.position = new Vector2D(row * w + offset, col * w + offset);
        this.mine = Maths.random(0, 1) < mineProbability;
        this.revealed = false;
        this.flag = false;
        this.neighboringMines = 0;
    }

    public int row() {
        return row;
    }

    public int col() {
        return col;
    }

    public boolean mine() {
        return mine;
    }

    public void reveal() {
        flag = false;
        revealed = true;
    }

    public boolean revealed() {
        return revealed;
    }

    public void flag() {
        flag = !flag;
    }

    public int neighboringMines() {
        return neighboringMines;
    }

    public void neighboringMines(final int count) {
        neighboringMines = count;
    }

    public boolean contains(final int x, final int y) {
        return x > position.x() &&
               x < position.x() + w &&
               y > position.y() &&
               y < position.y() + w;
    }

    public void show(final Sketch s) {
        s.stroke(Color.BLACK);
        s.noFill();
        s.rect(position.x(), position.y(), w, w);
        if (!revealed && !flag) return;

        if (flag) {
            s.fill(new Color(50, 50, 200));
            s.triangle(position.x() + w / 2, position.y() + w / 2, w / 3, w / 2, 0);
        } else if (mine) {
            s.fill(new Color(127, 127, 127));
            s.circle(position.x() + w / 2, position.y() + w / 2, w / 2);
        } else {
            s.fill(new Color(200, 200, 200));
            s.rect(position.x(), position.y(), w, w);
            s.fill(Color.BLACK);
            if (neighboringMines == 0) return;
            s.textAlign(TextAlignH.CENTER, TextAlignV.CENTER);
            s.textSize(32);
            s.text(String.valueOf(neighboringMines), position.x() + w / 2, position.y() + w / 2);
        }
    }
}
