# Better Mining Lamp

## Scope

Better Mining Lamp adds one always-on copper utility cap for Better Content:
`better_mining_lamp:better_mining_lamp`. It has 165 durability, one armor point when worn in the vanilla
head slot, and repairs with copper ingots. Its survival recipe uses one lantern and five copper
ingots.

The same stack can occupy the Curios `head` slot. In that slot it grants no armor, renders only
the lamp and bracket over any ordinary helmet, and loses `max(1, floor(final_damage / 4))`
durability on successful damage that does not bypass armor. A player cannot equip duplicate
Better Mining Lamps across armor and Curios slots.

Sodium/Embeddium Dynamic Lights is optional. When present, the helmet emits luminance 15 while
held, dropped, worn as armor, or worn as a head Curio; its light is water-sensitive. There is no
toggle, fuel, key binding, menu, or hidden statistic.

## Bounded playtest diagnostics

Loader SLF4J `bc.playtest.better_mining_lamp.*` reports one INFO startup/provider/source status
(UNKNOWN when own metadata is absent), DEBUG actual Curios damage processing and optional native
light-handler registration/absence. Requested wear is distinguished from observed stack damage
and breakage; native Unbreaking/RNG semantics are unchanged. Registration is not visible-light
proof. Luminance queries, render frames, inventories and item tags are never logged or scanned.
One mod-wide 120/minute burst20 detail bucket, 256 aggregate keys, 2048-character record limit
and 30-second summaries bound output. Client disconnect/server stop clears keys without bucket
refill. Standalone stays INFO; the pack selects scoped DEBUG.

## Stable visual asset contract

- Item model: `assets/better_mining_lamp/models/item/better_mining_lamp.json`
- Item texture: `assets/better_mining_lamp/textures/item/better_mining_lamp.png`
- Item emissive mask: `assets/better_mining_lamp/textures/item/better_mining_lamp_emissive.png`
- Armor layer/vanilla helmet UV: `assets/better_mining_lamp/textures/models/armor/better_mining_lamp_layer_1.png`
- Armor emissive mask: `assets/better_mining_lamp/textures/models/armor/better_mining_lamp_layer_1_emissive.png`
- Curios lamp/bracket overlay: `assets/better_mining_lamp/textures/entity/better_mining_lamp_curio.png`
- Curios emissive mask: `assets/better_mining_lamp/textures/entity/better_mining_lamp_curio_emissive.png`

The armor layer is the full copper utility cap. The Curios overlay and its baked geometry are
lamp/bracket only and must never add a second cap. Provisional art uses the authored copper,
leather, bracket, and warm-lens palette documented in `art/README.md`.

## Validation

```sh
./gradlew verifyFast
./gradlew verifyFull
```

`verifyFull` runs the deterministic checks and five GameTests against the actual Curios inventory
and registered Forge damage handler: ordinary wear, excluded damage, breakage, slot isolation,
and the existing registry/repair/recipe contract. GameTest-only player slot data lives in
`src/gameTestFixtures/resources` and is excluded from the runtime JAR. Rendering and dynamic-light
appearance remain manual checks.

During the active task, each GameTest invocation keeps a fresh world, logs, and `execution.json` under
`build/gametest/<run-token>/`. Verification requires the current token and every reviewed test ID
in `gametest/profiles/full.txt` to register, execute, and pass. Fresh fixtures provision an absent-only
comment-only `server.properties` before native startup; retained/custom property bytes are never
overwritten. The native `logs/latest.log` must exist and contain no ERROR/FATAL operations, even
when Minecraft's tests and process return success. `verifyGameTestEvidenceGuard` checks
that missing, stale, incomplete, duplicate, and failed results are rejected without starting Forge,
as well as absent-property creation, exact existing-property preservation and native ERROR/FATAL rejection.
