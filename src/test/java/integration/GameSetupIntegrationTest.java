package integration;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import domain.board.Hex;
import domain.board.TerrainType;
import domain.deck.DevelopmentCardType;
import domain.player.Player;
import domain.player.PlayerColor;
import domain.setup.Game;
import domain.setup.GameSetup;
import domain.setup.PlayerRegistration;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("integration")
class GameSetupIntegrationTest {

    @Test
    void gameSetup_buildsCompleteStandardStartingStateForThreePlayers() {
        GameSetup setup = new GameSetup(new Random(42));
        List<PlayerRegistration> registrations = threePlayers();

        setup.registerPlayers(registrations);

        assertCompleteGame(setup.build(), registrations);
    }

    @Test
    void gameSetup_buildsCompleteStandardStartingStateForFourPlayers() {
        GameSetup setup = new GameSetup(new Random(84));
        List<PlayerRegistration> registrations = fourPlayers();

        setup.registerPlayers(registrations);

        assertCompleteGame(setup.build(), registrations);
    }

    @Test
    void gameSetup_rejectsDuplicatePlayerColors() {
        GameSetup setup = new GameSetup(new Random(42));
        List<PlayerRegistration> registrations = List.of(
            new PlayerRegistration("Yuki", PlayerColor.RED),
            new PlayerRegistration("Minji", PlayerColor.RED),
            new PlayerRegistration("Arjun", PlayerColor.WHITE)
        );

        assertThrows(IllegalArgumentException.class,
            () -> setup.registerPlayers(registrations));
    }

    private static void assertCompleteGame(
            Game game,
            List<PlayerRegistration> registrations) {
        assertAll(
            () -> assertPlayersInitialized(game, registrations),
            () -> assertTurnOrderInitialized(game),
            () -> assertDeckInitialized(game),
            () -> assertBoardInitialized(game)
        );
    }

    private static void assertPlayersInitialized(
            Game game,
            List<PlayerRegistration> registrations) {
        assertEquals(registrations.size(), game.players().size());
        for (int i = 0; i < registrations.size(); i++) {
            Player player = game.players().get(i);
            PlayerRegistration registration = registrations.get(i);
            assertAll(
                () -> assertEquals(registration.name(), player.getName()),
                () -> assertEquals(registration.color(), player.getColor())
            );
        }
    }

    private static void assertTurnOrderInitialized(Game game) {
        assertAll(
            () -> assertEquals(game.players().size(), game.turnOrder().size()),
            () -> assertEquals(0, game.turnOrder().currentIndex()),
            () -> assertEquals(game.players().get(0), game.turnOrder().current())
        );
    }

    private static void assertDeckInitialized(Game game) {
        assertAll(
            () -> assertEquals(25, game.deck().size()),
            () -> assertEquals(deckCounts(), game.deck().typeCounts())
        );
    }

    private static void assertBoardInitialized(Game game) {
        List<Integer> tokenValues = game.board().getHexes().stream()
            .filter(hex -> hex.getTerrain() != TerrainType.DESERT)
            .map(hex -> hex.getToken().get().getValue())
            .sorted()
            .collect(Collectors.toList());
        List<Hex> deserts = game.board().getHexes().stream()
            .filter(hex -> hex.getTerrain() == TerrainType.DESERT)
            .collect(Collectors.toList());

        assertAll(
            () -> assertEquals(19, game.board().getHexes().size()),
            () -> assertEquals(terrainCounts(), game.board().terrainCounts()),
            () -> assertEquals(numberTokens(), tokenValues),
            () -> assertEquals(1, deserts.size()),
            () -> assertFalse(deserts.get(0).getToken().isPresent()),
            () -> assertEquals(game.board().getDesert(), deserts.get(0)),
            () -> assertTrue(game.board().getDesert().hasRobber()),
            () -> game.board().getHexes().stream()
                .filter(hex -> hex.getTerrain() != TerrainType.DESERT)
                .forEach(hex -> assertAll(
                    () -> assertTrue(hex.getToken().isPresent()),
                    () -> assertFalse(hex.hasRobber())
                ))
        );
    }

    private static List<PlayerRegistration> threePlayers() {
        return List.of(
            new PlayerRegistration("Yuki", PlayerColor.RED),
            new PlayerRegistration("Minji", PlayerColor.BLUE),
            new PlayerRegistration("Arjun", PlayerColor.WHITE)
        );
    }

    private static List<PlayerRegistration> fourPlayers() {
        return List.of(
            new PlayerRegistration("Yuki", PlayerColor.RED),
            new PlayerRegistration("Minji", PlayerColor.BLUE),
            new PlayerRegistration("Arjun", PlayerColor.WHITE),
            new PlayerRegistration("Hana", PlayerColor.ORANGE)
        );
    }

    private static List<Integer> numberTokens() {
        return Collections.unmodifiableList(
            Arrays.asList(
                2, 3, 3, 4, 4, 5, 5, 6, 6,
                8, 8, 9, 9, 10, 10, 11, 11, 12));
    }

    private static Map<TerrainType, Long> terrainCounts() {
        Map<TerrainType, Long> counts = new HashMap<>();
        counts.put(TerrainType.FOREST, 4L);
        counts.put(TerrainType.PASTURE, 4L);
        counts.put(TerrainType.FIELD, 4L);
        counts.put(TerrainType.HILLS, 3L);
        counts.put(TerrainType.MOUNTAIN, 3L);
        counts.put(TerrainType.DESERT, 1L);
        return Collections.unmodifiableMap(counts);
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
