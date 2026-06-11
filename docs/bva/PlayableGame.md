# BVA - `PlayableGame`

`PlayableGame` wraps a completed setup `Game` and adds the playable slice we
implemented: starting hex ownership, dice production, building settlements,
city upgrades, simplified road counts, Longest Road, buying/playing
development cards, Largest Army, and reaching 10 victory points. This is a
simplified CATAN model (hex ownership, simplified roads, not full
roads/ports/trading).

Boundary inputs:
- `Game`: null, valid setup game.
- Dice faces: below 1, 1, 6, above 6.
- Settlement position: below 0, 0, 18, above 18, desert, occupied, unoccupied.
- City position: owned settlement, unowned position, already-upgraded city.
- Road count: below 5, exactly 5, tied with holder, exceeding holder.
- Resource payment: missing one required resource, exactly enough resources.

## Method under test: `start(Game game)`

- **TC1: Null game rejected** ( :white_check_mark: )
    - **State**: `game = null`.
    - **Expected output**: throws `NullPointerException`.

- **TC2: Starting settlements assigned to players** ( :white_check_mark: )
    - **State**: valid 3-player setup game.
    - **Expected output**: each player owns exactly one non-desert hex and has
      1 victory point.

## Method under test: `rollDice(int dieOne, int dieTwo)`

- **TC3: Dice face below one rejected** ( :white_check_mark: )
    - **State**: `dieOne = 0`, `dieTwo = 1`.
    - **Expected output**: throws `IllegalArgumentException`.

- **TC4: Dice face above six rejected** ( :white_check_mark: )
    - **State**: `dieOne = 6`, `dieTwo = 7`.
    - **Expected output**: throws `IllegalArgumentException`.

- **TC5: Matching production roll grants resource** ( :white_check_mark: )
    - **State**: current player owns a non-desert hex whose token matches the
      dice sum.
    - **Expected output**: player's inventory for that terrain resource
      increases by 1 and return value is 1.

- **TC6: Non-matching production roll grants nothing** ( :white_check_mark: )
    - **State**: dice sum does not match any owned hex token.
    - **Expected output**: no inventory changes and return value is 0.

- **TC39: City production grants two resources** ( :white_check_mark: )
    - **State**: current player upgrades an owned settlement to a city and rolls
      that hex's production token.
    - **Expected output**: player's inventory for that terrain resource
      increases by 2 and return value is 2.

## Method under test: `buildSettlement(int position)`

- **TC7: Building on occupied hex rejected** ( :white_check_mark: )
    - **State**: current player chooses a position that already has a
      starting settlement.
    - **Expected output**: throws `IllegalArgumentException`.

- **TC8: Building without exact resources rejected** ( :white_check_mark: )
    - **State**: unoccupied non-desert position, but current player lacks one
      or more of lumber, brick, wool, and grain.
    - **Expected output**: throws `IllegalStateException`.

- **TC9: Building with exact resources succeeds** ( :white_check_mark: )
    - **State**: current player has exactly 1 lumber, 1 brick, 1 wool, and
      1 grain, and chooses an unoccupied non-desert position.
    - **Expected output**: resources are spent, player owns the position, and
      victory points increase by 1.

## Method under test: `buildCity(int position)` / `isCity(int position)`

- **TC39: City upgrade with exact resources succeeds** ( :white_check_mark: )
    - **State**: current player owns a settlement and has exactly 3 ore and
      2 grain.
    - **Expected output**: resources are spent, `isCity(position)` is true,
      victory points increase by 1 beyond the settlement point, and future
      production from that hex is doubled.

- **TC40: Invalid city upgrades rejected** ( :white_check_mark: )
    - **State**: current player chooses an unowned position, or chooses an
      owned settlement that has already been upgraded.
    - **Expected output**: throws `IllegalArgumentException`.

- **TC41: City position boundaries and false state are observable** ( :white_check_mark: )
    - **State**: position is unowned, below 0, or above 18.
    - **Expected output**: unowned valid positions report `false`; out-of-range
      `isCity` and `buildCity` calls throw `IllegalArgumentException`.

- **TC42: City and road actions after win rejected** ( :white_check_mark: )
    - **State**: a player has already reached 10 victory points.
    - **Expected output**: `buildCity` and `buildRoad` throw
      `IllegalStateException` before other validation.

## Method under test: `buildRoad()` / `longestRoadHolder()`

- **TC43: Road with exact resources succeeds** ( :white_check_mark: )
    - **State**: current player has exactly 1 lumber and 1 brick.
    - **Expected output**: resources are spent and road count increases by 1.

- **TC44: Longest Road awarded at exactly five roads once** ( :white_check_mark: )
    - **State**: current player has 4 roads, builds one paid road, then later
      adds more roads while already holding Longest Road.
    - **Expected output**: the 5-road threshold awards Longest Road and its
      2-point bonus, and later roads for the same holder do not re-award it.

