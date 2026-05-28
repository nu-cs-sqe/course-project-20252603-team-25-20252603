package ui.swing;

import domain.locale.LocaleManager;
import domain.player.PlayerColor;
import domain.setup.Game;
import domain.setup.GameSetup;
import domain.setup.PlayerRegistration;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Consumer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeListener;

/**
 * Collects player count, names, and colors, then builds a {@link Game} via
 * {@link GameSetup}. All visible text is resolved through {@link LocaleManager}.
 */
public final class PlayerSetupFrame extends JDialog {

    private static final int MIN_PLAYERS = 3;
    private static final int MAX_PLAYERS = 4;
    private static final long serialVersionUID = 1L;

    private final LocaleManager localeManager;
    private final Consumer<Game> onComplete;
    private final GameSetup gameSetup;
    private final JPanel playerRowsPanel;
    private final JLabel errorLabel;
    private final JSpinner playerCountSpinner;
    private final List<PlayerRow> playerRows = new ArrayList<>();

    /**
     * Builds the dialog. Construct on the Swing event-dispatch thread.
     *
     * @param owner         parent frame, may be {@code null}
     * @param localeManager active locale and message bundles
     * @param onComplete    invoked with a built game before the dialog closes
     */
    public PlayerSetupFrame(Frame owner,
                            LocaleManager localeManager,
                            Consumer<Game> onComplete) {
        this(owner, localeManager, onComplete, new GameSetup(new Random()));
    }

    PlayerSetupFrame(Frame owner,
                     LocaleManager localeManager,
                     Consumer<Game> onComplete,
                     GameSetup gameSetup) {
        super(owner, true);
        if (localeManager == null) {
            throw new IllegalArgumentException("localeManager must not be null");
        }
        if (onComplete == null) {
            throw new IllegalArgumentException("onComplete must not be null");
        }
        if (gameSetup == null) {
            throw new IllegalArgumentException("gameSetup must not be null");
        }
        this.localeManager = localeManager;
        this.onComplete = onComplete;
        this.gameSetup = gameSetup;
        this.playerRowsPanel = new JPanel(new GridLayout(0, 1, 8, 8));
        this.errorLabel = new JLabel(" ");
        this.playerCountSpinner = new JSpinner(
            new SpinnerNumberModel(MIN_PLAYERS, MIN_PLAYERS, MAX_PLAYERS, 1));
        buildLayout();
        rebuildPlayerRows(MIN_PLAYERS);
        SwingLocaleSupport.applyLocaleFont(this);
        pack();
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void buildLayout() {
        setTitle(localeManager.get("app.title"));
        setLayout(new BorderLayout(8, 8));
        add(buildCountPanel(), BorderLayout.NORTH);
        add(playerRowsPanel, BorderLayout.CENTER);
        add(buildSouthPanel(), BorderLayout.SOUTH);
    }

    private JPanel buildCountPanel() {
        JPanel panel = new JPanel(new BorderLayout(4, 4));
        panel.add(new JLabel(localeManager.get("setup.players.count.prompt")),
            BorderLayout.CENTER);
        ChangeListener listener = event -> rebuildPlayerRows(readPlayerCount());
        playerCountSpinner.addChangeListener(listener);
        panel.add(playerCountSpinner, BorderLayout.EAST);
        return panel;
    }

    private JPanel buildSouthPanel() {
        JPanel panel = new JPanel(new BorderLayout(4, 4));
        panel.add(errorLabel, BorderLayout.NORTH);
        JButton start = new JButton(localeManager.get("setup.start"));
        start.addActionListener(event -> attemptStart());
        panel.add(start, BorderLayout.SOUTH);
        return panel;
    }

    private int readPlayerCount() {
        return (Integer) playerCountSpinner.getValue();
    }

    private void rebuildPlayerRows(int count) {
        playerRows.clear();
        playerRowsPanel.removeAll();
        for (int index = 1; index <= count; index++) {
            PlayerRow row = new PlayerRow(index, localeManager);
            playerRows.add(row);
            playerRowsPanel.add(row.asPanel());
        }
        errorLabel.setText(" ");
        playerRowsPanel.revalidate();
        playerRowsPanel.repaint();
    }

    private void attemptStart() {
        errorLabel.setText(" ");
        int count = readPlayerCount();
        String countError = validateCount(count);
        if (countError != null) {
            errorLabel.setText(countError);
            return;
        }
        List<PlayerRegistration> registrations = collectRegistrations();
        if (registrations == null) {
            return;
        }
        gameSetup.registerPlayers(registrations);
        Game game = gameSetup.build();
        onComplete.accept(game);
        dispose();
    }

    private String validateCount(int count) {
        if (count < MIN_PLAYERS || count > MAX_PLAYERS) {
            return localeManager.get("setup.players.count.invalid");
        }
        return null;
    }

    private List<PlayerRegistration> collectRegistrations() {
        Set<String> names = new HashSet<>();
        Set<PlayerColor> colors = new HashSet<>();
        List<PlayerRegistration> registrations = new ArrayList<>();
        for (int index = 0; index < playerRows.size(); index++) {
            PlayerRow row = playerRows.get(index);
            String fieldError = validateRow(row, names, colors);
            if (fieldError != null) {
                errorLabel.setText(fieldError);
                return null;
            }
            registrations.add(row.toRegistration());
        }
        return registrations;
    }

    private String validateRow(PlayerRow row, Set<String> names, Set<PlayerColor> colors) {
        String name = row.nameText();
        if (name.isEmpty()) {
            return localeManager.get("setup.player.name.empty");
        }
        if (!names.add(name)) {
            return localeManager.get("setup.player.name.taken");
        }
        PlayerColor color = row.selectedColor();
        if (!colors.add(color)) {
            return localeManager.get("setup.player.color.taken");
        }
        return null;
    }

    private static final class PlayerRow {

        private final JTextField nameField;
        private final JComboBox<PlayerColor> colorBox;
        private final JPanel panel;

        private PlayerRow(int playerIndex, LocaleManager localeManager) {
            nameField = new JTextField(12);
            colorBox = new JComboBox<>(PlayerColor.values());
            panel = new JPanel(new GridLayout(2, 2, 4, 4));
            panel.add(new JLabel(localeManager.get("setup.player.name.prompt", playerIndex)));
            panel.add(nameField);
            panel.add(new JLabel(localeManager.get("setup.player.color.prompt", playerIndex)));
            panel.add(colorBox);
        }

        private JPanel asPanel() {
            return panel;
        }

        private String nameText() {
            return nameField.getText().trim();
        }

        private PlayerColor selectedColor() {
            return (PlayerColor) colorBox.getSelectedItem();
        }

        private PlayerRegistration toRegistration() {
            return new PlayerRegistration(nameText(), selectedColor());
        }
    }
}
