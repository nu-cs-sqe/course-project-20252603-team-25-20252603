# Clean Code — how we write code

Notes on team style and our response to instructor feedback (Week 7+).
Chapters refer to *Clean Code* (Martin), same set the course uses in review.

## What the build enforces

- **Checkstyle** (`config/checkstyle/checkstyle.xml`) — Google Java Style with
  team overrides (4-space indent; Javadoc checked when present). Any warning
  fails `./gradlew check` and CI.
- **SpotBugs** — high-confidence bug patterns fail the build.
- **Jacoco + PIT** — domain test quality; see
  [`docs/testing/mutation-coverage.md`](testing/mutation-coverage.md).

## Team conventions

**Names (Ch. 2)** — Domain types use game terms (`PlayableGame`, `TurnOrder`,
`ResourceInventory`) instead of abbreviations.

**Functions (Ch. 3)** — Public methods stay short. Validation lives in named
private helpers (for example `PlayableGame.rejectIfGameOver`).

**Comments (Ch. 4)** — Javadoc on public domain APIs that throw or have
non-obvious contracts. Trivial getters stay uncommented.

**Formatting (Ch. 5)** — Checkstyle enforces layout; we use 4-space indent
(documented in the config file).

**Objects (Ch. 6)** — `domain` never imports Swing. Collections returned to
callers are unmodifiable where it matters (`ownedHexes`, `getAvailableLocales`).

**Error handling (Ch. 7)** — Invalid input → `IllegalArgumentException` with a
clear message. Illegal state → `IllegalStateException`. Missing values →
`Optional` instead of null returns. `Objects.requireNonNull` only at public
entry points (instructor Week 7 feedback).

**Tests (Ch. 9)** — One behavior per test; BVA rows in `docs/bva/` map to test
names (`tc13`, `tc30`). Randomness always uses an injected `Random`.

**Classes (Ch. 10)** — One main job per class: setup (`GameSetup`), play rules
(`PlayableGame`), board (`Board`), i18n (`LocaleManager`), UI in `ui.swing`.

## Instructor feedback (Week 7)

1. **Too many null checks** — We use `Optional` for absent values and keep
   `requireNonNull` at boundaries only.
2. **Inconsistent Javadoc** — Public domain APIs now follow the same Javadoc
   style as `LocaleManager`.

## After the playable slice landed

- Play rules stay in `PlayableGame`; setup and deck logic stay in their own
  classes.
- Test-only seams in `LocaleManager` stay package-private.
- Mutation and coverage numbers live in
  [`docs/testing/mutation-coverage.md`](testing/mutation-coverage.md); last
  `./gradlew clean check` on `main` passes with 100% effective domain mutation
  score (one documented equivalent mutant).
