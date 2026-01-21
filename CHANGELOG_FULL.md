# Changelog

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
