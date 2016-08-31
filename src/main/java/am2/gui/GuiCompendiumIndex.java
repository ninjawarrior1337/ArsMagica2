package am2.gui;

import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.ImmutableList;

import am2.api.extensions.ISkillData;
import am2.compendium.CompendiumCategory;
import am2.extensions.SkillData;
import am2.gui.controls.GuiButtonCompendiumNext;
import am2.gui.controls.GuiButtonCompendiumTab;
import am2.gui.controls.GuiSpellImageButton;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

public class GuiCompendiumIndex extends GuiScreen{
	private CompendiumCategory currentCategory;
	private final ImmutableList<CompendiumCategory> categories;

	int xSize = 360;
	int ySize = 256;

	int page = 0;
	
	ArrayList<String> lines;
	int lineWidth = 140;
	int maxLines = 22;

	GuiButtonCompendiumNext nextPage;
	GuiButtonCompendiumNext prevPage;
	GuiButtonCompendiumTab backToIndex;

	GuiSpellImageButton updateButton;

	ISkillData sk;

	private static final ResourceLocation background = new ResourceLocation("arsmagica2", "textures/gui/ArcaneCompendiumIndexGui.png");

	public GuiCompendiumIndex(){
		categories = CompendiumCategory.getCategories();
		currentCategory = categories.iterator().next();
		lines = new ArrayList<String>();

		sk = SkillData.For(Minecraft.getMinecraft().thePlayer);
	}

	@Override
	public void initGui(){
		int idCount = 0;
		int posX = (width - xSize) / 2;
		int posY = (height - ySize) / 2;
		int tabY = posY + 40;
		int tabWidth = 1;
		for (CompendiumCategory category : categories) {
			GuiButtonCompendiumTab tab = new GuiButtonCompendiumTab(idCount++, posX + 10, tabY, category);
			if (category == currentCategory)
				tab.setActive(true);
			buttonList.add(tab);
			if (tabWidth < tab.getWidth())
				tabWidth = tab.getWidth();
			tabY += 18;
		}
		for (GuiButton button : buttonList) {
			if (button instanceof GuiButtonCompendiumTab)
				((GuiButtonCompendiumTab)button).setDimensions(tabWidth, 16);
		}
		
		super.initGui();
	}


	@Override
	protected void mouseClicked(int par1, int par2, int par3) throws IOException{
		for (int l = 0; l < this.buttonList.size(); ++l){
			GuiButton guibutton = (GuiButton)this.buttonList.get(l);

			if (guibutton.mousePressed(Minecraft.getMinecraft(), par1, par2)){
//				if (guibutton.id == updateButton.id){
//					this.storeBreadcrumb();
//					if (par3 == 0){ //left click
//						this.mc.displayGuiScreen(new GuiConfirmOpenLink(this, ArcaneCompendium.instance.getModDownloadLink(), 0, false));
//					}else if (par3 == 1){ //right click
//						this.mc.displayGuiScreen(new GuiConfirmOpenLink(this, ArcaneCompendium.instance.getPatchNotesLink(), 1, false));
//					}
//				}
				if (par3 == 0){
					this.actionPerformed(guibutton);
					return;
				}
			}
		}
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);
	}
	
	@Override
	public boolean doesGuiPauseGame(){
		return false;
	}

	@Override
	protected void keyTyped(char par1, int par2) throws IOException{
		if (par2 == 1){
			onGuiClosed();
		}
		super.keyTyped(par1, par2);
	}

	@Override
	public void drawScreen(int par1, int par2, float par3){

		this.drawDefaultBackground();

		RenderHelper.enableGUIStandardItemLighting();

		int l = (width - xSize) / 2;
		int i1 = (height - ySize) / 2;

		mc.renderEngine.bindTexture(background);
		GL11.glColor3f(1.0f, 1.0f, 1.0f);
		this.drawTexturedModalRect_Classic(l, i1, 0, 0, xSize, ySize, 256, 240);

		String compendiumTitle = "\247nArcane Compendium";

		int y_start_title = i1 + 20;
		int x_start_title = l + 100 - (fontRendererObj.getStringWidth(compendiumTitle) / 2);

		fontRendererObj.drawString(compendiumTitle, x_start_title, y_start_title, 0);

		int x_start_line = l + 35;
		int y_start_line = i1 + 35;

		if (lines != null && lines.size() > page){
			AMGuiHelper.drawCompendiumText(lines.get(page), x_start_line, y_start_line, lineWidth, 0x000000, fontRendererObj);
		}

		super.drawScreen(par1, par2, par3);
	}

	public void drawTexturedModalRect_Classic(int dst_x, int dst_y, int src_x, int src_y, int dst_width, int dst_height, int src_width, int src_height){
		float var7 = 0.00390625F;
		float var8 = 0.00390625F;

		Tessellator var9 = Tessellator.getInstance();
		var9.getBuffer().begin(7, DefaultVertexFormats.POSITION_TEX);
		var9.getBuffer().pos(dst_x + 0, dst_y + dst_height, this.zLevel).tex((src_x + 0) * var7, (src_y + src_height) * var8).endVertex();;
		var9.getBuffer().pos(dst_x + dst_width, dst_y + dst_height, this.zLevel).tex((src_x + src_width) * var7, (src_y + src_height) * var8).endVertex();;
		var9.getBuffer().pos(dst_x + dst_width, dst_y + 0, this.zLevel).tex((src_x + src_width) * var7, (src_y + 0) * var8).endVertex();;
		var9.getBuffer().pos(dst_x + 0, dst_y + 0, this.zLevel).tex((src_x + 0) * var7, (src_y + 0) * var8).endVertex();;
		var9.draw();
	}
}
