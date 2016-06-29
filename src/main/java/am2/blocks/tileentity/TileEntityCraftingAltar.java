package am2.blocks.tileentity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.common.collect.Lists;

import am2.ArsMagica2;
import am2.api.IMultiblockStructureController;
import am2.api.SpellRegistry;
import am2.api.power.IPowerNode;
import am2.blocks.BlockArsMagicaBlock;
import am2.defs.BlockDefs;
import am2.defs.ItemDefs;
import am2.multiblock.MultiblockGroup;
import am2.multiblock.MultiblockStructureDefinition;
import am2.multiblock.TypedMultiblockGroup;
import am2.packet.AMDataReader;
import am2.packet.AMDataWriter;
import am2.packet.AMNetHandler;
import am2.packet.AMPacketIDs;
import am2.particles.AMParticle;
import am2.particles.ParticleFadeOut;
import am2.particles.ParticleMoveOnHeading;
import am2.power.PowerNodeRegistry;
import am2.power.PowerTypes;
import am2.spell.ISpellPart;
import am2.spell.component.Summon;
import am2.spell.shape.Binding;
import am2.utils.KeyValuePair;
import am2.utils.SpellUtils;
import net.minecraft.block.BlockLever;
import net.minecraft.block.BlockLever.EnumOrientation;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockStairs.EnumHalf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.util.Constants;

public class TileEntityCraftingAltar extends TileEntityAMPower implements IMultiblockStructureController{

	private MultiblockStructureDefinition primary = new MultiblockStructureDefinition("craftingAltar_alt");
	private MultiblockStructureDefinition secondary = new MultiblockStructureDefinition("craftingAltar");
	
	private static final int BLOCKID = 0;
	private static final int STAIR_NORTH = 1;
	private static final int STAIR_SOUTH = 2;
	private static final int STAIR_EAST = 3;
	private static final int STAIR_WEST = 4;
	private static final int STAIR_NORTH_INVERTED = 5;
	private static final int STAIR_SOUTH_INVERTED = 6;
	private static final int STAIR_EAST_INVERTED = 7;
	private static final int STAIR_WEST_INVERTED = 8;
	

	private boolean isCrafting;
	private final ArrayList<ItemStack> allAddedItems;
	private final ArrayList<ItemStack> currentAddedItems;

	private final ArrayList<ISpellPart> spellDef;
	private final NBTTagCompound savedData = new NBTTagCompound();
	private final ArrayList<KeyValuePair<ArrayList<ISpellPart>, NBTTagCompound>> shapeGroups;
	private boolean allShapeGroupsAdded = false;

	private int currentKey = -1;
	private int checkCounter;
	private boolean structureValid;
	private BlockPos podiumLocation;
	private BlockPos switchLocation;
	private int maxEffects;

	private ItemStack addedPhylactery = null;
	private ItemStack addedBindingCatalyst = null;

	private int[] spellGuide;
	private int[] outputCombo;
	private int[][] shapeGroupGuide;

	private int currentConsumedPower = 0;
	private int ticksExisted = 0;
	private PowerTypes currentMainPowerTypes = PowerTypes.NONE;

	private static final byte CRAFTING_CHANGED = 1;
	private static final byte COMPONENT_ADDED = 2;
	private static final byte FULL_UPDATE = 3;

	private static final int augmatl_mutex = 2;
	private static final int lectern_mutex = 4;

	private String currentSpellName = "";

	public TileEntityCraftingAltar(){
		super(500);
		setupMultiblock();
		allAddedItems = new ArrayList<ItemStack>();
		currentAddedItems = new ArrayList<ItemStack>();
		isCrafting = false;
		structureValid = false;
		checkCounter = 0;
		setNoPowerRequests();
		maxEffects = 2;

		spellDef = new ArrayList<>();
		shapeGroups = new ArrayList<>();

		for (int i = 0; i < 5; ++i){
			shapeGroups.add(new KeyValuePair<ArrayList<ISpellPart>, NBTTagCompound>(new ArrayList<>(), new NBTTagCompound()));
		}
	}
	
	private HashMap<Integer, IBlockState> createStateMap(IBlockState block, IBlockState stairs) {
		HashMap<Integer, IBlockState> map = new HashMap<>();
		map.put(BLOCKID, block);
		map.put(STAIR_NORTH, stairs.withProperty(BlockStairs.FACING, EnumFacing.NORTH));
		map.put(STAIR_SOUTH, stairs.withProperty(BlockStairs.FACING, EnumFacing.SOUTH));
		map.put(STAIR_EAST, stairs.withProperty(BlockStairs.FACING, EnumFacing.EAST));
		map.put(STAIR_WEST, stairs.withProperty(BlockStairs.FACING, EnumFacing.WEST));
		map.put(STAIR_NORTH_INVERTED, stairs.withProperty(BlockStairs.FACING, EnumFacing.NORTH).withProperty(BlockStairs.HALF, EnumHalf.TOP));
		map.put(STAIR_SOUTH_INVERTED, stairs.withProperty(BlockStairs.FACING, EnumFacing.SOUTH).withProperty(BlockStairs.HALF, EnumHalf.TOP));
		map.put(STAIR_EAST_INVERTED, stairs.withProperty(BlockStairs.FACING, EnumFacing.EAST).withProperty(BlockStairs.HALF, EnumHalf.TOP));
		map.put(STAIR_WEST_INVERTED, stairs.withProperty(BlockStairs.FACING, EnumFacing.WEST).withProperty(BlockStairs.HALF, EnumHalf.TOP));
		return map;
	}
	
