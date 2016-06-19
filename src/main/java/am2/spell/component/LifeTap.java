package am2.spell.component;

import java.util.Random;
import java.util.Set;

import com.google.common.collect.Sets;

import am2.ArsMagica2;
import am2.affinity.Affinity;
import am2.api.extensions.IEntityExtension;
import am2.defs.ItemDefs;
import am2.defs.SkillDefs;
import am2.extensions.EntityExtension;
import am2.multiblock.MultiblockStructureDefinition;
import am2.particles.AMParticle;
import am2.particles.ParticleApproachEntity;
import am2.rituals.IRitualInteraction;
import am2.rituals.RitualShapeHelper;
import am2.spell.IComponent;
import am2.spell.SpellModifiers;
import am2.utils.AffinityShiftUtils;
import am2.utils.SpellUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class LifeTap implements IComponent, IRitualInteraction{

	@Override
	public boolean applyEffectBlock(ItemStack stack, World world, BlockPos pos, EnumFacing blockFace, double impactX, double impactY, double impactZ, EntityLivingBase caster){

//		if (world.getBlock(blockx, blocky, blockz) == Blocks.mob_spawner){
//			ItemStack[] reagents = RitualShapeHelper.instance.checkForRitual(this, world, blockx, blocky, blockz);
//			if (reagents != null){
//				if (!world.isRemote){
//					world.setBlockToAir(blockx, blocky, blockz);
//					RitualShapeHelper.instance.consumeReagents(this, world, blockx, blocky, blockz);
//					RitualShapeHelper.instance.consumeShape(this, world, blockx, blocky, blockz);
//					EntityItem item = new EntityItem(world);
//					item.setPosition(blockx + 0.5, blocky + 0.5, blockz + 0.5);
//					item.setEntityItemStack(new ItemStack(BlocksCommonProxy.inertSpawner));
//					world.spawnEntityInWorld(item);
//				}else{
//
//				}
//
//				return true;
//			}
//		}

		return false;
	}

	@Override
	public boolean applyEffectEntity(ItemStack stack, World world, EntityLivingBase caster, Entity target){
		if (!(target instanceof EntityLivingBase)) return false;
		if (!world.isRemote){
			double damage = SpellUtils.getModifiedDouble_Mul(2, stack, caster, target, world, SpellModifiers.DAMAGE);
			IEntityExtension casterProperties = EntityExtension.For(caster);
			float manaRefunded = (float)(((damage * 0.01)) * casterProperties.getMaxMana());

			if ((caster).attackEntityFrom(DamageSource.outOfWorld, (int)Math.floor(damage))){
				casterProperties.setCurrentMana(casterProperties.getCurrentMana() + manaRefunded);
			}else{
				return false;
			}
		}
		return true;
	}

	@Override
	public float manaCost(EntityLivingBase caster){
		return 0;
	}

	@Override
	public ItemStack[] reagents(EntityLivingBase caster){
		return null;
	}

	@Override
	public void spawnParticles(World world, double x, double y, double z, EntityLivingBase caster, Entity target, Random rand, int colorModifier){
		for (int i = 0; i < 25; ++i){
			AMParticle particle = (AMParticle)ArsMagica2.proxy.particleManager.spawn(world, "sparkle2", x, y, z);
			if (particle != null){
				particle.addRandomOffset(2, 2, 2);
				particle.setMaxAge(15);
				particle.setParticleScale(0.1f);
				particle.AddParticleController(new ParticleApproachEntity(particle, target, 0.1, 0.1, 1, false));
				if (rand.nextBoolean())
					particle.setRGBColorF(0.4f, 0.1f, 0.5f);
				else
					particle.setRGBColorF(0.1f, 0.5f, 0.1f);
				if (colorModifier > -1){
					particle.setRGBColorF(((colorModifier >> 16) & 0xFF) / 255.0f, ((colorModifier >> 8) & 0xFF) / 255.0f, (colorModifier & 0xFF) / 255.0f);
				}
			}
		}
	}

	@Override
	public Set<Affinity> getAffinity(){
		return Sets.newHashSet(SkillDefs.LIFE, SkillDefs.ENDER);
	}

	@Override
	public Object[] getRecipe(){
		return new Object[]{
				new ItemStack(ItemDefs.rune, 1, EnumDyeColor.BLACK.getDyeDamage()),
				//TODO BlocksCommonProxy.aum
		};
	}

	@Override
	public float getAffinityShift(Affinity affinity){
		return 0.01f;
	}

	@Override
	public MultiblockStructureDefinition getRitualShape(){
		return RitualShapeHelper.instance.corruption;
	}

	@Override
	public ItemStack[] getReagents(){
		return new ItemStack[]{
				new ItemStack(ItemDefs.mobFocus),
				AffinityShiftUtils.getEssenceForAffinity(SkillDefs.ENDER)
		};
	}

	@Override
	public int getReagentSearchRadius(){
		return 3;
	}

	@Override
	public void encodeBasicData(NBTTagCompound tag, Object[] recipe) {}
}
