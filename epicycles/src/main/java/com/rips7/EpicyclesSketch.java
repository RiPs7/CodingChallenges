package com.rips7;

import com.rips7.model.Epicycle;
import com.rips7.model.EpicycleChain;
import com.rips7.model.KnownPath;
import com.rips7.model.Signal;
import com.rips7.util.DFT;
import com.rips7.sketchkit.maths.Maths;
import com.rips7.sketchkit.render.Sketch;
import com.rips7.sketchkit.maths.Vector2D;

import java.awt.Color;
import java.util.Comparator;

public class EpicyclesSketch extends Sketch {

    private static final double SAMPLE_RATE = 100.0;

    private Signal signalX;
    private EpicycleChain epicycleChainX;

    private Signal signalY;
    private EpicycleChain epicycleChainY;

    public static void main(String[] args) {
        new EpicyclesSketch();
    }

    public EpicyclesSketch() {
        super("Epicycles", 1600, 1000);
    }

    @Override
    public void setup() {
        final KnownPath path = KnownPath.HYPOTROCHOID;

        signalX = new Signal(path.getXs());
        epicycleChainX = new EpicycleChain(
                Vector2D.of(width() * 0.6, 100),
                DFT.apply(signalX.originalSignal()).stream()
                        .sorted(Comparator.comparing(DFT.Result::amp).reversed())
                        .map(res -> new Epicycle(
                                res.amp(),
                                res.phase(),
                                res.freq() * SAMPLE_RATE / signalX.originalSize(),
                                Maths.HALF_PI))
                        .toList());

        signalY = new Signal(path.getYs());
        epicycleChainY = new EpicycleChain(
                Vector2D.of(100, height() * 0.6),
                DFT.apply(signalY.originalSignal()).stream()
                        .sorted(Comparator.comparing(DFT.Result::amp).reversed())
                        .map(res -> new Epicycle(
                                res.amp(),
                                res.phase(),
                                res.freq() * SAMPLE_RATE / signalY.originalSize(),
                                0))
                        .toList());
    }

    @Override
    public void draw() {
        epicycleChainX.update(deltaTime());
        epicycleChainX.draw(this);
        final Vector2D horizontalEndpoint = epicycleChainX.endpoint();
        signalX.addSample(horizontalEndpoint.x());

        epicycleChainY.update(deltaTime());
        epicycleChainY.draw(this);
        final Vector2D verticalEndpoint = epicycleChainY.endpoint();
        signalY.addSample(verticalEndpoint.y());

        stroke(Color.RED);
        for (int i = 1; i < Math.min(signalX.realTimeSamples().size(), signalY.realTimeSamples().size()); i++) {
            final double x1 = signalX.realTimeSamples().get(i);
            final double y1 = signalY.realTimeSamples().get(i);
            final double x2 = signalX.realTimeSamples().get(i - 1);
            final double y2 = signalY.realTimeSamples().get(i - 1);
            line(x1, y1, x2, y2);
        }

        stroke(Color.GRAY);
        line(horizontalEndpoint.x(), horizontalEndpoint.y(), horizontalEndpoint.x(), verticalEndpoint.y());
        line(verticalEndpoint.x(), verticalEndpoint.y(), horizontalEndpoint.x(), verticalEndpoint.y());
    }
}
