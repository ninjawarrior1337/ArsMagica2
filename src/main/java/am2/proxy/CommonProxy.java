package am2.proxy;

import static am2.defs.IDDefs.GUI_ARMOR_INFUSION;
import static am2.defs.IDDefs.GUI_CRYSTAL_MARKER;
import static am2.defs.IDDefs.GUI_FLICKER_HABITAT;
import static am2.defs.IDDefs.GUI_INSCRIPTION_TABLE;
import static am2.defs.IDDefs.GUI_KEYSTONE;
import static am2.defs.IDDefs.GUI_KEYSTONE_CHEST;
import static am2.defs.IDDefs.GUI_KEYSTONE_LOCKABLE;
import static am2.defs.IDDefs.GUI_OBELISK;
import static am2.defs.IDDefs.GUI_OCCULUS;
import static am2.defs.IDDefs.GUI_RIFT;
import static am2.defs.IDDefs.GUI_SPELL_BOOK;
import static am2.defs.IDDefs.GUI_SPELL_CUSTOMIZATION;
import static am2.defs.IDDefs.GUI_SPELL_SEALED_DOOR;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import com.google.common.collect.ImmutableMap;

import am2.AMChunkLoader;
import am2.ArsMagica2;
import am2.affinity.AffinityAbilityHelper;
import am2.api.ArsMagicaAPI;
import am2.api.affinity.Affinity;
import am2.api.blocks.IKeystoneLockable;
import am2.api.extensions.IAffinityData;
import am2.api.extensions.IArcaneCompendium;
import am2.api.extensions.IEntityExtension;
import am2.api.extensions.IRiftStorage;
import am2.api.extensions.ISkillData;
import am2.api.math.AMVector3;
import am2.api.power.IPowerNode;
import am2.armor.ArmorEventHandler;
import am2.armor.infusions.DamageReductionImbuement;
import am2.armor.infusions.Dispelling;
import am2.armor.infusions.FallProtection;
import am2.armor.infusions.FireProtection;
import am2.armor.infusions.Freedom;
import am2.armor.infusions.GenericImbuement;
import am2.armor.infusions.Healing;
import am2.armor.infusions.HungerBoost;
import am2.armor.infusions.ImbuementRegistry;
import am2.armor.infusions.JumpBoost;
import am2.armor.infusions.LifeSaving;
import am2.armor.infusions.Lightstep;
import am2.armor.infusions.MiningSpeed;
import am2.armor.infusions.Recoil;
import am2.armor.infusions.SwimSpeed;
import am2.armor.infusions.WaterBreathing;
import am2.armor.infusions.WaterWalking;
import am2.blocks.tileentity.TileEntityArmorImbuer;
import am2.blocks.tileentity.TileEntityBlackAurem;
import am2.blocks.tileentity.TileEntityCandle;
import am2.blocks.tileentity.TileEntityCelestialPrism;
import am2.blocks.tileentity.TileEntityCraftingAltar;
import am2.blocks.tileentity.TileEntityCrystalMarker;
import am2.blocks.tileentity.TileEntityCrystalMarkerSpellExport;
import am2.blocks.tileentity.TileEntityEssenceConduit;
import am2.blocks.tileentity.TileEntityFlickerHabitat;
import am2.blocks.tileentity.TileEntityFlickerLure;
import am2.blocks.tileentity.TileEntityInscriptionTable;
import am2.blocks.tileentity.TileEntityKeystoneChest;
import am2.blocks.tileentity.TileEntityKeystoneDoor;
import am2.blocks.tileentity.TileEntityKeystoneRecepticle;
import am2.blocks.tileentity.TileEntityLectern;
import am2.blocks.tileentity.TileEntityManaBattery;
import am2.blocks.tileentity.TileEntityObelisk;
import am2.blocks.tileentity.TileEntityOcculus;
import am2.blocks.tileentity.TileEntitySlipstreamGenerator;
import am2.blocks.tileentity.TileEntitySpellSealedDoor;
import am2.blocks.tileentity.flickers.FlickerOperatorButchery;
import am2.blocks.tileentity.flickers.FlickerOperatorContainment;
import am2.blocks.tileentity.flickers.FlickerOperatorFelledOak;
import am2.blocks.tileentity.flickers.FlickerOperatorFishing;
import am2.blocks.tileentity.flickers.FlickerOperatorFlatLands;
import am2.blocks.tileentity.flickers.FlickerOperatorGentleRains;
import am2.blocks.tileentity.flickers.FlickerOperatorInterdiction;
import am2.blocks.tileentity.flickers.FlickerOperatorItemTransport;
import am2.blocks.tileentity.flickers.FlickerOperatorLight;
import am2.blocks.tileentity.flickers.FlickerOperatorMoonstoneAttractor;
import am2.blocks.tileentity.flickers.FlickerOperatorNaturesBounty;
import am2.blocks.tileentity.flickers.FlickerOperatorPackedEarth;
import am2.blocks.tileentity.flickers.FlickerOperatorProgeny;
import am2.blocks.tileentity.flickers.FlickerOperatorRegistry;
import am2.container.ContainerArmorInfuser;
import am2.container.ContainerCrystalMarker;
import am2.container.ContainerFlickerHabitat;
import am2.container.ContainerInscriptionTable;
import am2.container.ContainerKeystoneChest;
import am2.container.ContainerKeystoneLockable;
import am2.container.ContainerObelisk;
import am2.container.ContainerRiftStorage;
import am2.container.ContainerSpellBook;
import am2.container.ContainerSpellCustomization;
import am2.container.ContainerSpellSealedDoor;
import am2.defs.AMRecipes;
import am2.defs.BlockDefs;
import am2.defs.CreativeTabsDefs;
import am2.defs.EntityManager;
import am2.defs.ItemDefs;
import am2.defs.LoreDefs;
import am2.defs.PotionEffectsDefs;
import am2.defs.SkillDefs;
import am2.defs.SpellDefs;
import am2.enchantments.AMEnchantments;
import am2.extensions.RiftStorage;
import am2.handler.EntityHandler;
import am2.handler.FlickerEvents;
import am2.handler.PotionEffectHandler;
import am2.items.ContainerKeystone;
import am2.items.ItemKeystone;
import am2.items.ItemSpellBook;
import am2.lore.CompendiumUnlockHandler;
import am2.network.SeventhSanctum;
import am2.packet.AMNetHandler;
import am2.packet.AMPacketProcessorServer;
import am2.particles.ParticleManagerServer;
import am2.power.PowerNodeCache;
import am2.power.PowerNodeEntry;
import am2.power.PowerTypes;
import am2.proxy.tick.ServerTickHandler;
import am2.spell.AbstractSpellPart;
import am2.trackers.ItemFrameWatcher;
import am2.trackers.PlayerTracker;
import am2.utils.InventoryUtilities;
import am2.utils.NPCSpells;
import am2.world.AM2WorldDecorator;
import am2.world.BiomeWitchwoodForest;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.BiomeManager.BiomeEntry;
import net.minecraftforge.common.BiomeManager.BiomeType;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;


