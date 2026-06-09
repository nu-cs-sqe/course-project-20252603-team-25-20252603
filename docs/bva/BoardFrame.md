# BVA - `BoardFrame`

Swing UI shown after `GameSetup.build()` completes. It creates a
`PlayableGame` and exposes the D-rubric playable slice: roll dice, build a
settlement, end turn, and inspect board ownership/resource state. Automated
headless UI tests are out of scope; cases below are verified manually via
`./gradlew run`.

## Constructor `BoardFrame(LocaleManager, Game)`

- **TC1: Null localeManager rejected** ( :white_check_mark: manual )
  - **State**: `localeManager = null`, `game` valid.
  - **Expected output**: `NullPointerException`.

- **TC2: Null game rejected** ( :white_check_mark: manual )
  - **State**: `localeManager` valid, `game = null`.
  - **Expected output**: `NullPointerException`.

## Rendering

- **TC3: Title resolved from active locale** ( :white_check_mark: manual )
  - **State**: active locale = English.
  - **Expected output**: window title equals `board.title` (`CATAN - Board`).

- **TC4: Starter and player list shown** ( :white_check_mark: manual )
  - **State**: 3 players registered.
  - **Expected output**: labels resolve `board.starter` and `board.players`.

- **TC5: Current player state shown** ( :white_check_mark: manual )
  - **State**: new playable game after setup.
  - **Expected output**: current player, victory points, resources, and board
    hex ownership are visible.

- **TC6: Winner state shown** ( :white_check_mark: manual )
  - **State**: a player reaches 10 victory points.
  - **Expected output**: winner label and win log show the winning player.

- **TC7: Development card state shown** ( :white_check_mark: manual )
  - **State**: player has played knights or road-building cards.
  - **Expected output**: knight count, road count, and Largest Army holder
    are visible.

## Actions

- **TC8: Roll Dice updates resource state/log** ( :white_check_mark: manual )
  - **State**: user clicks `board.roll`.
  - **Expected output**: dice roll is logged and resource counts refresh.

- **TC9: Build Settlement validates position/resources** ( :white_check_mark: manual )
  - **State**: user selects a hex and clicks `board.build`.
  - **Expected output**: successful build updates owner/victory/resources;
    invalid build shows localized failure log text.

- **TC10: Buy Development Card uses selected resources** ( :white_check_mark: manual )
  - **State**: user selects Monopoly and Year of Plenty resources, then clicks
    `board.buyDevCard`.
  - **Expected output**: drawn card applies the matching resource choices.

- **TC11: End Turn advances current player** ( :white_check_mark: manual )
  - **State**: user clicks `board.endTurn`.
  - **Expected output**: current-player label changes to the next player.

- **TC12: Actions after winner log game over** ( :white_check_mark: manual )
  - **State**: a player has already won.
  - **Expected output**: roll/build/buy/end-turn controls log localized
    game-over text instead of changing state.

- **TC13: Locale switch reflected in all strings** ( :white_check_mark: manual )
  - **State**: active locale = Spanish or Mandarin.
  - **Expected output**: labels, controls, and log messages use that locale.
