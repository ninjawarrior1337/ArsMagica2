package am2.extensions;

import static am2.extensions.DataDefinitions.AFFINITY_HEAL_COOLDOWN;
import static am2.extensions.DataDefinitions.CONTENGENCY_STACK;
import static am2.extensions.DataDefinitions.CONTENGENCY_TYPE;
import static am2.extensions.DataDefinitions.CURRENT_LEVEL;
import static am2.extensions.DataDefinitions.CURRENT_MANA;
import static am2.extensions.DataDefinitions.CURRENT_MANA_FATIGUE;
import static am2.extensions.DataDefinitions.CURRENT_SUMMONS;
import static am2.extensions.DataDefinitions.CURRENT_XP;
import static am2.extensions.DataDefinitions.FALL_PROTECTION;
import static am2.extensions.DataDefinitions.FLIP_ROTATION;
import static am2.extensions.DataDefinitions.HEAL_COOLDOWN;
import static am2.extensions.DataDefinitions.IS_INVERTED;
import static am2.extensions.DataDefinitions.IS_SHRUNK;
import static am2.extensions.DataDefinitions.MARK_DIMENSION;
import static am2.extensions.DataDefinitions.MARK_X;
import static am2.extensions.DataDefinitions.MARK_Y;
import static am2.extensions.DataDefinitions.MARK_Z;
import static am2.extensions.DataDefinitions.PREV_FLIP_ROTATION;
import static am2.extensions.DataDefinitions.PREV_SHRINK_PCT;
import static am2.extensions.DataDefinitions.REVERSE_INPUT;
import static am2.extensions.DataDefinitions.SHRINK_PCT;
import static am2.extensions.DataDefinitions.TK_DISTANCE;

import java.util.ArrayList;
import java.util.Iterator;

import com.google.common.base.Optional;

