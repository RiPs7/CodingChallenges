package com.rips7;

import com.rips7.model.Particle;
import com.rips7.model.Wall;
import com.rips7.sketchkit.render.Sketch;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.rips7.sketchkit.maths.Maths.constrain;
import static com.rips7.sketchkit.maths.Maths.map;
import static com.rips7.sketchkit.maths.Maths.random;

public class RaycastingSketch extends Sketch {

    private static final int PADDING = 1;

    private static final double SCENE_WIDTH = 1200;
    private static final double SCENE_HEIGHT = 400;

    private List<Wall> walls;
    private Particle p;

    public static void main(String[] args) {
        new RaycastingSketch();
    }

    public RaycastingSketch() {
        super("Ray Casting", (int) SCENE_WIDTH, (int) SCENE_HEIGHT * 2);
    }

    @Override
    public void setup() {
        walls = IntStream.range(0, 5)
                .mapToObj(i -> new Wall(
                        random(SCENE_WIDTH),
                        random(SCENE_HEIGHT),
                        random(SCENE_WIDTH),
                        random(SCENE_HEIGHT)))
                .collect(Collectors.toCollection(ArrayList::new));
        walls.add(new Wall(PADDING, PADDING, SCENE_WIDTH - PADDING, PADDING));
        walls.add(new Wall(SCENE_WIDTH - PADDING, PADDING, SCENE_WIDTH - PADDING, SCENE_HEIGHT - PADDING));
        walls.add(new Wall(SCENE_WIDTH - PADDING, SCENE_HEIGHT - PADDING, PADDING, SCENE_HEIGHT - PADDING));
        walls.add(new Wall(PADDING, SCENE_HEIGHT - PADDING, PADDING, PADDING));


        p = new Particle(SCENE_WIDTH / 2, SCENE_HEIGHT / 2);
    }

    @Override
    public void draw() {
        background(Color.BLACK);

        final int moveForward;
        final int moveRight;
        final int rotateA;
        if (isKeyPressed()) {
            moveForward = switch(keyCode()) {
                case KeyEvent.VK_W -> 1;
                case KeyEvent.VK_S -> -1;
                default -> 0;
            };
            moveRight = switch(keyCode()) {
                case KeyEvent.VK_D -> 1;
                case KeyEvent.VK_A -> -1;
                default -> 0;
            };
            rotateA = switch(keyCode()) {
                case KeyEvent.VK_LEFT -> -1;
                case KeyEvent.VK_RIGHT -> 1;
                default -> 0;
            };
        } else {
            moveForward = 0;
            moveRight = 0;
            rotateA = 0;
        }
        p.move(moveForward, moveRight);
        p.rotate(rotateA);
        p.show(this);

        final List<Double> scene = p.lookAt(this, walls);
        pushMatrix();
        noStroke();
        translate(0, SCENE_HEIGHT);
        final double w = SCENE_WIDTH / scene.size();
        for (int i = 0; i < scene.size(); i++) {
            final double sceneSq = scene.get(i) * scene.get(i);
            final int b = (int) constrain(map(sceneSq, 0, SCENE_WIDTH * SCENE_WIDTH, 255, 0), 0, 255);
            fill(new Color(b, b, b));
            final double h = map(scene.get(i), 0, SCENE_WIDTH, SCENE_HEIGHT, 0);
            rect(i * w, SCENE_HEIGHT / 2 - h / 2, w + 1, h);
        }
        popMatrix();

        walls.forEach(wall -> wall.show(this));
    }
}
