package ui;

import domain.locale.LocaleManager;
import domain.setup.Game;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import ui.swing.LocaleSelectionFrame;
import ui.swing.PlayerSetupFrame;

/**
 * Application entry point: locale selection, then player setup, then a ready
 * message when {@link domain.setup.GameSetup} finishes.
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
            game -> showGameReady(manager, game));
        setupDialog.setVisible(true);
    }

    private static void showGameReady(LocaleManager manager, Game game) {
        String starter = game.turnOrder().current().getName();
        JOptionPane.showMessageDialog(
            null,
            manager.get("setup.ready", starter),
            manager.get("app.title"),
            JOptionPane.INFORMATION_MESSAGE);
    }
}
