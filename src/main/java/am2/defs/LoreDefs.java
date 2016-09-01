package am2.defs;

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
		CompendiumCategory.GUIDE.addEntry(new CompendiumEntry("yourFirstSpell")
				.addObject("compendium.yourFirstSpell.page1")
				.addObject("compendium.yourFirstSpell.page2")
				.addObject("compendium.yourFirstSpell.page3")
				.addObject("compendium.yourFirstSpell.page4")
				.addObject("compendium.yourFirstSpell.page5"));		
	}
	
	private static void initMechanics() {
		initAffinities();
		CompendiumCategory.MECHANIC.addEntry(new CompendiumEntry("spell_creation")
				.addObject("compendium.spell_creation.page1")
				.addObject("compendium.spell_creation.page2")
				.addObject("compendium.spell_creation.page3"));
		
		CompendiumCategory.MECHANIC.addEntry(new CompendiumEntry("infusion")
				.addObject("compendium.infusion.page1"));
		
		CompendiumCategory.MECHANIC.addEntry(new CompendiumEntry("mana")
				.addObject("compendium.mana.page1"));
		
		CompendiumCategory.MECHANIC.addEntry(new CompendiumEntry("magic_level")
				.addObject("compendium.magic_level.page1"));
	}
	
	private static void initAffinities() {
		
		CompendiumCategory.MECHANIC_AFFINITY.addEntry(new CompendiumEntry("affinities")
				.addObject("compendium.affinities.page1")
				.addObject("compendium.affinities.page2")
				.addObject("compendium.affinities.page3")
				.addObject("compendium.affinities.page4"));
		
		CompendiumCategory.MECHANIC_AFFINITY.addEntry(new CompendiumEntry("earth_affinity")
				.addObject("compendium.earth_affinity.page1"));
		
		CompendiumCategory.MECHANIC_AFFINITY.addEntry(new CompendiumEntry("water_affinity")
				.addObject("compendium.water_affinity.page1"));
		
		CompendiumCategory.MECHANIC_AFFINITY.addEntry(new CompendiumEntry("air_affinity")
				.addObject("compendium.air_affinity.page1"));
		
		CompendiumCategory.MECHANIC_AFFINITY.addEntry(new CompendiumEntry("fire_affinity")
				.addObject("compendium.fire_affinity.page1"));

		CompendiumCategory.MECHANIC_AFFINITY.addEntry(new CompendiumEntry("nature_affinity")
				.addObject("compendium.nature_affinity.page1")
				.addObject("compendium.nature_affinity.page2")
				.addObject("compendium.nature_affinity.page3"));

		CompendiumCategory.MECHANIC_AFFINITY.addEntry(new CompendiumEntry("ice_affinity")
				.addObject("compendium.ice_affinity.page1"));

		CompendiumCategory.MECHANIC_AFFINITY.addEntry(new CompendiumEntry("lightning_affinity")
				.addObject("compendium.lightning_affinity.page1"));

		CompendiumCategory.MECHANIC_AFFINITY.addEntry(new CompendiumEntry("life_affinity")
				.addObject("compendium.life_affinity.page1"));

		CompendiumCategory.MECHANIC_AFFINITY.addEntry(new CompendiumEntry("arcane_affinity")
				.addObject("compendium.arcane_affinity.page1"));

		CompendiumCategory.MECHANIC_AFFINITY.addEntry(new CompendiumEntry("ender_affinity")
				.addObject("compendium.ender_affinity.page1"));
		
	}
	
	private static void initItems() {
		
	}
	
	private static void initBlocks() {
		
	}
	
	private static void initShapes() {
		
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

}
