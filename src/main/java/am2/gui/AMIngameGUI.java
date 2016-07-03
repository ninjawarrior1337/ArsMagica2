package am2.gui;

import java.util.ArrayList;
import java.util.Comparator;

import org.lwjgl.opengl.GL11;

import am2.ArsMagica2;
import am2.affinity.Affinity;
import am2.api.SpellRegistry;
import am2.api.extensions.IAffinityData;
import am2.api.extensions.IEntityExtension;
import am2.api.math.AMVector2;
import am2.defs.ItemDefs;
import am2.defs.SkillDefs;
import am2.extensions.AffinityData;
import am2.extensions.EntityExtension;
import am2.items.ItemSpellBook;
import am2.spell.ContingencyType;
import am2.spell.ISpellPart;
import am2.texture.SpellIconManager;
import am2.utils.AffinityShiftUtils;
import am2.utils.SpellUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;

@SuppressWarnings("deprecation")
public class AMIngameGUI{
	private final Minecraft mc;
//	private final RenderItem itemRenderer;
	private float zLevel;

	private static final short MANA_BAR_FLASH_SLOT = 4;
//	private final PotionEffectDurationComparator durationComparator = new PotionEffectDurationComparator();

//	private static final ResourceLocation inv_top = new ResourceLocation("arsmagica2", "textures/gui/Inventory_Top.png");
	private static final ResourceLocation mc_gui = new ResourceLocation("textures/gui/icons.png");
//	private static final ResourceLocation spellbook_ui = new ResourceLocation("arsmagica2", "textures/gui/spellbook_ui.png");
//	private static final ResourceLocation inventory = new ResourceLocation("textures/gui/container/inventory.png");

	public AMIngameGUI(){
		mc = Minecraft.getMinecraft();
//		itemRenderer = mc.getRenderItem();
	}

	public void renderGameOverlay(){
//		ItemStack ci = Minecraft.getMinecraft().thePlayer.getHeldItem(EnumHand.MAIN_HAND);

		boolean drawAMHud = !ArsMagica2.config.showHudMinimally() ;//TODO || (ci != null && (ci.getItem() == ItemDefs.spellBook || ci.getItem() == ItemDefs.spell || ci.getItem() == ItemDefs.arcaneSpellbook || ci.getItem() instanceof IBoundItem));
		ScaledResolution scaledresolution = new ScaledResolution(mc);
		int i = scaledresolution.getScaledWidth();
		int j = scaledresolution.getScaledHeight();

		GL11.glPushAttrib(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_TEXTURE_BIT);

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
//		if (drawAMHud)
//			RenderBuffs(i, j);
		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		if (drawAMHud)
			RenderContingency(i, j);
		if (drawAMHud)
			RenderArsMagicaGUIItems(i, j, mc.fontRendererObj);
		if (drawAMHud)
			RenderAffinity(i, j);
		RenderArmorStatus(i, j, mc, mc.fontRendererObj);
		if (drawAMHud)
			RenderMagicXP(i, j);
//		ItemStack item = mc.thePlayer.getHeldItem(EnumHand.MAIN_HAND);
//		if (item != null && item.getItem() instanceof ItemSpellBook){
//
//			RenderSpellBookUI(i, j, mc.fontRendererObj, mc.thePlayer.getHeldItem(EnumHand.MAIN_HAND));
//		}

		GL11.glPopAttrib();
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GlStateManager.disableAlpha();
		GL11.glColor4f(1, 1, 1, 1);
	}

