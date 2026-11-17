package consolemaze.ui;

import consolemaze.engine.MazeEngine;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Translates keyboard events into engine calls: player movement (WASD or arrows) and the
 * end-of-game menu (restart / exit).
 *
 * It tracks the set of currently held keys, so two keys can be combined for diagonal
 * movement. A step is made on every key-press event, so the speed follows the OS key-repeat rate.
 * Collaborators are passed in as callbacks to keep this class independent of the window.
 */
final class InputHandler extends KeyAdapter {

    private static final int[] RIGHT_KEYS = {KeyEvent.VK_D, KeyEvent.VK_RIGHT};
    private static final int[] LEFT_KEYS = {KeyEvent.VK_A, KeyEvent.VK_LEFT};
    private static final int[] DOWN_KEYS = {KeyEvent.VK_S, KeyEvent.VK_DOWN};
    private static final int[] UP_KEYS = {KeyEvent.VK_W, KeyEvent.VK_UP};

    private final Supplier<MazeEngine> engineSupplier;
    private final Runnable onPlayerMoved;
    private final Runnable onRestart;
    private final Runnable onExit;

    private final Set<Integer> pressedKeys = new HashSet<>();

    InputHandler(Supplier<MazeEngine> engineSupplier, Runnable onPlayerMoved, Runnable onRestart, Runnable onExit) {
        this.engineSupplier = engineSupplier;
        this.onPlayerMoved = onPlayerMoved;
        this.onRestart = onRestart;
        this.onExit = onExit;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        MazeEngine engine = engineSupplier.get();

        if (engine.isGameOver() || engine.isGameWon()) {
            handleEndScreenKey(e.getKeyCode());
            return;
        }

        pressedKeys.add(e.getKeyCode());
        movePlayer(engine);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressedKeys.remove(e.getKeyCode());
    }

    private void handleEndScreenKey(int keyCode) {
        if (keyCode == KeyEvent.VK_R) {
            pressedKeys.clear();
            onRestart.run();
        } else if (keyCode == KeyEvent.VK_ESCAPE) {
            onExit.run();
        }
    }

    private void movePlayer(MazeEngine engine) {
        int dx = axisDelta(RIGHT_KEYS, LEFT_KEYS);
        int dy = axisDelta(DOWN_KEYS, UP_KEYS);

        if (dx != 0 || dy != 0) {
            engine.processPlayerMove(dx, dy);
            onPlayerMoved.run();
        }
    }

    private int axisDelta(int[] positiveKeys, int[] negativeKeys) {
        int delta = 0;
        if (anyPressed(positiveKeys)) delta++;
        if (anyPressed(negativeKeys)) delta--;
        return delta;
    }

    private boolean anyPressed(int[] keys) {
        for (int key : keys) {
            if (pressedKeys.contains(key)) return true;
        }
        return false;
    }
}