package cip.interview.maze;

import cip.interview.maze.exceptions.InvalidMazeInput;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

public class Maze {

    private final Map<Coordinate, MazeCell> cells;
    private final int xAxis, yAxis;

    private boolean readyToBeUsed;

    public Maze(int xAxis, int yAxis) {
        if (xAxis < 0 || yAxis < 0) {
            throw new InvalidMazeInput("invalid maze coordinates");
        }

        this.xAxis = xAxis;
        this.yAxis = yAxis;
        cells = new HashMap<>(xAxis * yAxis);
    }

    Maze set(Coordinate coordinate, char symbol) {
        validateCoordinates(coordinate);

        cells.put(coordinate, MazeCell.mazeCellFrom(symbol));

        return this;
    }

    MazeCell get(Coordinate coordinate) {
        validateCoordinates(coordinate);

        return cells.get(coordinate);
    }

    Maze mazeCompleted() {

        Map<MazeCell, List<MazeCell>> cellsByType = cells.values().stream()
                .filter(cell -> cell == MazeCell.START || cell == MazeCell.EXIT)
                .collect(groupingBy(cell -> cell));

        validateStartPoints(cellsByType.get(MazeCell.START));
        validateExitPoints(cellsByType.get(MazeCell.EXIT));

        readyToBeUsed = true;

        return this;
    }

    Coordinate startPoint() {
        return null;
    }

    long numberOfWalls() {
        return countCells(MazeCell.WALL);
    }

    long numberOfEmptySpaces() {
        return countCells(MazeCell.EMPTY_SPACE);
    }

    boolean isReadyToBeUsed() {
        return readyToBeUsed;
    }

    private long countCells(MazeCell mazeCell) {
        return cells.values().stream()
                .filter(cell -> cell == mazeCell)
                .count();
    }

    private void validateStartPoints(List<MazeCell> startPoints) {
        if (startPoints == null || startPoints.size() != 1) {
            throw new InvalidMazeInput("a maze must have only one start point");
        }
    }

    private void validateExitPoints(List<MazeCell> exitPoints) {
        if (exitPoints == null || exitPoints.size() != 1) {
            throw new InvalidMazeInput("a maze must have only one exit point");
        }
    }

    private void validateCoordinates(Coordinate coordinate) {
        if (coordinate.x() < 0 || coordinate.x() > xAxis || coordinate.y() < 0 || coordinate.y() > yAxis) {
            throw new InvalidMazeInput("invalid maze coordinates");
        }
    }
}
