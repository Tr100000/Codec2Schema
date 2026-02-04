# Changelog

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
