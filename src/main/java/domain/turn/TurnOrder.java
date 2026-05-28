package domain.turn;

import domain.player.Player;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * Round-robin ordering over the registered players. After construction,
 * {@link #current()} returns the first player; {@link #advance()} rotates to
 * the next and wraps at the end.
 */
public final class TurnOrder {

    private static final int MINIMUM_PLAYERS = 2;

    private final List<Player> players;
    private int currentIndex;

    /**
     * Creates a turn order over the given players, starting at the first one.
     *
     * @param players non-null list of at least two distinct, non-null players
     * @throws IllegalArgumentException if any rule above is violated
     */
    public TurnOrder(List<Player> players) {
        validatePlayers(players);
        this.players = Collections.unmodifiableList(new ArrayList<>(players));
        this.currentIndex = 0;
    }

    public Player current() {
        return players.get(currentIndex);
    }

    /**
     * Rotates to the next player and returns them. Wraps from the last
     * player back to the first.
     */
    public Player advance() {
        currentIndex = (currentIndex + 1) % players.size();
        return current();
    }

    public int currentIndex() {
        return currentIndex;
    }

    public int size() {
        return players.size();
    }

    private static void validatePlayers(List<Player> players) {
        if (players == null || players.size() < MINIMUM_PLAYERS) {
            throw new IllegalArgumentException("turn order needs at least two players");
        }
        rejectNullPlayers(players);
        if (new HashSet<>(players).size() != players.size()) {
            throw new IllegalArgumentException("players must be unique");
        }
    }

    private static void rejectNullPlayers(List<Player> players) {
        for (Player player : players) {
            if (player == null) {
                throw new IllegalArgumentException("players must not contain null");
            }
        }
    }
}
