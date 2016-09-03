package am2.defs;

import static am2.compendium.CompendiumCategory.GUIDE;
import static am2.compendium.CompendiumCategory.MECHANIC;
import static am2.compendium.CompendiumCategory.MECHANIC_AFFINITY;
import static am2.compendium.CompendiumCategory.MECHANIC_ENCHANTS;
import static am2.compendium.CompendiumCategory.SPELL_COMPONENT;
import static am2.compendium.CompendiumCategory.SPELL_MODIFIER;
import static am2.compendium.CompendiumCategory.SPELL_SHAPE;

import am2.api.SpellRegistry;
import am2.api.spell.AbstractSpellPart;
import am2.compendium.CompendiumCategory;
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
		createEntry(GUIDE, "your_first_spell", 5);
	}
	
	private static void initMechanics() {
		initAffinities();
		initEnchantments();
		createEntry(MECHANIC, "spell_creation", 3);
		createEntry(MECHANIC, "infusion", 1);		
		createEntry(MECHANIC, "mana", 1);		
		createEntry(MECHANIC, "magic_level", 1);		
		createEntry(MECHANIC, "burnout", 1);		
		createEntry(MECHANIC, "rituals", 1);		
		createEntry(MECHANIC, "moonstone_meteor", 1);		
		createEntry(MECHANIC, "armor_xp_infusion", 1);		
		createEntry(MECHANIC, "silver_skills", 3);		
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
		createComponentEntry("accelerate", SpellRegistry.getComponentFromName("arsmagica2:accelerate"), 1);
		createComponentEntry("appropriation", SpellRegistry.getComponentFromName("arsmagica2:appropriation"), 1);
		createComponentEntry("astral_distortion", SpellRegistry.getComponentFromName("arsmagica2:astral_distortion"), 1);
		createComponentEntry("attract", SpellRegistry.getComponentFromName("arsmagica2:attract"), 1);
		createComponentEntry("banish_rain", SpellRegistry.getComponentFromName("arsmagica2:banish_rain"), 1);
		createComponentEntry("blind", SpellRegistry.getComponentFromName("arsmagica2:blind"), 1);
		createComponentEntry("blizzard", SpellRegistry.getComponentFromName("arsmagica2:blizzard"), 1);
		createComponentEntry("charm", SpellRegistry.getComponentFromName("arsmagica2:charm"), 1);
		createComponentEntry("chrono_anchor", SpellRegistry.getComponentFromName("arsmagica2:chrono_anchor"), 1);
		createComponentEntry("create_water", SpellRegistry.getComponentFromName("arsmagica2:create_water"), 1);
		createComponentEntry("daylight", SpellRegistry.getComponentFromName("arsmagica2:daylight"), 1);
		createComponentEntry("dig", SpellRegistry.getComponentFromName("arsmagica2:dig"), 1);
		createComponentEntry("disarm", SpellRegistry.getComponentFromName("arsmagica2:disarm"), 1);
		createComponentEntry("dispel", SpellRegistry.getComponentFromName("arsmagica2:dispel"), 1);
		createComponentEntry("divine_intervention", SpellRegistry.getComponentFromName("arsmagica2:divine_intervention"), 1);
		createComponentEntry("drought", SpellRegistry.getComponentFromName("arsmagica2:drought"), 1);
		createComponentEntry("drown", SpellRegistry.getComponentFromName("arsmagica2:drown"), 1);
		createComponentEntry("ender_intervention", SpellRegistry.getComponentFromName("arsmagica2:ender_intervention"), 1);
		createComponentEntry("entangle", SpellRegistry.getComponentFromName("arsmagica2:entangle"), 1);
		createComponentEntry("falling_star", SpellRegistry.getComponentFromName("arsmagica2:falling_star"), 1);
		createComponentEntry("fire_damage", SpellRegistry.getComponentFromName("arsmagica2:fire_damage"), 1);
		createComponentEntry("flight", SpellRegistry.getComponentFromName("arsmagica2:flight"), 1);
		createComponentEntry("forge", SpellRegistry.getComponentFromName("arsmagica2:forge"), 1);
		createComponentEntry("freeze", SpellRegistry.getComponentFromName("arsmagica2:freeze"), 1);
		createComponentEntry("frost_damage", SpellRegistry.getComponentFromName("arsmagica2:frost_damage"), 1);
		createComponentEntry("fury", SpellRegistry.getComponentFromName("arsmagica2:fury"), 1);
		createComponentEntry("gravity_well", SpellRegistry.getComponentFromName("arsmagica2:gravity_well"), 1);
		createComponentEntry("grow", SpellRegistry.getComponentFromName("arsmagica2:grow"), 1);
		createComponentEntry("harvest_plants", SpellRegistry.getComponentFromName("arsmagica2:harvest_plants"), 1);
		createComponentEntry("haste", SpellRegistry.getComponentFromName("arsmagica2:haste"), 1);
		createComponentEntry("heal", SpellRegistry.getComponentFromName("arsmagica2:heal"), 1);
		createComponentEntry("ignition", SpellRegistry.getComponentFromName("arsmagica2:ignition"), 1);
		createComponentEntry("invisibility", SpellRegistry.getComponentFromName("arsmagica2:invisibility"), 1);
		createComponentEntry("knockback", SpellRegistry.getComponentFromName("arsmagica2:knockback"), 1);
		createComponentEntry("leap", SpellRegistry.getComponentFromName("arsmagica2:leap"), 1);
		createComponentEntry("levitate", SpellRegistry.getComponentFromName("arsmagica2:levitate"), 1);
		createComponentEntry("life_drain", SpellRegistry.getComponentFromName("arsmagica2:life_drain"), 1);
		createComponentEntry("life_tap", SpellRegistry.getComponentFromName("arsmagica2:life_tap"), 1);
		createComponentEntry("light", SpellRegistry.getComponentFromName("arsmagica2:light"), 1);
		createComponentEntry("lightning_damage", SpellRegistry.getComponentFromName("arsmagica2:lightning_damage"), 1);
		createComponentEntry("magic_damage", SpellRegistry.getComponentFromName("arsmagica2:magic_damage"), 1);
		createComponentEntry("mana_drain", SpellRegistry.getComponentFromName("arsmagica2:mana_drain"), 1);
		createComponentEntry("mana_link", SpellRegistry.getComponentFromName("arsmagica2:mana_link"), 1);
		createComponentEntry("mana_shield", SpellRegistry.getComponentFromName("arsmagica2:mana_shield"), 1);
		createComponentEntry("mark", SpellRegistry.getComponentFromName("arsmagica2:mark"), 1);
		createComponentEntry("moonrise", SpellRegistry.getComponentFromName("arsmagica2:moonrise"), 1);
		createComponentEntry("night_vision", SpellRegistry.getComponentFromName("arsmagica2:night_vision"), 1);
		createComponentEntry("physical_damage", SpellRegistry.getComponentFromName("arsmagica2:physical_damage"), 1);
		createComponentEntry("place_block", SpellRegistry.getComponentFromName("arsmagica2:place_block"), 1);
		createComponentEntry("plant", SpellRegistry.getComponentFromName("arsmagica2:plant"), 1);
		createComponentEntry("plow", SpellRegistry.getComponentFromName("arsmagica2:plow"), 1);
		createComponentEntry("random_teleport", SpellRegistry.getComponentFromName("arsmagica2:random_teleport"), 1);
		createComponentEntry("recall", SpellRegistry.getComponentFromName("arsmagica2:recall"), 1);
		createComponentEntry("reflect", SpellRegistry.getComponentFromName("arsmagica2:reflect"), 1);
		createComponentEntry("regeneration", SpellRegistry.getComponentFromName("arsmagica2:regeneration"), 1);
		createComponentEntry("repel", SpellRegistry.getComponentFromName("arsmagica2:repel"), 1);
		createComponentEntry("rift", SpellRegistry.getComponentFromName("arsmagica2:rift"), 1);
		createComponentEntry("shield", SpellRegistry.getComponentFromName("arsmagica2:shield"), 1);
		createComponentEntry("shrink", SpellRegistry.getComponentFromName("arsmagica2:shrink"), 1);
		createComponentEntry("silence", SpellRegistry.getComponentFromName("arsmagica2:silence"), 1);
		createComponentEntry("slow", SpellRegistry.getComponentFromName("arsmagica2:slow"), 1);
		createComponentEntry("slowfall", SpellRegistry.getComponentFromName("arsmagica2:slowfall"), 1);
		createComponentEntry("storm", SpellRegistry.getComponentFromName("arsmagica2:storm"), 1);
		createComponentEntry("summon", SpellRegistry.getComponentFromName("arsmagica2:summon"), 1);
		createComponentEntry("swift_swim", SpellRegistry.getComponentFromName("arsmagica2:swift_swim"), 1);
		createComponentEntry("telekinesis", SpellRegistry.getComponentFromName("arsmagica2:telekinesis"), 1);
		createComponentEntry("transplace", SpellRegistry.getComponentFromName("arsmagica2:transplace"), 1);
		createComponentEntry("true_sight", SpellRegistry.getComponentFromName("arsmagica2:true_sight"), 1);
		createComponentEntry("water_breathing", SpellRegistry.getComponentFromName("arsmagica2:water_breathing"), 1);
		createComponentEntry("watery_grave", SpellRegistry.getComponentFromName("arsmagica2:watery_grave"), 1);
		createComponentEntry("wizards_autumn", SpellRegistry.getComponentFromName("arsmagica2:wizards_autumn"), 1);
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
	
	private static void createShapeEntry(String name, AbstractSpellPart shape, int textPages) {
		CompendiumEntry entry = new CompendiumEntry(name);
		for (int i = 1; i <= textPages; i++) {
			entry = entry.addObject("compendium." + name + ".page" + i);
		}
		entry.addObject(shape);
		SPELL_SHAPE.addEntry(entry);
	}
	
	private static void createComponentEntry(String name, AbstractSpellPart component, int textPages) {
		CompendiumEntry entry = new CompendiumEntry(name);
		for (int i = 1; i <= textPages; i++) {
			entry = entry.addObject("compendium." + name + ".page" + i);
		}
		entry.addObject(component);
		SPELL_COMPONENT.addEntry(entry);
	}
	
	private static void createModifierEntry(String name, AbstractSpellPart mod, int textPages) {
		CompendiumEntry entry = new CompendiumEntry(name);
		for (int i = 1; i <= textPages; i++) {
			entry = entry.addObject("compendium." + name + ".page" + i);
		}
		entry.addObject(mod);
		SPELL_MODIFIER.addEntry(entry);
	}
	
	private static void createEntry(CompendiumCategory category, String name, int pages) {
		CompendiumEntry entry = new CompendiumEntry(name);
		for (int i = 1; i <= pages; i++) {
			entry = entry.addObject("compendium." + name + ".page" + i);
		}
		category.addEntry(entry);
	}

}
