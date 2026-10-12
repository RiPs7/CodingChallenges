package com.rips7.model;

import com.rips7.sketchkit.maths.Vector2D;
import com.rips7.sketchkit.render.Sketch;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Snake {
    private final int size;
    private final List<Vector2D> body;
    private final Vector2D startingPosition;

    private Vector2D position;
    private Vector2D velocity;

    private int tailSize;

    public Snake(final int row, final int col, final int size) {
        this.size = size;
        this.startingPosition = Vector2D.of(row * size,col * size);
        this.body = new ArrayList<>();
        reset();
    }

    private void reset() {
        this.position = Vector2D.of(startingPosition.x(), startingPosition.y());
        this.velocity = Vector2D.of(0, -1);
        this.body.clear();
        this.tailSize = 3;
    }

    public void update() {
        if (tailSize != 0) {
            if (tailSize == body.size()) {
                for (int i = body.size() - 1; i >= 1; i--) {
                    body.set(i, body.get(i - 1));
                }
            } else {
                body.addFirst(Vector2D.of(position.x(), position.y()));
            }
            body.set(0, Vector2D.of(position.x(), position.y()));
        }

        position = Vector2D.of(position.x() + velocity.x() * size, position.y() + velocity.y() * size);
    }

    public void dir(final int x, final int y) {
        final Vector2D newVelocity = Vector2D.of(x, y);
        if (velocity.dot(newVelocity) != 0) return;
        velocity = Vector2D.of(x, y);
    }

    public boolean eat(final Food food) {
        if (position.dist(food.position()) > 1) {
            return false;
        }
        tailSize++;
        return true;
    }

    public void death(final Sketch s) {
        if (body.stream().skip(1).anyMatch(part -> part.dist(position) < 1)) {
            System.out.println("Game Over - Crashed into your own body");
            reset();
        }
        if (position.x() < 0 || position.x() >= s.width() || position.y() < 0 || position.y() >= s.height()) {
            System.out.println("Game Over - Went over the window edges");
            reset();
        }
    }

    public void show(final Sketch s) {
        s.stroke(Color.GRAY);

        s.fill(Color.GREEN);
        s.rect(position.x(), position.y(), size, size);

        s.fill(Color.LIGHT_GRAY);
        for (final Vector2D v : body) {
            s.rect(v.x(), v.y(), size, size);
        }
    }
}
