package cip.interview.maze;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import cip.interview.maze.exceptions.InvalidMazeInput;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static cip.interview.maze.Coordinate.coordinate;
import static cip.interview.maze.MazeCell.*;

public class MazeTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private static final char START_SYMBOL = START.symbol();
    private static final char EXIT_SYMBOL = EXIT.symbol();
    private static final char WALL_SYMBOL = WALL.symbol();
    private static final char EMPTY_SPACE_SYMBOL = EMPTY_SPACE.symbol();

    @Test
    public void shouldCreateMazeWhenCoordinatesAreValid() {
        Maze maze = new Maze(1, 2);

        assertThat(maze, is(notNullValue()));
    }

    @Test
    public void shouldNotCreateMazeWhen_X_CoordinateIsLessThanZero() {
        exception.expect(InvalidMazeInput.class);
        exception.expectMessage("invalid maze coordinates");

        new Maze(-1, 2);
    }

    @Test
    public void shouldNotCreateMazeWhen_Y_CoordinateIsLessThanZero() {
        exception.expect(InvalidMazeInput.class);
        exception.expectMessage("invalid maze coordinates");

        new Maze(2, -1);
    }

    @Test
    public void shouldNotBeReadyToBeUsedIfIsNotCompleted() {
        Maze maze = anyMaze();

        assertThat(maze.isReadyToBeUsed(), is(false));
    }

    @Test
    public void shouldBeReadyToBeUsedIfIsCompleted() {
        Maze maze = new Maze(5, 1)
                .set(coordinate(0, 0), WALL_SYMBOL)
                .set(coordinate(1, 0), START_SYMBOL)
                .set(coordinate(2, 0), EMPTY_SPACE_SYMBOL)
                .set(coordinate(3, 0), EXIT_SYMBOL)
                .set(coordinate(4, 0), WALL_SYMBOL)
                .mazeCompleted();

        assertThat(maze.isReadyToBeUsed(), is(true));
    }

    @Test
    public void shouldNotAllowUnknownSymbolsWhenCreateMaze() {
        exception.expect(InvalidMazeInput.class);
        exception.expectMessage("invalid maze symbol 'A'");

        anyMaze()
                .set(coordinate(1, 1), 'A');
    }

    @Test
    public void shouldHaveOnlyOneStartPointNotNone() {
        exception.expect(InvalidMazeInput.class);
        exception.expectMessage("a maze must have only one start point");

        anyMaze()
                .mazeCompleted();
    }

    @Test
    public void shouldHaveOnlyOneStartPointNotMany() {
        exception.expect(InvalidMazeInput.class);
        exception.expectMessage("a maze must have only one start point");

        anyMaze()
                .set(coordinate(1, 1), START_SYMBOL)
                .set(coordinate(1, 2), START_SYMBOL)
                .mazeCompleted();
    }

    @Test
    public void shouldHaveOnlyOneExitPointNotNone() {
        exception.expect(InvalidMazeInput.class);
        exception.expectMessage("a maze must have only one exit point");

        anyMaze()
                .set(coordinate(1, 1), START_SYMBOL)
                .mazeCompleted();
    }

    @Test
    public void shouldHaveOnlyOneExitPointNotMany() {
        exception.expect(InvalidMazeInput.class);
        exception.expectMessage("a maze must have only one exit point");

        anyMaze()
                .set(coordinate(1, 1), START_SYMBOL)
                .set(coordinate(1, 2), EXIT_SYMBOL)
                .set(coordinate(1, 3), EXIT_SYMBOL)
                .mazeCompleted();
    }

    @Test
    public void shouldNotBeAbleToSetMazeCellOutsideIts_X_Coordinates() {
        exception.expect(InvalidMazeInput.class);
        exception.expectMessage("invalid maze coordinates");

        Maze maze = new Maze(2, 2);
        maze.set(coordinate(3, 0), WALL_SYMBOL);
    }

    @Test
    public void shouldNotBeAbleToSetMazeCellOutsideIts_Y_Coordinates() {
        exception.expect(InvalidMazeInput.class);
        exception.expectMessage("invalid maze coordinates");

        Maze maze = new Maze(2, 2);
        maze.set(coordinate(0, 3), WALL_SYMBOL);
    }

    @Test
    public void shouldBeAbleToGetTheNumberOfWalls() {
        Maze maze = anyMaze()
                .set(coordinate(1, 1), WALL_SYMBOL)
                .set(coordinate(1, 2), START_SYMBOL)
                .set(coordinate(1, 3), WALL_SYMBOL);

        assertThat(maze.numberOfWalls(), is(2L));
    }

    @Test
    public void shouldBeAbleToGetTheNumberOfEmptySpaces() {
        Maze maze = anyMaze()
                .set(coordinate(1, 1), WALL_SYMBOL)
                .set(coordinate(1, 2), EMPTY_SPACE_SYMBOL)
                .set(coordinate(1, 3), EMPTY_SPACE_SYMBOL);

        assertThat(maze.numberOfEmptySpaces(), is(2L));
    }

    @Test
    public void shouldBeAbleToRetrieveMazeCellByItsCoordinates() {
        Maze maze = anyMaze()
                .set(coordinate(1, 1), WALL_SYMBOL)
                .set(coordinate(1, 2), EMPTY_SPACE_SYMBOL);

        assertThat(maze.get(coordinate(1, 1)), is(WALL));
        assertThat(maze.get(coordinate(1, 2)), is(EMPTY_SPACE));
        assertThat(maze.get(coordinate(0, 0)), is(nullValue()));
    }

    @Test
    public void shouldNotBeAbleToRetrieveMazeCellWhen_X_CoordinatesAreInvalid() {
        exception.expect(InvalidMazeInput.class);
        exception.expectMessage("invalid maze coordinates");

        Maze maze = new Maze(2, 2);

        maze.get(coordinate(3, 0));
    }

    @Test
    public void shouldNotBeAbleToRetrieveMazeCellWhen_Y_CoordinatesAreInvalid() {
        exception.expect(InvalidMazeInput.class);
        exception.expectMessage("invalid maze coordinates");

        Maze maze = new Maze(2, 2);

        maze.get(coordinate(0, 3));
    }

    private Maze anyMaze() {
        return new Maze(10, 10);
    }
}