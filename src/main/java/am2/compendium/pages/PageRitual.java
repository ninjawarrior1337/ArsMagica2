package am2.compendium.pages;

import java.util.ArrayList;
import java.util.Random;

import am2.gui.AMGuiHelper;
import am2.rituals.IRitualInteraction;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.oredict.OreDictionary;

@SuppressWarnings("deprecation")
public class PageRitual extends CompendiumPage<IRitualInteraction.Wrapper> {

	public PageRitual(IRitualInteraction.Wrapper element) {
		super(element);
	}

	@Override
	protected void renderPage(int posX, int posY, int mouseX, int mouseY) {
		String ritualName = TextFormatting.UNDERLINE.toString() + TextFormatting.BOLD.toString() + I18n.translateToLocal("ritual." + element.getRitualInteraction().getClass().getSimpleName().toLowerCase() + ".name");
		mc.fontRendererObj.drawString(ritualName, posX + 72 - (mc.fontRendererObj.getStringWidth(ritualName) / 2), posY, 0);
		String categoryName = TextFormatting.UNDERLINE.toString() + I18n.translateToLocal("am2.gui.ritualshape");
		mc.fontRendererObj.drawString(categoryName, posX + 72 - (mc.fontRendererObj.getStringWidth(categoryName) / 2), posY + 20, 0);
		String shapeName = I18n.translateToLocal("ritualshape." + element.getRitualInteraction().getRitualShape().getId() + ".name");
		mc.fontRendererObj.drawString(shapeName, posX + 72 - (mc.fontRendererObj.getStringWidth(shapeName) / 2), posY + 30, 0);
		
		String reagentsName = TextFormatting.UNDERLINE.toString() + I18n.translateToLocal("am2.gui.ritualreagents");
		mc.fontRendererObj.drawString(reagentsName, posX + 72 - (mc.fontRendererObj.getStringWidth(reagentsName) / 2), posY + 50, 0);
		Random randomizer = new Random(new Random(AMGuiHelper.instance.getSlowTicker()).nextLong());
		
		ItemStack[] reagents = element.getRitualInteraction().getReagents();
		int lines = (int) Math.ceil(reagents.length / 4F);
		int yOffset = 0;
		ItemStack stackTip = null;
		RenderHelper.enableGUIStandardItemLighting();
		for (int l = 0; l < lines; l++) {
			int items = (reagents.length - (l*4)) % 4;
			int xOffset = 16;
			if (items == 3)
				xOffset += 16;
			if (items == 2)
				xOffset += 32;
			if (items == 1)
				xOffset += 48;
			for (int i = 0; i < items; i++) {
				ItemStack stack = reagents[l * 4 + i];
				if (stack != null) {
					if (stack.getItemDamage() == OreDictionary.WILDCARD_VALUE) {
						ArrayList<ItemStack> subItems = new ArrayList<>();
						stack.getItem().getSubItems(stack.getItem(), stack.getItem().getCreativeTab(), subItems);
						stack = subItems.get(randomizer.nextInt(subItems.size()));
					}
					mc.getRenderItem().renderItemIntoGUI(stack, posX + xOffset, posY + yOffset + 60);
				}
				if (mouseX > posX + xOffset && mouseX < posX + xOffset + 16 && mouseY > posY + yOffset + 60 && mouseY < posY + yOffset + 76)
					stackTip = stack;
				xOffset += 32;
			}
			yOffset += 32;
		}
		RenderHelper.disableStandardItemLighting();
		if (stackTip != null)
			renderItemToolTip(stackTip, mouseX, mouseY);
	}

}
