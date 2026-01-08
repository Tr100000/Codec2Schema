# Changelog

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [0.1.3+1.21.11] - 2026-01-08

### Changed
- Messed with the build script again
- Schema generation is now performed after registry bootstrapping

### Fixed
- Removed some debugging stuff that was left by mistake

## [0.1.2+1.21.11] - 2026-01-05

### Added
- Added support for `FailSoftMapCodec` from the Fabric Dimensions API

### Changed
- Schemas generated on a previous run are now deleted before generation
  - This prevents old schemas from persisting

### Fixed
- The handler for `UnboundedMapCodec` now properly takes into account the key codec
- Gradle source and target compatibility is now correctly set to Java 21

## [0.1.1+1.21.11] - 2026-01-02

### Fixed

- Fixed crash without Fabric API
- Fixed typo in the generated file name for regional compliancies warnings
- Slightly improve some warnings
- Mess with the build script
- Remove an unused impl method

### Changed

- Slightly improve some warnings
- Mess with the build script

## [0.1.0+1.21.11] - 2025-12-27

Initial release
