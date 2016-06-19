package am2.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.MathHelper;

public class BlockArsMagicaBlock extends BlockAM {
	
	public static final PropertyEnum<EnumBlockType> BLOCK_TYPE = PropertyEnum.create("block_type", EnumBlockType.class);
	
	
	public BlockArsMagicaBlock() {
		super(Material.ROCK);
		setDefaultState(blockState.getBaseState().withProperty(BLOCK_TYPE, EnumBlockType.VINTEUM));
	}

	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, BlockArsMagicaBlock.BLOCK_TYPE);
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(BLOCK_TYPE).ordinal();
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(BLOCK_TYPE, EnumBlockType.values()[MathHelper.clamp_int(meta, 0, EnumBlockType.values().length - 1)]);
	}
	
	public static enum EnumBlockType implements IStringSerializable{
		VINTEUM,
		CHIMERITE,
		BLUETOPAZ,
		MOONSTONE,
		SUNSTONE;

		@Override
		public String getName() {
			return name().toLowerCase();
		}		
	}

}
