package am2.api.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;

public class MultiblockStructureDefinition{
	public class BlockDec{
		Block block;
		int meta;

		public BlockDec(Block block, int meta){
			this.block = block;
			this.meta = meta;
		}

		public Block getBlock(){
			return block;
		}

		public int getMeta(){
			return meta;
		}

		@Override
		public String toString(){
			String blockName = "";
			if (block != null){
				blockName = block.getLocalizedName();
			}else{
				blockName = "Unknown";
			}
			return String.format("Block: %s, meta: %d", blockName, meta);
		}

		@Override
		public boolean equals(Object obj){
			if (obj instanceof BlockDec){
				return this.block == ((BlockDec)obj).block && (this.meta == -1 || ((BlockDec)obj).meta == -1 || this.meta == ((BlockDec)obj).meta);
			}
			return false;
		}

		@Override
		public int hashCode(){
			return Block.getIdFromBlock(block);
		}
	}
	
	public class StructureGroup{
		String name;
		int mutex;
		HashMap<BlockPos, ArrayList<BlockDec>> allowedBlocks;

		public StructureGroup(String name, int mutex){
			this.name = name;
			this.mutex = mutex;
			allowedBlocks = new HashMap<BlockPos, ArrayList<BlockDec>>();
		}

		void addAllowedBlock(BlockPos pos, Block block, int meta){
			if (!allowedBlocks.containsKey(pos)){
				allowedBlocks.put(pos, new ArrayList<BlockDec>());
			}
			ArrayList<BlockDec> positionReplacements = allowedBlocks.get(pos);
			positionReplacements.add(new BlockDec(block, meta));
		}

		ArrayList<BlockDec> getAllowedBlocksAt(BlockPos coord){
			return allowedBlocks.get(coord);
		}

		boolean matchGroup(World world, BlockPos pos){
			for (BlockPos offset : allowedBlocks.keySet()){
				IBlockState block = world.getBlockState(pos.add(offset));
				ArrayList<BlockDec> positionReplacements = allowedBlocks.get(offset);
				boolean valid = false;
				for (BlockDec bd : positionReplacements){
					if (bd.block.equals(block.getBlock()) && (bd.meta == -1 || bd.meta == block.getBlock().getMetaFromState(block))){
						valid = true;
					}
				}
				if (!valid) return false;
			}
			return true;
		}

		HashMap<BlockPos, ArrayList<BlockDec>> getStructureLayer(int layer){
			HashMap<BlockPos, ArrayList<BlockDec>> toReturn = new HashMap<BlockPos, ArrayList<BlockDec>>();

			if (layer > getMaxLayer() || layer < getMinLayer()){
				return toReturn;
			}

			for (BlockPos bc : allowedBlocks.keySet()){
				if (bc.getY() == layer){
					toReturn.put(bc, allowedBlocks.get(bc));
				}
			}
			return toReturn;
		}

		public void replaceAllBlocksOfType(Block originalBlock, Block newBlock){
			replaceAllBlocksOfType(originalBlock, -1, newBlock, -1);
		}

		public void replaceAllBlocksOfType(Block originalBlock, int originalMeta, Block newBlock, int newMeta){
			for (BlockPos bc : allowedBlocks.keySet()){
				for (BlockDec bd : allowedBlocks.get(bc)){
					if (bd.block == originalBlock){
						if (bd.meta == originalMeta || originalMeta == -1){
							bd.block = newBlock;
							if (newMeta != -1){
								bd.meta = newMeta;
							}
						}
					}
				}
			}
		}

		public HashMap<BlockPos, ArrayList<BlockDec>> getAllowedBlocks(){
			return (HashMap<BlockPos, ArrayList<BlockDec>>)allowedBlocks.clone();
		}

		public void deleteBlocksFromWorld(World world, BlockPos pos){
			for (BlockPos offset : allowedBlocks.keySet()){
				world.setBlockToAir(pos.add(offset));
			}
		}
	}

	private StructureGroup mainGroup;
	private ArrayList<StructureGroup> blockGroups;
	private ArrayList<Integer> mutexCache;

	public static final int MAINGROUP_MUTEX = 1;

	private String id;

	private int maxX = 0;
	private int minX = 0;
	private int maxY = 0;
	private int minY = 0;
	private int maxZ = 0;
	private int minZ = 0;

	public MultiblockStructureDefinition(String id){
		blockGroups = new ArrayList<StructureGroup>();
		mutexCache = new ArrayList<Integer>();
		this.id = id;

		//default group
		mainGroup = createGroup("main", MAINGROUP_MUTEX);
	}

	public String getID(){
		return this.id;
	}

	public ArrayList<Integer> getMutexList(){
		return mutexCache;
	}

	public ArrayList<StructureGroup> getGroupsForMutex(int mutex){
		ArrayList<StructureGroup> toReturn = new ArrayList<StructureGroup>();

		for (StructureGroup group : blockGroups){
			if (group.mutex == mutex){
				toReturn.add(group);
			}
		}

		return toReturn;
	}

