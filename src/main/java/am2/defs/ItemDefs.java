package am2.defs;

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
import am2.items.ItemArsMagica2;
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
import am2.items.ItemCrystalWrench;
import am2.items.ItemEssence;
import am2.items.ItemInfinityOrb;
import am2.items.ItemKeystone;
import am2.items.ItemKeystoneDoor;
import am2.items.ItemLifeWard;
import am2.items.ItemLightningCharm;
import am2.items.ItemMagicBroom;
import am2.items.ItemNatureGuardianSickle;
import am2.items.ItemOre;
import am2.items.ItemRune;
import am2.items.ItemRuneBag;
import am2.items.ItemSpellBook;
import am2.items.ItemSpellComponent;
import am2.items.ItemWinterGuardianArm;
import am2.items.SpellBase;
import am2.items.rendering.AffinityRenderer;
import am2.items.rendering.CrystalWrenchRenderer;
import am2.items.rendering.DefaultWithMetaRenderer;
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

public class ItemDefs {
	
	public static final ToolMaterial BOUND = EnumHelper.addToolMaterial("BOUND", 3, 1561, 8.0F, 3.0F, 10);
	
	private static final ArmorMaterial MAGITECH_GOOGLES = EnumHelper.addArmorMaterial("magitech_goggles", "arsmagica2:magitech_goggles", 5, new int[]{1, 2, 3, 1}, 15, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0.0F);
	private static final ArmorMaterial ENDER = EnumHelper.addArmorMaterial("ender", "arsmagica2:ender", 7, new int[]{1, 3, 5, 2}, 25, SoundEvents.ITEM_ARMOR_EQUIP_GOLD, 0.0F);
	public static final ItemArsMagica2 spellParchment = new ItemArsMagica2().registerAndName("spell_parchment");
	public static final ItemArsMagica2 affinityTome = new ItemAffinityTome().registerAndName("tome");
	public static final ItemArsMagica2 itemOre = new ItemOre().registerAndName("itemOre");
	public static final ItemArsMagica2 essence = new ItemEssence().registerAndName("essence");
	public static final ItemArsMagica2 rune = new ItemRune().registerAndName("rune");
	
	public static final ItemArsMagica2 mobFocus = new ItemArsMagica2().registerAndName("mobFocus");
	public static final ItemArsMagica2 lesserFocus = new ItemArsMagica2().registerAndName("lesserFocus");
	public static final ItemArsMagica2 standardFocus = new ItemArsMagica2().registerAndName("standardFocus");
	public static final ItemArsMagica2 manaFocus = new ItemArsMagica2().registerAndName("manaFocus");
	public static final ItemArsMagica2 greaterFocus = new ItemArsMagica2().registerAndName("greaterFocus");
	public static final ItemArsMagica2 chargeFocus = new ItemArsMagica2().registerAndName("chargeFocus");
	public static final ItemArsMagica2 itemFocus = new ItemArsMagica2().registerAndName("itemFocus");
	public static final ItemArsMagica2 playerFocus = new ItemArsMagica2().registerAndName("playerFocus");
	public static final ItemArsMagica2 creatureFocus = new ItemArsMagica2().registerAndName("creatureFocus");
	public static final ItemArsMagica2 arcaneCompendium = new ItemArcaneCompendium().registerAndName("arcaneCompendium");
	public static final ItemArsMagica2 crystalWrench = new ItemCrystalWrench().registerAndName("crystalWrench");
	public static final Item magitechGoggles = new ItemMagitechGoggles(MAGITECH_GOOGLES, 0).registerAndName("magitechGoggles");
	
	// PlaceHolder items
	public static final ItemArsMagica2 spell_component = new ItemSpellComponent().registerAndName("spellComponent");
	public static final Item etherium = new ItemArsMagica2().registerAndName("etherium").setCreativeTab(null);
	
