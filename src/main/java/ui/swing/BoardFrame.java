package ui.swing;

import domain.deck.DevelopmentCard;
import domain.locale.LocaleManager;
import domain.play.PlayableGame;
import domain.play.ResourceInventory;
import domain.play.ResourceType;
import domain.player.Player;
import domain.setup.Game;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;

/**
 * Top-level frame shown after {@link domain.setup.GameSetup#build()} completes.
 * Provides the D-rubric playable slice: roll dice, collect resources from
 * starting settlements, build settlements, and advance turns. All visible text
 * is resolved through {@link LocaleManager}.
 */
public final class BoardFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private static final int LAYOUT_GAP = 8;
    private static final String PLAYER_SEPARATOR = ", ";
    private static final int TEXT_ROWS = 16;
    private static final int TEXT_COLUMNS = 48;

    private final LocaleManager localeManager;
    private final PlayableGame playableGame;
    private final Random dice = new Random();
    private final JLabel currentPlayerLabel = new JLabel();
    private final JLabel victoryLabel = new JLabel();
    private final JLabel winnerLabel = new JLabel();
    private final JLabel inventoryLabel = new JLabel();
    private final JTextArea boardText = new JTextArea(TEXT_ROWS, TEXT_COLUMNS);
    private final StringBuilder logHistory = new StringBuilder();
    private final JSpinner positionSpinner =
        new JSpinner(new SpinnerNumberModel(0, 0, 18, 1));

    /**
     * Builds the frame. Construct on the Swing event-dispatch thread.
     *
     * @param localeManager active locale and message bundles
     * @param game          fully built game produced by {@link domain.setup.GameSetup}
     */
    public BoardFrame(LocaleManager localeManager, Game game) {
        super();
        this.localeManager =
            Objects.requireNonNull(localeManager, "localeManager must not be null");
        this.playableGame = PlayableGame.start(
            Objects.requireNonNull(game, "game must not be null"));
        setTitle(this.localeManager.get("board.title"));
        setLayout(new BorderLayout(LAYOUT_GAP, LAYOUT_GAP));
        boardText.setEditable(false);
        add(buildSummaryPanel(game), BorderLayout.NORTH);
        add(new JScrollPane(boardText), BorderLayout.CENTER);
        add(buildControlPanel(), BorderLayout.SOUTH);
        appendLog(this.localeManager.get("board.log.ready"));
        refreshState();
        SwingLocaleSupport.applyLocaleFont(this);
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private JPanel buildSummaryPanel(Game game) {
        JPanel panel = new JPanel(new GridLayout(0, 1, LAYOUT_GAP, LAYOUT_GAP));
        String starter = game.turnOrder().current().getName();
        panel.add(new JLabel(localeManager.get("board.starter", starter)));
        String names = joinPlayerNames(game.players());
        panel.add(new JLabel(localeManager.get("board.players", names)));
        panel.add(currentPlayerLabel);
        panel.add(victoryLabel);
        panel.add(winnerLabel);
        panel.add(inventoryLabel);
        return panel;
    }

    private JPanel buildControlPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 0, LAYOUT_GAP, LAYOUT_GAP));
        JButton roll = new JButton(localeManager.get("board.roll"));
        roll.addActionListener(event -> rollDice());
        panel.add(roll);
        panel.add(new JLabel(localeManager.get("board.position")));
        panel.add(positionSpinner);
        JButton build = new JButton(localeManager.get("board.build"));
        build.addActionListener(event -> buildSettlement());
        panel.add(build);
        JButton buyCard = new JButton(localeManager.get("board.buyDevCard"));
        buyCard.addActionListener(event -> buyDevelopmentCard());
        panel.add(buyCard);
        JButton endTurn = new JButton(localeManager.get("board.endTurn"));
        endTurn.addActionListener(event -> endTurn());
        panel.add(endTurn);
        return panel;
    }

    private void rollDice() {
        try {
            int first = dice.nextInt(6) + 1;
            int second = dice.nextInt(6) + 1;
            int produced = playableGame.rollDice(first, second);
            appendLog(localeManager.get("board.log.roll", first, second, produced));
        } catch (IllegalStateException exception) {
            appendLog(localeManager.get("board.log.gameOver"));
        }
        refreshState();
    }

    private void buildSettlement() {
        int position = (Integer) positionSpinner.getValue();
        try {
            Player player = playableGame.currentPlayer();
            playableGame.buildSettlement(position);
            appendLog(localeManager.get("board.log.build", player.getName(), position));
            appendWinLogIfNeeded();
        } catch (IllegalArgumentException exception) {
            appendLog(localeManager.get("board.log.build.failed"));
        } catch (IllegalStateException exception) {
            appendLog(playableGame.hasWinner()
                ? localeManager.get("board.log.gameOver")
                : localeManager.get("board.log.build.failed"));
        }
        refreshState();
    }

    private void buyDevelopmentCard() {
        try {
            DevelopmentCard card = playableGame.buyDevelopmentCard();
            appendLog(localeManager.get("board.log.card", card.getType().name()));
            appendWinLogIfNeeded();
        } catch (IllegalStateException exception) {
            appendLog(playableGame.hasWinner()
                ? localeManager.get("board.log.gameOver")
                : localeManager.get("board.log.card.failed"));
        }
        refreshState();
    }

    private void endTurn() {
        try {
            Player player = playableGame.endTurn();
            appendLog(localeManager.get("board.log.turn", player.getName()));
        } catch (IllegalStateException exception) {
            appendLog(localeManager.get("board.log.gameOver"));
        }
        refreshState();
    }

    private void refreshState() {
        Player current = playableGame.currentPlayer();
        currentPlayerLabel.setText(localeManager.get("board.current", current.getName()));
        victoryLabel.setText(localeManager.get(
            "board.victory",
            playableGame.victoryPoints(current)));
        winnerLabel.setText(playableGame.winner()
            .map(player -> localeManager.get("board.winner", player.getName()))
            .orElse(localeManager.get("board.winningPoints", playableGame.winningPoints())));
        ResourceInventory inventory = playableGame.inventory(current);
        Map<ResourceType, Integer> resources = inventory.snapshot();
        inventoryLabel.setText(localeManager.get(
            "board.inventory",
            resources.get(ResourceType.LUMBER),
            resources.get(ResourceType.BRICK),
            resources.get(ResourceType.WOOL),
            resources.get(ResourceType.GRAIN),
            resources.get(ResourceType.ORE)));
        refreshBoardText();
    }

    private void refreshBoardText() {
        StringBuilder text = new StringBuilder();
        for (domain.board.Hex hex : playableGame.game().board().getHexes()) {
            Optional<Player> owner = playableGame.ownerOf(hex.getPosition());
            String token = hex.getToken().isPresent()
                ? String.valueOf(hex.getToken().get().getValue()) : "-";
            String ownerName = owner.map(Player::getName).orElse("-");
            text.append(localeManager.get(
                "board.hex",
                hex.getPosition(),
                localeManager.get("terrain." + hex.getTerrain().name()),
                token,
                ownerName))
                .append(System.lineSeparator());
        }
        text.append(System.lineSeparator()).append(logHistory);
        boardText.setText(text.toString());
        boardText.setCaretPosition(0);
    }

    private void appendLog(String message) {
        logHistory.append(message).append(System.lineSeparator());
    }

    private void appendWinLogIfNeeded() {
        playableGame.winner()
            .ifPresent(player -> appendLog(localeManager.get("board.log.win", player.getName())));
    }

    private static String joinPlayerNames(List<Player> players) {
        return players.stream()
            .map(Player::getName)
            .collect(Collectors.joining(PLAYER_SEPARATOR));
    }
}
