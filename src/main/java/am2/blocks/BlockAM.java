package am2.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockAM extends Block {

	public BlockAM(Material materialIn) {
		super(materialIn);
		// TODO Auto-generated constructor stub
	}

	public BlockAM(Material blockMaterialIn, MapColor blockMapColorIn) {
		super(blockMaterialIn, blockMapColorIn);
	}
	
	public BlockAM registerAndName(ResourceLocation rl) {
		this.setUnlocalizedName(rl.getResourcePath());
		GameRegistry.register(this, rl);
		GameRegistry.register(new ItemBlock(this), rl);
		return this;
	}
}
