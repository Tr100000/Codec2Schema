### Added
- Added a config option to disable plugins

### Changed
- The config now uses codecs for serialization
  - The mod now exports a JSON schema for the config file
- Re-enable compat for Fabric Model Loading API

### Fixed
- Fixed wrong mixin target in `ExtraCodecsMixin`
- Fixed a bunch of mixin warnings
- Fixed some schemas not being exported because I didn't know about them
  - This fixes `cat_sound_variant`, `chicken_sound_variant`, `damage_type`, `pig_sound_variant`, `world_clock`, and `zombie_nautilus_variant`
  - A check has been added to prevent this from happening with future updates (in some cases)
