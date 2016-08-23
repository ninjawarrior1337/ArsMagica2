package am2.blocks;

import java.util.List;

import am2.blocks.tileentity.TileEntityIllusionBlock;
import am2.defs.PotionEffectsDefs;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockIllusionBlock extends BlockAMContainer{

	public static final PropertyEnum<EnumIllusionType> ILLUSION_TYPE = PropertyEnum.create("illusion_type", EnumIllusionType.class);
	
	public BlockIllusionBlock() {
		super(Material.WOOD);
		setDefaultState(blockState.getBaseState().withProperty(ILLUSION_TYPE, EnumIllusionType.DEFAULT));
		setLightOpacity(255);

		this.setHardness(3.0f);
		this.setResistance(3.0f);

	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, ILLUSION_TYPE);
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(ILLUSION_TYPE).ordinal();
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(ILLUSION_TYPE, EnumIllusionType.values()[meta]);
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityIllusionBlock();
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state){
		return false;
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state){
		return EnumBlockRenderType.INVISIBLE;
	}

	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, Entity entityIn) {
		if (entityIn instanceof EntityLivingBase && ((EntityLivingBase)entityIn).isPotionActive(PotionEffectsDefs.trueSight))
			return;
		addCollisionBoxToList(pos, entityBox, collidingBoxes, state.getCollisionBoundingBox(worldIn, pos));
	}
	
	public static EnumIllusionType getIllusionType(IBlockState state) {
		return state.getValue(ILLUSION_TYPE);
	}
	
	public static enum EnumIllusionType implements IStringSerializable {
		DEFAULT(true, false),
		NON_COLLIDE(false, true);
		
		private final boolean isSolid;
		private final boolean canBeRevealed;
		
		private EnumIllusionType(boolean isSolid, boolean canBeRevealed) {
			this.isSolid = isSolid;
			this.canBeRevealed = canBeRevealed;
		}
		
		public boolean isSolid() {
			return isSolid;
		}
		
		public boolean canBeRevealed() {
			return canBeRevealed;
		}

		@Override
		public String getName() {
			return name().toLowerCase();
		}
		
	}
	
}
