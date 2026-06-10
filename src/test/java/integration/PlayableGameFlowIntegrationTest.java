package integration;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import domain.board.Hex;
import domain.board.TerrainType;
import domain.deck.DevelopmentCard;
import domain.deck.DevelopmentCardType;
import domain.play.PlayableGame;
import domain.play.ResourceInventory;
import domain.play.ResourceType;
import domain.player.Player;
import domain.player.PlayerColor;
import domain.setup.GameSetup;
import domain.setup.PlayerRegistration;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Full-session integration test for the playable-game feature. One scenario
 * drives the complete stack in order: player registration, {@code GameSetup}
 * build, {@code PlayableGame} start, a production dice roll, a settlement
 * build, a development-card purchase with its effect, turn rotation, and
 * finally the win condition reached through legal play.
 *
 * <p>Mirrors the conventions of the other integration suites: seeded
 * {@code Random(42)}, {@code @Tag("integration")}, {@code assertAll} groupings.
 */
@Tag("integration")
class PlayableGameFlowIntegrationTest {

    @Test
    void fullFlow_registrationThroughProductionBuildingCardPlayAndVictory() {
        PlayableGame playable = startedThreePlayerGame();
        Player first = playable.currentPlayer();
        assertAll(
            () -> assertEquals(1, playable.victoryPoints(first)),
            () -> assertFalse(playable.hasWinner())
        );

        assertProductionRollGrantsMatchingResource(playable, first);
        assertSettlementBuildAwardsOnePoint(playable, first);
        assertDevelopmentCardPurchaseTakesEffect(playable, first);

        Player second = playable.endTurn();
        assertEquals(playable.game().players().get(1), second);

        assertVictoryPointCardDeliversTheWin(playable, second);
    }

    private static void assertProductionRollGrantsMatchingResource(
            PlayableGame playable, Player roller) {
        Hex ownedHex = playable.ownedHexes(roller).get(0);
        int tokenValue = ownedHex.getToken().get().getValue();
        ResourceType expected = ResourceType.fromTerrain(ownedHex.getTerrain()).get();
        int countBefore = playable.inventory(roller).count(expected);

        int produced = playable.rollDice(dieOne(tokenValue), dieTwo(tokenValue));

        assertAll(
            () -> assertTrue(produced >= 1),
            () -> assertTrue(playable.inventory(roller).count(expected) > countBefore)
        );
    }

    private static void assertSettlementBuildAwardsOnePoint(
            PlayableGame playable, Player builder) {
        int pointsBefore = playable.victoryPoints(builder);
        int target = firstUnownedNonDesertPosition(playable);
        giveSettlementCost(playable.inventory(builder));

        playable.buildSettlement(target);

        assertAll(
            () -> assertEquals(Optional.of(builder), playable.ownerOf(target)),
            () -> assertEquals(pointsBefore + 1, playable.victoryPoints(builder))
        );
    }

    private static void assertDevelopmentCardPurchaseTakesEffect(
            PlayableGame playable, Player buyer) {
        giveDevelopmentCardCost(playable.inventory(buyer));
        int deckBefore = playable.game().deck().size();
        CardEffectBaseline baseline = new CardEffectBaseline(playable, buyer);

        DevelopmentCard card = playable.buyDevelopmentCard();

        assertAll(
            () -> assertEquals(deckBefore - 1, playable.game().deck().size()),
            () -> assertCardEffectApplied(playable, buyer, card, baseline)
        );
    }

    private static void assertVictoryPointCardDeliversTheWin(
            PlayableGame playable, Player current) {
        buildSettlementsUntilPoints(playable, current, 9);
        assertFalse(playable.hasWinner());

        playable.applyDevelopmentCard(
            new DevelopmentCard(DevelopmentCardType.VICTORY_POINT));

        assertAll(
            () -> assertEquals(10, playable.victoryPoints(current)),
            () -> assertTrue(playable.hasWinner()),
            () -> assertEquals(Optional.of(current), playable.winner()),
            () -> assertThrows(IllegalStateException.class, playable::endTurn)
        );
    }

    private static void assertCardEffectApplied(
            PlayableGame playable,
            Player buyer,
            DevelopmentCard card,
            CardEffectBaseline baseline) {
        switch (card.getType()) {
            case KNIGHT:
                assertEquals(baseline.knights + 1, playable.knightsPlayed(buyer));
                break;
            case VICTORY_POINT:
                assertEquals(baseline.points + 1, playable.victoryPoints(buyer));
                break;
            case ROAD_BUILDING:
                assertEquals(baseline.roads + 2, playable.roadCount(buyer));
                break;
            case MONOPOLY:
                assertEquals(
                    baseline.totalBrick,
                    playable.inventory(buyer).count(ResourceType.BRICK));
                break;
            case YEAR_OF_PLENTY:
                assertAll(
                    () -> assertEquals(
                        baseline.buyerOre,
                        playable.inventory(buyer).count(ResourceType.ORE)),
                    () -> assertEquals(
                        baseline.buyerGrain,
                        playable.inventory(buyer).count(ResourceType.GRAIN))
                );
                break;
            default:
                throw new AssertionError("unknown card type: " + card.getType());
        }
    }

    private static PlayableGame startedThreePlayerGame() {
        GameSetup setup = new GameSetup(new Random(42));
        setup.registerPlayers(List.of(
            new PlayerRegistration("Yuki", PlayerColor.RED),
            new PlayerRegistration("Minji", PlayerColor.BLUE),
            new PlayerRegistration("Arjun", PlayerColor.WHITE)
        ));
        return PlayableGame.start(setup.build());
    }

    private static void buildSettlementsUntilPoints(
            PlayableGame playable, Player player, int target) {
        while (playable.victoryPoints(player) < target) {
            giveSettlementCost(playable.inventory(player));
            playable.buildSettlement(firstUnownedNonDesertPosition(playable));
        }
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

    private static void giveDevelopmentCardCost(ResourceInventory inventory) {
        inventory.add(ResourceType.ORE, 1);
        inventory.add(ResourceType.WOOL, 1);
        inventory.add(ResourceType.GRAIN, 1);
    }

    private static int dieOne(int sum) {
        return Math.max(1, sum - 6);
    }

    private static int dieTwo(int sum) {
        return sum - dieOne(sum);
    }

    /**
     * Snapshot of every observable a development card can change, taken just
     * before the purchase so each card type's effect can be asserted exactly.
     * The card cost spends one ORE, WOOL, and GRAIN, so Year of Plenty's two
     * default picks (ORE, GRAIN) leave those counts equal to this baseline.
     */
    private static final class CardEffectBaseline {

        private final int points;
        private final int knights;
        private final int roads;
        private final int totalBrick;
        private final int buyerOre;
        private final int buyerGrain;

        CardEffectBaseline(PlayableGame playable, Player buyer) {
            this.points = playable.victoryPoints(buyer);
            this.knights = playable.knightsPlayed(buyer);
            this.roads = playable.roadCount(buyer);
            this.totalBrick = playable.game().players().stream()
                .mapToInt(player -> playable.inventory(player).count(ResourceType.BRICK))
                .sum();
            this.buyerOre = playable.inventory(buyer).count(ResourceType.ORE);
            this.buyerGrain = playable.inventory(buyer).count(ResourceType.GRAIN);
        }
    }
}
