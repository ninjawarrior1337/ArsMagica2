package am2.models;

import am2.items.rendering.ModelCullface;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.LoaderState;

public class CullfaceModelLoader implements ICustomModelLoader {

	@Override
	public void onResourceManagerReload(IResourceManager resourceManager) {
		
	}

	@Override
	public boolean accepts(ResourceLocation modelLocation) {
		return modelLocation.getResourcePath().contains("celestial_prism_cullface");
	}

	@Override
	public IModel loadModel(ResourceLocation modelLocation) throws Exception {
		if (!Loader.instance().hasReachedState(LoaderState.POSTINITIALIZATION)) {
			return ModelLoaderRegistry.getMissingModel();
		}
		IModel newModel = OBJLoader.INSTANCE.loadModel(modelLocation);
		return new ModelCullface(newModel);
	}

}
