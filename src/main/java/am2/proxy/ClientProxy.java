package am2.proxy;

import static am2.defs.IDDefs.*;
import static am2.defs.IDDefs.GUI_RIFT;

import am2.ArsMagica2;
import am2.blocks.render.TileBlackAuremRenderer;
import am2.blocks.render.TileCelestialPrismRenderer;
import am2.blocks.render.TileCraftingAltarRenderer;
import am2.blocks.render.TileObeliskRenderer;
import am2.blocks.tileentity.TileEntityBlackAurem;
import am2.blocks.tileentity.TileEntityCelestialPrism;
import am2.blocks.tileentity.TileEntityCraftingAltar;
import am2.blocks.tileentity.TileEntityObelisk;
import am2.commands.ConfigureAMUICommand;
import am2.entity.EntityRiftStorage;
import am2.entity.EntitySpellEffect;
import am2.entity.EntitySpellProjectile;
import am2.entity.EntityThrownRock;
import am2.entity.render.RenderHidden;
import am2.entity.render.RenderRiftStorage;
import am2.entity.render.RenderSpellProjectile;
import am2.entity.render.RenderThrownRock;
import am2.extensions.RiftStorage;
import am2.gui.GuiObelisk;
import am2.gui.GuiOcculus;
import am2.gui.GuiRiftStorage;
import am2.items.ItemSpellBase;
import am2.items.ItemSpellBook;
import am2.lore.ArcaneCompendium;
import am2.models.ArsMagicaModelLoader;
import am2.packet.AMPacketProcessorClient;
import am2.particles.AMParticleIcons;
import am2.particles.ParticleManagerClient;
import am2.proxy.tick.ClientTickHandler;
import am2.spell.IComponent;
import am2.spell.component.Telekinesis;
import am2.texture.SpellIconManager;
import am2.utils.RenderFactory;
import am2.utils.SpellUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {
	
	public ClientTickHandler clientTickHandler;
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		System.out.println("Running");
		switch (ID) {
		case GUI_OCCULUS: return new GuiOcculus(player);
		case GUI_RIFT: return new GuiRiftStorage(player, RiftStorage.For(player));
		case GUI_OBELISK: return new GuiObelisk((TileEntityObelisk)world.getTileEntity(new BlockPos(x, y, z)), player);
		}
		return super.getClientGuiElement(ID, player, world, x, y, z);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void preInit() {
		super.preInit();
		
		OBJLoader.INSTANCE.addDomain("arsmagica2");
		
		AMParticleIcons.instance.toString();
		SpellIconManager.INSTANCE.toString();
		
		RenderingRegistry.registerEntityRenderingHandler(EntityRiftStorage.class, new RenderFactory(RenderRiftStorage.class));
		RenderingRegistry.registerEntityRenderingHandler(EntitySpellProjectile.class, new RenderFactory(RenderSpellProjectile.class));
		RenderingRegistry.registerEntityRenderingHandler(EntitySpellEffect.class, new RenderFactory(RenderHidden.class));
		RenderingRegistry.registerEntityRenderingHandler(EntityThrownRock.class, new RenderFactory(RenderThrownRock.class));
		
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCraftingAltar.class, new TileCraftingAltarRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityObelisk.class, new TileObeliskRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCelestialPrism.class, new TileCelestialPrismRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBlackAurem.class, new TileBlackAuremRenderer());
		
		ModelLoaderRegistry.registerLoader(new ArsMagicaModelLoader());
		
		MinecraftForge.EVENT_BUS.register(new ArsMagicaModelLoader());
		MinecraftForge.EVENT_BUS.register(clientTickHandler);
		
		ArsMagica2.config.clientInit();
		
		ClientCommandHandler.instance.registerCommand(new ConfigureAMUICommand());
	}
	
	@Override
	public void initHandlers() {
		particleManager = new ParticleManagerClient();
		packetProcessor = new AMPacketProcessorClient();
		clientTickHandler = new ClientTickHandler();
	}
	
	/**
	 * Proxied compendium unlocks.  Do not call directly - use the CompendiumUnlockHandler instead.
	 */
	@Override
	public void unlockCompendiumEntry(String id){
		ArcaneCompendium.For(Minecraft.getMinecraft().thePlayer).unlockEntry(id);
	}
	
	@Override
	public void renderGameOverlay() {
		clientTickHandler.renderOverlays();
	}

	/**
	 * Proxied compendium unlocks.  Do not call directly - use the CompendiumUnlockHandler instead.
	 */
	@Override
	public void unlockCompendiumCategory(String id){
		ArcaneCompendium.For(Minecraft.getMinecraft().thePlayer).unlockCategory(id);
	}
	
	@Override
	public boolean setMouseDWheel(int dwheel){
		if (dwheel == 0) return false;

		ItemStack stack = Minecraft.getMinecraft().thePlayer.getActiveItemStack();
		System.out.println(stack);
		if (stack == null) return false;

		boolean store = checkForTKMove(stack);
		if (!store && stack.getItem() instanceof ItemSpellBook){
			store = Minecraft.getMinecraft().thePlayer.isSneaking();
		}

		if (store){
			clientTickHandler.setDWheel(dwheel / 120, Minecraft.getMinecraft().thePlayer.inventory.currentItem, Minecraft.getMinecraft().thePlayer.isHandActive());
			return true;
		}else{
			clientTickHandler.setDWheel(0, -1, false);
		}
		return false;
	}

	private boolean checkForTKMove(ItemStack stack){
		if (stack.getItem() instanceof ItemSpellBook){
			ItemStack activeStack = ((ItemSpellBook)stack.getItem()).GetActiveItemStack(stack);
			if (activeStack != null)
				stack = activeStack;
		}
		if (stack.getItem() instanceof ItemSpellBase && stack.hasTagCompound() && Minecraft.getMinecraft().thePlayer.isHandActive()){
			for (IComponent component : SpellUtils.getComponentsForStage(stack, -1)){
				if (component instanceof Telekinesis){
					return true;
				}
			}
		}
		return false;
	}
}
