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
1. [in progress] All: User Story + Use Cases for Game Setup (`docs/requirements/`) — PR TBD
2. [in progress] All: Design doc with classes, methods, and i18n architecture (`docs/design/`) — PR TBD
3. [not started] Daniel Kim: `LocaleManager` + `messages_en.properties` + `messages_es.properties` (BVA + TDD) — PR TBD
4. [not started] Daniel Wu: `Player` + `PlayerColor` (BVA + TDD) — PR TBD
5. [not started] Julian Tang: `Board` (hex generation, terrains, number tokens) (BVA + TDD) — PR TBD
6. [not started] Shun Fujita: `GameSetup` orchestrator + `TurnOrder` + `DevelopmentCardDeck` (BVA + TDD) — PR TBD
7. [not started] All: Integration tests on **Game Setup** and **Locale Selection** features (rubric: ≥2 main features) — PR TBD
8. [not started] All: Code review + merges to `main`

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
