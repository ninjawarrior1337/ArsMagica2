package am2.affinity;

import java.util.ArrayList;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.translation.I18n;
import am2.api.AffinityRegistry;
import am2.utils.NBTUtils;

@SuppressWarnings("deprecation")
public class Affinity implements Comparable<Affinity>{
	
	private static int currentMask = 1;
	private int color;
	private String name;
	private int mask;
	
//	static {
//	}
	
	public Affinity (String name, int color) {
		this.color = color;
		this.name = name;
		mask = currentMask;
		currentMask *= 2;
	}
	
	/**
	 * Rendering purpose
	 * Will return the color used by the occulus to render this affinity depth
	 * 
	 * @return the color of the affinity
	 */
	public int getColor() {
		return color;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	/**
	 * 
	 * @return the localized name of the affinity
	 */
	public String getLocalizedName() {
		return I18n.translateToLocal(getUnlocalisedName());
	}
	/**
	 * 
	 * @return the unlocalized name of the affinity (affinity.name)
	 */
	public String getUnlocalisedName() {
		return "affinity." + name;
	}
	/**
	 * Will write to an existing {@link NBTTagCompound} the data of the affinity
	 * 
	 * @param tag       Root tag of the entity / item
	 * @param affinity  The affinity to add
	 * @param depth     The the depth of this affinity
	 */
	public static void writeToNBT (NBTTagCompound tag, Affinity affinity, float depth) {
		NBTTagList affinityTag = NBTUtils.addCompoundList(NBTUtils.getAM2Tag(tag), "Affinity");
		NBTTagCompound tmp = new NBTTagCompound();
		tmp.setString("Name", affinity.name);
		tmp.setFloat("Depth", depth);
		affinityTag.appendTag(tmp);
		NBTUtils.getAM2Tag(tag).setTag("Affinity", affinityTag);
	}
	
	/**
	 * Will list all the affinities the player / item has if given a correct {@link NBTTagCompound}
	 * 
	 * @param tag       Root tag of the entity / item
	 * @return          A list of all the affinities this player / item has
	 */
	public static ArrayList<Affinity> readFromNBT (NBTTagCompound tag) {
		NBTTagList affinityTag = NBTUtils.addCompoundList(NBTUtils.getAM2Tag(tag), "Affinity");
		ArrayList<Affinity> affinities = new ArrayList<Affinity>();
		for (int i = 0; i < affinityTag.tagCount(); i++) {
			NBTTagCompound tmp = affinityTag.getCompoundTagAt(i);
			Affinity aff = AffinityRegistry.getAffinityFromName(tmp.getString("Name"));
			affinities.add(aff);
		}
		return affinities;
	}
	/**
	 * Will give the depth of an affinity the player / item has if given a correct {@link NBTTagCompound}
	 * 
	 * @param tag       Root tag of the entity / item
	 * @return          Depth in the affinity
	 */
	public float readDepth (NBTTagCompound tag) {
		NBTTagList affinityTag = NBTUtils.addCompoundList(NBTUtils.getAM2Tag(tag), "Affinity");
		for (int i = 0; i < affinityTag.tagCount(); i++) {
			NBTTagCompound tmp = affinityTag.getCompoundTagAt(i);
			if (tmp.getString("Name").equals(name))
				return tmp.getFloat("Depth");
		}
		return 0.0F;
	}

	public int getAffinityMask() {
		return mask;
	}

	public int compareTo(Affinity b) {
		return AffinityRegistry.getIdFor(b) - AffinityRegistry.getIdFor(this);
	}
	

}
