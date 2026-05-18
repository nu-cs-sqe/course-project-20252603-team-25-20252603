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
7. [not started] All: Integration tests for Game Setup and Locale Selection
   actually implemented in `src/test/java/integration/`, tagged
   `@Tag("integration")`. PR TBD.
8. [not started] Daniel Wu: `messages_zh.properties` added under
   `src/main/resources/` with translations for all 10 existing keys. Diff should
   contain no Java changes so we can point at it as proof the locale
   architecture is open for extension. PR TBD.
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
