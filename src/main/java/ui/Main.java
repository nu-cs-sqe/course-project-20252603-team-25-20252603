package ui;

import domain.locale.LocaleManager;
import domain.setup.Game;
import javax.swing.SwingUtilities;
import ui.swing.BoardFrame;
import ui.swing.LocaleSelectionFrame;
import ui.swing.PlayerSetupFrame;

/**
 * Application entry point: locale selection, then player setup, then the
 * {@link BoardFrame} stub once {@link domain.setup.GameSetup} finishes.
 */
public final class Main {

    private Main() {
    }

    /**
     * Boots the Swing UI on the event-dispatch thread.
     *
     * @param args command-line arguments (unused)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::launch);
    }

    private static void launch() {
        LocaleManager manager = LocaleManager.getInstance();
        LocaleSelectionFrame localeDialog = new LocaleSelectionFrame(
            null,
            manager,
            locale -> {
                manager.setActiveLocale(locale);
                showPlayerSetup(manager);
            });
        localeDialog.setVisible(true);
    }

    private static void showPlayerSetup(LocaleManager manager) {
        PlayerSetupFrame setupDialog = new PlayerSetupFrame(
            null,
            manager,
            game -> showBoardFrame(manager, game));
        setupDialog.setVisible(true);
    }

    private static void showBoardFrame(LocaleManager manager, Game game) {
        BoardFrame frame = new BoardFrame(manager, game);
        frame.setVisible(true);
    }
}
