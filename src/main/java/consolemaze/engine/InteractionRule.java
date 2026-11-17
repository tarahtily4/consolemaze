package consolemaze.engine;

/**
 * Strategy that defines what happens when the player walks into a particular kind of
 * trigger object. Rules are registered per object class in {@link MazeEngine}.
 */

@FunctionalInterface
interface InteractionRule {

    /** @return true, если после взаимодействия игрок может войти в клетку */
    boolean apply(InteractionContext context);
}