	private void RenderArsMagicaGUIItems(int i, int j, FontRenderer fontRenderer){
		if (EntityExtension.For(mc.thePlayer).getCurrentLevel() > 0 || mc.thePlayer.capabilities.isCreativeMode){
			RenderManaBar(i, j, fontRenderer);
		}
	}

//	private void RenderSpellBookUI(int i, int j, FontRenderer fontrenderer, ItemStack bookStack){
//		mc.renderEngine.bindTexture(spellbook_ui);
//
//		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
//
//		AMVector2 spellbookVec = getShiftedVector(ArsMagica2.config.getSpellBookPosition(), i, j);
//
//		int spellUI_x = spellbookVec.iX;
//		int spellUI_y = spellbookVec.iY;
//		int spellUI_width = 148;
//		int spellUI_height = 22;
//		float activeSpellSize = 15f;
//
//		int bookActiveSlot = ((ItemSpellBook)bookStack.getItem()).GetActiveSlot(bookStack);
//
//		float x = spellUI_x + bookActiveSlot * 12.9f;
//		float y = spellUI_y;
//
//		this.zLevel = -5;
//		drawTexturedModalRect_Classic(spellUI_x, spellUI_y, 0, 0, 106, 15, spellUI_width, spellUI_height);
//
//		ItemStack[] activeScrolls = ((ItemSpellBook)bookStack.getItem()).getActiveScrollInventory(bookStack);
//
//		mc.renderEngine.bindTexture(items);
//
//		this.zLevel = 0;
//		for (int n = 0; n < 8; ++n){
//			float IIconX = spellUI_x + 1.5f + n * 12.9f;
//			ItemStack stackItem = activeScrolls[n];
//			if (stackItem == null){
//				continue;
//			}
//			int d = 12;
//			DrawIconAtXY(((ItemSpellBase)stackItem.getItem()).getIconFromDamageForRenderPass(stackItem.getItemDamage(), 0), "items", IIconX, spellUI_y + 1.5f, d, d, false);
//			DrawIconAtXY(((ItemSpellBase)stackItem.getItem()).getIconFromDamageForRenderPass(stackItem.getItemDamage(), 1), "items", IIconX, spellUI_y + 1.5f, d, d, false);
//		}
//
//		mc.renderEngine.bindTexture(spellbook_ui);
//		this.zLevel = 1000;
//		drawTexturedModalRect_Classic(x, y, 148, 0, activeSpellSize, activeSpellSize, 20, 20);
//		this.zLevel = 0;
//
//		mc.renderEngine.bindTexture(mc_gui);
//	}

	private void RenderManaBar(int i, int j, FontRenderer fontRendererObj){

		int barWidth = i / 8;

		AMVector2 Burnout_hud = getShiftedVector(ArsMagica2.config.getBurnoutHudPosition(), i, j);
		AMVector2 mana_hud = getShiftedVector(ArsMagica2.config.getManaHudPosition(), i, j);

		float green = 0.5f;
		float blue = 1.0f;
		float red = 0.126f;

		IEntityExtension props = EntityExtension.For(mc.thePlayer);

		//mana bar
		float mana = props.getCurrentMana();
		float bonusMana = props.getBonusCurrentMana();
		float maxMana = props.getMaxMana();

		float BurnoutBarWidth = barWidth;
		float Burnout = props.getCurrentBurnout();
		float maxBurnout = props.getMaxBurnout();

		if (mana + bonusMana > maxMana)
			mana = maxMana;

		float progressScaled = (mana / (maxMana + 0.01f));

		if (ArsMagica2.config.showHudBars()){
			//handle flashing of mana bar
			float flashTimer = AMGuiHelper.instance.getFlashTimer(MANA_BAR_FLASH_SLOT);
			if (flashTimer > 0){
				green = 0.0f;
				float redShift = 1.0f - red;

				float halfFlash = AMGuiHelper.instance.flashDuration / 2;

				if (flashTimer > halfFlash){
					float pct = (flashTimer - halfFlash) / halfFlash;
					red += redShift - (redShift * pct);
				}else{
					float pct = flashTimer / halfFlash;
					red += (redShift * pct);
				}
				GL11.glColor3f(red, green, blue);
			}else{
				if (bonusMana > 0)
					GL11.glColor3f(0.2f, 0.9f, 0.6f);
			}

			ItemStack curItem = Minecraft.getMinecraft().thePlayer.getHeldItem(EnumHand.MAIN_HAND);
			//TODO Spell Groups
			if (curItem != null && (curItem.getItem() == ItemDefs.spell
					//|| curItem.getItem() == ItemDefs.spellBook || curItem.getItem() == ItemDefs.arcaneSpellbook
					)){
				ItemStack spellStack = curItem.getItem() == ItemDefs.spell ? curItem : ((ItemSpellBook)curItem.getItem()).GetActiveItemStack(curItem);
				if (spellStack != null){
					ArrayList<ISpellPart> parts = SpellUtils.getPartsForGroup(spellStack, 0);//SpellUtils.getShapeGroupParts(spellStack);
					int sx = mana_hud.iX - 2 * parts.size() / 2;
					int sy = mana_hud.iY - 2 * parts.size() / 2;
					for (ISpellPart p : parts){
						TextureAtlasSprite icon = SpellIconManager.INSTANCE.getSprite(SpellRegistry.getSkillFromPart(p).getID());
						if (icon != null){
							DrawIconAtXY(icon, "items", sx, sy, false);
							sx += 3;
							sy += 3;
						}
					}
				}
			}

			DrawPartialIconAtXY(AMGuiIcons.manaLevel, progressScaled, 1, mana_hud.iX + 16, mana_hud.iY + 1f, (int)(barWidth * 0.99F), 40, false);
			DrawIconAtXY(AMGuiIcons.manaBar, "items", mana_hud.iX + 15, mana_hud.iY + 3, barWidth, 50, false);

			GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);

			progressScaled = (Burnout / (maxBurnout + 0.01f));
			DrawIconAtXY(AMGuiIcons.fatigueIcon, "items", Burnout_hud.iX + barWidth, Burnout_hud.iY, false);

			DrawPartialIconAtXY(AMGuiIcons.fatigueLevel, progressScaled, 1, Burnout_hud.iX, Burnout_hud.iY + 3f, BurnoutBarWidth, 40, false);
			DrawIconAtXY(AMGuiIcons.fatigueBar, "items", Burnout_hud.iX, Burnout_hud.iY + 4, barWidth, 48, false);

			green = 0.5f;
			blue = 1.0f;
			red = 0.126f;
			//magic level
			int manaBarColor = Math.round(red * 255);
			manaBarColor = (manaBarColor << 8) + Math.round(green * 255);
			manaBarColor = (manaBarColor << 8) + Math.round(blue * 255);

			String magicLevel = (new StringBuilder()).append("").append(EntityExtension.For(mc.thePlayer).getCurrentLevel()).toString();
			AMVector2 magicLevelPos = getShiftedVector(ArsMagica2.config.getLevelPosition(), i, j);
			magicLevelPos.iX -= Minecraft.getMinecraft().fontRendererObj.getStringWidth(magicLevel) / 2;
			fontRendererObj.drawStringWithShadow(magicLevel, magicLevelPos.iX, magicLevelPos.iY, manaBarColor);

			if (flashTimer > 0){
				GL11.glColor3f(1.0f, 1.0f, 1.0f);
			}
		}

