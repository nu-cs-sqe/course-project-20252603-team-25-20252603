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
            new PlayerRegistration("Yuki", PlayerColor.RED);

        assertAll(
            () -> assertEquals("Yuki", registration.name()),
            () -> assertEquals(PlayerColor.RED, registration.color())
        );
    }

    @Test
    void tc2_nameWithWhitespaceIsTrimmed() {
        PlayerRegistration registration =
            new PlayerRegistration("  Yuki  ", PlayerColor.BLUE);

        assertEquals("Yuki", registration.name());
    }

    @Test
    void tc3_nullNameRejected() {
        assertThrows(NullPointerException.class,
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
        assertThrows(NullPointerException.class,
            () -> new PlayerRegistration("Yuki", null));
    }

    @Test
    void tc9_equalWhenNameAndColorMatch() {
        PlayerRegistration a = new PlayerRegistration("Yuki", PlayerColor.RED);
        PlayerRegistration b = new PlayerRegistration("Yuki", PlayerColor.RED);

        assertAll(
            () -> assertEquals(a, b),
            () -> assertEquals(a.hashCode(), b.hashCode())
        );
    }

    @Test
    void tc10_notEqualWhenNamesDiffer() {
        PlayerRegistration a = new PlayerRegistration("Yuki", PlayerColor.RED);
        PlayerRegistration b = new PlayerRegistration("Minji", PlayerColor.RED);

        assertNotEquals(a, b);
    }

    @Test
    void tc11_notEqualWhenColorsDiffer() {
        PlayerRegistration a = new PlayerRegistration("Yuki", PlayerColor.RED);
        PlayerRegistration b = new PlayerRegistration("Yuki", PlayerColor.BLUE);

        assertNotEquals(a, b);
    }

    @Test
    void tc12_notEqualToNullOrOtherTypes() {
        PlayerRegistration registration =
            new PlayerRegistration("Yuki", PlayerColor.RED);

        assertAll(
            () -> assertFalse(registration.equals(null)),
            () -> assertFalse(registration.equals("Yuki")),
            () -> assertTrue(registration.equals(registration))
        );
    }
}