- **TC45: Longest Road transferred only when exceeded** ( :white_check_mark: )
    - **State**: one player holds Longest Road; a second player later ties and
      then exceeds that road count.
    - **Expected output**: first player keeps the bonus on a tie and loses it
      only when exceeded.

## Method under test: `endTurn()`

- **TC10: End turn advances to next player** ( :white_check_mark: )
    - **State**: current player is player 0.
    - **Expected output**: current player becomes player 1.

## Method under test: `buyDevelopmentCard()`

- **TC11: Buying without resources rejected** ( :white_check_mark: )
    - **State**: current player has no ore, wool, or grain.
    - **Expected output**: throws `IllegalStateException`.

- **TC12: Victory Point card increases victory points** ( :white_check_mark: )
    - **State**: current player can pay for enough development cards to draw
      until the standard deck yields a `VICTORY_POINT`.
    - **Expected output**: drawing the Victory Point card increases that
      player's victory points by 1.

## Method under test: `applyDevelopmentCard(DevelopmentCard, ResourceType, ResourceType, ResourceType)`

- **TC13: Null development card rejected** ( :white_check_mark: )
    - **State**: `card = null`.
    - **Expected output**: throws `NullPointerException`.

- **TC14: Knight increments army count** ( :white_check_mark: )
    - **State**: current player applies one `KNIGHT`.
    - **Expected output**: that player's knight count increases by 1.

- **TC15: Largest Army awarded at three knights** ( :white_check_mark: )
    - **State**: current player applies three `KNIGHT` cards.
    - **Expected output**: current player is largest army holder and gains
      the 2-point bonus.

- **TC16: Road Building adds two roads** ( :white_check_mark: )
    - **State**: current player applies one `ROAD_BUILDING`.
    - **Expected output**: that player's road count increases by 2.

- **TC17: Monopoly transfers selected resource** ( :white_check_mark: )
    - **State**: other players hold brick; current player applies `MONOPOLY`
      choosing brick.
    - **Expected output**: other players lose all brick and current player
      gains the total.

- **TC18: Monopoly null choice rejected** ( :white_check_mark: )
    - **State**: current player applies `MONOPOLY` with null resource choice.
    - **Expected output**: throws `NullPointerException`.

- **TC19: Year of Plenty grants selected resources** ( :white_check_mark: )
    - **State**: current player applies `YEAR_OF_PLENTY` choosing ore and grain.
    - **Expected output**: current player gains 1 ore and 1 grain.

- **TC20: Year of Plenty null choice rejected** ( :white_check_mark: )
    - **State**: current player applies `YEAR_OF_PLENTY` with one null choice.
    - **Expected output**: throws `NullPointerException`.

## Method under test: `hasWinner()` / `winner()` / `winningPoints()`

- **TC21: New game has no winner** ( :white_check_mark: )
    - **State**: valid game immediately after `start`.
    - **Expected output**: `hasWinner()` is false, `winner()` is empty, and
      `winningPoints()` is 10.

- **TC22: Player wins at ten victory points** ( :white_check_mark: )
    - **State**: current player builds settlements until reaching 10 points.
    - **Expected output**: `hasWinner()` is true and `winner()` returns the
      current player.

- **TC23: Actions after win rejected** ( :white_check_mark: )
    - **State**: a player has already reached 10 victory points.
    - **Expected output**: rolling, building, buying a development card, and
      ending the turn throw `IllegalStateException`.

## Method under test: `inventory(Player)` / `victoryPoints(Player)` /
`ownedHexes(Player)` / `ownerOf(int)`

- **TC24: Unknown player rejected** ( :white_check_mark: )
    - **State**: player not in this game.
    - **Expected output**: throws `IllegalArgumentException`.

- **TC25: Position outside board rejected** ( :white_check_mark: )
    - **State**: `position = -1` or `position = 19`.
    - **Expected output**: throws `IllegalArgumentException`.


## Mutation-targeted boundaries

The following boundary cases harden tests against PIT survivors so each
non-equivalent mutant is killed.

- **TC30: Owned hexes returned in ascending board-position order** ( :white_check_mark: )
    - **State**: the same player owns hexes whose insertion order into the
      underlying owner map produces a non-monotonic iteration order (a
      starting settlement at a low position, then a high non-desert position
      built first, then a small non-desert position built second).
    - **Expected output**: `ownedHexes(player)` returns positions in strictly
      ascending order - the result is sorted, not in insertion order, and the
      comparator returns the signed difference (so the sort actually reorders).

- **TC31: Build after win rejected even with valid resources and position** ( :white_check_mark: )
    - **State**: a player has already reached 10 victory points; the same
      player has lumber, brick, wool, and grain; an unowned non-desert
      position exists.
    - **Expected output**: `buildSettlement(position)` throws
      `IllegalStateException` (the game-over guard, not the cost guard, rejects).

