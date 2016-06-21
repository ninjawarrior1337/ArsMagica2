package am2.data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map.Entry;

import am2.lore.ArcaneCompendium;
import am2.lore.CompendiumEntry;
import am2.lore.CompendiumEntryType;
import am2.lore.CompendiumEntryTypes;
import am2.utils.NBTUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

public class DataSaver {
	
	public static NBTBase writeArcaneCompendiumUnlocks (ArcaneCompendium compendium, World world, EntityPlayer player) throws IOException {
		NBTTagCompound nbt = new NBTTagCompound();
		NBTTagList list = NBTUtils.addCompoundList(nbt, "Unlocks");
		NBTTagList categories = NBTUtils.addCompoundList(nbt, "Categories");
		for (Entry<String, CompendiumEntry> entry : ArcaneCompendium.getCompendium().entrySet()) {
			NBTTagCompound tmp = new NBTTagCompound();
			tmp.setString("ID", entry.getKey());
			tmp.setBoolean("Locked", compendium.isUnlocked(entry.getKey()));
			list.appendTag(tmp);
		}
		for (CompendiumEntryType type : CompendiumEntryTypes.categoryList()) {
			NBTTagCompound tmp = new NBTTagCompound();
			tmp.setString("ID", type.getCategoryLabel());
			tmp.setBoolean("Locked", compendium.isUnlocked(type.getCategoryLabel()));
			categories.appendTag(tmp);			
		}
		CompressedStreamTools.write(nbt, new File(world.getSaveHandler().getWorldDirectory() + "//ArsMagica2//" + player.getUniqueID() + ".dat"));
		return nbt;
	}
	
	public static ArrayList<String> readArcaneCompendiumUnlocks (World world, EntityPlayer player) throws IOException {
		ArrayList<String> out = new ArrayList<>();
		NBTTagCompound nbt = CompressedStreamTools.read(new File(world.getSaveHandler().getWorldDirectory() + "//ArsMagica2//" + player.getUniqueID() + ".dat"));
		NBTTagList list = NBTUtils.addCompoundList(nbt, "Unlocks");
		for (int i = 0; i < list.tagCount(); i++) {
			NBTTagCompound tmp = list.getCompoundTagAt(i);
			if (tmp.getBoolean("Locked"))
				out.add(tmp.getString("ID"));
			list.appendTag(tmp);
		}
		return out;
	}
	
	public static ArrayList<String> readArcaneCompendiumCategoriesUnlocks (World world, EntityPlayer player) throws IOException {
		ArrayList<String> out = new ArrayList<>();
		NBTTagCompound nbt = CompressedStreamTools.read(new File(world.getSaveHandler().getWorldDirectory() + "//ArsMagica2//" + player.getUniqueID() + ".dat"));
		NBTTagList list = NBTUtils.addCompoundList(nbt, "Categories");
		for (int i = 0; i < list.tagCount(); i++) {
			NBTTagCompound tmp = list.getCompoundTagAt(i);
			if (tmp.getBoolean("Locked"))
				out.add(tmp.getString("ID"));
			list.appendTag(tmp);
		}
		return out;
	}
}
