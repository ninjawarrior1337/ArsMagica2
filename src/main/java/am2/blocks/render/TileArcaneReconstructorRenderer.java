package am2.blocks.render;

import org.lwjgl.opengl.GL11;

import com.google.common.base.Function;

import am2.api.math.AMVector3;
import am2.blocks.tileentity.TileEntityArcaneReconstructor;
import am2.bosses.renderers.RenderItemNoBob;
import am2.gui.AMGuiHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.entity.RenderEntityItem;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.model.TRSRTransformation;

public class TileArcaneReconstructorRenderer extends TileEntitySpecialRenderer<TileEntityArcaneReconstructor>{

	private ItemStack renderStack;
	RenderEntityItem renderItem;
	
	IModel mainModel;
	IModel ring1Model;
	IModel ring2Model;
	IModel ring3Model;
	IBakedModel mainBakedModel;
	IBakedModel ring1BakedModel;
	IBakedModel ring2BakedModel;
	IBakedModel ring3BakedModel;
	
	private void bake() {
		try {
			mainModel = ModelLoaderRegistry.getModel(new ResourceLocation("arsmagica2", "block/reconstructor/main.obj"));
			ring1Model =  ModelLoaderRegistry.getModel(new ResourceLocation("arsmagica2", "block/reconstructor/ring1.obj"));
			ring2Model =  ModelLoaderRegistry.getModel(new ResourceLocation("arsmagica2", "block/reconstructor/ring2.obj"));
			ring3Model =  ModelLoaderRegistry.getModel(new ResourceLocation("arsmagica2", "block/reconstructor/ring3.obj"));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		Function<ResourceLocation, TextureAtlasSprite> getter = location -> Minecraft.getMinecraft()
				.getTextureMapBlocks().getAtlasSprite(location.toString());
		mainBakedModel = mainModel.bake(TRSRTransformation.identity(), DefaultVertexFormats.ITEM, getter);
		ring1BakedModel = ring1Model.bake(TRSRTransformation.identity(), DefaultVertexFormats.ITEM, getter);
		ring2BakedModel = ring2Model.bake(TRSRTransformation.identity(), DefaultVertexFormats.ITEM, getter);
		ring3BakedModel = ring3Model.bake(TRSRTransformation.identity(), DefaultVertexFormats.ITEM, getter);
    }

	public TileArcaneReconstructorRenderer(){

		renderStack = new ItemStack(Items.WOODEN_SHOVEL);

	}

	@Override
	public void renderTileEntityAt(TileEntityArcaneReconstructor te, double x, double y, double z, float partialTicks, int destructionStage){
		if (renderItem == null)
			renderItem = new RenderItemNoBob(Minecraft.getMinecraft().getRenderManager());
		bake();
		float floatingOffset = te.getOffset();//(float) (Math.sin(te.getOffset()) * (Math.PI/ 180) * 1.4f);

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		if ((destructionStage == -10) && te.shouldRenderItemStack()){
			renderStack = te.getCurrentItem();
			if (renderStack != null)
				RenderItemAtCoords(renderStack, x + 0.5f, y + 0.85f, z + 0.5f, partialTicks);
		}

		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

		GL11.glPushMatrix(); //start
		GL11.glTranslatef((float)x + 0.5f, (float)y + 0.22f, (float)z + 0.5f);
		
		renderGroup(te, mainBakedModel);
		
		GL11.glTranslatef(0, 0.22f + floatingOffset, 0);
		RenderRotatedModelGroup(te, ring3BakedModel, te.getInnerRingRotation());
		RenderRotatedModelGroup(te, ring1BakedModel, te.getMiddleRingRotation());
		RenderRotatedModelGroup(te, ring2BakedModel, te.getOuterRingRotation());

		if (te.shouldRenderRotateOffset()){

			GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.4f);
			RenderRotatedModelGroup(te, ring3BakedModel, te.getInnerRingRotation().copy().sub(te.getInnerRingRotationSpeed().copy().scale(te.getRotateOffset())));
			RenderRotatedModelGroup(te, ring1BakedModel, te.getMiddleRingRotation().copy().sub(te.getMiddleRingRotationSpeed().copy().scale(te.getRotateOffset())));
			RenderRotatedModelGroup(te, ring2BakedModel, te.getOuterRingRotation().copy().sub(te.getOuterRingRotationSpeed().copy().scale(te.getRotateOffset())));

			GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.15f);

			RenderRotatedModelGroup(te, ring3BakedModel, te.getInnerRingRotation().copy().sub(te.getInnerRingRotationSpeed().copy().scale(te.getRotateOffset() * 2)));
			RenderRotatedModelGroup(te, ring1BakedModel, te.getMiddleRingRotation().copy().sub(te.getMiddleRingRotationSpeed().copy().scale(te.getRotateOffset() * 2)));
			RenderRotatedModelGroup(te, ring2BakedModel, te.getOuterRingRotation().copy().sub(te.getOuterRingRotationSpeed().copy().scale(te.getRotateOffset() * 2)));
		}

		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glPopMatrix(); //end

		//GL11.glDisable(GL11.GL_BLEND);
	}

	private void RenderRotatedModelGroup(TileEntityArcaneReconstructor te, IBakedModel model, AMVector3 rotation){
		GlStateManager.pushMatrix();

		GlStateManager.rotate(rotation.x, 1.0f, 0.0f, 0.0f);
		GlStateManager.rotate(rotation.y, 1.0f, 1.0f, 0.0f);
		GlStateManager.rotate(rotation.z, 1.0f, 0.0f, 1.0f);
		renderGroup(te, model);
		GlStateManager.popMatrix();
	}
	
	private void renderGroup(TileEntityArcaneReconstructor te, IBakedModel model) {
		try{
			GlStateManager.pushMatrix();
			GlStateManager.translate(-te.getPos().getX(), -te.getPos().getY(), -te.getPos().getZ());
			Tessellator t = Tessellator.getInstance();
			VertexBuffer wr = t.getBuffer();
			wr.begin(7, DefaultVertexFormats.BLOCK);
			Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().renderModel(te.getWorld(), model, te.getWorld().getBlockState(te.getPos()), te.getPos(), wr, false);
			t.draw();
			GlStateManager.popMatrix();
		}catch (Throwable t){

		}		
	}

	private void RenderItemAtCoords(ItemStack item, double x, double y, double z, float partialTick){
		item.stackSize = 1;
		AMGuiHelper.instance.dummyItem.setEntityItemStack(item);
		renderItem.doRender(AMGuiHelper.instance.dummyItem, x, y, z, AMGuiHelper.instance.dummyItem.rotationYaw, partialTick);
	}
}
