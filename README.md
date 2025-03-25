# One World Folder

A minecraft fabric mod, that allows minecraft to access the default `saves` folder located in `.minecraft`. To open the world select screen click on the OneWorldFolder icon next to the Singleplayer button.
If you like my mods consider supporting the development by buying me a coffee:

[![ko-fi](https://github.com/lni-dev/lni-dev/blob/main/images/support-me-on-ko-fi-mc-banner-smaller.png?raw=true)](https://ko-fi.com/T6T41BS1C9)

## Config
One world folder has a config to change its behavior. The config-file currently must be edited manually.

### Config Location
There are two possible locations for this config file:
- `.minecraft/config/oneworldfolder/oneworldfolder.json`
- `currentMinecraftInstanceFolder/config/oneworldfolder/oneworldfolder.json`

The config with the highest priority will be used.

### Default Config
The generated default config looks like this:
```json
{
    "external_saves_directory": "--auto-detect",
    "priority": -1,
    "swap_owf_and_singleplayer_button": false, 
    "replace_singleplayer_button": false, 
    "additional_resourcepacks_dirs": []
}
```
This config will automatically detect the default `saves` folder located in `.minecraft`.

### Custom external `saves` folder
To use a custom saves folder set `external_saves_directory` to your desired directory:
```json
{
    "external_saves_directory": "C:/Users/username/mycoolsaves",
    "priority": -1,
    "swap_owf_and_singleplayer_button": false,
    "replace_singleplayer_button": false,
    "additional_resourcepacks_dirs": []
}
```

### Custom external `resourcepacks` folder (since v. 1.0.13)
To use a custom resourcepacks folder add your desired directory to `additional_resourcepacks_dirs`:
```json
{
    "external_saves_directory": "C:/Users/username/mycoolsaves",
    "priority": -1,
    "swap_owf_and_singleplayer_button": false,
    "replace_singleplayer_button": false,
    "additional_resourcepacks_dirs": [
        "C:/Users/username/mycoolresourcepacks"
    ]
}
```
In the above example the `mycoolresourcepacks` folder acts exactly as the `resourcepacks` folder of your Minecraft
instance.

### Other Config Options
- `swap_owf_and_singleplayer_button` (since v. 1.0.8): The single player button now
  opens your specified external `saves` folder. The OneWorldFolder button now opens
  the normal `saves` folder of the current instance.
- `replace_singleplayer_button` (since v. 1.0.8): The single player button now
  opens your specified external `saves` folder.  No extra buttons are added. If this
  is enabled (set to `true`), the `swap_owf_and_singleplayer_button` option will be 
  ignored.
