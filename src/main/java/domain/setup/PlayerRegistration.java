package domain.setup;

import domain.player.PlayerColor;
import java.util.Objects;

/**
 * User-facing registration record collected from the setup UI before any
 * {@link domain.player.Player} is built. Validates name and color at the
 * boundary so downstream domain code can trust its inputs.
 */
public final class PlayerRegistration {

    private final String name;
    private final PlayerColor color;

    /**
     * Creates a registration with the given name and color.
     *
     * @param name  non-blank display name; surrounding whitespace is trimmed
     * @param color non-null color token
     * @throws IllegalArgumentException if {@code name} is blank
     * @throws NullPointerException     if {@code name} or {@code color} is null
     */
    public PlayerRegistration(String name, PlayerColor color) {
        Objects.requireNonNull(name, "name must not be null");
        if (name.trim().isEmpty()) {
            throw new IllegalArgumentException("name must be non-blank");
        }
        this.name = name.trim();
        this.color = Objects.requireNonNull(color, "color must not be null");
    }

    public String name() {
        return name;
    }

    public PlayerColor color() {
        return color;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof PlayerRegistration)) {
            return false;
        }
        PlayerRegistration that = (PlayerRegistration) other;
        return name.equals(that.name) && color == that.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, color);
    }
}
