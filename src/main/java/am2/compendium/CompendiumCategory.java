package am2.compendium;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.ImmutableList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings("deprecation")
public final class CompendiumCategory {
	
	private static final ArrayList<CompendiumCategory> CATEGORIES = new ArrayList<>();

	public static final CompendiumCategory GUIDE = createCompendiumCategory("guide", new ResourceLocation("arsmagica2", "items/arcanecompendium"));
	public static final CompendiumCategory MECHANIC = createCompendiumCategory("mechanic", new ResourceLocation("arsmagica2", "items/magitech_goggle"));
	public static final CompendiumCategory ITEM = createCompendiumCategory("item", new ResourceLocation("arsmagica2", "items/essence_ice"));
	public static final CompendiumCategory BLOCK = createCompendiumCategory("block", new ResourceLocation("arsmagica2", "items/crystal_wrench"));
	public static final CompendiumCategory SPELL_SHAPE = createCompendiumCategory("shape", new ResourceLocation("arsmagica2", "items/spells/shapes/Binding"));
	public static final CompendiumCategory SPELL_COMPONENT = createCompendiumCategory("component", new ResourceLocation("arsmagica2", "items/spells/components/LifeTap"));
	public static final CompendiumCategory SPELL_MODIFIER = createCompendiumCategory("modifier", new ResourceLocation("arsmagica2", "items/spells/modifiers/VelocityAdded"));
	public static final CompendiumCategory TALENT = createCompendiumCategory("talent", new ResourceLocation("arsmagica2", "icons/skills/AugmentedCasting"));
	public static final CompendiumCategory MOB = createCompendiumCategory("mob", new ResourceLocation("arsmagica2", "gui_icons/Fatigue_Icon"));
	public static final CompendiumCategory STRUCTURE = createCompendiumCategory("structure", new ResourceLocation("arsmagica2", "gui_icons/gateway"));
	public static final CompendiumCategory BOSS = createCompendiumCategory("boss", new ResourceLocation("arsmagica2", "items/evilBook"));
	
	private ArrayList<CompendiumEntry> entries;
	
	public static CompendiumCategory createCompendiumCategory(String id, ResourceLocation sprite) {
		if (id == null || id.isEmpty()) throw new NullPointerException("ID Can\'t be null");
		int num = StringUtils.countMatches(id, "\\.");
		String[] parents = new String[num];
		for (int i = 0; i < num; i++) {
			parents[i] = id.split("\\.")[i];
		}
		CompendiumCategory category = new CompendiumCategory(id.split("\\.")[num], sprite, parents);
		CATEGORIES.add(category);
		
		return category;
	}
	
	private String id;
	private ResourceLocation sprite;
	
	private CompendiumCategory(String id, ResourceLocation sprite, String[] parents) {
		this.id = id;
		this.sprite = sprite;
		this.entries = new ArrayList<>();
	}
	
	public String getID() {
		return id;
	}
	
	public ResourceLocation getSprite() {
		return sprite;
	}
	
	@SideOnly(Side.CLIENT)
	public TextureAtlasSprite getTexture() {
		return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(getSprite().toString());
	}
	
	@SideOnly(Side.CLIENT)
	public void registerTexture(TextureMap map) {
		map.registerSprite(getSprite());
	}
	
	public void addEntry(CompendiumEntry entry) {
		entries.add(entry.setCategory(this));
	}
	
	public String getCategoryName() {
		return I18n.translateToLocal("compendium.category." + id + ".name");
	}
	
	public static ImmutableList<CompendiumCategory> getCategories() {
		return ImmutableList.copyOf(CATEGORIES);
	}
	
	public static CompendiumEntry getEntryByID(String id) {
		for (CompendiumCategory category : CompendiumCategory.getCategories()) {
			for (CompendiumEntry entry : category.getEntries()) {
				if (entry.getID().equals(id))
					return entry;
			}
		}
		return null;
	}
	
	public static CompendiumCategory getCategoryFromID(String id) {
		for (CompendiumCategory category : CATEGORIES) {
			if (category.id.equals(id))
				return category;
		}
		return null;
	}

	public ImmutableList<CompendiumEntry> getEntries() {
		return ImmutableList.copyOf(entries);
	}
}
