# Codec2Schema

Through the power of mixins, sheer will, and unchecked type casts, this mod transforms codecs into (mostly) usable JSON schemas.

## What is a Codec?

Codecs are Mojang's way of easily serializing data to and from JSON. They define the structure of an object, meaning JSON schemas can be generated from them.

## What is a JSON Schema?

A JSON schema is a file that describes how JSON files should be structured, and can be used by IDEs to provide autocomplete when creating or editing JSON files.

## About This Mod

When installed, this mod exports JSON schemas for data and resource pack files to `<YOUR GAME DIRECTORY>/codec2schema`. You can use the API to export your own codecs as well!

The generated JSON schemas will have autocomplete for registries (items, blocks, etc), including modded content.

This mod is not perfect: there are many edge cases that aren't or can't be accounted for.

<details>
<summary>Example</summary>

The following is the generated JSON schema for [regional compliancies warnings](https://minecraft.wiki/w/Resource_pack#Regional_compliancies_warnings): 
```json
{
  "$schema": "https://json-schema.org/draft-07/schema",
  "$ref": "#/definitions/def",
  "definitions": {
    "def": {
      "type": "object",
      "propertyNames": {
        "type": "string"
      },
      "additionalProperties": {
        "type": "array",
        "items": {
          "$ref": "#/definitions/def_1"
        }
      }
    },
    "def_1": {
      "type": "object",
      "properties": {
        "delay": {
          "type": "integer",
          "minimum": -9223372036854775808,
          "maximum": 9223372036854775807,
          "default": 0
        },
        "period": {
          "type": "integer",
          "minimum": -9223372036854775808,
          "maximum": 9223372036854775807
        },
        "title": {
          "type": "string"
        },
        "message": {
          "type": "string"
        }
      },
      "required": [
        "period",
        "title",
        "message"
      ]
    }
  }
}
```
It's not perfect, but it's good enough.

</details>
