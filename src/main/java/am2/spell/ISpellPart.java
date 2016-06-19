package am2.spell;

import net.minecraft.nbt.NBTTagCompound;


public interface ISpellPart {

	/**
	 * Supports :
	 *     ItemStacks
	 *     String
	 *     Essence
	 * @return
	 */
	public Object[] getRecipe();
	
	public void encodeBasicData(NBTTagCompound tag, Object[] recipe);
	
}
