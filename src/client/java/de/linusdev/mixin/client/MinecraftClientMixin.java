package de.linusdev.mixin.client;

import de.linusdev.OneWorldFolderModClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.integrated.IntegratedServerLoader;
import net.minecraft.world.level.storage.LevelStorage;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

	@Shadow
	@Final
	private LevelStorage levelStorage;

	/**
	 * @author LinusDev
	 * @reason required to swap level storage because it is a final variable
	 */
	@Overwrite
	public LevelStorage getLevelStorage() {
		return OneWorldFolderModClient.useCustomLevelStorage ? OneWorldFolderModClient.customLevelStorage : this.levelStorage;
	}

	/**
	 * @author LinusDev
	 * @reason required to swap level storage because it is a final variable
	 */
	@Overwrite
	public IntegratedServerLoader createIntegratedServerLoader() {
		return new IntegratedServerLoader(MinecraftClient.getInstance(), getLevelStorage());
	}
}