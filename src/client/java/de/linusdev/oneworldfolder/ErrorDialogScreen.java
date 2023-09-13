package de.linusdev.oneworldfolder;

import com.google.common.collect.ImmutableList;
import de.linusdev.OneWorldFolderModClient;
import de.linusdev.oneworldfolder.config.Config;
import net.minecraft.client.gui.screen.DialogScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ErrorDialogScreen extends DialogScreen {

    public ErrorDialogScreen(@Nullable ButtonWidget.PressAction action) {
        super(
                Text.of("Cannot find custom saves location"),
                List.of(
                        OneWorldFolderModClient.config.isCannotFindMinecraftFolder() ?
                                Text.of("Cannot auto detect '.minecraft' directory. You can manually set your custom 'saves' folder in the \n'" + Config.OWF_CONFIG_FILE_NAME + "'\nconfig file.") :
                                Text.of("\nMinecraft directory \n'" + OneWorldFolderModClient.config.getExternalMinecraftDirectory() + "'\ncannot be found."),
                        Text.of("\nYou can set a custom save path in the config file located in \n'" + OneWorldFolderModClient.config.getConfigFile() + "'.")
                ),
                ImmutableList.of(
                        new ChoiceButton(Text.of("            okay :(            "), action),
                        new ChoiceButton(
                                Text.of("Open Config File"),
                                btn -> Util.getOperatingSystem().open(OneWorldFolderModClient.config.getConfigFile().getParent().toUri())
                        )
                )
        );
    }

}
