package de.linusdev.mixin.client;

import de.linusdev.OneWorldFolderModClient;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.*;
import net.minecraft.world.level.validation.DirectoryValidator;
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



@Mixin(PackRepository.class)
public abstract class PackRepositoryMixin {
	@Unique
	private static final Logger LOGGER = LoggerFactory.getLogger("ResourcePackManagerMixin");

	@Shadow
	@Final
	@Mutable
    private Set<RepositorySource> sources;

	@Shadow
	private Map<String, Pack> available;

	@Inject(method = "<init>", at = @At("RETURN"))
	public void construct(RepositorySource[] resourcePackProviders, CallbackInfo info) {
		// Use a LinkedHashSet to preserve ordering
		sources = new LinkedHashSet<>(sources);

		if(OneWorldFolderModClient.config == null) return;

		for (String additionalPackDir : OneWorldFolderModClient.config.getAdditionalPackDirs()) {
			sources.add(new net.minecraft.server.packs.repository.FolderRepositorySource(
					Paths.get(additionalPackDir),
					PackType.CLIENT_RESOURCES,
					PackSource.DEFAULT,
					new DirectoryValidator(path -> true)
			));
		}

	}
}