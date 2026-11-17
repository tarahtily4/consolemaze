package consolemaze.ui;

import consolemaze.engine.LevelData;
import consolemaze.engine.MazeEngine;
import consolemaze.engine.generator.MazeGenerator;

import javax.swing.JFrame;
import javax.swing.Timer;

/**
 * The application window and the game loop. A Swing {@link Timer} fires on the event
 * dispatch thread, so the game tick, input handling and painting never run concurrently
 * and no extra synchronization is needed. Restarting swaps the engine but keeps the window.
 */
public class GameWindow extends JFrame {

    private static final int MAP_WIDTH = 80;
    private static final int MAP_HEIGHT = 50;

    private MazeEngine engine;
    private final GamePanel panel;
    private final Timer gameTimer;

    public GameWindow() {

        this.engine = createEngine();
        this.panel = new GamePanel(engine);

        configureWindow();
        add(panel);

        panel.addKeyListener(new InputHandler(

                () -> engine,
                panel::repaint,
                this::restartGame,
                this::exitGame

        ));

        gameTimer = new Timer(UiConfig.GAME_TICK_MS, e -> onTick());

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        panel.requestFocusInWindow();
        gameTimer.start();

    }

    private void configureWindow() {

        setTitle("Console Maze - Clean Camera Edition");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

    }

    private void onTick() {

        engine.tick();
        panel.repaint();

    }

    private void restartGame() {

        engine = createEngine();
        panel.setEngine(engine);

    }

    private void exitGame() {

        gameTimer.stop();
        dispose();
        System.exit(0);

    }

    private static MazeEngine createEngine() {

        LevelData level = new MazeGenerator(MAP_WIDTH, MAP_HEIGHT).generate();
        return new MazeEngine(level.getMaze(), level.getPlayer(), level.getEnemies());

    }
}