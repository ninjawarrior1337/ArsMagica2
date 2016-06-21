package am2.lore;

import am2.gui.GuiArcaneCompendium;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CompendiumEntryMechanic extends CompendiumEntry{

	public CompendiumEntryMechanic(String id, String... related){
		super(CompendiumEntryTypes.instance.MECHANIC, id, related);
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
