package thehippomaster.AnimationExample;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;

@Mod(modid = "AnimationExample", name = "Animation Example", version = "1.0.0")
public class AnimationExample {
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent e) {
	}
	
	@EventHandler
	public void init(FMLInitializationEvent e) {
		EntityRegistry.registerModEntity(EntityTest.class, "EntityTest", 0, instance, 0, 0, false);
		
		proxy.registerRenderers();
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent e) {
	}
	
	@Instance("AnimationExample")
	public static AnimationExample instance;
	@SidedProxy(clientSide="thehippomaster.AnimationExample.client.ClientProxy", serverSide="thehippomaster.AnimationExample.CommonProxy")
	public static CommonProxy proxy;
}
