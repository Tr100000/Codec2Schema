# Changelog

## [0.3.3+26.1.1]

### Fixed
- Fixed `cow_sound_variant` not being exported (oops)

## [0.3.2+26.1]

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

## [0.3.1+26.1]

### Changed
- Changed `SchemaExporter` to print the path relative to the game directory instead of the full path when logging

## [0.3.0+26.1]

### Added
- Improve support for optional fields by including default values in exported schemas

### Fixed
- Fixed the value part of `SimpleMapCodec`s being completely ignored

## [0.3.0-beta.2+26.1]

**Updated to 26.1**

### Added
- Added a simple config
- Added a work-in-progress option to inline definitions only reference once (`inlineSingularReference`)
  - This still has some issues and may slightly increase output size (due to indentation)
- You can now register `CodecSchemaModifier`s
  - This is an experimental API feature
- `CodecWithValuePair`s can now return a custom fallback codec to use

### Changed
- Somewhat improved support for fixed-size list codecs
- A bunch of internal stuff that you shouldn't worry about

## [0.3.0-beta.1+26.1-pre-1]

**Updated to 26.1-pre-1**

### Changed
- Improve support for ranged codecs and constrained-size string codecs

## [0.2.2+26.1-snapshot-11]

### Changed
- Added support for `CodecFromMap` from TrUtils
- Small tweaks and performance improvements

## [0.2.1+26.1-snapshot-11]

**Updated to 26.1-snapshot-11**

## [0.2.1+26.1-snapshot-10]

**Updated to 26.1-snapshot-10**

### Fixed
- Compile with an older version of Fabric Model Loading API (v1)

## [0.2.1+26.1-snapshot-8]

**Updated to 26.1-snapshot-8**

As Fabric Model Loading API (v1) hasn't been ported yet, support for that has been disabled for this release.

## [0.2.1+26.1-snapshot-7]

**Updated to 26.1-snapshot-7**

As Fabric Model Loading API (v1) hasn't been ported yet, support for that has been disabled for this release.

## [0.2.1+26.1-snapshot-6]

**Updated to 26.1-snapshot-6**

### Changed
- Removed the old entrypoint classes
  - (I forgot to do this)

## [0.2.0+26.1-snapshot-5]

**Updated to 26.1-snapshot-5**

### Changed
- Replaced entrypoints with `codec2schema:main` and `codec2schema:client`, these must implement `Codec2SchemaPlugin`
  - Old entrypoints will no longer work, this is a **breaking change**
- Split client and common sources
  - This will help prevent bugs and crashes in the future

## [0.1.4+26.1-snapshot-4]

**Updated to 26.1-snapshot-4**

### Fixed
- Fixed erroneous entries in required fields in some cases
- Fixed crash without Fabric API (hopefully)

## [0.1.3+26.1-snapshot-3]

**Updated to 26.1-snapshot-3**

## [0.1.3+26.1-snapshot-2]

**Updated to 26.1-snapshot-2**

### Changed
- Messed with the build script again
- Schema generation is now performed after registry bootstrapping

### Fixed
- Removed some debugging stuff that was left by mistake

## [0.1.2+26.1-snapshot-1]

**Updated to 26.1-snapshot-1**

### Added
- Now generates schemas for `data/trade_set` and [`data/villager_trade`](https://minecraft.wiki/w/Villager_trade_definition)
- Added support for `FailSoftMapCodec` from the Fabric Dimensions API

### Changed
- Schemas generated on a previous run are now deleted before generation
    - This prevents old schemas from persisting

### Fixed
- The handler for `UnboundedMapCodec` now properly takes into account the key codec

## [0.1.1+1.21.11]

### Fixed

- Fixed crash without Fabric API
- Fixed typo in the generated file name for regional compliancies warnings
- Slightly improve some warnings
- Mess with the build script
- Remove an unused impl method

### Changed

- Slightly improve some warnings
- Mess with the build script

## [0.1.0+1.21.11]

Initial release
