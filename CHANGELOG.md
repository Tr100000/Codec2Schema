**Updated to 26.1-snapshot-5**

### Changed
- Replaced entrypoints with `codec2schema:main` and `codec2schema:client`, these must implement `Codec2SchemaPlugin`
  - Old entrypoints will no longer work, this is a **breaking change**
- Split client and common sources
  - This will help prevent bugs and crashes in the future
