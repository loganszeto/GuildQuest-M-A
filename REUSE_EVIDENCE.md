# Reuse Evidence (GuildQuest subsystems)

This file captures concrete reuse so the implementation is easy to audit.

## A) Reuse from Lance Vu (Assignment_3_INF122 (1).zip)

Source archive reviewed (not included in final repo):
`Assignment_3_INF122 (1).zip`

### A1) Settings subsystem reuse (separate section requested)

Source contributor for this settings section: **Nathaneil Heit**

- **Source files from Nathaneil Heit's prior code**:
  - `.../guildquest/domain/Settings.java`
  - `.../guildquest/domain/Theme.java`
  - `.../guildquest/domain/TimeDisplayPreference.java`
- **What we reused/adapted**:
  - The same settings domain idea: current realm + theme + time display preference.
  - Theme/time preference as dedicated types.
  - Central settings state object used by UI screens.
- **Where adapted in current repo**:
  - `Frontend/Settings.java`
  - `Frontend/Theme.java`
  - `Frontend/TimeDisplayPreference.java`
  - `Frontend/ThemeFactory.java`
  - `Frontend/TDPFactory.java`
  - `Frontend/SettingsScreen.java`
- **How it is used now**:
  - `Frontend/GMAEGUI.java` stores global settings and applies them.
  - `Frontend/AdventureMenuScreen.java` updates selected realm into settings.
  - `Frontend/SettingsScreen.java` mutates theme/time display at runtime.

### A2) Campaign model reuse/adaptation

- **Source file from Lance Vu's zip**:
  - `.../guildquest/domain/Campaign.java`
- **What we reused/adapted**:
  - Campaign concept as a first-class GuildQuest domain object.
  - Kept a simplified campaign representation appropriate for the current mini-adventure scope.
- **Where adapted in current repo**:
  - `Backend/Campaign.java`
  - `Backend/CampaignFactory.java`
- **How it is used now**:
  - Available as persistent campaign domain object and factory hook for future expansion.

### A3) Permission/security concept adaptation

- **Source files from Lance Vu's zip**:
  - `.../guildquest/domain/PermissionLevel.java`
  - `.../guildquest/domain/SharePermission.java`
- **What we reused/adapted**:
  - Role/permission gating idea for protected actions.
- **Where adapted in current repo**:
  - `Frontend/AccessControlService.java`
- **How it is used now**:
  - `Frontend/AdventureMenuScreen.java` checks permissions before start/delete operations.

## B) Other reused GuildQuest subsystems (team/project baseline)

### B1) Realm/Map model

- **Source**: earlier GuildQuest map/realm work (tiles, coordinates, map spaces).
- **Where in repo**:
  - `Backend/RealmSpace.java`
  - `Backend/Realm.java`
  - `Backend/Tiles/*`
  - `Backend/Maps/*`
- **What changed/adapted**:
  - Added/adapted menu integration and game backends to consume `RealmSpace`.
  - Timed Raid and Relic Hunt share map-space infrastructure.
- **Where used now**:
  - `Backend/RelicHuntGameBackend.java`
  - `Backend/TimedRaidGameBackend.java`
  - `Frontend/RelicHuntGameScreen.java`

### B2) Player profile model

- **Source**: earlier GuildQuest user/profile system.
- **Where in repo**:
  - `Backend/User.java`
  - `Backend/UserFactory.java`
- **What changed/adapted**:
  - Integrated directly into two-player flow and mini-adventure initialization.
  - Added usage across login/menu/game manager.
- **Where used now**:
  - `Frontend/LoginScreen.java`
  - `Frontend/TwoPlayerGameManager.java`
  - `Frontend/MiniAdventure.java`
  - `Backend/RelicHuntGameBackend.java`

### B3) Time-window rules

- **Source**: timed-raid style map/time concepts from earlier GuildQuest work.
- **Where in repo**:
  - `Backend/Maps/TimedRaidWindow.java`
  - `Backend/TimedRaidGameBackend.java`
- **What changed/adapted**:
  - Added explicit countdown and timeout lose condition in `TimedRaidGameBackend`.
- **Where used now**:
  - `Frontend/AdventureMenuScreen.java` (adventure selection)
  - `Frontend/RelicHuntGameScreen.java` (countdown display + timeout handling)

