# Week 10 — final (06/01/2026-06/10/2026)

**Theme**: Ship a playable game, hit coverage/mutation targets, and prep final
submission docs.

**What we changed**:
- Scoped play to a simplified CATAN model: hex ownership, dice production, dev
  cards, Largest Army, first to 10 VP. Roads, ports, trading, robber-on-7, and
  cities stay out of scope.
- Jacoco and PIT report on `domain` code only (not Swing or bare enums).
- Documented one equivalent PIT mutant in `PlayableGame.applyMonopoly` (`> 0` vs
  `>= 0` when amount is zero).
- Read instructor Week 10 rubric feedback — no issues on reviewed items
  (`feedback-codereview.md`, merged PR #32).

**Planning and Progress Tracking**:
1. [done] All: `PlayableGame` domain + BVA + playable `BoardFrame` — PR #33
2. [done] All: 10-point win condition — PR #34
3. [done] All: Development card effects in play — PR #35
4. [done] All: Domain-only Jacoco/PIT scope + mutation tests — PR #36
5. [done] All: Playable-game integration tests — PR #37, #40
6. [done] All: Mutation cleanup to 100% effective score — PR #38
7. [done] All: Cyclomatic coverage gaps closed — PR #39
8. [done] All: UTF-8 compile encoding — PR #41
9. [done] All: i18n write-up — `docs/i18n.md` (PR #42)
10. [done] All: Clean Code standards doc — `docs/clean-code-review.md` (PR #43)
11. [done] All: Process/board snapshot — `docs/project-board.md` (PR #44)

**Risks / Notes**:
- On `main` (June 10): `./gradlew clean check` and `./gradlew integrationTest`
  pass; domain Jacoco 100% / 0 missed complexity; PIT 100% effective mutation
  score (1 equivalent mutant documented).
- Before Canvas submit: confirm `main` still requires PR review + Gradle check;
  describe the product as our simplified model, not full official CATAN.

---

# Week 9 (05/25/2026-05/31/2026)

**Theme**: Finish Game Setup carry-over — full UI flow, integration tests, and
board stub.

**What we changed**:
- Wired locale → player setup → `GameSetup.build()` in `Main` via
  `PlayerSetupFrame`.
- Added Mandarin as a resource-only locale (`messages_zh.properties`).
- Expanded integration coverage for Game Setup and Locale Selection (en/es/zh
  plus negative paths).
- Tagged integration tests and added `./gradlew integrationTest`.
- Replaced `BoardFrame` placeholder after setup completes.

**Planning and Progress Tracking**:
1. [done] Daniel Wu: `PlayerSetupFrame` + `Main` flow — merged PR #21
2. [done] Daniel Wu: `messages_zh.properties` — merged PR #20
3. [done] All: Clean Code feedback (null checks + Javadoc) — merged PR #25
4. [done] Daniel Wu: Locale IT for en/es/zh + negative paths — merged PR #26
5. [done] Julian Tang: `@Tag("integration")` + `integrationTest` task — merged PR #27
6. [done] Shun Fujita: Expanded `GameSetupIntegrationTest` — merged PR #28
7. [done] Julian Tang: `BoardFrame` stub — merged PR #29
8. [done] All: Carry-over status in `report.md` — merged PRs #30, #31
9. [done] All: Closed Week 4–6 feedback PRs (#1, #2, #10, #19)

**Risks / Notes**:
- Setup phase is complete on `main`; next step is playable turn logic.
- Carry-over table below reflects final status as of PR #31.

---

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

**Theme**: Address instructor Week 7 code review — too many null checks and
inconsistent Javadoc.

**What we changed**:
- Swapped standalone null guards for `Objects.requireNonNull` on required
  constructor args (throws `NullPointerException` instead of `IAE`). Updated
  the matching unit tests.
- Left composite validation as `IllegalArgumentException` (blank names, bad
  dice values, duplicate colors, etc.) — that is domain rules, not null noise.
- Added Javadoc on public domain APIs to match `LocaleManager`.
- Did not refactor `PlayerSetupFrame.collectRegistrations` (null sentinel) in
  this pass — tracked as follow-up.

**Planning and Progress Tracking**:
1. [done] All: Read and discuss instructor feedback in `docs/weekly-reports/feedback-codereview.md` after merging PR #19.
2. [done] Daniel Kim: Add class-level + method-level Javadoc to all 17 production classes (`PlayerColor`, `TerrainType`, `DevelopmentCardType`, `Player`, `PlayerRegistration`, `Hex`, `NumberToken`, `Board`, `DevelopmentCard`, `DevelopmentCardDeck`, `Game`, `GameSetup`, `TurnOrder`, `Main`, `LocaleSelectionFrame`, `PlayerSetupFrame`, `SwingLocaleSupport`). `LocaleManager` already had it.
3. [done] Daniel Kim: Replace 14 pure-null `if-throw IAE` blocks across `Player`, `PlayerRegistration`, `Hex`, `Board`, `DevelopmentCard`, `DevelopmentCardDeck`, `Game`, `GameSetup`, `LocaleManager`, `LocaleSelectionFrame`, and `PlayerSetupFrame` with `Objects.requireNonNull`. Update the corresponding 13 unit tests to assert `NullPointerException`.
4. [done] Daniel Wu: `PlayerSetupFrame` + wire `Main` (locale → player setup → game ready). Carry-over Task A — merged PR #21.
5. [done] All: Land the Clean Code feedback PR (`chore/clean-code-feedback`) on `main`. Merged PR #25.
6. [done] All: Code review and merges to `main`.

**Risks / Notes**:
- `requireNonNull` still puts the parameter name in the exception message, same
  as our old `IAE` messages.
- New code: use `requireNonNull` for required refs; use `IAE` when you are
  checking a business rule, not just null.

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
3. [done] Julian Tang: Jacoco coverage and Pitest mutation testing wired
   into `build.gradle.kts`. Merged with later PRs #36–#39.
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
6. [done] Julian Tang: `Board` + hex/token types (BVA + TDD) — merged PR #11 (Week 7).
7. [done] Shun Fujita: `GameSetup` orchestrator + deck + turn order (BVA + TDD) — merged PR #13 (Week 7).
8. [done] All: Integration tests on Game Setup and Locale Selection — merged PRs #28, locale IT branch.
9. [ongoing] All: Code review + merges to `main`

**Risks / Notes**:
- Keep methods small and named per Clean Code (rubric: code standards).
- Pull `main` into feature branches frequently to avoid merge pain.
- Each PR must include the BVA file(s) updated with `:white_check_mark:` for
  newly implemented test cases.

---
