package de.linusdev.mixin.client;

import de.linusdev.OneWorldFolderModClient;
import de.linusdev.oneworldfolder.ErrorDialogScreen;
import de.linusdev.oneworldfolder.ITitleScreenMixin;
import de.linusdev.oneworldfolder.MySelectWorldScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextIconButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

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
        Identifier owfIconId = new Identifier("oneworldfolder","icon/owf-icon-1024");
       this.addDrawableChild(
                TextIconButtonWidget.builder(
                        Text.of(""),
                        (button) -> openMyWorldSelectScreen(true),
                        true
                )
                        .width(20)
                        .texture(owfIconId, 18, 18)
                        .build()
        ).setPosition(x, y);
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
