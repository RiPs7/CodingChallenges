package com.rips7.model;

import com.rips7.sketchkit.maths.Maths;
import com.rips7.sketchkit.maths.Vector2D;

import java.util.List;
import java.util.stream.IntStream;

public enum KnownPath {
    CIRCLE(circlePoints()),
    HEART(heartPoints()),
    STAR(starPoints()),
    SPIRAL(spiralPoints()),
    ROSE(rosePoints()),
    FLOWER(flowerPoints()),
    HYPOTROCHOID(hypotrochoidPoints()),
    INFINITY(infinityPoints()),
    WEB(webPoints()),
    ;

    private final List<Vector2D> points;

    KnownPath(final List<Vector2D> points) {
        this.points = points;
    }

    public List<Double> getXs() {
        return points.stream()
                .map(Vector2D::x)
                .toList();
    }

    public List<Double> getYs() {
        return points.stream()
                .map(Vector2D::y)
                .toList();
    }

    private static List<Vector2D> circlePoints() {
        return IntStream.range(0, 512).mapToObj(i -> {
            double t = Maths.TWO_PI * i / 512.0;
            double x = 150 * Math.cos(t);
            double y = 150 * Math.sin(t);
            return Vector2D.of(x, y);
        }).toList();
    }

    private static List<Vector2D> heartPoints() {
        return IntStream.range(0, 512).mapToObj(i -> {
            double t = Maths.TWO_PI * i / 512.0;
            double x = 10 * (16 * Math.pow(Math.sin(t), 3));
            double y = 10 * (13 * Math.cos(t) - 5 * Math.cos(2 * t) - 2 * Math.cos(3 * t) - Math.cos(4 * t));
            return Vector2D.of(x, y);
        }).toList();
    }

    private static List<Vector2D> starPoints() {
        return IntStream.range(0, 512).mapToObj(i -> {
            double t = Maths.TWO_PI * i / 512.0;
            double radius = 200 + 75 * Math.cos(5 * t);
            return Vector2D.of(radius * Math.cos(t), radius * Math.sin(t));
        }).toList();
    }

    private static List<Vector2D> spiralPoints() {
        return IntStream.range(0, 512).mapToObj(i -> {
            double t = Maths.TWO_PI * 3 * i / 511.0;
            double radius = 10 + 15 * t;
            return Vector2D.of(radius * Math.cos(t), radius * Math.sin(t));
        }).toList();
    }

    private static List<Vector2D> rosePoints() {
        return IntStream.range(0, 512).mapToObj(i -> {
            double t = Maths.TWO_PI * i / 512.0;
            double radius = 200 * Math.cos(5 * t);
            return Vector2D.of(radius * Math.cos(t), radius * Math.sin(t));
        }).toList();
    }

    private static List<Vector2D> flowerPoints() {
        return IntStream.range(0, 512).mapToObj(i -> {
            double t = Maths.TWO_PI * i / 512.0;
            double radius = 200 + 75 * Math.sin(13 * t);
            return Vector2D.of(radius * Math.cos(t), radius * Math.sin(t));
        }).toList();
    }

    private static List<Vector2D> infinityPoints() {
        return IntStream.range(0, 512).mapToObj(i -> {
            double t = Maths.TWO_PI * i / 512.0;
            double x = 150 * Math.sin(t);
            double y = 150 * Math.sin(t) * Math.cos(t);
            return Vector2D.of(x, y);
        }).toList();
    }

    private static List<Vector2D> webPoints() {
        return IntStream.range(0, 512).mapToObj(i -> {
            double t = Maths.TWO_PI * i / 512.0;
            double radius = 150 + 30 * Math.sin(8 * t) + 15 * Math.sin(16 * t);
            return Vector2D.of(radius * Math.cos(t), radius * Math.sin(t));
        }).toList();
    }

    private static List<Vector2D> hypotrochoidPoints() {
        return IntStream.range(0, 2048).mapToObj(i -> {
            double t = Maths.TWO_PI * 10 * i / 2048.0;
            double R = 120;
            double r = 35;
            double d = 60;
            double x = (R - r) * Math.cos(t) + d * Math.cos((R - r) / r * t);
            double y = (R - r) * Math.sin(t) - d * Math.sin((R - r) / r * t);
            return Vector2D.of(x, y);
        }).toList();
    }
}
