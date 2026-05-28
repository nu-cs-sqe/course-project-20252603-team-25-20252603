package domain.setup;

import domain.board.Board;
import domain.deck.DevelopmentCardDeck;
import domain.player.Player;
import domain.player.PlayerColor;
import domain.turn.TurnOrder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

/**
 * Orchestrates the Game Setup phase: validate registrations, build the
 * board and deck with the injected {@link Random}, and finalize the
 * initial {@link TurnOrder} into a ready-to-play {@link Game}.
 */
public final class GameSetup {

    private static final int MINIMUM_PLAYERS = 3;
    private static final int MAXIMUM_PLAYERS = 4;

    private final Random rng;
    private List<Player> players;

    /**
     * Creates an orchestrator that uses {@code rng} for board and deck shuffling.
     *
     * @param rng non-null source of randomness (seed in tests for determinism)
     * @throws NullPointerException if {@code rng} is null
     */
    public GameSetup(Random rng) {
        this.rng = Objects.requireNonNull(rng, "rng must not be null");
        this.players = new ArrayList<>();
    }

    /**
     * Validates the supplied registrations (3 or 4 players, unique names and
     * colors, no nulls) and converts them into {@link Player} instances.
     *
     * @param registrations user-facing player choices
     * @throws IllegalArgumentException if any validation rule is violated
     */
    public void registerPlayers(List<PlayerRegistration> registrations) {
        validateRegistrations(registrations);
        List<Player> registeredPlayers = new ArrayList<>();
        for (PlayerRegistration registration : registrations) {
            registeredPlayers.add(new Player(registration.name(), registration.color()));
        }
        this.players = registeredPlayers;
    }

    /**
     * Builds the immutable {@link Game} from the registered players.
     *
     * @throws IllegalStateException if {@link #registerPlayers} has not yet
     *                               produced enough players
     */
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
        if (registrations == null
                || registrations.size() < MINIMUM_PLAYERS
                || registrations.size() > MAXIMUM_PLAYERS) {
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
