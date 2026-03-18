### Added
- Added a work-in-progress option to inline definitions only reference once (`inlineSingularReference`)
  - This still has some issues and will slightly increase output size (due to indentation)
- You can now register `CodecSchemaModifier`s
- `CodecWithValuePair`s can now return a custom fallback codec to use

### Changed
- Somewhat improved support for fixed-size list codecs
- A bunch of internal stuff that you shouldn't worry about
