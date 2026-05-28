package domain.board;

/**
 * Terrain printed on each hex. Every non-{@link #DESERT} hex carries a
 * resource and a {@link NumberToken}; the desert produces nothing and is
 * where the robber starts.
 */
public enum TerrainType {
    FOREST,
    PASTURE,
    FIELD,
    HILLS,
    MOUNTAIN,
    DESERT
}
