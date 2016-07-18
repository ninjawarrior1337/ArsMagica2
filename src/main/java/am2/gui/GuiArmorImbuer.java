package am2.gui;

import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import am2.api.items.armor.ArmorImbuement;
import am2.api.items.armor.ImbuementTiers;
import am2.armor.ArmorHelper;
import am2.armor.infusions.ImbuementRegistry;
import am2.blocks.tileentity.TileEntityArmorImbuer;
import am2.container.ContainerArmorInfuser;
import am2.packet.AMNetHandler;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;

@SuppressWarnings("deprecation")
public class GuiArmorImbuer extends GuiContainer{

	private TileEntityArmorImbuer tileEntity;

	private static final ResourceLocation foreground = new ResourceLocation("arsmagica2", "textures/gui/ArmorUpgradeGUI.png");

	int spriteHeight = 24;
	int spriteWidth = 43;
	ResourceLocation hoveredID;

	public GuiArmorImbuer(EntityPlayer player, TileEntityArmorImbuer infuser){
		super(new ContainerArmorInfuser(player, infuser));
		this.tileEntity = infuser;
		xSize = 247;
		ySize = 250;
	}

	@Override
	protected void mouseClicked(int par1, int par2, int par3) throws IOException{
		if (hoveredID != null){
			AMNetHandler.INSTANCE.sendImbueToServer(tileEntity, hoveredID.toString());
			tileEntity.imbueCurrentArmor(hoveredID);
		}
		super.mouseClicked(par1, par2, par3);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j){
		int l = (width - xSize) / 2;
		int i1 = (height - ySize) / 2;

		ItemStack stack = tileEntity.getStackInSlot(0);

		int startX = l + 22;
		int stepX = 52;
		int startY = i1 + 23;
		int stepY = 45;

		int drawX = startX;
		int drawY = startY;

		ArrayList<String> hoverLines = new ArrayList<String>();

		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		GL11.glColor3f(0, 0, 0);
		drawTexturedModalRect(l, i1, 0, 0, xSize, ySize);

		GL11.glColor3f(1, 1, 1);
		hoveredID = null;
		if (stack != null){
			EntityEquipmentSlot armorType = null;
			if ( stack.getItem() instanceof ItemArmor) {
				armorType = ((ItemArmor)stack.getItem()).armorType;
			}

			for (ImbuementTiers tier : ImbuementTiers.values()){
				ArmorImbuement[] infusions = ImbuementRegistry.instance.getImbuementsForTier(tier, armorType);
				for (ArmorImbuement infusion : infusions){
					mc.renderEngine.bindTexture(new ResourceLocation(infusion.getRegistryName().getResourceDomain(), "textures/armorinfusions/" + infusion.getRegistryName().getResourcePath() + ".png"));
					drawInfusionIconAt(drawX, drawY, false);

					if (i >= drawX && i <= drawX + spriteWidth){
						if (j >= drawY && j <= drawY + spriteHeight){
							hoverLines.add(I18n.translateToLocal("am2.tooltip." + infusion.getID()));
						}
					}

					drawX += stepX;
				}
				drawY += stepY;
				drawX = startX;
			}

			drawX = startX;
			drawY = startY;

			int highestSelectedTier = 0;
			for (ImbuementTiers tier : ImbuementTiers.values()){
				ArmorImbuement[] infusions = ImbuementRegistry.instance.getImbuementsForTier(tier, armorType);
				ArmorImbuement[] existingInfusions = ArmorHelper.getInfusionsOnArmor(stack);
				ArmorImbuement tierInfusion = null;

				for (ArmorImbuement infusion : existingInfusions){
					if (infusion == null) continue;
					if (infusion.getTier() == tier){
						tierInfusion = infusion;
						if (tier.ordinal() >= highestSelectedTier)
							highestSelectedTier = tier.ordinal() + 1;
						break;
					}
				}
				for (ArmorImbuement infusion : infusions){
					mc.renderEngine.bindTexture(new ResourceLocation(infusion.getRegistryName().getResourceDomain(), "textures/armorinfusions/" + infusion.getRegistryName().getResourcePath() + ".png"));
					if ((tierInfusion == null && infusion.getTier().ordinal() <= highestSelectedTier)){
						if (tileEntity.isCreativeAllowed() || ArmorHelper.getArmorLevel(stack) >= ArmorHelper.getImbueCost(tier)){
							drawInfusionIconAt(drawX, drawY, true);
							if (i >= drawX && i <= drawX + spriteWidth){
								if (j >= drawY && j <= drawY + spriteHeight){
									hoveredID = infusion.getRegistryName();
								}
							}
						}
					}else if (tierInfusion == infusion){
						drawInfusionIconAt(drawX, drawY, true);
					}

					drawX += stepX;
				}
				drawY += stepY;
				drawX = startX;
			}
		}

		mc.renderEngine.bindTexture(foreground);
		drawTexturedModalRect(l, i1, 0, 0, xSize, ySize);

		if (hoverLines.size() > 0)
			AMGuiHelper.drawHoveringText(hoverLines, i, j, fontRendererObj, width, height);
	}

	private void drawInfusionIconAt(int x, int y, boolean active){
		drawTexturedModalRect(x, y, 0, active ? 0 : spriteHeight, spriteWidth, spriteHeight);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2){

	}

}
