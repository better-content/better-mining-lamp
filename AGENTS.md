# AGENTS.md

## Scope

This repository contains the Better Content-owned Forge mod **Better Mining Lamp**.

- Mod ID: `better_mining_lamp`
- Artifact: `better-mining-lamp-<version>.jar`
- Java 17, Minecraft 1.20.1, Forge 47.4.13
- Curios is required; Sodium/Embeddium Dynamic Lights is an optional client integration.

## Validation and release

Run `./gradlew verifyFull`, then commit and push each coherent change. Do not commit build output,
runtime worlds, logs, or IDE state. Repository-local validation does not authorize JAR deployment,
Packwiz refresh, or pack-level testing.
