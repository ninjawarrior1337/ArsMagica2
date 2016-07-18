package am2.api.extensions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import am2.api.affinity.Affinity;
import am2.extensions.AffinityData;
import am2.utils.NBTUtils;

public interface IAffinityData {	
	
	public float getAffinityDepth(Affinity aff);
	
	public void setAffinityDepth (Affinity name, float value);
	
	public HashMap<Affinity, Float> getAffinities();

	public void init(EntityPlayer entity);
	
	public boolean getIceBridgeState();
	
	public boolean hasNightVisionActive();
	
	public void setIceBridgeState(boolean bool);
	
	public void setNightVisionState(boolean bool);
	
	public static class Storage implements IStorage<IAffinityData> {
		
		@Override
		public NBTBase writeNBT(Capability<IAffinityData> capability, IAffinityData instance, EnumFacing side) {
			NBTTagCompound nbt = new NBTTagCompound();
			for (Entry<Affinity, Float> entry : instance.getAffinities().entrySet()) {
				Affinity.writeToNBT(nbt, entry.getKey(), entry.getValue());
			}
			NBTTagCompound am2Tag = NBTUtils.getAM2Tag(nbt);
			NBTTagList cooldowns = NBTUtils.addCompoundList(am2Tag, "Cooldowns");
			for (Entry<String, Integer> entry : instance.getCooldowns().entrySet()) {
				NBTTagCompound tmp = new NBTTagCompound();
				tmp.setString("Name", entry.getKey());
				tmp.setInteger("Value", entry.getValue());
				cooldowns.appendTag(tmp);
			}
			am2Tag.setTag("Cooldowns", cooldowns);
			return nbt;
		}

		@Override
		public void readNBT(Capability<IAffinityData> capability, IAffinityData instance, EnumFacing side, NBTBase nbt) {
			ArrayList<Affinity> affinities = Affinity.readFromNBT((NBTTagCompound) nbt);
			for (Affinity aff : affinities) {
				instance.setAffinityDepth(aff, aff.readDepth((NBTTagCompound) nbt));
			}
			NBTTagCompound am2Tag = NBTUtils.getAM2Tag((NBTTagCompound) nbt);
			NBTTagList cooldowns = NBTUtils.addCompoundList(am2Tag, "Cooldowns");
			for (int i = 0; i < cooldowns.tagCount(); i++) {
				NBTTagCompound tmp = cooldowns.getCompoundTagAt(i);
				instance.addCooldown(tmp.getString("Name"), tmp.getInteger("Value"));
			}
		}
	}
	
	public static class Factory implements Callable<IAffinityData> {
		@Override
		public IAffinityData call() throws Exception {
			return new AffinityData();
		}
	}

	public Affinity[] getHighestAffinities();

	public float getDiminishingReturnsFactor();

	public void tickDiminishingReturns();

	public void addDiminishingReturns(boolean isChanneled);

	public void addCooldown(String name, int cooldown);

	public Map<String, Integer> getCooldowns();

	public int getCooldown(String name);
}
