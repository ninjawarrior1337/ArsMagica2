package am2.proxy;

import static am2.defs.IDDefs.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import com.google.common.collect.ImmutableMap;

import am2.ArsMagica2;
import am2.affinity.AffinityAbilityHelper;
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
import am2.blocks.tileentity.TileEntityFlickerHabitat;
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
import am2.bosses.EntityAirGuardian;
import am2.bosses.EntityArcaneGuardian;
import am2.bosses.EntityEarthGuardian;
import am2.bosses.EntityEnderGuardian;
import am2.bosses.EntityFireGuardian;
import am2.bosses.EntityLifeGuardian;
import am2.bosses.EntityLightningGuardian;
import am2.bosses.EntityNatureGuardian;
import am2.bosses.EntityWaterGuardian;
import am2.bosses.EntityWinterGuardian;
import am2.container.ContainerArmorInfuser;
import am2.container.ContainerInscriptionTable;
import am2.container.ContainerKeystoneChest;
import am2.container.ContainerKeystoneLockable;
import am2.container.ContainerObelisk;
import am2.container.ContainerRiftStorage;
import am2.container.ContainerSpellBook;
import am2.container.ContainerSpellCustomization;
import am2.defs.BlockDefs;
import am2.defs.CreativeTabsDefs;
import am2.defs.ItemDefs;
import am2.defs.LoreDefs;
import am2.defs.PotionEffectsDefs;
import am2.defs.SkillDefs;
import am2.defs.SpellDefs;
import am2.enchantments.AMEnchantments;
import am2.entity.EntityAirSled;
import am2.entity.EntityBoundArrow;
import am2.entity.EntityBroom;
import am2.entity.EntityDarkMage;
import am2.entity.EntityDarkling;
import am2.entity.EntityDryad;
import am2.entity.EntityEarthElemental;
import am2.entity.EntityFireElemental;
import am2.entity.EntityHecate;
import am2.entity.EntityLightMage;
import am2.entity.EntityManaCreeper;
import am2.entity.EntityManaElemental;
import am2.entity.EntityManaVortex;
import am2.entity.EntityRiftStorage;
import am2.entity.EntityShockwave;
import am2.entity.EntitySpellEffect;
import am2.entity.EntitySpellProjectile;
import am2.entity.EntityThrownRock;
import am2.entity.EntityThrownSickle;
import am2.entity.EntityWaterElemental;
import am2.entity.EntityWhirlwind;
import am2.entity.EntityWinterGuardianArm;
import am2.extensions.RiftStorage;
import am2.handler.EntityHandler;
import am2.handler.PotionEffectHandler;
import am2.items.ContainerKeystone;
import am2.items.ItemKeystone;
import am2.items.ItemSpellBook;
import am2.lore.CompendiumUnlockHandler;
import am2.packet.AMNetHandler;
import am2.packet.AMPacketProcessorServer;
import am2.particles.ParticleManagerServer;
import am2.power.PowerNodeCache;
import am2.power.PowerNodeEntry;
import am2.power.PowerTypes;
import am2.proxy.tick.ServerTickHandler;
import am2.trackers.ItemFrameWatcher;
import am2.trackers.PlayerTracker;
import am2.utils.InventoryUtilities;
import am2.utils.NPCSpells;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;


public class CommonProxy implements IGuiHandler{

	public ParticleManagerServer particleManager;
	protected ServerTickHandler serverTickHandler;
	protected AMPacketProcessorServer packetProcessor;
	private HashMap<EntityLivingBase, ArrayList<PotionEffect>> deferredPotionEffects = new HashMap<>();
	private HashMap<EntityLivingBase, Integer> deferredDimensionTransfers = new HashMap<>();
	public ItemDefs items;
	public BlockDefs blocks;
	public AMEnchantments enchantments;
	
	public PlayerTracker playerTracker;
	public ItemFrameWatcher itemFrameWatcher;
	
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
		MinecraftForge.EVENT_BUS.register(new AffinityAbilityHelper());
		MinecraftForge.EVENT_BUS.register(packetProcessor);
		MinecraftForge.EVENT_BUS.register(PowerNodeCache.instance);
		MinecraftForge.EVENT_BUS.register(new ArmorEventHandler());
		MinecraftForge.EVENT_BUS.register(playerTracker);
		
