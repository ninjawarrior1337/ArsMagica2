package am2.multiblock;

import java.util.ArrayList;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MultiblockStructureDefinition {
	
	public ArrayList<MultiblockGroup> groups;
	
	public MultiblockStructureDefinition() {
		groups = new ArrayList<>();
	}
	
	public void addGroup (MultiblockGroup group) {
		groups.add(group);
	}
	
	public boolean matches (World world, BlockPos startCheckPos) {
		boolean flag = false;
		for (int i = getMinX(); i < getMaxX() && !flag; i++) {
			for (int j = getMinY(); j < getMaxY() && !flag; j++) {
				for (int k = getMinZ(); k < getMaxZ() && !flag; k++) {
					BlockPos toAdd = new BlockPos(i, j, k);
					for (int l = 0; l < 4 && !flag; l++) {
						boolean subFlag = true;
						for (MultiblockGroup group : groups) {
							MultiblockGroup gr = group.rotate(l);
							subFlag = gr.matches(world, startCheckPos.add(toAdd));
							if (!subFlag)
								break;
						}
						flag = subFlag;
					}
				}
			}
		}
		for (MultiblockGroup group : groups) {
			if (!group.matches(world, startCheckPos))
				return false;
		}
		return true;
	}
	
	public int getMinX () {
		int min = Integer.MAX_VALUE;
		for (MultiblockGroup group : groups) {
			if (group.getMinX() < min)
				min = group.getMinX();
		}
		return min;
	}
	
	public int getMinY () {
		int min = Integer.MAX_VALUE;
		for (MultiblockGroup group : groups) {
			if (group.getMinY() < min)
				min = group.getMinY();
		}
		return min;
	}
	
	public int getMinZ () {
		int min = Integer.MAX_VALUE;
		for (MultiblockGroup group : groups) {
			if (group.getMinZ() < min)
				min = group.getMinZ();
		}
		return min;
	}
	
	public int getMaxX () {
		int max = Integer.MIN_VALUE;
		for (MultiblockGroup group : groups) {
			if (group.getMaxX() > max)
				max = group.getMaxX();
		}
		return max;
	}
	
	public int getMaxY () {
		int max = Integer.MIN_VALUE;
		for (MultiblockGroup group : groups) {
			if (group.getMaxY() > max)
				max = group.getMaxY();
		}
		return max;
	}
	
	public int getMaxZ () {
		int max = Integer.MIN_VALUE;
		for (MultiblockGroup group : groups) {
			if (group.getMaxZ() > max)
				max = group.getMaxZ();
		}
		return max;
	}
}
