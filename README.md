## CoreLib

[![License](https://img.shields.io/github/license/Fallen-Breath/fabric-mod-template.svg)](http://www.gnu.org/licenses/lgpl-3.0.html)
[![workflow](https://github.com/sakura-ryoko/corelib/actions/workflows/gradle.yml/badge.svg)](https://github.com/sakura-ryoko/corelib/actions/workflows/gradle.yml)

### Description
* sakura's fabric core library mod.
* This is a Library API that all of my mods / future mods will be using.
* This is not related to [MaLiLib](https://modrinth.com/mod/malilib); it was only made for my own mods.
* None of these features utilize Fabric API in any way except returning the running game directory / "Running in IDE" status.
* CoreLib was forked out of my [AfkPlus](https://modrinth.com/mod/afkplus) mod.  It is primarily intended to be used as a Server-side library, so no GUI support exists.

### Features
- Mod Init standardization
- Config file management
- Client / Player / Server Events
- Server commands interface
- Thread management interface
- Networking interface (WIP-but works)
- Text formatting using a standard utility
- Time/Date & Duration formatting utility
- i18n translation manager utility
- CSV File management (csvtool)

### Using
* Maven: https://maven.sakuraryoko.com/com/sakuraryoko/corelib/
* Format: `corelib_version`-mc`minecraft_version`
* Start coding your Mod Init Dispatcher, and Config Dispatcher.
* Examples can be found under CoreLib's own source code.

[![Join Sakura's RyokoCraft Discord](https://sakuraryoko.com/files/1398873/discord-300px.png)](https://discord.gg/ryokocraftmc)