	public static final ItemArsMagica2 blankRune = new ItemArsMagica2().registerAndName("blankRune");
	public static final ItemArsMagica2 chalk = new ItemChalk().registerAndName("chalk");
	public static final ItemArsMagica2 spellStaffMagitech = null;
	public static final ItemArsMagica2 flickerFocus = null;
	public static final ItemArsMagica2 flickerJar = null;
	public static final ItemArsMagica2 evilBook = null;
	public static final ItemArsMagica2 wardingCandle = null;
	public static final ItemArsMagica2 liquidEssenceBottle = null;
	public static final ItemArsMagica2 bindingCatalyst = new ItemBindingCatalyst().registerAndName("binding_catalyst");
	public static final ItemArsMagica2 inscriptionUpgrade = new InscriptionTableUpgrade().registerAndName("inscription_upgrade");
	public static final ItemArsMagica2 infinityOrb = new ItemInfinityOrb().registerAndName("infinityOrb");
	public static final ItemSword BoundSword = new ItemBoundSword().registerAndName("bound_sword");
	public static final ItemAxe BoundAxe = new ItemBoundAxe().registerAndName("bound_axe");
	public static final ItemPickaxe BoundPickaxe = new ItemBoundPickaxe().registerAndName("bound_pickaxe");
	public static final ItemSpade BoundShovel = new ItemBoundShovel().registerAndName("bound_shovel");
	public static final ItemHoe BoundHoe = new ItemBoundHoe().registerAndName("bound_hoe");
	public static final ItemBow BoundBow = new ItemBoundBow().registerAndName("bound_bow");
	public static final ItemShield BoundShield = new ItemBoundShield().registerAndName("bound_shield");
	public static final Item manaCake = null;
	public static final Item woodenLeg = null;

	public static final ItemArrow BoundArrow = new ItemBoundArrow().registerAndName("bound_arrow");

	public static final Item natureScythe = new ItemNatureGuardianSickle().registerAndName("nature_scythe");
	public static final Item winterArm = new ItemWinterGuardianArm().registerAndName("winter_arm");
	public static final Item airSled = new ItemAirSled().registerAndName("air_sled");
	public static final Item arcaneSpellbook = new ItemArcaneGuardianSpellbook().registerAndName("arcane_spellbook");
	public static final Item earthArmor = new ItemEarthGuardianArmor(ArmorMaterial.GOLD, ArsMagicaArmorMaterial.UNIQUE, 0, EntityEquipmentSlot.CHEST).registerAndName("earth_armor");
	public static final Item enderBoots = new ItemEnderBoots(ENDER, ArsMagicaArmorMaterial.UNIQUE, 0, EntityEquipmentSlot.FEET).registerAndName("ender_boots");
	public static final Item fireEars = new ItemFireGuardianEars(ArmorMaterial.GOLD, ArsMagicaArmorMaterial.UNIQUE, 0, EntityEquipmentSlot.HEAD).registerAndName("fire_ears");
	public static final Item lifeWard = new ItemLifeWard().registerAndName("life_ward");
	public static final Item lightningCharm = new ItemLightningCharm().registerAndName("lighrining_charm");
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




	public static SpellBase spell = new SpellBase().registerAndName("spell");
	
	public static void init () {
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
		
		registerTexture(spellParchment);
		
		registerTexture(arcaneCompendium);
		registerTexture(blankRune);
		
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
		
		RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
		
		renderItem.getItemModelMesher().register(crystalWrench, new CrystalWrenchRenderer(crystalWrench));
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
		DefaultWithMetaRenderer catalystRenderer = new DefaultWithMetaRenderer(new ModelResourceLocation(bindingCatalyst.getRegistryName(), "inventory"));
		for (int i = 0; i < 7; i++) {
			ModelResourceLocation loc = new ModelResourceLocation(bindingCatalyst.getRegistryName() + "_" + ItemBindingCatalyst.NAMES[i], "inventory");
			ModelBakery.registerItemVariants(bindingCatalyst, loc);
			catalystRenderer.addModel(i, loc);
		}
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
	
	private static void registerTexture(Item item) {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, new IgnoreMetadataRenderer( new ModelResourceLocation(item.getRegistryName(), "inventory")));
	}
}
