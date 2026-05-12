# BVA - `DevelopmentCard`

Boundary inputs: `type` (null, valid enum value).

## Method under test: `DevelopmentCard(DevelopmentCardType type)`

- **TC1: Valid type accepted** ( :white_check_mark: )
    - **State**: `type = KNIGHT`.
    - **Expected output**: object created and `getType() = KNIGHT`.

- **TC2: Null type rejected** ( :white_check_mark: )
    - **State**: `type = null`.
    - **Expected output**: throws `IllegalArgumentException`.

## Method under test: `getType()`

- **TC3: Returns constructor type** ( implemented in TC1 )
    - **State**: card created with `KNIGHT`.
    - **Expected output**: returns `KNIGHT`.

## Method under test: `equals(Object)` / `hashCode()`

- **TC4: Equal when types match** ( :white_check_mark: )
    - **State**: two cards with `MONOPOLY`.
    - **Expected output**: cards are equal and have the same hash code.

- **TC5: Not equal when types differ** ( :white_check_mark: )
    - **State**: one `MONOPOLY` card and one `KNIGHT` card.
    - **Expected output**: cards are not equal.

- **TC6: Not equal to null or other types** ( :white_check_mark: )
    - **State**: one `KNIGHT` card.
    - **Expected output**: not equal to null or a string; equal to itself.
