package am2.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import am2.api.ArsMagicaAPI;
import am2.api.SkillPointRegistry;
import am2.api.SkillRegistry;
import am2.api.SkillTreeRegistry;
import am2.api.affinity.AbstractAffinityAbility;
import am2.api.affinity.Affinity;
import am2.api.extensions.ISkillData;
import am2.api.skill.Skill;
import am2.api.skill.SkillPoint;
import am2.api.skill.SkillTree;
import am2.defs.ItemDefs;
import am2.defs.SkillDefs;
import am2.extensions.AffinityData;
import am2.extensions.SkillData;
import am2.gui.controls.GuiButtonSkillTree;
import am2.lore.ArcaneCompendium;
import am2.texture.SpellIconManager;
import am2.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;

@SuppressWarnings("deprecation")
public class GuiOcculus extends GuiScreen {
	int xSize = 210;
	int ySize = 210;
	SkillTree currentTree = SkillDefs.TREE_OFFENSE;
	EntityPlayer player;
	int currentTabId = 0;
	
	private boolean isDragging = false;
	private int lastMouseX = 0;
	private int lastMouseY = 0;
	private int offsetX = 568 / 2 - 82 + 8;
	private int offsetY = 0;
	private Skill hoverItem = null;
	
	public GuiOcculus(EntityPlayer player) {
		this.player = player;
	}
	
