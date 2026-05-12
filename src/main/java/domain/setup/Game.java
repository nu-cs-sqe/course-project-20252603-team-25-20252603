package domain.setup;

import domain.board.Board;
import domain.deck.DevelopmentCardDeck;
import domain.player.Player;
import domain.turn.TurnOrder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Game {

    private final List<Player> players;
    private final Board board;
    private final DevelopmentCardDeck deck;
    private final TurnOrder turnOrder;

    public Game(
            List<Player> players,
            Board board,
            DevelopmentCardDeck deck,
            TurnOrder turnOrder) {
        if (players == null || players.isEmpty() || hasNullPlayer(players)) {
            throw new IllegalArgumentException("players must be non-empty and non-null");
        }
        if (board == null) {
            throw new IllegalArgumentException("board must not be null");
        }
        if (deck == null) {
            throw new IllegalArgumentException("deck must not be null");
        }
        if (turnOrder == null) {
            throw new IllegalArgumentException("turnOrder must not be null");
        }
        this.players = Collections.unmodifiableList(new ArrayList<>(players));
        this.board = board;
        this.deck = deck;
        this.turnOrder = turnOrder;
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
