package am2.extensions;

import static am2.extensions.DataDefinitions.AFFINITY_HEAL_COOLDOWN;
import static am2.extensions.DataDefinitions.CONTENGENCY_STACK;
import static am2.extensions.DataDefinitions.CONTENGENCY_TYPE;
import static am2.extensions.DataDefinitions.CURRENT_LEVEL;
import static am2.extensions.DataDefinitions.CURRENT_MANA;
import static am2.extensions.DataDefinitions.CURRENT_MANA_FATIGUE;
import static am2.extensions.DataDefinitions.CURRENT_SUMMONS;
import static am2.extensions.DataDefinitions.CURRENT_XP;
import static am2.extensions.DataDefinitions.HAS_FALL_PROTECTION;
import static am2.extensions.DataDefinitions.HEAL_COOLDOWN;
import static am2.extensions.DataDefinitions.IS_INVERTED;
import static am2.extensions.DataDefinitions.IS_SHRUNK;
import static am2.extensions.DataDefinitions.MARK_DIMENSION;
import static am2.extensions.DataDefinitions.MARK_X;
import static am2.extensions.DataDefinitions.MARK_Y;
import static am2.extensions.DataDefinitions.MARK_Z;
import static am2.extensions.DataDefinitions.MAX_MANA;
import static am2.extensions.DataDefinitions.MAX_MANA_FATIGUE;

import java.util.ArrayList;

import com.google.common.base.Optional;

