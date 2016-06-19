package am2.spell.component;

import java.util.Random;
import java.util.Set;

import com.google.common.collect.Sets;

import am2.affinity.Affinity;
import am2.defs.SkillDefs;
import am2.extensions.EntityExtension;
import am2.spell.IComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Mark implements IComponent {

	@Override
	public Object[] getRecipe() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void encodeBasicData(NBTTagCompound tag, Object[] recipe) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean applyEffectBlock(ItemStack stack, World world,
			BlockPos blockPos, EnumFacing blockFace, double impactX,
			double impactY, double impactZ, EntityLivingBase caster) {
		EntityExtension.For(caster).setMark(impactX, impactY, impactZ, caster.worldObj.provider.getDimension());
		return true;
	}

	@Override
	public boolean applyEffectEntity(ItemStack stack, World world,
			EntityLivingBase caster, Entity target) {
		EntityExtension.For(caster).setMark(target.posX, target.posY, target.posZ, caster.worldObj.provider.getDimension());
		return true;
	}

	@Override
	public float manaCost(EntityLivingBase caster) {
		// TODO Auto-generated method stub
		return 5;
	}

	@Override
	public ItemStack[] reagents(EntityLivingBase caster) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void spawnParticles(World world, double x, double y, double z,
			EntityLivingBase caster, Entity target, Random rand,
			int colorModifier) {
		// TODO Auto-generated method stub

	}

	@Override
	public Set<Affinity> getAffinity() {
		return Sets.newHashSet(SkillDefs.NONE);
	}

	@Override
	public float getAffinityShift(Affinity affinity) {
		return 0;
	}

}
