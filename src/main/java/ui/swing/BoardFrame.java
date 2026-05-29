package ui.swing;

import domain.locale.LocaleManager;
import domain.player.Player;
import domain.setup.Game;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Top-level frame shown after {@link domain.setup.GameSetup#build()} completes.
 * Stub for the upcoming board UI: renders the setup-complete state (starter
 * player, player list, placeholder text) and contains no game-play logic. All
 * visible text is resolved through {@link LocaleManager}.
 */
public final class BoardFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private static final int LAYOUT_GAP = 8;
    private static final String PLAYER_SEPARATOR = ", ";

    /**
     * Builds the frame. Construct on the Swing event-dispatch thread.
     *
     * @param localeManager active locale and message bundles
     * @param game          fully built game produced by {@link domain.setup.GameSetup}
     */
    public BoardFrame(LocaleManager localeManager, Game game) {
        super();
        Objects.requireNonNull(localeManager, "localeManager must not be null");
        Objects.requireNonNull(game, "game must not be null");
        setTitle(localeManager.get("board.title"));
        setLayout(new BorderLayout(LAYOUT_GAP, LAYOUT_GAP));
        add(buildSummaryPanel(localeManager, game), BorderLayout.CENTER);
        add(new JLabel(localeManager.get("board.placeholder")), BorderLayout.SOUTH);
        SwingLocaleSupport.applyLocaleFont(this);
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private static JPanel buildSummaryPanel(LocaleManager localeManager, Game game) {
        JPanel panel = new JPanel(new GridLayout(0, 1, LAYOUT_GAP, LAYOUT_GAP));
        String starter = game.turnOrder().current().getName();
        panel.add(new JLabel(localeManager.get("board.starter", starter)));
        String names = joinPlayerNames(game.players());
        panel.add(new JLabel(localeManager.get("board.players", names)));
        return panel;
    }

    private static String joinPlayerNames(List<Player> players) {
        return players.stream()
            .map(Player::getName)
            .collect(Collectors.joining(PLAYER_SEPARATOR));
    }
}
