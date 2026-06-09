package integration;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import domain.board.Hex;
import domain.board.TerrainType;
import domain.play.PlayableGame;
import domain.play.ResourceInventory;
import domain.play.ResourceType;
import domain.player.Player;
import domain.player.PlayerColor;
import domain.setup.Game;
import domain.setup.GameSetup;
import domain.setup.PlayerRegistration;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Integration coverage for the playable-game feature: the composed flow
 * {@link GameSetup} → {@link Game} → {@link PlayableGame}. These cases exercise
 * the end-to-end stack rather than any single class in isolation.
 *
 * <p>Mirrors the style of {@code GameSetupIntegrationTest}: seeded
 * {@code Random(42)}, {@code @Tag("integration")}, {@code assertAll} groupings.
 */
@Tag("integration")
class PlayableGameIntegrationTest {

    @Test
    void it1_productionRollDistributesResourcesThroughTheFullStack() {
        PlayableGame playable = startedGame();
        Player current = playable.currentPlayer();
        Hex ownedHex = playable.ownedHexes(current).get(0);
        int tokenValue = ownedHex.getToken().get().getValue();
        ResourceType expectedResource =
            ResourceType.fromTerrain(ownedHex.getTerrain()).get();

        int produced = playable.rollDice(dieOne(tokenValue), dieTwo(tokenValue));

        assertAll(
            () -> assertTrue(produced >= 1),
            () -> assertTrue(
                playable.inventory(current).count(expectedResource) >= 1),
            () -> playable.game().players().stream()
                .filter(other -> !other.equals(current))
                .filter(other -> playable.ownedHexes(other).stream()
                    .noneMatch(hex -> hex.getToken().isPresent()
                        && hex.getToken().get().getValue() == tokenValue))
                .forEach(other -> assertEquals(
                    0, totalResources(playable.inventory(other))))
        );
    }

    @Test
    void it2_settlementBuildConsumesExactCostAndIncrementsVictoryPoints() {
        PlayableGame playable = startedGame();
        Player current = playable.currentPlayer();
        int startingPoints = playable.victoryPoints(current);
        int target = firstUnownedNonDesertPosition(playable);
        giveSettlementCost(playable.inventory(current));

        playable.buildSettlement(target);

        assertAll(
            () -> assertEquals(Optional.of(current), playable.ownerOf(target)),
            () -> assertEquals(startingPoints + 1, playable.victoryPoints(current)),
            () -> assertEquals(0, totalResources(playable.inventory(current))),
            () -> assertTrue(playable.ownedHexes(current).stream()
                .anyMatch(hex -> hex.getPosition() == target))
        );
    }

    @Test
    void it3_endTurnCyclesThroughEveryPlayerAndWrapsAround() {
        PlayableGame playable = startedGame();
        List<Player> registered = playable.game().players();
        Player initial = playable.currentPlayer();
        assertEquals(registered.get(0), initial);

        for (int index = 1; index < registered.size(); index++) {
            Player advanced = playable.endTurn();
            assertEquals(registered.get(index), advanced);
            assertEquals(registered.get(index), playable.currentPlayer());
        }
        Player wrapped = playable.endTurn();

        assertAll(
            () -> assertEquals(initial, wrapped),
            () -> assertEquals(initial, playable.currentPlayer())
        );
    }

    @Test
    void it4_winnerEmergesWhenCurrentPlayerReachesTenVictoryPointsThroughLegalBuilds() {
        PlayableGame playable = startedGame();
        Player current = playable.currentPlayer();
        assertFalse(playable.hasWinner());

        driveToTenPoints(playable, current);

        assertAll(
            () -> assertEquals(10, playable.victoryPoints(current)),
            () -> assertTrue(playable.hasWinner()),
            () -> assertEquals(Optional.of(current), playable.winner()),
            () -> assertEquals(10, playable.winningPoints())
        );
    }

    @Test
    void it5_allStateChangingActionsRejectedAfterWinnerExists() {
        PlayableGame playable = startedGame();
        Player current = playable.currentPlayer();
        driveToTenPoints(playable, current);
        int frozenPoints = playable.victoryPoints(current);
        int frozenInventory = totalResources(playable.inventory(current));

        assertAll(
            () -> assertThrows(IllegalStateException.class,
                () -> playable.rollDice(1, 1)),
            () -> assertThrows(IllegalStateException.class,
                () -> playable.buildSettlement(
                    firstUnownedNonDesertPosition(playable))),
            () -> assertThrows(IllegalStateException.class,
                playable::buyDevelopmentCard),
            () -> assertThrows(IllegalStateException.class, playable::endTurn),
            () -> assertEquals(frozenPoints, playable.victoryPoints(current)),
            () -> assertEquals(frozenInventory,
                totalResources(playable.inventory(current))),
            () -> assertEquals(current, playable.currentPlayer())
        );
    }

    private static PlayableGame startedGame() {
        GameSetup setup = new GameSetup(new Random(42));
        setup.registerPlayers(List.of(
            new PlayerRegistration("Yuki", PlayerColor.RED),
            new PlayerRegistration("Minji", PlayerColor.BLUE),
            new PlayerRegistration("Arjun", PlayerColor.WHITE)
        ));
        return PlayableGame.start(setup.build());
    }

    private static int dieOne(int sum) {
        return Math.max(1, sum - 6);
    }

    private static int dieTwo(int sum) {
        return sum - dieOne(sum);
    }

    private static int totalResources(ResourceInventory inventory) {
        return inventory.snapshot().values().stream()
            .mapToInt(Integer::intValue).sum();
    }

    private static int firstUnownedNonDesertPosition(PlayableGame playable) {
        return playable.game().board().getHexes().stream()
            .filter(hex -> hex.getTerrain() != TerrainType.DESERT)
            .filter(hex -> !playable.ownerOf(hex.getPosition()).isPresent())
            .findFirst()
            .get()
            .getPosition();
    }

    private static void giveSettlementCost(ResourceInventory inventory) {
        inventory.add(ResourceType.LUMBER, 1);
        inventory.add(ResourceType.BRICK, 1);
        inventory.add(ResourceType.WOOL, 1);
        inventory.add(ResourceType.GRAIN, 1);
    }

    private static void driveToTenPoints(PlayableGame playable, Player player) {
        while (playable.victoryPoints(player) < 10) {
            int position = firstUnownedNonDesertPosition(playable);
            giveSettlementCost(playable.inventory(player));
            playable.buildSettlement(position);
        }
    }
}
