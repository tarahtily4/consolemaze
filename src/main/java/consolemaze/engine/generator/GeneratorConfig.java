package consolemaze.engine.generator;

import consolemaze.model.Maze;

/**
 * Tunable parameters of level generation (map size, room sizes and how many of each
 * object to spawn). The values are fixed in the constructor; change them here to rebalance the game.
 */

public class GeneratorConfig {
    private final int width;
    private final int height;
    private final int minRoomSize;
    private final int maxRoomSize;
    private final int maxRooms;
    private final int coinCount;
    private final int keyCount;
    private final int roomMargin;
    private final int ghostCount;
    private final int monkeyCount;
    private final int trapCount;
    private final int healCount;
    private final int healAmount;
    private final int boulderCount;
    private final int vegetationClusters;

    public GeneratorConfig(int width, int height) {
        this.width = Maze.clampSize(width);
        this.height = Maze.clampSize(height);
        this.minRoomSize = 6;
        this.maxRoomSize = 12;
        this.maxRooms = 10;
        this.coinCount = 16;
        this.keyCount = 7;
        this.roomMargin = 3;
        this.ghostCount = 2;
        this.monkeyCount = 3;
        this.trapCount = 3;
        this.healCount = 3;
        this.healAmount = 4;
        this.boulderCount = 5;
        this.vegetationClusters = 6;
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int getMinRoomSize() { return minRoomSize; }
    public int getMaxRoomSize() { return maxRoomSize; }
    public int getMaxRooms() { return maxRooms; }
    public int getCoinCount() { return coinCount; }
    public int getKeyCount() { return keyCount; }
    public int getRoomMargin() { return roomMargin; }
    public int getGhostCount() { return ghostCount; }
    public int getMonkeyCount() { return monkeyCount; }
    public int getTrapCount() { return trapCount; }
    public int getHealCount() { return healCount; }
    public int getHealAmount() { return healAmount; }
    public int getBoulderCount() { return boulderCount; }
    public int getVegetationClusters() { return vegetationClusters; }
}