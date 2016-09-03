package am2.blocks.render;

import org.lwjgl.opengl.GL11;

import am2.blocks.tileentity.TileEntityOtherworldAura;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

public class TileOtherworldAuraRenderer extends TileEntitySpecialRenderer<TileEntityOtherworldAura>{

	private ResourceLocation rLoc_aura;

	public TileOtherworldAuraRenderer(){
		rLoc_aura = new ResourceLocation("arsmagica2", "textures/blocks/custom/sc_auto.png");
	}

	public void renderTileEntityAt(TileEntityOtherworldAura tile, double d1, double d2, double d3, float f, int destroyStage){
		GL11.glPushMatrix();
		GL11.glPushAttrib(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_TEXTURE_BIT);

		GL11.glTranslatef((float)d1 + 0.5f, (float)d2 + 0.5f, (float)d3 + 0.5f);
		GL11.glDepthMask(false);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);


		RenderHelper.disableStandardItemLighting();

		Tessellator tessellator = Tessellator.getInstance();

		float n = 1.25f;
		float offset = 1;
		while (n > 0){
			renderArsMagicaEffect(tessellator, offset, n, f, destroyStage != -10);
			n -= 0.25f;
			offset *= -1;
		}

		RenderHelper.enableStandardItemLighting();
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDepthMask(true);
		GL11.glPopAttrib();
		GL11.glPopMatrix();
	}


	private void renderArsMagicaEffect(Tessellator tessellator, float offset, float scale, float smooth, boolean rotate){
		GL11.glPushMatrix();
		if (rotate){
			GL11.glRotatef(180F - Minecraft.getMinecraft().getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(-Minecraft.getMinecraft().getRenderManager().playerViewX, 1.0F, 0.0F, 0.0F);
		}else{
			GL11.glRotatef(35, 0, 1, 0);
			GL11.glTranslatef(0, -0.25f, 0);
		}

		Minecraft.getMinecraft().renderEngine.bindTexture(rLoc_aura);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(1, 0, 0.75f, 1);
		GL11.glTranslatef(0.0f, 0.25f, 0.0f);
		GL11.glRotatef((Minecraft.getMinecraft().thePlayer.ticksExisted + smooth) * (scale * 2) * offset, 0, 0, 1);
		float scalefactor = (float)Math.abs(Math.sin(System.currentTimeMillis() / 1000.0 * scale)) + 0.01f;
		GL11.glScalef(scale * scalefactor, scale * scalefactor, scale * scalefactor);
		GL11.glTranslatef(0.0f, -0.25f, 0.0f);
		renderSprite(tessellator);
		GL11.glPopMatrix();

	}

	private void renderSprite(Tessellator tessellator){

		float TLX = 0;
		float BRX = 1;
		float TLY = 0;
		float BRY = 1;

		float f4 = 1.0F;
		float f5 = 0.5F;
		float f6 = 0.25F;

		try{
			tessellator.getBuffer().begin(7, DefaultVertexFormats.POSITION_TEX);
			tessellator.getBuffer().pos(0.0F - f5, 0.0F - f6, 0.0D).tex(TLX, BRY).endVertex();
			tessellator.getBuffer().pos(f4 - f5, 0.0F - f6, 0.0D).tex(BRX, BRY).endVertex();
			tessellator.getBuffer().pos(f4 - f5, f4 - f6, 0.0D).tex(BRX, TLY).endVertex();
			tessellator.getBuffer().pos(0.0F - f5, f4 - f6, 0.0D).tex(TLX, TLY).endVertex();;
			tessellator.draw();
		}catch (Throwable t){
		}
	}
}
