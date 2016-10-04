package am2.extensions.datamanager;

import java.util.HashMap;
import java.util.Map.Entry;

import am2.api.extensions.IDataSyncExtension;
import am2.packet.AMDataReader;
import am2.packet.AMDataWriter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class DataSyncExtension implements IDataSyncExtension {
	
	public static final ResourceLocation ID = new ResourceLocation("arsmagica2:DataSync");
	
	@CapabilityInject(value = IDataSyncExtension.class)
	public static Capability<IDataSyncExtension> INSTANCE = null;
	
	private HashMap<Integer, Object> internalData = new HashMap<>();
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
		return (T) internalData.get(data.getId());
	}

	@Override
	public <T> void set(SavedObject<T> data, T object) {
		if (!internalData.containsKey(Integer.valueOf(data.getId())))
			throw new IllegalStateException("Item with id " + data.getId() + " isn\'t registered!");
		internalData.put(data.getId(), object);
	}

	@Override
	public <T> void register(SavedObject<T> data, T defaultValue) {
		if (internalData.containsKey(Integer.valueOf(data.getId())))
			throw new IllegalStateException("Item with id " + data.getId() + " is already registered");
		internalData.put(data.getId(), defaultValue);
	}

	@SuppressWarnings("unchecked")
	@Override
	public byte[] createUpdatePacket() {
		AMDataWriter writer = new AMDataWriter();
		writer.add(entity.getEntityId());
		writer.add(internalData.size());
		for (Entry<Integer, ?> entry : internalData.entrySet()) {
			writer.add(entry.getKey());
			try {
				ArsMagicaManager.getById(entry.getKey()).serialize(writer, entry.getValue());
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		return writer.generate();
	}

	@Override
	public void handleUpdatePacket(AMDataReader reader) {
		int size = reader.getInt();
		internalData.clear();
		for (int i = 0; i < size; i++) {
			try {
				ArsMagicaManager.getById(reader.getInt()).deserialize(reader);
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}

}
