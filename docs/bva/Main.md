# BVA — `Main`

Application entry point. Wires locale selection → player setup → board UI on
the Swing event-dispatch thread. No domain logic here.

Automated UI tests are out of scope; cases below are verified manually via
`./gradlew run`.

## Method under test: `main(String[] args)`

- **TC1: App launches locale screen first** ( :white_check_mark: manual )
    - **State**: fresh JVM start.
    - **Expected output**: `LocaleSelectionFrame` appears before any player setup prompt.

- **TC2: Full flow reaches board after valid setup** ( :white_check_mark: manual )
    - **State**: pick a locale, register 3 valid players, complete setup.
    - **Expected output**: `BoardFrame` opens with localized title and player list.

- **TC3: Unused args do not crash startup** ( :white_check_mark: manual )
    - **State**: launch with arbitrary command-line args.
    - **Expected output**: same locale screen as TC1.
