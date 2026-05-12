# BVA - `GameSetup`

Boundary inputs: `rng` (null, valid seeded instance), player registrations
(null, 2, 3, 4, 5, contains null, duplicate names, duplicate colors), build
before and after registration.

## Method under test: `GameSetup(Random rng)`

- **TC1: Null random rejected** ( :white_check_mark: )
    - **State of the system**: `rng = null`.
    - **Expected output**: throws `IllegalArgumentException`.

## Method under test: `registerPlayers(List<PlayerRegistration> registrations)`

- **TC2: Null registrations rejected** ( :white_check_mark: )
    - **State of the system**: `registrations = null`.
    - **Expected output**: throws `IllegalArgumentException`.

- **TC3: Two players rejected** ( :white_check_mark: )
    - **State of the system**: 2 valid registrations.
    - **Expected output**: throws `IllegalArgumentException`.

- **TC4: Three players accepted** ( :white_check_mark: )
    - **State of the system**: 3 valid registrations with unique names and colors.
    - **Expected output**: registration succeeds and `build()` returns a game with 3 players.

- **TC5: Four players accepted** ( :white_check_mark: )
    - **State of the system**: 4 valid registrations with unique names and colors.
    - **Expected output**: registration succeeds and `build()` returns a game with 4 players.

- **TC6: Five players rejected** ( :white_check_mark: )
    - **State of the system**: 5 registrations.
    - **Expected output**: throws `IllegalArgumentException`.

- **TC7: Null registration entry rejected** ( :white_check_mark: )
    - **State of the system**: 3-entry registration list where one entry is null.
    - **Expected output**: throws `IllegalArgumentException`.

- **TC8: Duplicate colors rejected** ( :white_check_mark: )
    - **State of the system**: 3 registrations where two players use RED.
    - **Expected output**: throws `IllegalArgumentException`.

- **TC9: Duplicate names rejected** ( :white_check_mark: )
    - **State of the system**: 3 registrations where two players are named Alice.
    - **Expected output**: throws `IllegalArgumentException`.

- **TC11: Registration names are trimmed** ( :white_check_mark: )
    - **State of the system**: registration name `"  Alice  "`.
    - **Expected output**: built player name is `"Alice"`.

- **TC13: Register players defensively copies registration list** ( :white_check_mark: )
    - **State of the system**: register with mutable 3-player list, then clear source list.
    - **Expected output**: `build()` still returns a game with 3 players.

## Method under test: `build()`

- **TC4: Build creates complete game after three-player registration** ( :white_check_mark: )
    - **State of the system**: 3 valid registrations.
    - **Expected output**: `Game` has 3 players, a board object, 25 development cards, and turn order starts at player 0.

- **TC5: Build creates complete game after four-player registration** ( :white_check_mark: )
    - **State of the system**: 4 valid registrations.
    - **Expected output**: `Game` has 4 players.

- **TC10: Build before registration rejected** ( :white_check_mark: )
    - **State of the system**: newly constructed setup, no registered players.
    - **Expected output**: throws `IllegalStateException`.

- **TC12: Game players list is unmodifiable** ( :white_check_mark: )
    - **State of the system**: game built from 3 valid registrations.
    - **Expected output**: adding to `game.players()` throws `UnsupportedOperationException`.

- **TC14: Build creates deck and board dependency** ( :white_check_mark: )
    - **State of the system**: game built from 3 valid registrations.
    - **Expected output**: board is present and deck counts are `{KNIGHT=14, VICTORY_POINT=5, ROAD_BUILDING=2, MONOPOLY=2, YEAR_OF_PLENTY=2}`.
