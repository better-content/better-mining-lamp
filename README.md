# Mining Helmet

Mining Helmet adds one always-on copper utility cap for Better Content:
`mining_helmet:mining_helmet`. It has 165 durability, one armor point when worn in the vanilla
head slot, and repairs with copper ingots. Its survival recipe uses one lantern and five copper
ingots.

The same stack can occupy the Curios `head` slot. In that slot it grants no armor, renders only
the lamp and bracket over any ordinary helmet, and loses `max(1, floor(final_damage / 4))`
durability on successful damage that does not bypass armor. A player cannot equip duplicate
Mining Helmets across armor and Curios slots.

Sodium/Embeddium Dynamic Lights is optional. When present, the helmet emits luminance 15 while
held, dropped, worn as armor, or worn as a head Curio; its light is water-sensitive. There is no
toggle, fuel, key binding, menu, or hidden statistic.

## Stable visual asset contract

- Item model: `assets/mining_helmet/models/item/mining_helmet.json`
- Item texture: `assets/mining_helmet/textures/item/mining_helmet.png`
- Item emissive mask: `assets/mining_helmet/textures/item/mining_helmet_emissive.png`
- Armor layer/vanilla helmet UV: `assets/mining_helmet/textures/models/armor/mining_helmet_layer_1.png`
- Armor emissive mask: `assets/mining_helmet/textures/models/armor/mining_helmet_layer_1_emissive.png`
- Curios lamp/bracket overlay: `assets/mining_helmet/textures/entity/mining_helmet_curio.png`
- Curios emissive mask: `assets/mining_helmet/textures/entity/mining_helmet_curio_emissive.png`

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

Each GameTest invocation retains a fresh world, logs, and `execution.json` under
`build/gametest/<run-token>/`. Verification requires the current token and every reviewed test ID
in `gametest/profiles/full.txt` to register, execute, and pass. `verifyGameTestEvidenceGuard` checks
that missing, stale, incomplete, duplicate, and failed results are rejected without starting Forge.
