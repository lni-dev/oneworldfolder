package de.linusdev.mixin.client;

import de.linusdev.OneWorldFolderModClient;
import de.linusdev.oneworldfolder.ErrorDialogScreen;
import de.linusdev.oneworldfolder.ITitleScreenMixin;
import de.linusdev.oneworldfolder.MySelectWorldScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.SpriteIconButton;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen implements ITitleScreenMixin {

    protected TitleScreenMixin(Component title) {
        super(title);
    }

    @Unique
    public void oneworldfolder$addCustomButton() {
        if(OneWorldFolderModClient.config == null) return;

        //reduce size of the singlePlayer button
        int x = 0;
        int y = 0;
        Component singlePlayerText = Component.translatable("menu.singleplayer");

        for(var c : this.children()) {
            if(c instanceof Button b) {
                if(b.getMessage().equals(singlePlayerText)) {

                    if(!OneWorldFolderModClient.config.isReplaceSingleplayerButton())
                        b.setWidth(b.getWidth() - 24); // Change width, if we need to draw a second button next
                                                       // to the single player btn

                    x = b.getX() + b.getWidth() + 4;
                    y = b.getY();

                    if(OneWorldFolderModClient.config.isReplaceSingleplayerButton()) {
                        this.addRenderableWidget(
                                SpriteIconButton.builder(
                                                Component.translatable("menu.singleplayer"),
                                                (button) -> openMyWorldSelectScreen(true),
                                                false
                                        )
                                        .sprite(OneWorldFolderModClient.OWF_ICON_ID, 15, 15)
                                        .size(b.getWidth(), b.getHeight())
                                        .build()
                        ).setPosition(b.getX(), b.getY());
                        this.removeWidget(b);

                    } else if(OneWorldFolderModClient.config.isSwapOwfButtonAndSingleplayerButton()) {
                        this.addRenderableWidget(
                                Button.builder(
                                                Component.translatable("menu.singleplayer"),
                                                (button) -> openMyWorldSelectScreen(true)
                                        )
                                        .bounds(
                                                b.getX(),
                                                b.getY(),
                                                b.getWidth(),
                                                b.getHeight()
                                        )
                                        .build()
                        );
                        this.removeWidget(b);

                    }

                    break;
                }
            }
        }

        if(!OneWorldFolderModClient.config.isReplaceSingleplayerButton()) {
            // We need a secondary button.

            if(OneWorldFolderModClient.config.isSwapOwfButtonAndSingleplayerButton()) {
                //add no-one-world-folder button
                this.addRenderableWidget(
                        SpriteIconButton.builder(
                                        Component.literal(""),
                                        (button) -> this.minecraft.setScreenAndShow(new SelectWorldScreen(this)),
                                        true
                                )
                                .width(20)
                                .sprite(OneWorldFolderModClient.NO_SMALL_OWF_ICON_ID, 15, 15)
                                .build()
                ).setPosition(x, y);
            } else {
                //add one-world-folder button
                this.addRenderableWidget(
                        SpriteIconButton.builder(
                                        Component.literal(""),
                                        (button) -> openMyWorldSelectScreen(true),
                                        true
                                )
                                .width(20)
                                .sprite(OneWorldFolderModClient.OWF_ICON_ID, 15, 15)
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

            OneWorldFolderModClient.LOG.error("One world folder error :( {}", OneWorldFolderModClient.config.getDebugString());
            this.minecraft.setScreenAndShow(new ErrorDialogScreen(() -> this.minecraft.setScreenAndShow(this)));
            return;
        }

        this.minecraft.setScreenAndShow(new MySelectWorldScreen(this));

    }
}
