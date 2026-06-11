package domain.play;

import domain.board.Hex;
import domain.board.TerrainType;
import domain.deck.DevelopmentCard;
import domain.deck.DevelopmentCardType;
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
import java.util.stream.Collectors;

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
    private static final int WINNING_POINTS = 10;
    private static final int LONGEST_ROAD_THRESHOLD = 5;
    private static final Map<ResourceType, Integer> SETTLEMENT_COST = settlementCost();
    private static final Map<ResourceType, Integer> CITY_COST = cityCost();
    private static final Map<ResourceType, Integer> ROAD_COST = roadCost();
    private static final Map<ResourceType, Integer> DEVELOPMENT_CARD_COST =
        developmentCardCost();

    private final Game game;
    private final Map<Player, ResourceInventory> inventories = new HashMap<>();
    private final Map<Integer, Player> settlementOwners = new HashMap<>();
    private final Map<Integer, Boolean> cityPositions = new HashMap<>();
    private final Map<Player, Integer> victoryPoints = new HashMap<>();
    private final Map<Player, Integer> knightsPlayed = new HashMap<>();
    private final Map<Player, Integer> roadCounts = new HashMap<>();
    private Player largestArmyHolder;
    private Player longestRoadHolder;

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
        rejectIfGameOver();
        validateDie(dieOne);
        validateDie(dieTwo);
        int total = dieOne + dieTwo;
        int produced = 0;
        for (Hex hex : game.board().getHexes()) {
            if (producesOn(hex, total) && settlementOwners.containsKey(hex.getPosition())) {
                Player owner = settlementOwners.get(hex.getPosition());
                ResourceType resource = ResourceType.fromTerrain(hex.getTerrain()).get();
                int amount = cityPositions.getOrDefault(hex.getPosition(), false) ? 2 : 1;
                inventories.get(owner).add(resource, amount);
                produced += amount;
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
        rejectIfGameOver();
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
     * Upgrades the current player's settlement at {@code position} to a city.
     *
     * @param position board position in [0, 18]
     * @throws IllegalArgumentException if the position is not owned by the current
     *                                  player or is already a city
     * @throws IllegalStateException    if the current player cannot pay the city cost
     */
    public void buildCity(int position) {
        rejectIfGameOver();
        Player player = currentPlayer();
        if (!Optional.of(player).equals(ownerOf(position))) {
            throw new IllegalArgumentException("current player must own the settlement");
        }
        if (cityPositions.getOrDefault(position, false)) {
            throw new IllegalArgumentException("settlement is already a city");
        }
        inventories.get(player).spend(CITY_COST);
        cityPositions.put(position, true);
        addVictoryPoints(player, 1);
    }

    /**
     * Builds one simplified road for the current player and updates Longest Road.
     *
     * @throws IllegalStateException if the current player cannot pay the road cost
     */
    public void buildRoad() {
        rejectIfGameOver();
        Player player = currentPlayer();
        inventories.get(player).spend(ROAD_COST);
        addRoads(player, 1);
    }

    /**
     * Buys and draws one development card for the current player. Victory Point
     * cards are fully implemented in this D slice: they immediately add one
     * victory point. Other card types are drawn but have no effect yet.
     *
     * @return drawn development card
     * @throws IllegalStateException if the current player cannot pay the cost
     */
    public DevelopmentCard buyDevelopmentCard() {
        return buyDevelopmentCard(ResourceType.BRICK, ResourceType.ORE, ResourceType.GRAIN);
    }

    /**
     * Buys, draws, and applies one development card using the supplied resource
     * choices for Monopoly and Year of Plenty effects.
     *
     * @param monopolyChoice resource to collect if the drawn card is Monopoly
     * @param plentyFirst    first resource if the drawn card is Year of Plenty
     * @param plentySecond   second resource if the drawn card is Year of Plenty
     * @return drawn development card
     * @throws IllegalStateException if the current player cannot pay the cost
     */
    public DevelopmentCard buyDevelopmentCard(
            ResourceType monopolyChoice,
            ResourceType plentyFirst,
            ResourceType plentySecond) {
        rejectIfGameOver();
        Player player = currentPlayer();
        inventories.get(player).spend(DEVELOPMENT_CARD_COST);
        DevelopmentCard card = game.deck().draw();
        applyDevelopmentCard(card, monopolyChoice, plentyFirst, plentySecond);
        return card;
    }

    /**
     * Applies a development card to the current player. Cards with resource
     * choices use default selections.
     *
     * @param card non-null card to apply
     */
    public void applyDevelopmentCard(DevelopmentCard card) {
        applyDevelopmentCard(card, ResourceType.BRICK, ResourceType.ORE, ResourceType.GRAIN);
    }

    /**
     * Applies a development card to the current player.
     *
     * @param card           non-null card to apply
     * @param monopolyChoice resource to collect for Monopoly
     * @param plentyFirst    first resource for Year of Plenty
     * @param plentySecond   second resource for Year of Plenty
     * @throws NullPointerException if {@code card} is null, or if a choice
     *                              required by the card type is null
     */
    public void applyDevelopmentCard(
            DevelopmentCard card,
            ResourceType monopolyChoice,
            ResourceType plentyFirst,
            ResourceType plentySecond) {
        Objects.requireNonNull(card, "card must not be null");
        DevelopmentCardType type = card.getType();
        if (type == DevelopmentCardType.KNIGHT) {
            applyKnight();
        } else if (type == DevelopmentCardType.VICTORY_POINT) {
            addVictoryPoints(currentPlayer(), 1);
        } else if (type == DevelopmentCardType.ROAD_BUILDING) {
            addRoads(currentPlayer(), 2);
        } else if (type == DevelopmentCardType.MONOPOLY) {
            applyMonopoly(Objects.requireNonNull(
                monopolyChoice, "monopolyChoice must not be null"));
        } else {
            applyYearOfPlenty(
                Objects.requireNonNull(plentyFirst, "plentyFirst must not be null"),
                Objects.requireNonNull(plentySecond, "plentySecond must not be null"));
        }
    }

    /**
     * Advances to the next player.
     *
     * @return player whose turn is now active
     */
    public Player endTurn() {
        rejectIfGameOver();
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

    /**
     * Returns how many Knight cards the player has played.
     *
     * @param player player in this game
     * @throws IllegalArgumentException if {@code player} is not in this game
     */
    public int knightsPlayed(Player player) {
        requireKnownPlayer(player);
        return knightsPlayed.get(player);
    }

    /**
     * Returns the number of roads credited to the player.
     *
     * @param player player in this game
     * @throws IllegalArgumentException if {@code player} is not in this game
     */
    public int roadCount(Player player) {
        requireKnownPlayer(player);
        return roadCounts.get(player);
    }

    /**
     * Returns whether the board position is a city.
     *
     * @param position board position in [0, 18]
     * @return true when the owned settlement has been upgraded
     */
    public boolean isCity(int position) {
        validatePosition(position);
        return cityPositions.getOrDefault(position, false);
    }

    /**
     * Returns the holder of Largest Army, if any.
     *
     * @return player with Largest Army, or empty before the bonus is awarded
     */
    public Optional<Player> largestArmyHolder() {
        return Optional.ofNullable(largestArmyHolder);
    }

    /**
     * Returns the holder of Longest Road, if any.
     *
     * @return player with Longest Road, or empty before the bonus is awarded
     */
    public Optional<Player> longestRoadHolder() {
        return Optional.ofNullable(longestRoadHolder);
    }

    public int winningPoints() {
        return WINNING_POINTS;
    }

    /**
     * Reports whether any player has reached the CATAN winning score.
     *
     * @return true when a winner exists
     */
    public boolean hasWinner() {
        return winner().isPresent();
    }

    /**
     * Returns the winning player, if someone has reached 10 victory points.
     *
     * @return winning player, or empty when the game is still active
     */
    public Optional<Player> winner() {
        for (Player player : game.players()) {
            if (victoryPoints.get(player) >= WINNING_POINTS) {
                return Optional.of(player);
            }
        }
        return Optional.empty();
    }

    private void initializePlayers() {
        for (Player player : game.players()) {
            inventories.put(player, new ResourceInventory());
            victoryPoints.put(player, 0);
            knightsPlayed.put(player, 0);
            roadCounts.put(player, 0);
        }
    }

    private void assignStartingSettlements() {
        List<Hex> startingHexes = game.board().getHexes().stream()
            .filter(hex -> hex.getTerrain() != TerrainType.DESERT)
            .collect(Collectors.toList());
        for (int playerIndex = 0; playerIndex < game.players().size(); playerIndex++) {
            Hex hex = startingHexes.get(playerIndex);
            Player player = game.players().get(playerIndex);
            settlementOwners.put(hex.getPosition(), player);
            addVictoryPoints(player, 1);
        }
    }

    private static boolean producesOn(Hex hex, int total) {
        return !hex.hasRobber()
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

    private void rejectIfGameOver() {
        if (hasWinner()) {
            throw new IllegalStateException("game already has a winner");
        }
    }

    private void addVictoryPoints(Player player, int points) {
        victoryPoints.put(player, victoryPoints.get(player) + points);
    }

    private void applyKnight() {
        Player player = currentPlayer();
        knightsPlayed.put(player, knightsPlayed.get(player) + 1);
        updateLargestArmy(player);
    }

    private void updateLargestArmy(Player candidate) {
        if (knightsPlayed.get(candidate) < 3) {
            return;
        }
        if (largestArmyHolder == null) {
            largestArmyHolder = candidate;
            addVictoryPoints(candidate, 2);
            return;
        }
        if (!largestArmyHolder.equals(candidate)
                && knightsPlayed.get(candidate) > knightsPlayed.get(largestArmyHolder)) {
            addVictoryPoints(largestArmyHolder, -2);
            largestArmyHolder = candidate;
            addVictoryPoints(candidate, 2);
        }
    }

    private void addRoads(Player player, int count) {
        roadCounts.put(player, roadCounts.get(player) + count);
        updateLongestRoad(player);
    }

    private void updateLongestRoad(Player candidate) {
        if (roadCounts.get(candidate) < LONGEST_ROAD_THRESHOLD) {
            return;
        }
        if (longestRoadHolder == null) {
            longestRoadHolder = candidate;
            addVictoryPoints(candidate, 2);
            return;
        }
        if (!longestRoadHolder.equals(candidate)
                && roadCounts.get(candidate) > roadCounts.get(longestRoadHolder)) {
            addVictoryPoints(longestRoadHolder, -2);
            longestRoadHolder = candidate;
            addVictoryPoints(candidate, 2);
        }
    }

    private void applyMonopoly(ResourceType resource) {
        Player player = currentPlayer();
        int collected = 0;
        for (Player other : game.players()) {
            if (other.equals(player)) {
                continue;
            }
            ResourceInventory inventory = inventories.get(other);
            int amount = inventory.count(resource);
            if (amount > 0) {
                inventory.spend(Collections.singletonMap(resource, amount));
                collected += amount;
            }
        }
        inventories.get(player).add(resource, collected);
    }

    private void applyYearOfPlenty(ResourceType first, ResourceType second) {
        ResourceInventory inventory = inventories.get(currentPlayer());
        inventory.add(first, 1);
        inventory.add(second, 1);
    }

    private static Map<ResourceType, Integer> settlementCost() {
        EnumMap<ResourceType, Integer> cost = new EnumMap<>(ResourceType.class);
        cost.put(ResourceType.LUMBER, 1);
        cost.put(ResourceType.BRICK, 1);
        cost.put(ResourceType.WOOL, 1);
        cost.put(ResourceType.GRAIN, 1);
        return Collections.unmodifiableMap(cost);
    }

    private static Map<ResourceType, Integer> cityCost() {
        EnumMap<ResourceType, Integer> cost = new EnumMap<>(ResourceType.class);
        cost.put(ResourceType.ORE, 3);
        cost.put(ResourceType.GRAIN, 2);
        return Collections.unmodifiableMap(cost);
    }

    private static Map<ResourceType, Integer> roadCost() {
        EnumMap<ResourceType, Integer> cost = new EnumMap<>(ResourceType.class);
        cost.put(ResourceType.LUMBER, 1);
        cost.put(ResourceType.BRICK, 1);
        return Collections.unmodifiableMap(cost);
    }

    private static Map<ResourceType, Integer> developmentCardCost() {
        EnumMap<ResourceType, Integer> cost = new EnumMap<>(ResourceType.class);
        cost.put(ResourceType.ORE, 1);
        cost.put(ResourceType.WOOL, 1);
        cost.put(ResourceType.GRAIN, 1);
        return Collections.unmodifiableMap(cost);
    }
}