	@SuppressWarnings("unchecked")
	private void setupMultiblock(){
//		primary = new MultiblockStructureDefinition("craftingAltar_alt");
//
//		IBlockState[] augMatls = new IBlockState[]{
//				Blocks.GLASS.getDefaultState(),
//				Blocks.COAL_BLOCK.getDefaultState(),
//				Blocks.REDSTONE_BLOCK.getDefaultState(),
//				Blocks.IRON_BLOCK.getDefaultState(),
//				Blocks.LAPIS_BLOCK.getDefaultState(),
//				Blocks.GOLD_BLOCK.getDefaultState(),
//				Blocks.DIAMOND_BLOCK.getDefaultState(),
//				Blocks.EMERALD_BLOCK.getDefaultState(),
//				BlockDefs.blocks.getDefaultState().withProperty(BlockArsMagicaBlock.BLOCK_TYPE, BlockArsMagicaBlock.EnumBlockType.SUNSTONE),
//				BlockDefs.blocks.getDefaultState().withProperty(BlockArsMagicaBlock.BLOCK_TYPE, BlockArsMagicaBlock.EnumBlockType.MOONSTONE)
//		};
		HashMap<Integer, IBlockState> glass = new HashMap<>();
		HashMap<Integer, IBlockState> coal = new HashMap<>();
		HashMap<Integer, IBlockState> redstone = new HashMap<>();
		HashMap<Integer, IBlockState> iron = new HashMap<>();
		HashMap<Integer, IBlockState> lapis = new HashMap<>();
		HashMap<Integer, IBlockState> gold = new HashMap<>();
		HashMap<Integer, IBlockState> diamond = new HashMap<>();
		HashMap<Integer, IBlockState> emerald = new HashMap<>();
		HashMap<Integer, IBlockState> moonstone = new HashMap<>();
		HashMap<Integer, IBlockState> sunstone = new HashMap<>();
		glass.put(0, Blocks.GLASS.getDefaultState());
		coal.put(0, Blocks.COAL_BLOCK.getDefaultState());
		redstone.put(0, Blocks.REDSTONE_BLOCK.getDefaultState());
		iron.put(0, Blocks.IRON_BLOCK.getDefaultState());
		lapis.put(0, Blocks.LAPIS_BLOCK.getDefaultState());
		gold.put(0, Blocks.GOLD_BLOCK.getDefaultState());
		diamond.put(0, Blocks.DIAMOND_BLOCK.getDefaultState());
		emerald.put(0, Blocks.EMERALD_BLOCK.getDefaultState());
		moonstone.put(0, BlockDefs.blocks.getDefaultState().withProperty(BlockArsMagicaBlock.BLOCK_TYPE, BlockArsMagicaBlock.EnumBlockType.MOONSTONE));
		sunstone.put(0, BlockDefs.blocks.getDefaultState().withProperty(BlockArsMagicaBlock.BLOCK_TYPE, BlockArsMagicaBlock.EnumBlockType.SUNSTONE));
		
		TypedMultiblockGroup catalysts = new TypedMultiblockGroup("catalysts", Lists.newArrayList(glass, coal, redstone, iron, lapis, gold, diamond, emerald, moonstone, sunstone), false);
		
		TypedMultiblockGroup out = new TypedMultiblockGroup("out", 
				Lists.newArrayList(
						createStateMap(Blocks.PLANKS.getDefaultState(), Blocks.OAK_STAIRS.getDefaultState()),
						createStateMap(Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.ACACIA), Blocks.ACACIA_STAIRS.getDefaultState()),
						createStateMap(Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.BIRCH), Blocks.BIRCH_STAIRS.getDefaultState()),
						createStateMap(Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.SPRUCE), Blocks.SPRUCE_STAIRS.getDefaultState()),
						createStateMap(Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.JUNGLE), Blocks.JUNGLE_STAIRS.getDefaultState()),
						createStateMap(Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.DARK_OAK), Blocks.DARK_OAK_STAIRS.getDefaultState()),
						createStateMap(Blocks.QUARTZ_BLOCK.getDefaultState(), Blocks.QUARTZ_STAIRS.getDefaultState()),
						createStateMap(Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK_STAIRS.getDefaultState()),
						createStateMap(Blocks.STONEBRICK.getDefaultState(), Blocks.STONE_BRICK_STAIRS.getDefaultState()),
						createStateMap(Blocks.BRICK_BLOCK.getDefaultState(), Blocks.BRICK_STAIRS.getDefaultState()),
						createStateMap(Blocks.SANDSTONE.getDefaultState(), Blocks.SANDSTONE_STAIRS.getDefaultState()),
						createStateMap(Blocks.PURPUR_BLOCK.getDefaultState(), Blocks.PURPUR_STAIRS.getDefaultState()),
						createStateMap(Blocks.RED_SANDSTONE.getDefaultState(), Blocks.RED_SANDSTONE_STAIRS.getDefaultState())
						), 
				false);
		
		catalysts.addBlock(new BlockPos(-1, 0, -2), 0);
		catalysts.addBlock(new BlockPos(1, 0, -2), 0);
		catalysts.addBlock(new BlockPos(-1, 0, 2), 0);
		catalysts.addBlock(new BlockPos(1, 0, 2), 0);
		catalysts.addBlock(new BlockPos(0, -4, 0), 0);

		out.addBlock(new BlockPos(-1, 0, -1), STAIR_EAST);
		out.addBlock(new BlockPos(-1, 0, 0), STAIR_EAST);
		out.addBlock(new BlockPos(-1, 0, 1), STAIR_EAST);
		out.addBlock(new BlockPos(1, 0, -1), STAIR_WEST);
		out.addBlock(new BlockPos(1, 0, 0), STAIR_WEST);
		out.addBlock(new BlockPos(1, 0, 1), STAIR_WEST);
		out.addBlock(new BlockPos(0, 0, -2), STAIR_SOUTH);
		out.addBlock(new BlockPos(0, 0, 2), STAIR_NORTH);
		out.addBlock(new BlockPos(-1, -1, -1), STAIR_NORTH_INVERTED);
		out.addBlock(new BlockPos(-1, -1, 1), STAIR_SOUTH_INVERTED);
		out.addBlock(new BlockPos(1, -1, -1), STAIR_NORTH_INVERTED);
		out.addBlock(new BlockPos(1, -1, 1), STAIR_SOUTH_INVERTED);
		
		out.addBlock(new BlockPos(0, 0, -1), 0);
		out.addBlock(new BlockPos(0, 0, 1), 0);
		out.addBlock(new BlockPos(1, -1, -2), 0);
		out.addBlock(new BlockPos(1, -1, 2), 0);
		out.addBlock(new BlockPos(-1, -1, -2), 0);
		out.addBlock(new BlockPos(-1, -1, 2), 0);
		out.addBlock(new BlockPos(1, -2, -2), 0);
		out.addBlock(new BlockPos(1, -2, 2), 0);
		out.addBlock(new BlockPos(-1, -2, -2), 0);
		out.addBlock(new BlockPos(-1, -2, 2), 0);
		out.addBlock(new BlockPos(1, -3, -2), 0);
		out.addBlock(new BlockPos(1, -3, 2), 0);
		out.addBlock(new BlockPos(-1, -3, -2), 0);
		out.addBlock(new BlockPos(-1, -3, 2), 0);
		out.addBlock(new BlockPos(-2, -4, -2), 0);		
		out.addBlock(new BlockPos(-2, -4, -1), 0);		
		out.addBlock(new BlockPos(-2, -4, 0), 0);		
		out.addBlock(new BlockPos(-2, -4, 1), 0);		
		out.addBlock(new BlockPos(-2, -4, 2), 0);		
		out.addBlock(new BlockPos(-1, -4, -2), 0);		
		out.addBlock(new BlockPos(-1, -4, -1), 0);		
		out.addBlock(new BlockPos(-1, -4, 0), 0);		
		out.addBlock(new BlockPos(-1, -4, 1), 0);		
		out.addBlock(new BlockPos(-1, -4, 2), 0);		
		out.addBlock(new BlockPos(0, -4, -2), 0);		
		out.addBlock(new BlockPos(0, -4, -1), 0);		
		out.addBlock(new BlockPos(0, -4, 1), 0);		
		out.addBlock(new BlockPos(0, -4, 2), 0);		
		out.addBlock(new BlockPos(1, -4, -2), 0);		
		out.addBlock(new BlockPos(1, -4, -1), 0);		
		out.addBlock(new BlockPos(1, -4, 0), 0);		
		out.addBlock(new BlockPos(1, -4, 1), 0);
		out.addBlock(new BlockPos(1, -4, 2), 0);		
		out.addBlock(new BlockPos(2, -4, -2), 0);		
		out.addBlock(new BlockPos(2, -4, -1), 0);		
		out.addBlock(new BlockPos(2, -4, 0), 0);		
		out.addBlock(new BlockPos(2, -4, 1), 0);		
		out.addBlock(new BlockPos(2, -4, 2), 0);		
		
		MultiblockGroup wall = new MultiblockGroup("wall", Lists.newArrayList(BlockDefs.magicWall.getDefaultState()), true);
		wall.addBlock(new BlockPos(0, -1, -2));
		wall.addBlock(new BlockPos(0, -2, -2));
		wall.addBlock(new BlockPos(0, -3, -2));
		wall.addBlock(new BlockPos(0, -1, 2));
		wall.addBlock(new BlockPos(0, -2, 2));
		wall.addBlock(new BlockPos(0, -3, 2));
		
		MultiblockGroup lever1 = new MultiblockGroup("lever1", Lists.newArrayList(
				Blocks.LEVER.getDefaultState().withProperty(BlockLever.FACING, EnumOrientation.EAST),
				Blocks.LEVER.getDefaultState().withProperty(BlockLever.FACING, EnumOrientation.EAST).withProperty(BlockLever.POWERED, true)
				), false);
		MultiblockGroup lever2 = new MultiblockGroup("lever2", Lists.newArrayList(
				Blocks.LEVER.getDefaultState().withProperty(BlockLever.FACING, EnumOrientation.EAST),
				Blocks.LEVER.getDefaultState().withProperty(BlockLever.FACING, EnumOrientation.EAST).withProperty(BlockLever.POWERED, true)
				), false);
		MultiblockGroup lever3 = new MultiblockGroup("lever3", Lists.newArrayList(
				Blocks.LEVER.getDefaultState().withProperty(BlockLever.FACING, EnumOrientation.WEST),
				Blocks.LEVER.getDefaultState().withProperty(BlockLever.FACING, EnumOrientation.WEST).withProperty(BlockLever.POWERED, true)
				), false);
		MultiblockGroup lever4 = new MultiblockGroup("lever4", Lists.newArrayList(
				Blocks.LEVER.getDefaultState().withProperty(BlockLever.FACING, EnumOrientation.WEST),
				Blocks.LEVER.getDefaultState().withProperty(BlockLever.FACING, EnumOrientation.WEST).withProperty(BlockLever.POWERED, true)
				), false);
		lever1.addBlock(new BlockPos(2, -2, 2));
		lever2.addBlock(new BlockPos(2, -2, -2));
		lever3.addBlock(new BlockPos(-2, -2, 2));
		lever4.addBlock(new BlockPos(-2, -2, -2));
		
		primary.addGroup(wall);
		primary.addGroup(lever1, lever2, lever3,  lever4);
		primary.addGroup(out);
		primary.addGroup(catalysts);
		
		TypedMultiblockGroup catalysts_alt = new TypedMultiblockGroup("catalysts_alt", Lists.newArrayList(glass, coal, redstone, iron, lapis, gold, diamond, emerald, moonstone, sunstone), false);
		
		TypedMultiblockGroup out_alt = new TypedMultiblockGroup("out_alt", 
				Lists.newArrayList(
						createStateMap(Blocks.PLANKS.getDefaultState(), Blocks.OAK_STAIRS.getDefaultState()),
						createStateMap(Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.ACACIA), Blocks.ACACIA_STAIRS.getDefaultState()),
						createStateMap(Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.BIRCH), Blocks.BIRCH_STAIRS.getDefaultState()),
						createStateMap(Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.SPRUCE), Blocks.SPRUCE_STAIRS.getDefaultState()),
						createStateMap(Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.JUNGLE), Blocks.JUNGLE_STAIRS.getDefaultState()),
						createStateMap(Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.DARK_OAK), Blocks.DARK_OAK_STAIRS.getDefaultState()),
						createStateMap(Blocks.QUARTZ_BLOCK.getDefaultState(), Blocks.QUARTZ_STAIRS.getDefaultState()),
						createStateMap(Blocks.NETHER_BRICK.getDefaultState(), Blocks.NETHER_BRICK_STAIRS.getDefaultState()),
						createStateMap(Blocks.STONEBRICK.getDefaultState(), Blocks.STONE_BRICK_STAIRS.getDefaultState()),
						createStateMap(Blocks.BRICK_BLOCK.getDefaultState(), Blocks.BRICK_STAIRS.getDefaultState()),
						createStateMap(Blocks.SANDSTONE.getDefaultState(), Blocks.SANDSTONE_STAIRS.getDefaultState()),
						createStateMap(Blocks.PURPUR_BLOCK.getDefaultState(), Blocks.PURPUR_STAIRS.getDefaultState()),
						createStateMap(Blocks.RED_SANDSTONE.getDefaultState(), Blocks.RED_SANDSTONE_STAIRS.getDefaultState())
						), 
				false);
		
		MultiblockGroup wall_alt = new MultiblockGroup("wall_alt", Lists.newArrayList(BlockDefs.magicWall.getDefaultState()), true);
		wall_alt.addBlock(new BlockPos(-2, -1, 0));
		wall_alt.addBlock(new BlockPos(-2, -2, 0));
		wall_alt.addBlock(new BlockPos(-2, -3, 0));
		wall_alt.addBlock(new BlockPos(2, -1, 0));
		wall_alt.addBlock(new BlockPos(2, -2, 0));
		wall_alt.addBlock(new BlockPos(2, -3, 0));

		
		catalysts_alt.addBlock(new BlockPos(-2, 0, -1), 0);
		catalysts_alt.addBlock(new BlockPos(-2, 0, 1), 0);
		catalysts_alt.addBlock(new BlockPos(2, 0, -1), 0);
		catalysts_alt.addBlock(new BlockPos(2, 0, 1), 0);
		catalysts_alt.addBlock(new BlockPos(0, -4, 0), 0);

		out_alt.addBlock(new BlockPos(-1, 0, -1), STAIR_SOUTH);
		out_alt.addBlock(new BlockPos(0, 0, -1), STAIR_SOUTH);
		out_alt.addBlock(new BlockPos(1, 0, -1), STAIR_SOUTH);
		out_alt.addBlock(new BlockPos(-1, 0, 1), STAIR_NORTH);
		out_alt.addBlock(new BlockPos(0, 0, 1), STAIR_NORTH);
		out_alt.addBlock(new BlockPos(1, 0, 1), STAIR_NORTH);
		out_alt.addBlock(new BlockPos(-2, 0, 0), STAIR_EAST);
		out_alt.addBlock(new BlockPos(2, 0, 0), STAIR_WEST);
		out_alt.addBlock(new BlockPos(-1, -1, -1), STAIR_WEST_INVERTED);
		out_alt.addBlock(new BlockPos(-1, -1, 1), STAIR_WEST_INVERTED);
		out_alt.addBlock(new BlockPos(1, -1, -1), STAIR_EAST_INVERTED);
		out_alt.addBlock(new BlockPos(1, -1, 1), STAIR_EAST_INVERTED);
		
		out_alt.addBlock(new BlockPos(-1, 0, 0), 0);
		out_alt.addBlock(new BlockPos(1, 0, 0), 0);
		out_alt.addBlock(new BlockPos(-2, -1, 1), 0);
		out_alt.addBlock(new BlockPos(2, -1, 1), 0);
		out_alt.addBlock(new BlockPos(-2, -1, -1), 0);
		out_alt.addBlock(new BlockPos(2, -1, -1), 0);
		out_alt.addBlock(new BlockPos(-2, -2, 1), 0);
		out_alt.addBlock(new BlockPos(2, -2, 1), 0);
		out_alt.addBlock(new BlockPos(-2, -2, -1), 0);
		out_alt.addBlock(new BlockPos(2, -2, -1), 0);
		out_alt.addBlock(new BlockPos(-2, -3, 1), 0);
		out_alt.addBlock(new BlockPos(2, -3, 1), 0);
		out_alt.addBlock(new BlockPos(-2, -3, -1), 0);
		out_alt.addBlock(new BlockPos(2, -3, -1), 0);
		out_alt.addBlock(new BlockPos(-2, -4, -2), 0);		
		out_alt.addBlock(new BlockPos(-2, -4, -1), 0);		
		out_alt.addBlock(new BlockPos(-2, -4, 0), 0);		
		out_alt.addBlock(new BlockPos(-2, -4, 1), 0);		
		out_alt.addBlock(new BlockPos(-2, -4, 2), 0);		
		out_alt.addBlock(new BlockPos(-1, -4, -2), 0);		
		out_alt.addBlock(new BlockPos(-1, -4, -1), 0);		
		out_alt.addBlock(new BlockPos(-1, -4, 0), 0);		
		out_alt.addBlock(new BlockPos(-1, -4, 1), 0);		
		out_alt.addBlock(new BlockPos(-1, -4, 2), 0);		
		out_alt.addBlock(new BlockPos(0, -4, -2), 0);		
		out_alt.addBlock(new BlockPos(0, -4, -1), 0);		
		out_alt.addBlock(new BlockPos(0, -4, 1), 0);		
		out_alt.addBlock(new BlockPos(0, -4, 2), 0);		
		out_alt.addBlock(new BlockPos(1, -4, -2), 0);		
		out_alt.addBlock(new BlockPos(1, -4, -1), 0);		
		out_alt.addBlock(new BlockPos(1, -4, 0), 0);		
		out_alt.addBlock(new BlockPos(1, -4, 1), 0);
		out_alt.addBlock(new BlockPos(1, -4, 2), 0);		
		out_alt.addBlock(new BlockPos(2, -4, -2), 0);		
		out_alt.addBlock(new BlockPos(2, -4, -1), 0);		
		out_alt.addBlock(new BlockPos(2, -4, 0), 0);		
		out_alt.addBlock(new BlockPos(2, -4, 1), 0);		
		out_alt.addBlock(new BlockPos(2, -4, 2), 0);
		
		MultiblockGroup lever1_alt = new MultiblockGroup("lever1_alt", Lists.newArrayList(
				Blocks.LEVER.getDefaultState().withProperty(BlockLever.FACING, EnumOrientation.SOUTH),
				Blocks.LEVER.getDefaultState().withProperty(BlockLever.FACING, EnumOrientation.SOUTH).withProperty(BlockLever.POWERED, true)
				), false);
		MultiblockGroup lever2_alt = new MultiblockGroup("lever2_alt", Lists.newArrayList(
				Blocks.LEVER.getDefaultState().withProperty(BlockLever.FACING, EnumOrientation.NORTH),
				Blocks.LEVER.getDefaultState().withProperty(BlockLever.FACING, EnumOrientation.NORTH).withProperty(BlockLever.POWERED, true)
				), false);
		MultiblockGroup lever3_alt = new MultiblockGroup("lever3_alt", Lists.newArrayList(
				Blocks.LEVER.getDefaultState().withProperty(BlockLever.FACING, EnumOrientation.SOUTH),
				Blocks.LEVER.getDefaultState().withProperty(BlockLever.FACING, EnumOrientation.SOUTH).withProperty(BlockLever.POWERED, true)
				), false);
		MultiblockGroup lever4_alt = new MultiblockGroup("lever4_alt", Lists.newArrayList(
				Blocks.LEVER.getDefaultState().withProperty(BlockLever.FACING, EnumOrientation.NORTH),
				Blocks.LEVER.getDefaultState().withProperty(BlockLever.FACING, EnumOrientation.NORTH).withProperty(BlockLever.POWERED, true)
				), false);
		lever1_alt.addBlock(new BlockPos(2, -2, 2));
		lever2_alt.addBlock(new BlockPos(2, -2, -2));
		lever3_alt.addBlock(new BlockPos(-2, -2, 2));
		lever4_alt.addBlock(new BlockPos(-2, -2, -2));

		
		secondary.addGroup(wall_alt);
		secondary.addGroup(lever1_alt, lever2_alt, lever3_alt, lever4_alt);
		secondary.addGroup(out_alt);
		secondary.addGroup(catalysts_alt);
		
		MultiblockGroup center = new MultiblockGroup("center", Lists.newArrayList(BlockDefs.altar.getDefaultState()), true);
		center.addBlock(new BlockPos(0, 0, 0));
		primary.addGroup(center);
		secondary.addGroup(center);
		
