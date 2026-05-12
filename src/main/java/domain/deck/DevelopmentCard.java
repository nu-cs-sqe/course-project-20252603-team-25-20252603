package domain.deck;

import java.util.Objects;

public final class DevelopmentCard {

    private final DevelopmentCardType type;

    public DevelopmentCard(DevelopmentCardType type) {
        if (type == null) {
            throw new IllegalArgumentException("type must not be null");
        }
        this.type = type;
    }

    public DevelopmentCardType getType() {
        return type;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof DevelopmentCard)) {
            return false;
        }
        DevelopmentCard that = (DevelopmentCard) other;
        return type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }
}
