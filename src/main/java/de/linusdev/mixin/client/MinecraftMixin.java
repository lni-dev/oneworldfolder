package de.linusdev.mixin.client;

import de.linusdev.OneWorldFolderModClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.worldselection.WorldOpenFlows;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Minecraft.class)
public class MinecraftMixin {

	@Shadow
	@Final
	private LevelStorageSource levelSource;

	/**
	 * @author LinusDev
	 * @reason required to swap level storage because it is a final variable
	 */
	@Overwrite
	public LevelStorageSource getLevelSource() {
		return OneWorldFolderModClient.useCustomLevelStorage ? OneWorldFolderModClient.customLevelStorage : this.levelSource;
	}

	/**
	 * @author LinusDev
	 * @reason required to swap level storage because it is a final variable
	 */
	@Overwrite
	public WorldOpenFlows createWorldOpenFlows() {
		return new WorldOpenFlows(Minecraft.getInstance(), getLevelSource());
	}
}