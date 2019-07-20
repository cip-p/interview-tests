package cip.interview.maze;

import cip.interview.maze.exceptions.InvalidMazeInput;
import cip.interview.maze.exceptions.MazeFileNotFound;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;

import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.Comparator.comparingInt;
import static cip.interview.maze.Coordinate.coordinate;

public class MazeBuilder {

    public static Maze createMazeFromFile(String fileName) {
        String mazeAsText = loadMazeFile(fileName);

        return crateMazeFromText(mazeAsText);
    }

    public static Maze crateMazeFromText(String mazeAsText) {
        if (mazeAsText == null || mazeAsText.length() == 0) {
            throw new InvalidMazeInput("input is null or empty");
        }

        String[] lines = mazeAsText.split("\n");
        return createAndFillMazeAsXYAxes(lines);
    }

    private static Maze createAndFillMazeAsXYAxes(String[] lines) {
        int xAxis = stream(lines)
                .map(String::length)
                .max(comparingInt(l -> l)).get()
                - 1;

        int yAxis = lines.length - 1;
        Maze maze = new Maze(xAxis, yAxis);

        for (int i = yAxis; i >= 0; i--) {
            char[] rowChars = lines[i].toCharArray();
            for (int j = 0; j < rowChars.length; j++) {
                maze.set(coordinate(j, yAxis - i), rowChars[j]);
            }
        }

        maze.mazeCompleted();
        return maze;
    }

    private static String loadMazeFile(String fileName) {
        try {
            URL resource = ClassLoader.getSystemClassLoader().getResource(fileName);
            if (resource == null) {
                throw new MazeFileNotFound(format("maze file not found '%s'", fileName));
            }
            File file = new File(resource.getFile());
            return new String(Files.readAllBytes(file.toPath()));

        } catch (IOException e) {
            throw new RuntimeException(format("caught unexpected error while loading maze file '%s'", fileName), e);
        }
    }

}