//		for (int i = 0; i < augMatls.length; ++i)
//			primary.addAllowedBlock(augMatl_primary[i], -1, 0, 2, augMatls[i], augMetas[i]);
//
//		primary.addAllowedBlock(0, 0, -1, Blocks.stonebrick, 0);
//		primary.addAllowedBlock(0, 0, 0, BlocksCommonProxy.craftingAltar);
//		primary.addAllowedBlock(0, 0, 1, Blocks.stonebrick, 0);
//
//		for (int i = 0; i < augMatls.length; ++i)
//			primary.addAllowedBlock(augMatl_primary[i], 1, 0, -2, augMatls[i], augMetas[i]);
//
//		for (int i = 0; i < augMatls.length; ++i)
//			primary.addAllowedBlock(augMatl_primary[i], 1, 0, 2, augMatls[i], augMetas[i]);
//
//		//row 1
//		primary.addAllowedBlock(1, -1, -2, Blocks.stonebrick, 0);
//		primary.addAllowedBlock(1, -1, 2, Blocks.stonebrick, 0);
//
//		primary.addAllowedBlock(0, -1, -2, BlocksCommonProxy.magicWall, 0);
//		primary.addAllowedBlock(0, -1, 2, BlocksCommonProxy.magicWall, 0);
//
//		primary.addAllowedBlock(-1, -1, -2, Blocks.stonebrick, 0);
//		primary.addAllowedBlock(-1, -1, 2, Blocks.stonebrick, 0);
//
//		//row 2
//		primary.addAllowedBlock(1, -2, -2, Blocks.stonebrick, 0);
//		primary.addAllowedBlock(1, -2, 2, Blocks.stonebrick, 0);
//
//		primary.addAllowedBlock(0, -2, -2, BlocksCommonProxy.magicWall, 0);
//		primary.addAllowedBlock(0, -2, 2, BlocksCommonProxy.magicWall, 0);
//
//		primary.addAllowedBlock(-1, -2, -2, Blocks.stonebrick, 0);
//		primary.addAllowedBlock(-1, -2, 2, Blocks.stonebrick, 0);
//
//
//		//row 3
//		primary.addAllowedBlock(1, -3, -2, Blocks.stonebrick, 0);
//		primary.addAllowedBlock(1, -3, 2, Blocks.stonebrick, 0);
//
//		primary.addAllowedBlock(0, -3, -2, BlocksCommonProxy.magicWall, 0);
//		primary.addAllowedBlock(0, -3, 2, BlocksCommonProxy.magicWall, 0);
//
//		primary.addAllowedBlock(-1, -3, -2, Blocks.stonebrick, 0);
//		primary.addAllowedBlock(-1, -3, 2, Blocks.stonebrick, 0);
//
//		//row 4
//		for (int i = -2; i <= 2; ++i){
//			for (int j = -2; j <= 2; ++j){
//				if (i == 0 && j == 0){
//					for (int n = 0; n < augMatls.length; ++n)
//						primary.addAllowedBlock(augMatl_primary[n], i, -4, j, augMatls[n], augMetas[n]);
//				}else{
//					primary.addAllowedBlock(i, -4, j, Blocks.stonebrick, 0);
//				}
//			}
//		}
//
//		wood_primary = primary.copyGroup("main", "main_wood");
//		wood_primary.replaceAllBlocksOfType(Blocks.stonebrick, Blocks.planks);
//		wood_primary.replaceAllBlocksOfType(Blocks.stone_brick_stairs, Blocks.oak_stairs);
//
//		quartz_primary = primary.copyGroup("main", "main_quartz");
//		quartz_primary.replaceAllBlocksOfType(Blocks.stonebrick, Blocks.quartz_block);
//		quartz_primary.replaceAllBlocksOfType(Blocks.stone_brick_stairs, Blocks.quartz_stairs);
//
//		netherbrick_primary = primary.copyGroup("main", "main_netherbrick");
//		netherbrick_primary.replaceAllBlocksOfType(Blocks.stonebrick, Blocks.nether_brick);
//		netherbrick_primary.replaceAllBlocksOfType(Blocks.stone_brick_stairs, Blocks.nether_brick_stairs);
//
//		cobble_primary = primary.copyGroup("main", "main_cobble");
//		cobble_primary.replaceAllBlocksOfType(Blocks.stonebrick, Blocks.cobblestone);
//		cobble_primary.replaceAllBlocksOfType(Blocks.stone_brick_stairs, Blocks.stone_stairs);
//
//		brick_primary = primary.copyGroup("main", "main_brick");
//		brick_primary.replaceAllBlocksOfType(Blocks.stonebrick, Blocks.brick_block);
//		brick_primary.replaceAllBlocksOfType(Blocks.stone_brick_stairs, Blocks.brick_stairs);
//
//		sandstone_primary = primary.copyGroup("main", "main_sandstone");
//		sandstone_primary.replaceAllBlocksOfType(Blocks.stonebrick, Blocks.sandstone);
//		sandstone_primary.replaceAllBlocksOfType(Blocks.stone_brick_stairs, Blocks.sandstone_stairs);
//
//		witchwood_primary = primary.copyGroup("main", "main_witchwood");
//		witchwood_primary.replaceAllBlocksOfType(Blocks.stonebrick, BlocksCommonProxy.witchwoodPlanks);
//		witchwood_primary.replaceAllBlocksOfType(Blocks.stone_brick_stairs, BlocksCommonProxy.witchwoodStairs);
//
//		//Secondary
//		secondary = new MultiblockStructureDefinition("craftingAltar");
//
//		lecternGroup_secondary = new MultiblockGroup[4];
//
//		for (int i = 0; i < lecternGroup_secondary.length; ++i){
//			lecternGroup_secondary[i] = secondary.createGroup("lectern" + i, lectern_mutex);
//		}
//
//		count = 0;
//		for (int i = -2; i <= 2; i += 4){
//			secondary.addAllowedBlock(lecternGroup_secondary[count], i, -3, i, BlocksCommonProxy.blockLectern);
//			secondary.addAllowedBlock(lecternGroup_secondary[count], -i, -2, i, Blocks.lever, (count < 2) ? 4 : 3);
//			secondary.addAllowedBlock(lecternGroup_secondary[count], -i, -2, i, Blocks.lever, (count < 2) ? 12 : 11);
//			count++;
//			secondary.addAllowedBlock(lecternGroup_secondary[count], -i, -3, i, BlocksCommonProxy.blockLectern);
//			secondary.addAllowedBlock(lecternGroup_secondary[count], i, -2, i, Blocks.lever, (count < 2) ? 4 : 3);
//			secondary.addAllowedBlock(lecternGroup_secondary[count], i, -2, i, Blocks.lever, (count < 2) ? 12 : 11);
//			count++;
//		}
//
//		augMatl_secondary = new MultiblockGroup[augMatls.length];
//		for (int i = 0; i < augMatls.length; ++i)
//			augMatl_secondary[i] = secondary.createGroup("augmatl" + i, augmatl_mutex);
//
//		//row 0
//		for (int i = 0; i < augMatls.length; ++i)
//			secondary.addAllowedBlock(augMatl_secondary[i], -2, 0, -1, augMatls[i], augMetas[i]);
//
//		secondary.addAllowedBlock(-1, 0, -1, Blocks.stone_brick_stairs, 2);
//		secondary.addAllowedBlock(0, 0, -1, Blocks.stone_brick_stairs, 2);
//		secondary.addAllowedBlock(1, 0, -1, Blocks.stone_brick_stairs, 2);
//
//		for (int i = 0; i < augMatls.length; ++i)
//			secondary.addAllowedBlock(augMatl_secondary[i], 2, 0, -1, augMatls[i], augMetas[i]);
//
//		secondary.addAllowedBlock(-2, 0, 0, Blocks.stone_brick_stairs, 0);
//		secondary.addAllowedBlock(-1, 0, 0, Blocks.stonebrick, 0);
//		secondary.addAllowedBlock(0, 0, 0, BlocksCommonProxy.craftingAltar);
//		secondary.addAllowedBlock(1, 0, 0, Blocks.stonebrick, 0);
//		secondary.addAllowedBlock(2, 0, 0, Blocks.stone_brick_stairs, 1);
//
//		for (int i = 0; i < augMatls.length; ++i)
//			secondary.addAllowedBlock(augMatl_secondary[i], -2, 0, 1, augMatls[i], augMetas[i]);
//
//		secondary.addAllowedBlock(-1, 0, 1, Blocks.stone_brick_stairs, 3);
//		secondary.addAllowedBlock(0, 0, 1, Blocks.stone_brick_stairs, 3);
//		secondary.addAllowedBlock(1, 0, 1, Blocks.stone_brick_stairs, 3);
//
//		for (int i = 0; i < augMatls.length; ++i)
//			secondary.addAllowedBlock(augMatl_secondary[i], 2, 0, 1, augMatls[i], augMetas[i]);
//
//		//row 1
//		secondary.addAllowedBlock(-2, -1, 1, Blocks.stonebrick, 0);
//		secondary.addAllowedBlock(-1, -1, 1, Blocks.stone_brick_stairs, 5);
//		secondary.addAllowedBlock(1, -1, 1, Blocks.stone_brick_stairs, 4);
//		secondary.addAllowedBlock(2, -1, 1, Blocks.stonebrick, 0);
//
//		secondary.addAllowedBlock(-2, -1, 0, BlocksCommonProxy.magicWall, 0);
//		secondary.addAllowedBlock(2, -1, 0, BlocksCommonProxy.magicWall, 0);
//
//		secondary.addAllowedBlock(-2, -1, -1, Blocks.stonebrick, 0);
//		secondary.addAllowedBlock(-1, -1, -1, Blocks.stone_brick_stairs, 5);
//		secondary.addAllowedBlock(1, -1, -1, Blocks.stone_brick_stairs, 4);
//		secondary.addAllowedBlock(2, -1, -1, Blocks.stonebrick, 0);
//
//		//row 2
//		secondary.addAllowedBlock(-2, -2, 1, Blocks.stonebrick, 0);
//		secondary.addAllowedBlock(2, -2, 1, Blocks.stonebrick, 0);
//
//		secondary.addAllowedBlock(-2, -2, 0, BlocksCommonProxy.magicWall, 0);
//		secondary.addAllowedBlock(2, -2, 0, BlocksCommonProxy.magicWall, 0);
//
//		secondary.addAllowedBlock(-2, -2, -1, Blocks.stonebrick, 0);
//		secondary.addAllowedBlock(2, -2, -1, Blocks.stonebrick, 0);
//
//
//		//row 3
//		secondary.addAllowedBlock(-2, -3, 1, Blocks.stonebrick, 0);
//		secondary.addAllowedBlock(2, -3, 1, Blocks.stonebrick, 0);
//
//		secondary.addAllowedBlock(-2, -3, 0, BlocksCommonProxy.magicWall, 0);
//		secondary.addAllowedBlock(2, -3, 0, BlocksCommonProxy.magicWall, 0);
//
//		secondary.addAllowedBlock(-2, -3, -1, Blocks.stonebrick, 0);
//		secondary.addAllowedBlock(2, -3, -1, Blocks.stonebrick, 0);
//
//		//row 4
//		for (int i = -2; i <= 2; ++i){
//			for (int j = -2; j <= 2; ++j){
//				if (i == 0 && j == 0){
//					for (int n = 0; n < augMatls.length; ++n)
//						secondary.addAllowedBlock(augMatl_secondary[n], i, -4, j, augMatls[n], augMetas[n]);
//				}else{
//					secondary.addAllowedBlock(i, -4, j, Blocks.stonebrick, 0);
//				}
//			}
//		}
//
//		wood_secondary = secondary.copyGroup("main", "main_wood");
//		wood_secondary.replaceAllBlocksOfType(Blocks.stonebrick, Blocks.planks);
//		wood_secondary.replaceAllBlocksOfType(Blocks.stone_brick_stairs, Blocks.oak_stairs);
//
//		quartz_secondary = secondary.copyGroup("main", "main_quartz");
//		quartz_secondary.replaceAllBlocksOfType(Blocks.stonebrick, Blocks.quartz_block);
//		quartz_secondary.replaceAllBlocksOfType(Blocks.stone_brick_stairs, Blocks.quartz_stairs);
//
//		netherbrick_secondary = secondary.copyGroup("main", "main_netherbrick");
//		netherbrick_secondary.replaceAllBlocksOfType(Blocks.stonebrick, Blocks.nether_brick);
//		netherbrick_secondary.replaceAllBlocksOfType(Blocks.stone_brick_stairs, Blocks.nether_brick_stairs);
//
//		cobble_secondary = secondary.copyGroup("main", "main_cobble");
//		cobble_secondary.replaceAllBlocksOfType(Blocks.stonebrick, Blocks.cobblestone);
//		cobble_secondary.replaceAllBlocksOfType(Blocks.stone_brick_stairs, Blocks.stone_stairs);
//
//		brick_secondary = secondary.copyGroup("main", "main_brick");
//		brick_secondary.replaceAllBlocksOfType(Blocks.stonebrick, Blocks.brick_block);
//		brick_secondary.replaceAllBlocksOfType(Blocks.stone_brick_stairs, Blocks.brick_stairs);
//
//		sandstone_secondary = secondary.copyGroup("main", "main_sandstone");
//		sandstone_secondary.replaceAllBlocksOfType(Blocks.stonebrick, Blocks.sandstone);
//		sandstone_secondary.replaceAllBlocksOfType(Blocks.stone_brick_stairs, Blocks.sandstone_stairs);
//
//		witchwood_secondary = secondary.copyGroup("main", "main_witchwood");
//		witchwood_secondary.replaceAllBlocksOfType(Blocks.stonebrick, BlocksCommonProxy.witchwoodPlanks);
//		witchwood_secondary.replaceAllBlocksOfType(Blocks.stone_brick_stairs, BlocksCommonProxy.witchwoodStairs);
//
	}

	@Override
	public MultiblockStructureDefinition getDefinition(){
		return secondary;
	}

	public ItemStack getNextPlannedItem(){
		if (spellGuide != null){
			if ((this.allAddedItems.size()) * 3 < spellGuide.length){
				int guide_id = spellGuide[(this.allAddedItems.size()) * 3];
				int guide_qty = spellGuide[((this.allAddedItems.size()) * 3) + 1];
				int guide_meta = spellGuide[((this.allAddedItems.size()) * 3) + 2];
				ItemStack stack = new ItemStack(Item.getItemById(guide_id), guide_qty, guide_meta);
				return stack;
			}else{
				return new ItemStack(ItemDefs.spellParchment);
			}
		}
		return null;
	}

	private int getNumPartsInSpell(){
		int parts = 0;
		if (outputCombo != null)
			parts = outputCombo.length;

		if (shapeGroupGuide != null){
			for (int i = 0; i < shapeGroupGuide.length; ++i){
				if (shapeGroupGuide[i] != null)
					parts += shapeGroupGuide[i].length;
			}
		}
		return parts;
	}

	private boolean spellGuideIsWithinStructurePower(){
		return getNumPartsInSpell() <= maxEffects;
	}

	private boolean currentDefinitionIsWithinStructurePower(){
		int count = this.spellDef.size();
		for (KeyValuePair<ArrayList<ISpellPart>, NBTTagCompound> part : shapeGroups)
			count += part.key.size();

		return count <= this.maxEffects;
	}

	public boolean structureValid(){
		return this.structureValid;
	}

	public boolean isCrafting(){
		return this.isCrafting;
	}

	@Override
	public void update(){
		super.update();
		this.worldObj.markAndNotifyBlock(pos, this.worldObj.getChunkFromBlockCoords(pos), this.worldObj.getBlockState(pos), this.worldObj.getBlockState(pos), 3);
		this.ticksExisted++;

		checkStructure();
		checkForStartCondition();
		updateLecternInformation();
		if (isCrafting){
			//System.out.println("Me");
			checkForEndCondition();
			updatePowerRequestData();
			if (!worldObj.isRemote && !currentDefinitionIsWithinStructurePower() && this.ticksExisted > 100){
				worldObj.newExplosion(null, pos.getX() + 0.5, pos.getY() - 1.5, pos.getZ() + 0.5, 5, false, true);
				setCrafting(false);
				return;
			}
			if (worldObj.isRemote && checkCounter == 1){
				ArsMagica2.proxy.particleManager.RibbonFromPointToPoint(worldObj, pos.getX() + 0.5, pos.getY() - 2, pos.getZ() + 0.5, pos.getX() + 0.5, pos.getY() - 3, pos.getZ() + 0.5);
			}
			List<EntityItem> components = lookForValidItems();
			ItemStack stack = getNextPlannedItem();
			for (EntityItem item : components){
				if (item.isDead) continue;
				ItemStack entityItemStack = item.getEntityItem();
				if (stack != null && compareItemStacks(stack, entityItemStack)){
					if (!worldObj.isRemote){
						updateCurrentRecipe(item);
						item.setDead();
					}else{
						//TODO worldObj.playSound(pos.getX(), pos.getY(), pos.getZ(), "arsmagica2:misc.craftingaltar.component_added", 1.0f, 0.4f + worldObj.rand.nextFloat() * 0.6f, false);
						for (int i = 0; i < 5 * ArsMagica2.config.getGFXLevel(); ++i){
							AMParticle particle = (AMParticle)ArsMagica2.proxy.particleManager.spawn(worldObj, "radiant", item.posX, item.posY, item.posZ);
							if (particle != null){
								particle.setMaxAge(40);
								particle.AddParticleController(new ParticleMoveOnHeading(particle, worldObj.rand.nextFloat() * 360, worldObj.rand.nextFloat() * 360, 0.01f, 1, false));
								particle.AddParticleController(new ParticleFadeOut(particle, 1, false).setFadeSpeed(0.05f).setKillParticleOnFinish(true));
								particle.setParticleScale(0.02f);
								particle.setRGBColorF(worldObj.rand.nextFloat(), worldObj.rand.nextFloat(), worldObj.rand.nextFloat());
							}
						}
					}
				}
			}
		}
	}

	private void updateLecternInformation(){
		if (podiumLocation == null) return;
		TileEntityLectern lectern = (TileEntityLectern)worldObj.getTileEntity(pos.add(podiumLocation));
		if (lectern != null){
			if (lectern.hasStack()){
				ItemStack lecternStack = lectern.getStack();
				if (lecternStack.hasTagCompound()){
					spellGuide = lecternStack.getTagCompound().getIntArray("spell_combo");
					outputCombo = lecternStack.getTagCompound().getIntArray("output_combo");
					currentSpellName = lecternStack.getDisplayName();

					int numShapeGroups = lecternStack.getTagCompound().getInteger("numShapeGroups");
					shapeGroupGuide = new int[numShapeGroups][];

					for (int i = 0; i < numShapeGroups; ++i){
						shapeGroupGuide[i] = lecternStack.getTagCompound().getIntArray("shapeGroupCombo_" + i);
					}
				}

				if (isCrafting){
					if (spellGuide != null){
						lectern.setNeedsBook(false);
						lectern.setTooltipStack(getNextPlannedItem());
					}else{
						lectern.setNeedsBook(true);
					}
				}else{
					lectern.setTooltipStack(null);
				}
				if (spellGuideIsWithinStructurePower()){
					lectern.setOverpowered(false);
				}else{
					lectern.setOverpowered(true);
				}
			}else{
				if (isCrafting){
					lectern.setNeedsBook(true);
				}
				lectern.setTooltipStack(null);
			}
		}
	}

	public BlockPos getSwitchLocation(){
		return this.switchLocation;
	}

	public boolean switchIsOn(){
		if (switchLocation == null) return false;
		IBlockState block = worldObj.getBlockState(pos.add(switchLocation));
		boolean b = false;
		if (block.getBlock() == Blocks.LEVER){
			for (int i = 0; i < 6; ++i){
				b |= block.getValue(BlockLever.POWERED);
				if (b) break;
			}
		}
		return b;
	}

	public void flipSwitch(){
		if (switchLocation == null) return;
		IBlockState block = worldObj.getBlockState(pos.add(switchLocation));
		if (block == Blocks.LEVER){
			worldObj.setBlockState(pos.add(switchLocation), block.withProperty(BlockLever.POWERED, false));
		}
	}

	private void updatePowerRequestData(){
		ItemStack stack = getNextPlannedItem();
		if (stack != null && stack.getItem().equals(ItemDefs.etherium)){
			if (switchIsOn()){
				int flags = stack.getItemDamage();
				setPowerRequests();
				pickPowerType(stack);
				if (this.currentMainPowerTypes != PowerTypes.NONE && PowerNodeRegistry.For(this.worldObj).checkPower(this, this.currentMainPowerTypes, 100)){
					currentConsumedPower += PowerNodeRegistry.For(worldObj).consumePower(this, this.currentMainPowerTypes, Math.min(100, stack.stackSize - currentConsumedPower));
				}
				if (currentConsumedPower >= stack.stackSize){
					PowerNodeRegistry.For(this.worldObj).setPower(this, this.currentMainPowerTypes, 0);
					if (!worldObj.isRemote)
						addItemToRecipe(new ItemStack(ItemDefs.essence, stack.stackSize, flags));
					currentConsumedPower = 0;
					currentMainPowerTypes = PowerTypes.NONE;
					setNoPowerRequests();
					flipSwitch();
				}
			}else{
				setNoPowerRequests();
			}
		}else{
			setNoPowerRequests();
		}
	}

	@Override
	protected void setNoPowerRequests(){
		currentConsumedPower = 0;
		currentMainPowerTypes = PowerTypes.NONE;

		super.setNoPowerRequests();
	}

	private void pickPowerType(ItemStack stack){
		if (this.currentMainPowerTypes != PowerTypes.NONE)
			return;
		int flags = stack.getItemDamage();
		PowerTypes highestValid = PowerTypes.NONE;
		float amt = 0;
		for (PowerTypes type : PowerTypes.all()){
			float tmpAmt = PowerNodeRegistry.For(worldObj).getPower(this, type);
			if (tmpAmt > amt)
				highestValid = type;
		}

		this.currentMainPowerTypes = highestValid;
	}

	private void updateCurrentRecipe(EntityItem item){
		ItemStack stack = item.getEntityItem();
		addItemToRecipe(stack);
	}

	private void addItemToRecipe(ItemStack stack){
		allAddedItems.add(stack);
		currentAddedItems.add(stack);

		if (!worldObj.isRemote){
			AMDataWriter writer = new AMDataWriter();
			writer.add(pos.getX());
			writer.add(pos.getY());
			writer.add(pos.getZ());
			writer.add(COMPONENT_ADDED);
			writer.add(stack);
			AMNetHandler.INSTANCE.sendPacketToAllClientsNear(worldObj.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 32, AMPacketIDs.CRAFTING_ALTAR_DATA, writer.generate());
		}

		if (matchCurrentRecipe()){
			currentAddedItems.clear();
			return;
		}
	}

	private boolean matchCurrentRecipe(){
		ISpellPart part = SpellRegistry.getPartByRecipe(currentAddedItems);
		if (part == null) return false;

		KeyValuePair<ArrayList<ISpellPart>, NBTTagCompound> currentShapeGroupList = getShapeGroupToAddTo();

		if (part instanceof Summon)
			handleSummonShape();
		if (part instanceof Binding)
			handleBindingShape();


		//if this is null, then we have already completed all of the shape groups that the book identifies
		//we're now creating the body of the spell
		if (currentShapeGroupList == null){
			part.encodeBasicData(savedData, currentAddedItems.toArray());
			spellDef.add(part);
		}else{
			part.encodeBasicData(currentShapeGroupList.value, currentAddedItems.toArray());
			currentShapeGroupList.key.add(part);
		}
		return true;
	}

	private KeyValuePair<ArrayList<ISpellPart>, NBTTagCompound> getShapeGroupToAddTo(){
		for (int i = 0; i < shapeGroupGuide.length; ++i){
			int guideLength = shapeGroupGuide[i].length;
			int addedLength = shapeGroups.get(i).key.size();
			if (addedLength < guideLength)
				return shapeGroups.get(i);
		}

		return null;
	}

	private void handleSummonShape(){
		if (currentAddedItems.size() > 2)
			addedPhylactery = currentAddedItems.get(currentAddedItems.size() - 2);
	}

	private void handleBindingShape(){
		if (currentAddedItems.size() == 7)
			addedBindingCatalyst = currentAddedItems.get(currentAddedItems.size() - 1);
	}

	private List<EntityItem> lookForValidItems(){
		if (!isCrafting) return new ArrayList<EntityItem>();
		double radius = worldObj.isRemote ? 2.1 : 2;
		List<EntityItem> items = this.worldObj.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos.getX() - radius, pos.getY() - 3, pos.getZ() - radius, pos.getX() + radius, pos.getY(), pos.getZ() + radius));
		return items;
	}

	private void checkStructure(){
		setStructureValid(primary.matches(worldObj, pos) || secondary.matches(worldObj, pos));
	}

	private void checkForStartCondition(){
		if (this.worldObj.isRemote || !structureValid || this.isCrafting) return;

		List<Entity> items = this.worldObj.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos.getX() - 2, pos.getY() - 3, pos.getZ() - 2, pos.getX() + 2, pos.getY(), pos.getZ() + 2));
		if (items.size() == 1){
			EntityItem item = (EntityItem)items.get(0);
			if (item != null && !item.isDead && item.getEntityItem().getItem() == ItemDefs.blankRune){
				item.setDead();
				setCrafting(true);
			}
		}
	}

	private void checkForEndCondition(){
		if (!structureValid || !this.isCrafting || worldObj == null) return;

		double radius = worldObj.isRemote ? 2.2 : 2;

		List<Entity> items = this.worldObj.getEntitiesWithinAABB(EntityItem.class, new AxisAlignedBB(pos.getX() - radius, pos.getY() - 3, pos.getZ() - radius, pos.getX() + radius, pos.getY(), pos.getZ() + radius));
		if (items.size() == 1){
			EntityItem item = (EntityItem)items.get(0);
			if (item != null && !item.isDead && item.getEntityItem() != null && item.getEntityItem().getItem() == ItemDefs.spellParchment){
				if (!worldObj.isRemote){
					item.setDead();
					setCrafting(false);
					EntityItem craftedItem = new EntityItem(worldObj);
					craftedItem.setPosition(pos.getX() + 0.5, pos.getY() - 1.5, pos.getZ() + 0.5);

					ItemStack craftStack = SpellUtils.createSpellStack(shapeGroups, spellDef, savedData);
					if (!craftStack.hasTagCompound())
						craftStack.setTagCompound(new NBTTagCompound());
					AddSpecialMetadata(craftStack);

					craftStack.getTagCompound().setString("suggestedName", currentSpellName != null ? currentSpellName : "");
					craftedItem.setEntityItemStack(craftStack);
					worldObj.spawnEntityInWorld(craftedItem);

					allAddedItems.clear();
					currentAddedItems.clear();
				}else{
					//worldObj.playSound(pos.getX(), pos.getY(), pos.getZ(), "arsmagica2:misc.craftingaltar.create_spell", 1.0f, 1.0f, true);
				}
			}
		}
	}

	private void AddSpecialMetadata(ItemStack craftStack){
//		if (addedPhylactery != null){
//			Summon summon = (Summon)SkillManager.instance.getSkill("Summon");
//			summon.setSummonType(craftStack, addedPhylactery);
//		}
//		if (addedBindingCatalyst != null){
//			Binding binding = (Binding)SkillManager.instance.getSkill("Binding");
//			binding.setBindingType(craftStack, addedBindingCatalyst);
//		}


	}

	private void setCrafting(boolean crafting){
		this.isCrafting = crafting;
		if (!worldObj.isRemote){
			AMDataWriter writer = new AMDataWriter();
			writer.add(pos.getX());
			writer.add(pos.getY());
			writer.add(pos.getZ());
			writer.add(CRAFTING_CHANGED);
			writer.add(crafting);
			AMNetHandler.INSTANCE.sendPacketToAllClientsNear(worldObj.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 32, AMPacketIDs.CRAFTING_ALTAR_DATA, writer.generate());
		}
		if (crafting){
			allAddedItems.clear();
			currentAddedItems.clear();

			spellDef.clear();
//			for (KeyValuePair<ArrayList<ISpellPart>, NBTTagCompound> groups : shapeGroups)
//				groups.clear();

			//find otherworld auras
			IPowerNode[] nodes = PowerNodeRegistry.For(worldObj).getAllNearbyNodes(worldObj, new Vec3d(pos), PowerTypes.DARK);
//			for (IPowerNode node : nodes){
//				if (node instanceof TileEntityOtherworldAura){
//					((TileEntityOtherworldAura)node).setActive(true, this);
//					break;
//				}
//			}
		}
	}

	private void setStructureValid(boolean valid){
		if (this.structureValid == valid) return;
		this.structureValid = valid;
		worldObj.markAndNotifyBlock(pos, worldObj.getChunkFromBlockCoords(pos), worldObj.getBlockState(pos), worldObj.getBlockState(pos), 3);
	}

	public void deactivate(){
		if (!worldObj.isRemote){
			this.setCrafting(false);
			for (ItemStack stack : allAddedItems){
				if (stack.getItem() == ItemDefs.etherium)
					continue;
				EntityItem eItem = new EntityItem(worldObj);
				eItem.setPosition(pos.getX(), pos.getY() - 1, pos.getZ());
				eItem.setEntityItemStack(stack);
				worldObj.spawnEntityInWorld(eItem);
			}
			allAddedItems.clear();
		}
	}

	@Override
	public boolean canProvidePower(PowerTypes type){
		return false;
	}

	private boolean compareItemStacks(ItemStack target, ItemStack input){
		if (target.getItem() == Items.POTIONITEM && input.getItem() == Items.POTIONITEM){
			return (target.getItemDamage() & 0xF) == (input.getItemDamage() & 0xF);
		}
		return target.getItem() == input.getItem() && (target.getItemDamage() == input.getItemDamage() || target.getItemDamage() == Short.MAX_VALUE) && target.stackSize == input.stackSize;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbttagcompound){
		super.writeToNBT(nbttagcompound);

		NBTTagCompound altarCompound = new NBTTagCompound();
		altarCompound.setBoolean("isCrafting", this.isCrafting);
		altarCompound.setInteger("currentKey", this.currentKey);
		altarCompound.setString("currentSpellName", currentSpellName);

		NBTTagList allAddedItemsList = new NBTTagList();
		for (ItemStack stack : allAddedItems){
			NBTTagCompound addedItem = new NBTTagCompound();
			stack.writeToNBT(addedItem);
			allAddedItemsList.appendTag(addedItem);
		}

		altarCompound.setTag("allAddedItems", allAddedItemsList);

		NBTTagList currentAddedItemsList = new NBTTagList();
		for (ItemStack stack : currentAddedItems){
			NBTTagCompound addedItem = new NBTTagCompound();
			stack.writeToNBT(addedItem);
			currentAddedItemsList.appendTag(addedItem);
		}

		altarCompound.setTag("currentAddedItems", currentAddedItemsList);

		if (addedPhylactery != null){
			NBTTagCompound phylactery = new NBTTagCompound();
			addedPhylactery.writeToNBT(phylactery);
			altarCompound.setTag("phylactery", phylactery);
		}

		if (addedBindingCatalyst != null){
			NBTTagCompound catalyst = new NBTTagCompound();
			addedBindingCatalyst.writeToNBT(catalyst);
			altarCompound.setTag("catalyst", catalyst);
		}

		NBTTagList shapeGroupData = new NBTTagList();
//		for (KeyValuePair<ArrayList<ISpellPart>, NBTTagCompound> list : shapeGroups){
//			shapeGroupData.appendTag(ISpellPartListToNBT(list.key));
//		}
//		altarCompound.setTag("shapeGroups", shapeGroupData);
//
//		NBTTagCompound spellDefSave = ISpellPartListToNBT(this.spellDef);
//		altarCompound.setTag("spellDef", spellDefSave);

		nbttagcompound.setTag("altarData", altarCompound);
		return nbttagcompound;
	}

