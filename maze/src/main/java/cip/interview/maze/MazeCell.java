package cip.interview.maze;

import cip.interview.maze.exceptions.InvalidMazeInput;

import static java.lang.String.format;

public enum MazeCell {

    START('S'),
    EXIT('F'),
    WALL('X'),
    EMPTY_SPACE(' ');

    private char symbol;

    MazeCell(char symbol) {
        this.symbol = symbol;
    }

    public static MazeCell mazeCellFrom(char from) {
        for (MazeCell mazeCell : values()) {
            if (mazeCell.symbol == from) {
                return mazeCell;
            }
        }
        throw new InvalidMazeInput(format("invalid maze symbol '%s'", from));
    }

    char symbol() {
        return symbol;
    }
}
