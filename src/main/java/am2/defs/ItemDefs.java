package am2.defs;

import java.util.Map.Entry;

import am2.api.SkillPointRegistry;
import am2.api.skill.SkillPoint;
import am2.armor.ArsMagicaArmorMaterial;
import am2.armor.ItemEarthGuardianArmor;
import am2.armor.ItemEnderBoots;
import am2.armor.ItemFireGuardianEars;
import am2.armor.ItemMagitechGoggles;
import am2.armor.ItemWaterGuardianOrbs;
import am2.enchantments.AMEnchantmentHelper;
import am2.items.InscriptionTableUpgrade;
import am2.items.ItemAffinityTome;
import am2.items.ItemAirSled;
import am2.items.ItemArcaneCompendium;
import am2.items.ItemArcaneGuardianSpellbook;
import am2.items.ItemArsMagica;
import am2.items.ItemBindingCatalyst;
import am2.items.ItemBoundArrow;
import am2.items.ItemBoundAxe;
import am2.items.ItemBoundBow;
import am2.items.ItemBoundHoe;
import am2.items.ItemBoundPickaxe;
import am2.items.ItemBoundShield;
import am2.items.ItemBoundShovel;
import am2.items.ItemBoundSword;
import am2.items.ItemChalk;
import am2.items.ItemCore;
import am2.items.ItemCrystalPhylactery;
import am2.items.ItemCrystalWrench;
import am2.items.ItemEssence;
import am2.items.ItemFlickerFocus;
import am2.items.ItemFlickerJar;
import am2.items.ItemFocusCharge;
import am2.items.ItemFocusCreature;
import am2.items.ItemFocusGreater;
import am2.items.ItemFocusItem;
import am2.items.ItemFocusLesser;
import am2.items.ItemFocusMana;
import am2.items.ItemFocusMob;
import am2.items.ItemFocusPlayer;
import am2.items.ItemFocusStandard;
import am2.items.ItemInfinityOrb;
import am2.items.ItemKeystone;
import am2.items.ItemKeystoneDoor;
import am2.items.ItemLifeWard;
import am2.items.ItemLightningCharm;
import am2.items.ItemLiquidEssenceBottle;
import am2.items.ItemLostJournal;
import am2.items.ItemMagicBroom;
import am2.items.ItemManaCake;
import am2.items.ItemNatureGuardianSickle;
import am2.items.ItemOre;
import am2.items.ItemRune;
import am2.items.ItemRuneBag;
import am2.items.ItemSpellBook;
import am2.items.ItemSpellComponent;
import am2.items.ItemSpellStaff;
import am2.items.ItemWinterGuardianArm;
import am2.items.SpellBase;
import am2.items.colorizers.CrystalPhylacteryColorizer;
import am2.items.colorizers.FlickerJarColorizer;
import am2.items.colorizers.LostJournalColorizer;
import am2.items.rendering.AffinityRenderer;
import am2.items.rendering.CrystalWrenchRenderer;
import am2.items.rendering.DefaultWithMetaRenderer;
import am2.items.rendering.FlickerOperatorRenderer;
import am2.items.rendering.IgnoreMetadataRenderer;
import am2.items.rendering.SpellRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemDefs {
	
	public static final ToolMaterial BOUND = EnumHelper.addToolMaterial("BOUND", 3, 1561, 8.0F, 3.0F, 10);
	
	private static final ArmorMaterial MAGITECH_GOOGLES = EnumHelper.addArmorMaterial("magitech_goggles", "arsmagica2:magitech_goggles", 5, new int[]{1, 2, 3, 1}, 15, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0.0F);
	private static final ArmorMaterial ENDER = EnumHelper.addArmorMaterial("ender", "arsmagica2:ender", 7, new int[]{1, 3, 5, 2}, 25, SoundEvents.ITEM_ARMOR_EQUIP_GOLD, 0.0F);
	
	public static final ItemArsMagica spellParchment = new ItemArsMagica().registerAndName("spell_parchment");
	public static final ItemArsMagica affinityTome = new ItemAffinityTome().registerAndName("tome");
	public static final ItemArsMagica itemOre = new ItemOre().registerAndName("itemOre");
	public static final ItemArsMagica essence = new ItemEssence().registerAndName("essence");
	public static final ItemArsMagica rune = new ItemRune().registerAndName("rune");
	
	public static final ItemArsMagica mobFocus = new ItemFocusMob().registerAndName("mobFocus");
	public static final ItemArsMagica lesserFocus = new ItemFocusLesser().registerAndName("lesserFocus");
	public static final ItemArsMagica standardFocus = new ItemFocusStandard().registerAndName("standardFocus");
	public static final ItemArsMagica manaFocus = new ItemFocusMana().registerAndName("manaFocus");
	public static final ItemArsMagica greaterFocus = new ItemFocusGreater().registerAndName("greaterFocus");
	public static final ItemArsMagica chargeFocus = new ItemFocusCharge().registerAndName("chargeFocus");
	public static final ItemArsMagica itemFocus = new ItemFocusItem().registerAndName("itemFocus");
	public static final ItemArsMagica playerFocus = new ItemFocusPlayer().registerAndName("playerFocus");
	public static final ItemArsMagica creatureFocus = new ItemFocusCreature().registerAndName("creatureFocus");
	public static final ItemArsMagica arcaneCompendium = new ItemArcaneCompendium().registerAndName("arcaneCompendium");
	public static final ItemArsMagica crystalWrench = new ItemCrystalWrench().registerAndName("crystalWrench");
	public static final Item magitechGoggles = new ItemMagitechGoggles(MAGITECH_GOOGLES, 0).registerAndName("magitechGoggles");
	
	// PlaceHolder items
	public static final ItemArsMagica spell_component = new ItemSpellComponent().registerAndName("spellComponent");
	public static final Item etherium = new ItemArsMagica().registerAndName("etherium").setCreativeTab(null);
	
	public static final ItemArsMagica blankRune = new ItemArsMagica().registerAndName("blankRune");
	public static final ItemArsMagica chalk = new ItemChalk().registerAndName("chalk");
	public static final ItemArsMagica spellStaffMagitech = new ItemSpellStaff(0, -1).registerAndName("spell_staff_magitech");
	public static final ItemArsMagica flickerFocus = new ItemFlickerFocus().registerAndName("flickerFocus");
	public static final ItemFlickerJar flickerJar = (ItemFlickerJar) new ItemFlickerJar().registerAndName("flickerJar");
	public static final ItemArsMagica evilBook = new ItemArsMagica().registerAndName("evilBook");
	public static final ItemArsMagica wardingCandle = null;
	public static final ItemArsMagica liquidEssenceBottle = new ItemLiquidEssenceBottle().registerAndName("liquid_essence_bottle");
	public static final ItemArsMagica bindingCatalyst = new ItemBindingCatalyst().registerAndName("binding_catalyst");
	public static final ItemArsMagica inscriptionUpgrade = new InscriptionTableUpgrade().registerAndName("inscription_upgrade");
	public static final ItemArsMagica infinityOrb = new ItemInfinityOrb().registerAndName("infinityOrb");
	public static final ItemSword BoundSword = new ItemBoundSword().registerAndName("bound_sword");
	public static final ItemAxe BoundAxe = new ItemBoundAxe().registerAndName("bound_axe");
	public static final ItemPickaxe BoundPickaxe = new ItemBoundPickaxe().registerAndName("bound_pickaxe");
	public static final ItemSpade BoundShovel = new ItemBoundShovel().registerAndName("bound_shovel");
	public static final ItemHoe BoundHoe = new ItemBoundHoe().registerAndName("bound_hoe");
	public static final ItemBow BoundBow = new ItemBoundBow().registerAndName("bound_bow");
	public static final ItemShield BoundShield = new ItemBoundShield().registerAndName("bound_shield");
	public static final ItemManaCake manaCake = new ItemManaCake().registerAndName("manaCake");
	public static final Item woodenLeg = new ItemArsMagica().registerAndName("woodenLeg");

	public static final ItemArrow BoundArrow = new ItemBoundArrow().registerAndName("bound_arrow");

	public static final Item natureScythe = new ItemNatureGuardianSickle().registerAndName("nature_scythe");
	public static final Item winterArm = new ItemWinterGuardianArm().registerAndName("winter_arm");
	public static final Item airSled = new ItemAirSled().registerAndName("air_sled");
	public static final Item arcaneSpellbook = new ItemArcaneGuardianSpellbook().registerAndName("arcane_spellbook");
	public static final Item earthArmor = new ItemEarthGuardianArmor(ArmorMaterial.GOLD, ArsMagicaArmorMaterial.UNIQUE, 0, EntityEquipmentSlot.CHEST).registerAndName("earth_armor");
	public static final Item enderBoots = new ItemEnderBoots(ENDER, ArsMagicaArmorMaterial.UNIQUE, 0, EntityEquipmentSlot.FEET).registerAndName("ender_boots");
	public static final Item fireEars = new ItemFireGuardianEars(ArmorMaterial.GOLD, ArsMagicaArmorMaterial.UNIQUE, 0, EntityEquipmentSlot.HEAD).registerAndName("fire_ears");
	public static final Item lifeWard = new ItemLifeWard().registerAndName("life_ward");
	public static final Item lightningCharm = new ItemLightningCharm().registerAndName("lightning_charm");
	public static final Item waterOrbs = new ItemWaterGuardianOrbs(ArmorMaterial.GOLD, ArsMagicaArmorMaterial.UNIQUE, 0, EntityEquipmentSlot.LEGS).registerAndName("water_orbs");	
	public static final ItemSpellBook spellBook = (ItemSpellBook) new ItemSpellBook().registerAndName("spell_book");

	public static ItemStack natureScytheEnchanted;
	public static ItemStack winterArmEnchanted;
	public static ItemStack airSledEnchanted;
	public static ItemStack arcaneSpellBookEnchanted;
	public static ItemStack earthArmorEnchanted;
	public static ItemStack enderBootsEnchanted;
	public static ItemStack fireEarsEnchanted;
	public static ItemStack lifeWardEnchanted;
	public static ItemStack lightningCharmEnchanted;
	public static ItemStack waterOrbsEnchanted;

	public static final Item magicBroom = new ItemMagicBroom().registerAndName("magic_broom");
	public static final ItemKeystone keystone = (ItemKeystone) new ItemKeystone().registerAndName("keystone");
	public static final Item itemKeystoneDoor = new ItemKeystoneDoor().registerAndName("item_keystone_door");
	public static final ItemRuneBag runeBag = (ItemRuneBag) new ItemRuneBag().registerAndName("rune_bag");

	public static final ItemLostJournal lostJournal = new ItemLostJournal().registerAndName("lostJournal");
	public static final ItemCrystalPhylactery crystalPhylactery = (ItemCrystalPhylactery) new ItemCrystalPhylactery().registerAndName("crystalPhylactery");

	public static final Item core = new ItemCore().registerAndName("core");
	public static final Item deficitCrystal = new ItemArsMagica().registerAndName("deficit_crystal");

	public static final Item workbenchUpgrade = new ItemArsMagica().registerAndName("workbench_upgrade");



	public static SpellBase spell = new SpellBase().registerAndName("spell");
	
	@SideOnly(Side.CLIENT)
	public static void initClient () {
		//Focus
		registerTexture(mobFocus);
		registerTexture(lesserFocus);
		registerTexture(standardFocus);
		registerTexture(manaFocus);
		registerTexture(greaterFocus);
		registerTexture(chargeFocus);
		registerTexture(itemFocus);
		registerTexture(playerFocus);
		registerTexture(creatureFocus);
		registerTexture(evilBook);
		
		registerTexture(spellParchment);
		
		registerTexture(arcaneCompendium);
		registerTexture(blankRune);
		registerTexture(spellStaffMagitech);
		
		registerTexture(magitechGoggles);
		registerTexture(etherium);
		registerTexture(chalk);
		registerTexture(BoundSword);
		registerTexture(BoundAxe);
		registerTexture(BoundPickaxe);
		registerTexture(BoundShovel);
		registerTexture(BoundHoe);
		registerTexture(BoundBow);
		registerTexture(BoundShield);
		registerTexture(liquidEssenceBottle);
		
		registerTexture(natureScythe);
		registerTexture(winterArm);
		registerTexture(airSled);
		registerTexture(arcaneSpellbook);
		registerTexture(earthArmor);
		registerTexture(enderBoots);
		registerTexture(fireEars);
		registerTexture(lifeWard);
		registerTexture(lightningCharm);
		registerTexture(waterOrbs);
		
		registerTexture(keystone);
		registerTexture(magicBroom);
		
		registerTexture(manaCake);
		registerTexture(flickerJar, 0, "_empty");
		
		registerTexture(itemKeystoneDoor);
		registerTexture(spellBook);
		registerTexture(runeBag);
		registerTexture(woodenLeg);
		registerTexture(manaCake);
		registerTexture(lostJournal);
		registerTexture(crystalPhylactery);
		registerTexture(deficitCrystal);
		
		Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new FlickerJarColorizer(), flickerJar);
		Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new LostJournalColorizer(), lostJournal);
		Minecraft.getMinecraft().getItemColors().registerItemColorHandler(new CrystalPhylacteryColorizer(), crystalPhylactery);
		
		RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
		
		renderItem.getItemModelMesher().register(crystalWrench, new CrystalWrenchRenderer(crystalWrench));
		renderItem.getItemModelMesher().register(flickerFocus, new FlickerOperatorRenderer().addModels(flickerFocus));
