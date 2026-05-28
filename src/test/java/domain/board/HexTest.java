package domain.board;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class HexTest {

    @Test
    void tc1_lowerPositionBoundaryValidNonDesertHex() {
        Hex hex = new Hex(0, TerrainType.FOREST, new NumberToken(8));
        assertAll(
            () -> assertEquals(0, hex.getPosition()),
            () -> assertEquals(TerrainType.FOREST, hex.getTerrain()),
            () -> assertTrue(hex.getToken().isPresent()),
            () -> assertEquals(8, hex.getToken().get().getValue()),
            () -> assertFalse(hex.hasRobber())
        );
    }

    @Test
    void tc2_upperPositionBoundary() {
        Hex hex = new Hex(18, TerrainType.MOUNTAIN, new NumberToken(6));
        assertEquals(18, hex.getPosition());
    }

    @Test
    void tc3_positionJustBelowRangeRejected() {
        assertThrows(IllegalArgumentException.class,
            () -> new Hex(-1, TerrainType.FOREST, new NumberToken(5)));
    }

    @Test
    void tc4_positionJustAboveRangeRejected() {
        assertThrows(IllegalArgumentException.class,
            () -> new Hex(19, TerrainType.FOREST, new NumberToken(5)));
    }

    @Test
    void tc5_nullTerrainRejected() {
        assertThrows(NullPointerException.class,
            () -> new Hex(5, null, new NumberToken(5)));
    }

    @Test
    void tc6_desertWithNullTokenIsValid() {
        Hex hex = new Hex(9, TerrainType.DESERT, null);
        assertFalse(hex.getToken().isPresent());
    }

    @Test
    void tc7_desertWithNonNullTokenRejected() {
        assertThrows(IllegalArgumentException.class,
            () -> new Hex(9, TerrainType.DESERT, new NumberToken(8)));
    }

    @Test
    void tc8_nonDesertWithNullTokenRejected() {
        assertThrows(IllegalArgumentException.class,
            () -> new Hex(9, TerrainType.HILLS, null));
    }

    @Test
    void tc9_getTokenReturnsEmptyForDesert() {
        Hex hex = new Hex(9, TerrainType.DESERT, null);
        assertFalse(hex.getToken().isPresent());
    }

    @Test
    void tc10_robberAbsentByDefault() {
        Hex hex = new Hex(0, TerrainType.FOREST, new NumberToken(8));
        assertFalse(hex.hasRobber());
    }

    @Test
    void tc11_placeRobberSetsRobberPresent() {
        Hex hex = new Hex(0, TerrainType.FOREST, new NumberToken(8));
        hex.placeRobber();
        assertTrue(hex.hasRobber());
    }

    @Test
    void tc12_placeRobberIsIdempotent() {
        Hex hex = new Hex(0, TerrainType.FOREST, new NumberToken(8));
        hex.placeRobber();
        hex.placeRobber();
        assertTrue(hex.hasRobber());
    }

    @Test
    void tc13_removeRobberClearsTheRobber() {
        Hex hex = new Hex(0, TerrainType.FOREST, new NumberToken(8));
        hex.placeRobber();
        hex.removeRobber();
        assertFalse(hex.hasRobber());
    }
}
