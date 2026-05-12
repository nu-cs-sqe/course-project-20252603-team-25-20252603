package domain.board;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class NumberToken {

    private static final Set<Integer> VALID_VALUES = Collections.unmodifiableSet(
        new HashSet<>(Arrays.asList(2, 3, 4, 5, 6, 8, 9, 10, 11, 12)));

    private final int value;

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
