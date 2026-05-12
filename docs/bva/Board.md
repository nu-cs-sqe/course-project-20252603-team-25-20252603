# BVA — `Board`

`Board` is a factory plus immutable holder of 19 hex tiles. The only input is
`Random rng`; randomness is the partition. Validity boundaries are about the
*multisets* produced, not numeric ranges:

- Terrain multiset must equal `{FOREST=4, PASTURE=4, FIELD=4, HILLS=3,
  MOUNTAIN=3, DESERT=1}` — total 19.
- Number-token multiset on the 18 non-desert hexes must equal
  `{2, 3, 3, 4, 4, 5, 5, 6, 6, 8, 8, 9, 9, 10, 10, 11, 11, 12}` — total 18.
- The desert hex must carry no token and must start with the robber.
- Positions assigned to hexes must be the exact set `{0, 1, ..., 18}`.
- `rng = null` is the boundary "no source of randomness" — invalid.

Tests use seeded `Random` so the property checks are deterministic. We also
include a sample of seeds (TC11) to exercise the randomization path more than
once.

## Method under test: `Board.generateRandom(Random rng)`

- **TC1: Produces exactly 19 hexes** ( :white_check_mark: )
    - **State**: `rng = new Random(0L)`.
    - **Expected output**: `getHexes().size() == 19`.

- **TC2: Positions are exactly {0..18}, each appearing once** ( :white_check_mark: )
    - **State**: `rng = new Random(0L)`.
    - **Expected output**: the multiset of `getPosition()` over `getHexes()`
      equals `{0, 1, 2, ..., 18}`.

- **TC3: Terrain multiset matches the standard CATAN distribution** ( :white_check_mark: )
    - **State**: `rng = new Random(0L)`.
    - **Expected output**: `terrainCounts()` equals
      `{FOREST=4, PASTURE=4, FIELD=4, HILLS=3, MOUNTAIN=3, DESERT=1}`.

- **TC4: Exactly one DESERT hex, and it carries no token** ( :white_check_mark: )
    - **State**: `rng = new Random(0L)`.
    - **Expected output**: exactly one hex `h` has `getTerrain() == DESERT`,
      and `h.getToken().isPresent() == false`.

- **TC5: All 18 non-desert hexes carry tokens** ( :white_check_mark: )
    - **State**: `rng = new Random(0L)`.
    - **Expected output**: every non-desert hex returns `getToken().isPresent()
      == true`.

- **TC6: Token multiset over non-desert hexes matches the standard set** ( :white_check_mark: )
    - **State**: `rng = new Random(0L)`.
    - **Expected output**: the multiset of token values over non-desert hexes
      equals `{2, 3, 3, 4, 4, 5, 5, 6, 6, 8, 8, 9, 9, 10, 10, 11, 11, 12}`.

- **TC7: Robber starts on the desert hex** ( :white_check_mark: )
    - **State**: `rng = new Random(0L)`.
    - **Expected output**: the unique DESERT hex has `hasRobber() == true`.

- **TC8: Robber is on no other hex** ( :white_check_mark: )
    - **State**: `rng = new Random(0L)`.
    - **Expected output**: every non-desert hex has `hasRobber() == false`.

- **TC9: Determinism — same seed produces the same board** ( :white_check_mark: )
    - **State**: `b1 = generateRandom(new Random(42L))`,
      `b2 = generateRandom(new Random(42L))`.
    - **Expected output**: hex sequence (position, terrain, token value) of
      `b1` equals that of `b2`.

- **TC10: Null Random rejected** ( :white_check_mark: )
    - **State**: `rng = null`.
    - **Expected output**: throws `IllegalArgumentException`.

## Method under test: `getDesert()`

- **TC11: All invariants hold across a sample of seeds** ( :white_check_mark: )
    - **State**: for each `seed ∈ {0, 1, 2, 7, 42, 100}`,
      `b = generateRandom(new Random(seed))`.
    - **Expected output**: for each board, terrain multiset, token multiset on
      non-desert hexes, and "robber on the unique desert hex" all hold; and
      `getDesert()` returns that desert hex.
