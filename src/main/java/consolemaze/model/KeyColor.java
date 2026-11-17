package consolemaze.model;

/**
 * Identifies a key/door pair: a {@link Key} opens only a {@link Door} of the same color.
 * Purely logical, the UI decides how each constant is actually drawn.
 * The number of constants is also the maximum number of doors per level.
 */

public enum KeyColor {

    RED, BLUE, GREEN, YELLOW, PURPLE, ORANGE, CYAN

}