- **TC32: Buy development card after win rejected even with funds** ( :white_check_mark: )
    - **State**: a player has already reached 10 victory points; the same
      player has ore, wool, and grain.
    - **Expected output**: `buyDevelopmentCard()` throws
      `IllegalStateException` (the game-over guard, not the cost guard, rejects).

- **TC33: Dice values exactly at both bounds accepted** ( :white_check_mark: )
    - **State**: `dieOne = 1, dieTwo = 1` and `dieOne = 6, dieTwo = 6`.
    - **Expected output**: both calls return a valid production count and do
      not throw (the `< 1` and `> 6` checks are strict, not `<= 1` or `>= 6`).

- **TC34: Position outside board rejected from `buildSettlement`** ( :white_check_mark: )
    - **State**: `position = -1` or `position = 19`.
    - **Expected output**: `buildSettlement(position)` throws
      `IllegalArgumentException` (validation happens inside `hexAt`, before
      indexing the hex list).

- **TC35: Building on desert rejected before resources are spent** ( :white_check_mark: )
    - **State**: current player has exact settlement resources and chooses the
      desert position.
    - **Expected output**: `buildSettlement(position)` throws
      `IllegalArgumentException`, and the player's resources are unchanged.

- **TC36: Robber blocks production on an owned hex** ( :white_check_mark: )
    - **State**: current player's owned non-desert hex has the robber, and the
      dice sum matches that hex's token.
    - **Expected output**: `rollDice` returns 0 and grants no resource.

- **TC37: Desert without robber still produces nothing** ( :white_check_mark: )
    - **State**: desert has no robber and dice are rolled.
    - **Expected output**: `rollDice` returns 0 because the desert has no
      production token/resource.

- **TC38: Largest Army holder keeps bonus when playing an additional knight** ( :white_check_mark: )
    - **State**: current player already holds Largest Army with three knights.
    - **Expected output**: playing a fourth knight increases knight count but
      does not re-award or remove the 2-point Largest Army bonus.

- **Equivalent mutant: Monopoly `amount > 0` vs `amount >= 0`** ( documented in `docs/testing/mutation-coverage.md` )
    - **State**: opponent inventory holds zero of the selected resource.
    - **Notes**: Mutating `if (amount > 0)` to `if (amount >= 0)` enters the
      `spend` branch with a zero amount and adds zero to the collector. Because
      `ResourceInventory.spend({resource: 0})` and `collected += 0` are both
      no-ops, the mutant is observationally indistinguishable from the
      original — recorded as equivalent.

## Integration boundaries (PlayableGameIntegrationTest)

These cases exercise the composed flow `GameSetup` → `Game` → `PlayableGame`
end-to-end with no direct construction of intermediate state. Inputs are the
seeded random source used by `GameSetup`, the player registration list, and the
sequence of public `PlayableGame` calls. The boundary classes below partition
the integration surface; one positive case and one negative case are taken per
class where applicable.

- **IT1: Production roll distributes resources across the full setup → playable
  composition** ( :white_check_mark: )
    - **State**: 3 players registered against a `GameSetup(new Random(42))`;
      `PlayableGame.start` invoked on the built game; dice rolled to the token
      value of a hex owned by the current player at startup.
    - **Expected output**: the current player's inventory gains exactly 1 unit
      of the terrain's mapped `ResourceType`; players who do not own a matching
      hex gain nothing.

- **IT2: Settlement build consumes the exact cost and increments victory
  points** ( :white_check_mark: )
    - **State**: current player has exactly one of each of LUMBER, BRICK, WOOL,
      and GRAIN (the canonical settlement cost); an unoccupied non-desert
      position exists on the seeded board.
    - **Expected output**: after the build, inventory totals drop to zero,
      `ownerOf(position)` returns the current player, and `victoryPoints`
      increases by 1 from the starting value of 1.

- **IT3: Turn order cycles through every registered player and wraps** ( :white_check_mark: )
    - **State**: N registered players, `endTurn` invoked N times.
    - **Expected output**: after N invocations the current player equals
      `players().get(0)`; the intermediate sequence matches the registration
      order exactly.

- **IT4: Victory at ten points is reached through legal builds and locks the
  game** ( :white_check_mark: )
    - **State**: current player drives victory points to 10 through repeated
      settlement builds (with the canonical cost supplied per build).
    - **Expected output**: `hasWinner()` is true, `winner()` returns the current
      player, and `winningPoints()` is 10.

- **IT5: Every state-changing action is rejected once the game has a winner**
  ( :white_check_mark: )
    - **State**: a player has already reached 10 victory points.
    - **Expected output**: `rollDice(1,1)`, `buildSettlement(...)`,
      `buyDevelopmentCard()`, and `endTurn()` all throw
      `IllegalStateException` with no observable state change.
