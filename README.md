# Wurst++ Client

![Wurst Client logo](https://arti4ikmin.github.io/Wurst++/img/w++%20nobg.png)

### Improved Wurst client having several new features

- **Downloads:** [github.com/arti4ikmin/WurstPlusPlus/releases](https://github.com/arti4ikmin/WurstPlusPlus/releases)

- **Feature list:** [arti4ikmin.github.io/Wurst++](https://arti4ikmin.github.io/Wurst++/index.html)

---
### Why another fork?
[I would contribute to the main repo; unfortenately the review process or PRs 
is going very slowly for whatever reason and a huge amount of PRs just go stale.](https://github.com/Wurst-Imperium/Wurst7/pull/318#issuecomment-1046826198)
<br> This issue was seen by a lot of people, for example Cheddar-BratWurst7.


---

## Installation

Wurst 7 can be installed just like any other Fabric mod. Here are the basic installation steps:

1. Run the Fabric installer.
2. Add the Wurst Client and Fabric API to your mods folder.
---

## Exclusive features:

- New Hack: AutoMLG - automates MLG water bucket/clutch maneuvers


- New Feature: [Lock interaction in freecam](https://github.com/Wurst-Imperium/Wurst7/issues/1172)
- New Feature: Disable Wurst++ Logo
- New Feature: Tracers in Search Hack (4 PRs and a few issues asking/Implementing that in the official repo, all rejected)


- More Privacy: Removed download count logging; Removed Analytics; Removed link redirect logging
- 


---
<details>
<summary> Development Setup </summary>

> [!IMPORTANT]
> Make sure you have [Java Development Kit 21](https://go.wimods.net/from/github.com/Wurst-Imperium/Wurst7?to=https%3A%2F%2Fadoptium.net%2F%3Fvariant%3Dopenjdk21%26jvmVariant%3Dhotspot) installed. It won't work with other versions.


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
