package domain.turn;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import domain.player.Player;
import domain.player.PlayerColor;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class TurnOrderTest {

    private static final Player ALICE = new Player("Alice", PlayerColor.RED);
    private static final Player BOB = new Player("Bob", PlayerColor.BLUE);
    private static final Player CARA = new Player("Cara", PlayerColor.WHITE);

    @Test
    void tc1_validTwoPlayerOrderStartsAtFirstPlayer() {
        TurnOrder order = new TurnOrder(List.of(ALICE, BOB));

        assertAll(
            () -> assertEquals(ALICE, order.current()),
            () -> assertEquals(0, order.currentIndex()),
            () -> assertEquals(2, order.size())
        );
    }

    @Test
    void tc2_validThreePlayerOrderAdvancesInRegistrationOrder() {
        TurnOrder order = new TurnOrder(List.of(ALICE, BOB, CARA));

        assertAll(
            () -> assertEquals(BOB, order.advance()),
            () -> assertEquals(1, order.currentIndex()),
            () -> assertEquals(CARA, order.advance())
        );
    }

    @Test
    void tc3_advanceWrapsFromLastPlayerToFirstPlayer() {
        TurnOrder order = new TurnOrder(List.of(ALICE, BOB));

        order.advance();

        assertAll(
            () -> assertEquals(ALICE, order.advance()),
            () -> assertEquals(0, order.currentIndex())
        );
    }

    @Test
    void tc4_nullPlayerListRejected() {
        assertThrows(IllegalArgumentException.class, () -> new TurnOrder(null));
    }

    @Test
    void tc5_emptyPlayerListRejected() {
        assertThrows(IllegalArgumentException.class, () -> new TurnOrder(List.of()));
    }

    @Test
    void tc6_singlePlayerListRejected() {
        assertThrows(IllegalArgumentException.class, () -> new TurnOrder(List.of(ALICE)));
    }

    @Test
    void tc7_nullPlayerEntryRejected() {
        List<Player> players = new ArrayList<>();
        players.add(ALICE);
        players.add(null);

        assertThrows(IllegalArgumentException.class, () -> new TurnOrder(players));
    }

    @Test
    void tc8_samePlayerInstanceDuplicateRejected() {
        assertThrows(IllegalArgumentException.class,
            () -> new TurnOrder(List.of(ALICE, ALICE)));
    }

    @Test
    void tc9_equalPlayerDuplicateRejected() {
        Player duplicateAlice = new Player("Alice", PlayerColor.RED);

        assertThrows(IllegalArgumentException.class,
            () -> new TurnOrder(List.of(ALICE, duplicateAlice)));
    }

    @Test
    void tc10_constructorDefensivelyCopiesPlayerList() {
        List<Player> players = new ArrayList<>(List.of(ALICE, BOB));
        TurnOrder order = new TurnOrder(players);

        players.add(CARA);

        assertAll(
            () -> assertEquals(2, order.size()),
            () -> assertEquals(ALICE, order.current())
        );
    }

    @Test
    void tc11_currentIsStableUntilAdvanceIsCalled() {
        TurnOrder order = new TurnOrder(List.of(ALICE, BOB));

        assertAll(
            () -> assertEquals(ALICE, order.current()),
            () -> assertEquals(ALICE, order.current())
        );
    }
}
