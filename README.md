![Gradle Build](https://github.com/nu-cs-sqe/course-project-20252603-team-25-20252603/actions/workflows/main.yml/badge.svg)
# CATAN

A simplified, playable CATAN implementation for COMP_SCI 380 (Software Quality
Engineering). The game runs as a Java 11 Swing desktop app with setup, board
play, development cards, and a 10-point win condition. See
[`docs/requirements/`](docs/requirements/) for scope details.

## Contributors
- Daniel Wu
- Daniel Kim
- Julian Tang
- Shun Fujita

## Task board
Open work: [GitHub Issues](https://github.com/nu-cs-sqe/course-project-20252603-team-25-20252603/issues) and [`docs/project-board.md`](docs/project-board.md).

## Dependencies
- JDK 11
- JUnit 5.10
- Gradle 8.10

## Running the app
```sh
./gradlew run
```

## Running tests
- `./gradlew test` — runs the full suite (unit + integration tests).
- `./gradlew integrationTest` — runs only tests tagged with `@Tag("integration")`
  (everything under `src/test/java/integration/`).
- `./gradlew clean check` — full quality gate (tests, Checkstyle, SpotBugs, Jacoco, PIT).

## Grading notes
- **Gameplay scope:** this is a simplified playable CATAN model. It keeps the
  standard 19-hex board, standard terrain/token/development-card setup, city
  upgrades, development-card effects, Largest Army, Longest Road, and the
  10-point win condition. It deliberately models ownership at the hex level
  and roads as a simplified road count, so it does not implement vertex/edge
  topology, snake-order initial placement, trading, ports, or robber movement
  after rolling 7. See [`docs/requirements/playable-turn-slice.md`](docs/requirements/playable-turn-slice.md).
- **Coverage and mutation exception:** Jacoco reports 100% filtered instruction
  and branch coverage with 0 missed complexity for non-GUI, non-enum domain
  code. PIT reports one surviving equivalent mutant in
  `PlayableGame.applyMonopoly`: changing `amount > 0` to `amount >= 0` is
  observationally identical because spending and collecting zero resources are
  both no-ops. See [`docs/testing/mutation-coverage.md`](docs/testing/mutation-coverage.md).
- **BVA/TDD evidence:** boundary-value cases are documented under
  [`docs/bva/`](docs/bva/), with the playable game rules covered in
  [`docs/bva/PlayableGame.md`](docs/bva/PlayableGame.md). Process, weekly
  planning, Clean Code review, and i18n evidence are documented under
  [`docs/`](docs/).

## Acknowledgements
- [Official CATAN rules](https://www.catan.com/game-rules)
- COMP_SCI 380 lectures, labs, and the Chess reference project
- Robert C. Martin, *Clean Code* (Week 7 review feedback)
- JUnit 5 and Gradle docs
