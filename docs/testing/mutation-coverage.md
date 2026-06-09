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
- PIT line coverage for mutated classes: 526 / 527, 99%
- PIT mutation score: 254 / 255 killed, 99%
- PIT mutation test strength (killed of covered): 254 / 255, 99%
- PIT no-coverage mutations: 0
- Equivalent mutants documented and excluded: 1 (see below)
- Effective non-equivalent mutation score: 254 / 254, 100%
- Jacoco instruction coverage: 100%
- Jacoco branch coverage: 98%
- Jacoco missed complexity: 0 of 230

Last verified on June 9, 2026 with `./gradlew integrationTest`:

- Build result: passing

## Test Improvements In This Branch

Targeted boundary tests were added so every non-equivalent mutant is killed.
Each test is anchored to a row in the corresponding `docs/bva/*.md` boundary
table.

- `PlayableGame` game-over guard: `tc31` and `tc32` set up the post-win state
  with sufficient resources/funds and assert that `buildSettlement` and
  `buyDevelopmentCard` still throw `IllegalStateException` — removing the
  guard would let either call succeed.
- `PlayableGame.ownedHexes` sort and comparator: `tc30` builds at position 18
  first (its bucket collides with a starting hex in the underlying owner map)
  then at a smaller position, forcing the raw HashMap iteration order to
  differ from the sorted order. The strict-ascending assertion kills both the
  removed-`List.sort` mutant and the comparator-returns-0 mutant.
- `PlayableGame.endTurn` return value: `tc10` now captures the returned
  `Player` and asserts equality, killing the "replace return with null" mutant.
- `PlayableGame.validateDie` strict bounds: `tc33` asserts `rollDice(1, 1)`
  and `rollDice(6, 6)` do not throw, killing the surviving conditional-
  boundary mutant on the `<`/`>` checks.
- `PlayableGame.hexAt` position guard: `tc34` calls `buildSettlement(-1)` and
  `buildSettlement(19)` with funded inventory and asserts
  `IllegalArgumentException`; removing the `validatePosition` call would
  surface `IndexOutOfBoundsException` from `List.get(int)` instead.
- `ResourceInventory.validateCost` strict bound: `tc13` asserts
  `spend({LUMBER: 0})` does not throw and leaves the count unchanged,
  killing the `< 0` → `<= 0` conditional-boundary mutant.
- `LocaleManager.containsAnyBundle` non-directory branch: `tc20` exercises
  the package-private helper directly with a regular file path and asserts
  `false`, killing the previously uncovered "replace boolean return with
  true" mutant. `tc21` covers the matching-directory branch as a
  control test.

## Equivalent Mutants

PIT reports a single surviving mutant after this branch:

- `PlayableGame.applyMonopoly` — `if (amount > 0)` → `if (amount >= 0)`
  (`changed conditional boundary`).

This mutant is **equivalent** to the original. The block guarded by the
condition is:

```java
if (amount > 0) {
    inventory.spend(Collections.singletonMap(resource, amount));
    collected += amount;
}
```

When `amount` is exactly zero (the only input that distinguishes the original
from the mutant) both observable effects are no-ops:

1. `ResourceInventory.spend(Map.of(resource, 0))` is a valid no-op — its
   cost-validation guard is `value < 0` (strict), `canAfford` is trivially
   true for zero, and the subtract loop reduces every count by zero. This is
   the same behavior verified by the new `ResourceInventoryTest.tc13`.
2. `collected += 0` does not change the running total.

No observer of `applyMonopoly` (resource counts of the current player, of
opponents, or the returned card) can distinguish "entered the block with
zero" from "skipped the block". Per the COMP_SCI 380 grading rubric, which
permits an equivalent-mutant exclusion when justified in writing, this
mutant is recorded here and excluded from the effective mutation score.

## Remaining Gaps

None for the B-rubric mutation and cyclomatic coverage targets: every
non-equivalent mutant is killed, the one equivalent mutant is documented above,
and Jacoco reports 0 missed complexity for the filtered non-GUI/non-enum domain
scope.

Jacoco still reports 3 missed branches in `domain.play`; these do not count as
missed complexity. They come from exhaustive enum-switch and domain-invariant
control-flow details that are not meaningful user-visible scenarios under the
current model.
