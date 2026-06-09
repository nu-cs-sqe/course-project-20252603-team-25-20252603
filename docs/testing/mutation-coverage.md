# Domain Mutation and Coverage Evidence

## Scope

The Gradle verification tasks now report Jacoco and PIT results for production
domain code only. UI code and enum-only types are excluded so the reports match
the grading rubric's focus on non-GUI and non-enum logic.

Included PIT target classes:

- `domain.*`

Excluded PIT classes:

- `domain.board.TerrainType`
- `domain.deck.DevelopmentCardType`
- `domain.play.ResourceType`
- `domain.player.PlayerColor`

Jacoco applies the same practical filter by excluding `ui/**`, `**/*Type.class`,
and `**/PlayerColor.class`.

## Commands

Run the full quality gate:

```sh
./gradlew clean check
```

Run integration tests separately:

```sh
./gradlew integrationTest
```

Generated reports:

- Jacoco: `build/reports/jacoco/index.html`
- PIT: `build/reports/pitest/index.html`
- Unit tests: `build/reports/tests/test/index.html`
- Integration tests: `build/reports/tests/integrationTest/index.html`
- Checkstyle: `build/reports/checkstyle/main.html` and `build/reports/checkstyle/test.html`
- SpotBugs: `build/reports/spotbugs/spotbugsMain.html` and `build/reports/spotbugs/spotbugsTest.html`

## Current Results

Last verified on June 9, 2026 with `./gradlew clean check`:

- Build result: passing
- PIT line coverage for mutated classes: 515 / 525, 98%
- PIT mutation score: 241 / 251 killed, 96%
- PIT no-coverage mutations: 1
- Jacoco instruction coverage: 98%
- Jacoco branch coverage: 94%
- Jacoco missed complexity: 11 of 227

Last verified on June 9, 2026 with `./gradlew integrationTest`:

- Build result: passing

## Test Improvements In This Branch

Focused tests were added for these mutation and coverage gaps:

- Largest Army award and transfer behavior in `PlayableGame`.
- Game query guards for unknown players.
- Board boundary queries and owned-hex ordering.
- Monopoly no-op behavior when opponents have none of the selected resource.
- `ResourceInventory` negative, null-key, and null-amount cost validation.
- `ResourceInventory.snapshot()` contents and immutability.
- Locale bundle discovery edge cases, region tags, and formatting with no args.
- Value-object equality and hash-code behavior for domain objects.

## Remaining Gaps

This branch documents and improves mutation testing, but it does not yet meet the
rubric's B/A target of 100% killed non-equivalent mutants and 100% cyclomatic
coverage.

Remaining PIT survivors are concentrated in:

- `PlayableGame`: game-over guard removal mutations, owned-hex sort mutation,
  dice boundary mutation, position validation through private `hexAt`, and a
  Monopoly boundary mutation.
- `ResourceInventory`: zero-cost boundary mutation in `validateCost`.
- `LocaleManager`: one no-coverage mutation in classpath bundle discovery for
  a no-bundle classpath path.

Remaining Jacoco missed complexity is concentrated in:

- `domain.play`
- `domain.locale`

The next branch should either add tests for these behaviors or mark any
equivalent mutants with a written justification after review.
