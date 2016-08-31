package am2.compendium.pages;

import java.util.ArrayList;

import am2.gui.AMGuiHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;

public class PageItemStack extends CompendiumPage<ItemStack> {

	public PageItemStack(ItemStack element) {
		super(element);
	}

	@Override
	protected void renderPage(int posX, int posY, int mouseX, int mouseY) {	
		int cx = posX + 60;
		int cy = posY + 92;
		RenderHelper.disableStandardItemLighting();
		AMGuiHelper.DrawItemAtXY(element, cx, cy, 0);
		if (mouseX > cx && mouseX < cx + 16){
			if (mouseY > cy && mouseY < cy + 16){
				ArrayList<String> tooltip = new ArrayList<>();
				tooltip.addAll(element.getTooltip(Minecraft.getMinecraft().thePlayer, false));
				drawHoveringText(tooltip, mouseX, mouseY, mc.fontRendererObj);
			}
		}
		GlStateManager.enableAlpha();
		RenderHelper.enableStandardItemLighting();
	}

}
