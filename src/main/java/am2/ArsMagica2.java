package am2;

import am2.api.ArsMagicaAPI;
import am2.api.affinity.Affinity;
import am2.config.AMConfig;
import am2.defs.AMRecipes;
import am2.defs.BindingsDefs;
import am2.defs.BlockDefs;
import am2.defs.ItemDefs;
import am2.extensions.DataDefinitions;
import am2.network.SeventhSanctum;
import am2.packet.MessageBoolean;
import am2.packet.MessageCapabilities;
import am2.proxy.CommonProxy;
import am2.spell.AbstractSpellPart;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.OreDictionary;


@Mod(modid=ArsMagica2.MODID, version=ArsMagica2.VERSION)
public class ArsMagica2 {
	
	public static final String MODID = "arsmagica2";
	public static final String VERSION = "$version";
	public static SimpleNetworkWrapper network;
	
	@SidedProxy(clientSide="am2.proxy.ClientProxy", serverSide="am2.proxy.CommonProxy", modId=MODID)
	public static CommonProxy proxy;
	
	@Instance(MODID)
	public static ArsMagica2 instance = new ArsMagica2();
	public static AMConfig config;
	
	static {
		
		new DataDefinitions();
		new ArsMagicaAPI();
		Affinity.registerAffinities();
		if (!FluidRegistry.isUniversalBucketEnabled())
			FluidRegistry.enableUniversalBucket();
		ForgeModContainer.replaceVanillaBucketModel = true;
	}
	
	@EventHandler
	public void preInit (FMLPreInitializationEvent e) {
		config = new AMConfig(e.getSuggestedConfigurationFile());
		proxy.preInit();
		ForgeChunkManager.setForcedChunkLoadingCallback(instance, AMChunkLoader.INSTANCE);
		network = NetworkRegistry.INSTANCE.newSimpleChannel("AM2");
		network.registerMessage(MessageBoolean.IceBridgeHandler.class, MessageBoolean.class, 1, Side.SERVER);
		network.registerMessage(MessageCapabilities.class, MessageCapabilities.class, 3, Side.SERVER);
		ClientRegistry.registerKeyBinding(BindingsDefs.iceBridge);
		ClientRegistry.registerKeyBinding(BindingsDefs.ENDER_TP);
		NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy);
		OreDictionary.registerOre("fence", Blocks.ACACIA_FENCE);
		OreDictionary.registerOre("fence", Blocks.OAK_FENCE);
		OreDictionary.registerOre("fence", Blocks.DARK_OAK_FENCE);
		OreDictionary.registerOre("fence", Blocks.SPRUCE_FENCE);
		OreDictionary.registerOre("fence", Blocks.BIRCH_FENCE);
		OreDictionary.registerOre("fence", Blocks.JUNGLE_FENCE);
		SeventhSanctum.instance.init();
	}
	
	@EventHandler
	public void init (FMLInitializationEvent e) {
		if (e.getSide().equals(Side.CLIENT)) {
			BlockDefs.init();
			ItemDefs.init();
		}
	}
	
	@EventHandler
	public void postInit (FMLPostInitializationEvent e) {
		AMRecipes.addRecipes();
		for (AbstractSpellPart part : ArsMagicaAPI.getSpellRegistry().getValues()) {
			if (ArsMagicaAPI.getSkillRegistry().getValue(part.getRegistryName()) == null)
				throw new IllegalStateException("Spell Part " + part.getRegistryName() + " is missing a skill, this would cause severe problems");
		}
	}

	public String getVersion() {
		return VERSION;
	}
	
}
