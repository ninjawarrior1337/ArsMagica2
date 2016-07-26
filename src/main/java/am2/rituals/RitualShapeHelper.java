package am2.rituals;

import java.util.HashMap;
import java.util.List;

import com.google.common.collect.Lists;

import am2.api.blocks.MultiblockGroup;
import am2.api.blocks.MultiblockStructureDefinition;
import am2.api.blocks.TypedMultiblockGroup;
import am2.defs.BlockDefs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RitualShapeHelper {
	
	public static final RitualShapeHelper instance = new RitualShapeHelper();
	
	public MultiblockStructureDefinition corruption = new MultiblockStructureDefinition("corruption");
	public MultiblockStructureDefinition purification = new MultiblockStructureDefinition("purification");
	public MultiblockStructureDefinition hourglass = new MultiblockStructureDefinition("hourglass");
	public MultiblockStructureDefinition ringedCross = new MultiblockStructureDefinition("ringedCross");
	
	public boolean matchesRitual(IRitualInteraction ritual, World world, BlockPos pos) {
		List<EntityItem> items = world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos, pos.add(1, 1, 1)).expand(ritual.getReagentSearchRadius(), ritual.getReagentSearchRadius(), ritual.getReagentSearchRadius()));
		if (!ritual.getRitualShape().matches(world, pos)) {
			return false;
		}
		for (ItemStack stack : ritual.getReagents()) {
			boolean matches = false;
			for (EntityItem item : items) {
				ItemStack is = item.getEntityItem();
				if (is.getItem().equals(stack.getItem()) && is.getMetadata() == stack.getMetadata() && is.stackSize >= stack.stackSize)
					matches = true;
			}
			if (!matches)
				return false;
		}
		return true;
	}
	
	public void consumeReagents(IRitualInteraction ritual, World world, BlockPos pos) {
		List<EntityItem> items = world.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos, pos.add(1, 1, 1)).expand(ritual.getReagentSearchRadius(), ritual.getReagentSearchRadius(), ritual.getReagentSearchRadius()));
		for (ItemStack stack : ritual.getReagents()) {
			for (EntityItem item : items) {
				ItemStack is = item.getEntityItem();
				if (is.getItem().equals(stack.getItem()) && is.getMetadata() == stack.getMetadata() && is.stackSize >= stack.stackSize) {
					is.stackSize -= stack.stackSize;
					if (is.stackSize <= 0)
						item.setDead();
					else
						item.setEntityItemStack(is);
				}
			}
		}
	}
	
	public void consumeShape(IRitualInteraction ritual, World world, BlockPos pos) {
		for (MultiblockGroup group : ritual.getRitualShape().getMatchingGroups(world, pos)) {
			for (BlockPos blockPos : group.getPositions()) {
				IBlockState state = world.getBlockState(pos.add(blockPos));
				world.setBlockToAir(pos.add(blockPos));
				world.notifyBlockUpdate(pos.add(blockPos), state, Blocks.AIR.getDefaultState(), 3);
			}
		}
	}
	
	private RitualShapeHelper() {
		corruptionRitual();
		purificationRitual();
		hourglassRitual();
		ringedCrossRitual();
	}
	
	@SuppressWarnings("unchecked")
	private void corruptionRitual() {
		HashMap<Integer, IBlockState> corruptionMap = new HashMap<>();
		corruptionMap.put(0, BlockDefs.wizardChalk.getDefaultState());
		corruptionMap.put(1, BlockDefs.wardingCandle.getDefaultState());
		TypedMultiblockGroup defaultRotation = new TypedMultiblockGroup("defaultRotation", Lists.newArrayList(corruptionMap), true);
		TypedMultiblockGroup rotated = new TypedMultiblockGroup("rotated", Lists.newArrayList(corruptionMap), true);
		
		defaultRotation.addBlock(new BlockPos(1, 0, 0), 0);
		defaultRotation.addBlock(new BlockPos(-1, 0, 0), 0);

		defaultRotation.addBlock(new BlockPos(2, 0, 1), 0);
		defaultRotation.addBlock(new BlockPos(-2, 0, 1), 0);
		defaultRotation.addBlock(new BlockPos(2, 0, -1), 0);
		defaultRotation.addBlock(new BlockPos(-2, 0, -1), 0);

		defaultRotation.addBlock(new BlockPos(2, 0, 2), 0);
		defaultRotation.addBlock(new BlockPos(1, 0, 2), 1);
		defaultRotation.addBlock(new BlockPos(0, 0, 2), 0);
		defaultRotation.addBlock(new BlockPos(-1, 0, 2), 1);
		defaultRotation.addBlock(new BlockPos(-2, 0, 2), 0);
		defaultRotation.addBlock(new BlockPos(2, 0, -2), 0);
		defaultRotation.addBlock(new BlockPos(1, 0, -2), 1);
		defaultRotation.addBlock(new BlockPos(0, 0, -2), 0);
		defaultRotation.addBlock(new BlockPos(-1, 0, -2), 1);
		defaultRotation.addBlock(new BlockPos(-2, 0, -2), 0);

		defaultRotation.addBlock(new BlockPos(1, 0, 3), 0);
		defaultRotation.addBlock(new BlockPos(-1, 0, 3), 0);
		defaultRotation.addBlock(new BlockPos(1, 0, -3), 0);
		defaultRotation.addBlock(new BlockPos(-1, 0, -3), 0);
		
		
		
		rotated.addBlock(new BlockPos(0, 0, 1), 0);
		rotated.addBlock(new BlockPos(0, 0, -1), 0);

		rotated.addBlock(new BlockPos(1, 0, 2), 0);
		rotated.addBlock(new BlockPos(1, 0, -2), 0);
		rotated.addBlock(new BlockPos(-1, 0, 2), 0);
		rotated.addBlock(new BlockPos(-1, 0, -2), 0);

		rotated.addBlock(new BlockPos(2, 0, 2), 0);
		rotated.addBlock(new BlockPos(2, 0, 1), 1);
		rotated.addBlock(new BlockPos(2, 0, 0), 0);
		rotated.addBlock(new BlockPos(2, 0, -1), 1);
		rotated.addBlock(new BlockPos(2, 0, -2), 0);
		rotated.addBlock(new BlockPos(-2, 0, 2), 0);
		rotated.addBlock(new BlockPos(-2, 0, 1), 1);
		rotated.addBlock(new BlockPos(-2, 0, 0), 0);
		rotated.addBlock(new BlockPos(-2, 0, -1), 1);
		rotated.addBlock(new BlockPos(-2, 0, -2), 0);

		rotated.addBlock(new BlockPos(3, 0, 1), 0);
		rotated.addBlock(new BlockPos(3, 0, -1), 0);
		rotated.addBlock(new BlockPos(-3, 0, 1), 0);
		rotated.addBlock(new BlockPos(-3, 0, -1), 0);
		
		corruption.addGroup(defaultRotation, rotated);
	}
	
	private void purificationRitual() {
		MultiblockGroup chalks = new MultiblockGroup("chalk", Lists.newArrayList(BlockDefs.wizardChalk.getDefaultState()), true);
		MultiblockGroup candles = new MultiblockGroup("candle", Lists.newArrayList(BlockDefs.wardingCandle.getDefaultState()), true);
		chalks.addBlock(new BlockPos(-1, 0, 1));
		chalks.addBlock(new BlockPos(-1, 0, -1));
		chalks.addBlock(new BlockPos(1, 0, 1));
		chalks.addBlock(new BlockPos(1, 0, -1));

		chalks.addBlock(new BlockPos(-2, 0, 1));
		chalks.addBlock(new BlockPos(-2, 0, -1));

		chalks.addBlock(new BlockPos(2, 0, 1));
		chalks.addBlock(new BlockPos(2, 0, -1));

		chalks.addBlock(new BlockPos(1, 0, -2));
		chalks.addBlock(new BlockPos(-1, 0, -2));

		chalks.addBlock(new BlockPos(1, 0, 2));
		chalks.addBlock(new BlockPos(-1, 0, 2));

		chalks.addBlock(new BlockPos(-3, 0, 1));
		chalks.addBlock(new BlockPos(-3, 0, 0));
		chalks.addBlock(new BlockPos(-3, 0, -1));

		chalks.addBlock(new BlockPos(3, 0, 1));
		chalks.addBlock(new BlockPos(3, 0, 0));
		chalks.addBlock(new BlockPos(3, 0, -1));

		chalks.addBlock(new BlockPos(1, 0, -3));
		chalks.addBlock(new BlockPos(0, 0, -3));
		chalks.addBlock(new BlockPos(-1, 0, -3));

		chalks.addBlock(new BlockPos(1, 0, 3));
		chalks.addBlock(new BlockPos(0, 0, 3));
		chalks.addBlock(new BlockPos(-1, 0, 3));

		candles.addBlock(new BlockPos(-2, 0, 2));
		candles.addBlock(new BlockPos(-2, 0, -2));
		candles.addBlock(new BlockPos(2, 0, 2));
		candles.addBlock(new BlockPos(2, 0, -2));
		
		purification.addGroup(candles);
		purification.addGroup(chalks);
	}
	
	private void hourglassRitual() {
		MultiblockGroup chalks = new MultiblockGroup("chalk", Lists.newArrayList(BlockDefs.wizardChalk.getDefaultState()), true);

		chalks.addBlock(new BlockPos(0, 0, 0));

		chalks.addBlock(new BlockPos(-1, 0, 1));
		chalks.addBlock(new BlockPos(-1, 0, -1));
		chalks.addBlock(new BlockPos(1, 0, 1));
		chalks.addBlock(new BlockPos(1, 0, -1));

		chalks.addBlock(new BlockPos(-2, 0, 1));
		chalks.addBlock(new BlockPos(-2, 0, 0));
		chalks.addBlock(new BlockPos(-2, 0, -1));
		chalks.addBlock(new BlockPos(2, 0, 1));
		chalks.addBlock(new BlockPos(2, 0, 0));
		chalks.addBlock(new BlockPos(2, 0, -1));
		
		hourglass.addGroup(chalks);
	}
	
	private void ringedCrossRitual() {
		MultiblockGroup chalks = new MultiblockGroup("chalk", Lists.newArrayList(BlockDefs.wizardChalk.getDefaultState()), true);
		
		chalks.addBlock(new BlockPos(1, 0, 0));
		chalks.addBlock(new BlockPos(-1, 0, 0));
		chalks.addBlock(new BlockPos(0, 0, 1));
		chalks.addBlock(new BlockPos(0, 0, -1));
		
		chalks.addBlock(new BlockPos(1, 0, 2));
		chalks.addBlock(new BlockPos(0, 0, 2));
		chalks.addBlock(new BlockPos(-1, 0, 2));

		chalks.addBlock(new BlockPos(1, 0, -2));
		chalks.addBlock(new BlockPos(0, 0, -2));
		chalks.addBlock(new BlockPos(-1, 0, -2));

		chalks.addBlock(new BlockPos(2, 0, 1));
		chalks.addBlock(new BlockPos(2, 0, 0));
		chalks.addBlock(new BlockPos(2, 0, -1));

		chalks.addBlock(new BlockPos(-2, 0, 1));
		chalks.addBlock(new BlockPos(-2, 0, 0));
		chalks.addBlock(new BlockPos(-2, 0, -1));
		
		ringedCross.addGroup(chalks);
	}
}
