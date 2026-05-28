package domain.board;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Immutable production-number chit placed on a non-desert hex. Legal values
 * are {@code 2..6} and {@code 8..12} (CATAN excludes 7, the robber roll).
 */
public final class NumberToken {

    private static final Set<Integer> VALID_VALUES = Collections.unmodifiableSet(
        new HashSet<>(Arrays.asList(2, 3, 4, 5, 6, 8, 9, 10, 11, 12)));

    private final int value;

    /**
     * Creates a number token with the given pip value.
     *
     * @param value pip value in {@code {2..6, 8..12}}
     * @throws IllegalArgumentException if {@code value} is outside the legal set
     */
    public NumberToken(int value) {
        if (!VALID_VALUES.contains(value)) {
            throw new IllegalArgumentException(
                "NumberToken value must be in {2..6, 8..12}; got " + value);
        }
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof NumberToken)) {
            return false;
        }
        return value == ((NumberToken) other).value;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(value);
    }
}
