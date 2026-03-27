package de.linusdev.oneworldfolder;

import de.linusdev.OneWorldFolderModClient;
import de.linusdev.oneworldfolder.config.Config;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Util;
import org.jetbrains.annotations.NotNull;

public class ErrorDialogScreen extends ConfirmScreen {

    public ErrorDialogScreen(@NotNull Runnable action) {
        super(
                confirmed -> {
                    if(confirmed) {
                        action.run();
                    } else {
                        Util.getPlatform().openUri(OneWorldFolderModClient.config.getConfigFile().getParent().toUri());
                    }
                },
                Component.literal("Cannot find custom saves location"),
                OneWorldFolderModClient.config.isCannotFindMinecraftFolder() ?
                        Component.literal("Cannot auto detect '.minecraft' directory. You can manually set your custom 'saves' folder in the \n'" + Config.OWF_CONFIG_FILE_NAME + "'\nconfig file.") :
                        Component.literal("\nMinecraft directory \n'" + OneWorldFolderModClient.config.getExternalMinecraftDirectory()
                                + "'\ncannot be found.\n\nYou can set a custom save path in the config file located in \n'" + OneWorldFolderModClient.config.getConfigFile() + "'." + (
                                OneWorldFolderModClient.isLinux()
                                        ?
                                        ("\n\nIf you are on Linux, this might be a permission related issue (See https://github.com/lni-dev/oneworldfolder/issues/7)")
                                        : "")
                        ),
                Component.literal("            okay :(            "),
                Component.literal("Open Config File")
        );
    }

}
