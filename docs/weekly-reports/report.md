# Carry-over assignments (Game Setup — unowned work split, May 2026)

Tasks below were previously unassigned or marked **All**. Status reflects `main`
as of PR #30 unless noted.

| # | Owner | Task | Status |
|---|--------|------|--------|
| A | **Daniel Wu** | `PlayerSetupFrame` + wire `Main` (locale → player setup → `GameSetup.build()`) + Swing CJK font helper | done — merged PR #21 |
| B | **Daniel Kim** | Extend `LocaleClasspathIntegrationTest` for `zh`; add negative-path locale cases | done — PR #26 (landed by Daniel Wu after reassignment from #17) |
| C | **Julian Tang** | `@Tag("integration")` on integration tests + Gradle task/filter; `BoardFrame` stub | done — merged PR #27 and PR #29 |
| D | **Shun Fujita** | Expand `GameSetupIntegrationTest` (invalid count, duplicate name/color) | done — merged PR #28 |
| E | **All** | Review instructor feedback PR #19; keep `report.md` PR links current | done — merged PR #19 and PR #25 |

---

# Week 8 (05/18/2026-05/24/2026)

**Theme**: Land the instructor's Week 7 code-review feedback. The instructor
read every line of production code on `main` (PR #19 from 5/20) and called out
two things to fix: (1) "your code is full of null checks", and (2) only
`LocaleManager` had method-level Javadoc, so the rest of the codebase was
inconsistent. Both are graded under the A-grade rubric for code standards
(Clean Code Ch. 4 on Comments and Ch. 7 on Error Handling), so we addressed
them on a dedicated branch before stacking more features on top.

**Decisions made this week**:
- Replace the team's `if (x == null) throw new IllegalArgumentException(...)`
  pattern for **pure** null guards with `java.util.Objects.requireNonNull(x, msg)`.
  That is a one-line, idiomatic-Java replacement that throws
  `NullPointerException` (the convention modern Java picks for required
  reference parameters). Trade-off: the affected unit tests had to switch
  from `assertThrows(IllegalArgumentException.class, ...)` to
  `assertThrows(NullPointerException.class, ...)` for those exact cases.
- Keep **composite** validators as `IllegalArgumentException`. The instructor's
  Clean Code complaint is about scattered standalone null checks, not about
  domain rule validation. So:
  - `Player`/`PlayerRegistration` blank-name check stays `IAE`.
  - `NumberToken` value-range, `Hex` position-range and terrain↔token
    coupling, `GameSetup.registerPlayers` count/uniqueness/contains-null,
    `TurnOrder` size/uniqueness/contains-null, `Game` empty/contains-null
    players — all stay `IAE`. Same for `LocaleManager.setActiveLocale`
    unknown-locale.
- Add a single-sentence summary plus a `@param/@return/@throws` block to
  every public constructor and non-trivial public method, matching the
  style already in `LocaleManager`. Enums and trivial accessors stay
  comment-free; Clean Code Ch. 4 still applies (self-documenting names
  beat redundant comments).
- Defer one related cleanup (the `PlayerSetupFrame` private `collectRegistrations`
  helper that returns `null` as a sentinel for "validation failed") to a
  follow-up branch. It is also a "passing null around" smell but lives
  inside one private method and is out of scope for this PR.

**Planning and Progress Tracking**:
1. [done] All: Read and discuss instructor feedback in `docs/weekly-reports/feedback-codereview.md` after merging PR #19.
2. [done] Daniel Kim: Add class-level + method-level Javadoc to all 17 production classes (`PlayerColor`, `TerrainType`, `DevelopmentCardType`, `Player`, `PlayerRegistration`, `Hex`, `NumberToken`, `Board`, `DevelopmentCard`, `DevelopmentCardDeck`, `Game`, `GameSetup`, `TurnOrder`, `Main`, `LocaleSelectionFrame`, `PlayerSetupFrame`, `SwingLocaleSupport`). `LocaleManager` already had it.
3. [done] Daniel Kim: Replace 14 pure-null `if-throw IAE` blocks across `Player`, `PlayerRegistration`, `Hex`, `Board`, `DevelopmentCard`, `DevelopmentCardDeck`, `Game`, `GameSetup`, `LocaleManager`, `LocaleSelectionFrame`, and `PlayerSetupFrame` with `Objects.requireNonNull`. Update the corresponding 13 unit tests to assert `NullPointerException`.
4. [done] Daniel Wu: `PlayerSetupFrame` + wire `Main` (locale → player setup → game ready). Carry-over Task A — merged PR #21.
5. [in progress] All: Land the Clean Code feedback PR (`chore/clean-code-feedback`) on `main`. Local `./gradlew check` passes (271 unit tests + integration tests, Checkstyle clean, SpotBugs clean, Jacoco run, Pitest 96% test strength).
6. [ongoing] All: Code review and merges to `main`.

**Risks / Notes**:
- `NullPointerException` from `Objects.requireNonNull` carries the same
  diagnostic value as the old `IllegalArgumentException("x must not be null")`
  because both encode the parameter name in the message. No information loss.
- Future contributors: if you add a constructor argument that must not be
  null, use `Objects.requireNonNull(arg, "arg")`. Only fall back to
  `if-throw IAE` when the check is composite (null *and* a range or shape
  rule), in which case `IAE` is the right exception.

---

# Week 7 (05/11/2026-05/17/2026)

**Theme**: Land the rest of the Game Setup domain (Board, Deck, Turn Order,
GameSetup orchestrator) and stand up the quality tooling the rubric expects:
Jacoco for code coverage and Pitest for mutation testing. Also start planning
the Integration Testing pass and add Mandarin Chinese as the third locale so
the i18n architecture proves it can take a new language with no code changes.

**Decisions made this week**:
- Code coverage tool: **Jacoco**, wired into `build.gradle.kts` so coverage runs
  as part of `./gradlew test`. We picked it because it matches the code coverage
  lab reference and it is the standard Gradle plugin.
- Mutation testing tool: **Pitest** via the Gradle Pitest plugin
  (gradle-pitest-plugin.solidsoft.info), again to match the lab reference and
  the B-grade rubric line that asks for 100% mutants killed.
- Third locale: **Mandarin Chinese** (`zh`). Adding it must be a resource-only
  change so we have evidence for the A-grade locale rubric. Drop in
  `messages_zh.properties`, no Java edits.
- Integration testing scope: two main features, **Game Setup** and **Locale
  Selection**, to satisfy the A-grade rubric line about integration tests on at
  least two main features. Tests will live in `src/test/java/integration/` and
  drive the domain through public APIs (no Swing, no mocks).

**Planning and Progress Tracking**:
1. [done] Julian Tang: `Board` + `Hex` + `NumberToken` + `TerrainType` with BVA
   and tests, finishing the carry-over from Week 6 item 6. Merged PR #11.
2. [done] Shun Fujita: `DevelopmentCardDeck`, `TurnOrder`, `GameSetup`
   orchestrator, `Game`, `PlayerRegistration` with BVA and tests, finishing the
   carry-over from Week 6 item 7. Merged PR #13.
3. [in progress] Julian Tang: Jacoco coverage and Pitest mutation testing wired
   into `build.gradle.kts` (commit 902fe22 on `chore/jacoco-pitest`). PR open,
   waiting on review.
4. [done] All: Integration Testing plan documented on the project management
   board (Item 4). Covers IT-1 Game Setup happy path and IT-2 Locale selection
   end-to-end, plus negative paths.
5. [done] All: i18n plan for Mandarin Chinese documented on the project
   management board (Item 5). Plan calls for a single new `messages_zh.properties`
   file plus a font fallback in Swing for CJK glyphs.
6. [done] All: Week 7 progress documented in `docs/weekly-reports/report.md`
   (Item 6, this file).
7. [done] All: Integration tests for Game Setup and Locale Selection
   implemented in `src/test/java/integration/`
   (`GameSetupIntegrationTest.java`, `LocaleClasspathIntegrationTest.java`).
   Tagging with `@Tag("integration")` deferred to Week 8/9.
8. [done] Daniel Wu: `messages_zh.properties` added under
   `src/main/resources/` with translations for all 10 existing keys. Diff contains
   no Java changes so we can point at it as proof the locale architecture is open
   for extension. Merged PR #20.
9. [ongoing] All: Code review and merges to `main`.

**Risks / Notes**:
- The Jacoco and Pitest PR needs to merge before we can show coverage and
  mutation numbers in next week's report.
- Mandarin font rendering in Swing needs a quick check on each team member's
  machine. If the default font is missing CJK glyphs we will switch the
  affected frames to a logical family like `SansSerif` once, for all locales.
- Integration tests should run headless on CI. Anything that opens a Swing
  frame stays out of the integration suite.
- Keep pulling `main` into feature branches frequently to avoid merge pain,
  same as last week.

---

# Week 6 (05/04/2026-05/10/2026)

**Theme**: Kick off the **Game Setup Phase** for CATAN. Establish locale infrastructure
(English + Spanish) early so it does not bolt on at the end. Practice TDD with
documented BVA on every public method. Java Swing chosen for the GUI.

**Decisions made this week**:
- GUI: **Java Swing** (matches Chess reference; team already familiar with it).
- Locale support: **English + Spanish** initially; architecture must allow new
  locales by adding a `messages_xx.properties` file only (no code changes).
- Game Setup scope (this week): player count validation (3–4), player creation
  (name + color), turn order initialization, randomized hex board (terrains +
  number tokens), and dev card deck shuffle. Initial settlement/road placement
  is deferred to a later week.

**Planning and Progress Tracking**:
1. [done] All: User Story + Use Cases for Game Setup (`docs/requirements/`) — merged PR #3
2. [done] All: Design doc with classes, methods, and i18n architecture (`docs/design/`) — merged PR #3
3. [done] Daniel Kim: `Player` + `PlayerColor` (BVA + TDD) — merged PR #4
4. [done] All: Checkstyle + SpotBugs setup (W3 Item 3) — merged PR #5
5. [done] Daniel Wu: `LocaleManager` + `messages_en.properties` + `messages_es.properties` + minimal Swing locale picker (BVA + TDD) — merged PR #7, #8
6. [not started] Julian Tang: `Board` (hex generation, terrains, number tokens) including `TerrainType`, `NumberToken`, `Hex` (BVA + TDD) — PR TBD
7. [not started] Shun Fujita: `GameSetup` orchestrator + `TurnOrder` + `DevelopmentCardDeck` + `Game` + `PlayerRegistration` (BVA + TDD) — depends on Board — PR TBD
8. [not started] All: Integration tests on **Game Setup** and **Locale Selection** features (rubric: ≥2 main features) — PR TBD
9. [ongoing] All: Code review + merges to `main`

**Risks / Notes**:
- Keep methods small and named per Clean Code (rubric: code standards).
- Pull `main` into feature branches frequently to avoid merge pain.
- Each PR must include the BVA file(s) updated with `:white_check_mark:` for
  newly implemented test cases.

---

# Week 3 (04/13/2026-04/19/2026)
**Planning and Progress Tracking**:
1. [done] Person: Task (Links to PR)
2. [not started] Person: Task (Links to PR)
3. [80% done] Person: Task (Links to PR)


# Week X (XX/XX/2026-XX/XX/2026) TEMPLATE (You can change the format to whatever the team likes better)
**Planning and Progress Tracking**:
1. [done] Person: Task (Links to PR)
2. [not started] Person: Task (Links to PR)
3. [80% done] Person: Task (Links to PR)
