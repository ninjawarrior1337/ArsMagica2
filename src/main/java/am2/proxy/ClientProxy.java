package am2.proxy;

import static am2.defs.IDDefs.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import org.lwjgl.opengl.GL11;

import am2.ArsMagica2;
import am2.api.math.AMVector3;
import am2.api.power.IPowerNode;
import am2.blocks.render.TileBlackAuremRenderer;
import am2.blocks.render.TileCelestialPrismRenderer;
import am2.blocks.render.TileCraftingAltarRenderer;
import am2.blocks.render.TileLecternRenderer;
import am2.blocks.render.TileObeliskRenderer;
import am2.blocks.tileentity.TileEntityBlackAurem;
import am2.blocks.tileentity.TileEntityCelestialPrism;
import am2.blocks.tileentity.TileEntityCraftingAltar;
import am2.blocks.tileentity.TileEntityInscriptionTable;
import am2.blocks.tileentity.TileEntityLectern;
import am2.blocks.tileentity.TileEntityObelisk;
import am2.commands.ConfigureAMUICommand;
import am2.defs.ItemDefs;
import am2.entity.EntityRiftStorage;
import am2.entity.EntitySpellEffect;
import am2.entity.EntitySpellProjectile;
import am2.entity.EntityThrownRock;
import am2.entity.render.RenderHidden;
import am2.entity.render.RenderRiftStorage;
import am2.entity.render.RenderSpellProjectile;
import am2.entity.render.RenderThrownRock;
import am2.extensions.RiftStorage;
import am2.gui.GuiInscriptionTable;
import am2.gui.GuiObelisk;
import am2.gui.GuiOcculus;
import am2.gui.GuiRiftStorage;
import am2.items.ItemSpellBase;
import am2.items.ItemSpellBook;
import am2.lore.ArcaneCompendium;
import am2.models.ArsMagicaModelLoader;
import am2.models.CullfaceModelLoader;
import am2.packet.AMNetHandler;
import am2.packet.AMPacketProcessorClient;
import am2.particles.AMParticleIcons;
import am2.particles.ParticleManagerClient;
import am2.power.PowerNodeEntry;
import am2.power.PowerTypes;
import am2.proxy.tick.ClientTickHandler;
import am2.spell.IComponent;
import am2.spell.component.Telekinesis;
import am2.texture.SpellIconManager;
import am2.utils.RenderFactory;
import am2.utils.RenderUtils;
import am2.utils.SpellUtils;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
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
		case GUI_INSCRIPTION_TABLE: return new GuiInscriptionTable(player.inventory, (TileEntityInscriptionTable)world.getTileEntity(new BlockPos(x, y, z)));
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
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityLectern.class, new TileLecternRenderer());
		
		ModelLoaderRegistry.registerLoader(new ArsMagicaModelLoader());
		ModelLoaderRegistry.registerLoader(new CullfaceModelLoader());
		
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
	
	@Override
	public void setTrackedLocation(AMVector3 location){
		clientTickHandler.setTrackLocation(location.toVec3D());
	}

	@Override
	public void setTrackedPowerCompound(NBTTagCompound compound){
		clientTickHandler.setTrackData(compound);
	}

	@Override
	public boolean hasTrackedLocationSynced(){
		return clientTickHandler.getHasSynced();
	}

	@Override
	public PowerNodeEntry getTrackedData(){
		return clientTickHandler.getTrackData();
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
	
	@SuppressWarnings("deprecation")
	@Override
	public void drawPowerOnBlockHighlight(EntityPlayer player, RayTraceResult target, float partialTicks){
		
		if (Minecraft.getMinecraft().thePlayer.getItemStackFromSlot(EntityEquipmentSlot.HEAD) != null &&
				(Minecraft.getMinecraft().thePlayer.getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() == ItemDefs.magitechGoggles)
				//|| ArmorHelper.isInfusionPreset(ArsMagica2.proxy.getLocalPlayer().getCurrentArmor(3), GenericImbuement.magitechGoggleIntegration))
				){
			if (target.getBlockPos() == null)
				return;
			TileEntity te = player.worldObj.getTileEntity(target.getBlockPos());
			if (te != null && te instanceof IPowerNode){
				ArsMagica2.proxy.setTrackedLocation(new AMVector3(target.getBlockPos()));
			}else{
				ArsMagica2.proxy.setTrackedLocation(AMVector3.zero());
			}

			if (ArsMagica2.proxy.hasTrackedLocationSynced()){
				PowerNodeEntry data = ArsMagica2.proxy.getTrackedData();
				Block block = player.worldObj.getBlockState(target.getBlockPos()).getBlock();
				float yOff = 0.5f;
				if (data != null){
					GL11.glPushAttrib(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_LIGHTING_BIT);
					for (PowerTypes type : ((IPowerNode<?>)te).getValidPowerTypes()){
						float pwr = data.getPower(type);
						float pct = pwr / ((IPowerNode<?>)te).getCapacity() * 100;
						AMVector3 offset = new AMVector3(target.getBlockPos().getX() + 0.5, target.getBlockPos().getX() + 0.5, target.getBlockPos().getZ() + 0.5).sub(
								new AMVector3((player.prevPosX - (player.prevPosX - player.posX) * partialTicks),
										(player.prevPosY - (player.prevPosY - player.posY) * partialTicks) + player.getEyeHeight(),
										(player.prevPosZ - (player.prevPosZ - player.posZ) * partialTicks)));
						offset = offset.normalize();
						if (target.getBlockPos().getY() <= player.posY + player.getEyeHeight()){
							RenderUtils.drawTextInWorldAtOffset(String.format("%s%.2f (%.2f%%)", type.getChatColor(), pwr, pct),
									target.getBlockPos().getX() - (player.prevPosX - (player.prevPosX - player.posX) * partialTicks) + 0.5f - offset.x,
									target.getBlockPos().getY() + yOff - (player.prevPosY - (player.prevPosY - player.posY) * partialTicks) + block.getBoundingBox(player.worldObj.getBlockState(target.getBlockPos()), player.worldObj, target.getBlockPos()).maxY * 0.8f,
									target.getBlockPos().getZ() - (player.prevPosZ - (player.prevPosZ - player.posZ) * partialTicks) + 0.5f - offset.z,
									0xFFFFFF);
							yOff += 0.12f;
						}else{
							RenderUtils.drawTextInWorldAtOffset(String.format("%s%.2f (%.2f%%)", type.getChatColor(), pwr, pct),
									target.getBlockPos().getX() - (player.prevPosX - (player.prevPosX - player.posX) * partialTicks) + 0.5f - offset.x,
									target.getBlockPos().getY() - yOff - (player.prevPosY - (player.prevPosY - player.posY) * partialTicks) - block.getBoundingBox(player.worldObj.getBlockState(target.getBlockPos()), player.worldObj, target.getBlockPos()).maxY * 0.2f,
									target.getBlockPos().getZ() - (player.prevPosZ - (player.prevPosZ - player.posZ) * partialTicks) + 0.5f - offset.z,
									0xFFFFFF);
							yOff -= 0.12f;
						}
					}
					GL11.glPopAttrib();
				}
			}
		}
	}
	
	@Override
	public void requestPowerPathVisuals(IPowerNode<?> node, EntityPlayerMP player){
		AMNetHandler.INSTANCE.syncPowerPaths(node, player);
	}

	@Override
	public void receivePowerPathVisuals(HashMap<PowerTypes, ArrayList<LinkedList<Vec3d>>> paths){
		powerPathVisuals = paths;
	}

	@Override
	public HashMap<PowerTypes, ArrayList<LinkedList<Vec3d>>> getPowerPathVisuals(){
		return powerPathVisuals;
	}
}
