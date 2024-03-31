# oneworldfolder

A minecraft fabric mod, that allows minecraft to access the default `saves` folder located in `.minecraft`. 

## Config Location
There are two possible locations for this config file:
- `.minecraft/config/oneworldfolder/oneworldfolder.json`
- `currentMinecraftInstanceFolder/config/oneworldfolder/oneworldfolder.json`

The config with the highest priority will be used.

## Default Config
By default one world folder will automatically detect the default `saves` folder located in `.minecraft`. The generated default config looks like this:
```json
{
	"external_saves_directory": "--auto-detect",
	"priority": -1,
	"replace_owf_and_singleplayer_button": false
}
```

## Custom external `saves` folder
To use a custom saves folder set `external_saves_directory` to your desired directory:
```json
{
	"external_saves_directory": "C:/Users/username/mycoolsaves",
	"priority": -1,
	"replace_owf_and_singleplayer_button": false
}
```
