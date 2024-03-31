package de.linusdev;

import com.mojang.logging.LogUtils;
import de.linusdev.data.parser.exceptions.ParseException;
import de.linusdev.mixin.client.MinecraftClientAccessor;
import de.linusdev.oneworldfolder.ITitleScreenMixin;
import de.linusdev.oneworldfolder.config.Config;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.event.Event;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.util.Identifier;
import net.minecraft.world.level.storage.LevelStorage;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class OneWorldFolderModClient implements ClientModInitializer {

	public static final Identifier OWF_TITLE_SCREEN_IDENTIFIER = new Identifier("oneworldfolder", "titlescreen");

	public static LevelStorage customLevelStorage;
	public static boolean useCustomLevelStorage;

	public static Config config;

	@Override
	public void onInitializeClient() {
		useCustomLevelStorage = false;
		reloadConfig();


		ScreenEvents.AFTER_INIT.register(OWF_TITLE_SCREEN_IDENTIFIER, (client, screen, scaledWidth, scaledHeight) -> {
			if(screen instanceof TitleScreen) {
				((ITitleScreenMixin) screen).oneworldfolder$addCustomButton();
			}
		});
		ScreenEvents.AFTER_INIT.addPhaseOrdering(Event.DEFAULT_PHASE, OWF_TITLE_SCREEN_IDENTIFIER);
	}

	public static void reloadConfig() {
		try {
			config = Config.from(getDefaultMinecraftFolder(), MinecraftClient.getInstance().runDirectory.toPath());
		} catch (IOException | ParseException e) {
			LogUtils.getLogger().error("Cannot find external saves folder: " + e.getMessage());
		}

		if(config.isSupportsCustomLevelStorage()) {
			customLevelStorage = new LevelStorage(
					config.getExternalMinecraftDirectory().resolve(config.getExternalSavesDirName()),
					config.getExternalMinecraftDirectory().resolve("backups"),
					LevelStorage.createSymlinkFinder(config.getExternalMinecraftDirectory().resolve("allowed_symlinks.txt")),
					((MinecraftClientAccessor)MinecraftClient.getInstance()).getDataFixer()
			);
		}
	}

	public static @Nullable Path getDefaultMinecraftFolder() {
		String OS = System.getProperty("os.name").toLowerCase();

		if (OS.contains("win")) {
			return Paths.get(System.getProperty("user.home"), "AppData", "Roaming", ".minecraft");
		} else if (OS.contains("nix") || OS.contains("nux")) {
			return Paths.get(System.getProperty("user.home"), ".minecraft");
		} else {
			return null;
		}
	}


}