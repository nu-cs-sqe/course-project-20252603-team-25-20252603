# BVA - `Game`

Boundary inputs: players list (null, empty, contains null, valid), board/deck/
turn order (null, valid), and source-list mutation.

## Method under test: `Game(List<Player> players, Board board, DevelopmentCardDeck deck, TurnOrder turnOrder)`

- **TC1: Valid game stores all parts** ( :white_check_mark: )
    - **State**: valid players, board, deck, and turn order.
    - **Expected output**: object created; getters return the same game parts.

- **TC2: Null players rejected** ( :white_check_mark: )
    - **State**: `players = null`.
    - **Expected output**: throws `IllegalArgumentException`.

- **TC3: Empty players rejected** ( :white_check_mark: )
    - **State**: `players = []`.
    - **Expected output**: throws `IllegalArgumentException`.

- **TC4: Null player entry rejected** ( :white_check_mark: )
    - **State**: players list contains null.
    - **Expected output**: throws `IllegalArgumentException`.

- **TC5: Null board/deck/turn order rejected** ( :white_check_mark: )
    - **State**: one of `board`, `deck`, or `turnOrder` is null.
    - **Expected output**: throws `IllegalArgumentException`.

- **TC6: Players list is defensively copied** ( :white_check_mark: )
    - **State**: construct with mutable players list, then clear source list.
    - **Expected output**: `game.players().size()` is unchanged.

- **TC7: Players list is unmodifiable** ( :white_check_mark: )
    - **State**: valid game.
    - **Expected output**: adding to `players()` throws `UnsupportedOperationException`.

## Method under test: `players()` / `board()` / `deck()` / `turnOrder()`

- **TC8: Getters return game parts** ( implemented in TC1 )
    - **State**: valid game.
    - **Expected output**: getters return the players, board, deck, and turn order.
