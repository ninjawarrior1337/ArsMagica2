package am2.multiblock;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import am2.defs.BlockDefs;

import com.google.common.collect.Lists;

public class CraftingAltar extends MultiblockStructureDefinition{
	
	public CraftingAltar() {
		super("crafting_altar");
		MultiblockGroup groupMain = new MultiblockGroup(Lists.newArrayList(Blocks.QUARTZ_BLOCK.getDefaultState()), false);
		for (int x = -2; x <= 2; x++) {
			for (int z = -2; z <= 2; z++) {
				if (x != 0 && z != 0)
					groupMain.addBlock(new BlockPos(x, 0, z));
			}
		}
		for (int y = 1; y <= 4; y++) {
			groupMain.addBlock(new BlockPos(-2, y, 1));
			groupMain.addBlock(new BlockPos(-2, y, -1));
			groupMain.addBlock(new BlockPos(2, y, 1));
			groupMain.addBlock(new BlockPos(2, y, -1));
		}
		groupMain.addBlock(new BlockPos(-2, 5, 0));
		groupMain.addBlock(new BlockPos(-1, 5, -1));
		groupMain.addBlock(new BlockPos(-1, 5, 0));
		groupMain.addBlock(new BlockPos(-1, 5, 1));
		groupMain.addBlock(new BlockPos(1, 5, -1));
		groupMain.addBlock(new BlockPos(1, 5, 0));
		groupMain.addBlock(new BlockPos(1, 5, 1));
		groupMain.addBlock(new BlockPos(2, 5, 0));
		MultiblockGroup groupSub = new MultiblockGroup(Lists.newArrayList(Blocks.GLASS.getDefaultState()), false);
		groupSub.addBlock(new BlockPos(0, 0, 0));
		groupSub.addBlock(new BlockPos(-2, 5, 1));
		groupSub.addBlock(new BlockPos(-2, 5, -1));
		groupSub.addBlock(new BlockPos(2, 5, 1));
		groupSub.addBlock(new BlockPos(2, 5, -1));
		MultiblockGroup groupWall = new MultiblockGroup(Lists.newArrayList(BlockDefs.magicWall.getDefaultState()), false);
		groupWall.addBlock(new BlockPos(-2, 1, 0));
		groupWall.addBlock(new BlockPos(-2, 2, 0));
		groupWall.addBlock(new BlockPos(-2, 3, 0));
		groupWall.addBlock(new BlockPos(2, 1, 0));
		groupWall.addBlock(new BlockPos(2, 2, 0));
		groupWall.addBlock(new BlockPos(2, 3, 0));
		this.addGroup(groupMain);
		this.addGroup(groupSub);
		this.addGroup(groupWall);
	}
}