		if (ArsMagica2.config.getShowNumerics()){
			GL11.glEnable(GL11.GL_BLEND);
			String manaStr = I18n.translateToLocal("am2.gui.mana") + ": " + (int)(mana + bonusMana) + "/" + (int)maxMana;
			String burnoutStr = I18n.translateToLocal("am2.gui.burnout") + ": " + (int)props.getCurrentBurnout() + "/" + (int)props.getMaxBurnout();
			AMVector2 manaNumericPos = getShiftedVector(ArsMagica2.config.getManaNumericPosition(), i, j);
			AMVector2 burnoutNumericPos = getShiftedVector(ArsMagica2.config.getBurnoutNumericPosition(), i, j);
			fontRendererObj.drawString(manaStr, manaNumericPos.iX, manaNumericPos.iY, bonusMana > 0 ? 0xeae31c : 0x2080FF);
			fontRendererObj.drawString(burnoutStr, burnoutNumericPos.iX + 25 - fontRendererObj.getStringWidth(burnoutStr), burnoutNumericPos.iY, 0xFF2020);
		}
		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
	}

//	private ItemStack getSpellFromStack(ItemStack stack){
//		if (stack.getItem() == ItemDefs.spell)
//			return stack;
//		else if (stack.getItem() == ItemDefs.spellBook || stack.getItem() == ItemDefs.arcaneSpellbook)
//			return ((ItemSpellBook)stack.getItem()).GetActiveItemStack(stack);
//		else
//			return null;
//	}

	private void RenderArmorStatus(int i, int j, Minecraft mc, FontRenderer fontRenderer){
		if (!ArsMagica2.config.showArmorUI())
			return;

//		ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());

		for (int slot = 0; slot < 4; ++slot){
//			if (ArmorHelper.PlayerHasArmorInSlot(mc.thePlayer, 3 - slot)){
//
//				AMVector2 position = getArmorSlotPosition(slot, scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight());
//				int blackoutTimer = AMGuiHelper.instance.getBlackoutTimer(3 - slot);
//				int blackoutMaxTimer = AMGuiHelper.instance.getBlackoutTimerMax(3 - slot);
//				GL11.glColor3f(1.0f, 1.0f, 1.0f);
//				ItemStack armor = mc.thePlayer.inventory.armorInventory[3 - slot];
//				float lineweight = 4f;
//				//durability
//				if (armor.isItemDamaged() && armor.getMaxDamage() > 0){
//					float pct = 1 - (float)armor.getItemDamage() / (float)armor.getMaxDamage();
//					AMGuiHelper.line2d(position.iX, position.iY + 10, position.iX + 10, position.iY + 10, this.zLevel + 100, lineweight, 0);
//
//					int color = (int)(255.0f * (1 - pct)) << 16 | (int)(255.0f * pct) << 8;
//
//					AMGuiHelper.line2d(position.iX, position.iY + 10, position.iX + (10 * pct), position.iY + 10, this.zLevel + 101, lineweight, color);
//				}
//				//cooldown
//				if (blackoutMaxTimer > 0){
//					float pct = (float)(blackoutMaxTimer - blackoutTimer) / (float)blackoutMaxTimer;
//					AMGuiHelper.line2d(position.iX, position.iY + 11, position.iX + 10, position.iY + 11, this.zLevel + 100, lineweight, 0);
//					AMGuiHelper.line2d(position.iX, position.iY + 11, position.iX + (10 * pct), position.iY + 11, this.zLevel + 101, lineweight, 0xFF0000);
//				}else{
//					AMGuiHelper.line2d(position.iX, position.iY + 11, position.iX + 10, position.iY + 11, this.zLevel + 101, lineweight, 0x0000FF);
//				}
//
//				TextureAtlasSprite icon = mc.getRenderItem().getItemModelMesher().getParticleIcon(mc.thePlayer.inventory.armorInventory[3 - slot].getItem(), mc.thePlayer.inventory.armorInventory[3 - slot].getItemDamage());
//				if (icon != null){
//					AMGuiHelper.DrawIconAtXY(icon, position.iX, position.iY, this.zLevel, 10, 10, true);
//				}else{
//					AMGuiHelper.DrawItemAtXY(mc.thePlayer.inventory.armorInventory[3 - slot], position.iX, position.iY, this.zLevel, 0.63f);
//				}
//			}
		}
		GL11.glColor3f(1.0f, 1.0f, 1.0f);
	}

