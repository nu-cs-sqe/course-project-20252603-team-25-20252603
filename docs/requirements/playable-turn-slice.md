# Feature: D-Level Playable Turn Slice

This feature extends the completed Game Setup Phase into a minimal playable
CATAN loop for the D rubric.

## Scope

- Start from a valid `Game` produced by `GameSetup`.
- Automatically assign one starting settlement to each player on a non-desert
  hex. This avoids needing the full intersection/road topology before the game
  can be played.
- Track resource inventories for each player.
- Let players roll two six-sided dice.
- Produce resources from owned hexes whose number token matches the dice sum.
- Let the current player build an additional settlement on an unoccupied
  non-desert hex by paying 1 lumber, 1 brick, 1 wool, and 1 grain.
- Track victory points from settlements.
- Let the current player buy a development card by paying 1 ore, 1 wool, and
  1 grain.
- Fully implement all development card types in the simplified play model:
  Knight tracks army size and Largest Army, Victory Point adds 1 point,
  Road Building adds 2 roads to the player's road count, Monopoly transfers
  all of the selected resource from other players, and Year of Plenty grants
  two selected resources.
- End the game when a player reaches 10 victory points.
- Let the current player end their turn and pass play to the next player.

## Deliberate Limits

This slice is intentionally smaller than full CATAN. It does not yet implement:

- Initial settlement/road placement in snake order.
- Vertex/edge topology or legal road connectivity.
- Cities.
- Trading.
- Robber movement after rolling 7.
- Longest Road.

Those remaining items are needed for C/B/A-level product-completion targets.
