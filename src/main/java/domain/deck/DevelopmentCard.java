package domain.deck;

import java.util.Objects;

/**
 * Single development card. Equality is by {@link DevelopmentCardType} so the
 * deck can be reasoned about in tests as a multiset of types.
 */
public final class DevelopmentCard {

    private final DevelopmentCardType type;

    /**
     * Creates a card of the given type.
     *
     * @param type non-null kind of card
     * @throws NullPointerException if {@code type} is null
     */
    public DevelopmentCard(DevelopmentCardType type) {
        this.type = Objects.requireNonNull(type, "type must not be null");
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
