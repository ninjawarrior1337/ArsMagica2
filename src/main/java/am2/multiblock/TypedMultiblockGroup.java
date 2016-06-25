package am2.multiblock;

import java.util.ArrayList;
import java.util.HashMap;

import am2.utils.KeyValuePair;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TypedMultiblockGroup extends MultiblockGroup{
	
	protected ArrayList<HashMap<Integer, IBlockState>> states;
	protected HashMap<BlockPos, Integer> groups = new HashMap<>();
	
	private static ArrayList<IBlockState> createStateList(ArrayList<HashMap<Integer, IBlockState>> arrayList) {
		ArrayList<IBlockState> stateMap = new ArrayList<>();
		for (HashMap<Integer, IBlockState> map : arrayList) {
			stateMap.addAll(map.values());
		}
		return stateMap;
	}
	
	public TypedMultiblockGroup(String name, ArrayList<HashMap<Integer, IBlockState>> arrayList, boolean ignoreState) {
		super(name, createStateList(arrayList), ignoreState);
		this.states = arrayList;
	}
	
	public void addBlock(BlockPos position, int group) {
		super.addBlock(position);
		groups.put(position, group);
	}
	
	public int getGroup(BlockPos pos) {
		return groups.get(pos);
	}
	
	public ArrayList<IBlockState> getState(BlockPos pos) {
		ArrayList<IBlockState> state = new ArrayList<>();
		for (HashMap<Integer, IBlockState> map : states) {
			state.add(map.get(getGroup(pos)));
		}
		return state;
	}
	
	@Override
	public boolean matches(World world, BlockPos startCheckPos) {
		boolean flag = true;
		for (HashMap<Integer, IBlockState> pair : states) {
			for (BlockPos pos : positions) {
				BlockPos newPos = startCheckPos.add(pos);
				if (ignoreState) {
					boolean subFlag = false;
					subFlag = world.getBlockState(startCheckPos.add(newPos)).getBlock().equals(pair.get(getGroup(newPos)));
					if (subFlag)
						break;
					flag = subFlag;
				}
				else {
					boolean subFlag = false;
					flag = world.getBlockState(startCheckPos.add(pos)).equals(pair.get(getGroup(newPos)).getBlock());
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
