package cip.interview.maze;

import cip.interview.maze.exceptions.ExploringException;

import java.util.ArrayList;
import java.util.List;

import static cip.interview.maze.MazeCell.EMPTY_SPACE;
import static cip.interview.maze.MazeCell.EXIT;

public class Explorer {

    private final Maze maze;

    private Coordinate currentPosition;
    private Direction currentDirection;

    public Explorer(Maze maze) {
        if (maze == null) {
            throw new ExploringException("maze can't be explored, is null");
        }

        if (!maze.isReadyToBeUsed()) {
            throw new ExploringException("maze is not ready to be explored");
        }

        this.maze = maze;
        this.currentPosition = maze.startPoint();
        this.currentDirection = Direction.NORTH;
    }

    Coordinate currentPosition() {
        return currentPosition;
    }

    Direction currentDirection() {
        return currentDirection;
    }

    void moveForward() {
        Coordinate frontCoordinate = nextPosition();
        MazeCell nextPosition = maze.get(frontCoordinate);
        switch (nextPosition) {
            case EMPTY_SPACE:
            case START:
            case EXIT:
                currentPosition = frontCoordinate;
                break;
        }
    }

    private Coordinate nextPosition() {
        switch (currentDirection) {
            case NORTH:
                return currentPosition.yPlus();
            case SOUTH:
                return currentPosition.yMinus();
            case EAST:
                return currentPosition.xPlus();
            case WEST:
                return currentPosition.xMinus();
        }
        return null;
    }

    void turnLeft() {
        Direction newDirection = null;
        switch (currentDirection) {
            case NORTH:
                newDirection = Direction.WEST;
                break;
            case SOUTH:
                newDirection = Direction.EAST;
                break;
            case EAST:
                newDirection = Direction.NORTH;
                break;
            case WEST:
                newDirection = Direction.SOUTH;
                break;
        }
        currentDirection = newDirection;
    }

    void turnRight() {
        Direction newDirection = null;
        switch (currentDirection) {
            case NORTH:
                newDirection = Direction.EAST;
                break;
            case SOUTH:
                newDirection = Direction.WEST;
                break;
            case EAST:
                newDirection = Direction.SOUTH;
                break;
            case WEST:
                newDirection = Direction.NORTH;
                break;
        }
        currentDirection = newDirection;
    }

    MazeCell whatIsInFront() {
        return maze.get(nextPosition());
    }

    List<Coordinate> movementOptions() {
        List<Coordinate> movements = new ArrayList<>();

        Coordinate coordinate = currentPosition.xMinus();
        if (isAMovementOption(coordinate)) {
            movements.add(coordinate);
        }

        coordinate = currentPosition.xPlus();
        if (isAMovementOption(coordinate)) {
            movements.add(coordinate);
        }

        coordinate = currentPosition.yMinus();
        if (isAMovementOption(coordinate)) {
            movements.add(coordinate);
        }

        coordinate = currentPosition.yPlus();
        if (isAMovementOption(coordinate)) {
            movements.add(coordinate);
        }

        return movements;
    }

    private boolean isAMovementOption(Coordinate coordinate) {
        MazeCell mazeCell = maze.get(coordinate);
        return mazeCell != null && (mazeCell == EMPTY_SPACE || mazeCell == EXIT);
    }
}
