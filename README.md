# Codec2Schema
<!-- modrinth_exclude.start -->
[![Link to Modrinth page](https://raw.githubusercontent.com/intergrav/devins-badges/refs/heads/v3/assets/cozy/available/modrinth_vector.svg)](https://modrinth.com/mod/codec2schema)
<!-- modrinth_exclude.end -->
Through the power of sheer will, mixins, and unchecked type casts, this mod transforms codecs into (mostly) usable JSON schemas.

When installed, this mod exports JSON schemas for data and resource pack files to `<YOUR GAME DIRECTORY>/codec2schema`. You can use the API to export your own codecs as well!

The generated JSON schemas will have autocomplete for registries (items, blocks, etc). This means that if you have mods that add items, for example, the ids for those items will show up for autocomplete. This is the main reason why I haven't provided any pre-generated schemas, if you were wondering.
