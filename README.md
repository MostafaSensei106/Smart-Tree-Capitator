<h1 align="center">🪓 Smart Tree Capitator 🌲</h1>

<p align="center">
  <img src="src/main/resources/assets/smart-tree-capitator/icon.png" width="112" alt="Smart Tree Capitator mod icon">
</p>

<p align="center">
  <strong>Chop the first log. Let the rest fall.</strong><br>
  A Fabric mod for Minecraft Java Edition that fells connected logs one by one, with drops, axe durability, and a crouch-to-cancel control.
</p>

<p align="center">
  <a href="gradle.properties"><img src="https://img.shields.io/badge/Minecraft-26.2-62b47a?style=for-the-badge" alt="Minecraft 26.2"></a>
  <a href="src/main/resources/fabric.mod.json"><img src="https://img.shields.io/badge/Loader-Fabric-d2a756?style=for-the-badge" alt="Fabric mod"></a>
  <a href="gradle.properties"><img src="https://img.shields.io/badge/Version-1.0.0-579dd5?style=for-the-badge" alt="Version 1.0.0"></a>
  <a href="LICENSE"><img src="https://img.shields.io/badge/License-MIT-7b68ae?style=for-the-badge" alt="MIT license"></a>
</p>

<p align="center">
  <a href="#-how-to-play">How to play</a> ·
  <a href="#-install">Install</a> ·
  <a href="#-features">Features</a> ·
  <a href="#-build-from-source">Build</a> ·
  <a href="#-faq">FAQ</a>
</p>

<p align="center">
  <a href=".github/age.png"><img src=".github/age.png" alt="Illustration of a Minecraft player chopping a tree with Smart Tree Capitator" width="100%"></a>
</p>

## 🎮 How to play

| | Action | What happens |
| :---: | --- | --- |
| 🪓 | **Hold an axe** and break a log | The mod finds nearby connected logs of the same block type. |
| 🌲 | **Keep holding the axe** | One more log breaks every 8 server ticks, dropping its items. |
| 🛑 | **Crouch** | Crouch before the first break for normal chopping, or crouch while felling to stop it. |

**Quick version:** axe + log = tree felling. Crouch + axe + log = one log.

> **Heads up:** The mod handles logs, not leaves. Leaves may decay through normal Minecraft behavior after the logs are gone.

## ✨ Features

| Feature | Details |
| --- | --- |
| 🌳 Connected tree felling | Searches matching logs at the same height or one block higher, including diagonal neighbors. |
| ⏱️ Visible progression | Breaks one extra log every 8 server ticks, about 0.4 seconds at 20 TPS. |
| 🎒 Regular drops | Extra logs are destroyed with drops enabled. |
| 🛠️ Fair durability | Each extra log costs one axe durability point outside Creative mode; the task stops before the axe would break. |
| 🧎 Player control | Sneaking prevents or stops felling. Swapping the held axe also stops the task. |
| 🧱 Built-in cap | At most **64 additional logs** can be broken from one initial log. |

The mod follows the exact same **log block type** as the first one. It does not search downward or jump over gaps, so unusual trees and disconnected branches can remain.

## 📦 Install

### Requirements

| Required | Version |
| --- | --- |
| Minecraft Java Edition | **26.2** |
| Fabric Loader | **0.20.1+** |
| Java | **25+** |
| Fabric API | Compatible with Minecraft 26.2 |
| Fabric Language Kotlin | Compatible with Minecraft 26.2 |

1. Install Minecraft 26.2 with Fabric Loader.
2. Add compatible Fabric API and Fabric Language Kotlin jars to your instance's `mods` folder.
3. Get the Smart Tree Capitator jar from [GitHub Releases](https://github.com/MostafaSensei106/Smart-Tree-Capitator/releases) **if one has been published**, or [build the jar yourself](#-build-from-source).
4. Put that jar in the same `mods` folder and start Minecraft.

For single-player, install the mod in your game instance. For multiplayer, install it and its dependencies on the server, which runs the felling logic.

<details>
<summary><strong>📁 Where is the mods folder?</strong></summary>

- Windows: `%APPDATA%\.minecraft\mods`
- macOS: `~/Library/Application Support/minecraft/mods`
- Linux: `~/.minecraft/mods`

Launchers with separate instances usually have their own `mods` folder. Use the folder for the instance you actually play.

</details>

## 🧰 Build from source

Install a **Java 25 JDK** first. The included Gradle wrapper downloads the needed build tools and Minecraft dependencies on its first run.

```bash
git clone https://github.com/MostafaSensei106/Smart-Tree-Capitator.git
cd Smart-Tree-Capitator
./gradlew build
```

On Windows, run `gradlew.bat build`. The installable jar appears in `build/libs/`; use the file without `-sources` or `-dev` in its name.

| Command | Purpose |
| --- | --- |
| `./gradlew build` | Compile and package the mod |
| `./gradlew runClient` | Start a development Minecraft client |
| `./gradlew runDatagen` | Run the configured data generator |

### 🧩 For contributors

The felling logic is in [`SmartTreeCapitator.kt`](src/main/kotlin/com/mostafa/sensei/SmartTreeCapitator.kt). It listens for a player breaking a log, queues nearby matching logs, and processes each active tree on server ticks. Mod metadata and dependencies live in [`fabric.mod.json`](src/main/resources/fabric.mod.json); versions are set in [`gradle.properties`](gradle.properties).

Run `./gradlew build` before opening a pull request. The repository's [GitHub Actions build workflow](.github/workflows/build.yml) is currently commented out and does not run automatically.

## ❓ FAQ

<details>
<summary><strong>Why did only one log break?</strong></summary>

Make sure you used an axe on a block in Minecraft's `logs` tag and were not crouching. The next logs must touch the first log and be the same block type. The search does not travel downward.

</details>

<details>
<summary><strong>Why did the felling stop halfway through?</strong></summary>

The task stops if you crouch, switch the held axe, leave the world, die, run out of connected matching logs, approach the axe's breaking point, or reach the 64 additional log cap.

</details>

<details>
<summary><strong>Does it work with modded trees and axes?</strong></summary>

It checks Minecraft's `logs` block tag and `axes` item tag. Modded blocks and items in those tags can trigger it, but connected logs still need to be the same block type.

</details>

<details>
<summary><strong>Are there settings or commands?</strong></summary>

There are no user-facing configuration options or commands in the current version.

</details>

## ⚖️ License

Smart Tree Capitator and its repository artwork are available under the [MIT License](LICENSE).

<p align="center">
  Made by <a href="https://github.com/MostafaSensei106">Mostafa Sensei</a> 🪓
</p>
