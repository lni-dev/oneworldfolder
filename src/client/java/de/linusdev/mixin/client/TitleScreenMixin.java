package de.linusdev.mixin.client;

import com.google.common.collect.ImmutableList;
import de.linusdev.OneWorldFolderModClient;
import de.linusdev.oneworldfolder.ErrorDialogScreen;
import de.linusdev.oneworldfolder.MySelectWorldScreen;
import de.linusdev.oneworldfolder.config.Config;
import net.minecraft.client.gui.screen.DialogScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.option.LanguageOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {

    protected TitleScreenMixin(Text title) {
        super(title);
    }

    @Inject(at = @At("RETURN"), method = "initWidgetsNormal")
    private void addCustomButton(int y, int spacingY, CallbackInfo ci) {

        //reduce size of the singlePlayer button
        Text singlePlayerText = Text.translatable("menu.singleplayer");

        for(var c : this.children()) {
            if(c instanceof ButtonWidget b) {
                if(b.getMessage().equals(singlePlayerText)) {
                    b.setWidth(b.getWidth() - 24);
                    break;
                }
            }
        }

        //add one world folder button
        int x = this.width / 2 - 100 + 180;

        //background
        this.addDrawableChild(ButtonWidget.builder(Text.of(""), (button) -> {
            openMyWorldSelectScreen(true);
        }).dimensions(x, y, 20, 20).build());

        //icon
        this.addDrawableChild(
                new TexturedButtonWidget(
                        x, y,
                        20, 20,
                        -1, -1,
                        0,
                        new Identifier("oneworldfolder","textures/gui/icon-1024.png"), 18, 18, (button) -> {
                    openMyWorldSelectScreen(true);
                }, Text.of("One World Folder")));


    }

    @Unique
    public void openMyWorldSelectScreen(boolean tryReload) {
        if(!OneWorldFolderModClient.config.isSupportsCustomLevelStorage()) {

            if(tryReload) {
                OneWorldFolderModClient.reloadConfig();
                openMyWorldSelectScreen(false);
                return;
            }

            this.client.setScreen(new ErrorDialogScreen(button -> this.client.setScreen(this)));
            return;
        }

        this.client.setScreen(new MySelectWorldScreen(this));

    }
}