//	private AMVector2 getArmorSlotPosition(int slot, int screenWidth, int screenHeight){
//		switch (slot){
//		case 0:
//			return getShiftedVector(ArsMagica2.config.getArmorPositionHead(), screenWidth, screenHeight);
//		case 1:
//			return getShiftedVector(ArsMagica2.config.getArmorPositionChest(), screenWidth, screenHeight);
//		case 2:
//			return getShiftedVector(ArsMagica2.config.getArmorPositionLegs(), screenWidth, screenHeight);
//		case 3:
//			return getShiftedVector(ArsMagica2.config.getArmorPositionBoots(), screenWidth, screenHeight);
//		}
//		return new AMVector2(0, 0);
//	}

	public void RenderAffinity(int i, int j){
		AMVector2 affinityPos = getShiftedVector(ArsMagica2.config.getAffinityPosition(), i, j);

		int x = affinityPos.iX;
		int y = affinityPos.iY;

		IAffinityData ad = AffinityData.For(Minecraft.getMinecraft().thePlayer);
		for (Affinity affinity : ad.getHighestAffinities()){
			if (affinity == null || affinity == SkillDefs.NONE) continue;
			GL11.glColor3f(1.0f, 1.0f, 1.0f);
			AMGuiHelper.DrawIconAtXY(mc.getRenderItem().getItemModelMesher().getParticleIcon(ItemDefs.essence, AffinityShiftUtils.getEssenceForAffinity(affinity).getItemDamage()), x, y, j, 12, 12, true);

			if (ArsMagica2.config.getShowNumerics()){
				GL11.glEnable(GL11.GL_BLEND);
				String display = String.format("%.2f%%", AffinityData.For(mc.thePlayer).getAffinityDepth(affinity) * 100);
				if (x < i / 2)
					Minecraft.getMinecraft().fontRendererObj.drawString(display, x + 14, y + 2, affinity.getColor());
				else
					Minecraft.getMinecraft().fontRendererObj.drawString(display, x - 2 - Minecraft.getMinecraft().fontRendererObj.getStringWidth(display), y + 2, affinity.getColor());
			}
			y += 15;
		}
	}

	public void RenderContingency(int i, int j){

		AMVector2 contingencyPos = getShiftedVector(ArsMagica2.config.getContingencyPosition(), i, j);
		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		TextureAtlasSprite icon = null;
		ContingencyType type = EntityExtension.For(Minecraft.getMinecraft().thePlayer).getContingencyType();
		switch (type){
		case DAMAGE:
			icon = SpellIconManager.INSTANCE.getSprite("contingency_damage");
			break;
		case FALL:
			icon = SpellIconManager.INSTANCE.getSprite("contingency_fall");
			break;
		case HEALTH:
			icon = SpellIconManager.INSTANCE.getSprite("contingency_health");
			break;
		case FIRE:
			icon = SpellIconManager.INSTANCE.getSprite("contingency_fire");
			break;
		case DEATH:
			icon = SpellIconManager.INSTANCE.getSprite("contingency_death");
			break;
		case NULL:
		default:
			return;
		}
		//System.out.println(icon);
		DrawIconAtXY(icon, "items", contingencyPos.iX, contingencyPos.iY, 16, 16, true);
		//GL11.glColor3f(1.0f, 1.0f, 1.0f);
	}

