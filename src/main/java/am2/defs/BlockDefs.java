package am2.defs;

import am2.blocks.BlockAM;
import am2.blocks.BlockAMFlower;
import am2.blocks.BlockArsMagicaBlock;
import am2.blocks.BlockArsMagicaOre;
import am2.blocks.BlockCandle;
import am2.blocks.BlockCraftingAltar;
import am2.blocks.BlockCrystalMarker;
import am2.blocks.BlockDesertNova;
import am2.blocks.BlockEssenceGenerator;
import am2.blocks.BlockFrost;
import am2.blocks.BlockInscriptionTable;
import am2.blocks.BlockInvisibleUtility;
import am2.blocks.BlockLectern;
import am2.blocks.BlockLightDecay;
import am2.blocks.BlockMageLight;
import am2.blocks.BlockMagicWall;
import am2.blocks.BlockManaBattery;
import am2.blocks.BlockOcculus;
import am2.blocks.BlockTarmaRoot;
import am2.blocks.BlockWakebloom;
import am2.blocks.BlockWizardsChalk;
import am2.items.rendering.IgnoreMetadataRenderer;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistry;

public class BlockDefs {
	
	public static final Block manaBattery = new BlockManaBattery().registerAndName(new ResourceLocation("arsmagica2:manaBattery"));
	public static final BlockFrost frost = new BlockFrost().registerAndName(new ResourceLocation("arsmagica2:frost"));
	public static final BlockOcculus occulus = new BlockOcculus().registerAndName(new ResourceLocation("arsmagica2:occulus"));
	public static final BlockAM magicWall = new BlockMagicWall().registerAndName(new ResourceLocation("arsmagica2:magic_wall"));
	public static final BlockAM invisibleLight = new BlockLightDecay().registerAndName(new ResourceLocation("arsmagica2:invisible_light"));
	public static final BlockAM invisibleUtility = new BlockInvisibleUtility().registerAndName(new ResourceLocation("arsmagica2:invisibleUtility"));
	public static final BlockAM ores = new BlockArsMagicaOre().registerAndName(new ResourceLocation("arsmagica2:ore"));
	public static final BlockAM blocks = new BlockArsMagicaBlock().registerAndName(new ResourceLocation("arsmagica2:block"));
	public static final BlockAM blockMageTorch = new BlockMageLight().registerAndName(new ResourceLocation("arsmagica2:blockMageLight"));
	public static final BlockAMFlower desertNova = new BlockDesertNova().registerAndName(new ResourceLocation("arsmagica2:desertNova"));
	public static final BlockAMFlower cerublossom = new BlockAMFlower().registerAndName(new ResourceLocation("arsmagica2:cerublossom"));
	public static final BlockAMFlower wakebloom = new BlockWakebloom().registerAndName(new ResourceLocation("arsmagica2:wakebloom"));
	public static final BlockAMFlower aum = new BlockAMFlower().registerAndName(new ResourceLocation("arsmagica2:aum"));
	public static final BlockAMFlower tarmaRoot = new BlockTarmaRoot().registerAndName(new ResourceLocation("arsmagica2:tarmaRoot"));
	public static final BlockCraftingAltar altar = new BlockCraftingAltar().registerAndName(new ResourceLocation("arsmagica2:craftingAltar"));
	public static final Block wizardChalk = new BlockWizardsChalk().registerAndName(new ResourceLocation("arsmagica2:wizardChalkBlock"));
	public static final Block obelisk = new BlockEssenceGenerator(BlockEssenceGenerator.NEXUS_STANDARD).registerAndName(new ResourceLocation("arsmagica2:obelisk"));
	public static final Block blackAurem = new BlockEssenceGenerator(BlockEssenceGenerator.NEXUS_DARK).registerAndName(new ResourceLocation("arsmagica2:blackAurem"));
	public static final Block celestialPrism = new BlockEssenceGenerator(BlockEssenceGenerator.NEXUS_LIGHT).registerAndName(new ResourceLocation("arsmagica2:celestialPrism"));
	public static final Block crystalMarker = new BlockCrystalMarker().registerAndName(new ResourceLocation("arsmagica2:crystalMarker"));
	public static final Block wardingCandle = new BlockCandle().registerAndName(new ResourceLocation("arsmagica2:wardingCandle"));
	public static final Block lectern = new BlockLectern().registerAndName(new ResourceLocation("arsmagica2:lectern"));
	public static final Block inscriptionTable = new BlockInscriptionTable().registerAndName(new ResourceLocation("arsmagica2:inscriptionTable"));
	public static final Block witchwoodLog = null;
	public static final Block essenceConduit = null;
	
	public static void init () {
		IForgeRegistry<Item> items = GameRegistry.findRegistry(Item.class);
		RenderItem renderer = Minecraft.getMinecraft().getRenderItem();
		//Utility Blocks
		registerTexture(frost);
		registerTexture(invisibleLight);
		registerTexture(invisibleUtility);
		registerTexture(blockMageTorch);
		
		//Building Blocks
		registerTexture(magicWall);
		
		//Power Blocks
		registerTexture(obelisk);
		registerTexture(celestialPrism);
		registerTexture(blackAurem);
		registerTexture(manaBattery);
		
		//Flickers
		registerTexture(crystalMarker);
		
		//Ritual Blocks
		registerTexture(wardingCandle);
		registerTexture(wizardChalk);
		
		//Spell Blocks
		registerTexture(occulus);
		registerTexture(lectern);
		registerTexture(altar);
		registerTexture(inscriptionTable);
		
		//Flowers
		registerTexture(aum);
		registerTexture(cerublossom);
		registerTexture(wakebloom);
		registerTexture(tarmaRoot);
		registerTexture(desertNova);
		
		Item ore = items.getValue(new ResourceLocation("arsmagica2:ore"));
		Item block = items.getValue(new ResourceLocation("arsmagica2:block"));
		for (int i = 0; i < BlockArsMagicaOre.EnumOreType.values().length; i++) {
			ModelResourceLocation blockLoc = new ModelResourceLocation("arsmagica2:block_" + BlockArsMagicaOre.EnumOreType.values()[i].getName(), "inventory");
			ModelResourceLocation oreLoc = new ModelResourceLocation("arsmagica2:ore_" + BlockArsMagicaOre.EnumOreType.values()[i].getName(), "inventory");
			ModelBakery.registerItemVariants(ore, oreLoc);
			ModelBakery.registerItemVariants(block, blockLoc);
			renderer.getItemModelMesher().register(ore, i, oreLoc);
			renderer.getItemModelMesher().register(block, i, blockLoc);
		}
	}
	
	private static void registerTexture(Block block) {
		ResourceLocation loc = block.getRegistryName();
		Item item = GameRegistry.findRegistry(Item.class).getValue(loc);
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, new IgnoreMetadataRenderer(new ModelResourceLocation(loc, "inventory")));
	}
}
