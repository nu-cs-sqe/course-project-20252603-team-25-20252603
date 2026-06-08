package domain.play;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import domain.board.TerrainType;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class ResourceTypeTest {

    @Test
    void tc1_forestMapsToLumber() {
        assertEquals(
            Optional.of(ResourceType.LUMBER),
            ResourceType.fromTerrain(TerrainType.FOREST));
    }

    @Test
    void tc2_pastureMapsToWool() {
        assertEquals(
            Optional.of(ResourceType.WOOL),
            ResourceType.fromTerrain(TerrainType.PASTURE));
    }

    @Test
    void tc3_fieldMapsToGrain() {
        assertEquals(
            Optional.of(ResourceType.GRAIN),
            ResourceType.fromTerrain(TerrainType.FIELD));
    }

    @Test
    void tc4_hillsMapsToBrick() {
        assertEquals(
            Optional.of(ResourceType.BRICK),
            ResourceType.fromTerrain(TerrainType.HILLS));
    }

    @Test
    void tc5_mountainMapsToOre() {
        assertEquals(
            Optional.of(ResourceType.ORE),
            ResourceType.fromTerrain(TerrainType.MOUNTAIN));
    }

    @Test
    void tc6_desertMapsToEmpty() {
        assertEquals(Optional.empty(), ResourceType.fromTerrain(TerrainType.DESERT));
    }

    @Test
    void tc7_nullTerrainRejected() {
        assertThrows(NullPointerException.class, () -> ResourceType.fromTerrain(null));
    }
}
