<h1 align="center">🪓 Smart Tree Capitator 🌲</h1>

<p align="center">
  <img src="src/main/resources/assets/smart-tree-capitator/icon.png" width="112" alt="Smart Tree Capitator mod icon">
</p>

<p align="center">
  <strong>Chop the first log. Let the rest fall.</strong><br>
  A Fabric mod for Minecraft Java Edition that fells natural trees in a visible cascade, gathers drops at the base, and replants saplings.
</p>

<p align="center">
  <a href="gradle.properties"><img src="https://img.shields.io/badge/Minecraft-26.2-62b47a?style=for-the-badge" alt="Minecraft 26.2"></a>
  <a href="src/main/resources/fabric.mod.json"><img src="https://img.shields.io/badge/Loader-Fabric-d2a756?style=for-the-badge" alt="Fabric mod"></a>
  <a href="gradle.properties"><img src="https://img.shields.io/badge/Version-1.1.0-579dd5?style=for-the-badge" alt="Version 1.1.0"></a>
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
| 🌲 | **Keep holding the axe** | Up to three logs break each server tick, from bottom to top. |
| 🛑 | **Crouch** | By default, crouching before the first break disables tree felling. This can be reversed in Config. |

**Quick version:** By default, axe + log fells a tree; crouch + axe + log breaks one block. The Shift setting reverses this.

> **Heads up:** The first log is broken by Minecraft normally. The mod consolidates the extra log and fast-decaying leaf drops at that log's position.

## ✨ Features

| Feature | Details |
| --- | --- |
| 🌳 Connected tree felling | Searches matching logs at the same height or one block higher, including diagonal neighbors. |
| ⏱️ Visible progression | Breaks up to three logs per server tick with normal break sound and particles. |
| 🎒 Consolidated drops | Extra logs and fast-decaying leaves drop at the first log's position. Identical items are combined into stacks first. |
| 🛠️ Fair durability | Each extra log costs one axe durability point outside Creative mode; the task stops before the axe would break. |
| 🧎 Player control | Config controls whether Shift is required or prevents felling. Swapping the held axe stops the task. |
| 🧱 Tree protection | Requires natural leaves near the top and rejects logs touching building blocks such as doors, glass, beds, planks, stairs, slabs, fences, or trapdoors. |
| 🌱 Replanting | Uses a matching sapling from the collected drops or player inventory on suitable soil. |
| 🍃 Fast leaf decay | Nearby unsupported natural leaves are removed over the following server ticks. |
| ⚙️ Configurable cap | Maximum connected logs defaults to **64** and can be set from 1 to 512. |

The mod follows the exact same **log block type** as the first one. It does not search downward or jump over gaps, so unusual trees and disconnected branches can remain.

Tree detection is a safety heuristic, not a guarantee for every player-built structure. It examines loaded blocks only and cancels automatic felling if the search would enter an unloaded chunk. The visual cascade uses block-break sound and particles; it does not simulate a rigid falling tree.

## 📦 Install

### Requirements

| Required | Version |
| --- | --- |
| Minecraft Java Edition | **26.2** |
| [Fabric Loader](https://fabricmc.net/use/installer/) | **0.20.1+** (select Minecraft 26.2) |
| Java | **25+** |
| [Fabric API](https://modrinth.com/mod/fabric-api/version/ewUK83HI) | **0.161.0+26.2** (Fabric) |
| [Fabric Language Kotlin](https://modrinth.com/mod/fabric-language-kotlin/version/SKW1BTL3) | **1.14.2+kotlin.2.4.21** (Fabric) |
| [YetAnotherConfigLib (YACL)](https://modrinth.com/mod/yacl/version/3.9.7%2B26.2-fabric) | **3.9.7+26.2-fabric** |

1. Install Minecraft **26.2** with Fabric Loader **0.20.1 or newer**, then select that Fabric profile in your launcher.
2. Download the three required Fabric jars from the exact version links above. Put all three in the Fabric 26.2 instance's `mods` folder. To open the Config screen in game, also install [Mod Menu 20.0.3 for 26.2](https://modrinth.com/mod/modmenu/version/iRo7AZXf) (optional).
3. Get the Smart Tree Capitator jar from [GitHub Releases](https://github.com/MostafaSensei106/Smart-Tree-Capitator/releases) **if one has been published**, or [build the jar yourself](#-build-from-source).
4. Put that jar in the same `mods` folder and start Minecraft.

The built Smart Tree Capitator jar does not contain Fabric API, Fabric Language Kotlin, or YACL. Gradle downloads them for development only. If Fabric reports that one is missing, check the `mods` folder of the **instance you actually launched**, not another Minecraft installation.

For single-player, install the mod in your game instance. For multiplayer, install it and its dependencies on the server, which runs the felling logic.

Open **Mod Menu → Smart Tree Capitator → Config** to set the maximum log count and Shift behavior. Settings are saved in `config/smart-tree-capitator.json`. On dedicated servers, edit that file on the server and restart it; a client's Config screen does not change server settings.

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

[`SmartTreeCapitator.kt`](src/main/kotlin/com/mostafa/sensei/SmartTreeCapitator.kt) registers the mod. [`TreeDetector.kt`](src/main/kotlin/com/mostafa/sensei/tree/TreeDetector.kt) performs a bounded connected-log search; [`NaturalTreeProtection.kt`](src/main/kotlin/com/mostafa/sensei/tree/NaturalTreeProtection.kt) applies the replaceable structure and leaf policy. [`FellingScheduler.kt`](src/main/kotlin/com/mostafa/sensei/tree/FellingScheduler.kt) queues up to 64 trees, reserves log positions per world, and advances at most eight tasks per server tick. [`FellingTask.kt`](src/main/kotlin/com/mostafa/sensei/tree/FellingTask.kt) moves through log, leaf, and completion phases; it breaks at most three logs per step. `LeafDecay` avoids scanning the same position twice, `DropCollector` merges matching stacks, and `SaplingReplanter` consumes a sapling only after it has been placed. `config/` stores one immutable settings snapshot; `client/config/` provides the Mod Menu screen. Mod metadata and dependencies live in [`fabric.mod.json`](src/main/resources/fabric.mod.json); versions are set in [`gradle.properties`](gradle.properties).

Run `./gradlew build` before opening a pull request. The repository's [GitHub Actions build workflow](.github/workflows/build.yml) is currently commented out and does not run automatically.

## ❓ FAQ

<details>
<summary><strong>Why did only one log break?</strong></summary>

Make sure you used an axe on a block in Minecraft's `logs` tag and used Shift according to Config. The next logs must touch the first log, be the same block type, and have natural leaves near the top. The search does not travel downward.

</details>

<details>
<summary><strong>Why did the felling stop halfway through?</strong></summary>

The task stops if you switch the held axe, leave the world, die, run out of connected matching logs, or approach the axe's breaking point. A tree exceeding the configured cap is left to normal chopping.

</details>

<details>
<summary><strong>Does it work with modded trees and axes?</strong></summary>

It checks Minecraft's `logs` block tag and `axes` item tag. Modded blocks and items in those tags can trigger it, but connected logs still need to be the same block type.

</details>

<details>
<summary><strong>Are there settings or commands?</strong></summary>

Yes. Mod Menu provides a Config screen for the maximum connected log count and Shift behavior. Settings are saved in `config/smart-tree-capitator.json`. There are no commands.

</details>

## ⚖️ License

Smart Tree Capitator and its repository artwork are available under the [MIT License](LICENSE).

<p align="center">
  Made by <a href="https://github.com/MostafaSensei106">Mostafa Sensei</a> 🪓
</p>