public class CommonProxy implements IGuiHandler{

	public ParticleManagerServer particleManager;
	protected ServerTickHandler serverTickHandler;
	protected AMPacketProcessorServer packetProcessor;
	private HashMap<EntityLivingBase, ArrayList<PotionEffect>> deferredPotionEffects = new HashMap<>();
	private HashMap<EntityLivingBase, Integer> deferredDimensionTransfers = new HashMap<>();
	public ItemDefs items;
	public BlockDefs blocks;
	public AMEnchantments enchantments;
	private int totalFlickerCount = 0;
	
	public PlayerTracker playerTracker;
	public ItemFrameWatcher itemFrameWatcher;
	private AM2WorldDecorator worldGen;
	
	public static HashMap<PowerTypes, ArrayList<LinkedList<Vec3d>>> powerPathVisuals;
	
	public CommonProxy() {
		playerTracker = new PlayerTracker();
		itemFrameWatcher = new ItemFrameWatcher();
	}
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
		switch (ID) {
		case GUI_OCCULUS: return null;
		case GUI_RIFT: return new ContainerRiftStorage(player, RiftStorage.For(player));
		case GUI_OBELISK: return new ContainerObelisk((TileEntityObelisk)world.getTileEntity(new BlockPos(x, y, z)), player);
		case GUI_INSCRIPTION_TABLE: return new ContainerInscriptionTable((TileEntityInscriptionTable)world.getTileEntity(new BlockPos(x, y, z)), player.inventory);
		case GUI_SPELL_CUSTOMIZATION: return new ContainerSpellCustomization(player);
		case GUI_ARMOR_INFUSION: return new ContainerArmorInfuser(player, (TileEntityArmorImbuer) world.getTileEntity(new BlockPos(x, y, z)));
		case GUI_SPELL_BOOK: 
			ItemStack bookStack = player.getHeldItemMainhand();
			if (bookStack.getItem() == null || !(bookStack.getItem() instanceof ItemSpellBook)){
				return null;
			}
			ItemSpellBook item = (ItemSpellBook)bookStack.getItem();
			return new ContainerSpellBook(player.inventory, bookStack, item.ConvertToInventory(bookStack));
		case GUI_KEYSTONE_CHEST:
			if (!(te instanceof TileEntityKeystoneChest)){
				return null;
			}
			return new ContainerKeystoneChest(player.inventory, (TileEntityKeystoneChest)te);
		case GUI_KEYSTONE:
			ItemStack keystoneStack = player.getHeldItemMainhand();
			if (keystoneStack.getItem() == null || !(keystoneStack.getItem() instanceof ItemKeystone)){
				return null;
			}
			ItemKeystone keystone = (ItemKeystone)keystoneStack.getItem();

			int runeBagSlot = InventoryUtilities.getInventorySlotIndexFor(player.inventory, ItemDefs.runeBag);
			ItemStack runeBag = null;
			if (runeBagSlot > -1)
				runeBag = player.inventory.getStackInSlot(runeBagSlot);

			return new ContainerKeystone(player.inventory, player.getHeldItemMainhand(), runeBag, keystone.ConvertToInventory(keystoneStack), runeBag == null ? null : ItemDefs.runeBag.ConvertToInventory(runeBag), runeBagSlot);
		case GUI_KEYSTONE_LOCKABLE:
			if (!(te instanceof IKeystoneLockable)){
				return null;
			}
			return new ContainerKeystoneLockable(player.inventory, (IKeystoneLockable<?>)te);
		case GUI_FLICKER_HABITAT: return new ContainerFlickerHabitat(player, (TileEntityFlickerHabitat) te);
		case GUI_CRYSTAL_MARKER: return new ContainerCrystalMarker(player, (TileEntityCrystalMarker)te);
		case GUI_SPELL_SEALED_DOOR: return new ContainerSpellSealedDoor(player.inventory, (TileEntitySpellSealedDoor)te);
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return null;
	}

