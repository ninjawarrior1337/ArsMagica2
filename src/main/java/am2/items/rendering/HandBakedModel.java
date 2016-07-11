package am2.items.rendering;

import java.util.HashMap;
import java.util.List;

import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.ImmutableMap;

import am2.affinity.Affinity;
import am2.utils.AffinityShiftUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.common.model.TRSRTransformation;

public class HandBakedModel implements IPerspectiveAwareModel {

	private IBakedModel parent;
	private ImmutableMap<TransformType, TRSRTransformation> transforms;
	private HashMap<Affinity, IBakedModel> parts;
	
	 public HandBakedModel(IBakedModel parent, ImmutableMap<TransformType, TRSRTransformation> transforms) {
		 this.parent = parent;
		 this.transforms = transforms;
		 this.parts = new HashMap<>();
	 }

	@Override
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
		return parent.getQuads(state, side, rand);
	}

	@Override
	public boolean isAmbientOcclusion() {
		return parent.isAmbientOcclusion();
	}

	@Override
	public boolean isGui3d() {
		return parent.isGui3d();
	}

	@Override
	public boolean isBuiltInRenderer() {
		return parent.isBuiltInRenderer();
	}

	@Override
	public TextureAtlasSprite getParticleTexture() {
		return parent.getParticleTexture();
	}

	@SuppressWarnings("deprecation")
	@Override
	public ItemCameraTransforms getItemCameraTransforms() {
		return parent.getItemCameraTransforms();
	}

	@Override
	public ItemOverrideList getOverrides() {
		return new SpellParticleRender(parent.getOverrides().getOverrides(), this);
	}
	
	@Override
	public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType) {
		return IPerspectiveAwareModel.MapWrapper.handlePerspective(this, transforms, cameraTransformType);
	}
	
	public void addPart(Affinity aff, IBakedModel model) {
		this.parts.put(aff, model);
	}
	
	public IBakedModel getModel(Affinity aff) {
		return this.parts.get(aff);
	}
	
	public static class Overrides extends ItemOverrideList{
		
		private HandBakedModel model;

		public Overrides(List<ItemOverride> overridesIn, HandBakedModel model) {
			super(overridesIn);
			this.model = model;
		}
		
		@Override
		public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, World world, EntityLivingBase entity) {
			if (world == null || entity == null)
				return super.handleItemState(originalModel, stack, world, entity);
			//System.out.println(AffinityShiftUtils.getMainShiftForStack(stack));
			IBakedModel model = this.model.getModel(AffinityShiftUtils.getMainShiftForStack(stack));
			return model == null ? super.handleItemState(originalModel, stack, world, entity) : model;
		}
	}

}