import am2.ArsMagica2;
import am2.api.ArsMagicaAPI;
import am2.api.extensions.IEntityExtension;
import am2.armor.ArmorHelper;
import am2.armor.ArsMagicaArmorMaterial;
import am2.armor.infusions.GenericImbuement;
import am2.armor.infusions.ImbuementRegistry;
import am2.defs.PotionEffectsDefs;
import am2.defs.SkillDefs;
import am2.packet.AMDataWriter;
import am2.packet.AMNetHandler;
import am2.packet.AMPacketIDs;
import am2.particles.AMLineArc;
import am2.spell.ContingencyType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class EntityExtension implements IEntityExtension, ICapabilityProvider, ICapabilitySerializable<NBTBase> {

	public static final ResourceLocation ID = new ResourceLocation("arsmagica2:ExtendedProp");
	private static int baseTicksForFullRegen = 2400;
	private int ticksForFullRegen = baseTicksForFullRegen;
	
	@CapabilityInject(value = IEntityExtension.class)
	public static Capability<IEntityExtension> INSTANCE = null;
	
	private ArrayList<Integer> summon_ent_ids = new ArrayList<Integer>();
	private EntityLivingBase entity;

	private ArrayList<ManaLinkEntry> manaLinks = new ArrayList<>();
		
	@Override
	public boolean hasEnoughtMana(float cost) {
		if (getCurrentMana() < cost)
			return false;
		return true;
	}
	
	@Override
	public void setContingency (ContingencyType type, ItemStack stack) {
		entity.getDataManager().set(CONTENGENCY_TYPE, type.name().toLowerCase());
		entity.getDataManager().set(CONTENGENCY_STACK, Optional.fromNullable(stack));
	}
	
	@Override
	public ContingencyType getContingencyType() {
		return ContingencyType.fromName(entity.getDataManager().get(CONTENGENCY_TYPE));
	}
	
	@Override
	public ItemStack getContingencyStack() {
		return entity.getDataManager().get(CONTENGENCY_STACK).get();
	}
	
	@Override
	public double getMarkX() {
		return (double) entity.getDataManager().get(MARK_X);
	}
	
	@Override
	public double getMarkY() {
		return (double) entity.getDataManager().get(MARK_Y);
	}
	
	@Override
	public double getMarkZ() {
		return (double) entity.getDataManager().get(MARK_Z);
	}
	
	@Override
	public int getMarkDimensionID() {
		return entity.getDataManager().get(MARK_DIMENSION);
	}
	
	@Override
	public float getCurrentMana() {
		return entity.getDataManager().get(CURRENT_MANA);
	}
	
	@Override
	public int getCurrentLevel() {
		return entity.getDataManager().get(CURRENT_LEVEL);
	}
	
	@Override
	public float getCurrentBurnout() {
		return entity.getDataManager().get(CURRENT_MANA_FATIGUE);
	}
	
	@Override
	public int getCurrentSummons() {
		return entity.getDataManager().get(CURRENT_SUMMONS);
	}
	
	@Override
	public float getCurrentXP() {
		return entity.getDataManager().get(CURRENT_XP);
	}
	
	@Override
	public int getHealCooldown() {
		return entity.getDataManager().get(HEAL_COOLDOWN);
	}
	
	@Override
	public void lowerHealCooldown(int amount) {
		setHealCooldown(getHealCooldown() - amount);
		if (getHealCooldown() < 0)
			setHealCooldown(0);
	}
	
	@Override
	public void placeHealOnCooldown() {
		entity.getDataManager().set(HEAL_COOLDOWN, 40);
	}
	
	@Override
	public void lowerAffinityHealCooldown (int amount) {
		setAffinityHealCooldown(getAffinityHealCooldown() - amount);
		if (getAffinityHealCooldown() < 0)
			setAffinityHealCooldown(0);
	}
	
	@Override
	public int getAffinityHealCooldown() {
		return entity.getDataManager().get(AFFINITY_HEAL_COOLDOWN);
	}
	
	@Override
	public void placeAffinityHealOnCooldown(boolean full) {
		entity.getDataManager().set(AFFINITY_HEAL_COOLDOWN, 40);
	}
	
	@Override
	public boolean useMana (float toUse) {
		boolean canUse = toUse <= getCurrentMana();
		if (canUse) setCurrentMana(getCurrentMana() - toUse);
		return canUse;
	}
	
	@Override
	public float getMaxMana() {
		float mana = (float)(Math.pow(getCurrentLevel(), 1.5f) * (85f * ((float)getCurrentLevel() / 99f)) + 500f);
		if (this.entity.isPotionActive(PotionEffectsDefs.manaBoost))
			mana *= 1 + (0.25 * (this.entity.getActivePotionEffect(PotionEffectsDefs.manaBoost).getAmplifier()));
		return (float)(mana + this.entity.getAttributeMap().getAttributeInstance(ArsMagicaAPI.maxManaBonus).getAttributeValue());
	}
	
	@Override
	public float getMaxXP () {
		return (float)Math.pow(getCurrentLevel() * 0.25f, 1.5f);
	}
	
	@Override
	public float getMaxBurnout () {
		return getCurrentLevel() * 10 + 1;
	}
	
	@Override
	public void setAffinityHealCooldown(int affinityHealCooldown) {
		entity.getDataManager().set(AFFINITY_HEAL_COOLDOWN, affinityHealCooldown);
	}
	
	@Override
	public void setCurrentBurnout(float currentBurnout) {
		entity.getDataManager().set(CURRENT_MANA_FATIGUE, currentBurnout);
	}
	
	@Override
	public void setCurrentLevel(int currentLevel) {
		ticksForFullRegen = (int)Math.round(baseTicksForFullRegen * (0.75 - (0.25 * (getCurrentLevel() / 99f))));
		entity.getDataManager().set(CURRENT_LEVEL, currentLevel);
	}
	
	@Override
	public void setCurrentMana(float currentMana) {
		entity.getDataManager().set(CURRENT_MANA, Math.max(currentMana, 0));
	}
	
	@Override
	public void setCurrentSummons(int currentSummons) {
		entity.getDataManager().set(CURRENT_SUMMONS, currentSummons);
	}
	
	@Override
	public void setCurrentXP(float currentXP) {
		if (currentXP >= this.getMaxXP()) {
			currentXP -= this.getMaxXP();
			setMagicLevelWithMana(getCurrentLevel() + 1);
		}
		entity.getDataManager().set(CURRENT_XP, currentXP);
	}
	
	@Override
	public void setHealCooldown(int healCooldown) {
		entity.getDataManager().set(HEAL_COOLDOWN, healCooldown);
	}
	
	@Override
	public void setMarkX(double markX) {
		entity.getDataManager().set(MARK_X, (float)markX);
	}
	
	@Override
	public void setMarkY(double markY) {
		entity.getDataManager().set(MARK_Y, (float)markY);
	}
	
	@Override
	public void setMarkZ(double markZ) {
		entity.getDataManager().set(MARK_Z, (float)markZ);
	}
	
	@Override
	public void setMarkDimensionID(int markDimensionID) {
		entity.getDataManager().set(MARK_DIMENSION, markDimensionID);
	}
	
	@Override
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
	public void setFallProtection(float hasFallProtection) {
		entity.getDataManager().set(FALL_PROTECTION, hasFallProtection);
	}
	
	@Override
	public boolean isInverted() {
		return entity.getDataManager().get(IS_INVERTED);
	}
	
	@Override
	public float getFallProtection() {
		return entity.getDataManager().get(FALL_PROTECTION);
	}
	
	@Override
	public void addEntityReference(EntityLivingBase entity) {
		this.entity = entity;
	}
	
	@Override
	public void init(EntityLivingBase entity) {
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
		this.entity.getDataManager().register(HEAL_COOLDOWN, 0);
		this.entity.getDataManager().register(AFFINITY_HEAL_COOLDOWN, 0);
		this.entity.getDataManager().register(MARK_X, 0F);
		this.entity.getDataManager().register(MARK_Y, 0F);
		this.entity.getDataManager().register(MARK_Z, 0F);
		this.entity.getDataManager().register(MARK_DIMENSION, -512);
		this.entity.getDataManager().register(CONTENGENCY_STACK, Optional.<ItemStack>absent());
		this.entity.getDataManager().register(CONTENGENCY_TYPE, "NULL");
		this.entity.getDataManager().register(FALL_PROTECTION, 0.0f);
		this.entity.getDataManager().register(IS_INVERTED, false);
		this.entity.getDataManager().register(IS_SHRUNK, false);
		this.entity.getDataManager().register(FLIP_ROTATION, 0.0f);
		this.entity.getDataManager().register(PREV_FLIP_ROTATION, 0.0f);
		this.entity.getDataManager().register(SHRINK_PCT, 0.0f);
		this.entity.getDataManager().register(PREV_SHRINK_PCT, 0.0f);
		this.entity.getDataManager().register(TK_DISTANCE, 8.0f);
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
	
	@Override
	public boolean removeSummon(){
		if (getCurrentSummons() == 0){
			return false;
		}
		if (!entity.worldObj.isRemote){
			setCurrentSummons(getCurrentSummons() - 1);
		}
		return true;
	}
	
	@Override
	public void updateManaLink(EntityLivingBase entity){
		ManaLinkEntry mle = new ManaLinkEntry(entity.getEntityId(), 20);
		if (!this.manaLinks .contains(mle))
			this.manaLinks.add(mle);
		else
			this.manaLinks.remove(mle);
	}
	
	@Override
	public void deductMana(float manaCost){
		float leftOver = manaCost - getCurrentMana();
		this.setCurrentMana(getCurrentMana() - manaCost);
		if (leftOver > 0){
			for (ManaLinkEntry entry : this.manaLinks){
				leftOver -= entry.deductMana(entity.worldObj, entity, leftOver);
				if (leftOver <= 0)
					break;
			}
		}
	}
	
	@Override
	public void cleanupManaLinks(){
		Iterator<ManaLinkEntry> it = this.manaLinks.iterator();
		while (it.hasNext()){
			ManaLinkEntry entry = it.next();
			Entity e = this.entity.worldObj.getEntityByID(entry.entityID);
			if (e == null)
				it.remove();
		}
	}
	
	@Override
	public float getBonusCurrentMana(){
		float bonus = 0;
		for (ManaLinkEntry entry : this.manaLinks){
			bonus += entry.getAdditionalCurrentMana(entity.worldObj, entity);
		}
		return bonus;
	}

	@Override
	public float getBonusMaxMana(){
		float bonus = 0;
		for (ManaLinkEntry entry : this.manaLinks){
			bonus += entry.getAdditionalMaxMana(entity.worldObj, entity);
		}
		return bonus;
	}
	
	@Override
	public boolean isManaLinkedTo(EntityLivingBase entity){
		for (ManaLinkEntry entry : manaLinks){
			if (entry.entityID == entity.getEntityId())
				return true;
		}
		return false;
	}
	
	@Override
	public void spawnManaLinkParticles(){
		if (entity.worldObj != null && entity.worldObj.isRemote){
			for (ManaLinkEntry entry : this.manaLinks){
				Entity e = entity.worldObj.getEntityByID(entry.entityID);
				if (e != null && e.getDistanceSqToEntity(entity) < entry.range && e.ticksExisted % 90 == 0){
					AMLineArc arc = (AMLineArc)ArsMagica2.proxy.particleManager.spawn(entity.worldObj, "textures/blocks/oreblockbluetopaz.png", e, entity);
					if (arc != null){
						arc.setIgnoreAge(false);
						arc.setRBGColorF(0.17f, 0.88f, 0.88f);
					}
				}
			}
		}
	}
	
	private class ManaLinkEntry{
		private final int entityID;
		private final int range;

		public ManaLinkEntry(int entityID, int range){
			this.entityID = entityID;
			this.range = range * range;
		}

		private EntityLivingBase getEntity(World world){
			Entity e = world.getEntityByID(entityID);
			if (e == null || !(e instanceof EntityLivingBase))
				return null;
			return (EntityLivingBase)e;
		}

		public float getAdditionalCurrentMana(World world, Entity host){
			EntityLivingBase e = getEntity(world);
			if (e == null || e.getDistanceSqToEntity(host) > range)
				return 0;
			return For(e).getCurrentMana();
		}

		public float getAdditionalMaxMana(World world, Entity host){
			EntityLivingBase e = getEntity(world);
			if (e == null || e.getDistanceSqToEntity(host) > range)
				return 0;
			return For(e).getMaxMana();
		}

		public float deductMana(World world, Entity host, float amt){
			EntityLivingBase e = getEntity(world);
			if (e == null || e.getDistanceSqToEntity(host) > range)
				return 0;
			amt = Math.min(For(e).getCurrentMana(), amt);
			For(e).deductMana(amt);
			return amt;
		}

		@Override
		public int hashCode(){
			return entityID;
		}

		@Override
		public boolean equals(Object obj){
			if (obj instanceof ManaLinkEntry)
				return ((ManaLinkEntry)obj).entityID == this.entityID;
			return false;
		}
	}

	@Override
	public boolean shouldReverseInput() {
		return entity.getDataManager().get(REVERSE_INPUT);
	}

	@Override
	public boolean getIsFlipped() {
		return entity.getDataManager().get(IS_INVERTED);
	}

	@Override
	public float getFlipRotation() {
		return entity.getDataManager().get(FLIP_ROTATION);
	}

	@Override
	public float getPrevFlipRotation() {
		return entity.getDataManager().get(PREV_FLIP_ROTATION);
	}

	@Override
	public float getShrinkPct() {
		return entity.getDataManager().get(SHRINK_PCT);
	}

	@Override
	public float getPrevShrinkPct() {
		return entity.getDataManager().get(PREV_SHRINK_PCT);
	}

	@Override
	public void setTKDistance(float TK_Distance) {
		entity.getDataManager().set(TK_DISTANCE, TK_Distance);
	}

	@Override
	public void addToTKDistance(float toAdd) {
		setTKDistance(getTKDistance() + toAdd);
	}

	@Override
	public float getTKDistance() {
		return entity.getDataManager().get(TK_DISTANCE);
	}
	
	@Override
	public void syncTKDistance() {
		AMDataWriter writer = new AMDataWriter();
		writer.add(this.getTKDistance());
		AMNetHandler.INSTANCE.sendPacketToServer(AMPacketIDs.TK_DISTANCE_SYNC, writer.generate());
	}
	
	@Override
	public void manaBurnoutTick(){
		float actualMaxMana = getMaxMana();
		if (getCurrentMana() < actualMaxMana) {
			if (entity instanceof EntityPlayer && ((EntityPlayer) entity).capabilities.isCreativeMode) {
				setCurrentMana(actualMaxMana);
			} else {
				if (getCurrentMana() < 0) {
					setCurrentMana(0);
				}

				int regenTicks = (int) Math.ceil(ticksForFullRegen * entity.getAttributeMap()
						.getAttributeInstance(ArsMagicaAPI.manaRegenTimeModifier).getAttributeValue());

				if (entity.isPotionActive(PotionEffectsDefs.manaRegen)) {
					PotionEffect pe = entity.getActivePotionEffect(PotionEffectsDefs.manaRegen);
					regenTicks *= (1.0f - Math.max(0.9f, (0.25 * (pe.getAmplifier()))));
				}

				if (entity instanceof EntityPlayer) {
					EntityPlayer player = (EntityPlayer) entity;
					int armorSet = ArmorHelper.getFullArsMagicaArmorSet(player);
					if (armorSet == ArsMagicaArmorMaterial.MAGE.getMaterialID()) {
						regenTicks *= 0.8;
					} else if (armorSet == ArsMagicaArmorMaterial.BATTLEMAGE.getMaterialID()) {
						regenTicks *= 0.95;
					} else if (armorSet == ArsMagicaArmorMaterial.ARCHMAGE.getMaterialID()) {
						regenTicks *= 0.5;
					}

					if (SkillData.For(player).hasSkill(SkillDefs.MANA_REGEN_3.getID())) {
						regenTicks *= 0.7f;
					} else if (SkillData.For(player).hasSkill(SkillDefs.MANA_REGEN_2.getID())) {
						regenTicks *= 0.85f;
					} else if (SkillData.For(player).hasSkill(SkillDefs.MANA_REGEN_1.getID())) {
						regenTicks *= 0.95f;
					}

					int numArmorPieces = 0;
					for (int i = 0; i < 4; ++i) {
						ItemStack stack = player.inventory.armorItemInSlot(i);
						if (ImbuementRegistry.instance.isImbuementPresent(stack, GenericImbuement.manaRegen))
							numArmorPieces++;
					}
					regenTicks *= 1.0f - (0.15f * numArmorPieces);
				}

				float manaToAdd = (actualMaxMana / regenTicks);

				setCurrentMana(getCurrentMana() + manaToAdd);
			}
		}
		if (getCurrentBurnout() > 0) {
			int numArmorPieces = 0;
			if (entity instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) entity;
				for (int i = 0; i < 4; ++i) {
					ItemStack stack = player.inventory.armorItemInSlot(i);
					if (ImbuementRegistry.instance.isImbuementPresent(stack, GenericImbuement.burnoutReduction))
						numArmorPieces++;
				}
			}
			float factor = (float) ((0.01f + (0.015f * numArmorPieces)) * entity.getAttributeMap()
					.getAttributeInstance(ArsMagicaAPI.burnoutReductionRate).getAttributeValue());
			float decreaseamt = factor * getCurrentLevel();
			setCurrentBurnout(getCurrentBurnout() - decreaseamt);
			if (getCurrentBurnout() < 0) {
				setCurrentBurnout(0);
			}
		}
	}
	
	@Override
	public boolean setMagicLevelWithMana(int level){

		if (level > 99) level = 99;
		if (level < 0) level = 0;
		setCurrentLevel(level);
		setCurrentMana(getMaxMana());
		setCurrentBurnout(0);
		return true;
	}
}
