package am2.blocks.render;

import static net.minecraft.client.renderer.texture.TextureMap.LOCATION_BLOCKS_TEXTURE;

import org.lwjgl.opengl.GL11;

import am2.blocks.tileentity.TileEntityCraftingAltar;
import am2.texture.SpellIconManager;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileCraftingAltarRenderer extends TileEntitySpecialRenderer<TileEntityCraftingAltar> {

	@Override
	public void renderTileEntityAt(TileEntityCraftingAltar te, double x, double y, double z, float partialTicks, int destroyStage) {
		World w = te.getWorld();
		BlockPos pos = te.getPos();
		GL11.glPushMatrix();
		TextureAtlasSprite def = SpellIconManager.INSTANCE.getSprite("CasterRuneSide");
		Tessellator t = Tessellator.getInstance();
		GL11.glTranslated(x, y, z);
		//GL11.glDisable(GL11.GL_BLEND);
		RenderHelper.disableStandardItemLighting();
		if (te.structureValid()) {
			GL11.glPushMatrix();
			IBlockState state = w.getBlockState(pos.down(4).north());
			//LogHelper.info(state);
			t.getBuffer().begin(7, DefaultVertexFormats.BLOCK);
			GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			GlStateManager.translate(-pos.getX(), -pos.getY(), -pos.getZ());
			Minecraft.getMinecraft().renderEngine.bindTexture(LOCATION_BLOCKS_TEXTURE);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			IBakedModel model = Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(state);
			Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().renderModel(Minecraft.getMinecraft().theWorld, model, state, new BlockPos(pos), t.getBuffer(), false);
			t.draw();
			GL11.glPopMatrix();
		} else {
			render(te, def);
		}
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glTranslated(-0.005, -0.005, -0.005);
		GL11.glScaled(1.01, 1.01, 1.01);
		render(te, SpellIconManager.INSTANCE.getSprite("RuneStone"));
		GL11.glDisable(GL11.GL_BLEND);
		RenderHelper.enableStandardItemLighting();
		GL11.glPopMatrix();
	}
	
	public void render(TileEntityCraftingAltar te, TextureAtlasSprite sprite) {
		if (sprite != null)
			Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		float minU = (sprite != null ? sprite.getMinU() : 0F);
		float maxU = (sprite != null ? sprite.getMaxU() : 1F);
		float minV = (sprite != null ? sprite.getMinV() : 0F);
		float maxV = (sprite != null ? sprite.getMaxV() : 1F);
		//LogHelper.info(sprite);
		GL11.glPushMatrix();
		RenderHelper.disableStandardItemLighting();
		//GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		Tessellator tessellator = Tessellator.getInstance();
		VertexBuffer buffer = tessellator.getBuffer();
		buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
		buffer.pos(0, 1, 0).tex(maxU, minV).endVertex();
		buffer.pos(1, 1, 0).tex(minU, minV).endVertex();
		buffer.pos(1, 0, 0).tex(minU, maxV).endVertex();
		buffer.pos(0, 0, 0).tex(maxU, maxV).endVertex();
		
		buffer.pos(1, 0, 1).tex(minU, maxV).endVertex();
		buffer.pos(1, 1, 1).tex(minU, minV).endVertex();
		buffer.pos(0, 1, 1).tex(maxU, minV).endVertex();
		buffer.pos(0, 0, 1).tex(maxU, maxV).endVertex();
		tessellator.draw();
		
		buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
		buffer.pos(1, 0, 0).tex(maxU, maxV).endVertex();
		buffer.pos(1, 0, 1).tex(maxU, minV).endVertex();
		buffer.pos(0, 0, 1).tex(minU, minV).endVertex();
		buffer.pos(0, 0, 0).tex(minU, maxV).endVertex();
		
		buffer.pos(0, 1, 1).tex(minU, maxV).endVertex();
		buffer.pos(1, 1, 1).tex(maxU, maxV).endVertex();
		buffer.pos(1, 1, 0).tex(maxU, minV).endVertex();
		buffer.pos(0, 1, 0).tex(minU, minV).endVertex();
		tessellator.draw();
				
		buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
		buffer.pos(1, 1, 0).tex(maxU, minV).endVertex();
		buffer.pos(1, 1, 1).tex(minU, minV).endVertex();
		buffer.pos(1, 0, 1).tex(minU, maxV).endVertex();
		buffer.pos(1, 0, 0).tex(maxU, maxV).endVertex();
		
		buffer.pos(0, 0, 1).tex(minU, maxV).endVertex();
		buffer.pos(0, 1, 1).tex(minU, minV).endVertex();
		buffer.pos(0, 1, 0).tex(maxU, minV).endVertex();
		buffer.pos(0, 0, 0).tex(maxU, maxV).endVertex();
		tessellator.draw();
		
		RenderHelper.enableStandardItemLighting();
		GL11.glPopMatrix();
	}

}
