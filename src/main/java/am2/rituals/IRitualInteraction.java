package am2.rituals;

import am2.api.blocks.MultiblockStructureDefinition;
import net.minecraft.item.ItemStack;

public interface IRitualInteraction {
	
	public ItemStack[] getReagents();
	public int getReagentSearchRadius();
	public MultiblockStructureDefinition getRitualShape();
	
	public static class Wrapper {
		
		private final IRitualInteraction interaction;
		
		public Wrapper(IRitualInteraction interaction) {
			this.interaction = interaction;
		}
		
		public IRitualInteraction getRitualInteraction() {
			return interaction;
		}
		
	}
}
