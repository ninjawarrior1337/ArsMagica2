package am2.api.affinity;

import java.util.ArrayList;

import am2.api.ArsMagicaAPI;
import am2.utils.NBTUtils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;

@SuppressWarnings("deprecation")
public class Affinity extends IForgeRegistryEntry.Impl<Affinity> implements Comparable<Affinity>{
	
	
	public static final Affinity NONE = new Affinity("none", 0xFFFFFF);
	public static final Affinity ARCANE = new Affinity("arcane", 0xb935cd);
	public static final Affinity WATER = new Affinity("water", 0x0b5cef);
	public static final Affinity FIRE = new Affinity("fire", 0xef260b);
	public static final Affinity EARTH = new Affinity("earth", 0x61330b);
	public static final Affinity AIR = new Affinity("air", 0x777777);
	public static final Affinity LIGHTNING = new Affinity("lightning", 0xdece19);
	public static final Affinity ICE = new Affinity("ice", 0xd3e8fc);
	public static final Affinity NATURE = new Affinity("nature", 0x228718);
	public static final Affinity LIFE = new Affinity("life", 0x34e122);
	public static final Affinity ENDER = new Affinity("ender", 0x3f043d);
	
	static {
		GameRegistry.register(NONE, new ResourceLocation("arsmagica2", "none"));
		GameRegistry.register(ARCANE, new ResourceLocation("arsmagica2", "arcane"));
		GameRegistry.register(WATER, new ResourceLocation("arsmagica2", "water"));
		GameRegistry.register(FIRE, new ResourceLocation("arsmagica2", "fire"));
		GameRegistry.register(EARTH, new ResourceLocation("arsmagica2", "earth"));
		GameRegistry.register(AIR, new ResourceLocation("arsmagica2", "air"));
		GameRegistry.register(LIGHTNING, new ResourceLocation("arsmagica2", "lightning"));
		GameRegistry.register(ICE, new ResourceLocation("arsmagica2", "ice"));
		GameRegistry.register(NATURE, new ResourceLocation("arsmagica2", "nature"));
		GameRegistry.register(LIFE, new ResourceLocation("arsmagica2", "life"));
		GameRegistry.register(ENDER, new ResourceLocation("arsmagica2", "ender"));
	}
	
	private static int currentMask = 1;
	private int color;
	private String name;
	private int mask;
	
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
		return getRegistryName().toString();
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
		tmp.setString("Name", affinity.getRegistryName().toString());
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
			Affinity aff = ArsMagicaAPI.getAffinityRegistry().getObject(new ResourceLocation(tmp.getString("Name")));
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
		return ArsMagicaAPI.getAffinityRegistry().getId(b) - ArsMagicaAPI.getAffinityRegistry().getId(this);
	}
	

}
