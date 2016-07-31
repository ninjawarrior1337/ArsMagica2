package am2;

import am2.api.ArsMagicaAPI;
import am2.api.affinity.Affinity;
import am2.config.AMConfig;
import am2.extensions.DataDefinitions;
import am2.packet.MessageBoolean;
import am2.packet.MessageCapabilities;
import am2.proxy.CommonProxy;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fluids.FluidRegistry;
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
		network = NetworkRegistry.INSTANCE.newSimpleChannel("AM2");
		network.registerMessage(MessageBoolean.IceBridgeHandler.class, MessageBoolean.class, 1, Side.SERVER);
		network.registerMessage(MessageCapabilities.class, MessageCapabilities.class, 3, Side.SERVER);
	}
	
	@EventHandler
	public void init (FMLInitializationEvent e) {
		proxy.init();
	}
	
	@EventHandler
	public void postInit (FMLPostInitializationEvent e) {
		proxy.postInit();
	}

	public String getVersion() {
		return VERSION;
	}
	
}
