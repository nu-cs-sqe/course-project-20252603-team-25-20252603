package domain.player;

import java.util.Objects;

/**
 * Immutable value object representing a registered player. Equality is by
 * trimmed name and color. Constructed by {@link domain.setup.GameSetup} from
 * validated {@link domain.setup.PlayerRegistration} instances.
 */
public final class Player {

    private final String name;
    private final PlayerColor color;

    /**
     * Creates a player with the given name and color.
     *
     * @param name  non-blank display name; surrounding whitespace is trimmed
     * @param color non-null color token
     * @throws IllegalArgumentException if {@code name} is blank
     * @throws NullPointerException     if {@code name} or {@code color} is null
     */
    public Player(String name, PlayerColor color) {
        Objects.requireNonNull(name, "name must not be null");
        if (name.trim().isEmpty()) {
            throw new IllegalArgumentException("name must be non-blank");
        }
        this.name = name.trim();
        this.color = Objects.requireNonNull(color, "color must not be null");
    }

    public String getName() {
        return name;
    }

    public PlayerColor getColor() {
        return color;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Player)) {
            return false;
        }
        Player that = (Player) other;
        return name.equals(that.name) && color == that.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, color);
    }
}