	public void preInit() {
		
		ForgeChunkManager.setForcedChunkLoadingCallback(ArsMagica2.instance, AMChunkLoader.INSTANCE);
		NetworkRegistry.INSTANCE.registerGuiHandler(ArsMagica2.instance, this);
		OreDictionary.registerOre("fenceWood", Blocks.ACACIA_FENCE);
		OreDictionary.registerOre("fenceWood", Blocks.OAK_FENCE);
		OreDictionary.registerOre("fenceWood", Blocks.DARK_OAK_FENCE);
		OreDictionary.registerOre("fenceWood", Blocks.SPRUCE_FENCE);
		OreDictionary.registerOre("fenceWood", Blocks.BIRCH_FENCE);
		OreDictionary.registerOre("fenceWood", Blocks.JUNGLE_FENCE);
		SeventhSanctum.instance.init();
		
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
		MinecraftForge.EVENT_BUS.register(new AffinityAbilityHelper());
		MinecraftForge.EVENT_BUS.register(packetProcessor);
		MinecraftForge.EVENT_BUS.register(PowerNodeCache.instance);
		MinecraftForge.EVENT_BUS.register(new ArmorEventHandler());
		MinecraftForge.EVENT_BUS.register(playerTracker);
		MinecraftForge.EVENT_BUS.register(new FlickerEvents());
		
		registerInfusions();
		registerFlickerOperators();
				
		GameRegistry.registerTileEntity(TileEntityOcculus.class, "TileEntityOcculus");
		GameRegistry.registerTileEntity(TileEntityCraftingAltar.class, "TileEntityCraftingAltar");
		GameRegistry.registerTileEntity(TileEntityLectern.class, "TileEntityLectern");
		GameRegistry.registerTileEntity(TileEntityObelisk.class, "TileEntityObelisk");
		GameRegistry.registerTileEntity(TileEntityCelestialPrism.class, "TileEntityCelestialPrism");
		GameRegistry.registerTileEntity(TileEntityBlackAurem.class, "TileEntityBlackAurem");
		GameRegistry.registerTileEntity(TileEntityCandle.class, "TileEntityCandle");
		GameRegistry.registerTileEntity(TileEntityCrystalMarker.class, "TileEntityCrystalMarker");
		GameRegistry.registerTileEntity(TileEntityCrystalMarkerSpellExport.class, "TileEntityCrystalMarkerSpellExport");
		GameRegistry.registerTileEntity(TileEntityFlickerHabitat.class, "TileEntityFlickerHabitat");
		GameRegistry.registerTileEntity(TileEntityInscriptionTable.class, "TileEntityInscriptionTable");
		GameRegistry.registerTileEntity(TileEntityManaBattery.class, "TileEntityManaBattery");
		GameRegistry.registerTileEntity(TileEntityArmorImbuer.class, "TileEntityArmorImbuer");
		GameRegistry.registerTileEntity(TileEntitySlipstreamGenerator.class, "TileEntitySlipstramGenerator");
		GameRegistry.registerTileEntity(TileEntityKeystoneRecepticle.class, "TileEntityKeystoneRecepticle");
		GameRegistry.registerTileEntity(TileEntityKeystoneDoor.class, "TileEntityKeystoneDoor");
		GameRegistry.registerTileEntity(TileEntityKeystoneChest.class, "TileEntityKeystoneChest");
		GameRegistry.registerTileEntity(TileEntitySpellSealedDoor.class, "TileEntitySpellSealedDoor");
		GameRegistry.registerTileEntity(TileEntityFlickerLure.class, "TileEntityFlickerLure");
		GameRegistry.registerTileEntity(TileEntityEssenceConduit.class, "TileEntityEssenceConduit");

		CapabilityManager.INSTANCE.register(IEntityExtension.class, new IEntityExtension.Storage(), new IEntityExtension.Factory());
		CapabilityManager.INSTANCE.register(IAffinityData.class, new IAffinityData.Storage(), new IAffinityData.Factory());
		CapabilityManager.INSTANCE.register(ISkillData.class, new ISkillData.Storage(), new ISkillData.Factory());
		CapabilityManager.INSTANCE.register(IRiftStorage.class, new IRiftStorage.Storage(), new IRiftStorage.Factory());
		CapabilityManager.INSTANCE.register(IArcaneCompendium.class, new IArcaneCompendium.Storage(), new IArcaneCompendium.Factory());
		
		worldGen = new AM2WorldDecorator();
		GameRegistry.registerWorldGenerator(worldGen, 0);
		
		EntityManager.instance.registerEntities();
		EntityManager.instance.initializeSpawns();
		AMEnchantments.Init();
		SkillDefs.init();
		SpellDefs.init();
		NPCSpells.instance.toString();
		LoreDefs.init();
		PotionEffectsDefs.init();
		items = new ItemDefs();
		blocks = new BlockDefs();
		blocks.preInit();
		new CreativeTabsDefs();
	}
	
