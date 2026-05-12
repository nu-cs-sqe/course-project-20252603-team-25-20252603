# BVA — `Hex`

Boundary inputs:
- `position`: integer in `[0, 18]` (19 board slots).
- `terrain`: one of the six `TerrainType` values, or `null`.
- `token`: `NumberToken` or `null`. Coupled to terrain — `DESERT` must have a
  `null` token; every non-`DESERT` terrain must have a non-`null` token.
- Robber state: initially `false`; toggled by `placeRobber()` / `removeRobber()`.

Equivalence partitions for `position`: invalid-below (`<0`), valid (`0..18`),
invalid-above (`>18`). Boundaries: `-1|0`, `18|19`.

## Method under test: `Hex(int position, TerrainType terrain, NumberToken token)`

- **TC1: Lower position boundary, valid non-desert hex** ( :white_check_mark: )
    - **State**: `position = 0`, `terrain = FOREST`, `token = new NumberToken(8)`.
    - **Expected output**: object created; `getPosition() == 0`,
      `getTerrain() == FOREST`, `getToken().get().getValue() == 8`,
      `hasRobber() == false`.

- **TC2: Upper position boundary** ( :white_check_mark: )
    - **State**: `position = 18`, `terrain = MOUNTAIN`, `token = new NumberToken(6)`.
    - **Expected output**: object created; `getPosition() == 18`.

- **TC3: Position just below valid range** ( :white_check_mark: )
    - **State**: `position = -1`, `terrain = FOREST`, `token = new NumberToken(5)`.
    - **Expected output**: throws `IllegalArgumentException`.

- **TC4: Position just above valid range** ( :white_check_mark: )
    - **State**: `position = 19`, `terrain = FOREST`, `token = new NumberToken(5)`.
    - **Expected output**: throws `IllegalArgumentException`.

- **TC5: Null terrain rejected** ( :white_check_mark: )
    - **State**: `position = 5`, `terrain = null`, `token = new NumberToken(5)`.
    - **Expected output**: throws `IllegalArgumentException`.

- **TC6: DESERT with null token is valid** ( :white_check_mark: )
    - **State**: `position = 9`, `terrain = DESERT`, `token = null`.
    - **Expected output**: object created; `getToken().isPresent() == false`.

- **TC7: DESERT with non-null token rejected** ( :white_check_mark: )
    - **State**: `position = 9`, `terrain = DESERT`, `token = new NumberToken(8)`.
    - **Expected output**: throws `IllegalArgumentException`.

- **TC8: Non-DESERT with null token rejected** ( :white_check_mark: )
    - **State**: `position = 9`, `terrain = HILLS`, `token = null`.
    - **Expected output**: throws `IllegalArgumentException`.

## Method under test: `getPosition()` / `getTerrain()` / `getToken()`
- Implemented in TC1 (non-desert, token present) and TC6 (desert, empty token).

- **TC9: getToken returns Optional.empty for desert** ( :white_check_mark: )
    - **State**: hex from TC6.
    - **Expected output**: `getToken()` returns `Optional.empty()`.

## Method under test: `hasRobber()` / `placeRobber()` / `removeRobber()`

- **TC10: Robber absent by default** ( :white_check_mark: )
    - **State**: any freshly constructed hex.
    - **Expected output**: `hasRobber() == false`.

- **TC11: placeRobber sets robber present** ( :white_check_mark: )
    - **State**: a fresh hex; call `placeRobber()`.
    - **Expected output**: `hasRobber() == true`.

- **TC12: placeRobber is idempotent** ( :white_check_mark: )
    - **State**: a fresh hex; call `placeRobber()` twice.
    - **Expected output**: `hasRobber() == true` (no exception, still true).

- **TC13: removeRobber clears the robber** ( :white_check_mark: )
    - **State**: a fresh hex; `placeRobber()` then `removeRobber()`.
    - **Expected output**: `hasRobber() == false`.
