package am2;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import am2.api.extensions.IAffinityData;
import am2.api.extensions.IArcaneCompendium;
import am2.api.extensions.IEntityExtension;
import am2.api.extensions.IRiftStorage;
import am2.api.extensions.ISkillData;
import am2.blocks.tileentity.TileEntityOcculus;
import am2.config.AMConfig;
import am2.defs.BindingsDefs;
import am2.defs.BlockDefs;
import am2.defs.CreativeTabsDefs;
import am2.defs.ItemDefs;
import am2.defs.LoreDefs;
import am2.defs.PotionEffectsDefs;
import am2.defs.SkillDefs;
import am2.defs.SpellDefs;
import am2.extensions.DataDefinitions;
import am2.handler.EntityHandler;
import am2.handler.PotionEffectHandler;
import am2.packet.MessageBoolean;
import am2.packet.MessageCapabilities;
import am2.proxy.CommonProxy;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
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
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;


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
	public static final Logger LOGGER = LogManager.getLogger("ArsMagica2");
	
	static {
		new DataDefinitions();
	}
	
	@EventHandler
	public void preInit (FMLPreInitializationEvent e) {
		proxy.preInit();
		MinecraftForge.EVENT_BUS.register(new EntityHandler());
		MinecraftForge.EVENT_BUS.register(new PotionEffectHandler());
		network = NetworkRegistry.INSTANCE.newSimpleChannel("AM2");
		network.registerMessage(MessageBoolean.IceBridgeHandler.class, MessageBoolean.class, 1, Side.SERVER);
		network.registerMessage(MessageCapabilities.class, MessageCapabilities.class, 3, Side.SERVER);
		CapabilityManager.INSTANCE.register(IEntityExtension.class, new IEntityExtension.Storage(), new IEntityExtension.Factory());
		CapabilityManager.INSTANCE.register(IAffinityData.class, new IAffinityData.Storage(), new IAffinityData.Factory());
		CapabilityManager.INSTANCE.register(ISkillData.class, new ISkillData.Storage(), new ISkillData.Factory());
		CapabilityManager.INSTANCE.register(IRiftStorage.class, new IRiftStorage.Storage(), new IRiftStorage.Factory());
		CapabilityManager.INSTANCE.register(IArcaneCompendium.class, new IArcaneCompendium.Storage(), new IArcaneCompendium.Factory());
		new ItemDefs();
		new CreativeTabsDefs();
		new BlockDefs();
		config = new AMConfig(e.getSuggestedConfigurationFile());
		config.init();
		SkillDefs.init();
		SpellDefs.init();
		LoreDefs.init();
		PotionEffectsDefs.init();
		ClientRegistry.registerKeyBinding(BindingsDefs.iceBridge);
		ClientRegistry.registerKeyBinding(BindingsDefs.enderTP);
		NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy);
		GameRegistry.registerTileEntity(TileEntityOcculus.class, "TileEntityOcculus");
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
	}

	public String getVersion() {
		return VERSION;
	}
	
}