import am2.api.extensions.IEntityExtension;
import am2.config.AM2Config;
import am2.defs.SkillDefs;
import am2.spell.ContingencyType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class EntityExtension implements IEntityExtension, ICapabilityProvider, ICapabilitySerializable<NBTBase> {

	public static final ResourceLocation ID = new ResourceLocation("arsmagica2:ExtendedProp");

	@CapabilityInject(value = IEntityExtension.class)
	public static Capability<IEntityExtension> INSTANCE = null;
	
	private ArrayList<Integer> summon_ent_ids = new ArrayList<Integer>();
	
	public float TK_Distance = 8;
	
	private Entity entity;
		
	public boolean hasEnoughtMana(int cost) {
		if (getCurrentMana() < cost)
			return false;
		return true;
	}
	
	public void setContingency (ContingencyType type, ItemStack stack) {
		entity.getDataManager().set(CONTENGENCY_TYPE, type.name().toLowerCase());
		entity.getDataManager().set(CONTENGENCY_STACK, Optional.fromNullable(stack));
	}
	
	public ContingencyType getContingencyType() {
		return ContingencyType.fromName(entity.getDataManager().get(CONTENGENCY_TYPE));
	}
	
	public ItemStack getContingencyStack() {
		return entity.getDataManager().get(CONTENGENCY_STACK).get();
	}
	
	public double getMarkX() {
		return (double) entity.getDataManager().get(MARK_X);
	}
	
	public double getMarkY() {
		return (double) entity.getDataManager().get(MARK_Y);
	}
	
	public double getMarkZ() {
		return (double) entity.getDataManager().get(MARK_Z);
	}
	
	public int getMarkDimensionID() {
		return entity.getDataManager().get(MARK_DIMENSION);
	}
	
	public float getCurrentMana() {
		return entity.getDataManager().get(CURRENT_MANA);
	}
	
	public int getCurrentLevel() {
		return entity.getDataManager().get(CURRENT_LEVEL);
	}
	
	public float getCurrentBurnout() {
		return entity.getDataManager().get(CURRENT_MANA_FATIGUE);
	}
	
	public int getCurrentSummons() {
		return entity.getDataManager().get(CURRENT_SUMMONS);
	}
	
	public float getCurrentXP() {
		return entity.getDataManager().get(CURRENT_XP);
	}
	
	public int getHealCooldown() {
		return entity.getDataManager().get(HEAL_COOLDOWN);
	}
	
	public void lowerHealCooldown(int amount) {
		setHealCooldown(getHealCooldown() - amount);
		if (getHealCooldown() < 0)
			setHealCooldown(0);
	}
	
	public void placeHealOnCooldown() {
		entity.getDataManager().set(HEAL_COOLDOWN, AM2Config.healCooldown);
	}
	
	public void lowerAffinityHealCooldown (int amount) {
		setAffinityHealCooldown(getAffinityHealCooldown() - amount);
		if (getAffinityHealCooldown() < 0)
			setAffinityHealCooldown(0);
	}
	
	public int getAffinityHealCooldown() {
		return entity.getDataManager().get(AFFINITY_HEAL_COOLDOWN);
	}
	
	public void placeAffinityHealOnCooldown(boolean full) {
		entity.getDataManager().set(AFFINITY_HEAL_COOLDOWN, full ? AM2Config.healCooldown : AM2Config.healCooldown / 2);
	}
	
	public boolean useMana (int toUse) {
		boolean canUse = toUse <= getCurrentMana();
		if (canUse) setCurrentMana(getCurrentMana() - toUse);
		return canUse;
	}
	
	public float getMaxMana() {
		return entity.getDataManager().get(MAX_MANA);
	}
	
	public float getMaxXP () {
		return AM2Config.XPNeedDefault + (getCurrentLevel() * AM2Config.XPNeedGrowth);
	}
	
	public float getMaxBurnout () {
		return entity.getDataManager().get(MAX_MANA_FATIGUE);
	}
	
	public void setAffinityHealCooldown(int affinityHealCooldown) {
		entity.getDataManager().set(AFFINITY_HEAL_COOLDOWN, affinityHealCooldown);
	}
	
	public void setCurrentBurnout(float currentBurnout) {
		entity.getDataManager().set(CURRENT_MANA_FATIGUE, currentBurnout);
	}
	
	public void setCurrentLevel(int currentLevel) {
		entity.getDataManager().set(CURRENT_LEVEL, currentLevel);
	}
	
	public void setCurrentMana(float currentMana) {
		entity.getDataManager().set(CURRENT_MANA, currentMana);
	}
	
	public void setCurrentSummons(int currentSummons) {
		entity.getDataManager().set(CURRENT_SUMMONS, currentSummons);
	}
	
	public void setCurrentXP(float currentXP) {
		entity.getDataManager().set(CURRENT_XP, currentXP);
	}
	
	public void setHealCooldown(int healCooldown) {
		entity.getDataManager().set(HEAL_COOLDOWN, healCooldown);
	}
	
	public void setMarkX(double markX) {
		entity.getDataManager().set(MARK_X, (float)markX);
	}
	
	public void setMarkY(double markY) {
		entity.getDataManager().set(MARK_Y, (float)markY);
	}
	
	public void setMarkZ(double markZ) {
		entity.getDataManager().set(MARK_Z, (float)markZ);
	}
	
	public void setMarkDimensionID(int markDimensionID) {
		entity.getDataManager().set(MARK_DIMENSION, markDimensionID);
	}
	
	public void setMark (double x, double y, double z, int dim) {
		setMarkX(x);
		setMarkY(y);
		setMarkZ(z);
		setMarkDimensionID(dim);
	}
	
	@Override
	public boolean isShrunk() {
		return entity.getDataManager().get(IS_SHRUNK);
	}
	
	@Override
	public void setShrunk(boolean shrunk) {
		entity.getDataManager().set(IS_SHRUNK, shrunk);
	}
	
	@Override
	public void setInverted(boolean isInverted) {
		entity.getDataManager().set(IS_INVERTED, isInverted);
	}
	
	@Override
	public void setFallProtection(boolean hasFallProtection) {
		entity.getDataManager().set(HAS_FALL_PROTECTION, hasFallProtection);
	}
	
	@Override
	public boolean isInverted() {
		return entity.getDataManager().get(IS_INVERTED);
	}
	
	@Override
	public boolean hasFallProtection() {
		return entity.getDataManager().get(HAS_FALL_PROTECTION);
	}
	
	@Override
	public void addEntityReference(Entity entity) {
		this.entity = entity;
	}
	
	@Override
	public void init(Entity entity) {
		this.addEntityReference(entity);
		if (this.entity instanceof EntityPlayer) {
			this.entity.getDataManager().register(CURRENT_LEVEL, 0);
			this.entity.getDataManager().register(CURRENT_MANA, 0F);
			this.entity.getDataManager().register(CURRENT_MANA_FATIGUE, 0F);
			this.entity.getDataManager().register(CURRENT_XP, 0F);
			this.entity.getDataManager().register(CURRENT_SUMMONS, 0);
		} else {
			this.entity.getDataManager().register(CURRENT_LEVEL, 0);
			this.entity.getDataManager().register(CURRENT_MANA, 100F);
			this.entity.getDataManager().register(CURRENT_MANA_FATIGUE, 0F);
			this.entity.getDataManager().register(CURRENT_XP, 0F);
			this.entity.getDataManager().register(CURRENT_SUMMONS, 0);			
		}
		this.entity.getDataManager().register(MAX_MANA, 100F);
		this.entity.getDataManager().register(MAX_MANA_FATIGUE, 38F);
		this.entity.getDataManager().register(HEAL_COOLDOWN, 0);
		this.entity.getDataManager().register(AFFINITY_HEAL_COOLDOWN, 0);
		this.entity.getDataManager().register(MARK_X, 0F);
		this.entity.getDataManager().register(MARK_Y, 0F);
		this.entity.getDataManager().register(MARK_Z, 0F);
		this.entity.getDataManager().register(MARK_DIMENSION, -512);
		this.entity.getDataManager().register(CONTENGENCY_STACK, Optional.<ItemStack>absent());
		this.entity.getDataManager().register(CONTENGENCY_TYPE, "NULL");
		this.entity.getDataManager().register(HAS_FALL_PROTECTION, false);
		this.entity.getDataManager().register(IS_INVERTED, false);
		this.entity.getDataManager().register(IS_SHRUNK, false);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == INSTANCE;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == INSTANCE)
			return (T) this;
		return null;
	}
	
	public static IEntityExtension For(EntityLivingBase thePlayer) {
		return thePlayer.getCapability(INSTANCE, null);
	}
	
	@Override
	public NBTBase serializeNBT() {
		return new IEntityExtension.Storage().writeNBT(INSTANCE, this, null);
	}

	@Override
	public void deserializeNBT(NBTBase nbt) {
		new IEntityExtension.Storage().readNBT(INSTANCE, this, null, nbt);
	}

	@Override
	public boolean canHeal() {
		return getHealCooldown() <= 0;
	}

	@Override
	public int getMaxSummons() {
		int maxSummons = 1;
		if (entity instanceof EntityPlayer && SkillData.For((EntityPlayer)entity).hasSkill(SkillDefs.EXTRA_SUMMONS.getID()));
			maxSummons++;
		return maxSummons;
	}

	@Override
	public boolean addSummon(EntityCreature entityliving) {
		if (!entity.worldObj.isRemote){
			summon_ent_ids.add(entity.getEntityId());
			setCurrentSummons(getCurrentSummons() + 1);
		}
		return true;
	}

	@Override
	public boolean getCanHaveMoreSummons() {
//		if (entity instanceof EntityLifeGuardian)
//			return true;

		verifySummons();

		return this.getCurrentSummons() < getMaxSummons();
	}
	
	private void verifySummons(){
		for (int i = 0; i < summon_ent_ids.size(); ++i){
			int id = summon_ent_ids.get(i);
			Entity e = entity.worldObj.getEntityByID(id);
			if (e == null || !(e instanceof EntityLivingBase)){
				summon_ent_ids.remove(i);
				i--;
				removeSummon();
			}
		}
	}
	
	public boolean removeSummon(){
		if (getCurrentSummons() == 0){
			return false;
		}
		if (!entity.worldObj.isRemote){
			setCurrentSummons(getCurrentSummons() - 1);
		}
		return true;
	}
}
