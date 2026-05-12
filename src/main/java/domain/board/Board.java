package domain.board;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public final class Board {

    static final int HEX_COUNT = 19;

    private static final List<TerrainType> STANDARD_TERRAINS = buildStandardTerrains();
    private static final List<Integer> STANDARD_TOKEN_VALUES = Collections.unmodifiableList(
        Arrays.asList(2, 3, 3, 4, 4, 5, 5, 6, 6, 8, 8, 9, 9, 10, 10, 11, 11, 12));

    private final List<Hex> hexes;
    private final Hex desert;

    private Board(List<Hex> hexes, Hex desert) {
        this.hexes = Collections.unmodifiableList(new ArrayList<>(hexes));
        this.desert = desert;
    }

    public static Board generateRandom(Random rng) {
        if (rng == null) {
            throw new IllegalArgumentException("rng must not be null");
        }
        List<TerrainType> terrains = new ArrayList<>(STANDARD_TERRAINS);
        Collections.shuffle(terrains, rng);
        List<Integer> tokens = new ArrayList<>(STANDARD_TOKEN_VALUES);
        Collections.shuffle(tokens, rng);

        List<Hex> hexes = new ArrayList<>(HEX_COUNT);
        Hex desert = null;
        int tokenCursor = 0;
        for (int position = 0; position < HEX_COUNT; position++) {
            TerrainType terrain = terrains.get(position);
            Hex hex;
            if (terrain == TerrainType.DESERT) {
                hex = new Hex(position, terrain, null);
                hex.placeRobber();
                desert = hex;
            } else {
                hex = new Hex(position, terrain, new NumberToken(tokens.get(tokenCursor)));
                tokenCursor++;
            }
            hexes.add(hex);
        }
        return new Board(hexes, desert);
    }

    public List<Hex> getHexes() {
        return hexes;
    }

    public Hex getDesert() {
        return desert;
    }

    public Map<TerrainType, Long> terrainCounts() {
        EnumMap<TerrainType, Long> counts = new EnumMap<>(TerrainType.class);
        for (Hex hex : hexes) {
            counts.merge(hex.getTerrain(), 1L, Long::sum);
        }
        return Collections.unmodifiableMap(counts);
    }

    private static List<TerrainType> buildStandardTerrains() {
        List<TerrainType> list = new ArrayList<>(HEX_COUNT);
        addCopies(list, TerrainType.FOREST, 4);
        addCopies(list, TerrainType.PASTURE, 4);
        addCopies(list, TerrainType.FIELD, 4);
        addCopies(list, TerrainType.HILLS, 3);
        addCopies(list, TerrainType.MOUNTAIN, 3);
        addCopies(list, TerrainType.DESERT, 1);
        return Collections.unmodifiableList(list);
    }

    private static void addCopies(List<TerrainType> list, TerrainType terrain, int count) {
        for (int i = 0; i < count; i++) {
            list.add(terrain);
        }
    }
}
