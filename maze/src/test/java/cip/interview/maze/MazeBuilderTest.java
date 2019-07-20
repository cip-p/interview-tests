package cip.interview.maze;

import cip.interview.maze.exceptions.InvalidMazeInput;
import cip.interview.maze.exceptions.MazeFileNotFound;
import org.hamcrest.CoreMatchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class MazeBuilderTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private String validMazeFile = "Maze.txt";

    @Test
    public void shouldCreateMazeWhenInputFileIsValid() {

        Maze maze = MazeBuilder.createMazeFromFile(validMazeFile);

        assertThat(maze, is(notNullValue()));
        assertThat(maze.isReadyToBeUsed(), is(true));

        assertThat(maze.numberOfWalls(), is(149L));
        assertThat(maze.numberOfEmptySpaces(), is(74L));

        assertThat(maze.startPoint(), is(nullValue()));

        assertThat(maze.get(Coordinate.coordinate(3, 11)), CoreMatchers.is(MazeCell.START));
        assertThat(maze.get(Coordinate.coordinate(1, 0)), CoreMatchers.is(MazeCell.EXIT));
    }

    @Test
    public void shouldNotCreateMazeWhenInputFileDoesntExist() {
        String fileName = "maze-file-doesnt-exist.txt";
        exception.expect(MazeFileNotFound.class);
        exception.expectMessage("maze file not found 'maze-file-doesnt-exist.txt'");

        MazeBuilder.createMazeFromFile(fileName);
    }

    @Test
    public void shouldCreateMazeWhenInputTextIsValid() {
        String mazeAsText = "X S FX";

        Maze maze = MazeBuilder.crateMazeFromText(mazeAsText);

        assertThat(maze, is(notNullValue()));
    }

    @Test
    public void shouldNotCreateMazeWhenInputIsEmpty() {
        String mazeAsText = "";

        exception.expect(InvalidMazeInput.class);
        exception.expectMessage("input is null or empty");

        MazeBuilder.crateMazeFromText(mazeAsText);
    }

    @Test
    public void shouldNotCreateMazeWhenInputIsNull() {
        String mazeAsText = null;

        exception.expect(InvalidMazeInput.class);
        exception.expectMessage("input is null or empty");

        MazeBuilder.crateMazeFromText(mazeAsText);
    }
}