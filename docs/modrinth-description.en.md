# Vindicator's Slash Blade

**Give your mobs a real SlashBlade.** Vindicator's Slash Blade teaches hostile mobs to actually fight with SlashBlade: Resharped — full combo chains, slash arts, aerial juggles and step tricks — while keeping them from slaughtering their own allies.

In vanilla SlashBlade, only the player can use a blade. Mobs can hold one (via datapack or commands), but they just swing it like a stick: no combos, no arts, no animations. This mod fills that gap. It hooks the SlashBlade combat system into mob AI, so an illager with a katana becomes an actual duelling opponent instead of a reskinned zombie.

---

## Features

### Any mob can be a blader

A config whitelist decides which entity types fight with a blade, and at what level:

```toml
bladerWhitelist = ["minecraft:vindicator, 5", "minecraft:pillager, 2"]
```

**Effect on gameplay:** you are not locked into one enemy type. You can turn a single vindicator into a mini-boss, make an entire pillager raid blade-wielding, or add mobs from other mods — no datapack or code required. Any entity that has a main hand and a pathfinder AI can be enrolled.

### Six blader levels

Every whitelist entry takes a level from 1 to 6. The level decides which techniques the mob is allowed to use and how fast it attacks:

| Level | Attack cooldown | Rapid Slash | Upper Slash Jump | Slash Arts | Just Slash Arts | Void Slash | Air Trick / Trick Down / Trick Dodge | Powerful | Mirage Blade |
|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|
| **1** | 5 ticks | – | – | – | – | – | – | – | – |
| **2** | 4 ticks | – | – | – | – | – | – | ✔ | – |
| **3** | 3 ticks | ✔ | ✔ | – | – | – | – | ✔ | – |
| **4** | 2 ticks | ✔ | ✔ | ✔ | – | ✔ | ✔ | ✔ | – |
| **5** | 2 ticks | ✔ | ✔ | ✔ | ✔ | ✔ | ✔ | ✔ | – |
| **6** | 2 ticks | ✔ | ✔ | ✔ | ✔ | ✔ | ✔ | ✔ | ✔ |

`Powerful` makes the mob interrupt its own combo more easily and shortens its Just Slash cooldown, so it feels relentless rather than scripted.

**Effect on gameplay:** difficulty is now a dial instead of a switch. A level 1 blader is a novelty; a level 5 blader is a genuine threat that chains aerial combos and vanishes with step tricks, and a level 6 blader adds a ring of spinning blades and homing projectiles on top. You can put the same mob type at different levels in different structures by editing one line.

### Mirage blade (level 6)

Level 6 bladers unlock the summoned-sword system: **a spiral sword that rotates around them and homing blades that track their target**, giving them ranged pressure that runs in parallel with melee combos. Summoned swords respect friendly-fire protection too — they never trigger damage against the blader's own allies.

### Born with a blade

Mobs on the `bornWithBladeWhitelist` spawn already holding a random blade — picked from **every SlashBlade registered in your pack**, so modded and datapack blades are included automatically.

```toml
bornWithBladeWhitelist = ["minecraft:vindicator"]
bornWithBladeChance = 1.0
bladeDropChance = 0.0
```

**Effect on gameplay:** no more setting up commands or loot tables to arm your enemies. Because the blade is rolled from the registry, the same mob can show up with a different weapon each time, which keeps encounters varied. Drop chance defaults to **0**, so bladers never hand out free endgame katanas — you can raise it if you want them to be a loot source.

### Friendly-fire protection

Area-of-effect slash attacks no longer shred the attacker's own side:

- a blader never damages itself;
- a blader never damages entities it is allied to (same team, same raid);
- a blader never damages players **unless** it is a hostile mob or is already targeting a player;
- summoned swords (level 6) respect the same protection and never trigger damage against allies.

**Effect on gameplay:** this is the feature that makes the mod playable rather than chaotic. Pillager raids keep their internal cohesion, villagers and iron golems can be given blades without wiping out the village they defend, and a patrolling ally will not clip you with a sweeping slash while you fight next to it.

