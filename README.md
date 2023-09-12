# oneworldfolder

A minecraft fabric mod, that allows minecraft to access the default `saves` folder located in `.minecraft`. 

It can also enable access to any other external `saves` folder by setting it in the config. The generated default config looks like this:
```json
{
	"external_saves_directory": "--auto-detect",
	"priority": -1
}
```
There are two possible locations for this config file:
- `.minecraft/config/oneworldfolder/oneworldfolder.json`
- `currentMinecraftInstanceFolder/config/oneworldfolder/oneworldfolder.json`

The config with the highest priority will be used.

To use a custom saves folder set `external_saves_directory` to your desired directory:
```json
{
	"external_saves_directory": "C:/Users/username/mycoolsaves",
	"priority": -1
}
```
