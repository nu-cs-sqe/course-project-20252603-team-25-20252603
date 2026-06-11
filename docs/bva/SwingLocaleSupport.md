# BVA — `SwingLocaleSupport`

Utility that sets `Font.SANS_SERIF` on a Swing container tree so Latin and CJK
text render on typical JDK installs. Used by locale and setup dialogs.

Manual verification via `./gradlew run` with the Mandarin locale selected.

## Method under test: `applyLocaleFont(Container root)`

- **TC1: Null root rejected** ( :white_check_mark: manual )
    - **State**: `root = null`.
    - **Expected output**: `NullPointerException`.

- **TC2: Root container font updated** ( :white_check_mark: manual )
    - **State**: call on a `JDialog` with child buttons.
    - **Expected output**: root and children use sans-serif 14pt.

- **TC3: Mandarin labels render without tofu** ( :white_check_mark: manual )
    - **State**: active locale `zh`, open locale or setup screen.
    - **Expected output**: Chinese characters in button/label text are legible.
