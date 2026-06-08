# BVA - `ResourceInventory`

Boundary inputs: resource type (null, valid), amount (negative, zero, one),
and payment maps that are affordable or unaffordable.

## Method under test: `add(ResourceType type, int amount)`

- **TC1: Null type rejected** ( :white_check_mark: )
    - **State**: `type = null`, `amount = 1`.
    - **Expected output**: throws `NullPointerException`.

- **TC2: Negative amount rejected** ( :white_check_mark: )
    - **State**: `type = LUMBER`, `amount = -1`.
    - **Expected output**: throws `IllegalArgumentException`.

- **TC3: Zero amount accepted as no-op** ( :white_check_mark: )
    - **State**: `type = LUMBER`, `amount = 0`.
    - **Expected output**: count remains unchanged.

- **TC4: Positive amount increases count** ( :white_check_mark: )
    - **State**: `type = LUMBER`, `amount = 1`.
    - **Expected output**: count increases by 1.

## Method under test: `spend(Map<ResourceType, Integer> cost)`

- **TC5: Null cost rejected** ( :white_check_mark: )
    - **State**: `cost = null`.
    - **Expected output**: throws `NullPointerException`.

- **TC6: Unaffordable cost rejected** ( :white_check_mark: )
    - **State**: cost asks for one more resource than the inventory contains.
    - **Expected output**: throws `IllegalStateException`.

- **TC7: Affordable exact cost is spent** ( :white_check_mark: )
    - **State**: inventory has exactly the cost resources.
    - **Expected output**: resources are reduced to zero.

## Method under test: `count(ResourceType type)` / `snapshot()`

- **TC8: Count returns current amount** ( implemented in TC3, TC4, TC7 )

- **TC9: Snapshot is unmodifiable** ( :white_check_mark: )
    - **State**: any inventory.
    - **Expected output**: modifying `snapshot()` throws
      `UnsupportedOperationException`.
