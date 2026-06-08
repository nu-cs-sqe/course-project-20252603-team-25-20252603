package domain.play;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import domain.board.Hex;
import domain.board.TerrainType;
import domain.player.Player;
import domain.player.PlayerColor;
import domain.setup.Game;
import domain.setup.GameSetup;
import domain.setup.PlayerRegistration;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import org.junit.jupiter.api.Test;

class PlayableGameTest {

    @Test
    void tc1_nullGameRejected() {
        assertThrows(NullPointerException.class, () -> PlayableGame.start(null));
    }

    @Test
    void tc2_startingSettlementsAssignedToPlayers() {
        Game game = game();
        PlayableGame playable = PlayableGame.start(game);

        for (Player player : game.players()) {
            assertAll(
                () -> assertEquals(1, playable.ownedHexes(player).size()),
                () -> assertTrue(playable.ownedHexes(player).stream()
                    .noneMatch(hex -> hex.getTerrain() == TerrainType.DESERT)),
                () -> assertEquals(1, playable.victoryPoints(player))
            );
        }
    }

    @Test
    void tc3_diceFaceBelowOneRejected() {
        PlayableGame playable = PlayableGame.start(game());

        assertThrows(IllegalArgumentException.class, () -> playable.rollDice(0, 1));
    }

    @Test
    void tc4_diceFaceAboveSixRejected() {
        PlayableGame playable = PlayableGame.start(game());

        assertThrows(IllegalArgumentException.class, () -> playable.rollDice(6, 7));
    }

    @Test
    void tc5_matchingProductionRollGrantsResource() {
        PlayableGame playable = PlayableGame.start(game());
        Player current = playable.currentPlayer();
        Hex owned = playable.ownedHexes(current).get(0);
        int value = owned.getToken().get().getValue();
        ResourceType resource = ResourceType.fromTerrain(owned.getTerrain()).get();

        int produced = playable.rollDice(dieOne(value), dieTwo(value));

        assertAll(
            () -> assertEquals(1, produced),
            () -> assertEquals(1, playable.inventory(current).count(resource))
        );
    }

    @Test
    void tc6_nonMatchingProductionRollGrantsNothing() {
        PlayableGame playable = PlayableGame.start(game());
        Player current = playable.currentPlayer();

        int produced = playable.rollDice(3, 4);

        assertAll(
            () -> assertEquals(0, produced),
            () -> assertEquals(0, totalResources(playable.inventory(current)))
        );
    }

    @Test
    void tc7_buildingOnOccupiedHexRejected() {
        PlayableGame playable = PlayableGame.start(game());
        int occupiedPosition = playable.ownedHexes(playable.currentPlayer()).get(0).getPosition();

        assertThrows(IllegalArgumentException.class,
            () -> playable.buildSettlement(occupiedPosition));
    }

    @Test
    void tc8_buildingWithoutResourcesRejected() {
        PlayableGame playable = PlayableGame.start(game());
        int position = unownedNonDesertPosition(playable);

        assertThrows(IllegalStateException.class, () -> playable.buildSettlement(position));
    }

    @Test
    void tc9_buildingWithExactResourcesSucceeds() {
        PlayableGame playable = PlayableGame.start(game());
        Player current = playable.currentPlayer();
        int position = unownedNonDesertPosition(playable);
        giveSettlementCost(playable.inventory(current));

        playable.buildSettlement(position);

        assertAll(
            () -> assertEquals(Optional.of(current), playable.ownerOf(position)),
            () -> assertEquals(2, playable.victoryPoints(current)),
            () -> assertEquals(0, totalResources(playable.inventory(current)))
        );
    }

    @Test
    void tc10_endTurnAdvancesToNextPlayer() {
        Game game = game();
        PlayableGame playable = PlayableGame.start(game);

        playable.endTurn();

        assertEquals(game.players().get(1), playable.currentPlayer());
    }

    @Test
    void tc11_unknownPlayerRejected() {
        PlayableGame playable = PlayableGame.start(game());
        Player unknown = new Player("Nope", PlayerColor.ORANGE);

        assertThrows(IllegalArgumentException.class, () -> playable.inventory(unknown));
    }

    @Test
    void tc12_positionOutsideBoardRejected() {
        PlayableGame playable = PlayableGame.start(game());

        assertAll(
            () -> assertThrows(IllegalArgumentException.class, () -> playable.ownerOf(-1)),
            () -> assertThrows(IllegalArgumentException.class, () -> playable.ownerOf(19))
        );
    }

    private static Game game() {
        GameSetup setup = new GameSetup(new Random(42));
        setup.registerPlayers(List.of(
            new PlayerRegistration("Yuki", PlayerColor.RED),
            new PlayerRegistration("Minji", PlayerColor.BLUE),
            new PlayerRegistration("Arjun", PlayerColor.WHITE)
        ));
        return setup.build();
    }

    private static int dieOne(int sum) {
        return Math.max(1, sum - 6);
    }

    private static int dieTwo(int sum) {
        return sum - dieOne(sum);
    }

    private static int totalResources(ResourceInventory inventory) {
        return inventory.snapshot().values().stream().mapToInt(Integer::intValue).sum();
    }

    private static int unownedNonDesertPosition(PlayableGame playable) {
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
}
