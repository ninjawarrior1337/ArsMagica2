package am2.multiblock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MultiblockStructureDefinition {
	
	public ArrayList<List<MultiblockGroup>> groups;
	String id;
	
	public MultiblockStructureDefinition(String id) {
		groups = new ArrayList<>();
		this.id = id;
	}
	
	public void addGroup (MultiblockGroup group, MultiblockGroup... rest) {
		groups.add(Lists.asList(group, rest));
	}
	
	public boolean matches (World world, BlockPos startCheckPos) {
		boolean flag = false;
		for (int l = 0; l < 4 && !flag; l++) {
			boolean subFlag = true;
			for (List<MultiblockGroup> subGroup : groups) {
				boolean groupCheck = false;

				for (MultiblockGroup group : subGroup) {
					MultiblockGroup gr = group.rotate(l);
					subFlag |= gr.matches(world, startCheckPos);
				}
				subFlag &= groupCheck;
			}
			flag = subFlag;
		}
		return flag;
	}
	
	public List<MultiblockGroup> getMatchingGroups (World world, BlockPos startCheckPos) {
		List<MultiblockGroup> list = new ArrayList<>();
		for (int l = 0; l < 4 && list.size() != groups.size(); l++) {
			list.clear();
			for (List<MultiblockGroup> subGroup : groups) {
				for (MultiblockGroup group : subGroup) {
					MultiblockGroup gr = group.rotate(l);
					if (gr.matches(world, startCheckPos)) {
						list.add(gr);
						break;
					}
				}
			}
		}
		return list.size() == groups.size() ? list : new ArrayList<>();
	}
	
	public HashMap<BlockPos, List<IBlockState>> getStructureLayer(MultiblockGroup selected, int layer) {
		HashMap<BlockPos, List<IBlockState>> stateMap = new HashMap<>();
		for (BlockPos entry : selected.getPositions()) {
			if (entry.getY() == layer)
				stateMap.put(entry, selected.getStates());
		}
		return stateMap;
	}
	
	public int getMinX () {
		int min = Integer.MAX_VALUE;
		for (List<MultiblockGroup> group : groups) {
			for (MultiblockGroup gr : group) {
				if (gr.getMinX() < min)
					min = gr.getMinX();
			}
		}
		return min;
	}
	
	public int getMinY () {
		int min = Integer.MAX_VALUE;
		for (List<MultiblockGroup> group : groups) {
			for (MultiblockGroup gr : group) {
				if (gr.getMinY() < min)
					min = gr.getMinY();
			}
		}
		return min;
	}
	
	public int getMinZ () {
		int min = Integer.MAX_VALUE;
		for (List<MultiblockGroup> group : groups) {
			for (MultiblockGroup gr : group) {
				if (gr.getMinZ() < min)
					min = gr.getMinZ();
			}
		}
		return min;
	}
	
	public int getMaxX () {
		int max = Integer.MIN_VALUE;
		for (List<MultiblockGroup> group : groups) {
			for (MultiblockGroup gr : group) {
				if (gr.getMaxX() > max)
					max = gr.getMaxX();
			}
		}
		return max;
	}
	
	public int getMaxY () {
		int max = Integer.MIN_VALUE;
		for (List<MultiblockGroup> group : groups) {
			for (MultiblockGroup gr : group) {
				if (gr.getMaxY() > max)
					max = gr.getMaxY();
			}
		}
		return max;
	}
	
	public int getMaxZ () {
		int max = Integer.MIN_VALUE;
		for (List<MultiblockGroup> group : groups) {
			for (MultiblockGroup gr : group) {
				if (gr.getMaxZ() > max)
					max = gr.getMaxZ();
			}
		}
		return max;
	}

	public int getWidth() {
		return getMaxX() - getMinX();
	}
	
	public int getLength() {
		return getMaxZ() - getMinZ();
	}
	
	public int getHeight() {
		return getMaxY() - getMinY();
	}
	
	public String getId() {
		return id;
	}

	public ArrayList<MultiblockGroup> getGroups() {
		ArrayList<MultiblockGroup> list = new ArrayList<>();
		for (List<MultiblockGroup> groups : this.groups) {
			list.add(groups.get(0));
		}
		return list;
	}
}
