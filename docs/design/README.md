# CATAN — Design Overview

This document describes the classes, methods, and relationships for game setup
and the simplified playable slice. Play rules are documented in more detail in
`docs/requirements/playable-turn-slice.md`. Locale architecture:

> The code supports easily adding new locales **without changing existing code**.

---

## 1. Package layout

```
src/main/java
├── domain
│   ├── locale
│   │   └── LocaleManager.java
│   ├── player
│   │   ├── Player.java
│   │   └── PlayerColor.java
│   ├── board
│   │   ├── Board.java
│   │   ├── Hex.java
│   │   ├── TerrainType.java
│   │   └── NumberToken.java
│   ├── deck
│   │   ├── DevelopmentCard.java
│   │   ├── DevelopmentCardType.java
│   │   └── DevelopmentCardDeck.java
│   ├── turn
│   │   └── TurnOrder.java
│   ├── setup
│   │   ├── GameSetup.java
│   │   ├── Game.java
│   │   └── PlayerRegistration.java
│   └── play
│       ├── PlayableGame.java
│       ├── ResourceInventory.java
│       └── ResourceType.java
└── ui
    ├── Main.java
    └── swing
        ├── LocaleSelectionFrame.java
        ├── PlayerSetupFrame.java
        ├── BoardFrame.java
        └── SwingLocaleSupport.java

src/main/resources
├── messages_en.properties
├── messages_es.properties
└── messages_zh.properties
```

The `domain` package contains all game logic. The `ui` package only renders
state and forwards events. The split keeps the rubric's Clean Code goals
(Single Responsibility, dependency direction inward toward `domain`) and lets
us write integration tests without touching Swing.

---

## 2. Locale architecture (rubric requirement)

### Goals
- ≥ 2 locales selectable at game start (we ship English, Spanish, and Mandarin).
- Adding a new locale = drop in `messages_<lang>.properties`. **No source
  changes, no recompilation of existing classes.**

### Mechanism
- We use Java's `ResourceBundle` with a base name `messages`.
- `LocaleManager` discovers available locales **at runtime** by scanning the
  classpath for `messages_*.properties`. There is no hardcoded list of
  supported locales anywhere in code.
- All user-facing strings in `domain` and `ui` are looked up via
  `LocaleManager.get(key)`. No string literals shown to the user appear
  inline in classes other than the resource bundles.

### Public API of `LocaleManager`
```java
public final class LocaleManager {
    public static LocaleManager getInstance();        // process-wide singleton
    public List<Locale> getAvailableLocales();        // discovered at runtime
    public void setActiveLocale(Locale locale);       // throws if not available
    public Locale getActiveLocale();                  // current active locale
    public String get(String key);                    // resolves via active bundle
    public String get(String key, Object... args);    // MessageFormat-style
}
```

### Resource keys used in this iteration (excerpt)
| Key                              | English                        | Spanish                          |
|----------------------------------|--------------------------------|----------------------------------|
| `app.title`                      | CATAN                          | CATAN                            |
| `setup.start`                    | Start Game                     | Empezar partida                  |
| `setup.players.count.prompt`     | Number of players (3–4):       | Número de jugadores (3–4):       |
| `setup.players.count.invalid`    | Player count must be 3 or 4.   | El número de jugadores debe ser 3 o 4. |
| `setup.player.name.prompt`       | Player {0} name:               | Nombre del jugador {0}:          |
| `setup.player.name.empty`        | Name cannot be empty.          | El nombre no puede estar vacío.  |
| `setup.player.color.prompt`      | Player {0} color:              | Color del jugador {0}:           |
| `setup.player.color.taken`       | Color already taken.           | El color ya está en uso.         |
| `setup.ready`                    | Game is ready. {0} starts.     | La partida está lista. Empieza {0}. |

> Adding French later means dropping `messages_fr.properties` with these keys.
> No code in `LocaleManager`, `GameSetup`, or any UI class changes.

---

## 3. Domain classes (public surface)

### `PlayerColor` (enum)
```java
public enum PlayerColor { RED, BLUE, WHITE, ORANGE; }
```

### `Player`
```java
public final class Player {
    public Player(String name, PlayerColor color);
    public String getName();
    public PlayerColor getColor();
    // value-equality on (name, color)
    public boolean equals(Object o);
    public int hashCode();
}
```
- Constructor throws `IllegalArgumentException` if `name == null`,
  `name.trim().isEmpty()`, or `color == null`.

### `TerrainType` (enum)
```java
public enum TerrainType { FOREST, PASTURE, FIELD, HILLS, MOUNTAIN, DESERT; }
```

### `NumberToken`
```java
public final class NumberToken {
    public NumberToken(int value);     // accepts 2..12 except 7
    public int getValue();
}
```
- Constructor throws `IllegalArgumentException` for invalid values.

### `Hex`
```java
public final class Hex {
    public Hex(int position, TerrainType terrain, NumberToken token); // token may be null only when terrain == DESERT
    public int getPosition();          // 0..18
    public TerrainType getTerrain();
    public Optional<NumberToken> getToken();
    public boolean hasRobber();
    public void placeRobber();
    public void removeRobber();
}
```

