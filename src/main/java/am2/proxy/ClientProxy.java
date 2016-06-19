package am2.proxy;

import static am2.defs.IDDefs.OCCULUS_GUI_ID;
import static am2.defs.IDDefs.RIFT_GUI_ID;

import am2.entity.EntityRiftStorage;
import am2.entity.EntitySpellProjectile;
import am2.entity.render.RenderRiftStorage;
import am2.entity.render.RenderSpellProjectile;
import am2.extensions.RiftStorage;
import am2.gui.GuiOcculus;
import am2.gui.GuiRiftStorage;
import am2.models.ModelLoader;
import am2.particles.AMParticleIcons;
import am2.particles.ParticleManagerClient;
import am2.texture.SpellPartManager;
import am2.utils.RenderFactory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		System.out.println("Running");
		switch (ID) {
		case OCCULUS_GUI_ID: return new GuiOcculus(player);
		case RIFT_GUI_ID: return new GuiRiftStorage(player, RiftStorage.For(player));
		}
		return super.getClientGuiElement(ID, player, world, x, y, z);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void preInit() {
		super.preInit();
		AMParticleIcons.instance.toString();
		SpellPartManager.INSTANCE.toString();
		particleManager = new ParticleManagerClient();
		RenderingRegistry.registerEntityRenderingHandler(EntityRiftStorage.class, new RenderFactory(RenderRiftStorage.class));
		RenderingRegistry.registerEntityRenderingHandler(EntitySpellProjectile.class, new RenderFactory(RenderSpellProjectile.class));
		ModelLoaderRegistry.registerLoader(new ModelLoader());
		MinecraftForge.EVENT_BUS.register(new ModelLoader());
	}
	
}
