package domain.turn;

import domain.player.Player;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public final class TurnOrder {

    private static final int MINIMUM_PLAYERS = 2;

    private final List<Player> players;
    private int currentIndex;

    public TurnOrder(List<Player> players) {
        validatePlayers(players);
        this.players = Collections.unmodifiableList(new ArrayList<>(players));
        this.currentIndex = 0;
    }

    public Player current() {
        return players.get(currentIndex);
    }

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
        if (players == null) {
            throw new IllegalArgumentException("players must not be null");
        }
        if (players.size() < MINIMUM_PLAYERS) {
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
