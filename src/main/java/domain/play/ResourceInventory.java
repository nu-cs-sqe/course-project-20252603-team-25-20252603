package domain.play;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

/**
 * Mutable resource hand for one player.
 */
public final class ResourceInventory {

    private final EnumMap<ResourceType, Integer> resources =
        new EnumMap<>(ResourceType.class);

    /**
     * Creates an empty inventory with zero of every resource.
     */
    public ResourceInventory() {
        for (ResourceType type : ResourceType.values()) {
            resources.put(type, 0);
        }
    }

    /**
     * Adds resources to this inventory.
     *
     * @param type   non-null resource type
     * @param amount amount to add; zero is allowed
     * @throws IllegalArgumentException if {@code amount} is negative
     * @throws NullPointerException     if {@code type} is null
     */
    public void add(ResourceType type, int amount) {
        Objects.requireNonNull(type, "type must not be null");
        if (amount < 0) {
            throw new IllegalArgumentException("amount must be non-negative");
        }
        resources.put(type, resources.get(type) + amount);
    }

    /**
     * Spends the supplied cost atomically.
     *
     * @param cost non-null cost map
     * @throws IllegalArgumentException if any required amount is negative
     * @throws IllegalStateException    if this inventory cannot pay the cost
     * @throws NullPointerException     if {@code cost} or a cost key is null
     */
    public void spend(Map<ResourceType, Integer> cost) {
        Objects.requireNonNull(cost, "cost must not be null");
        validateCost(cost);
        if (!canAfford(cost)) {
            throw new IllegalStateException("not enough resources");
        }
        for (Map.Entry<ResourceType, Integer> entry : cost.entrySet()) {
            ResourceType type = entry.getKey();
            resources.put(type, resources.get(type) - entry.getValue());
        }
    }

    /**
     * Returns the amount held for one resource.
     *
     * @param type non-null resource type
     * @throws NullPointerException if {@code type} is null
     */
    public int count(ResourceType type) {
        Objects.requireNonNull(type, "type must not be null");
        return resources.get(type);
    }

    /**
     * Takes an unmodifiable snapshot of the current resource counts.
     *
     * @return unmodifiable resource count map
     */
    public Map<ResourceType, Integer> snapshot() {
        return Collections.unmodifiableMap(new EnumMap<>(resources));
    }

    private boolean canAfford(Map<ResourceType, Integer> cost) {
        for (Map.Entry<ResourceType, Integer> entry : cost.entrySet()) {
            if (resources.get(entry.getKey()) < entry.getValue()) {
                return false;
            }
        }
        return true;
    }

    private static void validateCost(Map<ResourceType, Integer> cost) {
        for (Map.Entry<ResourceType, Integer> entry : cost.entrySet()) {
            Objects.requireNonNull(entry.getKey(), "cost resource type must not be null");
            Objects.requireNonNull(entry.getValue(), "cost amount must not be null");
            if (entry.getValue() < 0) {
                throw new IllegalArgumentException("cost amounts must be non-negative");
            }
        }
    }
}
