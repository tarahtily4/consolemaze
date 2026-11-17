package consolemaze.engine;

import consolemaze.model.GameObject;
import consolemaze.model.Position;

/**
 * Immutable data of one attempt by the player to enter a trigger cell: the target
 * object, its position and the movement direction (needed e.g. to push a boulder).
 * Package-private: only the engine and its rules use it.
 */

final class InteractionContext {

    private final GameObject target;
    private final Position position;
    private final int dx;
    private final int dy;

    InteractionContext(GameObject target, Position position, int dx, int dy) {
        this.target = target;
        this.position = position;
        this.dx = dx;
        this.dy = dy;
    }

    GameObject getTarget() { return target; }
    Position getPosition() { return position; }
    int getDx() { return dx; }
    int getDy() { return dy; }
}