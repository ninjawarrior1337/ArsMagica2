package am2.api.extensions;

import java.util.concurrent.Callable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import am2.extensions.EntityExtension;
import am2.spell.ContingencyType;
import am2.utils.NBTUtils;

public interface IEntityExtension {
	
	public boolean hasEnoughtMana(int cost);
		
	public void setContingency (ContingencyType type, ItemStack stack);
	
	public ContingencyType getContingencyType();
	
	public ItemStack getContingencyStack();
	
	public double getMarkX();
	
	public double getMarkY();
	
	public double getMarkZ();
	
	public int getMarkDimensionID();
	
	public float getCurrentMana();
	
	public int getCurrentLevel();
	
	public float getCurrentBurnout();
	
	public int getCurrentSummons();
	
	public float getCurrentXP();
	
	public int getHealCooldown();
	
	public void lowerHealCooldown(int amount);
	
	public void placeHealOnCooldown();
	
	public void lowerAffinityHealCooldown (int amount);
	
	public int getAffinityHealCooldown();
	
	public void placeAffinityHealOnCooldown(boolean full);
	
	public boolean useMana (int toUse);
	
	public float getMaxMana();
	
	public float getMaxXP ();
	
	public float getMaxBurnout ();
	
	public void setAffinityHealCooldown(int affinityHealCooldown);
	
	public void setCurrentBurnout(float currentBurnout);
	
	public void setCurrentLevel(int currentLevel);
	
	public void setCurrentMana(float currentMana);
	
	public void setCurrentSummons(int currentSummons);
	
	public void setCurrentXP(float currentXP);
	
	public void setHealCooldown(int healCooldown);
	
	public void setMarkX(double markX);
	
	public void setMarkY(double markY);
	
	public void setMarkZ(double markZ);
	
	public void setMarkDimensionID(int markDimensionID);
	
	public void setMark (double x, double y, double z, int dim);
	
	public void setShrunk(boolean shrunk);
	
	public boolean isShrunk();

	public void setInverted(boolean inverted);

	public void setFallProtection(boolean fallProtection);
	
	public boolean isInverted();
	
	public boolean hasFallProtection();
	
	public void addEntityReference(Entity entity);
	
	public void init(Entity entity);
	
	public boolean canHeal();
	
	public int getMaxSummons();

	public static class Storage implements IStorage<IEntityExtension> {
		
		@Override
		public NBTBase writeNBT(Capability<IEntityExtension> capability, IEntityExtension instance, EnumFacing side) {
			NBTTagCompound compound = new NBTTagCompound();
			NBTTagCompound am2tag = NBTUtils.getAM2Tag(compound);
			am2tag.setFloat("CurrentMana", instance.getCurrentMana());
			am2tag.setInteger("CurrentLevel", instance.getCurrentLevel());
			am2tag.setFloat("CurrentXP", instance.getCurrentMana());
			am2tag.setFloat("CurrentBurnout", instance.getCurrentBurnout());
			am2tag.setInteger("CurrentSummons", instance.getCurrentSummons());
			
			am2tag.setInteger("HealCooldown", instance.getHealCooldown());
			am2tag.setInteger("AffinityHealCooldown", instance.getAffinityHealCooldown());
			
			am2tag.setBoolean("Shrunk", instance.isShrunk());
			am2tag.setBoolean("Inverted", instance.isInverted());
			am2tag.setBoolean("FallProtection", instance.hasFallProtection());
			
			am2tag.setDouble("MarkX", instance.getMarkX());
			am2tag.setDouble("MarkY", instance.getMarkY());
			am2tag.setDouble("MarkZ", instance.getMarkZ());
			am2tag.setInteger("MarkDimensionId", instance.getMarkDimensionID());
			NBTTagCompound contingencyTag = NBTUtils.addTag(am2tag, "Contingency");
			if (instance.getContingencyType() != ContingencyType.NULL) {
				contingencyTag.setString("Type", instance.getContingencyType().name().toLowerCase());
				contingencyTag.setTag("Stack", instance.getContingencyStack().writeToNBT(new NBTTagCompound()));
			} else {
				contingencyTag.setString("Type", "null");			
			}
			return compound;
		}
	
		@Override
		public void readNBT(Capability<IEntityExtension> capability, IEntityExtension instance, EnumFacing side, NBTBase nbt) {
			NBTTagCompound am2tag = NBTUtils.getAM2Tag((NBTTagCompound)nbt);
			instance.setCurrentMana(am2tag.getInteger("CurrentMana"));
			instance.setCurrentLevel(am2tag.getInteger("CurrentLevel"));
			instance.setCurrentXP(am2tag.getInteger("CurrentXP"));
			instance.setCurrentBurnout(am2tag.getInteger("CurrentBurnout"));
			instance.setCurrentSummons(am2tag.getInteger("CurrentSummons"));
			
			instance.setHealCooldown(am2tag.getInteger("HealCooldown"));
			instance.setAffinityHealCooldown(am2tag.getInteger("AffinityHealCooldown"));
			
			instance.setShrunk(am2tag.getBoolean("Shrunk"));
			instance.setInverted(am2tag.getBoolean("Inverted"));
			instance.setFallProtection(am2tag.getBoolean("FallProtection"));
			
			instance.setMarkX(am2tag.getDouble("MarkX"));
			instance.setMarkY(am2tag.getDouble("MarkY"));
			instance.setMarkZ(am2tag.getDouble("MarkZ"));
			instance.setMarkDimensionID(am2tag.getInteger("MarkDimensionId"));
			NBTTagCompound contingencyTag = NBTUtils.addTag(am2tag, "Contingency");
			if (!contingencyTag.hasKey("Type") || !contingencyTag.getString("Type").equals("null")) {
				instance.setContingency(ContingencyType.fromName(contingencyTag.getString("Type")), ItemStack.loadItemStackFromNBT(contingencyTag.getCompoundTag("Stack")));
			} else {
				instance.setContingency(ContingencyType.NULL, null);
			}
		}
	}
	
	public static class Factory implements Callable<IEntityExtension> {

		@Override
		public IEntityExtension call() throws Exception {
			return new EntityExtension();
		}
		
	}

	public boolean addSummon(EntityCreature entityliving);

	public boolean getCanHaveMoreSummons();

}