### `Board`
```java
public final class Board {
    public static Board generateRandom(Random rng);    // factory; deterministic when given a seeded Random
    public List<Hex> getHexes();                       // size 19
    public Hex getDesert();                            // the unique desert hex (where the robber starts)
    public Map<TerrainType, Long> terrainCounts();     // for invariants/tests
}
```
Invariants enforced inside `generateRandom`:
- 19 hexes total.
- Terrain multiset: `{FOREST=4, PASTURE=4, FIELD=4, HILLS=3, MOUNTAIN=3, DESERT=1}`.
- Number tokens placed on the 18 non-desert hexes form the multiset
  `{2,3,3,4,4,5,5,6,6,8,8,9,9,10,10,11,11,12}`.
- Robber starts on the desert hex.

### `DevelopmentCardType` (enum)
```java
public enum DevelopmentCardType {
    KNIGHT, VICTORY_POINT, ROAD_BUILDING, MONOPOLY, YEAR_OF_PLENTY;
}
```

### `DevelopmentCard`
```java
public final class DevelopmentCard {
    public DevelopmentCard(DevelopmentCardType type);
    public DevelopmentCardType getType();
}
```

### `DevelopmentCardDeck`
```java
public final class DevelopmentCardDeck {
    public static DevelopmentCardDeck standardShuffled(Random rng);
    public int size();                                 // 25 at start
    public DevelopmentCard draw();                     // throws if empty
    public Map<DevelopmentCardType, Long> typeCounts();
}
```
Invariant: a fresh standard deck has type counts
`{KNIGHT=14, VICTORY_POINT=5, ROAD_BUILDING=2, MONOPOLY=2, YEAR_OF_PLENTY=2}`.

### `TurnOrder`
```java
public final class TurnOrder {
    public TurnOrder(List<Player> players);   // copy-defensive; throws on null/empty/duplicates
    public Player current();
    public Player advance();                  // returns the new current player
    public int currentIndex();
    public int size();
}
```

### `GameSetup` (orchestrator)
```java
public final class GameSetup {
    public GameSetup(Random rng);                         // injectable RNG for tests
    public void registerPlayers(List<PlayerRegistration> registrations); // validates count + uniqueness
    public Game build();                                  // returns assembled Game
}

public final class PlayerRegistration {
    public PlayerRegistration(String name, PlayerColor color);
    public String name();
    public PlayerColor color();
}

public final class Game {
    public List<Player> players();
    public Board board();
    public DevelopmentCardDeck deck();
    public TurnOrder turnOrder();
}
```

`GameSetup.registerPlayers` rules:
- Throws if `registrations.size()` is not in {3, 4}.
- Throws if any name is null/blank or any color repeats.
- Trims names before construction.

`GameSetup.build` orchestrates:
1. `Board.generateRandom(rng)`
2. `DevelopmentCardDeck.standardShuffled(rng)`
3. `new TurnOrder(players)` (registration order)
4. Returns immutable `Game`.

---

## 4. Relationships

```
GameSetup ─owns─▶ List<Player>
GameSetup ─uses ─▶ Board.generateRandom(Random)
GameSetup ─uses ─▶ DevelopmentCardDeck.standardShuffled(Random)
GameSetup ─builds▶ TurnOrder(List<Player>)
GameSetup ─returns▶ Game(players, board, deck, turnOrder)

Board ─composed of─▶ 19 × Hex
Hex   ─has 0..1 ────▶ NumberToken

LocaleManager ─reads─▶ messages_*.properties (classpath)
ui.swing.* ───────uses────▶ LocaleManager.get(key)
```

No cycles. UI depends on `domain`; `domain` never depends on `ui` or Swing.

---

## 5. Sequence: "Start New Game"

```
Player ▶ Main
Main   ▶ LocaleSelectionFrame.show()
        LocaleSelectionFrame ▶ LocaleManager.getAvailableLocales()
Player ▶ select locale
        LocaleSelectionFrame ▶ LocaleManager.setActiveLocale(...)
Main   ▶ PlayerSetupFrame.show()
        PlayerSetupFrame ▶ LocaleManager.get("setup.players.count.prompt")
Player ▶ enter count + per-player name/color
        PlayerSetupFrame ▶ GameSetup.registerPlayers(...)
        PlayerSetupFrame ▶ Game game = GameSetup.build()
Main   ▶ BoardFrame.show(game)   // playable board UI
```

---

## 6. Testability decisions

- All randomness flows through an injected `java.util.Random`. Tests pass a
  seeded `Random` to make `Board.generateRandom` and
  `DevelopmentCardDeck.standardShuffled` deterministic.
- `LocaleManager` exposes a package-private constructor used by tests to
  point at a custom classpath root, while production code uses the singleton.
- The `Game` returned by `GameSetup.build()` is the entry point for our
  ≥2 integration tests:
  - **IT-1: Game Setup happy path** — 3 players → board + deck + turn order
    invariants all hold.
  - **IT-2: Locale selection end-to-end** — switching locale changes the
    string returned by `LocaleManager.get(key)` for all keys used by setup.

---

## 7. Clean Code commitments (rubric)

- Methods do one thing; no method longer than ~20 lines.
- Names reveal intent: `generateRandom`, `standardShuffled`, `registerPlayers`.
- No magic numbers — terrain/token/deck multisets live in named constants.
- No comments that narrate code; comments only capture non-obvious intent.
- Every public method has a BVA file in `docs/bva/<ClassName>.md`.
