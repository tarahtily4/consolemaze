package consolemaze;

import consolemaze.ui.GameWindow;

import javax.swing.SwingUtilities;

// creates the game window on the Swing event dispatch thread.

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(GameWindow::new);

    }

}