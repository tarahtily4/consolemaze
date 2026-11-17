package consolemaze.engine.generator;

import consolemaze.engine.LevelData;

/**
 * Strategy interface for level generation algorithms. An implementation produces a
 * complete, ready-to-play {@link LevelData} (map, player and enemies) for the requested size.
 */

public interface MapGenerator {

    LevelData generate(int width, int height);

}
