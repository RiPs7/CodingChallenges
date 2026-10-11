package com.rips7.model;

import com.rips7.sketchkit.render.Sketch;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public record Grid(Cell[][] cells) {

    private static final List<BiFunction<Cell[][], Cell, Optional<Cell>>> neighborGetters = List.of(
            (g, c) -> getCell(g, c.row() - 1, c.col()),
            (g, c) -> getCell(g, c.row(), c.col() + 1),
            (g, c) -> getCell(g, c.row() + 1, c.col()),
            (g, c) -> getCell(g, c.row(), c.col() - 1));

    public Grid(final int rows, final int cols, final double res) {
        this(initGrid(rows, cols, res));
    }

    public Cell topLeft() {
        return cells[0][0];
    }

    public Cell bottomRight() {
        return cells[cells.length - 1][cells[cells.length - 1].length - 1];
    }

    public List<Cell> neighbors(final Cell current) {
        return neighbors(current, cell -> true);
    }

    public List<Cell> connectedNeighbors(final Cell current) {
        return neighbors(current, current::isConnectedTo);
    }

    public List<Cell> neighbors(final Cell current, final Predicate<Cell> filter) {
        return neighborGetters.stream()
                .map(ng -> ng.apply(cells, current))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(filter)
                .toList();
    }

    public void show(final Sketch s) {
        for (final Cell[] row : cells) {
            for (final Cell cell : row) {
                cell.show(s);
            }
        }
    }

    private static Cell[][] initGrid(final int rows, final int cols, final double res) {
        return IntStream.range(0, rows)
                .mapToObj(row -> IntStream.range(0, cols)
                        .mapToObj(col -> new Cell(row, col, res))
                        .toArray(Cell[]::new))
                .toArray(Cell[][]::new);
    }

    private static Optional<Cell> getCell(final Cell[][] cells, final int row, final int col) {
        if (row < 0 || col < 0 || row > cells.length - 1 || col > cells[0].length - 1) {
            return Optional.empty();
        }
        return Optional.of(cells[row][col]);
    }

}
