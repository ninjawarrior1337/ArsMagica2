package am2.items.rendering;

import java.util.Collection;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import am2.affinity.Affinity;
import am2.api.AffinityRegistry;
import am2.models.ArsMagicaModelLoader;
import am2.utils.ResourceUtils;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.client.model.ItemLayerModel;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;

public class SpellModel implements IModel {
	
	
	private ImmutableList<ResourceLocation> textures;
	public static HandBakedModel handRender = null;

	public SpellModel(ImmutableList<ResourceLocation> textures) {
		this.textures = textures;
	}
	
	@Override
	public Collection<ResourceLocation> getDependencies() {
		return ImmutableList.of();
	}

	@Override
	public Collection<ResourceLocation> getTextures() {
		return textures;
	}

	@Override
	public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
		ImmutableMap<TransformType, TRSRTransformation> map = IPerspectiveAwareModel.MapWrapper.getTransforms(state);
		IBakedModel model = new ItemLayerModel(textures).bake(state, format, bakedTextureGetter);
		if (handRender == null) {
			handRender = new HandBakedModel(model, map);
			for (Affinity affinity : AffinityRegistry.getAffinityMap().values()) {
				IBakedModel subModel = ItemLayerModel.INSTANCE.retexture(ImmutableMap.of("layer0", ArsMagicaModelLoader.sprites.get(affinity).getIconName())).bake(state, format, bakedTextureGetter);
				handRender.addPart(affinity, subModel);
			}
		}
		return new SpellBakedModel(model, handRender, map);
	}

	@Override
	public IModelState getDefaultState() {
		return ResourceUtils.DEFAULT_ITEM_STATE;
	}

}
