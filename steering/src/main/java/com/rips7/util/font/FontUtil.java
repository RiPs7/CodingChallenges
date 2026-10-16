package com.rips7.util.font;

import com.rips7.SteeringSketch;
import com.rips7.sketchkit.maths.Vector2D;
import com.rips7.sketchkit.render.Sketch;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FontUtil {

    public static Font loadFont(final String path){
        try (final InputStream is = SteeringSketch.class.getResourceAsStream(path)) {
            return Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(is));
        } catch (final FontFormatException | IOException e) {
            throw new RuntimeException("Failed to load font " + path + ": " + e.getMessage());
        }
    }

    public static List<List<Vector2D>> textToShapes(final Sketch s, final Font font, final String text, final double x,
            final double y, final float size, final int pointSpacing) {
        final List<List<Vector2D>> allShapes = new ArrayList<>();

        final Font sizedFont = font.deriveFont(size);
        final GlyphVector glyphVector = sizedFont.createGlyphVector(
                new FontRenderContext(new AffineTransform(), true, true),
                text);

        final FontMetrics fm = s.getFontMetrics(sizedFont);
        final double offsetX = x - fm.stringWidth(text) / 2.0;
        final double offsetY = y + (fm.getAscent() - fm.getDescent()) / 2.0;

        final PathIterator pathIterator = glyphVector.getOutline().getPathIterator(
                AffineTransform.getTranslateInstance(offsetX, offsetY));

        double[] coords = new double[6];
        List<Vector2D> currentShape = null;
        double[] lastPoint = new double[2];

        while (!pathIterator.isDone()) {
            switch (pathIterator.currentSegment(coords)) {
                case PathIterator.SEG_MOVETO:
                    currentShape = new ArrayList<>();
                    currentShape.add(new Vector2D(coords[0], coords[1]));
                    allShapes.add(currentShape);
                    lastPoint[0] = coords[0];
                    lastPoint[1] = coords[1];
                    break;

                case PathIterator.SEG_LINETO:
                    if (currentShape != null) {
                        interpolateLine(lastPoint, coords, currentShape, pointSpacing);
                        lastPoint[0] = coords[0];
                        lastPoint[1] = coords[1];
                    }
                    break;

                case PathIterator.SEG_QUADTO:
                    if (currentShape != null) {
                        interpolateQuad(lastPoint, coords, currentShape, pointSpacing);
                        lastPoint[0] = coords[2];
                        lastPoint[1] = coords[3];
                    }
                    break;

                case PathIterator.SEG_CUBICTO:
                    if (currentShape != null) {
                        interpolateCubic(lastPoint, coords, currentShape, pointSpacing);
                        lastPoint[0] = coords[4];
                        lastPoint[1] = coords[5];
                    }
                    break;

                case PathIterator.SEG_CLOSE:
                    break;
            }
            pathIterator.next();
        }

        return allShapes;
    }

    private static void interpolateLine(double[] from, double[] to, List<Vector2D> shape, final int pointSpacing) {
        double len = Math.hypot(to[0] - from[0], to[1] - from[1]);
        int steps = Math.max(1, (int) Math.round(len / pointSpacing));
        for (int i = 1; i <= steps; i++) {
            double t = (double) i / steps;
            shape.add(new Vector2D(lerp(from[0], to[0], t), lerp(from[1], to[1], t)));
        }
    }

    private static void interpolateQuad(double[] from, double[] coords, List<Vector2D> shape, final int pointSpacing) {
        double x0 = from[0], y0 = from[1];
        double cx = coords[0], cy = coords[1];
        double x1 = coords[2], y1 = coords[3];
        double len = estimateQuadLength(x0, y0, cx, cy, x1, y1);
        int steps = Math.max(1, (int) Math.round(len / pointSpacing));
        for (int i = 1; i <= steps; i++) {
            double t = (double) i / steps;
            double mt = 1 - t;
            shape.add(new Vector2D(
                    mt*mt*x0 + 2*mt*t*cx + t*t*x1,
                    mt*mt*y0 + 2*mt*t*cy + t*t*y1
            ));
        }
    }

    private static void interpolateCubic(double[] from, double[] coords, List<Vector2D> shape, final int pointSpacing) {
        double x0 = from[0], y0 = from[1];
        double cx1 = coords[0], cy1 = coords[1];
        double cx2 = coords[2], cy2 = coords[3];
        double x1  = coords[4], y1  = coords[5];
        double len = estimateCubicLength(x0, y0, cx1, cy1, cx2, cy2, x1, y1);
        int steps = Math.max(1, (int) Math.round(len / pointSpacing));
        for (int i = 1; i <= steps; i++) {
            double t = (double) i / steps;
            double mt = 1 - t;
            shape.add(new Vector2D(
                    mt*mt*mt*x0 + 3*mt*mt*t*cx1 + 3*mt*t*t*cx2 + t*t*t*x1,
                    mt*mt*mt*y0 + 3*mt*mt*t*cy1 + 3*mt*t*t*cy2 + t*t*t*y1
            ));
        }
    }

    // Approximate curve lengths by sampling N segments along them
    private static double estimateQuadLength(double x0, double y0, double cx, double cy, double x1, double y1) {
        int n = 20;
        double len = 0, px = x0, py = y0;
        for (int i = 1; i <= n; i++) {
            double t = (double) i / n, mt = 1 - t;
            double qx = mt*mt*x0 + 2*mt*t*cx + t*t*x1;
            double qy = mt*mt*y0 + 2*mt*t*cy + t*t*y1;
            len += Math.hypot(qx - px, qy - py);
            px = qx; py = qy;
        }
        return len;
    }

    private static double estimateCubicLength(double x0, double y0, double cx1, double cy1,
                                              double cx2, double cy2, double x1, double y1) {
        int n = 20;
        double len = 0, px = x0, py = y0;
        for (int i = 1; i <= n; i++) {
            double t = (double) i / n, mt = 1 - t;
            double qx = mt*mt*mt*x0 + 3*mt*mt*t*cx1 + 3*mt*t*t*cx2 + t*t*t*x1;
            double qy = mt*mt*mt*y0 + 3*mt*mt*t*cy1 + 3*mt*t*t*cy2 + t*t*t*y1;
            len += Math.hypot(qx - px, qy - py);
            px = qx; py = qy;
        }
        return len;
    }

    private static double lerp(double a, double b, double t) {
        return a + (b - a) * t;
    }

}
