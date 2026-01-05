### Added
- Added support for `FailSoftMapCodec` from the Fabric Dimensions API

### Changed
- Schemas generated on a previous run are now deleted before generation
  - This prevents old schemas from persisting 

### Fixed
- The handler for `UnboundedMapCodec` now properly takes into account the key codec
- Gradle source and target compatibility is now correctly set to Java 21
