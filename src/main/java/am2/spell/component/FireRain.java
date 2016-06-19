package am2.spell.component;

import java.util.Random;
import java.util.Set;

import com.google.common.collect.Sets;

import am2.affinity.Affinity;
import am2.defs.ItemDefs;
import am2.defs.SkillDefs;
import am2.items.ItemOre;
import am2.spell.IComponent;
import am2.utils.AffinityShiftUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FireRain implements IComponent{

	@Override
	public Object[] getRecipe(){
		return new Object[]{
				AffinityShiftUtils.getEssenceForAffinity(SkillDefs.FIRE),
				new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_ARCANEASH),
				Blocks.NETHERRACK,
				AffinityShiftUtils.getEssenceForAffinity(SkillDefs.FIRE),
				Items.LAVA_BUCKET
		};
	}

	private boolean spawnFireRain(ItemStack stack, World world, EntityLivingBase caster, Entity target, double x, double y, double z){

//		List<EntitySpellEffect> zones = world.getEntitiesWithinAABB(EntitySpellEffect.class, AxisAlignedBB.getBoundingBox(x - 10, y - 10, z - 10, x + 10, y + 10, z + 10));
//
//		for (EntitySpellEffect zone : zones){
//			if (zone.isRainOfFire())
//				return false;
//		}
//
//		if (!world.isRemote){
//			int radius = SpellUtils.instance.getModifiedInt_Add(2, stack, caster, target, world, 0, SpellModifiers.RADIUS) / 2 + 1;
//			double damage = SpellUtils.instance.getModifiedDouble_Mul(1, stack, caster, target, world, 0, SpellModifiers.DAMAGE);
//			int duration = SpellUtils.instance.getModifiedInt_Mul(100, stack, caster, target, world, 0, SpellModifiers.DURATION);
//
//			EntitySpellEffect fire = new EntitySpellEffect(world);
//			fire.setPosition(x, y, z);
//			fire.setRainOfFire(false);
//			fire.setRadius(radius);
//			fire.setDamageBonus((float)damage);
//			fire.setTicksToExist(duration);
//			fire.SetCasterAndStack(caster, stack);
//			world.spawnEntityInWorld(fire);
//		}
		return true;
	}

	@Override
	public boolean applyEffectBlock(ItemStack stack, World world, BlockPos pos, EnumFacing blockFace, double impactX, double impactY, double impactZ, EntityLivingBase caster){
		return spawnFireRain(stack, world, caster, caster, impactX, impactY, impactZ);
	}

	@Override
	public boolean applyEffectEntity(ItemStack stack, World world, EntityLivingBase caster, Entity target){
		return spawnFireRain(stack, world, caster, target, target.posX, target.posY, target.posZ);
	}

	@Override
	public float manaCost(EntityLivingBase caster){
		return 3000;
	}

	@Override
	public ItemStack[] reagents(EntityLivingBase caster){
		return null;
	}

	@Override
	public void spawnParticles(World world, double x, double y, double z, EntityLivingBase caster, Entity target, Random rand, int colorModifier){
	}

	@Override
	public Set<Affinity> getAffinity(){
		return Sets.newHashSet(SkillDefs.FIRE);
	}

	@Override
	public float getAffinityShift(Affinity affinity){
		return 0.1f;
	}
	
	@Override
	public void encodeBasicData(NBTTagCompound tag, Object[] recipe) {
		// TODO Auto-generated method stub
		
	}

}
