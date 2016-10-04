package am2.api.extensions;

import java.util.concurrent.Callable;

import am2.extensions.datamanager.DataSyncExtension;
import am2.extensions.datamanager.SavedObject;
import am2.packet.AMDataReader;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public interface IDataSyncExtension extends ICapabilityProvider{
	
	public static class Factory implements Callable<IDataSyncExtension> {
		@Override
		public IDataSyncExtension call() throws Exception {
			return new DataSyncExtension();
		}
	}
	
	public class Storage implements IStorage<IDataSyncExtension> {

		@Override
		public NBTBase writeNBT(Capability<IDataSyncExtension> capability, IDataSyncExtension instance, EnumFacing side) {
			return new NBTTagCompound();
		}

		@Override
		public void readNBT(Capability<IDataSyncExtension> capability, IDataSyncExtension instance, EnumFacing side, NBTBase nbt) {
			
		}

	}
	public void init(Entity entity);
	/**
	 * Get the data stored at some variable.
	 * 
	 * @param data : the data object
	 * @return the stored thing
	 */
	public <T> T get(SavedObject<T> data);
	public <T> void set(SavedObject<T> data, T object);
	public <T> void register(SavedObject<T> data, T defaultValue);
	public byte[] createUpdatePacket();
	public void handleUpdatePacket(AMDataReader in);
	
}
