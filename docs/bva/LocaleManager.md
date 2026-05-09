# BVA — `LocaleManager`

`LocaleManager` is the i18n entry point. It discovers locales at runtime by
scanning a directory of `messages_<lang>.properties` files (so adding a new
locale never requires a code change), holds the active `Locale`, and resolves
keys to localized strings.

Boundary inputs:
- **bundle directory contents**: 0 bundles, 1 bundle, 2+ bundles, malformed
  filenames mixed with valid ones.
- **`Locale` argument to `setActiveLocale`**: `null`, a locale not present in
  the discovered set, a locale that *is* present.
- **`String key` argument to `get`**: `null`, missing key, present key.
- **`Object[] args` to `get(key, args)`**: 0 args, 1 arg, multiple args.

## Method under test: `LocaleManager(Path bundleDir)` (package-private; production singleton uses the same constructor with a discovered classpath dir)

- **TC1: Single locale bundle present** ( :x: )
    - **State**: `bundleDir` contains only `messages_en.properties`.
    - **Expected output**: `getAvailableLocales()` returns `[en]`;
      `getActiveLocale()` returns `en` (default = first available).

- **TC2: Two locale bundles present, deterministic ordering** ( :x: )
    - **State**: `bundleDir` contains `messages_en.properties` and
      `messages_es.properties`.
    - **Expected output**: `getAvailableLocales()` returns `[en, es]`
      (sorted by language tag); `getActiveLocale()` returns `en`.

- **TC3: Empty bundle directory rejected** ( :x: )
    - **State**: `bundleDir` exists but contains no `messages_*.properties`.
    - **Expected output**: throws `IllegalStateException` (rubric: fail-fast
      with a clear message directing the developer to add a bundle).

- **TC4: Files that don't match the pattern are ignored** ( :x: )
    - **State**: `bundleDir` contains `messages_en.properties`,
      `README.md`, and `messages.txt`.
    - **Expected output**: `getAvailableLocales()` returns `[en]`.

- **TC5: Null bundle directory rejected** ( :x: )
    - **State**: `bundleDir = null`.
    - **Expected output**: throws `IllegalArgumentException`.

## Method under test: `getAvailableLocales()`
- **TC6: Returned list is unmodifiable** ( :x: )
    - **State**: any non-empty manager.
    - **Expected output**: attempting to mutate the returned list throws
      `UnsupportedOperationException`.

## Method under test: `setActiveLocale(Locale locale)`

- **TC7: Switches active locale to a discovered locale** ( :x: )
    - **State**: manager with `[en, es]`, currently active `en`.
    - **Action**: `setActiveLocale(new Locale("es"))`.
    - **Expected output**: `getActiveLocale()` returns `es`; subsequent
      `get(key)` resolves from the `es` bundle.

- **TC8: Null locale rejected** ( :x: )
    - **State**: any manager.
    - **Expected output**: `setActiveLocale(null)` throws
      `IllegalArgumentException`; active locale is unchanged.

- **TC9: Locale not in available set rejected** ( :x: )
    - **State**: manager with `[en]`.
    - **Action**: `setActiveLocale(new Locale("fr"))`.
    - **Expected output**: throws `IllegalArgumentException`; active locale
      is unchanged.

## Method under test: `getActiveLocale()`
- **TC10: Returns first available locale by default** ( implemented in TC1, TC2 )
- **TC11: Returns the locale set via setActiveLocale** ( implemented in TC7 )

## Method under test: `get(String key)`

- **TC12: Returns the value from the active bundle** ( :x: )
    - **State**: manager with `en` bundle containing `app.title=CATAN`,
      active locale `en`.
    - **Expected output**: `get("app.title")` returns `"CATAN"`.

- **TC13: Returns localized value after switching locale** ( :x: )
    - **State**: manager with `en` (`setup.start=Start Game`) and `es`
      (`setup.start=Empezar partida`); active locale just switched to `es`.
    - **Expected output**: `get("setup.start")` returns `"Empezar partida"`.

- **TC14: Null key rejected** ( :x: )
    - **State**: any manager.
    - **Expected output**: throws `IllegalArgumentException`.

- **TC15: Missing key throws MissingResourceException** ( :x: )
    - **State**: manager whose active bundle has no entry for `nope.key`.
    - **Expected output**: `get("nope.key")` throws
      `MissingResourceException`.

## Method under test: `get(String key, Object... args)`

- **TC16: Substitutes positional arguments via MessageFormat** ( :x: )
    - **State**: active bundle has `setup.player.name.prompt=Player {0} name:`.
    - **Expected output**: `get("setup.player.name.prompt", 1)` returns
      `"Player 1 name:"`.

- **TC17: Zero args delegates to plain get** ( implemented in TC12 )
