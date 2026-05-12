package domain.setup;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import domain.player.PlayerColor;
import org.junit.jupiter.api.Test;

class PlayerRegistrationTest {

    @Test
    void tc1_validNameAndColor() {
        PlayerRegistration registration =
            new PlayerRegistration("Alice", PlayerColor.RED);

        assertAll(
            () -> assertEquals("Alice", registration.name()),
            () -> assertEquals(PlayerColor.RED, registration.color())
        );
    }

    @Test
    void tc2_nameWithWhitespaceIsTrimmed() {
        PlayerRegistration registration =
            new PlayerRegistration("  Alice  ", PlayerColor.BLUE);

        assertEquals("Alice", registration.name());
    }

    @Test
    void tc3_nullNameRejected() {
        assertThrows(IllegalArgumentException.class,
            () -> new PlayerRegistration(null, PlayerColor.RED));
    }

    @Test
    void tc4_emptyNameRejected() {
        assertThrows(IllegalArgumentException.class,
            () -> new PlayerRegistration("", PlayerColor.RED));
    }

    @Test
    void tc5_blankNameRejected() {
        assertThrows(IllegalArgumentException.class,
            () -> new PlayerRegistration("   ", PlayerColor.RED));
    }

    @Test
    void tc6_nullColorRejected() {
        assertThrows(IllegalArgumentException.class,
            () -> new PlayerRegistration("Alice", null));
    }

    @Test
    void tc9_equalWhenNameAndColorMatch() {
        PlayerRegistration a = new PlayerRegistration("Alice", PlayerColor.RED);
        PlayerRegistration b = new PlayerRegistration("Alice", PlayerColor.RED);

        assertAll(
            () -> assertEquals(a, b),
            () -> assertEquals(a.hashCode(), b.hashCode())
        );
    }

    @Test
    void tc10_notEqualWhenNamesDiffer() {
        PlayerRegistration a = new PlayerRegistration("Alice", PlayerColor.RED);
        PlayerRegistration b = new PlayerRegistration("Bob", PlayerColor.RED);

        assertNotEquals(a, b);
    }

    @Test
    void tc11_notEqualWhenColorsDiffer() {
        PlayerRegistration a = new PlayerRegistration("Alice", PlayerColor.RED);
        PlayerRegistration b = new PlayerRegistration("Alice", PlayerColor.BLUE);

        assertNotEquals(a, b);
    }

    @Test
    void tc12_notEqualToNullOrOtherTypes() {
        PlayerRegistration registration =
            new PlayerRegistration("Alice", PlayerColor.RED);

        assertAll(
            () -> assertFalse(registration.equals(null)),
            () -> assertFalse(registration.equals("Alice")),
            () -> assertTrue(registration.equals(registration))
        );
    }
}
