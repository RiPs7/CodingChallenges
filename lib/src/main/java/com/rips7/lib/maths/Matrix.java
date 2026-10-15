package com.rips7.lib.maths;

import com.rips7.sketchkit.maths.Maths;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.IntStream;

public record Matrix(int rows, int cols, double[][] data) {

    public Matrix(final int rows, final int cols, final BiFunction<Integer, Integer, Double> initialiser) {
        this(rows, cols, new double[rows][cols]);
        initMatrix(data,initialiser);
    }

    public static Matrix from(final double[][] data) {
        return new Matrix(data.length, data[0].length, data);
    }

    public static Matrix addHorizontalVector(final Matrix matrix, final Matrix horizontalVector) {
        return new Matrix(matrix.rows(), matrix.cols(), (i, j) -> matrix.data[i][j] + horizontalVector.data[0][j]);
    }

    public static Matrix random(final int rows, final int cols) {
        return new Matrix(rows, cols, (i, j) -> Maths.random(-1, 1));
    }

    public static Matrix add(final Matrix a, final Matrix b) {
        return new Matrix(a.rows, a.cols, (i, j) -> a.data[i][j] + b.data[i][j]);
    }

    public static Matrix add(final Matrix a, final double n) {
        return new Matrix(a.rows, a.cols, (i, j) -> a.data[i][j] + n);
    }

    public static Matrix sub(final Matrix a, final Matrix b) {
        return new Matrix(a.rows, a.cols, (i, j) -> a.data[i][j] - b.data[i][j]);
    }

    public static Matrix mul(final Matrix a, final Matrix b) {
        return new Matrix(a.rows, a.cols, (i, j) -> a.data[i][j] * b.data[i][j]);
    }

    public static Matrix mul(final Matrix a, final double n) {
        return new Matrix(a.rows, a.cols, (i, j) -> a.data[i][j] * n);
    }

    public static Matrix scale(final Matrix a, final double n) {
        return new Matrix(a.rows, a.cols, (i, j) -> a.data[i][j] * n);
    }

    public static Matrix product(final Matrix a, final Matrix b) {
        return new Matrix(
                a.rows,
                b.cols,
                (i, j) -> IntStream.range(0, a.cols)
                        .mapToObj(k -> a.data[i][k] * b.data[k][j])
                        .reduce(0.0 , Double::sum));
    }

    public static Matrix map(final Matrix a, final Function<Double, Double> f) {
        return new Matrix(a.rows, a.cols, (i, j) -> f.apply(a.data[i][j]));
    }

    public static Matrix transpose(final Matrix a) {
        return new Matrix(a.cols, a.rows, (i, j) -> a.data[j][i]);
    }

    private static void initMatrix(final double[][] arr, final BiFunction<Integer, Integer, Double> cb) {
        loop2D(arr, (r, c, e) -> arr[r][c] = cb.apply(r, c));
    }

    private static void loop2D(final double[][] arr, final TriConsumer<Integer, Integer, Double> cb) {
        for (int r = 0; r < arr.length; r++) {
            for (int c = 0; c < arr[r].length; c++) {
                cb.accept(r, c, arr[r][c]);
            }
        }
    }

    @FunctionalInterface
    private interface TriConsumer<A, B, C> {
        void accept(A a, B b, C c);
    }
}
