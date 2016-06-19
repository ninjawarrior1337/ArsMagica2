package am2.defs;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import am2.blocks.BlockArsMagicaBlock;
import am2.blocks.BlockArsMagicaOre;
import am2.blocks.BlockFrost;
import am2.blocks.BlockLightDecay;
import am2.blocks.BlockMagicWall;
import am2.blocks.BlockOcculus;

public class BlockDefs {
	
	//public static final Block manaBattery = new Block(Material.rock);
	public static final Block frost = new BlockFrost().registerAndName(new ResourceLocation("arsmagica2:frost"));
	public static final Block occulus = new BlockOcculus().registerAndName(new ResourceLocation("arsmagica2:occulus"));
	public static final Block magicWall = new BlockMagicWall().registerAndName(new ResourceLocation("arsmagica2:magic_wall"));
	public static final Block invisibleLight = new BlockLightDecay().registerAndName(new ResourceLocation("arsmagica2:invisible_light"));
	public static final Block ores = new BlockArsMagicaOre().registerAndName(new ResourceLocation("arsmagica2:ore"));
	public static final Block blocks = new BlockArsMagicaBlock().registerAndName(new ResourceLocation("arsmagica2:block"));
	
	public static void init () {
		Item occulus = GameRegistry.findRegistry(Item.class).getValue(new ResourceLocation("arsmagica2", "occulus"));
		RenderItem renderer = Minecraft.getMinecraft().getRenderItem();
		renderer.getItemModelMesher().register(occulus, 0, new ModelResourceLocation("arsmagica2:occulus", "inventory"));
		Item wall = GameRegistry.findRegistry(Item.class).getValue(new ResourceLocation("arsmagica2", "magic_wall"));
		renderer.getItemModelMesher().register(wall, 0, new ModelResourceLocation("arsmagica2:magic_wall", "inventory"));
	}
	
}
