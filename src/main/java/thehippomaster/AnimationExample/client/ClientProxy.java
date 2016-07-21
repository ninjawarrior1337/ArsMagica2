package thehippomaster.AnimationExample.client;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thehippomaster.AnimationExample.CommonProxy;
import thehippomaster.AnimationExample.EntityTest;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void registerRenderers() {
		RenderingRegistry.registerEntityRenderingHandler(EntityTest.class, new IRenderFactory() {
			@Override
			public Render createRenderFor(RenderManager manager) {
				return new RenderTest(manager);
			}
		});
	}
}
