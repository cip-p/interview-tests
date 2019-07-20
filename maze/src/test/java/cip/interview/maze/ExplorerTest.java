package cip.interview.maze;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import cip.interview.maze.exceptions.ExploringException;

import java.lang.reflect.Field;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static cip.interview.maze.Coordinate.coordinate;
import static cip.interview.maze.Direction.*;
import static cip.interview.maze.MazeCell.*;

@RunWith(MockitoJUnitRunner.class)
public class ExplorerTest {

    private Explorer explorer;

    @Mock
    private Maze maze;

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Before
    public void setUp() {
        when(maze.isReadyToBeUsed()).thenReturn(true);
    }

    @Test
    public void shouldNotBeAbleToExploreANullMaze() {
        exception.expect(ExploringException.class);
        exception.expectMessage("maze can't be explored, is null");

        new Explorer(null);
    }

    @Test
    public void shouldNotBeAbleToExploreAMazeWhichIsNotReadyToBeUsed() {
        exception.expect(ExploringException.class);
        exception.expectMessage("maze is not ready to be explored");

        when(maze.isReadyToBeUsed()).thenReturn(false);

        new Explorer(maze);
    }

    @Test
    public void shouldBeDroppedATStartPointWhenStartExploring() {
        Coordinate startPoint = coordinate(1, 2);
        when(maze.startPoint()).thenReturn(startPoint);

        explorer = new Explorer(maze);

        assertThat(explorer.currentPosition(), is(startPoint));
    }

    @Test
    public void shouldBeFacingNorthWhenStartExploring() {
        explorer = new Explorer(maze);

        assertThat(explorer.currentDirection(), is(NORTH));
    }

    @Test
    public void shouldMoveForwardIfThereIsNotAWall() {
        MazeCell[] cellsICanMoveOn = new MazeCell[]{EMPTY_SPACE, START, EXIT};

        for (MazeCell mazeCell : cellsICanMoveOn) {
            explorer = new Explorer(maze);
            setExplorerCurrentPosition(explorer, coordinate(1, 1));
            setExplorerCurrentDirection(explorer, NORTH);
            when(maze.get(coordinate(1, 2))).thenReturn(mazeCell);

            explorer.moveForward();

            assertThat("can't move on cell " + mazeCell, explorer.currentPosition(), is(coordinate(1, 2)));
        }
    }

    @Test
    public void shouldTurnLeft() {
        explorer = new Explorer(maze);
        setExplorerCurrentDirection(explorer, NORTH);

        explorer.turnLeft();
        assertThat(explorer.currentDirection(), is(WEST));

        explorer.turnLeft();
        assertThat(explorer.currentDirection(), is(SOUTH));

        explorer.turnLeft();
        assertThat(explorer.currentDirection(), is(EAST));

        explorer.turnLeft();
        assertThat(explorer.currentDirection(), is(NORTH));
    }

    @Test
    public void shouldTurnRight() {
        explorer = new Explorer(maze);
        setExplorerCurrentDirection(explorer, NORTH);

        explorer.turnRight();
        assertThat(explorer.currentDirection(), is(EAST));

        explorer.turnRight();
        assertThat(explorer.currentDirection(), is(SOUTH));

        explorer.turnRight();
        assertThat(explorer.currentDirection(), is(WEST));

        explorer.turnRight();
        assertThat(explorer.currentDirection(), is(NORTH));
    }

    @Test
    public void shouldDeclareWhatIsInFront() {
        explorer = new Explorer(maze);
        setExplorerCurrentDirection(explorer, NORTH);
        setExplorerCurrentPosition(explorer, coordinate(1, 1));
        when(maze.get(coordinate(1, 2))).thenReturn(EMPTY_SPACE);

        assertThat(explorer.whatIsInFront(), is(EMPTY_SPACE));
    }

    @Test
    public void shouldDeclareMovementOptions() {
        explorer = new Explorer(maze);
        setExplorerCurrentPosition(explorer, coordinate(1, 1));
        when(maze.get(coordinate(0, 1))).thenReturn(WALL);
        when(maze.get(coordinate(1, 0))).thenReturn(EMPTY_SPACE);
        when(maze.get(coordinate(1, 2))).thenReturn(EMPTY_SPACE);
        when(maze.get(coordinate(2, 1))).thenReturn(EXIT);

        assertEquals(3, explorer.movementOptions().size());
        assertThat(explorer.movementOptions(), hasItems(coordinate(1, 0), coordinate(1, 2), coordinate(2, 1)));
    }

    private void setExplorerCurrentPosition(Explorer explorer, Coordinate coordinate) {
        setFieldValue(explorer, "currentPosition", coordinate);
    }

    private void setExplorerCurrentDirection(Explorer explorer, Direction direction) {
        setFieldValue(explorer, "currentDirection", direction);
    }

    private void setFieldValue(Object theObject, String fieldName, Object fieldValue) {
        try {
            Field f1 = theObject.getClass().getDeclaredField(fieldName);
            f1.setAccessible(true);
            f1.set(theObject, fieldValue);
        } catch (Exception e) {
            throw new RuntimeException("unable to set field value", e);
        }
    }
}