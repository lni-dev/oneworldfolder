package de.linusdev.mixin.client;

import de.linusdev.OneWorldFolderModClient;
import net.minecraft.resource.*;
import net.minecraft.util.path.SymlinkFinder;
import org.apache.commons.io.filefilter.SymbolicLinkFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.file.Paths;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

@Mixin(ResourcePackManager.class)
public abstract class ResourcePackManagerMixin {
	@Unique
	private static final Logger LOGGER = LoggerFactory.getLogger("ResourcePackManagerMixin");

	@Shadow
	@Final
	@Mutable
    private Set<ResourcePackProvider> providers;

	@Shadow
	private Map<String, ResourcePackProfile> profiles;

	@Inject(method = "<init>", at = @At("RETURN"))
	public void construct(ResourcePackProvider[] resourcePackProviders, CallbackInfo info) {
		// Use a LinkedHashSet to preserve ordering
		providers = new LinkedHashSet<>(providers);

		if(OneWorldFolderModClient.config == null) return;

		for (String additionalPackDir : OneWorldFolderModClient.config.getAdditionalPackDirs()) {
			providers.add(new FileResourcePackProvider(
					Paths.get(additionalPackDir),
					ResourceType.CLIENT_RESOURCES,
					ResourcePackSource.NONE,
					new SymlinkFinder(path -> true)
			));
		}

	}
}