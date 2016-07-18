package am2.spell.shape;

import am2.items.ItemSpellBase;
import am2.spell.SpellShape;
import am2.spell.SpellCastResult;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class MissingShape extends SpellShape {

	@Override
	public Object[] getRecipe() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isChanneled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public float manaCostMultiplier(ItemStack spellStack) {
		// TODO Auto-generated method stub
		return 0F;
	}

	@Override
	public boolean isTerminusShape() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isPrincipumShape() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public SpellCastResult beginStackStage(ItemSpellBase item, ItemStack stack,
			EntityLivingBase caster, EntityLivingBase target, World world,
			double x, double y, double z, EnumFacing side, boolean giveXP, int useCount) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void encodeBasicData(NBTTagCompound tag, Object[] recipe) {
		// TODO Auto-generated method stub
		
	}

}
