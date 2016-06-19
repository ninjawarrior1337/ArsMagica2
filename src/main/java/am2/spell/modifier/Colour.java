package am2.spell.modifier;

import java.util.EnumSet;

import am2.spell.IModifier;
import am2.spell.SpellModifiers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class Colour implements IModifier{

	public int color = 0xFFFFFF;
	
	@Override
	public Object[] getRecipe() {
		return null;
	}

	@Override
	public void encodeBasicData(NBTTagCompound tag, Object[] recipe) {
		tag.setInteger("Color", color);
	}
	
	public int getColor() {
		return color;
	}
	
	public void setColor(int color) {
		this.color = color;
	}

	@Override
	public EnumSet<SpellModifiers> getAspectsModified() {
		return EnumSet.of(SpellModifiers.COLOR);
	}

	@Override
	public float getModifier(SpellModifiers type, EntityLivingBase caster,
			Entity target, World world, NBTTagCompound nbt) {
		color = nbt.getInteger("Color");
		return color;
	}

	@Override
	public float getManaCostMultiplier(ItemStack spellStack, int stage, int quantity) {
		return 1F;
	}

}
