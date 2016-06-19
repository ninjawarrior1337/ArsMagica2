package am2.utils;

import java.util.ArrayList;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

import am2.ArsMagica2;
import am2.affinity.Affinity;
import am2.api.SpellRegistry;
import am2.api.SpellRegistry.SpellData;
import am2.api.extensions.IEntityExtension;
import am2.config.AM2Config;
import am2.defs.ItemDefs;
import am2.defs.PotionEffectsDefs;
import am2.defs.SpellDefs;
import am2.event.SpellCastEvent;
import am2.extensions.EntityExtension;
import am2.items.ItemSpellBase;
import am2.spell.IComponent;
import am2.spell.IModifier;
import am2.spell.IShape;
import am2.spell.ISpellPart;
import am2.spell.SpellCastResult;
import am2.spell.SpellModifiers;
import am2.spell.modifier.Colour;
import am2.spell.shape.MissingShape;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class SpellUtils {
	public static IShape getShapeForStage(ItemStack stack, int stage){
		if (stack == null || !stack.hasTagCompound()) return SpellDefs.MISSING_SHAPE;
		NBTTagCompound am2Tag = NBTUtils.getAM2Tag(stack.getTagCompound());
		NBTTagList stageTag = NBTUtils.addCompoundList(am2Tag, "Stage_" + stage);
		String shapeName = "null";
		for (int i = 0; i < stageTag.tagCount(); i++) {
			if (stageTag.getCompoundTagAt(i).getString("Type").equals("Shape")) {
				shapeName = stageTag.getCompoundTagAt(i).getString("ID");
				break;
			}
		}

		return SpellRegistry.getShapeFromName(shapeName) != null ? SpellRegistry.getShapeFromName(shapeName).part : SpellDefs.MISSING_SHAPE;
	}
	
	public static float modifyDamage(EntityLivingBase caster, float damage){
		float factor = (float)(EntityExtension.For(caster).getCurrentLevel() < 20 ?
				0.5 + (0.5 * (EntityExtension.For(caster).getCurrentLevel() / 19)) :
				1.0 + (1.0 * (EntityExtension.For(caster).getCurrentLevel() - 20) / 79));
		return damage * factor;
	}

	
	public static boolean modifierIsPresent (SpellModifiers mod, ItemStack stack) {
		ArrayList<IModifier> mods = getModifiersForStage(stack, -1);
		for (IModifier m : mods) {
			if (m.getAspectsModified().contains(mod)) 
				return true;
		}
		
		return false;
	}
	
	public static int countModifiers (SpellModifiers mod, ItemStack stack) {
		ArrayList<IModifier> mods = getModifiersForStage(stack, -1);
		int i = 0;
		for (IModifier m : mods) {
			if (m.getAspectsModified().contains(mod)) 
				i++;
		}
		
		return i;
	}
	
	public static boolean attackTargetSpecial(ItemStack spellStack, Entity target, DamageSource damagesource, float magnitude){

		if (target.worldObj.isRemote)
			return true;

//		EntityPlayer dmgSrcPlayer = null;

		if (damagesource.getSourceOfDamage() != null){
			if (damagesource.getSourceOfDamage() instanceof EntityLivingBase){
				EntityLivingBase source = (EntityLivingBase)damagesource.getSourceOfDamage();
//				if ((source instanceof EntityLightMage || source instanceof EntityDarkMage) && target.getClass() == EntityCreeper.class){
//					return false;
//				}else if (source instanceof EntityLightMage && target instanceof EntityLightMage){
//					return false;
//				}else if (source instanceof EntityDarkMage && target instanceof EntityDarkMage){
//					return false;
//				}else 
				if (source instanceof EntityPlayer && target instanceof EntityPlayer && !target.worldObj.isRemote && (!FMLCommonHandler.instance().getMinecraftServerInstance().isPVPEnabled() || ((EntityPlayer)target).capabilities.isCreativeMode)){
					return false;
				}

				if (source.isPotionActive(PotionEffectsDefs.fury))
					magnitude += 4;
			}

//			if (damagesource.getSourceOfDamage() instanceof EntityPlayer){
//				dmgSrcPlayer = (EntityPlayer)damagesource.getSourceOfDamage();
//				int armorSet = ArmorHelper.getFullArsMagicaArmorSet(dmgSrcPlayer);
//				if (armorSet == ArsMagicaArmorMaterial.MAGE.getMaterialID()){
//					magnitude *= 1.05f;
//				}else if (armorSet == ArsMagicaArmorMaterial.BATTLEMAGE.getMaterialID()){
//					magnitude *= 1.025f;
//				}else if (armorSet == ArsMagicaArmorMaterial.ARCHMAGE.getMaterialID()){
//					magnitude *= 1.1f;
//				}
//
//				ItemStack equipped = (dmgSrcPlayer).getCurrentEquippedItem();
//				if (equipped != null && equipped.getItem() == ItemsCommonProxy.arcaneSpellbook){
//					magnitude *= 1.1f;
//				}
//			}
		}

//		if (target instanceof EntityLivingBase){
//			if (EntityUtilities.isSummon((EntityLivingBase)target) && damagesource.damageType.equals("magic")){
//				magnitude *= 3.0f;
//			}
//		}

		magnitude *= ArsMagica2.config.getDamageMultiplier();

//		ItemStack oldItemStack = null;

		boolean success = false;
		if (target instanceof EntityDragon){
			success = ((EntityDragon)target).attackEntityFromPart(((EntityDragon)target).dragonPartBody, damagesource, magnitude);
		}else{
			success = target.attackEntityFrom(damagesource, magnitude);
		}

//		if (dmgSrcPlayer != null){
//			if (spellStack != null && target instanceof EntityLivingBase){
//				if (!target.worldObj.isRemote &&
//						((EntityLivingBase)target).getHealth() <= 0 &&
//						modifierIsPresent(SpellModifiers.DISMEMBERING_LEVEL, spellStack)){
//					double chance = SpellUtils.getModifiedDouble_Add(0, spellStack, dmgSrcPlayer, target, dmgSrcPlayer.worldObj, SpellModifiers.DISMEMBERING_LEVEL);
//					if (dmgSrcPlayer.worldObj.rand.nextDouble() <= chance){
//						dropHead(target, dmgSrcPlayer.worldObj);
//					}
//				}
//			}
//		}

		return success;
	}
	
	@SafeVarargs
	public static ItemStack createSpellStack(int id, SpellData<? extends ISpellPart>... components) {
		ItemStack stack = new ItemStack(ItemDefs.spell, 1, id);
		NBTTagCompound tag = new NBTTagCompound();
		NBTTagCompound am2 = NBTUtils.getAM2Tag(tag);
		NBTTagCompound compound = NBTUtils.addTag(am2, "SpellData");
		Multimap<Integer, ISpellPart> spellStageMap = HashMultimap.create();
		int stage = 0;
		int manaCost = 0;
		float multiplier = 1F;
		int finalCost = 0;
		for (SpellData<? extends ISpellPart> info : components) {
			if (info == null)
				continue;
			//System.out.println(info.id);
			spellStageMap.put(stage, info.part);
			NBTTagList stageTag = NBTUtils.addCompoundList(am2, "Stage_" + stage);
			NBTTagCompound tmp = new NBTTagCompound();
			tmp.setString("ID", info.id);
			String type = "";
			if (info.part instanceof IShape) {
				type = "Shape";
				multiplier *= ((IShape)info.part).manaCostMultiplier(stack);
			}
			if (info.part instanceof IModifier) {
				type = "Modifier";
				multiplier *= ((IModifier)info.part).getManaCostMultiplier(stack, stage, 1);
			}
			if (info.part instanceof IComponent) {
				type = "Component";
				manaCost += ((IComponent)info.part).manaCost(null);
			}
			tmp.setString("Type", type);
			info.part.encodeBasicData(compound, null);
			stageTag.appendTag(tmp);
			am2.setTag("Stage_" + stage, stageTag);
			if (info.part instanceof IShape) {
				stage++;
				finalCost += (int) ((float)manaCost * multiplier);
				manaCost = 0;
				multiplier = 1.0F;
			}
		}
		finalCost += (int) ((float)manaCost * multiplier);
		am2.setInteger("Cost", finalCost);
		am2.setInteger("StageNum", stage);
		am2.setInteger("CurrentGroup", 0);
		stack.setTagCompound(tag);		
		return stack;
	}
	
	public static ItemStack popStackStage(ItemStack is) {
		NBTUtils.getAM2Tag(is.getTagCompound()).setInteger("CurrentGroup", NBTUtils.getAM2Tag(is.getTagCompound()).getInteger("CurrentGroup"));
		return is;
	}
	
	public static int numStages(ItemStack stack){
		return NBTUtils.getAM2Tag(stack.getTagCompound()).getInteger("StageNum");
	}
	
	public static int getManaCost(ItemStack stack) {
		return NBTUtils.getAM2Tag(stack.getTagCompound()).getInteger("Cost");
	}
	
	public static SpellCastResult applyStackStage(ItemStack stack, EntityLivingBase caster, EntityLivingBase target, double x, double y, double z, EnumFacing side, World world, boolean consumeMBR, boolean giveXP, int ticksUsed) {
		if (caster.isPotionActive(PotionEffectsDefs.silence))
			return SpellCastResult.SILENCED;
		
		IEntityExtension ext = EntityExtension.For(caster);
		int group = NBTUtils.getAM2Tag(stack.getTagCompound()).getInteger("CurrentGroup");
		IShape shape = getShapeForStage(stack, group);
		if (!(caster instanceof EntityPlayer))
			return SpellCastResult.EFFECT_FAILED;
		
		if (shape instanceof MissingShape)
			return SpellCastResult.MALFORMED_SPELL_STACK;
		int manaCost = getManaCost(stack);
		manaCost *= 1F + (float)((float)EntityExtension.For(caster).getCurrentBurnout() / (float)EntityExtension.For(caster).getMaxBurnout());
		SpellCastEvent.Pre pre = new SpellCastEvent.Pre(caster, stack, manaCost);
		MinecraftForge.EVENT_BUS.post(pre);
		manaCost = pre.manaCost;
				
		if (consumeMBR) {
			if (!ext.hasEnoughtMana(manaCost) && !((EntityPlayer)caster).capabilities.isCreativeMode)
				return SpellCastResult.NOT_ENOUGH_MANA;
			if (!((EntityPlayer)caster).capabilities.isCreativeMode) {
				ext.useMana(manaCost);
				ext.setCurrentBurnout((int) (ext.getCurrentBurnout() + (manaCost * AM2Config.MANA_BURNOUT_RATIO)));
				if (ext.getCurrentBurnout() > ext.getMaxBurnout())
					ext.setCurrentBurnout(ext.getMaxBurnout());
			}
		}
				
		ItemStack stack2 = stack.copy();
		NBTUtils.getAM2Tag(stack2.getTagCompound()).setInteger("CurrentGroup", group + 1);

		if (group == 0)
			shape.beginStackStage((ItemSpellBase)stack.getItem(), stack2, caster, target, world, x, y, z, side, giveXP, ticksUsed);
		else {
			NBTUtils.getAM2Tag(stack.getTagCompound()).setInteger("CurrentGroup", group + 1);
			shape.beginStackStage((ItemSpellBase)stack.getItem(), stack, caster, target, world, x, y, z, side, giveXP, ticksUsed);
		}
		
		return SpellCastResult.SUCCESS;
	}


	public static double getModifiedStat (double defaultValue, int operation, ItemStack stack, EntityLivingBase caster, Entity target, World world, int stage, SpellModifiers modified) {
		double val = defaultValue;
		if (stage != -1) {
			NBTTagList stageTag = NBTUtils.addCompoundList(NBTUtils.getAM2Tag(stack.getTagCompound()), "Stage_" + stack);
			for (int i = 0; i < stageTag.tagCount(); i++) {
				NBTTagCompound tag = stageTag.getCompoundTagAt(i);
				String tagType = tag.getString("Type");
				if (tagType.equals("Modifier")) {
					String tagID = tag.getString("ID");
					SpellData<IModifier> mod = SpellRegistry.getModifierFromName(tagID);
					if (mod.part.getAspectsModified().contains(caster))
						val = makeCalculation(operation, val, mod.part.getModifier(modified, caster, target, world, stack.getTagCompound()));
				}
			}
		} else {
			for (int j = 0; j < NBTUtils.getAM2Tag(stack.getTagCompound()).getInteger("StageNum"); j++) { 
				NBTTagList stageTag = NBTUtils.addCompoundList(NBTUtils.getAM2Tag(stack.getTagCompound()), "Stage_" + j);
				for (int i = 0; i < stageTag.tagCount(); i++) {
					NBTTagCompound tag = stageTag.getCompoundTagAt(i);
					String tagType = tag.getString("Type");
					if (tagType.equals("Modifier")) {
						String tagID = tag.getString("ID");
						SpellData<IModifier> mod = SpellRegistry.getModifierFromName(tagID);
						if (mod.part.getAspectsModified().contains(caster)) {
						}
					}
				}
			}
		}
		return val;
	}
	
	private static double makeCalculation (int operation, double val, double mod ) {
		if (operation == 0) {
			return val + mod;
		}
		if (operation == 1) {
			return val - mod;
		}
		if (operation == 2) {
			return val * mod;
		}
		if (operation == 3) {
			return val / mod;
		}
		return 0;
	}
	
	/**
	 * 
	 * @param operation 0 add, 1 subtract, 2 multiply, 3 divide
	 * @param stage set to -1 for all;
	 * @param modified
	 * @param stack
	 * @return
	 */
	public static double getModifiedStat (int operation, ItemStack stack, EntityLivingBase caster, Entity target, World world, int stage, SpellModifiers modified) {
		return getModifiedStat(modified.defaultValue, operation, stack, caster, target, world, stage, modified);
	}
	
	public static ArrayList<IModifier> getModifiersForStage (ItemStack stack, int stage) {
		ArrayList<IModifier> mods = new ArrayList<IModifier>();
		if (stage != -1) {
			NBTTagList stageTag = NBTUtils.addCompoundList(NBTUtils.getAM2Tag(stack.getTagCompound()), "Stage_" + stage);
			for (int i = 0; i < stageTag.tagCount(); i++) {
				NBTTagCompound tag = stageTag.getCompoundTagAt(i);
				String tagType = tag.getString("Type");
				if (tagType.equals("Modifier")) {
					mods.add(SpellRegistry.getModifierFromName(tag.getString("ID")).part);
				}
			}
		} else {
			for (int j = 0; j <= NBTUtils.getAM2Tag(stack.getTagCompound()).getInteger("StageNum"); j++) { 
				NBTTagList stageTag = NBTUtils.addCompoundList(NBTUtils.getAM2Tag(stack.getTagCompound()), "Stage_" + j);
				for (int i = 0; i < stageTag.tagCount(); i++) {
					NBTTagCompound tag = stageTag.getCompoundTagAt(i);
					String tagType = tag.getString("Type");
					if (tagType.equals("Modifier")) {
						mods.add(SpellRegistry.getModifierFromName(tag.getString("ID")).part);
					}
				}
			}
		}
		return mods;
	}
	
	public static ArrayList<IComponent> getComponentsForStage (ItemStack stack, int stage) {
		try {
			ArrayList<IComponent> mods = new ArrayList<IComponent>();
			if (stage != -1) {
				NBTTagList stageTag = NBTUtils.addCompoundList(NBTUtils.getAM2Tag(stack.getTagCompound()), "Stage_" + stage);
				for (int i = 0; i < stageTag.tagCount(); i++) {
					NBTTagCompound tag = stageTag.getCompoundTagAt(i);
					String tagType = tag.getString("Type");
					if (tagType.equals("Component")) {
						mods.add(SpellRegistry.getComponentFromName(tag.getString("ID")).part);
					}
				}
			} else {
				//System.out.println(NBTUtils.getAM2Tag(stack.getTagCompound()).getInteger("StageNum"));
				for (int j = 0; j <= NBTUtils.getAM2Tag(stack.getTagCompound()).getInteger("StageNum"); j++) { 
					NBTTagList stageTag = NBTUtils.addCompoundList(NBTUtils.getAM2Tag(stack.getTagCompound()), "Stage_" + j);
					for (int i = 0; i < stageTag.tagCount(); i++) {
						NBTTagCompound tag = stageTag.getCompoundTagAt(i);
						String tagType = tag.getString("Type");
						if (tagType.equals("Component")) {
							mods.add(SpellRegistry.getComponentFromName(tag.getString("ID")).part);
						}
					}
				}
			}
			return mods;
		} catch (Exception e) {
			return Lists.newArrayList();
		}
	}
	
	public static SpellCastResult applyStageToGround(ItemStack stack, EntityLivingBase caster, World world, BlockPos pos, EnumFacing blockFace, double impactX, double impactY, double impactZ, boolean consumeMBR){
		IShape stageShape = SpellUtils.getShapeForStage(stack, 0);
		if (stageShape == null || stageShape == SpellDefs.MISSING_SHAPE){
			return SpellCastResult.MALFORMED_SPELL_STACK;
		}
		//System.out.println("LEL");
		int group = NBTUtils.getAM2Tag(stack.getTagCompound()).getInteger("CurrentGroup");
		ArrayList<IComponent> components = SpellUtils.getComponentsForStage(stack, group);
		//System.out.println(components.size());
		for (IComponent component : components){
			if (component.applyEffectBlock(stack, world, pos, blockFace, impactX, impactY, impactZ, caster)){
				if (component.getAffinity() != null) {
					for (Affinity aff : component.getAffinity()) {
						AffinityShiftUtils.applyShift((EntityPlayer)caster, component.getAffinityShift(aff), aff);
					}
				}
				if (world.isRemote){
					int color = -1;
					if (modifierIsPresent(SpellModifiers.COLOR, stack)){
						ArrayList<IModifier> mods = SpellUtils.getModifiersForStage(stack, -1);
						for (IModifier mod : mods){
							if (mod instanceof Colour){
								color = (int)mod.getModifier(SpellModifiers.COLOR, null, null, null, NBTUtils.getAM2Tag(stack.getTagCompound()));
							}
						}
					}
					component.spawnParticles(world, pos.getX(), pos.getY(), pos.getZ(), caster, caster, world.rand, color);
				}
			}
		}

		return SpellCastResult.SUCCESS;
	}

	public static SpellCastResult applyStageToEntity(ItemStack stack, EntityLivingBase caster, World world, Entity target, boolean shiftAffinityAndXP){
		IShape stageShape = SpellUtils.getShapeForStage(stack, 0);
		if (stageShape == null) return SpellCastResult.MALFORMED_SPELL_STACK;

//		if ((!AMCore.config.getAllowCreativeTargets()) && target instanceof EntityPlayerMP && ((EntityPlayerMP) target).capabilities.isCreativeMode) {
//			return SpellCastResult.EFFECT_FAILED;
//		}
		int group = NBTUtils.getAM2Tag(stack.getTagCompound()).getInteger("CurrentGroup");
		ArrayList<IComponent> components = SpellUtils.getComponentsForStage(stack, group);

		boolean appliedOneComponent = false;

		for (IComponent component : components){

//			if (SkillTreeManager.instance.isSkillDisabled(component))
//				continue;

			if (component.applyEffectEntity(stack, world, caster, target)){
				appliedOneComponent = true;
				if (world.isRemote){
					int color = -1;
					if (SpellUtils.modifierIsPresent(SpellModifiers.COLOR, stack)){
						ArrayList<IModifier> mods = SpellUtils.getModifiersForStage(stack, -1);
						for (IModifier mod : mods){
							if (mod instanceof Colour){
								color = (int)mod.getModifier(SpellModifiers.COLOR, null, null, null, NBTUtils.getAM2Tag(stack.getTagCompound()));
							}
						}
					}
					component.spawnParticles(world, target.posX, target.posY + target.getEyeHeight(), target.posZ, caster, target, world.rand, color);
				}
				if (caster instanceof EntityPlayer) {
					for (Affinity aff : component.getAffinity()) {
						AffinityShiftUtils.applyShift((EntityPlayer)caster, component.getAffinityShift(aff), aff);
					}
				}
			}
		}

		if (appliedOneComponent)
			return SpellCastResult.SUCCESS;
		else
			return SpellCastResult.EFFECT_FAILED;
	}

	public static int currentStage(ItemStack spellStack) {
		return NBTUtils.getAM2Tag(spellStack.getTagCompound()).getInteger("CurrentGroup");
	}

	public static boolean componentIsPresent(ItemStack stack, Class<? extends IComponent> clazz) {
		for (IComponent comp : getComponentsForStage(stack, currentStage(stack)))
			if (clazz.isInstance(comp))
				return true;
		return false;
	}

	public static int getModifiedInt_Mul(int defaultValue, ItemStack stack, EntityLivingBase caster, Entity target, World world, SpellModifiers modified) {
		return (int) getModifiedStat(defaultValue, 2, stack, caster, target, world, -1, modified);
	}
	
	public static double getModifiedDouble_Mul(double defaultValue, ItemStack stack, EntityLivingBase caster, Entity target, World world, SpellModifiers modified) {
		return getModifiedStat(defaultValue, 2, stack, caster, target, world, -1, modified);
	}
	
	public static double getModifiedDouble_Mul(ItemStack stack, EntityLivingBase caster, Entity target, World world, SpellModifiers modified) {
		return getModifiedStat(modified.defaultValue, 2, stack, caster, target, world, -1, modified);
	}
	
	public static int getModifiedInt_Add(int defaultValue, ItemStack stack, EntityLivingBase caster, Entity target, World world, SpellModifiers modified) {
		return (int) getModifiedDouble_Add(defaultValue, stack, caster, target, world, modified);
	}
	
	public static double getModifiedDouble_Add(double defaultValue, ItemStack stack, EntityLivingBase caster, Entity target, World world, SpellModifiers modified) {
		return getModifiedStat(defaultValue, 0, stack, caster, target, world, -1, modified);
	}
	
	public static double getModifiedDouble_Add(ItemStack stack, EntityLivingBase caster, Entity target, World world, SpellModifiers modified) {
		return getModifiedStat(modified.defaultValueInt, 0, stack, caster, target, world, -1, modified);
	}

	public static int getModifiedInt_Add(ItemStack stack, EntityLivingBase caster, EntityLivingBase target, World world, SpellModifiers modified) {
		return getModifiedInt_Add(modified.defaultValueInt, stack, caster, target, world, modified);
	}

	public static String getSpellMetadata(ItemStack stack, String string) {
		return NBTUtils.addTag(NBTUtils.getAM2Tag(stack.getTagCompound()), "SpellData").getString(string);
	}

	public static void setSpellMetadata(ItemStack stack, String string, String s) {
		NBTUtils.addTag(NBTUtils.getAM2Tag(stack.getTagCompound()), "SpellData").setString(string, s);
	}

}
