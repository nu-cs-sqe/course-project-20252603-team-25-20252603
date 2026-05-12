# BVA - `DevelopmentCardDeck`

Boundary inputs: `rng` (null, valid seeded instance), standard deck size,
standard card-count multiset, drawing from non-empty and empty deck.

## Method under test: `standardShuffled(Random rng)`

- **TC1: Standard deck has 25 cards** ( :white_check_mark: )
    - **State of the system**: `rng = new Random(1)`.
    - **Expected output**: deck is created and `size() = 25`.

- **TC2: Standard deck has CATAN development-card counts** ( :white_check_mark: )
    - **State of the system**: `rng = new Random(1)`.
    - **Expected output**: `typeCounts()` is `{KNIGHT=14, VICTORY_POINT=5, ROAD_BUILDING=2, MONOPOLY=2, YEAR_OF_PLENTY=2}`.

- **TC3: Null random rejected** ( :white_check_mark: )
    - **State of the system**: `rng = null`.
    - **Expected output**: throws `IllegalArgumentException`.

- **TC4: Same seed produces same draw order** ( :white_check_mark: )
    - **State of the system**: two decks built with `new Random(7)`.
    - **Expected output**: drawing all cards from both decks produces the same type sequence.

- **TC5: Different seeds can produce different draw order** ( :white_check_mark: )
    - **State of the system**: one deck uses `new Random(7)` and one uses `new Random(8)`.
    - **Expected output**: drawing all cards from both decks produces different type sequences.

## Method under test: `draw()`

- **TC6: Draw returns card and reduces size** ( :white_check_mark: )
    - **State of the system**: fresh standard deck.
    - **Expected output**: `draw()` returns a `DevelopmentCard`; `size()` changes from 25 to 24.

- **TC7: Draw from empty deck rejected** ( :white_check_mark: )
    - **State of the system**: standard deck after 25 successful draws.
    - **Expected output**: next `draw()` throws `NoSuchElementException`.

## Method under test: `typeCounts()`

- **TC8: Type counts reflect drawn card** ( :white_check_mark: )
    - **State of the system**: fresh standard deck after one draw.
    - **Expected output**: count for the drawn card type is reduced by 1.

- **TC9: Type counts map is unmodifiable** ( :white_check_mark: )
    - **State of the system**: fresh standard deck.
    - **Expected output**: modifying the returned map throws `UnsupportedOperationException`.

## Method under test: `size()`

- **TC10: Size reports remaining card count** ( implemented in TC1, TC6 )
    - **State of the system**: fresh deck and deck after one draw.
    - **Expected output**: returns 25 before draw and 24 after draw.
