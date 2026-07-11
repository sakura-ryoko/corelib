## corelib

[![License](https://img.shields.io/github/license/Fallen-Breath/fabric-mod-template.svg)](http://www.gnu.org/licenses/lgpl-3.0.html)
[![workflow](https://github.com/sakura-ryoko/corelib/actions/workflows/gradle.yml/badge.svg)](https://github.com/sakura-ryoko/corelib/actions/workflows/gradle.yml)

### Description
* sakura's fabric core library mod.
* This is a Library API that all of my mods / future mods will be using.
* This is not related to MaLiLib; it was only made for my own mods.
* None of these features utilize Fabric API in any way except returning the running game directory / "Running in IDE" status.

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
* Examples can be found under corelib's own source code.

[![Join Sakura's RyokoCraft Discord](https://sakuraryoko.com/files/1398873/discord-300px.png)](https://discord.gg/ryokocraftmc)
