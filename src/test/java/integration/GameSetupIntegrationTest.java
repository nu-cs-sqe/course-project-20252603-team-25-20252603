package integration;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import domain.board.TerrainType;
import domain.deck.DevelopmentCardType;
import domain.player.PlayerColor;
import domain.setup.Game;
import domain.setup.GameSetup;
import domain.setup.PlayerRegistration;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.junit.jupiter.api.Test;

class GameSetupIntegrationTest {

    @Test
    void gameSetup_buildsCompleteStandardStartingState() {
        GameSetup setup = new GameSetup(new Random(42));
        setup.registerPlayers(List.of(
            new PlayerRegistration("Alice", PlayerColor.RED),
            new PlayerRegistration("Bob", PlayerColor.BLUE),
            new PlayerRegistration("Cara", PlayerColor.WHITE)
        ));

        Game game = setup.build();

        assertAll(
            () -> assertEquals(3, game.players().size()),
            () -> assertEquals(terrainCounts(), game.board().terrainCounts()),
            () -> assertTrue(game.board().getDesert().hasRobber()),
            () -> assertTrue(game.board().getDesert().getToken().isEmpty()),
            () -> assertEquals(deckCounts(), game.deck().typeCounts()),
            () -> assertEquals(game.players().get(0), game.turnOrder().current())
        );
    }

    private static Map<TerrainType, Long> terrainCounts() {
        EnumMap<TerrainType, Long> counts = new EnumMap<>(TerrainType.class);
        counts.put(TerrainType.FOREST, 4L);
        counts.put(TerrainType.PASTURE, 4L);
        counts.put(TerrainType.FIELD, 4L);
        counts.put(TerrainType.HILLS, 3L);
        counts.put(TerrainType.MOUNTAIN, 3L);
        counts.put(TerrainType.DESERT, 1L);
        return counts;
    }

    private static Map<DevelopmentCardType, Long> deckCounts() {
        EnumMap<DevelopmentCardType, Long> counts =
            new EnumMap<>(DevelopmentCardType.class);
        counts.put(DevelopmentCardType.KNIGHT, 14L);
        counts.put(DevelopmentCardType.VICTORY_POINT, 5L);
        counts.put(DevelopmentCardType.ROAD_BUILDING, 2L);
        counts.put(DevelopmentCardType.MONOPOLY, 2L);
        counts.put(DevelopmentCardType.YEAR_OF_PLENTY, 2L);
        return counts;
    }
}
