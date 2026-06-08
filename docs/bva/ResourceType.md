# BVA - `ResourceType`

Boundary inputs: each terrain enum value and null terrain.

## Method under test: `fromTerrain(TerrainType terrain)`

- **TC1: Forest maps to lumber** ( implemented in `PlayableGameTest` TC5 )
  - **State**: `terrain = FOREST`.
  - **Expected output**: returns `Optional.of(LUMBER)`.

- **TC2: Pasture maps to wool** ( :white_check_mark: )
  - **State**: `terrain = PASTURE`.
  - **Expected output**: returns `Optional.of(WOOL)`.

- **TC3: Field maps to grain** ( :white_check_mark: )
  - **State**: `terrain = FIELD`.
  - **Expected output**: returns `Optional.of(GRAIN)`.

- **TC4: Hills maps to brick** ( :white_check_mark: )
  - **State**: `terrain = HILLS`.
  - **Expected output**: returns `Optional.of(BRICK)`.

- **TC5: Mountain maps to ore** ( :white_check_mark: )
  - **State**: `terrain = MOUNTAIN`.
  - **Expected output**: returns `Optional.of(ORE)`.

- **TC6: Desert maps to empty** ( :white_check_mark: )
  - **State**: `terrain = DESERT`.
  - **Expected output**: returns `Optional.empty()`.

- **TC7: Null terrain rejected** ( :white_check_mark: )
  - **State**: `terrain = null`.
  - **Expected output**: throws `NullPointerException`.
