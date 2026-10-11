package com.rips7.model;

import com.rips7.sketchkit.render.Sketch;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class Maze {
    private final Grid grid;

    private Stack<Cell> generatingStack;
    private Cell generatingCurrent;

    private Queue<AStarNode> solvingOpenSet;
    private Set<Cell> solvingClosedSet;
    private HashMap<Cell, Cell> solvingCameFrom;
    private List<Cell> solvingPath;
    private Cell solvingEnd;
    private AStarNode solvingCurrent;

    private State state;

    public Maze(final int rows, final int cols, final double res) {
        this.grid = new Grid(rows, cols, res);
        this.state = null;
    }

    public void generate() {
        if (this.state != null) {
            throw new IllegalStateException("Cannot generate maze - current state is %s".formatted(this.state));
        }
        this.generatingStack = new Stack<>();

        this.generatingCurrent = grid.topLeft();
        this.generatingCurrent.visit();

        this.state = State.GENERATING;
    }

    public boolean isGenerated() {
        return this.state == State.FINISHED_GENERATING;
    }

    public void solve() {
        if (this.state != State.FINISHED_GENERATING) {
            throw new IllegalStateException(
                    "Cannot solve maze - current state is %s".formatted(this.state)
            );
        }

        this.solvingEnd = grid.bottomRight();
        this.solvingOpenSet = new PriorityQueue<>(Comparator.comparingDouble(AStarNode::f));
        this.solvingCameFrom = new HashMap<>();
        this.solvingClosedSet = new HashSet<>();
        this.solvingPath = new ArrayList<>();

        final Cell startCell = grid.topLeft();
        final AStarNode start = new AStarNode(startCell);

        start.g(0);
        start.f(h(startCell, solvingEnd));

        this.solvingOpenSet.add(start);
        this.state = State.SOLVING;
    }

    public boolean isSolved() {
        return this.state == State.FINISHED_SOLVING;
    }

    public void update() {
        switch (this.state) {
            case GENERATING:
                generateStep();
                break;
            case SOLVING:
                solveStep();
                break;
        }
    }

    public void show(final Sketch s) {
        grid.show(s);

        if (state == State.GENERATING || state == State.FINISHED_GENERATING) {
            generatingCurrent.highlight(s, new Color(0, 0, 255, 100));
        } else if (state == State.SOLVING) {
            solvingOpenSet.forEach(node -> node.cell().highlight(s, new Color(0, 255, 0, 100)));
            solvingCameFrom.keySet().forEach(cell -> cell.highlight(s, new Color(255, 0, 0, 100)));
        } else if (state == State.FINISHED_SOLVING) {
            solvingPath.forEach(cell -> cell.highlight(s, new Color(0, 100, 255, 100)));
        }
    }

    private void generateStep() {
        grid.neighbors(generatingCurrent).stream()
                .filter(cell -> !cell.isVisited())
                .collect(Collectors.collectingAndThen(
                        toList(),
                        list -> {
                            Collections.shuffle(list);
                            return list.stream();
                        }))
                .findFirst()
                .map(Cell::visit)
                .ifPresentOrElse(
                    (next) -> {
                        generatingCurrent.removeWalls(next);
                        generatingStack.push(generatingCurrent);
                        generatingCurrent = next;
                    },
                    () -> {
                        if (generatingStack.isEmpty()) {
                            state = State.FINISHED_GENERATING;
                            return;
                        }
                        generatingCurrent = generatingStack.pop();
                    });
    }

    private void solveStep() {
        if (solvingOpenSet.isEmpty()) {
            state = State.FINISHED_SOLVING;
            return;
        }

        solvingCurrent = solvingOpenSet.poll();

        if (solvingCurrent.cell().equals(solvingEnd)) {
            reconstructPath();
            state = State.FINISHED_SOLVING;
            return;
        }

        solvingClosedSet.add(solvingCurrent.cell());

        grid.connectedNeighbors(solvingCurrent.cell()).stream()
                .filter(neighbor -> !solvingClosedSet.contains(neighbor))
                .forEach(neighbor -> {
                    final double tentativeG = solvingCurrent.g() + 1;
                    solvingOpenSet.stream()
                            .filter(node -> node.cell().equals(neighbor))
                            .findFirst()
                            .ifPresentOrElse(
                                (existing) -> {
                                    if (tentativeG < existing.g()) {
                                        solvingOpenSet.remove(existing);
                                        existing.g(tentativeG);
                                        existing.f(tentativeG + h(neighbor, solvingEnd));
                                        solvingOpenSet.add(existing);
                                        solvingCameFrom.put(neighbor, solvingCurrent.cell());
                                    }
                                },
                                () -> {
                                    final AStarNode newNode = new AStarNode(neighbor);
                                    newNode.g(tentativeG);
                                    newNode.f(tentativeG + h(neighbor, solvingEnd));
                                    solvingOpenSet.add(newNode);
                                    solvingCameFrom.put(neighbor, solvingCurrent.cell());
                                });
                });
    }

    private double h(final Cell a, final Cell b) {
        return Math.abs(b.row() - a.row()) + Math.abs(b.col() - a.col());
    }

    private void reconstructPath() {
        solvingPath.clear();

        Cell current = solvingEnd;
        while (current != null) {
            solvingPath.add(current);
            current = solvingCameFrom.get(current);
        }

        Collections.reverse(solvingPath);
    }

    private enum State {
        GENERATING,
        FINISHED_GENERATING,
        SOLVING,
        FINISHED_SOLVING
    }
}
