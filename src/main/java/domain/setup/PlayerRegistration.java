package domain.setup;

import domain.player.PlayerColor;
import java.util.Objects;

public final class PlayerRegistration {

    private final String name;
    private final PlayerColor color;

    public PlayerRegistration(String name, PlayerColor color) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("name must be non-blank");
        }
        if (color == null) {
            throw new IllegalArgumentException("color must not be null");
        }
        this.name = name.trim();
        this.color = color;
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
