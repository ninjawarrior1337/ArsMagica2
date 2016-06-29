package am2.defs;

import am2.blocks.tileentity.TileEntityCraftingAltar;
import am2.lore.ArcaneCompendium;

public class LoreDefs {

	public static void init() {
		ArcaneCompendium.AddCompendiumEntry("guide", "yourFirstSpell", null, false, true);
		ArcaneCompendium.AddCompendiumEntry(TileEntityCraftingAltar.class, "craftingAltar", null, false, true);
		ArcaneCompendium.AddCompendiumEntry(BlockDefs.altar, "craftingAltarBlock", null, false, true, "craftingAltar");
		ArcaneCompendium.AddCompendiumEntry(BlockDefs.magicWall, "magicWall", null, false, true, "craftingAltarBlock");
	}

}
