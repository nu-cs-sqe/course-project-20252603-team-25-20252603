package domain.setup;

import domain.board.Board;
import domain.deck.DevelopmentCardDeck;
import domain.player.Player;
import domain.turn.TurnOrder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Immutable snapshot of a fully set-up CATAN game: players, board, deck, and
 * the initial turn order. Produced by {@link GameSetup#build()} once player
 * registration has been validated.
 */
public final class Game {

    private final List<Player> players;
    private final Board board;
    private final DevelopmentCardDeck deck;
    private final TurnOrder turnOrder;

    /**
     * Creates a fully set-up game from the supplied parts.
     *
     * @param players   non-empty list of unique, non-null players
     * @param board     non-null board
     * @param deck      non-null development-card deck
     * @param turnOrder non-null turn order initialized over {@code players}
     * @throws IllegalArgumentException if {@code players} is empty or contains null
     * @throws NullPointerException     if {@code players}, {@code board},
     *                                  {@code deck}, or {@code turnOrder} is null
     */
    public Game(
            List<Player> players,
            Board board,
            DevelopmentCardDeck deck,
            TurnOrder turnOrder) {
        Objects.requireNonNull(players, "players must not be null");
        if (players.isEmpty() || hasNullPlayer(players)) {
            throw new IllegalArgumentException("players must be non-empty and contain no nulls");
        }
        this.players = Collections.unmodifiableList(new ArrayList<>(players));
        this.board = Objects.requireNonNull(board, "board must not be null");
        this.deck = Objects.requireNonNull(deck, "deck must not be null");
        this.turnOrder = Objects.requireNonNull(turnOrder, "turnOrder must not be null");
    }

    public List<Player> players() {
        return players;
    }

    public Board board() {
        return board;
    }

    public DevelopmentCardDeck deck() {
        return deck;
    }

    public TurnOrder turnOrder() {
        return turnOrder;
    }

    private static boolean hasNullPlayer(List<Player> players) {
        for (Player player : players) {
            if (player == null) {
                return true;
            }
        }
        return false;
    }
}