//	public void RenderBuffs(int i, int j){
//
//		if (!ArsMagica2.config.getShowBuffs()){
//			return;
//		}
//
//		int barWidth = i / 8;
//
//		AMVector2 posBuffStart = getShiftedVector(ArsMagica2.config.getPositiveBuffsPosition(), i, j);
//		AMVector2 negBuffStart = getShiftedVector(ArsMagica2.config.getNegativeBuffsPosition(), i, j);
//
//		int positive_buff_x = posBuffStart.iX;
//		int positive_buff_y = posBuffStart.iY;
//
//		int negative_buff_x = negBuffStart.iX;
//		int negative_buff_y = negBuffStart.iY;
//		for (PotionEffect pe : getPotionEffectsByTimeRemaining()){
//			this.mc.renderEngine.bindTexture(inventory);
//
//			int potionID = pe.getPotionID();
//			if (potionID < 0 || potionID >= Potion.potionTypes.length)
//				continue;
//
//			Potion potion = Potion.potionTypes[potionID];
//
//			if (potion == null)
//				continue;
//
//			if (potion.isBadEffect()){
//				if (potion.hasStatusIcon()){
//					int l = potion.getStatusIconIndex();
//					if (pe.getDuration() < 100){
//						GL11.glColor4f(1.0f, 1.0f, 1.0f, AMGuiHelper.instance.fastFlashAlpha);
//					}else if (pe.getDuration() < 200){
//						GL11.glColor4f(1.0f, 1.0f, 1.0f, AMGuiHelper.instance.slowFlashAlpha);
//					}
//					this.drawTexturedModalRect_Classic(negative_buff_x, negative_buff_y, 0 + l % 8 * 18, 198 + l / 8 * 18, 10, 10, 18, 18);
//					GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
//					negative_buff_x -= 12;
//					if (negative_buff_x <= negBuffStart.iX - 48){
//						negative_buff_x = negBuffStart.iX;
//						negative_buff_y += 12;
//					}
//				}
//			}else{
//				this.mc.renderEngine.bindTexture(inventory);
//				if (potion.hasStatusIcon()){
//					int l = potion.getStatusIconIndex();
//					if (pe.getDuration() < 100){
//						GL11.glColor4f(1.0f, 1.0f, 1.0f, AMGuiHelper.instance.fastFlashAlpha);
//					}else if (pe.getDuration() < 200){
//						GL11.glColor4f(1.0f, 1.0f, 1.0f, AMGuiHelper.instance.slowFlashAlpha);
//					}
//					this.drawTexturedModalRect_Classic(positive_buff_x, positive_buff_y, 0 + l % 8 * 18, 198 + l / 8 * 18, 10, 10, 18, 18);
//					GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
//					positive_buff_x += 12;
//					if (positive_buff_x >= posBuffStart.iX + 48){
//						positive_buff_x = posBuffStart.iX;
//						positive_buff_y += 12;
//					}
//				}
//			}
//		}
//	}

	public void RenderMagicXP(int i, int j){
		IEntityExtension props = EntityExtension.For(Minecraft.getMinecraft().thePlayer);
		if (props.getCurrentLevel() > 0){
			GL11.glEnable(GL11.GL_BLEND);
			AMVector2 position = getShiftedVector(ArsMagica2.config.getXPBarPosition(), i, j);
			AMVector2 dimensions = new AMVector2(182, 5);
			Minecraft.getMinecraft().renderEngine.bindTexture(mc_gui);
			GL11.glColor4f(0.5f, 0.5f, 1.0f, ArsMagica2.config.showXPAlways() ? 1.0f : AMGuiHelper.instance.getMagicXPBarAlpha());

			//base XP bar
			drawTexturedModalRect_Classic(position.iX, position.iY, 0, 64, dimensions.iX, dimensions.iY, dimensions.iX, dimensions.iY);

			if (props.getCurrentXP() > 0){
				float pctXP = props.getCurrentXP() / props.getMaxXP();
				if (pctXP > 1)
					pctXP = 1;
				int width = (int)((dimensions.iX + 1) * pctXP);
				drawTexturedModalRect_Classic(position.iX, position.iY, 0, 69, width, dimensions.iY, width, dimensions.iY);
			}

			if (ArsMagica2.config.getShowNumerics() && (ArsMagica2.config.showXPAlways() || AMGuiHelper.instance.getMagicXPBarAlpha() > 0)){
				String xpStr = I18n.translateToLocal("am2.gui.xp") + ": " + +(int)(props.getCurrentXP() * 100) + "/" + (int)(props.getMaxXP() * 100);
				AMVector2 numericPos = getShiftedVector(ArsMagica2.config.getXPNumericPosition(), i, j);
				Minecraft.getMinecraft().fontRendererObj.drawString(xpStr, numericPos.iX, numericPos.iY, 0x999999);
			}
		}
	}

