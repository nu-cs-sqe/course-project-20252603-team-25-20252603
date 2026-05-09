# BVA — `Player`

Boundary inputs: `name` (null, empty, blank/whitespace, valid), `color` (null,
each enum value).

## Method under test: `Player(String name, PlayerColor color)`

- **TC1: Valid name and color** ( :white_check_mark: )
    - **State**: `name = "Daniel"`, `color = PlayerColor.RED`.
    - **Expected output**: object created; `getName() = "Daniel"`, `getColor() = RED`.

- **TC2: Name with surrounding whitespace is trimmed** ( :white_check_mark: )
    - **State**: `name = "  Daniel  "`, `color = BLUE`.
    - **Expected output**: `getName() = "Daniel"`.

- **TC3: Null name rejected** ( :white_check_mark: )
    - **State**: `name = null`, `color = RED`.
    - **Expected output**: throws `IllegalArgumentException`.

- **TC4: Empty name rejected** ( :white_check_mark: )
    - **State**: `name = ""`, `color = RED`.
    - **Expected output**: throws `IllegalArgumentException`.

- **TC5: Whitespace-only name rejected** ( :white_check_mark: )
    - **State**: `name = "   "`, `color = RED`.
    - **Expected output**: throws `IllegalArgumentException`.

- **TC6: Null color rejected** ( :white_check_mark: )
    - **State**: `name = "Daniel"`, `color = null`.
    - **Expected output**: throws `IllegalArgumentException`.

## Method under test: `getName()`
- **TC7: Returns the constructor-provided (trimmed) name** ( implemented in TC1, TC2 )

## Method under test: `getColor()`
- **TC8: Returns the constructor-provided color** ( implemented in TC1 )

## Method under test: `equals(Object)` / `hashCode()`

- **TC9: Equal when (name, color) match** ( :white_check_mark: )
    - **State**: `a = new Player("Daniel", RED)`, `b = new Player("Daniel", RED)`.
    - **Expected output**: `a.equals(b)` is `true`; `a.hashCode() == b.hashCode()`.

- **TC10: Not equal when names differ** ( :white_check_mark: )
    - **State**: `a = ("Daniel", RED)`, `b = ("Julian", RED)`.
    - **Expected output**: `a.equals(b)` is `false`.

- **TC11: Not equal when colors differ** ( :white_check_mark: )
    - **State**: `a = ("Daniel", RED)`, `b = ("Daniel", BLUE)`.
    - **Expected output**: `a.equals(b)` is `false`.

- **TC12: Not equal to null and to other types** ( :white_check_mark: )
    - **State**: `a = ("Daniel", RED)`.
    - **Expected output**: `a.equals(null)` is `false`; `a.equals("Daniel")` is `false`.
