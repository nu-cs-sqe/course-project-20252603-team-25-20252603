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

## Local Git hooks (optional)
To keep commit messages free of automatic `Co-authored-by: Cursor ...` trailers
from the IDE, run once per clone:

`git config core.hooksPath .githooks`

## Acknowledgements
- [Official CATAN rules](https://www.catan.com/game-rules)
- COMP_SCI 380 lectures, labs, and the Chess reference project
- Robert C. Martin, *Clean Code* (Week 7 review feedback)
- JUnit 5 and Gradle docs
