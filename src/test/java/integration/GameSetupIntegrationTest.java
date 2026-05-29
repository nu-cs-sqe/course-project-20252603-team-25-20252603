package integration;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import domain.deck.DevelopmentCardType;
import domain.player.PlayerColor;
import domain.setup.Game;
import domain.setup.GameSetup;
import domain.setup.PlayerRegistration;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("integration")
class GameSetupIntegrationTest {

    @Test
    void gameSetup_buildsCompleteStandardStartingState() {
        GameSetup setup = new GameSetup(new Random(42));
        setup.registerPlayers(List.of(
            new PlayerRegistration("Yuki", PlayerColor.RED),
            new PlayerRegistration("Minji", PlayerColor.BLUE),
            new PlayerRegistration("Arjun", PlayerColor.WHITE)
        ));

        Game game = setup.build();

        assertAll(
            () -> assertEquals(3, game.players().size()),
            () -> assertNotNull(game.board()),
            () -> assertEquals(deckCounts(), game.deck().typeCounts()),
            () -> assertEquals(game.players().get(0), game.turnOrder().current())
        );
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
