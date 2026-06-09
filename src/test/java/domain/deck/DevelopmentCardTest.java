package domain.deck;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class DevelopmentCardTest {

    @Test
    void tc1_validTypeAccepted() {
        DevelopmentCard card = new DevelopmentCard(DevelopmentCardType.KNIGHT);

        assertEquals(DevelopmentCardType.KNIGHT, card.getType());
    }

    @Test
    void tc2_nullTypeRejected() {
        assertThrows(NullPointerException.class, () -> new DevelopmentCard(null));
    }

    @Test
    void tc4_equalWhenTypesMatch() {
        DevelopmentCard a = new DevelopmentCard(DevelopmentCardType.MONOPOLY);
        DevelopmentCard b = new DevelopmentCard(DevelopmentCardType.MONOPOLY);

        assertAll(
            () -> assertEquals(a, b),
            () -> assertEquals(a.hashCode(), b.hashCode())
        );
    }

    @Test
    void tc5_notEqualWhenTypesDiffer() {
        DevelopmentCard a = new DevelopmentCard(DevelopmentCardType.MONOPOLY);
        DevelopmentCard b = new DevelopmentCard(DevelopmentCardType.KNIGHT);

        assertNotEquals(a, b);
    }

    @Test
    void tc6_notEqualToNullOrOtherTypes() {
        DevelopmentCard card = new DevelopmentCard(DevelopmentCardType.KNIGHT);

        assertAll(
            () -> assertFalse(card.equals(null)),
            () -> assertFalse(card.equals("KNIGHT")),
            () -> assertTrue(card.equals(card)),
            () -> assertNotEquals(0, card.hashCode())
        );
    }
}
