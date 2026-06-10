# Clean Code Standards and Review

This document records the coding standards the team follows, where each one
is enforced automatically, and how we responded to instructor code-review
feedback. Chapter references are to *Clean Code* (Martin), matching the
chapters covered in the instructor's weekly review (1, 2, 3, 4, 5, 6, 7, 9,
10).

## Automated enforcement

Style is not left to discipline; the build fails on violations:

- **Checkstyle** (`config/checkstyle/checkstyle.xml`): Google Java Style with
  two documented team overrides (4-space indentation, Javadoc optional in
  favor of self-documenting names). `maxWarnings = 0` and
  `isIgnoreFailures = false`, so a single violation fails `./gradlew check`
  and therefore CI.
- **SpotBugs**: high-confidence bug patterns fail the build.
- **PIT + Jacoco**: test quality gates (see
  [`docs/testing/mutation-coverage.md`](testing/mutation-coverage.md)).

## Standards by Clean Code chapter

### Ch. 2 — Meaningful names

Intention-revealing, pronounceable names; no abbreviations
(`AbbreviationAsWordInName` allows none). Domain types read as the game's
ubiquitous language: `PlayableGame.buildSettlement`, `ResourceInventory.canAfford`,
`DevelopmentCardDeck.standardShuffled`, `TurnOrder.advance`.

### Ch. 3 — Functions

Small functions that do one thing at one level of abstraction. Long
operations are decomposed into named private steps — e.g.
`PlayableGame.buildSettlement` delegates to `rejectIfGameOver`, `hexAt`, and
inventory `spend` rather than inlining validation. Jacoco reports 0 missed
complexity across the domain, which keeps every branch small enough to test
directly.

### Ch. 4 — Comments

Code should explain itself; comments are reserved for *why*, not *what*.
This is codified as a team override in `checkstyle.xml`: Javadoc is required
style-checked when present (`JavadocMethod`, `SummaryJavadoc`,
`AtclauseOrder`) and used on public domain APIs that carry contracts
(`@throws` conditions, valid ranges), while trivial accessors stay
uncommented. No commented-out code is kept; history lives in git.

### Ch. 5 — Formatting

Google Java Style, enforced by Checkstyle: 100-column limit, import order,
brace placement, whitespace, and the team's 4-space indent override (noted
inline in the config with its rationale).

### Ch. 6 — Objects and data structures

Domain objects hide their representation. Collections are never exposed
mutably: `PlayableGame.ownedHexes` and `LocaleManager.getAvailableLocales`
return unmodifiable views; `DevelopmentCardDeck` exposes counts, not its card
list. The `domain` packages have no dependency on `ui`; Swing classes are
pure views that hold no game state.

### Ch. 7 — Error handling

- Exceptions, never return codes: invalid input throws
  `IllegalArgumentException` with a message naming the violated bound
  (`"die face must be in [1, 6]"`); illegal state throws
  `IllegalStateException`.
- **Don't return null, don't pass null** (instructor Week 7 feedback): absent
  values are modeled with `Optional` — `PlayableGame.winner()`,
  `ownerOf(position)`, `Hex.getToken()`, `ResourceType.fromTerrain()` — so
  callers can never forget a null check. The remaining
  `Objects.requireNonNull` calls sit only at public constructor/factory
  boundaries to fail fast at the system's perimeter with a named message;
  internal code neither passes nor checks null.
- No empty catch blocks (`EmptyCatchBlock` is enforced).

### Ch. 9 — Unit tests

- Tests follow Arrange-Act-Assert with one behavior per test; multi-property
  outcomes use `assertAll` so every failing assertion is reported.
- Every domain class has a boundary-value analysis table in
  [`docs/bva/`](bva/), and test names are anchored to its rows (`tc13`,
  `tc30`), keeping tests F.I.R.S.T. and traceable to the definition of done.
- Determinism: all randomness is injected (`new Random(42)`), never ambient.
- Test quality is itself tested: 100% of non-equivalent mutants killed, with
  the single equivalent mutant argued in writing
  ([`docs/testing/mutation-coverage.md`](testing/mutation-coverage.md)).

### Ch. 10 — Classes

Single responsibility per class: board construction (`Board`), deck
(`DevelopmentCardDeck`), turn rotation (`TurnOrder`), setup orchestration
(`GameSetup`), play rules (`PlayableGame`), localization (`LocaleManager`).
Classes are `final` with package-private seams for tests rather than
inheritance hooks. The locale subsystem follows the Open-Closed Principle:
new languages are added without modifying any existing class (see
[`docs/i18n.md`](i18n.md)).

## Response to instructor code review (Week 7)

1. **Null checks** — addressed as described under Ch. 7: `Optional` for all
   absent-value returns, and `requireNonNull` retained only as fail-fast
   guards on public entry points, never sprinkled through internal logic.
2. **Inconsistent Javadoc** — public domain APIs with non-obvious contracts
   (`PlayableGame`, `DevelopmentCardDeck`, `LocaleManager`, `GameSetup`) now
   carry consistent Javadoc including `@throws` clauses; the policy itself is
   documented in `checkstyle.xml` so it survives the team.
