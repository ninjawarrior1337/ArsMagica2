package am2.utils;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldUtils {
	
	public static Block getBlockAt (World world, BlockPos pos) {
		return getBlockStateAt(world, pos).getBlock();
	}
	
	public static IBlockState getBlockStateAt (World world, BlockPos pos) {
		return world.getBlockState(pos);
	}
	
	public static boolean hasBlockUnder (World world, Block block, BlockPos checkPos) {
		boolean flag = false;
		BlockPos checkStart = checkPos.down();
		for (int x = -1; x < 2 || flag; x++) {
			for (int y = -1; y < 1 || flag; y++) {
				for (int z = -1; z < 2 || flag; z++) {
					BlockPos currentPos = new BlockPos(checkStart.getX() + x, checkStart.getY() + y, checkStart.getZ() + z);
					if (getBlockAt(world, currentPos).equals(block)) {
						flag = true;
					}
				}
			}
		}
		return flag;
	}
	
	public static int getBlockMeta(IBlockState state) {
		return state.getBlock().getMetaFromState(state);
	}
}
