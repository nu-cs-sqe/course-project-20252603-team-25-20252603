package domain.setup;

import domain.board.Board;
import domain.deck.DevelopmentCardDeck;
import domain.player.Player;
import domain.player.PlayerColor;
import domain.turn.TurnOrder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public final class GameSetup {

    private static final int MINIMUM_PLAYERS = 3;
    private static final int MAXIMUM_PLAYERS = 4;

    private final Random rng;
    private List<Player> players;

    public GameSetup(Random rng) {
        if (rng == null) {
            throw new IllegalArgumentException("rng must not be null");
        }
        this.rng = rng;
        this.players = new ArrayList<>();
    }

    public void registerPlayers(List<PlayerRegistration> registrations) {
        validateRegistrations(registrations);
        List<Player> registeredPlayers = new ArrayList<>();
        for (PlayerRegistration registration : registrations) {
            registeredPlayers.add(new Player(registration.name(), registration.color()));
        }
        this.players = registeredPlayers;
    }

    public Game build() {
        if (players.size() < MINIMUM_PLAYERS) {
            throw new IllegalStateException("register 3 or 4 players before build");
        }
        Board board = Board.generateRandom(rng);
        DevelopmentCardDeck deck = DevelopmentCardDeck.standardShuffled(rng);
        TurnOrder turnOrder = new TurnOrder(players);
        return new Game(players, board, deck, turnOrder);
    }

    private static void validateRegistrations(List<PlayerRegistration> registrations) {
        if (registrations == null) {
            throw new IllegalArgumentException("registrations must not be null");
        }
        if (registrations.size() < MINIMUM_PLAYERS || registrations.size() > MAXIMUM_PLAYERS) {
            throw new IllegalArgumentException("player count must be 3 or 4");
        }
        rejectNullRegistrations(registrations);
        validateUniquePlayerFields(registrations);
    }

    private static void rejectNullRegistrations(List<PlayerRegistration> registrations) {
        for (PlayerRegistration registration : registrations) {
            if (registration == null) {
                throw new IllegalArgumentException("registrations must not contain null");
            }
        }
    }

    private static void validateUniquePlayerFields(List<PlayerRegistration> registrations) {
        Set<String> names = new HashSet<>();
        Set<PlayerColor> colors = new HashSet<>();
        for (PlayerRegistration registration : registrations) {
            if (!names.add(registration.name())) {
                throw new IllegalArgumentException("player names must be unique");
            }
            if (!colors.add(registration.color())) {
                throw new IllegalArgumentException("player colors must be unique");
            }
        }
    }
}
