package de.linusdev;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExampleMod implements ModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("oneworldfolder");

	@Override
	public void onInitialize() {
		LOGGER.info("Hello");
	}
}