//	private ArrayList<PotionEffect> getPotionEffectsByTimeRemaining(){
//		Iterator i = mc.thePlayer.getActivePotionEffects().iterator();
//		ArrayList<PotionEffect> potions = new ArrayList<PotionEffect>();
//
//		while (i.hasNext())
//			potions.add((PotionEffect)i.next());
//
//		Collections.sort(potions, durationComparator);
//		return potions;
//	}

	public void drawTexturedModalRect_Classic(int par1, int par2, int par3, int par4, int par5, int par6){
		float var7 = 0.00390625F;
		float var8 = 0.00390625F;

		Tessellator var9 = Tessellator.getInstance();
		var9.getBuffer().begin(7, DefaultVertexFormats.POSITION_TEX);
		var9.getBuffer().pos(par1 + 0, par2 + par6, this.zLevel).tex((par3 + 0) * var7, (par4 + par6) * var8).endVertex();
		var9.getBuffer().pos(par1 + par5, par2 + par6, this.zLevel).tex((par3 + par5) * var7, (par4 + par6) * var8).endVertex();
		var9.getBuffer().pos(par1 + par5, par2 + 0, this.zLevel).tex((par3 + par5) * var7, (par4 + 0) * var8).endVertex();
		var9.getBuffer().pos(par1 + 0, par2 + 0, this.zLevel).tex((par3 + 0) * var7, (par4 + 0) * var8).endVertex();
		var9.draw();
	}

	/**
	 * Draw a section of the currently bound texture to the screen.
	 *
	 * @param dst_x      The x coordinate on the screen to draw to
	 * @param dst_y      The y coordinate on the screen to draw to
	 * @param src_x      The x coordinate on the texture to pull from
	 * @param src_y      The y coordinate on the texture to pull from
	 * @param dst_width  The width on screen to draw
	 * @param dst_height The height on screen to draw
	 * @param src_width  The width of the texture section
	 * @param src_height The height of the texture section
	 */
	public void drawTexturedModalRect_Classic(float dst_x, float dst_y, float src_x, float src_y, float dst_width, float dst_height, float src_width, float src_height){
		float var7 = 0.00390625F;
		float var8 = 0.00390625F;

		Tessellator var9 = Tessellator.getInstance();
		var9.getBuffer().begin(7, DefaultVertexFormats.POSITION_TEX);
		var9.getBuffer().pos(dst_x + 0, dst_y + dst_height, this.zLevel).tex((src_x + 0) * var7, (src_y + src_height) * var8).endVertex();
		var9.getBuffer().pos(dst_x + dst_width, dst_y + dst_height, this.zLevel).tex((src_x + src_width) * var7, (src_y + src_height) * var8).endVertex();
		var9.getBuffer().pos(dst_x + dst_width, dst_y + 0, this.zLevel).tex((src_x + src_width) * var7, (src_y + 0) * var8).endVertex();
		var9.getBuffer().pos(dst_x + 0, dst_y + 0, this.zLevel).tex((src_x + 0) * var7, (src_y + 0) * var8).endVertex();
		var9.draw();
	}

	public void drawTexturedModalRect(int par1, int par2, int par3, int par4, int par5, int par6){
		float var9 = 0;
		float var10 = 1;
		float var11 = 0;
		float var12 = 1;

		Tessellator var8 = Tessellator.getInstance();
		//GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0F);
		var8.getBuffer().begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
		var8.getBuffer().pos(par1 + 0, par2 + par6, this.zLevel).tex(var9, var12).normal(0.0F, 1.0F, 0.0F).endVertex();
		var8.getBuffer().pos(par1 + par5, par2 + par6, this.zLevel).tex(var10, var12).normal(0.0F, 1.0F, 0.0F).endVertex();
		var8.getBuffer().pos(par1 + par5, par2 + 0, this.zLevel).tex(var10, var11).normal(0.0F, 1.0F, 0.0F).endVertex();
		var8.getBuffer().pos(par1 + 0, par2 + 0, this.zLevel).tex(var9, var11).normal(0.0F, 1.0F, 0.0F).endVertex();
		var8.draw();
	}

