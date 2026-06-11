# Domain Mutation and Coverage

Jacoco and PIT run on **domain code only**. Swing UI and simple enums are
excluded because the course rubric focuses mutation and cyclomatic coverage on
non-GUI, non-enum logic.

**PIT targets:** `domain.*`

**Excluded from PIT:** `TerrainType`, `DevelopmentCardType`, `ResourceType`,
`PlayerColor`

**Jacoco filter:** excludes `ui/**`, `**/*Type.class`, `**/PlayerColor.class`

## Commands

```sh
./gradlew clean check      # tests + Checkstyle + SpotBugs + Jacoco + PIT
./gradlew integrationTest  # @Tag("integration") tests only
```

Reports: `build/reports/jacoco/`, `build/reports/pitest/`,
`build/reports/tests/test/`, `build/reports/tests/integrationTest/`.

## Latest numbers (June 10, 2026, `./gradlew clean check`)

| Metric | Result |
|--------|--------|
| Build | passing |
| PIT mutations killed | 254 / 255 (99%) |
| Effective score (non-equivalent) | **254 / 254 (100%)** |
| Jacoco instruction coverage (filtered) | 100% |
| Jacoco missed complexity (filtered) | 0 / 230 |
| Integration tests | passing |

## Equivalent mutant (excluded from effective score)

PIT leaves one survivor in `PlayableGame.applyMonopoly`:

- Condition change: `amount > 0` → `amount >= 0`

When `amount == 0`, both versions behave the same: `spend` with zero is a
no-op and `collected += 0` changes nothing. We document this here per the
course rubric allowance for equivalent mutants.

## How we got to 100%

We added targeted tests tied to BVA rows in `docs/bva/` — mainly around game-over
guards, sorted hex ownership, die bounds, inventory zero-spend, and
`LocaleManager` bundle discovery edge cases. See `PlayableGameTest`,
`ResourceInventoryTest`, and `LocaleManagerTest` for examples.
