package com.rips7;

import com.rips7.model.MineHitException;
import com.rips7.model.Grid;
import com.rips7.sketchkit.canvas.config.TextAlignH;
import com.rips7.sketchkit.canvas.config.TextAlignV;
import com.rips7.sketchkit.input.MouseButton;
import com.rips7.sketchkit.render.Sketch;

import java.awt.Color;

public class MinesweeperSketch extends Sketch {

    private static final int RES = 40;
    private static final int PAD = 5;
    private static final double MINE_PROBABILITY = 0.3;

    private Grid grid;
    private boolean gameOver;
    private boolean youWin;

    public static void main(String[] args) {
        new MinesweeperSketch();
    }

    public MinesweeperSketch() {
        super("Minesweeper", 400 + 2 * PAD, 400 + 2 * PAD);
    }

    @Override
    public void setup() {
        grid = new Grid(
                (int) (height() / RES),
                (int) (width() / RES),
                RES,
                PAD,
                MINE_PROBABILITY);
        gameOver = false;
    }

    @Override
    public void mousePressed() {
        if (gameOver) return;
        try {
            if (mouseButton() == MouseButton.LEFT) {
                grid.reveal(mouseX(), mouseY());
            } else {
                grid.flag(mouseX(), mouseY());
            }
            youWin = grid.checkComplete();
        } catch (final MineHitException bhe) {
            gameOver = true;
        }
    }

    @Override
    public void draw() {
        background(Color.WHITE);

        grid.show(this);

        if (gameOver) {
            textSize(40);
            textAlign(TextAlignH.CENTER, TextAlignV.CENTER);
            fill(Color.RED);
            text("GAME OVER!", width() / 2, height() / 2);
            grid.revealAll();
        } else if (youWin) {
            textSize(40);
            textAlign(TextAlignH.CENTER, TextAlignV.CENTER);
            fill(Color.GREEN);
            text("YOU WIN!", width() / 2, height() / 2);
        }
    }
}
