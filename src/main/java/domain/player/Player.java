package domain.player;

import java.util.Objects;

public final class Player {

    private final String name;
    private final PlayerColor color;

    public Player(String name, PlayerColor color) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("name must be non-blank");
        }
        if (color == null) {
            throw new IllegalArgumentException("color must not be null");
        }
        this.name = name.trim();
        this.color = color;
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
