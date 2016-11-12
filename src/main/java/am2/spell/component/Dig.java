package am2.spell.component;

import java.util.EnumSet;
import java.util.Random;
import java.util.Set;

import com.google.common.collect.Sets;

import am2.api.affinity.Affinity;
import am2.api.spell.SpellComponent;
import am2.api.spell.SpellModifiers;
import am2.extensions.EntityExtension;
import am2.utils.SpellUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

public class Dig extends SpellComponent {

	@Override
	public Object[] getRecipe() {
		return new Object[] {
			new ItemStack(Items.IRON_SHOVEL),
			new ItemStack(Items.IRON_AXE),
			new ItemStack(Items.IRON_PICKAXE)
		};
	}

	@Override
	public boolean applyEffectBlock(ItemStack stack, World world, BlockPos blockPos, EnumFacing blockFace, double impactX, double impactY, double impactZ, EntityLivingBase caster) {
		if (!(caster instanceof EntityPlayer))
			return false;
		if (world.isRemote) return true;
		IBlockState state = world.getBlockState(blockPos);
		float hardness = state.getBlockHardness(world, blockPos);
		if (ForgeEventFactory.doPlayerHarvestCheck((EntityPlayer)caster, state, true) && state.getBlockHardness(world, blockPos) != -1 && state.getBlock().getHarvestLevel(state) <= SpellUtils.getModifiedInt_Add(2, stack, caster, null, world, SpellModifiers.MINING_POWER)) {
			IBlockState old = world.getBlockState(blockPos);
			if (!SpellUtils.modifierIsPresent(SpellModifiers.SILKTOUCH_LEVEL, stack)) {
				BreakSequence(false, world, blockPos, stack, caster);
			}else{
				if (state.getBlock().canSilkHarvest(world, blockPos, state, (EntityPlayer)caster)) {
					BreakSequence(true, world, blockPos, stack, caster);
				}else{
					BreakSequence(false, world, blockPos, stack, caster);
				}
			}
			EntityExtension.For(caster).deductMana(hardness * 1.28f);
		}
		return true;
	}

	public void BreakSequence(boolean hasSilk, World world, BlockPos pos, ItemStack stack, EntityLivingBase caster){
		IBlockState bState = world.getBlockState(pos);
		if (hasSilk){
			bState.getBlock().breakBlock(world, pos, bState);
			world.destroyBlock(pos, false);
			world.spawnEntityInWorld(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(bState.getBlock(), 1, bState.getBlock().getMetaFromState(bState))));
		}else{
			bState.getBlock().breakBlock(world, pos, bState);
			bState.getBlock().dropBlockAsItem(world, pos, world.getBlockState(pos), SpellUtils.getModifiedInt_Add(0, stack, caster, null, world, SpellModifiers.FORTUNE_LEVEL));
			world.destroyBlock(pos, true);
			bState.getBlock().dropXpOnBlockBreak(world, pos, bState.getBlock().getExpDrop(bState, world, pos, SpellUtils.getModifiedInt_Add(0, stack, caster, null, world, SpellModifiers.FORTUNE_LEVEL)));
		}
	}

	@Override
	public EnumSet<SpellModifiers> getModifiers() {
		return EnumSet.of(SpellModifiers.FORTUNE_LEVEL, SpellModifiers.MINING_POWER);
	}

	@Override
	public boolean applyEffectEntity(ItemStack stack, World world,
			EntityLivingBase caster, Entity target) {
		return false;
	}

	@Override
	public float manaCost(EntityLivingBase caster) {
		return 10;
	}

	@Override
	public ItemStack[] reagents(EntityLivingBase caster) {
		return null;
	}

	@Override
	public void spawnParticles(World world, double x, double y, double z,
			EntityLivingBase caster, Entity target, Random rand,
			int colorModifier) {
	}

	@Override
	public Set<Affinity> getAffinity() {
		return Sets.newHashSet(Affinity.EARTH);
	}

	@Override
	public float getAffinityShift(Affinity affinity) {
		return 0.001F;
	}

	@Override
	public void encodeBasicData(NBTTagCompound tag, Object[] recipe) {
	}



}