//	private NBTTagCompound ISpellPartListToNBT(ArrayList<ISpellPart> spellDef2){
//		NBTTagCompound shapeGroupData = new NBTTagCompound();
//		int[] ids = new int[spellDef2.size()];
//		byte[][] meta = new byte[spellDef2.size()][];
//		for (int d = 0; d < spellDef2.size(); ++d){
//			ids[d] = SkillManager.instance.getShiftedPartID(spellDef2.get(d).getKey());
//			meta[d] = spellDef2.get(d).getValue();
//		}
//		shapeGroupData.setIntArray("group_ids", ids);
//		for (int i = 0; i < meta.length; ++i){
//			shapeGroupData.setByteArray("meta_" + i, meta[i]);
//		}
//		return shapeGroupData;
//	}

//	private ArrayList<KeyValuePair<ISpellPart, byte[]>> NBTToISpellPartList(NBTTagCompound compound){
//		int[] ids = compound.getIntArray("group_ids");
//		ArrayList<KeyValuePair<ISpellPart, byte[]>> list = new ArrayList<KeyValuePair<ISpellPart, byte[]>>();
//		for (int i = 0; i < ids.length; ++i){
//			int partID = ids[i];
//			ISkillTreeEntry part = SkillManager.instance.getSkill(i);
//			byte[] partMeta = compound.getByteArray("meta_" + i);
//			if (part instanceof ISpellPart){
//				list.add(new KeyValuePair<ISpellPart, byte[]>((ISpellPart)part, partMeta));
//			}
//		}
//		return list;
//	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound){
		super.readFromNBT(nbttagcompound);

		if (!nbttagcompound.hasKey("altarData"))
			return;

		NBTTagCompound altarCompound = nbttagcompound.getCompoundTag("altarData");

		NBTTagList allAddedItems = altarCompound.getTagList("allAddedItems", Constants.NBT.TAG_COMPOUND);
		NBTTagList currentAddedItems = altarCompound.getTagList("currentAddedItems", Constants.NBT.TAG_COMPOUND);

		this.isCrafting = altarCompound.getBoolean("isCrafting");
		this.currentKey = altarCompound.getInteger("currentKey");
		this.currentSpellName = altarCompound.getString("currentSpellName");

		if (altarCompound.hasKey("phylactery")){
			NBTTagCompound phylactery = altarCompound.getCompoundTag("phylactery");
			if (phylactery != null)
				this.addedPhylactery = ItemStack.loadItemStackFromNBT(phylactery);
		}

		if (altarCompound.hasKey("catalyst")){
			NBTTagCompound catalyst = altarCompound.getCompoundTag("catalyst");
			if (catalyst != null)
				this.addedBindingCatalyst = ItemStack.loadItemStackFromNBT(catalyst);
		}

		this.allAddedItems.clear();
		for (int i = 0; i < allAddedItems.tagCount(); ++i){
			NBTTagCompound addedItem = (NBTTagCompound)allAddedItems.getCompoundTagAt(i);
			if (addedItem == null)
				continue;
			ItemStack stack = ItemStack.loadItemStackFromNBT(addedItem);
			if (stack == null)
				continue;
			this.allAddedItems.add(stack);
		}

		this.currentAddedItems.clear();
		for (int i = 0; i < currentAddedItems.tagCount(); ++i){
			NBTTagCompound addedItem = (NBTTagCompound)currentAddedItems.getCompoundTagAt(i);
			if (addedItem == null)
				continue;
			ItemStack stack = ItemStack.loadItemStackFromNBT(addedItem);
			if (stack == null)
				continue;
			this.currentAddedItems.add(stack);
		}

		this.spellDef.clear();
