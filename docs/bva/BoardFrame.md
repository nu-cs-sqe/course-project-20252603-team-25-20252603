# BVA — `BoardFrame`

Swing UI stub shown after `GameSetup.build()` completes. Renders setup-complete
state only; no game-play logic. Automated headless UI tests are out of scope;
cases below are verified manually via `./gradlew run`.

## Constructor `BoardFrame(LocaleManager, Game)`

- **TC1: Null localeManager rejected** ( :white_check_mark: manual )
  - **State**: `localeManager = null`, `game` valid.
  - **Expected output**: `NullPointerException` with message `localeManager must not be null`.

- **TC2: Null game rejected** ( :white_check_mark: manual )
  - **State**: `localeManager` valid, `game = null`.
  - **Expected output**: `NullPointerException` with message `game must not be null`.

## Rendering

- **TC3: Title resolved from active locale** ( :white_check_mark: manual )
  - **State**: active locale = English.
  - **Expected output**: window title equals `board.title` (`CATAN - Board`).

- **TC4: Starter player name shown** ( :white_check_mark: manual )
  - **State**: 3 players registered; first player named `Shun`.
  - **Expected output**: a label resolves `board.starter` with `Shun`
    (`Starting player: Shun`).

- **TC5: All player names shown, comma-separated** ( :white_check_mark: manual )
  - **State**: 3 players `Shun`, `Julian`, `Daniel`.
  - **Expected output**: a label resolves `board.players` with
    `Shun, Julian, Daniel`.

- **TC6: Placeholder text shown** ( :white_check_mark: manual )
  - **State**: any valid game.
  - **Expected output**: a label shows `board.placeholder`
    (`Board UI coming in a later iteration.`).

- **TC7: Locale switch reflected in all strings** ( :white_check_mark: manual )
  - **State**: active locale = Spanish.
  - **Expected output**: title, starter, players, and placeholder labels all
    use the Spanish strings from `messages_es.properties`.
