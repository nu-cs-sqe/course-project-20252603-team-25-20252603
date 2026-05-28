package domain.board;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class BoardTest {

    private static final Map<TerrainType, Long> STANDARD_TERRAIN_COUNTS = standardTerrainCounts();
    private static final List<Integer> STANDARD_TOKENS = Collections.unmodifiableList(
        Arrays.asList(2, 3, 3, 4, 4, 5, 5, 6, 6, 8, 8, 9, 9, 10, 10, 11, 11, 12));

    @Test
    void tc1_producesExactlyNineteenHexes() {
        Board board = Board.generateRandom(new Random(0L));
        assertEquals(19, board.getHexes().size());
    }

    @Test
    void tc2_positionsAreExactlyZeroThroughEighteen() {
        Board board = Board.generateRandom(new Random(0L));
        Set<Integer> positions = board.getHexes().stream()
            .map(Hex::getPosition)
            .collect(Collectors.toCollection(HashSet::new));
        Set<Integer> expected = new HashSet<>();
        for (int i = 0; i <= 18; i++) {
            expected.add(i);
        }
        assertEquals(expected, positions);
    }

    @Test
    void tc3_terrainMultisetMatchesStandardDistribution() {
        Board board = Board.generateRandom(new Random(0L));
        assertEquals(STANDARD_TERRAIN_COUNTS, board.terrainCounts());
    }

    @Test
    void tc4_exactlyOneDesertHexAndItCarriesNoToken() {
        Board board = Board.generateRandom(new Random(0L));
        List<Hex> deserts = board.getHexes().stream()
            .filter(h -> h.getTerrain() == TerrainType.DESERT)
            .collect(Collectors.toList());
        assertAll(
            () -> assertEquals(1, deserts.size()),
            () -> assertFalse(deserts.get(0).getToken().isPresent())
        );
    }

    @Test
    void tc5_allNonDesertHexesCarryTokens() {
        Board board = Board.generateRandom(new Random(0L));
        board.getHexes().stream()
            .filter(h -> h.getTerrain() != TerrainType.DESERT)
            .forEach(h -> assertTrue(h.getToken().isPresent()));
    }

    @Test
    void tc6_tokenMultisetOverNonDesertHexesMatchesStandardSet() {
        Board board = Board.generateRandom(new Random(0L));
        List<Integer> values = board.getHexes().stream()
            .filter(h -> h.getTerrain() != TerrainType.DESERT)
            .map(h -> h.getToken().get().getValue())
            .sorted()
            .collect(Collectors.toList());
        assertEquals(STANDARD_TOKENS, values);
    }

    @Test
    void tc7_robberStartsOnTheDesertHex() {
        Board board = Board.generateRandom(new Random(0L));
        assertTrue(board.getDesert().hasRobber());
    }

    @Test
    void tc8_robberIsOnNoOtherHex() {
        Board board = Board.generateRandom(new Random(0L));
        board.getHexes().stream()
            .filter(h -> h.getTerrain() != TerrainType.DESERT)
            .forEach(h -> assertFalse(h.hasRobber()));
    }

    @Test
    void tc9_determinismSameSeedProducesSameBoard() {
        Board first = Board.generateRandom(new Random(42L));
        Board second = Board.generateRandom(new Random(42L));
        List<String> firstSignature = signature(first);
        List<String> secondSignature = signature(second);
        assertEquals(firstSignature, secondSignature);
    }

    @Test
    void tc10_nullRandomRejected() {
        assertThrows(NullPointerException.class, () -> Board.generateRandom(null));
    }

    @Test
    void tc11_allInvariantsHoldAcrossSampleOfSeeds() {
        long[] seeds = {0L, 1L, 2L, 7L, 42L, 100L};
        for (long seed : seeds) {
            Board board = Board.generateRandom(new Random(seed));
            List<Integer> tokens = board.getHexes().stream()
                .filter(h -> h.getTerrain() != TerrainType.DESERT)
                .map(h -> h.getToken().get().getValue())
                .sorted()
                .collect(Collectors.toList());
            assertAll(
                "seed=" + seed,
                () -> assertEquals(STANDARD_TERRAIN_COUNTS, board.terrainCounts()),
                () -> assertEquals(STANDARD_TOKENS, tokens),
                () -> assertEquals(TerrainType.DESERT, board.getDesert().getTerrain()),
                () -> assertTrue(board.getDesert().hasRobber())
            );
        }
    }

    private static List<String> signature(Board board) {
        List<Hex> sorted = new ArrayList<>(board.getHexes());
        sorted.sort((a, b) -> Integer.compare(a.getPosition(), b.getPosition()));
        return sorted.stream()
            .map(h -> h.getPosition() + ":" + h.getTerrain() + ":"
                + (h.getToken().isPresent() ? h.getToken().get().getValue() : "-"))
            .collect(Collectors.toList());
    }

    private static Map<TerrainType, Long> standardTerrainCounts() {
        Map<TerrainType, Long> map = new HashMap<>();
        map.put(TerrainType.FOREST, 4L);
        map.put(TerrainType.PASTURE, 4L);
        map.put(TerrainType.FIELD, 4L);
        map.put(TerrainType.HILLS, 3L);
        map.put(TerrainType.MOUNTAIN, 3L);
        map.put(TerrainType.DESERT, 1L);
        return Collections.unmodifiableMap(map);
    }
}
