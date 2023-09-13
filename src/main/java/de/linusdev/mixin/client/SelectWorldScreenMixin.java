package de.linusdev.mixin.client;

import de.linusdev.OneWorldFolderModClient;
import de.linusdev.oneworldfolder.MySelectWorldScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SelectWorldScreen.class)
public class SelectWorldScreenMixin {

    @Inject(at = @At("HEAD"), method = "init")
    public void init(CallbackInfo ci) {
        Object this_ = this;
        OneWorldFolderModClient.useCustomLevelStorage = this_ instanceof MySelectWorldScreen;
    }

}
