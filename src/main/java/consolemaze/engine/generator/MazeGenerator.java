package consolemaze.engine.generator;

import consolemaze.engine.LevelData;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Entry point of level generation. It keeps the registered {@link MapGenerator}
 * algorithms and picks one at random for every new level.
 */

public class MazeGenerator {

    private final int width;
    private final int height;
    private final List<MapGenerator> generators = new ArrayList<>();
    private final Random random = new Random();

    public MazeGenerator(int width, int height) {

        this.width = width;
        this.height = height;

        generators.add(new BspMazeGenerator());

    }

    public LevelData generate() {

        MapGenerator selectedGenerator = generators.get(random.nextInt(generators.size()));
        return selectedGenerator.generate(width, height);

    }
}