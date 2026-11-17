package consolemaze.engine.generator;

import consolemaze.model.Maze;
import consolemaze.model.Position;
import consolemaze.model.Room;
import consolemaze.model.Tile;
import consolemaze.model.Wall;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** Geometry helpers shared by the spawners. Package-private, not meant for use outside the generator. */
final class GeneratorUtils {

    private GeneratorUtils() {
    }

    static List<Position> orthogonalNeighbors(Position p) {
        List<Position> list = new ArrayList<>(4);
        list.add(new Position(p.getX() + 1, p.getY()));
        list.add(new Position(p.getX() - 1, p.getY()));
        list.add(new Position(p.getX(), p.getY() + 1));
        list.add(new Position(p.getX(), p.getY() - 1));
        return list;
    }

    static boolean isStrictNarrowCorridor(Maze maze, Position pos) {
        int x = pos.getX();
        int y = pos.getY();

        boolean leftIsWall = maze.getObjectAt(new Position(x - 1, y)) instanceof Wall;
        boolean rightIsWall = maze.getObjectAt(new Position(x + 1, y)) instanceof Wall;
        boolean upIsWall = maze.getObjectAt(new Position(x, y - 1)) instanceof Wall;
        boolean downIsWall = maze.getObjectAt(new Position(x, y + 1)) instanceof Wall;

        return (leftIsWall && rightIsWall && !upIsWall && !downIsWall) ||
                (upIsWall && downIsWall && !leftIsWall && !rightIsWall);
    }

    static Position findBottleneckForRoom(Maze maze, Room room) {
        Set<Position> roomTiles = new HashSet<>(room.getTiles());

        for (Position pos : room.getTiles()) {
            for (Position neighborPos : orthogonalNeighbors(pos)) {
                if (maze.isInside(neighborPos) && !roomTiles.contains(neighborPos)) {
                    if (maze.getObjectAt(neighborPos) instanceof Tile && isStrictNarrowCorridor(maze, neighborPos)) {
                        return neighborPos;
                    }
                }
            }
        }
        return null;
    }
}