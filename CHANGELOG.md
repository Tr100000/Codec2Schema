**Updated to 26.1-snapshot-1** 

### Added
- Now generates schemas for `data/trade_set` and [`data/villager_trade`](https://minecraft.wiki/w/Villager_trade_definition)
- Added support for `FailSoftMapCodec` from the Fabric Dimensions API

### Changed
- Schemas generated on a previous run are now deleted before generation
  - This prevents old schemas from persisting 

### Fixed
- The handler for `UnboundedMapCodec` now properly takes into account the key codec