//		Iterator<Affinity> iter = ArsMagicaAPI.getAffinityRegistry().getValues().iterator();
//		int effMeta = 0;
//		for (int i = 0; i < ArsMagicaAPI.getAffinityRegistry().getValues().size(); i++) {
//			if (!iter.hasNext())
//				break;
//			Affinity entry = iter.next();
//			ModelBakery.registerItemVariants(affinityTome, new ModelResourceLocation("arsmagica2:affinity_tome_" + entry.getName(), "inventory"));
//			renderItem.getItemModelMesher().register(affinityTome, i, new ModelResourceLocation("arsmagica2:affinity_tome_" + entry.getName(), "inventory"));
//			if (entry.equals(Affinity.NONE)) {
//				continue;
//			}
//			ModelBakery.registerItemVariants(essence, new ModelResourceLocation("arsmagica2:essence_" + entry.getName(), "inventory"));
//			renderItem.getItemModelMesher().register(essence, effMeta, new ModelResourceLocation("arsmagica2:essence_" + entry.getName(), "inventory"));
//			effMeta++;
//		}
		renderItem.getItemModelMesher().register(essence, new AffinityRenderer("essence_").addModels(essence));
		renderItem.getItemModelMesher().register(affinityTome, new AffinityRenderer("affinity_tome_").addModels(affinityTome));
		for (int i = 0; i < 16; i++) {
			ModelBakery.registerItemVariants(rune, new ModelResourceLocation("arsmagica2:rune_" + EnumDyeColor.byDyeDamage(i).getName().toLowerCase(), "inventory"));
			renderItem.getItemModelMesher().register(rune, i, new ModelResourceLocation("arsmagica2:rune_" + EnumDyeColor.byDyeDamage(i).getName().toLowerCase(), "inventory"));
		}
		for (int i = 0; i < ItemOre.names.length; i++) {
			ModelResourceLocation loc = new ModelResourceLocation(new ResourceLocation("arsmagica2", "item_ore_" + ItemOre.names[i]), "inventory");
			ModelBakery.registerItemVariants(itemOre, loc);
			renderItem.getItemModelMesher().register(itemOre, i, loc);
		}
		DefaultWithMetaRenderer crystalPhylacteryRenderer = new DefaultWithMetaRenderer(new ModelResourceLocation(crystalPhylactery.getRegistryName(), "inventory"));
		for(int i = 1; i < 4; i++) {
			ModelResourceLocation loc = new ModelResourceLocation(crystalPhylactery.getRegistryName().toString() + (i == ItemCrystalPhylactery.META_QUARTER ? "_quarter" : (i == ItemCrystalPhylactery.META_HALF ? "_half" : "_full")), "inventory");
			ModelBakery.registerItemVariants(crystalPhylactery, loc);
			crystalPhylacteryRenderer.addModel(i, loc);
		}
		ModelBakery.registerItemVariants(crystalPhylactery, new ModelResourceLocation(crystalPhylactery.getRegistryName(), "inventory"));
		renderItem.getItemModelMesher().register(crystalPhylactery, crystalPhylacteryRenderer);
		
		DefaultWithMetaRenderer catalystRenderer = new DefaultWithMetaRenderer(new ModelResourceLocation(bindingCatalyst.getRegistryName(), "inventory"));
		for (int i = 0; i < 7; i++) {
			ModelResourceLocation loc = new ModelResourceLocation(bindingCatalyst.getRegistryName() + "_" + ItemBindingCatalyst.NAMES[i], "inventory");
			ModelBakery.registerItemVariants(bindingCatalyst, loc);
			catalystRenderer.addModel(i, loc);
		}
		ModelBakery.registerItemVariants(bindingCatalyst, new ModelResourceLocation(bindingCatalyst.getRegistryName(), "inventory"));
		
		DefaultWithMetaRenderer inforbRenderer = new DefaultWithMetaRenderer(new ModelResourceLocation(infinityOrb.getRegistryName(), "inventory"));
		for (Entry<Integer, SkillPoint> entry : SkillPointRegistry.getSkillPointMap().entrySet()) {
			if (entry.getKey().intValue() < 0)
				continue;
			ModelResourceLocation loc = new ModelResourceLocation(infinityOrb.getRegistryName() + "_" + entry.getValue().toString().toLowerCase(), "inventory");
			ModelBakery.registerItemVariants(infinityOrb, loc);
			inforbRenderer.addModel(entry.getKey(), loc);
		}
		renderItem.getItemModelMesher().register(infinityOrb, inforbRenderer);
		
		renderItem.getItemModelMesher().register(bindingCatalyst, catalystRenderer);
		for (int i = 0; i < 3; i++) {
			ModelResourceLocation loc = new ModelResourceLocation(inscriptionUpgrade.getRegistryName() + "_" + (i + 1), "inventory");
			ModelBakery.registerItemVariants(inscriptionUpgrade, loc);
			renderItem.getItemModelMesher().register(inscriptionUpgrade, i, loc);
		}
		renderItem.getItemModelMesher().register(spell, new SpellRenderer());
		
		
		natureScytheEnchanted = AMEnchantmentHelper.soulbindStack(new ItemStack(natureScythe));
		arcaneSpellBookEnchanted = AMEnchantmentHelper.soulbindStack(new ItemStack(arcaneSpellbook));
		winterArmEnchanted = AMEnchantmentHelper.soulbindStack(new ItemStack(winterArm));
		fireEarsEnchanted = AMEnchantmentHelper.soulbindStack(new ItemStack(fireEars));
		earthArmorEnchanted = AMEnchantmentHelper.soulbindStack(new ItemStack(earthArmor));
		airSledEnchanted = AMEnchantmentHelper.soulbindStack(new ItemStack(airSled));
		waterOrbsEnchanted = AMEnchantmentHelper.soulbindStack(new ItemStack(waterOrbs));
		enderBootsEnchanted = AMEnchantmentHelper.soulbindStack(new ItemStack(enderBoots));
		lightningCharmEnchanted = AMEnchantmentHelper.soulbindStack(new ItemStack(lightningCharm));
		lifeWardEnchanted = AMEnchantmentHelper.soulbindStack(new ItemStack(lifeWard));

	}
	
	@SideOnly(Side.CLIENT)
	private static void registerTexture(Item item) {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, new IgnoreMetadataRenderer( new ModelResourceLocation(item.getRegistryName(), "inventory")));
	}
	
	@SideOnly(Side.CLIENT)
	private static void registerTexture(Item item, int meta, String suffix) {
		DefaultWithMetaRenderer renderer = new DefaultWithMetaRenderer(new ModelResourceLocation(item.getRegistryName(), "inventory"));
		renderer.addModel(meta, new ModelResourceLocation(item.getRegistryName().toString() + suffix, "inventory"));
		ModelBakery.registerItemVariants(item, new ModelResourceLocation(item.getRegistryName().toString() + suffix, "inventory"), new ModelResourceLocation(item.getRegistryName(), "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, renderer);
	}
}
