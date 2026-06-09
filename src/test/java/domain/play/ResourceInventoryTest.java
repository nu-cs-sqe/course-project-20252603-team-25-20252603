package domain.play;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

class ResourceInventoryTest {

    @Test
    void tc1_nullTypeRejected() {
        ResourceInventory inventory = new ResourceInventory();

        assertThrows(NullPointerException.class, () -> inventory.add(null, 1));
    }

    @Test
    void tc2_negativeAmountRejected() {
        ResourceInventory inventory = new ResourceInventory();

        assertThrows(IllegalArgumentException.class,
            () -> inventory.add(ResourceType.LUMBER, -1));
    }

    @Test
    void tc3_zeroAmountAcceptedAsNoop() {
        ResourceInventory inventory = new ResourceInventory();

        inventory.add(ResourceType.LUMBER, 0);

        assertEquals(0, inventory.count(ResourceType.LUMBER));
    }

    @Test
    void tc4_positiveAmountIncreasesCount() {
        ResourceInventory inventory = new ResourceInventory();

        inventory.add(ResourceType.LUMBER, 1);

        assertEquals(1, inventory.count(ResourceType.LUMBER));
    }

    @Test
    void tc5_nullCostRejected() {
        ResourceInventory inventory = new ResourceInventory();

        assertThrows(NullPointerException.class, () -> inventory.spend(null));
    }

    @Test
    void tc6_unaffordableCostRejected() {
        ResourceInventory inventory = new ResourceInventory();

        assertThrows(IllegalStateException.class,
            () -> inventory.spend(Map.of(ResourceType.BRICK, 1)));
    }

    @Test
    void tc7_affordableExactCostIsSpent() {
        ResourceInventory inventory = new ResourceInventory();
        inventory.add(ResourceType.BRICK, 1);

        inventory.spend(Map.of(ResourceType.BRICK, 1));

        assertEquals(0, inventory.count(ResourceType.BRICK));
    }

    @Test
    void tc8_negativeCostRejectedBeforeResourcesChange() {
        ResourceInventory inventory = new ResourceInventory();
        inventory.add(ResourceType.BRICK, 1);

        assertThrows(IllegalArgumentException.class,
            () -> inventory.spend(Map.of(ResourceType.BRICK, -1)));
        assertEquals(1, inventory.count(ResourceType.BRICK));
    }

    @Test
    void tc9_nullCostKeyRejectedBeforeResourcesChange() {
        ResourceInventory inventory = new ResourceInventory();
        inventory.add(ResourceType.BRICK, 1);
        Map<ResourceType, Integer> cost = new HashMap<>();
        cost.put(null, 1);

        assertThrows(NullPointerException.class, () -> inventory.spend(cost));
        assertEquals(1, inventory.count(ResourceType.BRICK));
    }

    @Test
    void tc10_nullCostAmountRejectedBeforeResourcesChange() {
        ResourceInventory inventory = new ResourceInventory();
        inventory.add(ResourceType.BRICK, 1);
        Map<ResourceType, Integer> cost = new HashMap<>();
        cost.put(ResourceType.BRICK, null);

        assertThrows(NullPointerException.class, () -> inventory.spend(cost));
        assertEquals(1, inventory.count(ResourceType.BRICK));
    }

    @Test
    void tc11_snapshotContainsCurrentCounts() {
        ResourceInventory inventory = new ResourceInventory();
        inventory.add(ResourceType.BRICK, 2);
        inventory.add(ResourceType.ORE, 1);
        EnumMap<ResourceType, Integer> expected = new EnumMap<>(ResourceType.class);
        for (ResourceType type : ResourceType.values()) {
            expected.put(type, 0);
        }
        expected.put(ResourceType.BRICK, 2);
        expected.put(ResourceType.ORE, 1);

        assertEquals(expected, inventory.snapshot());
    }

    @Test
    void tc13_zeroAmountCostEntryAcceptedAsNoop() {
        ResourceInventory inventory = new ResourceInventory();
        inventory.add(ResourceType.LUMBER, 1);

        assertDoesNotThrow(() -> inventory.spend(Map.of(ResourceType.LUMBER, 0)));
        assertEquals(1, inventory.count(ResourceType.LUMBER));
    }

    @Test
    void tc12_snapshotIsUnmodifiable() {
        ResourceInventory inventory = new ResourceInventory();

        assertThrows(UnsupportedOperationException.class,
            () -> inventory.snapshot().put(ResourceType.BRICK, 1));
    }
}
