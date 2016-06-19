package am2.spell.component;

import java.util.List;
import java.util.Random;
import java.util.Set;

import com.google.common.collect.Sets;

import am2.affinity.Affinity;
import am2.defs.ItemDefs;
import am2.defs.SkillDefs;
import am2.items.SpellBase;
import am2.spell.IComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

public class Attract implements IComponent{

	public Attract(){
	}

	@Override
	public boolean applyEffectBlock(ItemStack stack, World world, BlockPos pos, EnumFacing blockFace, double impactX, double impactY, double impactZ, EntityLivingBase caster){

		doTK_Extrapolated(stack, world, impactX, impactY, impactZ, caster);
		return true;
	}

	private boolean doTK_Extrapolated(ItemStack stack, World world, double impactX, double impactY, double impactZ, EntityLivingBase caster){
		if (caster instanceof EntityPlayer){
			double range = 8;
			RayTraceResult mop = ((SpellBase) ItemDefs.spell).getMovingObjectPosition(caster, world, range, false, false);
			if (mop == null){
				impactX = caster.posX + (Math.cos(Math.toRadians(caster.rotationYaw + 90)) * range);
				impactZ = caster.posZ + (Math.sin(Math.toRadians(caster.rotationYaw + 90)) * range);
				impactY = caster.posY + caster.getEyeHeight() + (-Math.sin(Math.toRadians(caster.rotationPitch)) * range);
			}
		}

		EntityLivingBase target = getClosestEntityToPointWithin(caster, world, new Vec3i(impactX, impactY, impactZ), 16);

		if (target == null) return false;

		Vec3d movement = new Vec3d(target.getPosition().subtract(new Vec3i(impactX, impactY, impactZ))).normalize();

		if (!world.isRemote){
			float factor = 0.75f;
			double x = -(movement.xCoord * factor);
			double y = -(movement.yCoord * factor);
			double z = -(movement.zCoord * factor);

			target.addVelocity(x, y, z);
			if (Math.abs(target.motionX) > Math.abs(x * 2)){
				target.motionX = x * (target.motionX / target.motionX);
			}
			if (Math.abs(target.motionY) > Math.abs(y * 2)){
				target.motionY = y * (target.motionY / target.motionY);
			}
			if (Math.abs(target.motionZ) > Math.abs(z * 2)){
				target.motionZ = z * (target.motionZ / target.motionZ);
			}
		}
		return true;
	}

	private EntityLivingBase getClosestEntityToPointWithin(EntityLivingBase caster, World world, Vec3i point, double radius){
		AxisAlignedBB bb = new AxisAlignedBB(point.getX() - radius, point.getY() - radius, point.getZ() - radius, point.getX() + radius, point.getY() + radius, point.getZ() + radius);
		List<EntityLivingBase> entities = world.getEntitiesWithinAABB(EntityLivingBase.class, bb);
		EntityLivingBase closest = null;
		for (EntityLivingBase e : entities){
			if (e == caster) continue;
			if (closest == null || point.distanceSq(e.getPosition()) < point.distanceSq(closest.getPosition()))
				closest = e;
		}
		return closest;
	}

	@Override
	public boolean applyEffectEntity(ItemStack stack, World world, EntityLivingBase caster, Entity target){
		doTK_Extrapolated(stack, world, target.posX, target.posY, target.posZ, caster);
		return true;
	}

	@Override
	public float manaCost(EntityLivingBase caster){
		return 2.6f;
	}

	@Override
	public ItemStack[] reagents(EntityLivingBase caster){
		return null;
	}

	@Override
	public void spawnParticles(World world, double x, double y, double z, EntityLivingBase caster, Entity target, Random rand, int colorModifier){
//		AMParticle effect = (AMParticle)AMCore.instance.proxy.particleManager.spawn(world, "arcane"blockPos);
//		if (effect != null){
//			effect.addRandomOffset(1, 1, 1);
//			effect.AddParticleController(new ParticleApproachPoint(effectblockPos, 0.025f, 0.025f, 1, false));
//			effect.setRGBColorF(0.8f, 0.3f, 0.7f);
//			if (colorModifier > -1){
//				effect.setRGBColorF(((colorModifier >> 16) & 0xFF) / 255.0f, ((colorModifier >> 8) & 0xFF) / 255.0f, (colorModifier & 0xFF) / 255.0f);
//			}
//		}
	}

	@Override
	public Set<Affinity> getAffinity(){
		return Sets.newHashSet(SkillDefs.NONE);
	}

	@Override
	public Object[] getRecipe(){
		return new Object[]{
				new ItemStack(ItemDefs.rune, 1, EnumDyeColor.BLACK.getDyeDamage()),
				Items.IRON_INGOT
		};
	}

	@Override
	public float getAffinityShift(Affinity affinity){
		return 1;
	}

	@Override
	public void encodeBasicData(NBTTagCompound tag, Object[] recipe) {
		
	}

}
