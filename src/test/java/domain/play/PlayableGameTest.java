package domain.play;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import domain.board.Hex;
import domain.board.TerrainType;
import domain.deck.DevelopmentCard;
import domain.deck.DevelopmentCardType;
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
    void tc11_buyingDevelopmentCardWithoutResourcesRejected() {
        PlayableGame playable = PlayableGame.start(game());

        assertThrows(IllegalStateException.class, playable::buyDevelopmentCard);
    }

    @Test
    void tc12_victoryPointDevelopmentCardIncreasesVictoryPoints() {
        PlayableGame playable = PlayableGame.start(game());
        Player current = playable.currentPlayer();
        giveDevelopmentCardBudget(playable.inventory(current));
        int startingPoints = playable.victoryPoints(current);

        DevelopmentCard drawn;
        do {
            drawn = playable.buyDevelopmentCard();
        } while (drawn.getType() != DevelopmentCardType.VICTORY_POINT);

        assertEquals(startingPoints + 1, playable.victoryPoints(current));
    }

    @Test
    void tc13_nullDevelopmentCardRejected() {
        PlayableGame playable = PlayableGame.start(game());

        assertThrows(NullPointerException.class,
            () -> playable.applyDevelopmentCard(
                null,
                ResourceType.BRICK,
                ResourceType.ORE,
                ResourceType.GRAIN));
    }

    @Test
    void tc14_knightIncrementsArmyCount() {
        PlayableGame playable = PlayableGame.start(game());
        Player current = playable.currentPlayer();

        playable.applyDevelopmentCard(card(DevelopmentCardType.KNIGHT));

        assertEquals(1, playable.knightsPlayed(current));
    }

    @Test
    void tc15_largestArmyAwardedAtThreeKnights() {
        PlayableGame playable = PlayableGame.start(game());
        final Player current = playable.currentPlayer();

        playable.applyDevelopmentCard(card(DevelopmentCardType.KNIGHT));
        playable.applyDevelopmentCard(card(DevelopmentCardType.KNIGHT));
        playable.applyDevelopmentCard(card(DevelopmentCardType.KNIGHT));

        assertAll(
            () -> assertEquals(Optional.of(current), playable.largestArmyHolder()),
            () -> assertEquals(3, playable.victoryPoints(current))
        );
    }

    @Test
    void tc16_largestArmyTransfersOnlyWhenAnotherPlayerExceedsHolder() {
        PlayableGame playable = PlayableGame.start(game());
        final Player first = playable.currentPlayer();
        playKnights(playable, 3);
        playable.endTurn();
        final Player second = playable.currentPlayer();

        playKnights(playable, 3);

        assertAll(
            () -> assertEquals(Optional.of(first), playable.largestArmyHolder()),
            () -> assertEquals(3, playable.victoryPoints(first)),
            () -> assertEquals(1, playable.victoryPoints(second))
        );

        playable.applyDevelopmentCard(card(DevelopmentCardType.KNIGHT));

        assertAll(
            () -> assertEquals(Optional.of(second), playable.largestArmyHolder()),
            () -> assertEquals(1, playable.victoryPoints(first)),
            () -> assertEquals(3, playable.victoryPoints(second))
        );
    }

    @Test
    void tc17_roadBuildingAddsTwoRoads() {
        PlayableGame playable = PlayableGame.start(game());
        Player current = playable.currentPlayer();

        playable.applyDevelopmentCard(card(DevelopmentCardType.ROAD_BUILDING));

        assertEquals(2, playable.roadCount(current));
    }

    @Test
    void tc18_monopolyTransfersSelectedResource() {
        PlayableGame playable = PlayableGame.start(game());
        final Player current = playable.currentPlayer();
        playable.endTurn();
        playable.inventory(playable.currentPlayer()).add(ResourceType.BRICK, 2);
        playable.endTurn();
        playable.inventory(playable.currentPlayer()).add(ResourceType.BRICK, 1);
        playable.endTurn();

        playable.applyDevelopmentCard(
            card(DevelopmentCardType.MONOPOLY),
            ResourceType.BRICK,
            ResourceType.ORE,
            ResourceType.GRAIN);

        assertAll(
            () -> assertEquals(3, playable.inventory(current).count(ResourceType.BRICK)),
            () -> playable.game().players().stream()
                .filter(player -> !player.equals(current))
                .forEach(player -> assertEquals(
                    0,
                    playable.inventory(player).count(ResourceType.BRICK)))
        );
    }

    @Test
    void tc19_monopolyLeavesHandsUnchangedWhenNoOpponentHasResource() {
        PlayableGame playable = PlayableGame.start(game());
        final Player current = playable.currentPlayer();

        playable.applyDevelopmentCard(
            card(DevelopmentCardType.MONOPOLY),
            ResourceType.BRICK,
            ResourceType.ORE,
            ResourceType.GRAIN);

        assertAll(
            () -> assertEquals(0, playable.inventory(current).count(ResourceType.BRICK)),
            () -> playable.game().players().stream()
                .filter(player -> !player.equals(current))
                .forEach(player -> assertEquals(
                    0,
                    playable.inventory(player).count(ResourceType.BRICK)))
        );
    }

    @Test
    void tc20_monopolyNullChoiceRejected() {
        PlayableGame playable = PlayableGame.start(game());

        assertThrows(NullPointerException.class,
            () -> playable.applyDevelopmentCard(
                card(DevelopmentCardType.MONOPOLY),
                null,
                ResourceType.ORE,
                ResourceType.GRAIN));
    }

    @Test
    void tc21_yearOfPlentyGrantsSelectedResources() {
        PlayableGame playable = PlayableGame.start(game());
        Player current = playable.currentPlayer();

        playable.applyDevelopmentCard(
            card(DevelopmentCardType.YEAR_OF_PLENTY),
            ResourceType.BRICK,
            ResourceType.ORE,
            ResourceType.GRAIN);

        assertAll(
            () -> assertEquals(1, playable.inventory(current).count(ResourceType.ORE)),
            () -> assertEquals(1, playable.inventory(current).count(ResourceType.GRAIN))
        );
    }

    @Test
    void tc22_yearOfPlentyNullChoiceRejected() {
        PlayableGame playable = PlayableGame.start(game());

        assertThrows(NullPointerException.class,
            () -> playable.applyDevelopmentCard(
                card(DevelopmentCardType.YEAR_OF_PLENTY),
                ResourceType.BRICK,
                ResourceType.ORE,
                null));
    }

    @Test
    void tc23_newGameHasNoWinner() {
        PlayableGame playable = PlayableGame.start(game());

        assertAll(
            () -> assertFalse(playable.hasWinner()),
            () -> assertFalse(playable.winner().isPresent()),
            () -> assertEquals(10, playable.winningPoints())
        );
    }

    @Test
    void tc24_playerWinsAtTenVictoryPoints() {
        PlayableGame playable = PlayableGame.start(game());
        Player current = playable.currentPlayer();

        buildUntilWin(playable);

        assertAll(
            () -> assertEquals(10, playable.victoryPoints(current)),
            () -> assertTrue(playable.hasWinner()),
            () -> assertEquals(Optional.of(current), playable.winner())
        );
    }

    @Test
    void tc25_actionsAfterWinRejected() {
        PlayableGame playable = PlayableGame.start(game());
        buildUntilWin(playable);

        assertAll(
            () -> assertThrows(IllegalStateException.class, () -> playable.rollDice(1, 1)),
            () -> assertThrows(IllegalStateException.class, () -> playable.buildSettlement(18)),
            () -> assertThrows(IllegalStateException.class, playable::buyDevelopmentCard),
            () -> assertThrows(IllegalStateException.class, playable::endTurn)
        );
    }

    @Test
    void tc26_unknownPlayerRejected() {
        PlayableGame playable = PlayableGame.start(game());
        Player unknown = new Player("Nope", PlayerColor.ORANGE);

        assertAll(
            () -> assertThrows(IllegalArgumentException.class, () -> playable.inventory(unknown)),
            () -> assertThrows(IllegalArgumentException.class, () -> playable.ownedHexes(unknown)),
            () -> assertThrows(IllegalArgumentException.class,
                () -> playable.victoryPoints(unknown)),
            () -> assertThrows(IllegalArgumentException.class,
                () -> playable.knightsPlayed(unknown)),
            () -> assertThrows(IllegalArgumentException.class, () -> playable.roadCount(unknown))
        );
    }

    @Test
    void tc27_positionOutsideBoardRejected() {
        PlayableGame playable = PlayableGame.start(game());

        assertAll(
            () -> assertThrows(IllegalArgumentException.class, () -> playable.ownerOf(-1)),
            () -> assertThrows(IllegalArgumentException.class, () -> playable.ownerOf(19)),
            () -> assertThrows(IllegalArgumentException.class, () -> playable.ownerOf(-2)),
            () -> assertThrows(IllegalArgumentException.class, () -> playable.ownerOf(20))
        );
    }

    @Test
    void tc28_minAndMaxBoardPositionsCanBeQueried() {
        PlayableGame playable = PlayableGame.start(game());

        assertAll(
            () -> assertEquals(playable.ownerOf(0), playable.ownerOf(0)),
            () -> assertEquals(playable.ownerOf(18), playable.ownerOf(18))
        );
    }

    @Test
    void tc29_ownedHexesAreSortedByBoardPosition() {
        PlayableGame playable = PlayableGame.start(game());
        Player current = playable.currentPlayer();
        int firstOwnedPosition = playable.ownedHexes(current).get(0).getPosition();
        giveSettlementCost(playable.inventory(current));
        playable.buildSettlement(18);

        List<Hex> owned = playable.ownedHexes(current);

        assertAll(
            () -> assertEquals(firstOwnedPosition, owned.get(0).getPosition()),
            () -> assertEquals(18, owned.get(1).getPosition())
        );
    }

    @Test
    void tc30_buildSettlementAfterWinRejectedEvenWithValidResourcesAndPosition() {
        PlayableGame playable = PlayableGame.start(game());
        Player winner = playable.currentPlayer();
        buildUntilWin(playable);
        int unowned = unownedNonDesertPosition(playable);
        giveSettlementCost(playable.inventory(winner));

        assertThrows(IllegalStateException.class, () -> playable.buildSettlement(unowned));
    }

    @Test
    void tc31_buyDevelopmentCardAfterWinRejectedEvenWithFunds() {
        PlayableGame playable = PlayableGame.start(game());
        Player winner = playable.currentPlayer();
        buildUntilWin(playable);
        giveDevelopmentCardBudget(playable.inventory(winner));

        assertThrows(IllegalStateException.class, playable::buyDevelopmentCard);
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

    private static DevelopmentCard card(DevelopmentCardType type) {
        return new DevelopmentCard(type);
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

    private static void playKnights(PlayableGame playable, int count) {
        for (int i = 0; i < count; i++) {
            playable.applyDevelopmentCard(card(DevelopmentCardType.KNIGHT));
        }
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

    private static void giveDevelopmentCardBudget(ResourceInventory inventory) {
        inventory.add(ResourceType.ORE, 25);
        inventory.add(ResourceType.WOOL, 25);
        inventory.add(ResourceType.GRAIN, 25);
    }

    private static void buildUntilWin(PlayableGame playable) {
        while (playable.victoryPoints(playable.currentPlayer()) < 10) {
            int position = unownedNonDesertPosition(playable);
            giveSettlementCost(playable.inventory(playable.currentPlayer()));
            playable.buildSettlement(position);
        }
    }
}
