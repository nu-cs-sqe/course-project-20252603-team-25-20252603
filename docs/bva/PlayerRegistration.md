# BVA - `PlayerRegistration`

Boundary inputs: `name` (null, empty, blank, valid, valid with whitespace) and
`color` (null, valid enum value).

## Method under test: `PlayerRegistration(String name, PlayerColor color)`

- **TC1: Valid name and color** ( :white_check_mark: )
    - **State**: `name = "Alice"`, `color = RED`.
    - **Expected output**: object created; `name() = "Alice"`, `color() = RED`.

- **TC2: Name with whitespace is trimmed** ( :white_check_mark: )
    - **State**: `name = "  Alice  "`, `color = BLUE`.
    - **Expected output**: `name() = "Alice"`.

- **TC3: Null name rejected** ( :white_check_mark: )
    - **State**: `name = null`, `color = RED`.
    - **Expected output**: throws `IllegalArgumentException`.

- **TC4: Empty name rejected** ( :white_check_mark: )
    - **State**: `name = ""`, `color = RED`.
    - **Expected output**: throws `IllegalArgumentException`.

- **TC5: Blank name rejected** ( :white_check_mark: )
    - **State**: `name = "   "`, `color = RED`.
    - **Expected output**: throws `IllegalArgumentException`.

- **TC6: Null color rejected** ( :white_check_mark: )
    - **State**: `name = "Alice"`, `color = null`.
    - **Expected output**: throws `IllegalArgumentException`.

## Method under test: `name()`

- **TC7: Returns trimmed name** ( implemented in TC1, TC2 )
    - **State**: valid registration.
    - **Expected output**: returns the trimmed name.

## Method under test: `color()`

- **TC8: Returns color** ( implemented in TC1 )
    - **State**: valid registration.
    - **Expected output**: returns the constructor color.

## Method under test: `equals(Object)` / `hashCode()`

- **TC9: Equal when name and color match** ( :white_check_mark: )
    - **State**: two registrations with `("Alice", RED)`.
    - **Expected output**: objects are equal and have the same hash code.

- **TC10: Not equal when names differ** ( :white_check_mark: )
    - **State**: `("Alice", RED)` and `("Bob", RED)`.
    - **Expected output**: objects are not equal.

- **TC11: Not equal when colors differ** ( :white_check_mark: )
    - **State**: `("Alice", RED)` and `("Alice", BLUE)`.
    - **Expected output**: objects are not equal.

- **TC12: Not equal to null or other types** ( :white_check_mark: )
    - **State**: one valid registration.
    - **Expected output**: not equal to null or a string; equal to itself.