### Blade reach instead of vanilla melee range

Melee range checks for blade-holding mobs use SlashBlade's own resolved reach instead of the vanilla 3-block box.

**Effect on gameplay:** what you see is what hits you. The mob starts its combo at the distance its animation suggests, so fights stop feeling like the enemy is swinging at air from two blocks away.

### Full combat animations on vanilla models

Blade combat animations are driven through playerAnimator and converted to vanilla model parts, with dedicated support for:

- **humanoid models** (zombies, skeletons, players-shaped mobs),
- **illager models** (vindicators, pillagers, evokers, illusioners),
- **villager models**.

Body rotation is applied to the render pass so spins and turns read correctly.

**Effect on gameplay:** you can tell at a glance what a blader is doing — wind-up, combo step, aerial slash — instead of guessing from a static pose. It is the difference between "a mob with a sword texture" and "a mob that fights".

### Mobs don't wear out blades

Blade durability is skipped when a non-player entity lands a hit.

**Effect on gameplay:** blades held by mobs last as long as the mob does. Nothing silently downgrades mid-fight, and a blader you equipped by hand will not break its weapon after twenty hits.

### Step-trick effects

Bladers emit ender particles when they perform step tricks, and the whole trick system (air trick, trick down, trick dodge) is available at level 5. Toggle with `showEnderParticles`.

**Effect on gameplay:** mobility abilities become readable. You can see a blader blink out and reposition instead of it looking like a teleporting bug.

---

## Configuration

All options live in `config/vindicators_slash_blade-common.toml` and are **server-side** — change them without touching the client.

| Option | Type | Default | What it does |
|---|---|---|---|
| `bladerWhitelist` | list of `"entity_id, level"` | `["minecraft:vindicator, 5"]` | Which mobs fight with a blade, and at which level (1–5) |
| `bornWithBladeWhitelist` | list of `entity_id` | `["minecraft:vindicator"]` | Which mobs spawn already holding a random blade |
| `bornWithBladeChance` | 0.0 – 1.0 | `1.0` | Chance that a born-with-blade mob actually gets one |
| `bladeDropChance` | 0.0 – 1.0 | `0.0` | Chance a blader drops its blade on death |
| `showEnderParticles` | boolean | `true` | Ender particles during step tricks |
| `usePlayerAnimator` | boolean | `true` | Play blade animations via playerAnimator (disable for low-end machines) |

Ships with a deliberately minimal default: **vindicators only**. That keeps the mod close to its name out of the box, and lets you build up from there.

---

## Requirements

| Mod | Version |
|---|---|
| Minecraft | 1.21.1 |
| NeoForge | 21.1.228 or newer |
| SlashBlade: Resharped | 2.1.9-1.21.1 or newer |
| SBR Core | 1.5.2-hotfix1 or newer |
| playerAnimator | 2.0.4+1.21.1 or newer |

Install on **both client and server**. The AI and damage logic run on the server; the animation and model mixins run on the client. A client-only install will work but you will lose all blade animations, and a server-only install will leave you fighting invisible combo steps.

---

## Notes

- This mod adds **no items, no blocks and no worldgen**. It is pure behaviour, so it is safe to add to an existing world.
- Blades are picked from the SlashBlade registry at spawn time. If your pack has zero registered blades, born-with-blade mobs simply spawn empty-handed instead of crashing.
- Mobs are allowed to chain any combo sequence; there is no per-level combo whitelist yet.
- Removing the mod leaves already-equipped mobs holding ordinary SlashBlades — they will still swing them, just without combos.

---

## Credits

- **SlashBlade: Resharped** — flammpfeil and contributors, for the combat system this mod drives.
- **SBR Core** — mrqx, for `SimpleSlashGoal` and the mob-side attack utilities.
- **playerAnimator** — KosmX, for the animation layer.

Licensed under MIT. Issues and suggestions are welcome on the issue tracker.
