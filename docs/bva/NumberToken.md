# BVA — `NumberToken`

Boundary inputs: `value` is an integer that must lie in the set
`{2, 3, 4, 5, 6, 8, 9, 10, 11, 12}`. This is the standard CATAN production-roll
multiset; 7 is excluded (rolling 7 triggers the robber, not a tile production).

Equivalence partitions:
- **Valid**: `{2..6, 8..12}` (10 values).
- **Invalid — below**: `value <= 1`.
- **Invalid — above**: `value >= 13`.
- **Invalid — hole**: `value == 7`.

Boundary points exercised: `1|2` (lower), `12|13` (upper), `6|7|8` (around the
hole at 7).

## Method under test: `NumberToken(int value)`

- **TC1: Lower valid boundary** ( :white_check_mark: )
    - **State**: `value = 2`.
    - **Expected output**: object created; `getValue() == 2`.

- **TC2: Upper valid boundary** ( :white_check_mark: )
    - **State**: `value = 12`.
    - **Expected output**: object created; `getValue() == 12`.

- **TC3: Just below the valid range** ( :white_check_mark: )
    - **State**: `value = 1`.
    - **Expected output**: throws `IllegalArgumentException`.

- **TC4: Just above the valid range** ( :white_check_mark: )
    - **State**: `value = 13`.
    - **Expected output**: throws `IllegalArgumentException`.

- **TC5: The hole at 7 (robber roll)** ( :white_check_mark: )
    - **State**: `value = 7`.
    - **Expected output**: throws `IllegalArgumentException`.

- **TC6: Just below the hole** ( :white_check_mark: )
    - **State**: `value = 6`.
    - **Expected output**: object created; `getValue() == 6`.

- **TC7: Just above the hole** ( :white_check_mark: )
    - **State**: `value = 8`.
    - **Expected output**: object created; `getValue() == 8`.

- **TC8: Far below the valid range** ( :white_check_mark: )
    - **State**: `value = 0`.
    - **Expected output**: throws `IllegalArgumentException`.

## Method under test: `getValue()`
- Implemented in TC1, TC2, TC6, TC7.

## Method under test: `equals(Object)` / `hashCode()`

- **TC9: Equal when values match** ( :white_check_mark: )
    - **State**: `a = new NumberToken(8)`, `b = new NumberToken(8)`.
    - **Expected output**: `a.equals(b)` is `true`; `a.hashCode() == b.hashCode()`.

- **TC10: Not equal when values differ; not equal to null/other types** ( :white_check_mark: )
    - **State**: `a = new NumberToken(8)`, `b = new NumberToken(9)`.
    - **Expected output**: `a.equals(b)` is `false`; `a.equals(null)` is
      `false`; `a.equals("8")` is `false`.
