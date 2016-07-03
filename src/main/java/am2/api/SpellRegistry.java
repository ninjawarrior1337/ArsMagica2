package am2.api;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;

import com.google.common.collect.ImmutableMap;

import am2.lore.ArcaneCompendium;
import am2.skill.Skill;
import am2.skill.SkillPoint;
import am2.skill.SkillTree;
import am2.spell.IComponent;
import am2.spell.IModifier;
import am2.spell.IShape;
import am2.spell.ISpellPart;
import am2.spell.SpellModifiers;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/**
 * Contains all spell parts, used for both registration<BR>
 * Skill are automatically created when doing any thing
 *
 */
public class SpellRegistry {
	
	private static final HashMap<String, SpellData<IComponent>> componentMap = new HashMap<String, SpellRegistry.SpellData<IComponent>>();
	private static final HashMap<String, SpellData<IModifier>> modifierMap = new HashMap<String, SpellRegistry.SpellData<IModifier>>();
	private static final HashMap<String, SpellData<IShape>> shapeMap = new HashMap<String, SpellRegistry.SpellData<IShape>>();
	
	/**
	 * Register a spell component
	 * 
	 * @param id : Name of this component
	 * @param icon : Icon
	 * @param tier : Skill Point required to unlock
	 * @param part : Actual Component, use new {@link IComponent} ()
	 * @param tree : Skill Tree
	 * @param posX : Position in the tree
	 * @param posY : Position in the tree
	 * @param parents : Skills that need to be unlocked before this one (occulus only)
	 */
	public static void registerSpellComponent (String id, ResourceLocation icon, SkillPoint tier, IComponent part, SkillTree tree, int posX, int posY, EnumSet<SpellModifiers> mods, String... parents) {
		id = id.toLowerCase();
		componentMap.put(id, new SpellData<IComponent>(icon, part, id));
		SkillRegistry.registerSkill(false, id, icon, tier, posX, posY, tree, parents);
		ArcaneCompendium.AddCompendiumEntry(part, id, mods, false);		
	}
	
	/**
	 * Register a spell modifier
	 * 
	 * @param id : Name of this modifier
	 * @param icon : Icon
	 * @param tier : Skill Point required to unlock
	 * @param part : Actual Modifier, use new {@link IModifier} ()
	 * @param tree : Skill Tree
	 * @param posX : Position in the tree
	 * @param posY : Position in the tree
	 * @param parents : Skills that need to be unlocked before this one (occulus only)
	 */
	public static void registerSpellModifier (String id, ResourceLocation icon, SkillPoint tier, IModifier part, SkillTree tree, int posX, int posY, String... parents) {
		id = id.toLowerCase();
		modifierMap.put(id, new SpellData<IModifier>(icon, part, id));
		SkillRegistry.registerSkill(false, id, icon, tier, posX, posY, tree, parents);
		ArcaneCompendium.AddCompendiumEntry(part, id, null, false);
	}
	
	/**
	 * Register a spell shape
	 * 
	 * @param id : Name of this shape
	 * @param icon : Icon
	 * @param tier : Skill Point required to unlock
	 * @param part : Actual Shape, use new {@link IShape} ()
	 * @param tree : Skill Tree
	 * @param posX : Position in the tree
	 * @param posY : Position in the tree
	 * @param parents : Skills that need to be unlocked before this one (occulus only)
	 */
	public static void registerSpellShape (String id, ResourceLocation icon, SkillPoint tier, IShape part, SkillTree tree, int posX, int posY, EnumSet<SpellModifiers> mods, String... parents) {
		id = id.toLowerCase();
		shapeMap.put(id, new SpellData<IShape>(icon, part, id));
		SkillRegistry.registerSkill(false, id, icon, tier, posX, posY, tree, parents);
		ArcaneCompendium.AddCompendiumEntry(part, id, mods, false);
	}
	
	/**
	 * Get a component from the name
	 * 
	 * @param name : Name of the component
	 * @return the matching component
	 */
	public static SpellData<IComponent> getComponentFromName (String name) {
		return componentMap.get(name.toLowerCase());
	}

	/**
	 * Get a modifier from the name
	 * 
	 * @param name : Name of the modifier
	 * @return the matching modifier
	 */
	public static SpellData<IModifier> getModifierFromName (String name) {
		return modifierMap.get(name.toLowerCase());
	}

	/**
	 * Get a shape from the name
	 * 
	 * @param name : Name of the shape
	 * @return the matching shape
	 */
	public static SpellData<IShape> getShapeFromName (String name) {
		return shapeMap.get(name.toLowerCase());
	}
	
	public static ImmutableMap<String, SpellData<IComponent>> getComponentMap() {
		return ImmutableMap.copyOf(componentMap);
	}
	
	public static ImmutableMap<String, SpellData<IModifier>> getModifierMap() {
		return ImmutableMap.copyOf(modifierMap);
	}
	
	public static ImmutableMap<String, SpellData<IShape>> getShapeMap() {
		return ImmutableMap.copyOf(shapeMap);
	}
	
	public static ImmutableMap<String, SpellData<? extends ISpellPart>> getCombinedMap() {
		return ImmutableMap.<String, SpellData<? extends ISpellPart>>builder().putAll(getComponentMap()).putAll(getModifierMap()).putAll(getShapeMap()).build();
	}

	public static class SpellData<K extends ISpellPart> {
		
		public ResourceLocation icon;
		public K part;
		public String id;
		
		public SpellData(ResourceLocation icon, K part, String id) {
			this.icon = icon;
			this.id = id;
			this.part = part;
		}
		
	}

	public static Skill getSkillFromPart(ISpellPart part) {
		for (SpellData<? extends ISpellPart> comp : getCombinedMap().values()) {
			if (comp == null || comp.part == null) continue;
			if (part.getClass().isInstance(comp.part)) return SkillRegistry.getSkillFromName(comp.id);
		}
		return null;
	}

	public static ISpellPart getPartByRecipe(ArrayList<ItemStack> currentAddedItems) {
		for (SpellData<? extends ISpellPart> data : getCombinedMap().values()) {
			if (data != null && data.part != null && currentAddedItems.toArray().equals(data.part.getRecipe()))
				return data.part;
		}
		return null;
	}
}