	public void init() {
		if (ArsMagica2.config.getEnableWitchwoodForest()){
			BiomeDictionary.registerBiomeType(BiomeWitchwoodForest.instance, Type.FOREST, Type.MAGICAL);
			BiomeManager.addBiome(BiomeType.COOL, new BiomeEntry(BiomeWitchwoodForest.instance, 6));
		}
	}
	
	public void postInit() {
		playerTracker.postInit();
		MinecraftForge.EVENT_BUS.register(playerTracker);
		
		AMRecipes.addRecipes();
		for (AbstractSpellPart part : ArsMagicaAPI.getSpellRegistry().getValues()) {
			if (ArsMagicaAPI.getSkillRegistry().getValue(part.getRegistryName()) == null)
				throw new IllegalStateException("Spell Part " + part.getRegistryName() + " is missing a skill, this would cause severe problems");
		}
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
	
	public void setTrackedPowerCompound(NBTTagCompound compound){
	}

	public void setTrackedLocation(AMVector3 location){
	}

	public boolean hasTrackedLocationSynced(){
		return false;
	}

	public PowerNodeEntry getTrackedData(){
		return null;
	}

	public void drawPowerOnBlockHighlight(EntityPlayer player, RayTraceResult target, float partialTicks) {}

	public void receivePowerPathVisuals(HashMap<PowerTypes, ArrayList<LinkedList<Vec3d>>> nodePaths) {}

	public void requestPowerPathVisuals(IPowerNode<?> node, EntityPlayerMP player) {}

	public HashMap<PowerTypes, ArrayList<LinkedList<Vec3d>>> getPowerPathVisuals() {
		return null;
	}

	public void blackoutArmorPiece(EntityPlayerMP player, EntityEquipmentSlot slot, int cooldown){
		serverTickHandler.blackoutArmorPiece(player, slot, cooldown);
	}
	
	public void registerInfusions(){
		DamageReductionImbuement.registerAll();
		GenericImbuement.registerAll();
		ImbuementRegistry.instance.registerImbuement(new Dispelling());
		ImbuementRegistry.instance.registerImbuement(new FallProtection());
		ImbuementRegistry.instance.registerImbuement(new FireProtection());
		ImbuementRegistry.instance.registerImbuement(new Freedom());
		ImbuementRegistry.instance.registerImbuement(new Healing());
		ImbuementRegistry.instance.registerImbuement(new HungerBoost());
		ImbuementRegistry.instance.registerImbuement(new JumpBoost());
		ImbuementRegistry.instance.registerImbuement(new LifeSaving());
		ImbuementRegistry.instance.registerImbuement(new Lightstep());
		ImbuementRegistry.instance.registerImbuement(new MiningSpeed());
		ImbuementRegistry.instance.registerImbuement(new Recoil());
		ImbuementRegistry.instance.registerImbuement(new SwimSpeed());
		ImbuementRegistry.instance.registerImbuement(new WaterBreathing());
		ImbuementRegistry.instance.registerImbuement(new WaterWalking());
	}

	public void flashManaBar() {}
	
	public void incrementFlickerCount(){
		this.totalFlickerCount++;
	}

	public void decrementFlickerCount(){
		this.totalFlickerCount--;
		if (this.totalFlickerCount < 0)
			this.totalFlickerCount = 0;
	}

	public int getTotalFlickerCount(){
		return this.totalFlickerCount;
	}
	
	private void registerFlickerOperators(){
		FlickerOperatorRegistry.instance.registerFlickerOperator(
				new FlickerOperatorItemTransport(),
				Affinity.AIR
		);
		FlickerOperatorRegistry.instance.registerFlickerOperator(
				new FlickerOperatorButchery(),
				Affinity.FIRE, Affinity.LIFE
		);
		FlickerOperatorRegistry.instance.registerFlickerOperator(
				new FlickerOperatorContainment(),
				Affinity.AIR, Affinity.ENDER
		);
		FlickerOperatorRegistry.instance.registerFlickerOperator(
				new FlickerOperatorFelledOak(),
				Affinity.NATURE, Affinity.LIGHTNING
		);
		FlickerOperatorRegistry.instance.registerFlickerOperator(
				new FlickerOperatorFlatLands(),
				Affinity.EARTH, Affinity.ICE
		);
		FlickerOperatorRegistry.instance.registerFlickerOperator(
				new FlickerOperatorGentleRains(),
				Affinity.WATER
		);
		FlickerOperatorRegistry.instance.registerFlickerOperator(
				new FlickerOperatorInterdiction(),
				Affinity.AIR, Affinity.ARCANE
		);
		FlickerOperatorRegistry.instance.registerFlickerOperator(
				new FlickerOperatorLight(),
				Affinity.FIRE, Affinity.LIGHTNING
		);
		FlickerOperatorRegistry.instance.registerFlickerOperator(
				new FlickerOperatorMoonstoneAttractor(),
				Affinity.LIGHTNING, Affinity.ARCANE, Affinity.EARTH
		);
		FlickerOperatorRegistry.instance.registerFlickerOperator(
				new FlickerOperatorNaturesBounty(),
				Affinity.NATURE, Affinity.WATER, Affinity.LIFE
		);
		FlickerOperatorRegistry.instance.registerFlickerOperator(
				new FlickerOperatorPackedEarth(),
				Affinity.EARTH
		);
		FlickerOperatorRegistry.instance.registerFlickerOperator(
				new FlickerOperatorProgeny(),
				Affinity.LIFE
		);
		FlickerOperatorRegistry.instance.registerFlickerOperator(
				new FlickerOperatorFishing(),
				Affinity.WATER, Affinity.NATURE
		);
	}
	
	public AM2WorldDecorator getWorldGenerator() {
		return worldGen;
	}
}
