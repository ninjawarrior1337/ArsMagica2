package am2.rituals;

import net.minecraft.item.ItemStack;
import am2.multiblock.MultiblockStructureDefinition;

public interface IRitualInteraction {
	
	public ItemStack[] getReagents();
	public int getReagentSearchRadius();
	public MultiblockStructureDefinition getRitualShape();
}
