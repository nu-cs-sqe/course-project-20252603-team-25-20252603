package ui;

import domain.locale.LocaleManager;
import javax.swing.SwingUtilities;
import ui.swing.LocaleSelectionFrame;

/**
 * Application entry point. Shows the locale picker first; once the user
 * chooses, the selected locale becomes active for all subsequent UI text.
 * Subsequent setup screens (player count, names, board) will be wired in
 * later iterations.
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
        LocaleSelectionFrame dialog = new LocaleSelectionFrame(
            null,
            manager,
            locale -> {
                manager.setActiveLocale(locale);
                System.out.println(manager.get("app.title"));
            });
        dialog.setVisible(true);
    }
}
