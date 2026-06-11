# BVA — `LocaleSelectionFrame`

Modal dialog listing every locale from `LocaleManager.getAvailableLocales()`.
Each button is labeled with `locale.getDisplayName(locale)`. Selecting a button
calls the supplied callback once and closes the dialog.

Manual verification via `./gradlew run`.

## Constructor `LocaleSelectionFrame(Frame, LocaleManager, Consumer<Locale>)`

- **TC1: Null localeManager rejected** ( :white_check_mark: manual )
    - **State**: `localeManager = null`.
    - **Expected output**: `NullPointerException`.

- **TC2: Null onSelected rejected** ( :white_check_mark: manual )
    - **State**: `onSelected = null`.
    - **Expected output**: `NullPointerException`.

- **TC3: One button per discovered locale** ( :white_check_mark: manual )
    - **State**: classpath bundles for en, es, zh.
    - **Expected output**: three buttons; labels readable in each language.

- **TC4: Selection invokes callback with chosen locale** ( :white_check_mark: manual )
    - **State**: click the Spanish button.
    - **Expected output**: callback receives `es`; dialog closes; next screen uses Spanish strings.

- **TC5: Dialog title uses active bundle** ( :white_check_mark: manual )
    - **State**: default locale on first paint (English bundle).
    - **Expected output**: title resolves `locale.select.title`.
