package am2.utils;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.math.Vec3d;

public class NBTUtils {
	
	public static NBTTagCompound getAM2Tag (NBTTagCompound baseTag) {
		return addTag(baseTag, "AM2");
	}
	
	public static NBTTagCompound getEssenceTag (NBTTagCompound baseTag) {
		return addTag(getAM2Tag(baseTag), "Essence");
	}
	
	public static void writeVecToNBT(Vec3d vec, NBTTagCompound nbt) {
		nbt.setDouble("X", vec.xCoord);
		nbt.setDouble("Y", vec.yCoord);
		nbt.setDouble("Z", vec.zCoord);
	}
	
	public static Vec3d readVecFromNBT(NBTTagCompound nbt) {
		Vec3d vec = new Vec3d(nbt.getDouble("X"), nbt.getDouble("Y"), nbt.getDouble("Z"));
		//System.out.println(vec);
		return vec;
	}
	
	public static Object getValueAt (NBTTagCompound baseTag, String tagName) {
		NBTBase base = baseTag.getTag(tagName);
		switch (base.getId()) {
		case 0: return null;
		case 1: return ((NBTTagByte)base).getByte();
		case 2: return ((NBTTagShort)base).getShort();
		case 3: return ((NBTTagInt)base).getInt();
		case 4: return ((NBTTagLong)base).getLong();
		case 5: return ((NBTTagFloat)base).getFloat();
		case 6: return ((NBTTagDouble)base).getDouble();
		case 7: return ((NBTTagByteArray)base).getByteArray();
		case 8: return ((NBTTagString)base).getString();
		case 9: return ((NBTTagCompound)base);
		case 10: return ((NBTTagIntArray)base).getIntArray();
		default:
			return null;
		}
	}
	
	public static NBTTagCompound addTag (NBTTagCompound upper, String name) {
		if (upper == null) throw new IllegalStateException("Base Tag must exist");
		NBTTagCompound newTag = new NBTTagCompound();
		if (upper.getCompoundTag(name) != null) {
			newTag = upper.getCompoundTag(name);
		}
		upper.setTag(name, newTag);
		return newTag;		
	}
	
	public static NBTTagCompound addTag (NBTTagCompound upper, NBTTagCompound compound, String name) {
		if (upper == null) throw new IllegalStateException("Base Tag must exist");
		upper.setTag(name, compound);
		return upper;		
	}
	
	public static NBTTagList addList (NBTTagCompound upper, int type, String name) {
		if (upper == null) throw new IllegalStateException("Base Tag must exist");
		NBTTagList newTag = new NBTTagList();
		if (upper.getTagList(name, type) != null) {
			newTag = upper.getTagList(name, type);
		}
		upper.setTag(name, newTag);
		return newTag;
	}
	
	public static NBTTagList addCompoundList(NBTTagCompound upper, String name) {
		return addList(upper, 10, name);
	}

}
