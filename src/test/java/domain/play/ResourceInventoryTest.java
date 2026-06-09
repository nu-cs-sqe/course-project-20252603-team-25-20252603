package domain.play;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    void tc9_snapshotIsUnmodifiable() {
        ResourceInventory inventory = new ResourceInventory();

        assertThrows(UnsupportedOperationException.class,
            () -> inventory.snapshot().put(ResourceType.BRICK, 1));
    }
}
