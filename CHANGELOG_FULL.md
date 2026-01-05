# Changelog

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [0.1.2+26.1-snapshot-1] - 2026-01-05

**Updated to 26.1-snapshot-1**

### Added
- Now generates schemas for `data/trade_set` and [`data/villager_trade`](https://minecraft.wiki/w/Villager_trade_definition)
- Added support for `FailSoftMapCodec` from the Fabric Dimensions API

### Changed
- Schemas generated on a previous run are now deleted before generation
    - This prevents old schemas from persisting

### Fixed
- The handler for `UnboundedMapCodec` now properly takes into account the key codec

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
