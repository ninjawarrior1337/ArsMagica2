package am2.blocks;

import net.minecraft.block.BlockBush;
import net.minecraft.block.SoundType;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockAMFlower extends BlockBush{

	public BlockAMFlower(){
		super();
		setSoundType(SoundType.PLANT);
	}

	public BlockAMFlower registerAndName(ResourceLocation loc){
		setUnlocalizedName(loc.getResourcePath());
		GameRegistry.register(this, loc);
		GameRegistry.register(new ItemBlock(this), loc);
		return this;
	}

	public boolean canGrowOn(World worldIn, BlockPos pos) {
		return canBlockStay(worldIn, pos, worldIn.getBlockState(pos));
	}
	
}
