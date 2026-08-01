# Changelog

## [0.3.7+1.21.11]

### Added
- Added an option to export plugin info (ids, versions, exported schemas, etc.)

### Changed
- The game version in exported info now uses the internal name instead of the display name

## [0.3.6+1.21.11]

Backported changes

## [0.2.4+1.21.11]

### Changed
- Changed `SchemaExporter` to print the path relative to the game directory instead of the full path when logging

### Fixed
- Fixed the value part of `SimpleMapCodec`s being completely ignored

## [0.2.3+1.21.11]

### Changed
- Improve support for ranged codecs and constrained-size string codecs

## [0.2.2+1.21.11]

### Changed
- Added support for `CodecFromMap` from TrUtils
- Small tweaks and performance improvements

## [0.2.1+1.21.11]

### Changed
- Removed the old entrypoint classes
  - (I forgot to do this)

## [0.2.0+1.21.11]

### Changed
- Replaced entrypoints with `codec2schema:main` and `codec2schema:client`, these must implement `Codec2SchemaPlugin`
  - Old entrypoints will no longer work, this is a **breaking change**
- Split client and common sources
  - This will help prevent bugs and crashes in the future

## [0.1.4+1.21.11]

### Fixed
- Fixed erroneous entries in required fields in some cases
- Fixed crash without Fabric API (hopefully)

## [0.1.3+1.21.11]

### Changed
- Messed with the build script again
- Schema generation is now performed after registry bootstrapping

### Fixed
- Removed some debugging stuff that was left by mistake

## [0.1.2+1.21.11]

### Added
- Added support for `FailSoftMapCodec` from the Fabric Dimensions API

### Changed
- Schemas generated on a previous run are now deleted before generation
  - This prevents old schemas from persisting

### Fixed
- The handler for `UnboundedMapCodec` now properly takes into account the key codec
- Gradle source and target compatibility is now correctly set to Java 21

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
