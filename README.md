# ConsoleMaze

A roguelike-style maze game written in pure Java (Swing, no external libraries). Every level is generated procedurally: rooms, corridors, locked doors with matching keys, enemies with different behaviours, and interactive props.

> Pet project. Built to practice layered architecture, procedural generation and clean OOP in plain Java.

<img width="707" height="667" alt="image" src="https://github.com/user-attachments/assets/bde21bd5-b7a1-4ab4-8234-a1d16d8a1391" />

## Features

- **Procedural levels**: random rooms joined by one-tile corridors, a new map on every run or restart.
- **Colored keys and doors**: up to 7 pairs. A door blocks a corridor until you find the key of its color.
- **Three enemy types**, each with its own AI and health.
- **Items**: coins, health potions (can be used at full health too).
- **Pushable boulders** that can stand in the way of traps.
- **Destructible vegetation**: the player breaks it by walking through it; it also slows monkeys down.
- **Camera with zoom** (1.5x by default) that follows the player; the HUD is drawn separately and is not scaled.
- Hit feedback: a short invulnerability window with a blinking player.

## Requirements

- Requires JDK 21 or newer and Maven.

## Build and run

```bash
# from the project root, assuming sources are in src/
mvn clean compile
java -cp target/classes consolemaze.Main
```

Or open the project in any IDE (IntelliJ IDEA, Eclipse) and run `consolemaze.Main`.

## Controls

| Key | Action |
|-----|--------|
| `W` `A` `S` `D` / arrow keys | Move (hold two keys for diagonal movement) |
| `R` | Restart the level (on the end screen) |
| `Esc` | Exit the game (on the end screen) |

**Goal:** reach the exit `▣`. Collect coins on the way and avoid dying.

## Game objects

| Symbol | Object | Behaviour |
|:------:|--------|-----------|
| `■` | Wall | Impassable |
| `.` | Floor | Walkable |
| `▣` | Exit | Reach it to win |
| `⦿` | Coin | Collected on contact |
| `⚿` | Key | Opens the door of the same color |
| `☲` / `▯` | Door (closed / open) | Needs the matching key |
| `♥` | Health potion | Restores 4 HP |
| `●` | Boulder | Can be pushed, but only inside its home room |
| `ψ` | Vegetation | Broken by walking into it |
| `Ω` | Ghost | Chases the player through walls, 1 damage, 1 HP |
| `♣` | Monkey | Patrols a fixed route, 2 damage, 2 HP, breaks vegetation |
| `▦` | Trap | Sweeps along a room wall, 3 damage, 3 HP |

The player starts with 10 HP.

## Architecture

The project is split into four layers. Dependencies point downward only: the model knows nothing about the engine, the engine knows nothing about the UI.

```
consolemaze
├── model              Data and simple object behaviour
│   ├── GameObject, MovableEntity, Player, Enemy (Ghost, Monkey, Trap)
│   └── Tile, Wall, Door, Key, Coin, Exit, HealthPotion, Boulder, Vegetation, Maze, Room, Position
├── engine             Game rules and the tick-based loop
│   ├── MazeEngine, PlayerMovementHandler, InteractionHandler, InteractionRule, LevelData
│   └── generator      Level generation
│       └── MazeGenerator, BspMazeGenerator, EntitySpawner, EnemySpawner, PropSpawner, GeneratorConfig
└── ui                 Swing rendering and input
    └── GameWindow, GamePanel, InputHandler, Camera, WorldRenderer, HudRenderer, OverlayRenderer
```

### Design notes

- **Template Method**: `Enemy.tick()` is final and handles timing; each subclass implements only its own `step()`.
- **Registry of interaction rules**: when the player walks into a "trigger" cell, `MazeEngine` looks up an `InteractionRule` by the target's class. Adding a new interactive object means registering one rule instead of extending a chain of `if` statements.
- **Strategy**: level generators implement `MapGenerator`; `MazeGenerator` picks one of the registered algorithms.
- **Player and enemies live outside the map grid.** The `Maze` stores only static objects, and the UI draws moving entities on top. This removed a whole class of "restore what was under the entity" bugs.
- **Immutable value objects** (`Position`, `Collision`) and a clear invariant: for objects stored in the grid, `getPosition()` always equals their cell coordinates.
- **Rendering is split into small renderers** (world, HUD, overlays) behind a thin `GamePanel`; colors live only in `ColorPalette`, so the model has no dependency on `java.awt`.
- **Game loop on the Swing EDT**: a `javax.swing.Timer` drives the tick, so input, logic and painting never run concurrently and no synchronization is needed.

### Level generation pipeline

1. Fill the map with walls.
2. Place random non-overlapping rooms.
3. Build a room graph (a chain that guarantees connectivity plus extra links to nearby rooms).
4. Carve rooms and L-shaped corridors.
5. Place the player, the exit, doors with keys, and coins.
6. Place enemies: traps at room entrances, monkeys on BFS-computed patrol routes, ghosts far from the player.
7. Place potions, boulders and vegetation clusters.

All counts and sizes are in `GeneratorConfig`, for example `vegetationClusters` or `monkeyCount`.

## Configuration

| What | Where |
|------|-------|
| Number of keys, enemies, coins, potions, boulders, plants | `GeneratorConfig` |
| Map size | `GameWindow` (`MAP_WIDTH`, `MAP_HEIGHT`) |
| Zoom, field size, tick speed, fonts | `UiConfig` |
| Colors | `ColorPalette` |
| Enemy damage and speed | constructors of `Ghost`, `Monkey`, `Trap` |
