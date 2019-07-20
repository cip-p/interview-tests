package cip.interview.maze;

import java.util.Objects;

public class Coordinate {

    private final int x;
    private final int y;


    static Coordinate coordinate(int x, int y) {
        return new Coordinate(x, y);
    }

    private Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    Coordinate xPlus() {
        return coordinate(x + 1, y);
    }

    Coordinate xMinus() {
        return coordinate(x - 1, y);
    }

    Coordinate yPlus() {
        return coordinate(x, y + 1);
    }

    Coordinate yMinus() {
        return coordinate(x, y - 1);
    }

    int x() {
        return x;
    }

    int y() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate that = (Coordinate) o;
        return x == that.x &&
                y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Coordinate{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
