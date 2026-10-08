package com.rips7.util;

import com.rips7.sketchkit.maths.Maths;

import java.util.ArrayList;
import java.util.List;

public final class DFT {

    private DFT() {
        // Do not instantiate
    }

    public record Result(double re, double im, double freq, double amp, double phase) {}

    public static List<Result> apply(final List<Double> x) {
        final List<Result> X = new ArrayList<>();
        final int N = x.size();
        for (int k = 0; k < N; k++) {
            double re = 0;
            double im = 0;
            for (int n = 0; n < N; n++) {
                final double phi = Maths.TWO_PI * k * n / N;
                re += x.get(n) * Math.cos(phi);
                im -= x.get(n) * Math.sin(phi);
            }
            re /= N;
            im /= N;
            final double freq = k <= N / 2 ? k : k - N;
            final double amp = Math.sqrt(re * re + im * im);
            final double phase = Math.atan2(im, re);
            X.add(new Result(re, im, freq, amp, phase));
        }
        return X;
    }
}
