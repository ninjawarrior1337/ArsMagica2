package am2.lore;

import am2.gui.GuiArcaneCompendium;
import am2.multiblock.MultiblockStructureDefinition;
import am2.rituals.IRitualInteraction;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CompendiumEntryRitual extends CompendiumEntry{

	private MultiblockStructureDefinition ritualShape;
	private IRitualInteraction ritualController;

	public CompendiumEntryRitual(String id, IRitualInteraction ritualController, MultiblockStructureDefinition def, String... related){
		super(CompendiumEntryTypes.instance.RITUAL, id, related);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public GuiArcaneCompendium getCompendiumGui(){
		if (this.ritualShape != null){
			try{
				return new GuiArcaneCompendium(id, ritualShape, ritualController);
				
			}catch (Throwable e){
				e.printStackTrace();
			}
		}
		return new GuiArcaneCompendium(id);
	}

	@Override
	public ItemStack getRepresentStack(){
		return null;
	}

}
