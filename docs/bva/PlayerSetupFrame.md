# BVA — `PlayerSetupFrame`

Swing UI; validation mirrors `GameSetup` / `PlayerRegistration` rules using localized
keys. Automated headless UI tests are out of scope; cases below are verified manually
via `./gradlew run`.

## Constructor `PlayerSetupFrame(Frame, LocaleManager, Consumer<Game>)`

- **TC1: Null localeManager rejected** ( :white_check_mark: manual )
    - **State**: `localeManager = null`.
    - **Expected output**: `IllegalArgumentException`.

- **TC2: Null onComplete rejected** ( :white_check_mark: manual )
    - **State**: `onComplete = null`.
    - **Expected output**: `IllegalArgumentException`.

## Action: Start Game

- **TC3: Empty player name shows localized error** ( :white_check_mark: manual )
    - **State**: one name field blank.
    - **Expected output**: error label shows `setup.player.name.empty`.

- **TC4: Duplicate player name shows localized error** ( :white_check_mark: manual )
    - **State**: two identical non-empty names.
    - **Expected output**: error label shows `setup.player.name.taken`.

- **TC5: Duplicate color shows localized error** ( :white_check_mark: manual )
    - **State**: two players share a color.
    - **Expected output**: error label shows `setup.player.color.taken`.

- **TC6: Valid three players builds game** ( :white_check_mark: manual )
    - **State**: 3 unique names and colors.
    - **Expected output**: dialog closes; `onComplete` receives `Game`; ready dialog shows
      `setup.ready` with first player name.
