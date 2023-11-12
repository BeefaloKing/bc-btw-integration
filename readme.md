# BuildCraft Better Than Wolves Integration
Produces patches for BuildCraft-Client Core, Energy, and Factory that adds
integration with Better Than Wolves. There's no configuration, but the feature
set is minimal and patches can be applied independantly.

Unrelated, also patches a crash in ExtraBuildCraftPipes because I happen to be
using it in my single player world right now.

Compatible with:
- Minecraft beta 1.7.3
- BuildCraft 2.1.1
- Better Than Wolves 2.94
- ExtraBuildCraftPipes 2.0.1.2

## Core
Renames "Iron Gear" to "Steel Gear", and changes recipe to require BTW steel.

## Energy
Renames "Redstone Engine" to "Wooden Engine".

Wooden Engines now require continuous mechanical power from BTW to operate. The
axle must be placed at the back of the engine.

## Factory
Changes "Automatic Crafting Table" recipe to require one Iron/Steel Gear
(on top) and three Wooden Gears instead of four Wooden Gears.

# Building
Setup an MCP v4.3 instance with a client jar produced by the mods listed in
[sources.yaml][1]. Ensure you are running with a Java 8 JDK.

Decompile the client, apply [patches][2], copy in my modified sources, compile,
reobfuscate.

Finally create archives specified in [targets.yml][3] consisting of the
resulting reobfuscated class files listed. The intent here is for these to be
extracted ontop of the existing BuildCraft zips.

\(It's maybe just me, but I ran into an issue with name mangled class names
changing between what I recompiled and what the original buildcraft zips had.
Maybe `jdk8u352-b08` isn't the right version to be using? You'll run into issues
reobfuscating if you have the same issue I did.\)

# Attribution & Licensing
This is a distribution of modifed BuildCraft source code. My implementation of
BTW mechanical power integration was derived from FlowerChilds Better Than
Wolves source code.

Both their software licenses can be found under [licenses][4].

[1]: ./sources.yaml
[2]: ./patches/
[3]: ./targets.yml
[4]: ./licenses/
