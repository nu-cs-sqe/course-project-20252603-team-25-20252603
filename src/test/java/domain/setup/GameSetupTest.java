package domain.setup;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import domain.deck.DevelopmentCardType;
import domain.player.PlayerColor;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.junit.jupiter.api.Test;

class GameSetupTest {

    @Test
    void tc1_nullRandomRejected() {
        assertThrows(NullPointerException.class, () -> new GameSetup(null));
    }

    @Test
    void tc2_nullRegistrationsRejected() {
        GameSetup setup = new GameSetup(new Random(1));

        assertThrows(IllegalArgumentException.class, () -> setup.registerPlayers(null));
    }

    @Test
    void tc3_twoPlayersRejected() {
        GameSetup setup = new GameSetup(new Random(1));

        assertThrows(IllegalArgumentException.class,
            () -> setup.registerPlayers(players(2)));
    }

    @Test
    void tc4_threePlayersAcceptedAndBuildsGame() {
        GameSetup setup = new GameSetup(new Random(1));

        setup.registerPlayers(players(3));
        Game game = setup.build();

        assertAll(
            () -> assertEquals(3, game.players().size()),
            () -> assertNotNull(game.board()),
            () -> assertEquals(25, game.deck().size()),
            () -> assertEquals(game.players().get(0), game.turnOrder().current())
        );
    }

    @Test
    void tc5_fourPlayersAcceptedAndBuildsGame() {
        GameSetup setup = new GameSetup(new Random(1));

        setup.registerPlayers(players(4));
        Game game = setup.build();

        assertEquals(4, game.players().size());
    }

    @Test
    void tc6_fivePlayersRejected() {
        GameSetup setup = new GameSetup(new Random(1));

        assertThrows(IllegalArgumentException.class,
            () -> setup.registerPlayers(players(5)));
    }

    @Test
    void tc7_nullRegistrationEntryRejected() {
        GameSetup setup = new GameSetup(new Random(1));
        List<PlayerRegistration> registrations = new ArrayList<>(players(3));
        registrations.set(1, null);

        assertThrows(IllegalArgumentException.class,
            () -> setup.registerPlayers(registrations));
    }

    @Test
    void tc8_duplicateColorsRejected() {
        GameSetup setup = new GameSetup(new Random(1));
        List<PlayerRegistration> registrations = List.of(
            new PlayerRegistration("Yuki", PlayerColor.RED),
            new PlayerRegistration("Minji", PlayerColor.RED),
            new PlayerRegistration("Arjun", PlayerColor.WHITE)
        );

        assertThrows(IllegalArgumentException.class,
            () -> setup.registerPlayers(registrations));
    }

    @Test
    void tc9_duplicateNamesRejected() {
        GameSetup setup = new GameSetup(new Random(1));
        List<PlayerRegistration> registrations = List.of(
            new PlayerRegistration("Yuki", PlayerColor.RED),
            new PlayerRegistration("Yuki", PlayerColor.BLUE),
            new PlayerRegistration("Arjun", PlayerColor.WHITE)
        );

        assertThrows(IllegalArgumentException.class,
            () -> setup.registerPlayers(registrations));
    }

    @Test
    void tc10_buildBeforeRegistrationRejected() {
        GameSetup setup = new GameSetup(new Random(1));

        assertThrows(IllegalStateException.class, setup::build);
    }

    @Test
    void tc11_registrationNamesAreTrimmed() {
        GameSetup setup = new GameSetup(new Random(1));
        List<PlayerRegistration> registrations = List.of(
            new PlayerRegistration("  Yuki  ", PlayerColor.RED),
            new PlayerRegistration("Minji", PlayerColor.BLUE),
            new PlayerRegistration("Arjun", PlayerColor.WHITE)
        );

        setup.registerPlayers(registrations);

        assertEquals("Yuki", setup.build().players().get(0).getName());
    }

    @Test
    void tc12_gamePlayersListIsUnmodifiable() {
        GameSetup setup = new GameSetup(new Random(1));
        setup.registerPlayers(players(3));

        Game game = setup.build();

        assertThrows(UnsupportedOperationException.class,
            () -> game.players().add(game.players().get(0)));
    }

    @Test
    void tc13_registerPlayersDefensivelyCopiesRegistrationList() {
        GameSetup setup = new GameSetup(new Random(1));
        List<PlayerRegistration> registrations = new ArrayList<>(players(3));

        setup.registerPlayers(registrations);
        registrations.clear();

        assertEquals(3, setup.build().players().size());
    }

    @Test
    void tc14_buildCreatesStandardDeckAndBoard() {
        GameSetup setup = new GameSetup(new Random(1));
        setup.registerPlayers(players(3));

        Game game = setup.build();

        assertAll(
            () -> assertNotNull(game.board()),
            () -> assertEquals(deckCounts(), game.deck().typeCounts())
        );
    }

    private static List<PlayerRegistration> players(int count) {
        List<PlayerRegistration> registrations = new ArrayList<>();
        PlayerColor[] colors = PlayerColor.values();
        String[] names = {"Yuki", "Minji", "Arjun", "Hana", "Kenji"};
        for (int i = 0; i < count; i++) {
            registrations.add(new PlayerRegistration(names[i], colors[i % colors.length]));
        }
        return registrations;
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