		registerInfusions();
		
		EntityRegistry.registerModEntity(EntitySpellProjectile.class, "SpellProjectile", 0, ArsMagica2.instance, 64, 2, true);
		EntityRegistry.registerModEntity(EntityRiftStorage.class, "RiftStorage", 1, ArsMagica2.instance, 64, 2, false);
		EntityRegistry.registerModEntity(EntitySpellEffect.class, "SpellEffect", 2, ArsMagica2.instance, 64, 2, true);
		EntityRegistry.registerModEntity(EntityThrownRock.class, "ThrownRock", 3, ArsMagica2.instance, 64, 2, true);
		EntityRegistry.registerModEntity(EntityBoundArrow.class, "BoundArrow", 4, ArsMagica2.instance, 64, 2, true);
		EntityRegistry.registerModEntity(EntityDarkling.class, "Darkling", 5, ArsMagica2.instance, 64, 2, true);
		EntityRegistry.registerModEntity(EntityDarkMage.class, "DarkMage", 6, ArsMagica2.instance, 64, 2, true);
		EntityRegistry.registerModEntity(EntityDryad.class, "Dryad", 7, ArsMagica2.instance, 64, 2, true);
		EntityRegistry.registerModEntity(EntityEarthElemental.class, "EarthElemental", 8, ArsMagica2.instance, 64, 2, true);
		EntityRegistry.registerModEntity(EntityFireElemental.class, "FireElemental", 9, ArsMagica2.instance, 64, 2, true);
		EntityRegistry.registerModEntity(EntityLightMage.class, "LightMage", 10, ArsMagica2.instance, 64, 2, true);
		EntityRegistry.registerModEntity(EntityManaElemental.class, "ManaElemental", 11, ArsMagica2.instance, 64, 2, true);
		EntityRegistry.registerModEntity(EntityManaVortex.class, "ManaVortex", 12, ArsMagica2.instance, 64, 2, true);
		EntityRegistry.registerModEntity(EntityShockwave.class, "Shockwave", 13, ArsMagica2.instance, 64, 2, true);
		EntityRegistry.registerModEntity(EntityThrownSickle.class, "ThrownSickle", 14, ArsMagica2.instance, 64, 2, true);
		EntityRegistry.registerModEntity(EntityWhirlwind.class, "Whirlwind", 15, ArsMagica2.instance, 64, 2, true);
		EntityRegistry.registerModEntity(EntityWinterGuardianArm.class, "WinterGuardianArm", 16, ArsMagica2.instance, 64, 2, true);
		
		EntityRegistry.registerModEntity(EntityAirGuardian.class, "AirGuardian", 17, ArsMagica2.instance, 64, 2, true);
		EntityRegistry.registerModEntity(EntityArcaneGuardian.class, "ArcaneGuardian", 18, ArsMagica2.instance, 64, 2, true);
		EntityRegistry.registerModEntity(EntityEarthGuardian.class, "EarthGuardian", 19, ArsMagica2.instance, 64, 2, true);
		EntityRegistry.registerModEntity(EntityEnderGuardian.class, "EnderGuardian", 20, ArsMagica2.instance, 64, 2, true);
		EntityRegistry.registerModEntity(EntityFireGuardian.class, "FireGuardian", 21, ArsMagica2.instance, 64, 2, true);
		EntityRegistry.registerModEntity(EntityLifeGuardian.class, "LifeGuardian", 22, ArsMagica2.instance, 64, 2, true);
		EntityRegistry.registerModEntity(EntityLightningGuardian.class, "LightningGuardian", 23, ArsMagica2.instance, 64, 2, true);
		EntityRegistry.registerModEntity(EntityNatureGuardian.class, "NatureGuardian", 24, ArsMagica2.instance, 64, 2, true);
		EntityRegistry.registerModEntity(EntityWaterGuardian.class, "WaterGuardian", 25, ArsMagica2.instance, 64, 2, true);
		EntityRegistry.registerModEntity(EntityWinterGuardian.class, "WinterGuardian", 26, ArsMagica2.instance, 64, 2, true);
		
