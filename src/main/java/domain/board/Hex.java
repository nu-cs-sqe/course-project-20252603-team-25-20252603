package domain.board;

import java.util.Objects;
import java.util.Optional;

/**
 * One of the 19 hexes that make up the standard CATAN board. Carries a
 * terrain, an optional number token (absent only on the desert), and a
 * mutable robber flag.
 */
public final class Hex {

    static final int MIN_POSITION = 0;
    static final int MAX_POSITION = 18;

    private final int position;
    private final TerrainType terrain;
    private final NumberToken token;
    private boolean robber;

    /**
     * Creates a hex at the given position with the given terrain and token.
     *
     * @param position board slot in {@code [0, 18]}
     * @param terrain  non-null terrain printed on the hex
     * @param token    number chit; must be {@code null} iff {@code terrain == DESERT}
     * @throws IllegalArgumentException if {@code position} is out of range or
     *                                  if the terrain/token pairing is invalid
     * @throws NullPointerException     if {@code terrain} is null
     */
    public Hex(int position, TerrainType terrain, NumberToken token) {
        if (position < MIN_POSITION || position > MAX_POSITION) {
            throw new IllegalArgumentException(
                "position must be in [" + MIN_POSITION + ", " + MAX_POSITION
                    + "]; got " + position);
        }
        Objects.requireNonNull(terrain, "terrain must not be null");
        if (terrain == TerrainType.DESERT && token != null) {
            throw new IllegalArgumentException("DESERT hex must not have a number token");
        }
        if (terrain != TerrainType.DESERT && token == null) {
            throw new IllegalArgumentException(
                "non-DESERT hex must have a number token; terrain=" + terrain);
        }
        this.position = position;
        this.terrain = terrain;
        this.token = token;
        this.robber = false;
    }

    public int getPosition() {
        return position;
    }

    public TerrainType getTerrain() {
        return terrain;
    }

    public Optional<NumberToken> getToken() {
        return Optional.ofNullable(token);
    }

    public boolean hasRobber() {
        return robber;
    }

    public void placeRobber() {
        this.robber = true;
    }

    public void removeRobber() {
        this.robber = false;
    }
}
