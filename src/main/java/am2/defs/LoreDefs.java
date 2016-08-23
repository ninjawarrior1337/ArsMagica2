package am2.defs;

import am2.blocks.tileentity.TileEntityCraftingAltar;
import am2.blocks.tileentity.TileEntityKeystoneRecepticle;
import am2.lore.ArcaneCompendium;
import am2.spell.component.Light;

public class LoreDefs {

	public static void postInit() {
		ArcaneCompendium.AddCompendiumEntry("guide", "yourFirstSpell", null, false, true);
		ArcaneCompendium.AddCompendiumEntry(TileEntityCraftingAltar.class, "craftingAltar", null, false, true);
		ArcaneCompendium.AddCompendiumEntry(TileEntityKeystoneRecepticle.class, "gateways", null, false, true);
		ArcaneCompendium.AddCompendiumEntry(BlockDefs.altar, "craftingAltarBlock", null, false, true, "craftingAltar");
		ArcaneCompendium.AddCompendiumEntry(BlockDefs.magicWall, "magicWall", null, false, true, "craftingAltarBlock");
		ArcaneCompendium.AddCompendiumEntry(Light.class, "purificationRitual", null, false, true, "obelisk", "celestialPrism");
		ArcaneCompendium.AddCompendiumEntry(BlockDefs.obelisk, "obelisk", null, false, true);
		ArcaneCompendium.AddCompendiumEntry(BlockDefs.celestialPrism, "celestialPrism", null, false, true, "purificationRitual");
	}

}
