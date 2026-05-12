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

    private static final Player YUKI = new Player("Yuki", PlayerColor.RED);
    private static final Player MINJI = new Player("Minji", PlayerColor.BLUE);
    private static final Player ARJUN = new Player("Arjun", PlayerColor.WHITE);

    @Test
    void tc1_validTwoPlayerOrderStartsAtFirstPlayer() {
        TurnOrder order = new TurnOrder(List.of(YUKI, MINJI));

        assertAll(
            () -> assertEquals(YUKI, order.current()),
            () -> assertEquals(0, order.currentIndex()),
            () -> assertEquals(2, order.size())
        );
    }

    @Test
    void tc2_validThreePlayerOrderAdvancesInRegistrationOrder() {
        TurnOrder order = new TurnOrder(List.of(YUKI, MINJI, ARJUN));

        assertAll(
            () -> assertEquals(MINJI, order.advance()),
            () -> assertEquals(1, order.currentIndex()),
            () -> assertEquals(ARJUN, order.advance())
        );
    }

    @Test
    void tc3_advanceWrapsFromLastPlayerToFirstPlayer() {
        TurnOrder order = new TurnOrder(List.of(YUKI, MINJI));

        order.advance();

        assertAll(
            () -> assertEquals(YUKI, order.advance()),
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
        assertThrows(IllegalArgumentException.class, () -> new TurnOrder(List.of(YUKI)));
    }

    @Test
    void tc7_nullPlayerEntryRejected() {
        List<Player> players = new ArrayList<>();
        players.add(YUKI);
        players.add(null);

        assertThrows(IllegalArgumentException.class, () -> new TurnOrder(players));
    }

    @Test
    void tc8_samePlayerInstanceDuplicateRejected() {
        assertThrows(IllegalArgumentException.class,
            () -> new TurnOrder(List.of(YUKI, YUKI)));
    }

    @Test
    void tc9_equalPlayerDuplicateRejected() {
        Player duplicateYuki = new Player("Yuki", PlayerColor.RED);

        assertThrows(IllegalArgumentException.class,
            () -> new TurnOrder(List.of(YUKI, duplicateYuki)));
    }

    @Test
    void tc10_constructorDefensivelyCopiesPlayerList() {
        List<Player> players = new ArrayList<>(List.of(YUKI, MINJI));
        TurnOrder order = new TurnOrder(players);

        players.add(ARJUN);

        assertAll(
            () -> assertEquals(2, order.size()),
            () -> assertEquals(YUKI, order.current())
        );
    }

    @Test
    void tc11_currentIsStableUntilAdvanceIsCalled() {
        TurnOrder order = new TurnOrder(List.of(YUKI, MINJI));

        assertAll(
            () -> assertEquals(YUKI, order.current()),
            () -> assertEquals(YUKI, order.current())
        );
    }
}
