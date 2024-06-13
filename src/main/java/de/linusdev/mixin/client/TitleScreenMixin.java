package de.linusdev.mixin.client;

import de.linusdev.OneWorldFolderModClient;
import de.linusdev.oneworldfolder.ErrorDialogScreen;
import de.linusdev.oneworldfolder.ITitleScreenMixin;
import de.linusdev.oneworldfolder.MySelectWorldScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
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

                    if(!OneWorldFolderModClient.config.isReplaceSingleplayerButton())
                        b.setWidth(b.getWidth() - 24); // Change width, if we need to draw a second button next
                                                       // to the single player btn

                    x = b.getX() + b.getWidth() + 4;
                    y = b.getY();

                    if(OneWorldFolderModClient.config.isReplaceSingleplayerButton()) {
                        this.addDrawableChild(
                                TextIconButtonWidget.builder(
                                                Text.translatable("menu.singleplayer"),
                                                (button) -> openMyWorldSelectScreen(true),
                                                false
                                        )
                                        .texture(OneWorldFolderModClient.OWF_ICON_ID, 18, 18)
                                        .dimension(b.getWidth(), b.getHeight())
                                        .build()
                        ).setPosition(b.getX(), b.getY());
                        this.remove(b);

                    } else if(OneWorldFolderModClient.config.isSwapOwfButtonAndSingleplayerButton()) {
                        this.addDrawableChild(
                                ButtonWidget.builder(
                                                Text.translatable("menu.singleplayer"),
                                                (button) -> openMyWorldSelectScreen(true)
                                        )
                                        .dimensions(
                                                b.getX(),
                                                b.getY(),
                                                b.getWidth(),
                                                b.getHeight()
                                        )
                                        .build()
                        );
                        this.remove(b);

                    }

                    break;
                }
            }
        }

        if(!OneWorldFolderModClient.config.isReplaceSingleplayerButton()) {
            // We need a secondary button.

            if(OneWorldFolderModClient.config.isSwapOwfButtonAndSingleplayerButton()) {
                //add no-one-world-folder button
                this.addDrawableChild(
                        TextIconButtonWidget.builder(
                                        Text.of(""),
                                        (button) -> this.client.setScreen(new SelectWorldScreen(this)),
                                        true
                                )
                                .width(20)
                                .texture(OneWorldFolderModClient.NO_SMALL_OWF_ICON_ID, 18, 18)
                                .build()
                ).setPosition(x, y);
            } else {
                //add one-world-folder button
                this.addDrawableChild(
                        TextIconButtonWidget.builder(
                                        Text.of(""),
                                        (button) -> openMyWorldSelectScreen(true),
                                        true
                                )
                                .width(20)
                                .texture(OneWorldFolderModClient.OWF_ICON_ID, 18, 18)
                                .build()
                ).setPosition(x, y);
            }

        }
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
