package am2.defs;

import am2.compendium.CompendiumCategory;
import static am2.compendium.CompendiumCategory.*;

import am2.api.SpellRegistry;
import am2.api.spell.SpellShape;
import am2.compendium.CompendiumEntry;

public class LoreDefs {

	public static void postInit() {
		initGuides();
		initMechanics();
		initItems();
		initBlocks();
		initShapes();
		initModifiers();
		initComponents();
		initStructures();
		initMobs();
		initBosses();
		initTalents();
	}
	
	private static void initGuides() {
		createEntry(GUIDE, "spell_creation", 5);
	}
	
	private static void initMechanics() {
		initAffinities();
		initEnchantments();
		createEntry(MECHANIC, "spell_creation", 3);
		createEntry(MECHANIC_AFFINITY, "infusion", 1);		
		createEntry(MECHANIC_AFFINITY, "mana", 1);		
		createEntry(MECHANIC_AFFINITY, "magic_level", 1);		
		createEntry(MECHANIC_AFFINITY, "burnout", 1);		
		createEntry(MECHANIC_AFFINITY, "rituals", 1);		
		createEntry(MECHANIC_AFFINITY, "moonstone_meteor", 1);		
		createEntry(MECHANIC_AFFINITY, "armor_xp_infusion", 1);		
		createEntry(MECHANIC_AFFINITY, "silver_skills", 3);		
	}
	
	private static void initEnchantments() {
		createEntry(MECHANIC_ENCHANTS, "magic_resistance", 1);
		createEntry(MECHANIC_ENCHANTS, "soulbound", 1);
	}
	
	private static void initAffinities() {
		createEntry(MECHANIC_AFFINITY, "affinities", 4);		
		createEntry(MECHANIC_AFFINITY, "earth_affinity", 1);		
		createEntry(MECHANIC_AFFINITY, "water_affinity", 1);		
		createEntry(MECHANIC_AFFINITY, "air_affinity", 1);		
		createEntry(MECHANIC_AFFINITY, "fire_affinity", 1);		
		createEntry(MECHANIC_AFFINITY, "nature_affinity", 1);		
		createEntry(MECHANIC_AFFINITY, "ice_affinity", 1);		
		createEntry(MECHANIC_AFFINITY, "lightning_affinity", 1);		
		createEntry(MECHANIC_AFFINITY, "life_affinity", 1);		
		createEntry(MECHANIC_AFFINITY, "arcane_affinity", 1);		
		createEntry(MECHANIC_AFFINITY, "ender_affinity", 1);		
	}
	
	private static void initItems() {
		
	}
	
	private static void initBlocks() {
		
	}
	
	private static void initShapes() {
		createShapeEntry("projectile", SpellRegistry.getShapeFromName("arsmagica2:projectile"), 1);
		createShapeEntry("channel", SpellRegistry.getShapeFromName("arsmagica2:channel"), 1);
		createShapeEntry("beam", SpellRegistry.getShapeFromName("arsmagica2:beam"), 1);
		createShapeEntry("self", SpellRegistry.getShapeFromName("arsmagica2:self"), 1);
		createShapeEntry("touch", SpellRegistry.getShapeFromName("arsmagica2:touch"), 1);
		createShapeEntry("zone", SpellRegistry.getShapeFromName("arsmagica2:zone"), 1);
		createShapeEntry("aoe", SpellRegistry.getShapeFromName("arsmagica2:aoe"), 1);
		createShapeEntry("chain", SpellRegistry.getShapeFromName("arsmagica2:chain"), 1);
		createShapeEntry("rune", SpellRegistry.getShapeFromName("arsmagica2:rune"), 1);
		createShapeEntry("contingency_fall", SpellRegistry.getShapeFromName("arsmagica2:contingency_fall"), 1);
		createShapeEntry("contingency_damage", SpellRegistry.getShapeFromName("arsmagica2:contingency_damage"), 1);
		createShapeEntry("contingency_fire", SpellRegistry.getShapeFromName("arsmagica2:contingency_fire"), 1);
		createShapeEntry("contingency_health", SpellRegistry.getShapeFromName("arsmagica2:contingency_health"), 1);
		createShapeEntry("contingency_death", SpellRegistry.getShapeFromName("arsmagica2:contingency_death"), 1);
		createShapeEntry("binding", SpellRegistry.getShapeFromName("arsmagica2:binding"), 1);
		createShapeEntry("wall", SpellRegistry.getShapeFromName("arsmagica2:wall"), 1);
		createShapeEntry("wave", SpellRegistry.getShapeFromName("arsmagica2:wave"), 1);
	}
	
	private static void initComponents() {
		
	}
	
	private static void initModifiers() {
		
	}
	
	private static void initTalents() {
		
	}
	
	private static void initMobs() {
		
	}
	
	private static void initStructures() {
		
	}
	
	private static void initBosses() {
		
	}
	
	private static void createShapeEntry(String name, SpellShape shape, int textPages) {
		CompendiumEntry entry = new CompendiumEntry(name);
		for (int i = 1; i <= textPages; i++) {
			entry = entry.addObject("compendium." + name + ".page" + i);
		}
		entry.addObject(shape);
		SPELL_SHAPE.addEntry(entry);
	}
	
	private static void createEntry(CompendiumCategory category, String name, int pages) {
		CompendiumEntry entry = new CompendiumEntry(name);
		for (int i = 1; i <= pages; i++) {
			entry = entry.addObject("compendium." + name + ".page" + i);
		}
		category.addEntry(entry);
	}

}
