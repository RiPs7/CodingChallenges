package com.rips7;

import com.rips7.model.Food;
import com.rips7.model.Snake;
import com.rips7.sketchkit.maths.Maths;
import com.rips7.sketchkit.render.Sketch;

import java.awt.Color;
import java.awt.event.KeyEvent;

public class SnakeSketch extends Sketch {

    private static final int RES = 16;

    private int rows;
    private int cols;

    private Snake snake;
    private Food food;

    public static void main(String[] args) {
        new SnakeSketch();
    }

    public SnakeSketch() {
        super("Snake", 512, 512);
    }

    @Override
    public void setup() {
        frameRate(4);

        rows = (int) (height() / RES);
        cols = (int) (width() / RES);
        snake = new Snake(rows / 2, cols / 2, RES);
        generateFood();
    }

    @Override
    public void keyPressed() {
        switch(keyCode()) {
            case KeyEvent.VK_UP: snake.dir(0, -1); break;
            case KeyEvent.VK_DOWN: snake.dir(0, 1); break;
            case KeyEvent.VK_LEFT: snake.dir(-1, 0); break;
            case KeyEvent.VK_RIGHT: snake.dir(1, 0); break;
        }
    }

    @Override
    public void draw() {
        background(new Color(51, 51, 51));

        snake.update();
        snake.death(this);
        snake.show(this);

        if (snake.eat(food)) {
            generateFood();
        }

        food.show(this);
    }

    private void generateFood() {
        food = new Food(
                (int) Math.floor(Maths.random(0, rows)),
                (int) Math.floor(Maths.random(0, cols)),
                RES);
    }
}
