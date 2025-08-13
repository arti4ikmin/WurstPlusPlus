# Wurst++ Client

![Wurst Client logo](https://arti4ikmin.github.io/Wurst++/img/w++%20nobg.png)

### Improved Wurst client having several new features

- **Downloads:** [github.com/arti4ikmin/WurstPlusPlus/releases](https://github.com/arti4ikmin/WurstPlusPlus/releases)

- **Feature list:** [Wurst++/#exclusive-features](https://github.com/arti4ikmin/WurstPlusPlus?tab=readme-ov-file#exclusive-features)

---
### Why another fork?
I would contribute to the main repo; [unfortenately the review process of PRs 
is going very slowly for whatever reason and a huge amount of PRs just go stale](https://github.com/Wurst-Imperium/Wurst7/pull/318#issuecomment-1046826198)
<br> This issue was seen by a lot of people, for example Cheddar-BratWurst7.

\+ So many beautiful feature requests were ignored...

https://arti4ikmin.github.io/Wurst++/

---

## Installation

Wurst++ can be installed just like any other Fabric mod. Here are the basic installation steps:

1. Run the [Fabric installer](https://fabricmc.net/use/installer/). 
2. Add the [Wurst++ Client](https://github.com/arti4ikmin/WurstPlusPlus/releases) and [Fabric API](https://modrinth.com/mod/fabric-api/versions) to your mods folder.
---

## Exclusive features:

| Feature                       | Category     | Description                                                                                    |
|:------------------------------|:-------------|:-----------------------------------------------------------------------------------------------|
| **AutoMLG**                   | New Hack     | Automates MLG water bucket/clutch maneuvers.                                                   |
| **XCarry**                    | New Hack     | Allows to use 4 more slots (in the )crafting slots) like normal inventory slots.               |
| **BeaconExploit**             | New Hack     | Allows to get ANY combo of effects (even Regen 2).                                             |
| AutoLightning                 | New Hack     | Automatically lights up dark areas with light sources.                                         |
| Lock interaction in freecam   | New Feature  | Allows locking interaction while in freecam mode.                                              |
| Disable Wurst++ Logo          | New Feature  | Provides an option to disable the Wurst logo.                                                  |
| Tracers in Search Hack        | New Feature  | Implements tracers functionality within the Search Hack.                                       |
| Improved AntiHunger           | New Feature  | Avoids breaking features; Added Soft NoSprint which makes the server think you arent sprinting |
| Removed Analytics             | More Privacy | Enhances user privacy by removing analytic + download count logging                            |
| Removed link redirect logging | More Privacy | Enhances user privacy by no longer logging link redirects (github)                             |


|                               |              |                                                                     |

More to go!
<br>

---

<details>
<summary> Development Setup </summary>

> [!IMPORTANT]
> Make sure you have [Java Development Kit 21](https://www.oracle.com/de/java/technologies/downloads/) installed. It won't work with other versions.


### Development using IntelliJ IDEA

```pwsh
git clone https://github.com/arti4ikmin/WurstPlusPlus.git
cd WurstPlusPlus
./gradlew genSources idea
```


### Development using Eclipse

1. Clone the repository:

   ```pwsh
   git clone https://github.com/arti4ikmin/WurstPlusPlus.git
   cd WurstPlusPlus
   ```

2. Generate the sources:

   ```pwsh
   ./gradlew genSources eclipse
   ```

3. In Eclipse, go to `Import...` > `Existing Projects into Workspace` and select this project.

4. **Optional:** Right-click on the project and select `Properties` > `Java Code Style`. Then under `Clean Up`, `Code Templates`, `Formatter`, import the respective files in the `codestyle` folder.

### Development using VSCode / Cursor

> [!TIP]
> You'll probably want to install the [Extension Pack for Java](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack) to make development easier.

1. Clone the repository:

   ```pwsh
   git clone https://github.com/Wurst-Imperium/Wurst7.git
   cd WurstPlusPlus
   ```

2. Generate the sources:

   ```pwsh
   ./gradlew genSources vscode
   ```

3. Open the `Wurst7` folder in VSCode / Cursor.

4. **Optional:** In the VSCode settings, set `java.format.settings.url` to `https://raw.githubusercontent.com/Wurst-Imperium/Wurst7/master/codestyle/formatter.xml` and `java.format.settings.profile` to `Wurst-Imperium`.
</details>

---

## License

This code is licensed under the GNU General Public License v3. **You can only use this code in open-source clients that you release under the same license! Using it in closed-source/proprietary clients is not allowed!**
