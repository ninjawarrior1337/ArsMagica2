package am2.api.extensions;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import am2.lore.ArcaneCompendium;
import am2.lore.CompendiumEntry;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public interface IArcaneCompendium {
	
	/**
	 * Unlocks a compendium entry
	 * @param entry : id of the entry
	 */
	public void unlockEntry(String entry);
	
	/**
	 * Unlocks a category
	 * @param category : id of the category
	 */
	public void unlockCategory(String category);
	
	/**
	 * Unlock related entries to this one
	 * @param crafting
	 */
	public void unlockRelatedItems(ItemStack crafting);
	
	/**
	 * 
	 * @param string
	 * @return true if the entry/category is unlocked
	 */
	public boolean isUnlocked(String string);
	
	public static class Storage implements IStorage<IArcaneCompendium> {

		@Override
		public NBTBase writeNBT(Capability<IArcaneCompendium> capability, IArcaneCompendium instance, EnumFacing side) {
			return null;
		}

		@Override
		public void readNBT(Capability<IArcaneCompendium> capability, IArcaneCompendium instance, EnumFacing side,
				NBTBase nbt) {
			
		}
	}
	
	public static class Factory implements Callable<IArcaneCompendium> {

		@SuppressWarnings("deprecation")
		@Override
		public IArcaneCompendium call() throws Exception {
			return new ArcaneCompendium();
		}
		
	}

	public ArrayList<CompendiumEntry> getEntriesForCategory(String categoryName);

	public boolean isNew(String id);

	public void write();

}
