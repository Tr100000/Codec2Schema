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
