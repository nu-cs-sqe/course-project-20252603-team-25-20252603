package domain.board;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class NumberTokenTest {

    @Test
    void tc1_lowerValidBoundary() {
        NumberToken token = new NumberToken(2);
        assertEquals(2, token.getValue());
    }

    @Test
    void tc2_upperValidBoundary() {
        NumberToken token = new NumberToken(12);
        assertEquals(12, token.getValue());
    }

    @Test
    void tc3_justBelowValidRangeRejected() {
        assertThrows(IllegalArgumentException.class, () -> new NumberToken(1));
    }

    @Test
    void tc4_justAboveValidRangeRejected() {
        assertThrows(IllegalArgumentException.class, () -> new NumberToken(13));
    }

    @Test
    void tc5_holeAtSevenRejected() {
        assertThrows(IllegalArgumentException.class, () -> new NumberToken(7));
    }

    @Test
    void tc6_justBelowTheHoleValid() {
        NumberToken token = new NumberToken(6);
        assertEquals(6, token.getValue());
    }

    @Test
    void tc7_justAboveTheHoleValid() {
        NumberToken token = new NumberToken(8);
        assertEquals(8, token.getValue());
    }

    @Test
    void tc8_farBelowValidRangeRejected() {
        assertThrows(IllegalArgumentException.class, () -> new NumberToken(0));
    }

    @Test
    void tc9_equalWhenValuesMatch() {
        NumberToken a = new NumberToken(8);
        NumberToken b = new NumberToken(8);
        assertAll(
            () -> assertEquals(a, b),
            () -> assertEquals(a.hashCode(), b.hashCode()),
            () -> assertNotEquals(0, a.hashCode()),
            () -> assertEquals(a, a)
        );
    }

    @Test
    void tc10_notEqualWhenValuesDifferOrTypesDiffer() {
        NumberToken a = new NumberToken(8);
        NumberToken b = new NumberToken(9);
        assertAll(
            () -> assertNotEquals(a, b),
            () -> assertFalse(a.equals(null)),
            () -> assertFalse(a.equals("8"))
        );
    }
}
