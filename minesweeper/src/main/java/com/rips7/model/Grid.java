package com.rips7.model;

import com.rips7.sketchkit.render.Sketch;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public record Grid(Cell[][] cells) {

    private static Cell[][] initGrid(final int rows, final int cols, final double w, final double offset, final double mineProbability) {
        return IntStream.range(0, rows)
                .mapToObj(row -> IntStream.range(0, cols)
                        .mapToObj(col -> new Cell(row, col, w, offset, mineProbability))
                        .toArray(Cell[]::new))
                .toArray(Cell[][]::new);
    }

    public Grid(final int rows, final int cols, final double w, final double offset, final double mineProbability) {
        this(initGrid(rows, cols, w, offset, mineProbability));
        updateMineCount();
    }

    public void reveal(final int x, final int y) throws MineHitException {
        final Cell clickedCell = findCell(x, y).orElse(null);
        reveal(clickedCell);
    }

    public void reveal(final Cell cell) throws MineHitException {
        if (cell == null) return;
        if (cell.revealed()) return;
        cell.reveal();
        if (cell.mine()) throw new MineHitException();
        if (cell.neighboringMines() != 0) return;
        for (int xoff = -1; xoff <= 1; xoff++) {
            for (int yoff = -1; yoff <= 1; yoff++) {
                final int nr = cell.row() + xoff;
                final int nc = cell.col() + yoff;
                final Optional<Cell> nCell = getCell(nr, nc);
                if (nCell.isPresent()) {
                    reveal(nCell.get());
                }
            }
        }
    }

    public void revealAll() {
        loopGrid((r, c, cell) -> cell.reveal());
    }

    public void flag(final int x, final int y) {
        findCell(x, y).ifPresent(Cell::flag);
    }

    public boolean checkComplete() {
        return Arrays.stream(cells).flatMap(Arrays::stream)
                .filter(cell -> !cell.mine())
                .allMatch(Cell::revealed);
    }

    public void show(final Sketch s) {
        loopGrid((i, c, cell) -> cell.show(s));
    }

    private Optional<Cell> findCell(final int x, final int y) {
        return Arrays.stream(cells)
                .flatMap(Arrays::stream)
                .filter(cell -> cell.contains(x, y))
                .findFirst();
    }

    private void loopGrid(final TriConsumer<Integer, Integer, Cell> cb) {
        IntStream.range(0, cells.length).forEach(i ->
                IntStream.range(0, cells[i].length).forEach(j ->
                        cb.accept(i, j, cells[i][j])));
    }

    private void updateMineCount() {
        loopGrid((r, c, cell) -> {
            if (cell.mine()) return;
            cell.neighboringMines(countNeighboringMines(r, c));
        });
    }

    private int countNeighboringMines(final int row, final int col) {
        // Assumes current cell at [row][col] has already been checked for whether it is a mine or not, and it's not one
        AtomicInteger count = new AtomicInteger(0);
        for (int xoff = -1; xoff <= 1; xoff++) {
            for (int yoff = -1; yoff <= 1; yoff++) {
                final int nr = row + xoff;
                final int nc = col + yoff;
                getCell(nr, nc).ifPresent(n -> { if (n.mine()) count.incrementAndGet(); });
            }
        }
        return count.get();
    }

    private Optional<Cell> getCell(final int row, final int col) {
        if (row < 0 || col < 0 || row > cells.length - 1 || col > cells[0].length - 1) {
            return Optional.empty();
        }
        return Optional.of(cells[row][col]);
    }

    @FunctionalInterface
    private interface TriConsumer<A, B, C> {
        void accept(A a, B b, C c);
    }

}