	public ArrayList<BlockDec> getAllowedBlocksAt(StructureGroup group, BlockPos coord){
		return group.getAllowedBlocksAt(coord);
	}

	public ArrayList<BlockDec> getAllowedBlocksAt(BlockPos coord){
		return mainGroup.getAllowedBlocksAt(coord);
	}

	public void addAllowedBlock(BlockPos pos, Block block, int meta){

		if (pos.getY() > maxY){
			maxY = pos.getY();
		}else if (pos.getY() < minY){
			minY = pos.getY();
		}

		if (pos.getX() > maxX){
			maxX = pos.getX();
		}else if (pos.getX() < minX){
			minX = pos.getX();
		}

		if (pos.getZ() > maxZ){
			maxZ = pos.getZ();
		}else if (pos.getZ() < minZ){
			minZ = pos.getZ();
		}

		mainGroup.addAllowedBlock(pos, block, meta);
	}

	public void addAllowedBlock(BlockPos pos, Block block){
		addAllowedBlock(pos, block, -1);
	}

	public void addAllowedBlock(StructureGroup group, BlockPos pos, Block block, int meta){
		if (!blockGroups.contains(group)){
			blockGroups.add(group);
		}

		if (pos.getY() > maxY){
			maxY = pos.getY();
		}else if (pos.getY() < minY){
			minY = pos.getY();
		}

		if (pos.getX() > maxX){
			maxX = pos.getX();
		}else if (pos.getX() < minX){
			minX = pos.getX();
		}

		if (pos.getZ() > maxZ){
			maxZ = pos.getZ();
		}else if (pos.getZ() < minZ){
			minZ = pos.getZ();
		}

		group.addAllowedBlock(pos, block, meta);
	}

	public void addAllowedBlock(StructureGroup group, BlockPos pos, Block block){
		addAllowedBlock(group, pos, block, -1);
	}

	public StructureGroup createGroup(String name, int mutex){
		if (!mutexCache.contains(mutex)){
			mutexCache.add(mutex);
		}
		StructureGroup group = new StructureGroup(name, mutex);
		blockGroups.add(group);
		return group;
	}

	public StructureGroup copyGroup(String originalName, String destinationName, int newMutex){
		StructureGroup copyGroup = null;
		for (StructureGroup group : blockGroups){
			if (group.name.equals(originalName)){
				copyGroup = group;
				break;
			}
		}

		if (copyGroup == null) return null;

		StructureGroup newGroup;

		if (newMutex > -1){
			newGroup = new StructureGroup(destinationName, newMutex);
		}else{
			newGroup = new StructureGroup(destinationName, copyGroup.mutex);
		}

		for (BlockPos bc : copyGroup.allowedBlocks.keySet()){
			for (BlockDec bd : copyGroup.allowedBlocks.get(bc)){
				newGroup.addAllowedBlock(bc, bd.block, bd.meta);
			}
		}

		blockGroups.add(newGroup);

		return newGroup;
	}

	public StructureGroup copyGroup(String originalName, String destinationName){
		return copyGroup(originalName, destinationName, -1);
	}

	public ArrayList<StructureGroup> getMatchedGroups(int mutex, World world, BlockPos pos){
		ArrayList<StructureGroup> toReturn = new ArrayList<StructureGroup>();
		for (StructureGroup group : blockGroups){
			if ((group.mutex & mutex) == group.mutex){
				if (group.matchGroup(world, pos)){
					toReturn.add(group);
				}
			}
		}
		return toReturn;
	}

	private boolean matchMutex(int mutex, World world, BlockPos pos){
		for (StructureGroup group : blockGroups){
			if ((group.mutex & mutex) == group.mutex){
				if (group.matchGroup(world, pos)){
					return true;
				}
			}
		}
		return false;
	}

	public boolean checkStructure(World world, BlockPos pos){
		boolean valid = true;
		for (int i : mutexCache){
			valid &= matchMutex(i, world, pos);
			if (!valid) break;
		}
		return valid;
	}

	public int getMinLayer(){
		return this.minY;
	}

	public int getMaxLayer(){
		return this.maxY;
	}

	public int getHeight(){
		return this.maxY - this.minY;
	}

	public int getWidth(){
		return this.maxX - this.minX;
	}

	public int getLength(){
		return this.maxZ - this.minZ;
	}

	public HashMap<BlockPos, ArrayList<BlockDec>> getStructureLayer(int layer){
		return mainGroup.getStructureLayer(layer);
	}

	public HashMap<BlockPos, ArrayList<BlockDec>> getStructureLayer(StructureGroup group, int layer){
		return group.getStructureLayer(layer);
	}

	public void removeMutex(int mutex, World world, BlockPos pos){
		for (StructureGroup group : blockGroups){
			if (group.mutex == mutex){
				group.deleteBlocksFromWorld(world, pos);
			}
		}
	}
}
