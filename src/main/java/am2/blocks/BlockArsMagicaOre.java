package am2.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.MathHelper;

public class BlockArsMagicaOre extends BlockAM {
	
	public static final PropertyEnum<EnumOreType> ORE_TYPE = PropertyEnum.create("ore_type", EnumOreType.class);
	
	public BlockArsMagicaOre() {
		super(Material.ROCK);
		setDefaultState(blockState.getBaseState().withProperty(ORE_TYPE, EnumOreType.VINTEUM));
	}
	
	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[]{BlockArsMagicaOre.ORE_TYPE});
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(ORE_TYPE).ordinal();
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(ORE_TYPE, EnumOreType.values()[MathHelper.clamp_int(meta, 0, EnumOreType.values().length - 1)]);
	}
	
	public static enum EnumOreType implements IStringSerializable{
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
