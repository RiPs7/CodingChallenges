package com.rips7;

import com.rips7.model.Vehicle;
import com.rips7.sketchkit.maths.Maths;
import com.rips7.sketchkit.maths.Vector2D;
import com.rips7.sketchkit.render.Sketch;
import com.rips7.util.font.FontUtil;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

public class SteeringSketch extends Sketch {

    private List<Vehicle> vehicles;

    public static void main(String[] args) {
        new SteeringSketch();
    }

    public SteeringSketch() {
        super("Steering", 1080, 720);
    }

    @Override
    public void setup() {
        final Font font = FontUtil.loadFont("/AvenirNextLTProDemi.otf");

        vehicles = new ArrayList<>();

        FontUtil.textToShapes(this, font, "RiPs7 - Steering", width() / 2, height() / 2, 100, 5).forEach(
                shape -> shape.forEach(
                        v -> vehicles.add(
                                new Vehicle(
                                        Vector2D.of(Maths.random(width()), Maths.random(height())),
                                        Vector2D.of(v.x(), v.y())))));
    }

    @Override
    public void draw() {
        background(new Color(51, 51, 51));

        vehicles.forEach(vehicle -> {
            vehicle.update(mouseX(), mouseY());
            vehicle.show(this);
        });
    }

}
