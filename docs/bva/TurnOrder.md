# BVA - `TurnOrder`

Boundary inputs: `players` (null, empty, one player, two players, three players,
contains null, contains duplicate), current index at start and at wraparound.

## Method under test: `TurnOrder(List<Player> players)`

- **TC1: Valid two-player order starts at first player** ( :white_check_mark: )
    - **State of the system**: players `[Alice, Bob]`.
    - **Expected output**: object created; `current() = Alice`, `currentIndex() = 0`, `size() = 2`.

- **TC2: Valid three-player order accepted** ( :white_check_mark: )
    - **State of the system**: players `[Alice, Bob, Cara]`.
    - **Expected output**: object created and advances in registration order.

- **TC4: Null player list rejected** ( :white_check_mark: )
    - **State of the system**: `players = null`.
    - **Expected output**: throws `IllegalArgumentException`.

- **TC5: Empty player list rejected** ( :white_check_mark: )
    - **State of the system**: `players = []`.
    - **Expected output**: throws `IllegalArgumentException`.

- **TC6: Single-player list rejected** ( :white_check_mark: )
    - **State of the system**: players `[Alice]`.
    - **Expected output**: throws `IllegalArgumentException`.

- **TC7: Null player entry rejected** ( :white_check_mark: )
    - **State of the system**: players `[Alice, null]`.
    - **Expected output**: throws `IllegalArgumentException`.

- **TC8: Same player instance duplicate rejected** ( :white_check_mark: )
    - **State of the system**: players `[Alice, Alice]`.
    - **Expected output**: throws `IllegalArgumentException`.

- **TC9: Equal player duplicate rejected** ( :white_check_mark: )
    - **State of the system**: players `[Alice, new Player("Alice", RED)]`.
    - **Expected output**: throws `IllegalArgumentException`.

- **TC10: Constructor defensively copies player list** ( :white_check_mark: )
    - **State of the system**: construct with mutable list `[Alice, Bob]`, then append `Cara` to source list.
    - **Expected output**: `size()` remains 2 and `current()` remains Alice.

## Method under test: `advance()`

- **TC2: Advance follows registration order** ( :white_check_mark: )
    - **State of the system**: players `[Alice, Bob, Cara]`, current index 0.
    - **Expected output**: first `advance()` returns Bob and index 1; second returns Cara.

- **TC3: Advance wraps from last player to first player** ( :white_check_mark: )
    - **State of the system**: players `[Alice, Bob]`, current index 1.
    - **Expected output**: `advance()` returns Alice and index becomes 0.

## Method under test: `current()`

- **TC11: Current is stable until advance is called** ( :white_check_mark: )
    - **State of the system**: players `[Alice, Bob]`, no call to `advance()`.
    - **Expected output**: repeated `current()` calls return Alice.

## Method under test: `currentIndex()`

- **TC12: Current index reports active turn position** ( implemented in TC1, TC2, TC3 )
    - **State of the system**: newly created order, then advanced order.
    - **Expected output**: returns 0 initially, 1 after one advance, and 0 after wraparound.

## Method under test: `size()`

- **TC13: Size reports copied player count** ( implemented in TC1, TC10 )
    - **State of the system**: valid two-player order and source-list mutation.
    - **Expected output**: returns 2 and is not affected by source-list mutation.
