package domain.player;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class PlayerTest {

    @Test
    void tc1_validNameAndColor() {
        Player p = new Player("Daniel", PlayerColor.RED);
        assertAll(
            () -> assertEquals("Daniel", p.getName()),
            () -> assertEquals(PlayerColor.RED, p.getColor())
        );
    }

    @Test
    void tc2_nameWithSurroundingWhitespaceIsTrimmed() {
        Player p = new Player("  Daniel  ", PlayerColor.BLUE);
        assertEquals("Daniel", p.getName());
    }

    @Test
    void tc3_nullNameRejected() {
        assertThrows(NullPointerException.class,
            () -> new Player(null, PlayerColor.RED));
    }

    @Test
    void tc4_emptyNameRejected() {
        assertThrows(IllegalArgumentException.class,
            () -> new Player("", PlayerColor.RED));
    }

    @Test
    void tc5_whitespaceOnlyNameRejected() {
        assertThrows(IllegalArgumentException.class,
            () -> new Player("   ", PlayerColor.RED));
    }

    @Test
    void tc6_nullColorRejected() {
        assertThrows(NullPointerException.class,
            () -> new Player("Daniel", null));
    }

    @Test
    void tc9_equalWhenNameAndColorMatch() {
        Player a = new Player("Daniel", PlayerColor.RED);
        Player b = new Player("Daniel", PlayerColor.RED);
        assertAll(
            () -> assertEquals(a, b),
            () -> assertEquals(a.hashCode(), b.hashCode())
        );
    }

    @Test
    void tc10_notEqualWhenNamesDiffer() {
        Player a = new Player("Daniel", PlayerColor.RED);
        Player b = new Player("Julian", PlayerColor.RED);
        assertNotEquals(a, b);
    }

    @Test
    void tc11_notEqualWhenColorsDiffer() {
        Player a = new Player("Daniel", PlayerColor.RED);
        Player b = new Player("Daniel", PlayerColor.BLUE);
        assertNotEquals(a, b);
    }

    @Test
    void tc12_notEqualToNullOrOtherTypes() {
        Player a = new Player("Daniel", PlayerColor.RED);
        assertAll(
            () -> assertFalse(a.equals(null)),
            () -> assertFalse(a.equals("Daniel")),
            () -> assertTrue(a.equals(a))
        );
    }
}
