package am2.blocks;

import am2.defs.CreativeTabsDefs;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockAM extends Block {

	public BlockAM(Material materialIn) {
		super(materialIn);
		setCreativeTab(CreativeTabsDefs.tabAM2Blocks);
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
