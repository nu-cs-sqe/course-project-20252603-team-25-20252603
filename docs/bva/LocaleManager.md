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

- **TC1: Single locale bundle present** ( :white_check_mark: )
    - **State**: `bundleDir` contains only `messages_en.properties`.
    - **Expected output**: `getAvailableLocales()` returns `[en]`;
      `getActiveLocale()` returns `en` (default = first available).

- **TC2: Two locale bundles present, deterministic ordering** ( :white_check_mark: )
    - **State**: `bundleDir` contains `messages_en.properties` and
      `messages_es.properties`.
    - **Expected output**: `getAvailableLocales()` returns `[en, es]`
      (sorted by language tag); `getActiveLocale()` returns `en`.

- **TC3: Empty bundle directory rejected** ( :white_check_mark: )
    - **State**: `bundleDir` exists but contains no `messages_*.properties`.
    - **Expected output**: throws `IllegalStateException` (rubric: fail-fast
      with a clear message directing the developer to add a bundle).

- **TC4: Files that don't match the pattern are ignored** ( :white_check_mark: )
    - **State**: `bundleDir` contains `messages_en.properties`,
      `README.md`, and `messages.txt`.
    - **Expected output**: `getAvailableLocales()` returns `[en]`.

- **TC5: Null bundle directory rejected** ( :white_check_mark: )
    - **State**: `bundleDir = null`.
    - **Expected output**: throws `NullPointerException`.

## Method under test: `getAvailableLocales()`
- **TC6: Returned list is unmodifiable** ( :white_check_mark: )
    - **State**: any non-empty manager.
    - **Expected output**: attempting to mutate the returned list throws
      `UnsupportedOperationException`.

## Method under test: `setActiveLocale(Locale locale)`

- **TC7: Switches active locale to a discovered locale** ( :white_check_mark: )
    - **State**: manager with `[en, es]`, currently active `en`.
    - **Action**: `setActiveLocale(new Locale("es"))`.
    - **Expected output**: `getActiveLocale()` returns `es`; subsequent
      `get(key)` resolves from the `es` bundle.

- **TC8: Null locale rejected** ( :white_check_mark: )
    - **State**: any manager.
    - **Expected output**: `setActiveLocale(null)` throws
      `NullPointerException`; active locale is unchanged.

- **TC9: Locale not in available set rejected** ( :white_check_mark: )
    - **State**: manager with `[en]`.
    - **Action**: `setActiveLocale(new Locale("fr"))`.
    - **Expected output**: throws `IllegalArgumentException`; active locale
      is unchanged.

## Method under test: `getActiveLocale()`
- **TC10: Returns first available locale by default** ( implemented in TC1, TC2 )
- **TC11: Returns the locale set via setActiveLocale** ( implemented in TC7 )

## Method under test: `get(String key)`

- **TC12: Returns the value from the active bundle** ( :white_check_mark: )
    - **State**: manager with `en` bundle containing `app.title=CATAN`,
      active locale `en`.
    - **Expected output**: `get("app.title")` returns `"CATAN"`.

- **TC13: Returns localized value after switching locale** ( implemented in TC7 )
    - **State**: manager with `en` (`setup.start=Start Game`) and `es`
      (`setup.start=Empezar partida`); active locale just switched to `es`.
    - **Expected output**: `get("setup.start")` returns `"Empezar partida"`.

- **TC14: Null key rejected** ( :white_check_mark: )
    - **State**: any manager.
    - **Expected output**: throws `NullPointerException`.

- **TC15: Missing key throws MissingResourceException** ( :white_check_mark: )
    - **State**: manager whose active bundle has no entry for `nope.key`.
    - **Expected output**: `get("nope.key")` throws
      `MissingResourceException`.

## Method under test: `get(String key, Object... args)`

- **TC16: Substitutes positional arguments via MessageFormat** ( :white_check_mark: )
    - **State**: active bundle has `setup.player.name.prompt=Player {0} name:`.
    - **Expected output**: `get("setup.player.name.prompt", 1)` returns
      `"Player 1 name:"`.

- **TC17: Zero args delegates to plain get** ( :white_check_mark: )
    - **State**: active bundle has `setup.start=Start Game`; caller passes
      either an empty argument array or a null argument array.
    - **Expected output**: `get("setup.start", args)` returns `"Start Game"`.

- **TC18: Region bundle tag is parsed** ( :white_check_mark: )
    - **State**: `bundleDir` contains `messages_en_US.properties`.
    - **Expected output**: `getAvailableLocales()` returns `[en_US]` and
      lookups resolve through that bundle.

- **TC19: Non-directory bundle path rejected** ( :white_check_mark: )
    - **State**: constructor receives a regular file path instead of a
      directory.
    - **Expected output**: throws `IllegalStateException` because no bundles
      can be loaded from the supplied path.

## Method under test: `containsAnyBundle(Path)` (package-private; called from `discoverDefaultBundleDir` during `getInstance`)

`containsAnyBundle` is the defensive guard that lets the classpath scanner
skip non-directory file URLs when discovering the bundle root. The classpath
typically only exposes directory roots, so this branch needs an explicit
boundary test to be exercised.

- **TC20: Non-directory path reports no bundles** ( :white_check_mark: )
    - **State**: a path that exists as a regular file (not a directory),
      regardless of name.
    - **Expected output**: `containsAnyBundle(path)` returns `false`, so the
      classpath scanner continues to the next root instead of attempting to
      load bundles from a non-directory.

- **TC21: Directory with matching bundle reports bundles** ( :white_check_mark: )
    - **State**: a directory contains at least one
      `messages_<lang>.properties` file.
    - **Expected output**: `containsAnyBundle(path)` returns `true`.

## Method under test: `getInstance()` / classpath discovery helpers

- **TC22: Singleton returns cached manager** ( :white_check_mark: )
    - **State**: process-wide manager has already been initialized from the
      test classpath.
    - **Expected output**: a second `getInstance()` call returns the same
      object without rediscovering bundles.

- **TC23: Bundle reader failure is wrapped** ( :white_check_mark: )
    - **State**: bundle directory exists, but the low-level reader throws
      `IOException`.
    - **Expected output**: the loader throws `IllegalStateException` with the
      I/O failure as the cause.

- **TC24: Discovery skips non-file roots and finds bundle directory** ( :white_check_mark: )
    - **State**: classpath root enumeration contains a non-file URL followed by
      a file URL for a directory with a matching bundle.
    - **Expected output**: discovery ignores the non-file root and returns the
      bundle directory.

- **TC25: Discovery rejects classpath with no bundles** ( :white_check_mark: )
    - **State**: classpath root enumeration is empty.
    - **Expected output**: discovery throws `IllegalStateException` explaining
      that no bundles were found.

- **TC26: Discovery wraps classpath scan failure** ( :white_check_mark: )
    - **State**: classloader throws `IOException` while enumerating roots.
    - **Expected output**: discovery throws `IllegalStateException` with the
      scan failure as the cause.
