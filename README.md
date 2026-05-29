![Gradle Build](https://github.com/nu-cs-sqe/course-project-20252603-team-25-20252603/actions/workflows/main.yml/badge.svg)
# CATAN

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

## Running tests
- `./gradlew test` — runs the full suite (unit + integration tests).
- `./gradlew integrationTest` — runs only tests tagged with `@Tag("integration")`
  (everything under `src/test/java/integration/`).

## Local Git hooks (optional)
To keep commit messages free of automatic `Co-authored-by: Cursor ...` trailers
from the IDE, run once per clone:

`git config core.hooksPath .githooks`

## Acknowledgements
REFERENCES, SOURCE OF HELP ETC