	@Override
	public void initGui() {
		int tabId = 0;
		int posX = width/2 - xSize/2;
		int posY = height/2 - ySize/2;
		ImmutableList<SkillTree> testTab = SkillTreeRegistry.getSkillTreeMap();
		for (SkillTree entry : testTab) {
			if (tabId < 7)
				buttonList.add(new GuiButtonSkillTree(tabId, posX + 7 + (tabId * 24), posY - 22, entry, false));
			else 
				buttonList.add(new GuiButtonSkillTree(tabId, posX + 7 + ((tabId - 7) * 24), posY + 210, entry, true));
				
			tabId++;
		}
		super.initGui();
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		if (mouseButton == 0) {
			if (hoverItem != null && !SkillData.For(player).hasSkill(hoverItem.getID())) {
				ISkillData data = SkillData.For(player);
				if (data.canLearn(hoverItem.getID())) {
					data.unlockSkill(hoverItem.getID());
					ArcaneCompendium.For(player).unlockEntry(hoverItem.getID());
				}
			}
			else if (this.currentTree != SkillDefs.TREE_AFFINITY)
				isDragging = true;
		}
	}
	
	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		super.mouseReleased(mouseX, mouseY, state);
		isDragging = false;
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);
		currentTree = ((GuiButtonSkillTree)button).getTree();
		currentTabId = button.id;
		offsetX = 568 / 2 - 82 + 8;
		offsetY = 0;
	}
	
	private int calcXOffset(int posX, Skill s) {
		return (int) (posX - this.offsetX + s.getPosX());
	}
	
	private int calcYOffset(int posY, Skill s) {
		return (int) (posY - this.offsetY + s.getPosY());
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		int posX = width/2 - xSize/2;
		int posY = height/2 - ySize/2;
		float renderSize = 32F;
		float renderRatio = 0.29F;
		Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation("arsmagica2", "textures/occulus/overlay.png"));
		//Overlay
		drawTexturedModalRect(posX, posY, 0, 0, 210, 210);
		drawTexturedModalRect(posX + 188, posY - 22, 210, 0, 22, 22);
        float f = 0.00390625F;
		if (SkillPointRegistry.getPointForTier(3) != null || SkillPointRegistry.getPointForTier(4) != null || SkillPointRegistry.getPointForTier(5) != null)
			RenderUtils.drawBox(posX + 188, posY + 210, 22, 22, -90F, 232 * f, 22 * f, 210 * f, 0 * f);
		//Tab Under
		if (currentTabId < 8)
			drawTexturedModalRect(posX + 7 + (currentTabId * 24), posY, 22, 210, 22, 7);
		else 
			drawTexturedModalRect(posX + 7 + ((currentTabId - 7) * 24), posY + 203, 22, 210, 22, 7);
		zLevel = -18F;
		if (isDragging){
			int dx = lastMouseX - mouseX;
			int dy = lastMouseY - mouseY;

			this.offsetX += dx;
			this.offsetY += dy;

			if (this.offsetX < 0) this.offsetX = 0;
			if (this.offsetX > 568) this.offsetX = 568;

			if (this.offsetY < 0) this.offsetY = 0;
			if (this.offsetY > 568) this.offsetY = 568;
		}
		lastMouseX = mouseX;
		lastMouseY = mouseY;
		float calcYOffest = ((float)offsetY / 568) * (1 - renderRatio);
		float calcXOffest = ((float)offsetX / 568) * (1 - renderRatio);
		Minecraft.getMinecraft().renderEngine.bindTexture(currentTree.getBackground());
		if (currentTree != SkillDefs.TREE_AFFINITY) {
			RenderUtils.drawBox(posX + 7, posY + 7, 196, 196, zLevel, calcXOffest, calcYOffest, renderRatio + calcXOffest, renderRatio + calcYOffest);
			ArrayList<Skill> skills = SkillRegistry.getSkillsForTree(currentTree);
			zLevel = 1F;
			ISkillData data = SkillData.For(player);
			for (Skill s : skills) {
				if (!s.getPoint().canRender() && !data.hasSkill(s.getID()))
					continue;
				for (String p : s.getParents()) {
		        	Skill parent = SkillRegistry.getSkillFromName(p);
		        	if (parent == null || !skills.contains(parent)) continue;
					if (!parent.getPoint().canRender() && !data.hasSkill(parent.getID()))
						continue;
					int offsetX = calcXOffset(posX, s) + 16;
					int offsetY = calcYOffset(posY, s) + 16;
					int offsetX2 = calcXOffset(posX, parent) + 16;
					int offsetY2 = calcYOffset(posY, parent) + 16;
			        offsetX = MathHelper.clamp_int(offsetX, posX + 7, posX + 203);
					offsetY = MathHelper.clamp_int(offsetY, posY + 7, posY + 203);
					offsetX2 = MathHelper.clamp_int(offsetX2, posX + 7, posX + 203);
					offsetY2 = MathHelper.clamp_int(offsetY2, posY + 7, posY + 203);
					boolean hasPrereq = data.canLearn(s.getID());
					int color = (!SkillData.For(player).hasSkill(s.getID()) ? s.getPoint().getColor() & 0x999999 : 0x00ff00);
					if (!hasPrereq) color = 0x000000;
					if (!(offsetX == posX + 7 || offsetX == posX + 203))
						RenderUtils.lineThick2d(offsetX, offsetY, offsetX, offsetY2, hasPrereq ? 0 : -1, color);
					if (!(offsetY2 == posY + 7 || offsetY2 == posY + 203))
						RenderUtils.lineThick2d(offsetX, offsetY2, offsetX2, offsetY2, hasPrereq ? 0 : -1, color);
				}
			}
			Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			for (Skill s : skills) {
				if (!s.getPoint().canRender() && !data.hasSkill(s.getID()))
					continue;
				GlStateManager.color(1, 1, 1, 1.0F);
				ISkillData skillData = SkillData.For(player);
				boolean hasPrereq = skillData.canLearn(s.getID());
				int offsetX = calcXOffset(posX, s);
				int offsetY = calcYOffset(posY, s);
				int tick = (player.ticksExisted % 80) >= 40 ? (player.ticksExisted % 40) - 20 : -(player.ticksExisted % 40) + 20;
				float multiplier = 0.75F + tick / 80F;
				TextureAtlasSprite sprite = SpellIconManager.INSTANCE.getSprite(s.getID());
				if (offsetX + renderSize < posX + 7 || offsetX > posX + 203 || offsetY + renderSize < posY + 7 || offsetY > posY + 203 || sprite == null) {
					continue;
				}
				float spriteXSize = sprite.getMaxU() - sprite.getMinU();
				float spriteYSize = sprite.getMaxV() - sprite.getMinV();
				float xStartMod = 0;
				float yStartMod = 0;
				float xEndMod = 0;
				float yEndMod = 0;
				if (offsetX < posX + 7) {
					float mod = (posX + 7 - offsetX);
					xStartMod = mod;
				}
				else if (offsetX + renderSize > posX + 203) {
					float mod = renderSize - (posX + 203 - offsetX);
					xEndMod = mod;
				}
				if (offsetY < posY + 7) {
					float mod = (posY + 7 - offsetY);
					yStartMod = mod;
				}
				else if (offsetY + renderSize > posY + 203) {
					float mod = renderSize - (posY + 203 - offsetY);
					yEndMod = mod;
				}
				if (!hasPrereq)
					GlStateManager.color(0.1F, 0.1F, 0.1F);
				else if (!skillData.hasSkill(s.getID()))
					GlStateManager.color(Math.max(RenderUtils.getRed(s.getPoint().getColor()), 0.6F) * multiplier, Math.max(RenderUtils.getGreen(s.getPoint().getColor()), 0.6F) * multiplier, Math.max(RenderUtils.getBlue(s.getPoint().getColor()), 0.6F) * multiplier);
				RenderUtils.drawBox(offsetX + xStartMod,
						offsetY + yStartMod,
						renderSize - xStartMod - xEndMod,
						renderSize - yStartMod - yEndMod,
						0,
						sprite.getMinU() + (xStartMod / renderSize * spriteXSize),
						sprite.getMinV() + (yStartMod / renderSize * spriteYSize),
						sprite.getMaxU() - (xEndMod / renderSize * spriteXSize),
						sprite.getMaxV() - (yEndMod / renderSize * spriteYSize));
				GlStateManager.color(1, 1, 1, 1.0F);
				
			}
			
			//Get the skill
			
			if (mouseX > posX && mouseX < posX + 210 && mouseY > posY && mouseY < posY + 210) {
				boolean flag = false;
				zLevel = 0F;
				for (Skill s : skills) {
					if (!s.getPoint().canRender() && !data.hasSkill(s.getID()))
						continue;
					int offsetX = calcXOffset(posX, s);
					int offsetY = calcYOffset(posY, s);
					if (offsetX > mouseX || offsetX < mouseX - renderSize|| offsetY > mouseY || offsetY < mouseY - renderSize)
						continue;
					boolean hasPrereq = true;
					for (String subParent : s.getParents()) {
						hasPrereq &= data.hasSkill(subParent);
					}
					ArrayList<String> list = new ArrayList<String>();
					list.add(s.getPoint().getChatColor().toString() + I18n.translateToLocal(s.getName()));
					if (hasPrereq)
						list.add(TextFormatting.DARK_GRAY.toString() + I18n.translateToLocal(s.getOcculusDesc()));
					else
						list.add(TextFormatting.DARK_RED.toString() + I18n.translateToLocal("occulus.missingrequirements"));
					
					drawHoveringText(list, mouseX, mouseY, Minecraft.getMinecraft().fontRendererObj);
					flag = true;
					hoverItem = s;
		            RenderHelper.disableStandardItemLighting();
		            GlStateManager.color(1.0F, 1.0F, 1.0F);
				}
				if (!flag)
					hoverItem = null;
			}
			
		} else {
			RenderUtils.drawBox(posX + 7, posY + 7, 196, 196, zLevel, 0, 0, 1, 1);
			int affNum = ArsMagicaAPI.getAffinityRegistry().getValues().size() - 1;
			int portion = 360 / affNum;
			int currentID = 0;
			int cX = posX + xSize/2;
			int cY = posY + ySize/2;
			//float finalPercentage = AffinityData.For(player).getAffinityDepth(SkillDefs.NONE) * 100;
			ArrayList<String> drawString = new ArrayList<>();
			for (Affinity aff : ArsMagicaAPI.getAffinityRegistry().getValues()) {
				if (aff == Affinity.NONE)
					continue;
				float depth = AffinityData.For(player).getAffinityDepth(aff);
				double affEndX = Math.cos(Math.toRadians(portion*currentID)) * 10F + Math.cos(Math.toRadians(portion*currentID)) * depth * 60F;
				double affEndY = Math.sin(Math.toRadians(portion*currentID)) * 10F + (Math.sin(Math.toRadians(portion*currentID))) * depth * 60F;
				double affStartX1 = Math.cos(Math.toRadians(portion*currentID - portion/2)) * 10F;
				double affStartY1 = Math.sin(Math.toRadians(portion*currentID - portion/2)) * 10F;
				double affStartX2 = Math.cos(Math.toRadians(portion*currentID + portion/2)) * 10F;
				double affStartY2 = Math.sin(Math.toRadians(portion*currentID + portion/2)) * 10F;
				double affDrawTextX =  Math.cos(Math.toRadians(portion*currentID)) * 80F - 7;
				double affDrawTextY =  Math.sin(Math.toRadians(portion*currentID)) * 80F - 7;
				currentID++;
				
				int displace = (int)((Math.max(affStartX1, affStartX2) - Math.min(affStartX1, affStartX2) + Math.max(affStartY1, affStartY2) - Math.min(affStartY1, affStartY2)) / 2);
				if (depth > 0.01F) {
					RenderUtils.fractalLine2dd(affStartX1 + cX, affStartY1 + cY, affEndX + cX, affEndY + cY, zLevel, aff.getColor(), displace, 0.8F);
					RenderUtils.fractalLine2dd(affStartX2 + cX, affStartY2 + cY, affEndX + cX, affEndY + cY, zLevel, aff.getColor(), displace, 0.8F);
				
					RenderUtils.fractalLine2dd(affStartX1 + cX, affStartY1 + cY, affEndX + cX, affEndY + cY, zLevel, aff.getColor(), displace, 1.1F);
					RenderUtils.fractalLine2dd(affStartX2 + cX, affStartY2 + cY, affEndX + cX, affEndY + cY, zLevel, aff.getColor(), displace, 1.1F);
				} else {
					RenderUtils.line2d((float)affStartX1 + cX, (float)affStartY1 + cY, (float)affEndX + cX, (float)affEndY + cY, zLevel, aff.getColor());
					RenderUtils.line2d((float)affStartX2 + cX, (float)affStartY2 + cY, (float)affEndX + cX, (float)affEndY + cY, zLevel, aff.getColor());
				}
				
				Minecraft.getMinecraft().fontRendererObj.drawString("" + (float)Math.round(depth * 10000) / 100F, (int)((affDrawTextX *0.9) + cX), (int)((affDrawTextY*0.9) + cY), aff.getColor());
				//Minecraft.getMinecraft().fontRendererObj.drawString("" + (float)Math.round(depth * 10000) / 100F, , aff.getColor());
				int xMovement = affDrawTextX > 0 ? 5 : -5;
				xMovement = affDrawTextX == 0 ? 0 : xMovement;
				int yMovement = affDrawTextY > 0 ? 5 : -5;
				yMovement = affDrawTextY == 0 ? 0 : yMovement;
				int drawX = (int)((affDrawTextX * 1.1) + cX + xMovement);
				int drawY = (int)((affDrawTextY * 1.1) + cY + yMovement);
				this.itemRender.renderItemAndEffectIntoGUI(new ItemStack(ItemDefs.essence, 1, ArsMagicaAPI.getAffinityRegistry().getId(aff)) , drawX, drawY);
				if (mouseX > drawX && mouseX < drawX + 16 && mouseY > drawY && mouseY < drawY + 16) {
					drawString.add(TextFormatting.RESET.toString() + aff.getLocalizedName());
					ArrayList<AbstractAffinityAbility> abilites = Lists.newArrayList(ArsMagicaAPI.getAffinityAbilityRegistry().getValues());
					abilites.sort(new Comparator<AbstractAffinityAbility>() {

						@Override
						public int compare(AbstractAffinityAbility o1, AbstractAffinityAbility o2) {
							return (int) ((o1.getMinimumDepth() * 100) - (o2.getMinimumDepth() * 100));
						}
					});
					for (AbstractAffinityAbility ability : abilites) {
						if (ability.getAffinity() == aff) {
							drawString.add(TextFormatting.RESET.toString()
									+ (ability.canApply(player) ? TextFormatting.GREEN.toString()
											: TextFormatting.DARK_RED.toString())
									+ I18n.translateToLocal("affinityability."
											+ ability.getRegistryName().toString().replaceAll("arsmagica2:", "")
											+ ".name"));
						}
					}
				}
				GlStateManager.color(1, 1, 1);
			}
			drawHoveringText(drawString, mouseX, mouseY);
			RenderHelper.disableStandardItemLighting();
		}
		
		int tier0 = SkillData.For(player).getSkillPoint(SkillPoint.SKILL_POINT_1);
		int tier1 = SkillData.For(player).getSkillPoint(SkillPoint.SKILL_POINT_2);
		int tier2 = SkillData.For(player).getSkillPoint(SkillPoint.SKILL_POINT_3);
		int tier3 = SkillData.For(player).getSkillPoint(SkillPoint.SKILL_POINT_4);
		int tier4 = SkillData.For(player).getSkillPoint(SkillPoint.SKILL_POINT_5);
		int tier5 = SkillData.For(player).getSkillPoint(SkillPoint.SKILL_POINT_6);
		GlStateManager.disableDepth();
		fontRendererObj.drawString("" + tier0, posX + 191, posY - 19, SkillPointRegistry.getPointForTier(0).getColor());
		fontRendererObj.drawString("" + tier1, posX + 203, posY - 19, SkillPointRegistry.getPointForTier(1).getColor());
		fontRendererObj.drawString("" + tier2, posX + 197, posY - 9, SkillPointRegistry.getPointForTier(2).getColor());
		if (SkillPointRegistry.getPointForTier(3) != null)
			fontRendererObj.drawString("" + tier3, posX + 191, posY + 210 + 2, SkillPointRegistry.getPointForTier(3).getColor());
		if (SkillPointRegistry.getPointForTier(4) != null)
			fontRendererObj.drawString("" + tier4, posX + 203, posY + 210 + 2, SkillPointRegistry.getPointForTier(4).getColor());
		if (SkillPointRegistry.getPointForTier(5) != null)
			fontRendererObj.drawString("" + tier5, posX + 197, posY + 210 + 12, SkillPointRegistry.getPointForTier(5).getColor());
		
		GlStateManager.color(1, 1, 1);

		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	
	
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
	
}
