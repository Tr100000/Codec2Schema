### Fixed
- `"$schema"` is now the first property in generated schema files
- Fixed some mixins being incorrectly applied when only some Fabric API modules are loaded
- Added support for Fabric's tag file entry removals
  - This feature seems to modify the tag file codec in a way the mod wasn't expecting, which is probably why it didn't work
