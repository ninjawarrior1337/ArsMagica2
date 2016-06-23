package am2.multiblock;

import java.util.ArrayList;
import java.util.HashMap;

import am2.utils.KeyValuePair;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BiMultiblockGroup extends MultiblockGroup{
	
	protected ArrayList<KeyValuePair<IBlockState, IBlockState>> states;
	protected HashMap<BlockPos, Integer> groups = new HashMap<>();

	public BiMultiblockGroup(String name, ArrayList<KeyValuePair<IBlockState, IBlockState>> arrayList, boolean ignoreState) {
		super(name, KeyValuePair.merge(arrayList), ignoreState);
		this.states = arrayList;
	}
	
	public void addBlock(BlockPos position, int group) {
		super.addBlock(position);
		groups.put(position, group);
	}
	
	public int getGroup(BlockPos pos) {
		return groups.get(pos);
	}
	
	@Override
	public boolean matches(World world, BlockPos startCheckPos) {
		boolean flag = true;
		for (KeyValuePair<IBlockState, IBlockState> pair : states) {
			for (BlockPos pos : positions) {
				BlockPos newPos = startCheckPos.add(pos);
				if (ignoreState) {
					boolean subFlag = false;
					subFlag = world.getBlockState(startCheckPos.add(newPos)).getBlock().equals(getGroup(pos) == 0 ? pair.key : pair.value);
					if (subFlag)
						break;
					flag = subFlag;
				}
				else {
					boolean subFlag = false;
					flag = world.getBlockState(startCheckPos.add(pos)).equals(getGroup(pos) == 0 ? pair.key : pair.value);
					if (subFlag)
						break;
					flag = subFlag;
					
				}
				if (!flag)
					break;
			}
			if (flag)
				break;
		}
		
		return flag;
	}
}
