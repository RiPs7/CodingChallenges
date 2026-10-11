package com.rips7;

import com.rips7.model.Maze;
import com.rips7.sketchkit.render.Sketch;

import java.awt.Color;

public class MazeSketch extends Sketch {

    private static final double RES = 8;

    private Maze maze;

    public static void main(String[] args) {
        new MazeSketch();
    }

    public MazeSketch() {
        super("Maze", 800, 800);
    }

    @Override
    public void setup() {
        maze = new Maze((int) (height() / RES), (int) (width() / RES), RES);
        maze.generate();
    }

    @Override
    public void draw() {
        background(Color.GRAY);

        maze.update();

        if (maze.isGenerated()) {
            maze.solve();
        }

        maze.show(this);

        if (maze.isSolved()) {
            System.out.println("Maze generated and solved");
            noLoop();
        }
    }
}