//		for (ArrayList<KeyValuePair<ISpellPart, byte[]>> groups : shapeGroups)
//			groups.clear();
//
//		NBTTagCompound currentSpellDef = altarCompound.getCompoundTag("spellDef");
//		this.spellDef.addAll(NBTToISpellPartList(currentSpellDef));
//
//		NBTTagList currentShapeGroups = altarCompound.getTagList("shapeGroups", Constants.NBT.TAG_COMPOUND);
//
//		for (int i = 0; i < currentShapeGroups.tagCount(); ++i){
//			NBTTagCompound compound = (NBTTagCompound)currentShapeGroups.getCompoundTagAt(i);
//			shapeGroups.get(i).addAll(NBTToISpellPartList(compound));
//		}
	}

	@Override
	public int getChargeRate(){
		return 250;
	}

	@Override
	public boolean canRelayPower(PowerTypes type){
		return false;
	}


	public void HandleUpdatePacket(byte[] remainingBytes){
		AMDataReader rdr = new AMDataReader(remainingBytes, false);
		byte subID = rdr.getByte();
		switch (subID){
		case FULL_UPDATE:
			this.isCrafting = rdr.getBoolean();
			this.currentKey = rdr.getInt();

			this.allAddedItems.clear();
			this.currentAddedItems.clear();

			int itemCount = rdr.getInt();
			for (int i = 0; i < itemCount; ++i)
				this.allAddedItems.add(rdr.getItemStack());
			break;
		case CRAFTING_CHANGED:
			this.setCrafting(rdr.getBoolean());
			break;
		case COMPONENT_ADDED:
			this.allAddedItems.add(rdr.getItemStack());
			break;
		}
	}
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket(){
		NBTTagCompound compound = new NBTTagCompound();
		this.writeToNBT(compound);
		SPacketUpdateTileEntity packet = new SPacketUpdateTileEntity(pos, this.getBlockMetadata(), compound);
		return packet;
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt){
		this.readFromNBT(pkt.getNbtCompound());
		this.worldObj.markAndNotifyBlock(pos, this.worldObj.getChunkFromBlockCoords(pos), this.worldObj.getBlockState(pos), this.worldObj.getBlockState(pos), 3);
	}

}
