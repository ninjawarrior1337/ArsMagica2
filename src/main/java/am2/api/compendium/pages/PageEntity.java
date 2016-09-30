package am2.api.compendium.pages;

import java.io.IOException;

import am2.bosses.AM2Boss;
import am2.entity.EntityFlicker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;

public class PageEntity extends CompendiumPage<Entity> {
	
	private Render<Entity> renderer;
	private float curRotationH = 0;
	private int lastMouseX = 0;
	private boolean isDragging;

	@SuppressWarnings("unchecked")
	public PageEntity(Entity element) {
		super(element);
		renderer = (Render<Entity>) Minecraft.getMinecraft().getRenderManager().entityRenderMap.get(element.getClass());
	}

	@Override
	protected void renderPage(int posX, int posY, int mouseX, int mouseY) {
		if (renderer == null)
			return;
		int cx = posX + 60;
		int cy = posY + 92;
		
		GlStateManager.pushMatrix();
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		GlStateManager.translate((float)(cx - 2), (float)(cy + 20), -3.0F + Minecraft.getMinecraft().getRenderItem().zLevel);
		GlStateManager.scale(10.0F, 10.0F, 10.0F);
		GlStateManager.translate(1.0F, 6.5F, 1.0F);
		GlStateManager.scale(6.0F, 6.0F, -1.0F);
		GlStateManager.rotate(210.0F, 1.0F, 0.0F, 0.0F);
		GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(-90.0F, 0.0F, 1.0F, 0.0F);

		{
			GlStateManager.pushMatrix();
			if (element instanceof AM2Boss){
				float scaleFactorX = (1 / element.width);
				float scaleFactorY = (2 / element.height);
				GlStateManager.scale(scaleFactorX, scaleFactorY, scaleFactorX);
			}else if (element instanceof EntityFlicker){
				GlStateManager.translate(0, 1.3f, 0);
			}
			GlStateManager.rotate(curRotationH, 0, 1, 0);

			//entity, x, y, z, yaw, partialtick
			renderer.doRender(element, 0.0, 0.0, 0.0, 90.0F, 0.0F);

			GlStateManager.popMatrix();
		}
		GlStateManager.popMatrix();

		String renderString = "Click and drag to rotate";
		mc.fontRendererObj.drawString(renderString, cx - mc.fontRendererObj.getStringWidth(renderString) / 2, posY + 20, 0x000000);
	}
	
	@Override
	public void dragMouse(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
		if (isDragging) {
			curRotationH -= (lastMouseX - mouseX);
			lastMouseX = mouseX;
		}
	}
	
	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		isDragging = true;
		lastMouseX = mouseX;
	}
	
	@Override
	public void mouseReleased(int mouseX, int mouseY, int state) {
		if (state == 1)
			isDragging = false;
	}
}
