# BVA - `PlayableGame`

`PlayableGame` wraps the completed setup `Game` with the smallest playable
CATAN slice needed for the D rubric: starting settlements, dice production,
settlement building, victory-point tracking, and turn advancement.

Boundary inputs:
- `Game`: null, valid setup game.
- Dice faces: below 1, 1, 6, above 6.
- Settlement position: below 0, 0, 18, above 18, desert, occupied, unoccupied.
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

## Method under test: `inventory(Player)` / `victoryPoints(Player)` /
`ownedHexes(Player)` / `ownerOf(int)`

- **TC13: Unknown player rejected** ( :white_check_mark: )
    - **State**: player not in this game.
    - **Expected output**: throws `IllegalArgumentException`.

- **TC14: Position outside board rejected** ( :white_check_mark: )
    - **State**: `position = -1` or `position = 19`.
    - **Expected output**: throws `IllegalArgumentException`.
