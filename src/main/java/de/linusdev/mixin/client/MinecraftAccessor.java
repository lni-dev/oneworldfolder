package de.linusdev.mixin.client;

import com.mojang.datafixers.DataFixer;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Minecraft.class)
public interface MinecraftAccessor {

    @Accessor("fixerUpper")
    public DataFixer getFixerUpper();


}
