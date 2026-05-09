# Feature: Game Setup Phase (CATAN)

This document captures the functional requirements for CATAN's Game Setup Phase
in the form of a User Story (with acceptance criteria) and Use Cases.

> Scope for this iteration: player count validation, player creation
> (name + color), turn order initialization, randomized hex board generation
> (terrains + number tokens), and dev card deck shuffle. **Initial settlement
> and road placement are deferred to a later iteration.**

---

## User Story

As a player of CATAN, I want the game to be properly set up automatically when
a new game starts — including locale selection, player registration, and a
randomized but rules-compliant board — so that I can begin playing without
manually arranging hexes, number tokens, or shuffling decks.

### Acceptance Criteria

- The player can select a locale (English or Spanish) before any other prompt,
  and all subsequent prompts/messages are rendered in that locale.
- Adding a new locale (e.g., `messages_fr.properties`) makes that locale
  available at startup **without modifying existing source code**.
- The game must not start unless there are **3 or 4 players**.
- Each player must have a **non-empty unique name** and a **unique color**
  chosen from {RED, BLUE, WHITE, ORANGE}.
- The board must contain exactly **19 hex tiles** with terrain counts:
  4 FOREST, 4 PASTURE, 4 FIELD, 3 HILLS, 3 MOUNTAIN, 1 DESERT.
- Terrain placement on the 19 hex positions must be **randomized** each game.
- The desert tile receives **no number token**; the other 18 tiles receive
  exactly the standard token multiset:
  `{2, 3, 3, 4, 4, 5, 5, 6, 6, 8, 8, 9, 9, 10, 10, 11, 11, 12}`.
- The robber starts on the **desert** tile.
- The development card deck must contain exactly **25 cards**:
  14 KNIGHT, 5 VICTORY_POINT, 2 ROAD_BUILDING, 2 MONOPOLY, 2 YEAR_OF_PLENTY,
  shuffled at setup completion.
- A **turn order** is established before setup ends; the first turn belongs to
  position 0 of the turn order.

---

## Use Case 1: Start New Game

**Actor**: Player

**Preconditions**:
- The application is launched.

**Main Flow**:
1. System displays a locale selection screen (locales are discovered at runtime
   from `messages_*.properties` files on the classpath).
2. Player selects a locale.
3. System displays all further prompts in the selected locale.
4. Player clicks "Start Game".
5. System asks for the total number of players.
6. Player enters a number of players.
7. For each player, system asks for a name and a color.
8. System generates a randomized 19-hex board satisfying the terrain multiset.
9. System places the standard number-token multiset on the 18 non-desert tiles
   and places the robber on the desert tile.
10. System builds the development card deck (25 cards) and shuffles it.
11. System establishes the turn order (default: in registration order; can be
    randomized by a future enhancement).
12. System transitions to the first player's turn.

**Alternate Flows**:
- **6.a** The number of players is not in {3, 4}: system displays a localized
  error and resumes at Step 5.
- **7.a** A player name is empty or a color is already taken: system displays
  a localized error and re-prompts for that player only.
- **2.a** No locale files are found on the classpath: system fails fast with
  a clear (non-localized) error directing the developer to add a
  `messages_<lang>.properties` file.

**Postconditions**:
- A `Game` object exists holding: a list of 3–4 distinct `Player`s, a `Board`
  with 19 valid tiles and correctly placed number tokens and robber,
  a shuffled `DevelopmentCardDeck` of 25 cards, and a `TurnOrder` whose
  current index is 0.
- The application is ready for the first turn.

---

## Use Case 2: Choose Locale at Startup

**Actor**: Player

**Preconditions**:
- The application is launched.
- At least one `messages_<lang>.properties` resource bundle exists on the
  classpath.

**Main Flow**:
1. System scans the classpath for `messages_*.properties` files and builds
   the list of available locales.
2. System renders a locale selection screen showing each locale by its
   display name (e.g., "English", "Español").
3. Player picks a locale.
4. System sets the active locale and resolves all subsequent UI text via
   `LocaleManager.get(key)`.

**Alternate Flow**:
- **1.a** Only one locale bundle is present: system skips the selection
  screen and uses that locale.

**Postconditions**:
- `LocaleManager.getActiveLocale()` returns the player's choice.
- All future calls to `LocaleManager.get(key)` resolve from the chosen bundle.

---

## Out of Scope (this iteration)

- Initial settlement and road placement in snake order.
- Trading, building, or any in-game actions.
- Persistence (save/load).
- Multiplayer over network.
