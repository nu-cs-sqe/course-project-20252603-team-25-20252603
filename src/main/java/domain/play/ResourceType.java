package domain.play;

import domain.board.TerrainType;
import java.util.Optional;

/**
 * Resources produced by non-desert CATAN terrain.
 */
public enum ResourceType {
    LUMBER,
    WOOL,
    GRAIN,
    BRICK,
    ORE;

    /**
     * Converts a terrain to the resource it produces.
     *
     * @param terrain non-null terrain
     * @return produced resource, or empty for desert
     * @throws NullPointerException if {@code terrain} is null
     */
    public static Optional<ResourceType> fromTerrain(TerrainType terrain) {
        switch (terrain) {
            case FOREST:
                return Optional.of(LUMBER);
            case PASTURE:
                return Optional.of(WOOL);
            case FIELD:
                return Optional.of(GRAIN);
            case HILLS:
                return Optional.of(BRICK);
            case MOUNTAIN:
                return Optional.of(ORE);
            case DESERT:
                return Optional.empty();
            default:
                throw new IllegalArgumentException("unknown terrain: " + terrain);
        }
    }
}
