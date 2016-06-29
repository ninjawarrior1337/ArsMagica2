package am2.proxy;

import static am2.defs.IDDefs.GUI_OBELISK;
import static am2.defs.IDDefs.GUI_OCCULUS;
import static am2.defs.IDDefs.GUI_RIFT;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.common.collect.ImmutableMap;

import am2.ArsMagica2;
import am2.api.extensions.IAffinityData;
import am2.api.extensions.IArcaneCompendium;
import am2.api.extensions.IEntityExtension;
import am2.api.extensions.IRiftStorage;
import am2.api.extensions.ISkillData;
import am2.blocks.tileentity.TileEntityBlackAurem;
import am2.blocks.tileentity.TileEntityCelestialPrism;
import am2.blocks.tileentity.TileEntityCraftingAltar;
import am2.blocks.tileentity.TileEntityLectern;
import am2.blocks.tileentity.TileEntityObelisk;
import am2.blocks.tileentity.TileEntityOcculus;
import am2.container.ContainerObelisk;
import am2.container.ContainerRiftStorage;
import am2.defs.BlockDefs;
import am2.defs.CreativeTabsDefs;
import am2.defs.ItemDefs;
import am2.defs.LoreDefs;
import am2.defs.PotionEffectsDefs;
import am2.defs.SkillDefs;
import am2.defs.SpellDefs;
import am2.enchantments.AMEnchantments;
import am2.entity.EntityRiftStorage;
import am2.entity.EntitySpellEffect;
import am2.entity.EntitySpellProjectile;
import am2.entity.EntityThrownRock;
import am2.extensions.RiftStorage;
import am2.handler.EntityHandler;
import am2.handler.PotionEffectHandler;
import am2.lore.CompendiumUnlockHandler;
import am2.packet.AMNetHandler;
import am2.packet.AMPacketProcessorServer;
import am2.particles.ParticleManagerServer;
import am2.proxy.tick.ServerTickHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;


public class CommonProxy implements IGuiHandler{

	public ParticleManagerServer particleManager;
	protected ServerTickHandler serverTickHandler;
	protected AMPacketProcessorServer packetProcessor;
	private HashMap<EntityLivingBase, ArrayList<PotionEffect>> deferredPotionEffects = new HashMap<>();
	private HashMap<EntityLivingBase, Integer> deferredDimensionTransfers = new HashMap<>();
	public ArrayList<Item> items = new ArrayList<>();
	public AMEnchantments enchantments;


	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		System.out.println("Running");
		switch (ID) {
		case GUI_OCCULUS: return null;
		case GUI_RIFT: return new ContainerRiftStorage(player, RiftStorage.For(player));
		case GUI_OBELISK: return new ContainerObelisk((TileEntityObelisk)world.getTileEntity(new BlockPos(x, y, z)), player);
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return null;
	}

	public void preInit() {
		initHandlers();
		ArsMagica2.config.init();
		serverTickHandler = new ServerTickHandler();
		enchantments = new AMEnchantments();
		AMNetHandler.INSTANCE.init();
		AMNetHandler.INSTANCE.registerChannels(packetProcessor);
		
		MinecraftForge.EVENT_BUS.register(serverTickHandler);
		MinecraftForge.EVENT_BUS.register(new CompendiumUnlockHandler());
		MinecraftForge.EVENT_BUS.register(new EntityHandler());
		MinecraftForge.EVENT_BUS.register(new PotionEffectHandler());
		MinecraftForge.EVENT_BUS.register(packetProcessor);

		EntityRegistry.registerModEntity(EntitySpellProjectile.class, "SpellProjectile", 0, ArsMagica2.instance, 80, 1, false);
		EntityRegistry.registerModEntity(EntityRiftStorage.class, "RiftStorage", 1, ArsMagica2.instance, 80, 1, false);
		EntityRegistry.registerModEntity(EntitySpellEffect.class, "SpellEffect", 2, ArsMagica2.instance, 80, 1, false);
		EntityRegistry.registerModEntity(EntityThrownRock.class, "ThrownRock", 3, ArsMagica2.instance, 80, 1, false);
		
		GameRegistry.registerTileEntity(TileEntityOcculus.class, "TileEntityOcculus");
		GameRegistry.registerTileEntity(TileEntityCraftingAltar.class, "TileEntityCraftingAltar");
		GameRegistry.registerTileEntity(TileEntityLectern.class, "TileEntityLectern");
		GameRegistry.registerTileEntity(TileEntityObelisk.class, "TileEntityObelisk");
		GameRegistry.registerTileEntity(TileEntityCelestialPrism.class, "TileEntityCelestialPrism");
		GameRegistry.registerTileEntity(TileEntityBlackAurem.class, "TileEntityBlackAurem");
		
		CapabilityManager.INSTANCE.register(IEntityExtension.class, new IEntityExtension.Storage(), new IEntityExtension.Factory());
		CapabilityManager.INSTANCE.register(IAffinityData.class, new IAffinityData.Storage(), new IAffinityData.Factory());
		CapabilityManager.INSTANCE.register(ISkillData.class, new ISkillData.Storage(), new ISkillData.Factory());
		CapabilityManager.INSTANCE.register(IRiftStorage.class, new IRiftStorage.Storage(), new IRiftStorage.Factory());
		CapabilityManager.INSTANCE.register(IArcaneCompendium.class, new IArcaneCompendium.Storage(), new IArcaneCompendium.Factory());
		
		SkillDefs.init();
		SpellDefs.init();
		LoreDefs.init();
		PotionEffectsDefs.init();
		new ItemDefs();
		new CreativeTabsDefs();
		new BlockDefs();

	}
	
	public void initHandlers() {
		particleManager = new ParticleManagerServer();
		packetProcessor = new AMPacketProcessorServer();
	}
	
	public void addDeferredTargetSet(EntityLiving ent, EntityLivingBase target){
		serverTickHandler.addDeferredTarget(ent, target);
	}
	
	public ImmutableMap<EntityLivingBase, ArrayList<PotionEffect>> getDeferredPotionEffects(){
		return ImmutableMap.copyOf(deferredPotionEffects);
	}
	
	public void clearDeferredPotionEffects(){
		deferredPotionEffects.clear();
	}
	
	public void clearDeferredDimensionTransfers(){
		deferredDimensionTransfers.clear();
	}

	public ImmutableMap<EntityLivingBase, Integer> getDeferredDimensionTransfers(){
		return ImmutableMap.copyOf(deferredDimensionTransfers);
	}

	public void unlockCompendiumEntry(String id) {
		
	}

	public void unlockCompendiumCategory(String id) {
		
	}

	public void renderGameOverlay() {}
	
	public void addDeferredDimensionTransfer(EntityLivingBase ent, int dimension){
		deferredDimensionTransfers.put(ent, dimension);
	}

	public boolean setMouseDWheel(int dwheel) {
		return false;
	}

}
