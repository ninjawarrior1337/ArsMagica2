package am2.spell.shape;

import am2.defs.ItemDefs;
import am2.entity.EntitySpellProjectile;
import am2.items.ItemOre;
import am2.items.ItemSpellBase;
import am2.particles.AMParticleIcons;
import am2.spell.IShape;
import am2.spell.SpellCastResult;
import am2.spell.SpellModifiers;
import am2.utils.AffinityShiftUtils;
import am2.utils.SpellUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class Projectile implements IShape {

	@Override
	public Object[] getRecipe() {
		return new Object[]{
				new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_VINTEUM),
				Items.ARROW,
				Items.SNOWBALL
		};
	}

	@Override
	public boolean isChanneled() {
		return false;
	}

	@Override
	public float manaCostMultiplier(ItemStack spellStack) {
		return 1.25F;
	}

	@Override
	public boolean isTerminusShape() {
		return false;
	}

	@Override
	public boolean isPrincipumShape() {
		return false;
	}

	@Override
	public SpellCastResult beginStackStage(ItemSpellBase item, ItemStack stack, EntityLivingBase caster, EntityLivingBase target, World world, double x, double y, double z, EnumFacing side, boolean giveXP, int useCount) {
		if (!world.isRemote) {
			double projectileSpeed = SpellUtils.getModifiedDouble_Add(stack, caster, target, world, SpellModifiers.SPEED);
			float projectileGravity = (float) SpellUtils.getModifiedDouble_Mul(stack, caster, target, world, SpellModifiers.GRAVITY);
			int projectileBounce = SpellUtils.getModifiedInt_Add(stack, caster, target, world, SpellModifiers.GRAVITY);
			int pierces = SpellUtils.getModifiedInt_Add(stack, caster, target, world, SpellModifiers.PIERCING);
			EntitySpellProjectile projectile = new EntitySpellProjectile(world);
			projectile.setPosition(caster.posX, caster.getEyeHeight() + caster.posY, caster.posZ);
			projectile.motionX = caster.getLookVec().xCoord * projectileSpeed;
			projectile.motionY = caster.getLookVec().yCoord * projectileSpeed;
			projectile.motionZ = caster.getLookVec().zCoord * projectileSpeed;
			if (SpellUtils.modifierIsPresent(SpellModifiers.TARGET_NONSOLID_BLOCKS, stack))
				projectile.setTargetWater();
			projectile.setGravity(projectileGravity);
			projectile.setBounces(projectileBounce);
			projectile.setNumPierces(pierces);
			projectile.setShooter(caster);
			projectile.setHoming(SpellUtils.modifierIsPresent(SpellModifiers.HOMING, stack));
			projectile.setSpell(stack);
			projectile.setIcon(AMParticleIcons.instance.getParticleForAffinity(AffinityShiftUtils.getMainShiftForStack(stack)));
			world.spawnEntityInWorld(projectile);
		}
		return SpellCastResult.SUCCESS;
	}

	@Override
	public void encodeBasicData(NBTTagCompound tag, Object[] recipe) {
		
	}

}
