package am2.extensions;

import java.util.HashMap;
import java.util.Map.Entry;

import am2.api.ArsMagicaAPI;
import am2.api.affinity.Affinity;
import am2.api.extensions.IAffinityData;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class AffinityData implements IAffinityData, ICapabilityProvider, ICapabilitySerializable<NBTBase> {
	public static final ResourceLocation ID = new ResourceLocation("arsmagica2:AffinityData");
	private EntityPlayer player;
	
	@CapabilityInject(value = IAffinityData.class)
	public static Capability<IAffinityData> INSTANCE = null;
		
	public static IAffinityData For(EntityLivingBase living){
		return living.getCapability(INSTANCE, null);
	}
	
	public float getAffinityDepth(Affinity aff) {
		return player.getDataManager().get(DataDefinitions.AFFINITY_DATA).get(aff);
	}
	
	public void setAffinityDepth (Affinity name, float value) {
		HashMap<Affinity, Float> map = player.getDataManager().get(DataDefinitions.AFFINITY_DATA);
		map.put(name, value);
		player.getDataManager().set(DataDefinitions.AFFINITY_DATA, map);
	}
	
	public HashMap<Affinity, Float> getAffinities() {
		return player.getDataManager().get(DataDefinitions.AFFINITY_DATA);
	}

	public void init(EntityPlayer entity) {
		this.player = entity;
		HashMap<Affinity, Float> map = new HashMap<Affinity, Float>();
		for (Affinity DEPTH : ArsMagicaAPI.getAffinityRegistry().getValues())
			map.put(DEPTH, 0F);
		map.put(Affinity.NONE, 0F);
		player.getDataManager().register(DataDefinitions.AFFINITY_DATA, map);
		player.getDataManager().register(DataDefinitions.ICE_BRIDGE_STATE, false);
		player.getDataManager().register(DataDefinitions.NIGHT_VISION_ACTIVE, false);
		player.getDataManager().register(DataDefinitions.NATURE_SPEED, 0);
		player.getDataManager().register(DataDefinitions.ICE_SPEED, 0);
		player.getDataManager().register(DataDefinitions.DIMINISHING_RETURNS, 1.0F);
	}

	@Override
	public boolean getIceBridgeState() {
		return player.getDataManager().get(DataDefinitions.ICE_BRIDGE_STATE);
	}

	@Override
	public void setIceBridgeState(boolean bool) {
		player.getDataManager().set(DataDefinitions.ICE_BRIDGE_STATE, bool);
	}
	
	
	
	@Override
	public float getDiminishingReturnsFactor(){
		return player.getDataManager().get(DataDefinitions.DIMINISHING_RETURNS);
	}
	
	@Override
	public void tickDiminishingReturns(){
		if (getDiminishingReturnsFactor() < 1.2f){
			player.getDataManager().set(DataDefinitions.DIMINISHING_RETURNS, player.getDataManager().get(DataDefinitions.DIMINISHING_RETURNS) + 0.005f);
		}
	}
	
	@Override
	public void addDiminishingReturns(boolean isChanneled){
		player.getDataManager().set(DataDefinitions.DIMINISHING_RETURNS, getDiminishingReturnsFactor() - (isChanneled ? 0.1f : 0.3f));
		if (this.getDiminishingReturnsFactor() < 0) player.getDataManager().set(DataDefinitions.DIMINISHING_RETURNS, 0F);
	}

	@Override
	public int getNatureSpeed() {
		return player.getDataManager().get(DataDefinitions.NATURE_SPEED);
	}

	@Override
	public void setNatureSpeed(int speed) {
		player.getDataManager().set(DataDefinitions.NATURE_SPEED, speed);
	}

	@Override
	public int getIceSpeed() {
		return player.getDataManager().get(DataDefinitions.ICE_SPEED);
	}

	@Override
	public void setIceSpeed(int speed) {
		player.getDataManager().set(DataDefinitions.ICE_SPEED, speed);
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

	@Override
	public NBTBase serializeNBT() {
		return new IAffinityData.Storage().writeNBT(INSTANCE, this, null);
	}

	@Override
	public void deserializeNBT(NBTBase nbt) {
		new IAffinityData.Storage().readNBT(INSTANCE, this, null, nbt);
	}

	@Override
	public Affinity[] getHighestAffinities() {
		float max1 = 0;
		float max2 = 0;
		Affinity maxAff1 = Affinity.NONE;
		Affinity maxAff2 = Affinity.NONE;
		for (Entry<Affinity, Float> entry : getAffinities().entrySet()) {
			if (entry.getValue() > max1) {
				max2 = max1;
				maxAff2 = maxAff1;
				max1 = entry.getValue();
				maxAff1 = entry.getKey();
			} else if (entry.getValue() > max2) {
				max2 = entry.getValue();
				maxAff2 = entry.getKey();
			}
		}
		return new Affinity[] {maxAff1, maxAff2};
	}

	@Override
	public boolean hasNightVisionActive() {
		return player.getDataManager().get(DataDefinitions.NIGHT_VISION_ACTIVE);
	}

	@Override
	public void setNightVisionState(boolean bool) {
		player.getDataManager().set(DataDefinitions.NIGHT_VISION_ACTIVE, bool);
	}

}
