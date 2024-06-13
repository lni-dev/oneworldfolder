package de.linusdev.oneworldfolder.config;

import de.linusdev.OneWorldFolderModClient;
import de.linusdev.data.parser.JsonParser;
import de.linusdev.data.parser.exceptions.ParseException;
import de.linusdev.data.so.SOData;
import net.minecraft.client.MinecraftClient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class Config {
    public static final @NotNull JsonParser JSON_PARSER = new JsonParser();

    public static final String CONFIG_DIR_NAME = "config";
    public static final String OWF_CONFIG_DIR_NAME = "oneworldfolder";
    public static final String OWF_CONFIG_FILE_NAME = "oneworldfolder.json";

    public static final String EXTERNAL_SAVES_DIR_KEY = "external_saves_directory";
    public static final String AUTO_DETECT = "--auto-detect";

    public static final String PRIORITY_KEY = "priority";

    public static final String SWAP_OWF_BUTTON_AND_SINGLEPLAYER_BUTTON_OLD_KEY = "replace_owf_and_singleplayer_button";
    public static final String SWAP_OWF_BUTTON_AND_SINGLEPLAYER_BUTTON_KEY = "swap_owf_and_singleplayer_button";
    public static final String REPLACE_SINGLEPLAYER_BUTTON_KEY = "replace_singleplayer_button";

    public static @NotNull Config from(@Nullable Path @NotNull ... locations) throws IOException, ParseException {
        Config highestPriority = null;
        for(Path location : locations) {
            if(location == null) continue;

            Path configFile = location
                    .resolve(CONFIG_DIR_NAME)
                    .resolve(OWF_CONFIG_DIR_NAME)
                    .resolve(OWF_CONFIG_FILE_NAME);

            if(Files.exists(configFile)) {
                Config c = new Config(configFile);
                c.store(); // add missing fields

                if(highestPriority == null || highestPriority.getPriority() < c.getPriority()) {
                    highestPriority = c;
                }
            }
        }

        if(highestPriority != null)
            return highestPriority;

        return new Config(
                MinecraftClient.getInstance().runDirectory.toPath()
                        .resolve(CONFIG_DIR_NAME)
                        .resolve(OWF_CONFIG_DIR_NAME)
                        .resolve(OWF_CONFIG_FILE_NAME)
        ).store();
    }

    private final @NotNull Path configFile;
    private final boolean supportsCustomLevelStorage;
    private boolean cannotFindMinecraftFolder;
    private boolean autodetectSavesPath = false;

    private final Path externalMinecraftDirectory;
    private final String externalSavesDirName;
    private final int priority;
    private final boolean swapOwfButtonAndSingleplayerButton;
    private final boolean replaceSingleplayerButton;

    public Config(@NotNull Path configFile) throws IOException, ParseException {
        this.configFile = configFile;

        SOData data = SOData.newOrderedDataWithKnownSize(10);
        if(Files.exists(configFile))
            data = JSON_PARSER.parseStream(Files.newInputStream(configFile));

        Path externalSavesDir = data
                .getContainer(EXTERNAL_SAVES_DIR_KEY)
                .castAndConvert((String string) -> {
                    if (string == null || string.equals(AUTO_DETECT)) {
                        autodetectSavesPath = true;
                        Path md = OneWorldFolderModClient.getDefaultMinecraftFolder();
                        return md == null ? null : md.resolve("saves");
                    }

                    return Paths.get(string);

                })
                .get();

        externalMinecraftDirectory = externalSavesDir == null ? null : externalSavesDir.getParent();
        externalSavesDirName = externalSavesDir == null ? null : externalSavesDir.getFileName().toString();
        priority = data.getNumberAsInt(PRIORITY_KEY, key -> -1);
        boolean oldSwapOwfButtonAndSingleplayerButton = (boolean) data.getOrDefaultBoth(SWAP_OWF_BUTTON_AND_SINGLEPLAYER_BUTTON_OLD_KEY, false);
        swapOwfButtonAndSingleplayerButton = (boolean) data.getOrDefaultBoth(SWAP_OWF_BUTTON_AND_SINGLEPLAYER_BUTTON_KEY, oldSwapOwfButtonAndSingleplayerButton);
        replaceSingleplayerButton = (boolean) data.getOrDefaultBoth(REPLACE_SINGLEPLAYER_BUTTON_KEY, false);

        supportsCustomLevelStorage = !(externalMinecraftDirectory == null || !Files.exists(externalMinecraftDirectory));
        cannotFindMinecraftFolder = externalMinecraftDirectory == null;

    }

    public boolean isSupportsCustomLevelStorage() {
        return supportsCustomLevelStorage;
    }

    public boolean isCannotFindMinecraftFolder() {
        return cannotFindMinecraftFolder;
    }

    public Path getExternalMinecraftDirectory() {
        return externalMinecraftDirectory;
    }

    public String getExternalSavesDirName() {
        return externalSavesDirName;
    }

    public boolean isSwapOwfButtonAndSingleplayerButton() {
        return swapOwfButtonAndSingleplayerButton;
    }

    public boolean isReplaceSingleplayerButton() {
        return replaceSingleplayerButton;
    }

    public int getPriority() {
        return priority;
    }

    public @NotNull Path getConfigFile() {
        return configFile;
    }

    public Config store() throws IOException {
        if(!Files.exists(configFile.getParent()))
            Files.createDirectories(configFile.getParent());

        SOData data = SOData.newOrderedDataWithKnownSize(10);

        data.add(EXTERNAL_SAVES_DIR_KEY,
                autodetectSavesPath ? AUTO_DETECT :
                externalMinecraftDirectory.resolve(externalSavesDirName).toString()
        );

        data.add(PRIORITY_KEY, priority);
        data.add(SWAP_OWF_BUTTON_AND_SINGLEPLAYER_BUTTON_KEY, swapOwfButtonAndSingleplayerButton);
        data.add(REPLACE_SINGLEPLAYER_BUTTON_KEY, replaceSingleplayerButton);

        Writer writer = Files.newBufferedWriter(configFile, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE);
        JSON_PARSER.writeData(writer, data);
        writer.close();
        return this;
    }
}
