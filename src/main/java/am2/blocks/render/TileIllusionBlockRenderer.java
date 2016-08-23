package am2.blocks.render;

import am2.blocks.tileentity.TileEntityIllusionBlock;
import am2.defs.BlockDefs;
import am2.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.init.Blocks;

public class TileIllusionBlockRenderer extends TileEntitySpecialRenderer<TileEntityIllusionBlock> {

	@Override
	public void renderTileEntityAt(TileEntityIllusionBlock te, double x, double y, double z, float partialTicks, int destroyStage) {
		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		GlStateManager.pushMatrix();
		RenderHelper.disableStandardItemLighting();
		GlStateManager.translate(x, y, z);
		GlStateManager.translate(-te.getPos().getX(), -te.getPos().getY(), -te.getPos().getZ());
		if (te.getMimicBlock() != null && te.getMimicBlock() != Blocks.AIR.getDefaultState()) {
			Tessellator.getInstance().getBuffer().begin(7, DefaultVertexFormats.BLOCK);
			Minecraft.getMinecraft().getBlockRendererDispatcher().renderBlock(te.getMimicBlock(), te.getPos(), te.getWorld(), Tessellator.getInstance().getBuffer());
			Tessellator.getInstance().draw();
			if (destroyStage >= 0) {
				Tessellator.getInstance().getBuffer().begin(7, DefaultVertexFormats.BLOCK);
				TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/destroy_stage_" + destroyStage);
	            IBakedModel ibakedmodel = Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelForState(te.getMimicBlock());
	            IBakedModel ibakedmodel1 = net.minecraftforge.client.ForgeHooksClient.getDamageModel(ibakedmodel, sprite, te.getMimicBlock(), te.getWorld(), te.getPos());
				Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().renderModel(te.getWorld(), ibakedmodel1, te.getMimicBlock(), te.getPos(), Tessellator.getInstance().getBuffer(), true);
				Tessellator.getInstance().draw();
			}
		} else {
			RenderUtils.renderBlockModel(te, Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getModelForState(BlockDefs.illusionBlock.getDefaultState()), BlockDefs.illusionBlock.getDefaultState());
		}
		GlStateManager.popMatrix();
		RenderHelper.enableStandardItemLighting();
	}
}
