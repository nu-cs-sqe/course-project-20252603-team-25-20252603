# Feature: Simplified Playable Turn Slice

This feature extends the completed Game Setup Phase into the current playable
CATAN loop used by the product-completion rubric.

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
- Let the current player upgrade an owned settlement to a city by paying
  3 ore and 2 grain. Cities add 1 victory point beyond the settlement point
  and produce 2 resources on matching production rolls.
- Let the current player build a simplified road by paying 1 lumber and
  1 brick.
- Track victory points from settlements, cities, Largest Army, Longest Road,
  and Victory Point development cards.
- Award Longest Road when a player reaches at least 5 roads. The bonus remains
  with the current holder on ties and transfers only when another player
  exceeds the holder's road count.
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
- Trading.
- Robber movement after rolling 7.

Those remaining items should be described as model simplifications during
grading; the implemented win condition can still be satisfied on the reduced
board.
