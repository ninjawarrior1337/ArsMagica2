package am2.extensions.datamanager;

import java.util.ArrayList;

import am2.api.extensions.IDataSyncExtension;
import am2.packet.AMDataReader;
import am2.packet.AMDataWriter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class DataSyncExtension implements IDataSyncExtension {
	
	public static final ResourceLocation ID = new ResourceLocation("arsmagica2:DataSync");
	
	@CapabilityInject(value = IDataSyncExtension.class)
	public static Capability<IDataSyncExtension> INSTANCE = null;
	
	private ArrayList<Object> internalData = new ArrayList<>();
	private ArrayList<Boolean> hasChanged = new ArrayList<>();
	private Entity entity;
	
	public static DataSyncExtension For(EntityLivingBase living){
		return (DataSyncExtension) living.getCapability(INSTANCE, null);
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
	public void init(Entity entity) {
		this.entity = entity;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(SavedObject<T> data) {
		fillWithNull(data.getId());
		return (T) internalData.get(Integer.valueOf(data.getId()));
	}

	@Override
	public <T> void set(SavedObject<T> data, T object) {
		fillWithNull(data.getId());
		Object checkObj = internalData.get(data.getId());
		hasChanged.set(data.getId(), object != null && !object.equals(checkObj));
		internalData.set(data.getId(), object);
	}

	@Override
	public <T> void register(SavedObject<T> data, T defaultValue) {
		fillWithNull(data.getId());
		hasChanged.set(data.getId(), true);
		internalData.set(data.getId(), defaultValue);
	}
	
	private void fillWithNull(int upTo) {
		while (internalData.size() <= upTo)
			internalData.add(null);
		while (hasChanged.size() <= upTo)
			hasChanged.add(false);
	}

	@SuppressWarnings("unchecked")
	@Override
	public byte[] createUpdatePacket() {
		AMDataWriter writer = new AMDataWriter();
		writer.add(entity.getEntityId());
		boolean fullSync = entity instanceof EntityPlayer && entity.ticksExisted % 20 == 0;
		int size = 0;
		for (int i = 0; i < internalData.size(); i++) {
			if (internalData.get(i) == null || ArsMagicaManager.getById(i) == null) continue;
			if (!fullSync && !hasChanged.get(i).booleanValue()) continue;
			size++;
		}
		writer.add(size);
		for (int i = 0; i < internalData.size(); i++) {
			if (internalData.get(i) == null || ArsMagicaManager.getById(i) == null) continue;
			if (!fullSync && !hasChanged.get(i).booleanValue()) continue;
			writer.add(i);
			try {
				ArsMagicaManager.getById(i).serialize(writer, internalData.get(i));
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		hasChanged.clear();
		fillWithNull(internalData.size());
		return writer.generate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void handleUpdatePacket(AMDataReader reader) {
		int size = reader.getInt();
		for (int i = 0; i < size; i++) {
			int index = reader.getInt();
			try {
				fillWithNull(index);
				this.set(ArsMagicaManager.getById(index), ArsMagicaManager.getById(index).deserialize(reader));
			} catch (Throwable e) {
				System.out.println("Error trying to load " + index);
			}
		}
	}

	public boolean shouldSync() {
		boolean bool = entity instanceof EntityPlayer;
		for (Boolean b : hasChanged)
			bool |= b.booleanValue();
		return bool;
	}

}
