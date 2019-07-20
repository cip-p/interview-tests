package cip.interview.maze;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class CoordinateTest {

    @Test
    public void shouldReturnCoordinateAxes() {
        Coordinate coordinate = Coordinate.coordinate(2, 3);

        assertThat(coordinate.x(), is(2));
        assertThat(coordinate.y(), is(3));
    }

    @Test
    public void shouldMoveRightOnX() {
        Coordinate coordinate = Coordinate.coordinate(2, 3).xPlus();

        assertThat(coordinate, CoreMatchers.is(Coordinate.coordinate(3, 3)));
    }

    @Test
    public void shouldMoveLeftOnX() {
        Coordinate coordinate = Coordinate.coordinate(2, 3).xMinus();

        assertThat(coordinate, CoreMatchers.is(Coordinate.coordinate(1, 3)));
    }

    @Test
    public void shouldMoveUpOnY() {
        Coordinate coordinate = Coordinate.coordinate(2, 3).yPlus();

        assertThat(coordinate, CoreMatchers.is(Coordinate.coordinate(2, 4)));
    }

    @Test
    public void shouldMoveDownOnY() {
        Coordinate coordinate = Coordinate.coordinate(2, 3).yMinus();

        assertThat(coordinate, CoreMatchers.is(Coordinate.coordinate(2, 2)));
    }

}