//	private void renderPortalOverlay(float par1, int par2, int par3){
//
//	}

	private void DrawIconAtXY(TextureAtlasSprite icon, String base, float x, float y, boolean semitransparent){
		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		DrawIconAtXY(icon, base, x, y, 16, 16, semitransparent);
	}

	private void DrawIconAtXY(TextureAtlasSprite IIcon, String base, float x, float y, int w, int h, boolean semitransparent){
		if (IIcon == null) return;
		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		Tessellator tessellator = Tessellator.getInstance();
		tessellator.getBuffer().begin(7, DefaultVertexFormats.POSITION_TEX);
		tessellator.getBuffer().pos(x, y + h, this.zLevel).tex(IIcon.getMinU(), IIcon.getMaxV()).endVertex();
		tessellator.getBuffer().pos(x + w, y + h, this.zLevel).tex(IIcon.getMaxU(), IIcon.getMaxV()).endVertex();
		tessellator.getBuffer().pos(x + w, y, this.zLevel).tex(IIcon.getMaxU(), IIcon.getMinV()).endVertex();
		tessellator.getBuffer().pos(x, y, this.zLevel).tex(IIcon.getMinU(), IIcon.getMinV()).endVertex();
		tessellator.draw();
	}

	private void DrawPartialIconAtXY(TextureAtlasSprite IIcon, float pct_x, float pct_y, float x, float y, float w, float h, boolean semitransparent){
		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		if (IIcon == null) return;

		Tessellator tessellator = Tessellator.getInstance();
		tessellator.getBuffer().begin(7, DefaultVertexFormats.POSITION_TEX);

		tessellator.getBuffer().pos(x, y + (h * pct_y), this.zLevel).tex( IIcon.getMinU(), IIcon.getMaxV()).endVertex();
		tessellator.getBuffer().pos(x + (w * pct_x), y + (h * pct_y), this.zLevel).tex( IIcon.getMaxU(), IIcon.getMaxV()).endVertex();
		tessellator.getBuffer().pos(x + (w * pct_x), y, this.zLevel).tex(IIcon.getMaxU(), IIcon.getMinV()).endVertex();
		tessellator.getBuffer().pos(x, y, this.zLevel).tex(IIcon.getMinU(), IIcon.getMinV()).endVertex();

		tessellator.draw();
	}

	private AMVector2 getShiftedVector(AMVector2 configVec, int screenWidth, int screenHeight){
		int x = (int)Math.round(configVec.x * screenWidth);
		int y = (int)Math.round(configVec.y * screenHeight);

		return new AMVector2(x, y);
	}

	class PotionEffectDurationComparator implements Comparator<PotionEffect>{


		public PotionEffectDurationComparator(){
		}

		@Override
		public int compare(PotionEffect o1, PotionEffect o2){
			if (o1.getDuration() < o2.getDuration()) return -1;
			else if (o1.getDuration() > o2.getDuration()) return 1;
			else return 0;
		}

	}
}
