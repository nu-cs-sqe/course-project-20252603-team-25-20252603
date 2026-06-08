package domain.play;

import domain.board.Hex;
import domain.board.TerrainType;
import domain.player.Player;
import domain.setup.Game;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Minimal playable game controller layered on top of the completed setup
 * state. This D-rubric slice uses automatic starting settlements so players
 * can roll dice, collect resources, build more settlements, and advance turns.
 */
public final class PlayableGame {

    private static final int MIN_DIE_FACE = 1;
    private static final int MAX_DIE_FACE = 6;
    private static final int MIN_POSITION = 0;
    private static final int MAX_POSITION = 18;
    private static final Map<ResourceType, Integer> SETTLEMENT_COST = settlementCost();

    private final Game game;
    private final Map<Player, ResourceInventory> inventories = new HashMap<>();
    private final Map<Integer, Player> settlementOwners = new HashMap<>();
    private final Map<Player, Integer> victoryPoints = new HashMap<>();

    private PlayableGame(Game game) {
        this.game = game;
        initializePlayers();
        assignStartingSettlements();
    }

    /**
     * Starts a playable session from a completed setup game.
     *
     * @param game non-null setup game
     * @return playable controller with one starting settlement per player
     * @throws NullPointerException if {@code game} is null
     */
    public static PlayableGame start(Game game) {
        return new PlayableGame(Objects.requireNonNull(game, "game must not be null"));
    }

    public Game game() {
        return game;
    }

    public Player currentPlayer() {
        return game.turnOrder().current();
    }

    /**
     * Rolls production dice and grants resources to owners of matching hexes.
     *
     * @param dieOne first die face in [1, 6]
     * @param dieTwo second die face in [1, 6]
     * @return number of resources produced
     * @throws IllegalArgumentException if either die face is outside [1, 6]
     */
    public int rollDice(int dieOne, int dieTwo) {
        validateDie(dieOne);
        validateDie(dieTwo);
        int total = dieOne + dieTwo;
        int produced = 0;
        for (Hex hex : game.board().getHexes()) {
            if (producesOn(hex, total) && settlementOwners.containsKey(hex.getPosition())) {
                Player owner = settlementOwners.get(hex.getPosition());
                ResourceType resource = ResourceType.fromTerrain(hex.getTerrain()).get();
                inventories.get(owner).add(resource, 1);
                produced++;
            }
        }
        return produced;
    }

    /**
     * Builds a settlement for the current player on an unoccupied non-desert hex.
     *
     * @param position board position in [0, 18]
     * @throws IllegalArgumentException if the position is invalid, occupied, or desert
     * @throws IllegalStateException    if the current player cannot pay the settlement cost
     */
    public void buildSettlement(int position) {
        Hex hex = hexAt(position);
        if (hex.getTerrain() == TerrainType.DESERT) {
            throw new IllegalArgumentException("cannot build on desert");
        }
        if (settlementOwners.containsKey(position)) {
            throw new IllegalArgumentException("position already has a settlement");
        }
        Player player = currentPlayer();
        inventories.get(player).spend(SETTLEMENT_COST);
        settlementOwners.put(position, player);
        victoryPoints.put(player, victoryPoints.get(player) + 1);
    }

    /**
     * Advances to the next player.
     *
     * @return player whose turn is now active
     */
    public Player endTurn() {
        return game.turnOrder().advance();
    }

    /**
     * Returns a player's mutable inventory.
     *
     * @param player player in this game
     * @throws IllegalArgumentException if {@code player} is not in this game
     */
    public ResourceInventory inventory(Player player) {
        requireKnownPlayer(player);
        return inventories.get(player);
    }

    /**
     * Returns the settlement hexes owned by a player.
     *
     * @param player player in this game
     * @return unmodifiable list of owned hexes
     * @throws IllegalArgumentException if {@code player} is not in this game
     */
    public List<Hex> ownedHexes(Player player) {
        requireKnownPlayer(player);
        List<Hex> owned = new ArrayList<>();
        for (Map.Entry<Integer, Player> entry : settlementOwners.entrySet()) {
            if (entry.getValue().equals(player)) {
                owned.add(hexAt(entry.getKey()));
            }
        }
        owned.sort((left, right) -> Integer.compare(left.getPosition(), right.getPosition()));
        return Collections.unmodifiableList(owned);
    }

    /**
     * Returns the owner of a board position, if any.
     *
     * @param position board position in [0, 18]
     * @return owner of the position, or empty when unoccupied
     * @throws IllegalArgumentException if {@code position} is outside the board
     */
    public Optional<Player> ownerOf(int position) {
        validatePosition(position);
        return Optional.ofNullable(settlementOwners.get(position));
    }

    /**
     * Returns a player's current victory points.
     *
     * @param player player in this game
     * @throws IllegalArgumentException if {@code player} is not in this game
     */
    public int victoryPoints(Player player) {
        requireKnownPlayer(player);
        return victoryPoints.get(player);
    }

    private void initializePlayers() {
        for (Player player : game.players()) {
            inventories.put(player, new ResourceInventory());
            victoryPoints.put(player, 0);
        }
    }

    private void assignStartingSettlements() {
        int playerIndex = 0;
        for (Hex hex : game.board().getHexes()) {
            if (hex.getTerrain() == TerrainType.DESERT) {
                continue;
            }
            Player player = game.players().get(playerIndex);
            settlementOwners.put(hex.getPosition(), player);
            victoryPoints.put(player, victoryPoints.get(player) + 1);
            playerIndex++;
            if (playerIndex == game.players().size()) {
                return;
            }
        }
    }

    private static boolean producesOn(Hex hex, int total) {
        return !hex.hasRobber()
            && hex.getTerrain() != TerrainType.DESERT
            && hex.getToken().isPresent()
            && hex.getToken().get().getValue() == total;
    }

    private Hex hexAt(int position) {
        validatePosition(position);
        return game.board().getHexes().get(position);
    }

    private static void validateDie(int value) {
        if (value < MIN_DIE_FACE || value > MAX_DIE_FACE) {
            throw new IllegalArgumentException("die face must be in [1, 6]");
        }
    }

    private static void validatePosition(int position) {
        if (position < MIN_POSITION || position > MAX_POSITION) {
            throw new IllegalArgumentException("position must be in [0, 18]");
        }
    }

    private void requireKnownPlayer(Player player) {
        Objects.requireNonNull(player, "player must not be null");
        if (!inventories.containsKey(player)) {
            throw new IllegalArgumentException("player is not in this game");
        }
    }

    private static Map<ResourceType, Integer> settlementCost() {
        EnumMap<ResourceType, Integer> cost = new EnumMap<>(ResourceType.class);
        cost.put(ResourceType.LUMBER, 1);
        cost.put(ResourceType.BRICK, 1);
        cost.put(ResourceType.WOOL, 1);
        cost.put(ResourceType.GRAIN, 1);
        return Collections.unmodifiableMap(cost);
    }
}