		EntityRegistry.registerModEntity(EntityAirSled.class, "AirSled", 27, ArsMagica2.instance, 64, 2, true);
		EntityRegistry.registerModEntity(EntityBroom.class, "Broom", 28, ArsMagica2.instance, 64, 2, true);
		EntityRegistry.registerModEntity(EntityWaterElemental.class, "WaterElemental", 29, ArsMagica2.instance, 64, 2, true);
		EntityRegistry.registerModEntity(EntityManaCreeper.class, "ManaCreeper", 30, ArsMagica2.instance, 64, 2, true);
		EntityRegistry.registerModEntity(EntityHecate.class, "Hecate", 31, ArsMagica2.instance, 64, 2, true);
		
		initializeSpawns();
		
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

		CapabilityManager.INSTANCE.register(IEntityExtension.class, new IEntityExtension.Storage(), new IEntityExtension.Factory());
		CapabilityManager.INSTANCE.register(IAffinityData.class, new IAffinityData.Storage(), new IAffinityData.Factory());
		CapabilityManager.INSTANCE.register(ISkillData.class, new ISkillData.Storage(), new ISkillData.Factory());
		CapabilityManager.INSTANCE.register(IRiftStorage.class, new IRiftStorage.Storage(), new IRiftStorage.Factory());
		CapabilityManager.INSTANCE.register(IArcaneCompendium.class, new IArcaneCompendium.Storage(), new IArcaneCompendium.Factory());
		
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
	}
	
	public void postInit() {
		playerTracker.postInit();
		MinecraftForge.EVENT_BUS.register(playerTracker);		
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
	
	public void initializeSpawns(){
		BiomeDictionary.registerAllBiomes();

		//SpawnListEntry wisps = new SpawnListEntry(EntityWisp.class, 1, 1, 1);
		SpawnListEntry manaElementals = new SpawnListEntry(EntityManaElemental.class, ArsMagica2.config.GetManaElementalSpawnRate(), 1, 1);
		SpawnListEntry dryads = new SpawnListEntry(EntityDryad.class, ArsMagica2.config.GetDryadSpawnRate(), 1, 2);
		SpawnListEntry hecates_nonHell = new SpawnListEntry(EntityHecate.class, ArsMagica2.config.GetHecateSpawnRate(), 1, 1);
		SpawnListEntry hecates_hell = new SpawnListEntry(EntityHecate.class, ArsMagica2.config.GetHecateSpawnRate() * 2, 1, 2);
		SpawnListEntry manaCreepers = new SpawnListEntry(EntityManaCreeper.class, ArsMagica2.config.GetManaCreeperSpawnRate(), 1, 1);
		SpawnListEntry lightMages = new SpawnListEntry(EntityLightMage.class, ArsMagica2.config.GetMageSpawnRate(), 1, 3);
		SpawnListEntry darkMages = new SpawnListEntry(EntityDarkMage.class, ArsMagica2.config.GetMageSpawnRate(), 1, 3);
		SpawnListEntry waterElementals = new SpawnListEntry(EntityWaterElemental.class, ArsMagica2.config.GetWaterElementalSpawnRate(), 1, 3);
		SpawnListEntry darklings = new SpawnListEntry(EntityDarkling.class, ArsMagica2.config.GetDarklingSpawnRate(), 4, 8);
		SpawnListEntry earthElementals = new SpawnListEntry(EntityEarthElemental.class, ArsMagica2.config.GetEarthElementalSpawnRate(), 1, 2);
		SpawnListEntry fireElementals = new SpawnListEntry(EntityFireElemental.class, ArsMagica2.config.GetFireElementalSpawnRate(), 1, 1);

		initSpawnsForBiomeTypes(manaElementals, EnumCreatureType.MONSTER, new Type[]{Type.BEACH, Type.DRY, Type.FOREST, Type.COLD, Type.HILLS, Type.JUNGLE, Type.MAGICAL, Type.MOUNTAIN, Type.PLAINS, Type.SWAMP, Type.WASTELAND}, new Type[]{Type.END, Type.NETHER, Type.MUSHROOM});

		initSpawnsForBiomeTypes(dryads, EnumCreatureType.CREATURE, new Type[]{Type.BEACH, Type.FOREST, Type.MAGICAL, Type.HILLS, Type.JUNGLE, Type.MOUNTAIN, Type.PLAINS}, new Type[]{Type.END, Type.COLD, Type.MUSHROOM, Type.NETHER, Type.WASTELAND, Type.SWAMP, Type.DRY});

		initSpawnsForBiomeTypes(hecates_nonHell, EnumCreatureType.MONSTER, new Type[]{Type.BEACH, Type.DRY, Type.FOREST, Type.COLD, Type.HILLS, Type.JUNGLE, Type.MAGICAL, Type.MOUNTAIN, Type.PLAINS, Type.SWAMP, Type.WASTELAND}, new Type[]{Type.END, Type.NETHER, Type.MUSHROOM});

		initSpawnsForBiomeTypes(hecates_hell, EnumCreatureType.MONSTER, new Type[]{Type.NETHER}, new Type[]{Type.MUSHROOM});

		initSpawnsForBiomeTypes(darklings, EnumCreatureType.MONSTER, new Type[]{Type.NETHER}, new Type[]{Type.MUSHROOM});

		initSpawnsForBiomeTypes(manaCreepers, EnumCreatureType.MONSTER, new Type[]{Type.BEACH, Type.DRY, Type.FOREST, Type.COLD, Type.HILLS, Type.JUNGLE, Type.MAGICAL, Type.MOUNTAIN, Type.PLAINS, Type.SWAMP, Type.WASTELAND}, new Type[]{Type.END, Type.NETHER, Type.MUSHROOM});

		initSpawnsForBiomeTypes(lightMages, EnumCreatureType.MONSTER, new Type[]{Type.BEACH, Type.DRY, Type.FOREST, Type.COLD, Type.HILLS, Type.JUNGLE, Type.MAGICAL, Type.MOUNTAIN, Type.PLAINS, Type.SWAMP, Type.WASTELAND}, new Type[]{Type.END, Type.NETHER, Type.MUSHROOM});

		initSpawnsForBiomeTypes(darkMages, EnumCreatureType.MONSTER, new Type[]{Type.BEACH, Type.DRY, Type.FOREST, Type.COLD, Type.HILLS, Type.JUNGLE, Type.MAGICAL, Type.MOUNTAIN, Type.PLAINS, Type.SWAMP, Type.WASTELAND}, new Type[]{Type.END, Type.NETHER, Type.MUSHROOM});

		initSpawnsForBiomeTypes(waterElementals, EnumCreatureType.MONSTER, new Type[]{Type.WATER}, new Type[]{Type.END, Type.NETHER, Type.MUSHROOM});
		initSpawnsForBiomeTypes(waterElementals, EnumCreatureType.WATER_CREATURE, new Type[]{Type.WATER}, new Type[]{Type.END, Type.NETHER, Type.MUSHROOM});

		initSpawnsForBiomeTypes(earthElementals, EnumCreatureType.MONSTER, new Type[]{Type.HILLS, Type.MOUNTAIN}, new Type[]{Type.MUSHROOM});
		initSpawnsForBiomeTypes(fireElementals, EnumCreatureType.MONSTER, new Type[]{Type.NETHER}, new Type[]{Type.MUSHROOM});

	}

	private void initSpawnsForBiomeTypes(SpawnListEntry spawnListEntry, EnumCreatureType creatureType, Type[] types, Type[] exclusions){
		if (spawnListEntry.itemWeight == 0){
			ArsMagica2.LOGGER.info("Skipping spawn list entry for %s (as type %s), as the weight is set to 0.  This can be changed in config.", spawnListEntry.entityClass.getName(), creatureType.toString());
			return;
		}
		for (Type type : types){
			initSpawnsForBiomes(BiomeDictionary.getBiomesForType(type), spawnListEntry, creatureType, exclusions);
		}
	}

	private void initSpawnsForBiomes(Biome[] biomes, SpawnListEntry spawnListEntry, EnumCreatureType creatureType, Type[] exclusions){
		if (biomes == null) return;
		for (Biome biome : biomes){
			if (biomeIsExcluded(biome, exclusions)) continue;
			if (!biome.getSpawnableList(creatureType).contains(spawnListEntry))
				biome.getSpawnableList(creatureType).add(spawnListEntry);
		}
	}

	private boolean biomeIsExcluded(Biome biome, Type[] exclusions){

		Type biomeTypes[] = BiomeDictionary.getTypesForBiome(biome);

		for (Type exclusion : exclusions){
			for (Type biomeType : biomeTypes){
				if (biomeType == exclusion) return true;
			}
		}
		return false;
	}
}
