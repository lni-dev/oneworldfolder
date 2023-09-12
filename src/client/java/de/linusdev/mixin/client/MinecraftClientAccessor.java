package de.linusdev.mixin.client;

import com.mojang.datafixers.DataFixer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.world.level.storage.LevelStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MinecraftClient.class)
public interface MinecraftClientAccessor {

    @Accessor("dataFixer")
    public DataFixer getDataFixer();


}
