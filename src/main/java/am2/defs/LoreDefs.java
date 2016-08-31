package am2.defs;

import am2.compendium.CompendiumCategory;
import am2.compendium.CompendiumEntry;

public class LoreDefs {

	public static void postInit() {
		
		CompendiumCategory.GUIDE.addEntry(new CompendiumEntry("yourFirstSpell")
				.addObject("compendium.yourFirstSpell.page1")
				.addObject("compendium.yourFirstSpell.page2")
				.addObject("compendium.yourFirstSpell.page3")
				.addObject("compendium.yourFirstSpell.page4")
				.addObject("compendium.yourFirstSpell.page5"));
		
//		ArcaneCompendium.AddCompendiumEntry("guide", "yourFirstSpell", null, false, true);
//		ArcaneCompendium.AddCompendiumEntry(TileEntityCraftingAltar.class, "craftingAltar", null, false);
//		ArcaneCompendium.AddCompendiumEntry(TileEntityKeystoneRecepticle.class, "gateways", null, false);
//		ArcaneCompendium.AddCompendiumEntry(BlockDefs.altar, "craftingAltarBlock", null, false, "craftingAltar");
//		ArcaneCompendium.AddCompendiumEntry(BlockDefs.magicWall, "magicWall", null, false, "craftingAltarBlock");
//		ArcaneCompendium.AddCompendiumEntry(Light.class, "purificationRitual", null, false, "obelisk", "celestialPrism");
//		ArcaneCompendium.AddCompendiumEntry(BlockDefs.obelisk, "obelisk", null, false);
//		ArcaneCompendium.AddCompendiumEntry(BlockDefs.celestialPrism, "celestialPrism", null, false, "purificationRitual");
//		ArcaneCompendium.AddCompendiumEntry(ItemDefs.itemOre, "item_ore", null, false, "calefactor");
//		ArcaneCompendium.AddCompendiumEntry(new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_VINTEUM), "item_ore.vinteum", null, false, "ores.vinteum", "calefactor", "vinteumTorch");
//		ArcaneCompendium.AddCompendiumEntry(new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_PURIFIED_VINTEUM), "item_ore.purified_vinteum", null, false);
//		ArcaneCompendium.AddCompendiumEntry(new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_CHIMERITE), "item_ore.chimerite", null, false);
//		ArcaneCompendium.AddCompendiumEntry(new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_MOONSTONE), "item_ore.moonstone", null, false);
//		ArcaneCompendium.AddCompendiumEntry(new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_SUNSTONE), "item_ore.sunstone", null, false);
//		ArcaneCompendium.AddCompendiumEntry(new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_BLUE_TOPAZ), "item_ore.blue_topaz", null, false);
//		ArcaneCompendium.AddCompendiumEntry(new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_ARCANEASH), "item_ore.arcane_ash", null, false, "item_ore.arcane_compound", "calefactor");
//		ArcaneCompendium.AddCompendiumEntry(new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_ARCANECOMPOUND), "item_ore.arcane_compound", null, false, "item_ore.arcane_ash", "calefactor");
//		ArcaneCompendium.AddCompendiumEntry(new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_ANIMALFAT), "item_ore.animal_fat", null, false);
//		ArcaneCompendium.AddCompendiumEntry(ItemDefs.essence, "essence", null, false);
//		for (Affinity aff : ArsMagicaAPI.getAffinityRegistry().getValues()) {
//			ArcaneCompendium.AddCompendiumEntry(new ItemStack(ItemDefs.itemOre, 1, ArsMagicaAPI.getAffinityRegistry().getId(aff)), "essence." + aff.getName(), null, false);
//		}
	}

}
