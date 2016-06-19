package am2.gui;

import java.io.IOException;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class GuiColorTablet extends GuiScreen {
	
	int xSize = 210;
	int ySize = 210;
	private ItemStack stack;
	private GuiTextField fieldRed;
	private GuiTextField fieldGreen;
	private GuiTextField fieldBlue;
	
	public GuiColorTablet(ItemStack stack) {
		this.stack = stack;
	}
	
	@Override
	public void initGui() {
		int posX = width/2 - xSize/2;
		int posY = height/2 - ySize/2;
		buttonList.add(new GuiButton(0, posX, posY + 90, "Save"));
		fieldRed= new GuiTextField(1, fontRendererObj, posX, posY, 60, 20);
		fieldGreen = new GuiTextField(2, fontRendererObj, posX, posY + 30, 60, 20);
		fieldBlue = new GuiTextField(3, fontRendererObj, posX, posY + 60, 60, 20);
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);
		if (button.id == 0) {
			try {
				NBTTagCompound nbt = new NBTTagCompound();
				nbt.setInteger("Color", Integer.valueOf(fieldRed.getText()) + (Integer.valueOf(fieldGreen.getText()) << 8) + (Integer.valueOf(fieldBlue.getText()) << 16));
				stack.setTagCompound(nbt);
				mc.currentScreen = null;
				mc.setIngameFocus();
			} catch (NumberFormatException e) {}
		}
	}
	
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		int posX = width/2 - xSize/2;
		int posY = height/2 - ySize/2;
		drawTexturedModalRect(posY, posX, 0, 0, xSize, ySize);
	}
	
}
