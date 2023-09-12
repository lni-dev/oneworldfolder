package de.linusdev.mixin.client;

import de.linusdev.OneWorldFolderModClient;
import de.linusdev.oneworldfolder.MySelectWorldScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.option.LanguageOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {

    protected TitleScreenMixin(Text title) {
        super(title);
    }

    @Inject(at = @At("RETURN"), method = "initWidgetsNormal")
    private void addCustomButton(int y, int spacingY, CallbackInfo ci) {
        if(OneWorldFolderModClient.config.isSupportsCustomLevelStorage()) {
            this.addDrawableChild(ButtonWidget.builder(Text.of(""), (button) -> {
                this.client.setScreen(new MySelectWorldScreen(this));
            }).dimensions(this.width / 2 - 100 + 205, y, 20, 20).build());

            int l = this.height / 4 + 48;
            this.addDrawableChild(
                    new TexturedButtonWidget(
                            this.width / 2 - 100 + 205, y,
                            20, 20,
                            -1, -1,
                            0,
                            new Identifier("oneworldfolder","textures/gui/icon-1024.png"), 18, 18, (button) -> {
                        this.client.setScreen(new MySelectWorldScreen(this));
            }, Text.of("One World Folder")));
        }

    }
}
