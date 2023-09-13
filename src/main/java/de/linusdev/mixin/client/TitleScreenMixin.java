package de.linusdev.mixin.client;

import de.linusdev.OneWorldFolderModClient;
import de.linusdev.oneworldfolder.ErrorDialogScreen;
import de.linusdev.oneworldfolder.ITitleScreenMixin;
import de.linusdev.oneworldfolder.MySelectWorldScreen;
import net.minecraft.client.gui.screen.DialogScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.option.LanguageOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen implements ITitleScreenMixin {

    protected TitleScreenMixin(Text title) {
        super(title);
    }

    @Unique
    public void oneworldfolder$addCustomButton() {
        //reduce size of the singlePlayer button
        int x = 0;
        int y = 0;
        Text singlePlayerText = Text.translatable("menu.singleplayer");

        for(var c : this.children()) {
            if(c instanceof ButtonWidget b) {
                if(b.getMessage().equals(singlePlayerText)) {
                    b.setWidth(b.getWidth() - 24);
                    x = b.getX() + b.getWidth() + 4;
                    y = b.getY();
                    break;
                }
            }
        }

        //add one world folder button
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
