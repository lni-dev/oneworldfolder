package de.linusdev.oneworldfolder;

import de.linusdev.OneWorldFolderModClient;
import de.linusdev.oneworldfolder.config.Config;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import org.jetbrains.annotations.NotNull;

public class ErrorDialogScreen extends ConfirmScreen {

    public ErrorDialogScreen(@NotNull Runnable action) {
        super(
                confirmed -> {
                    if(confirmed) {
                        action.run();
                    } else {
                        Util.getOperatingSystem().open(OneWorldFolderModClient.config.getConfigFile().getParent().toUri());
                    }
                },
                Text.of("Cannot find custom saves location"),
                OneWorldFolderModClient.config.isCannotFindMinecraftFolder() ?
                        Text.of("Cannot auto detect '.minecraft' directory. You can manually set your custom 'saves' folder in the \n'" + Config.OWF_CONFIG_FILE_NAME + "'\nconfig file.") :
                        Text.of("\nMinecraft directory \n'" + OneWorldFolderModClient.config.getExternalMinecraftDirectory()
                                + "'\ncannot be found.\nYou can set a custom save path in the config file located in \n'" + OneWorldFolderModClient.config.getConfigFile() + "'."),
                Text.of("            okay :(            "),
                Text.of("Open Config File")
        );
    }

}
