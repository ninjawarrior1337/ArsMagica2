package am2.lore;

import am2.gui.GuiArcaneCompendium;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CompendiumEntryGuide extends CompendiumEntry{

	public CompendiumEntryGuide(String id, String... related){
		super(CompendiumEntryTypes.instance.GUIDE, id, related);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public GuiArcaneCompendium getCompendiumGui(){
		return new GuiArcaneCompendium(id);
	}

	@Override
	public ItemStack getRepresentStack(){
		return null;
	}

}
