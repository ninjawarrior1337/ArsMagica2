package am2.spell.component;

import java.util.Random;
import java.util.Set;

import com.google.common.collect.Sets;

import am2.affinity.Affinity;
import am2.defs.SkillDefs;
import am2.extensions.EntityExtension;
import am2.spell.IComponent;
import am2.spell.SpellModifiers;
import am2.utils.SpellUtils;
import am2.utils.WorldUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

public class Dig implements IComponent {

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
		Block block = world.getBlockState(blockPos).getBlock();
		@SuppressWarnings("deprecation")
		float hardness = block.getBlockHardness(world.getBlockState(blockPos), world, blockPos);
		if (block.equals(Blocks.AIR) || hardness == -1 || block.getHarvestLevel(world.getBlockState(blockPos)) > SpellUtils.getModifiedInt_Add(2, stack, caster, null, world, SpellModifiers.MINING_POWER))
			return false;
		if (EntityExtension.For(caster).useMana((int) hardness)) {
			IBlockState old = world.getBlockState(blockPos);
			block.breakBlock(world, blockPos, old);
			block.dropBlockAsItem(world, blockPos, world.getBlockState(blockPos), SpellUtils.getModifiedInt_Add(0, stack, caster, null, world, SpellModifiers.FORTUNE_LEVEL));
			world.setBlockToAir(blockPos);
			world.markAndNotifyBlock(blockPos, world.getChunkFromBlockCoords(blockPos), old, Blocks.AIR.getDefaultState(), 3);
		}
		return true;
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
		return Sets.newHashSet(SkillDefs.EARTH);
	}

	@Override
	public float getAffinityShift(Affinity affinity) {
		return 0.001F;
	}

	@Override
	public void encodeBasicData(NBTTagCompound tag, Object[] recipe) {
	}

}
