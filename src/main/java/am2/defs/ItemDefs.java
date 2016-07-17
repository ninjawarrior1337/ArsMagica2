package am2.defs;

import java.util.Iterator;

import am2.api.ArsMagicaAPI;
import am2.api.affinity.Affinity;
import am2.armor.ItemMagitechGoggles;
import am2.items.InscriptionTableUpgrade;
import am2.items.ItemAffinityTome;
import am2.items.ItemArcaneCompendium;
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
import am2.items.ItemOre;
import am2.items.ItemRune;
import am2.items.ItemSpellComponent;
import am2.items.SpellBase;
import am2.items.rendering.CrystalWrenchRenderer;
import am2.items.rendering.DefaultWithMetaRenderer;
import am2.items.rendering.IgnoreMetadataRenderer;
import am2.items.rendering.SpellRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemSword;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemDefs {
	
	public static final ToolMaterial BOUND = EnumHelper.addToolMaterial("BOUND", 3, 1561, 8.0F, 3.0F, 10);
	
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
	public static final Item magitechGoggles = new ItemMagitechGoggles(0).registerAndName("magitechGoggles");
	
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
	public static final ItemSword BoundSword = new ItemBoundSword().registerAndName("bound_sword");
	public static final ItemAxe BoundAxe = new ItemBoundAxe().registerAndName("bound_axe");
	public static final ItemPickaxe BoundPickaxe = new ItemBoundPickaxe().registerAndName("bound_pickaxe");
	public static final ItemSpade BoundShovel = new ItemBoundShovel().registerAndName("bound_shovel");
	public static final ItemHoe BoundHoe = new ItemBoundHoe().registerAndName("bound_hoe");
	public static final ItemBow BoundBow = new ItemBoundBow().registerAndName("bound_bow");
	public static final ItemShield BoundShield = new ItemBoundShield().registerAndName("bound_shield");

	public static final ItemArrow BoundArrow = new ItemBoundArrow().registerAndName("bound_arrow");

	public static final Item waterGuardianOrbs = null;

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
		
		RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
		
		renderItem.getItemModelMesher().register(crystalWrench, new CrystalWrenchRenderer(crystalWrench));
		Item essence = GameRegistry.findRegistry(Item.class).getValue(new ResourceLocation("arsmagica2", "essence"));
		Iterator<Affinity> iter = ArsMagicaAPI.getAffinityRegistry().getValues().iterator();
		int effMeta = 0;
		for (int i = 0; i < ArsMagicaAPI.getAffinityRegistry().getValues().size(); i++) {
			if (!iter.hasNext())
				break;
			Affinity entry = iter.next();
			ModelBakery.registerItemVariants(affinityTome, new ModelResourceLocation("arsmagica2:affinity_tome_" + entry.getName(), "inventory"));
			renderItem.getItemModelMesher().register(affinityTome, i, new ModelResourceLocation("arsmagica2:affinity_tome_" + entry.getName(), "inventory"));
			if (entry.equals(Affinity.NONE)) {
				continue;
			}
			ModelBakery.registerItemVariants(essence, new ModelResourceLocation("arsmagica2:essence_" + entry.getName(), "inventory"));
			renderItem.getItemModelMesher().register(essence, effMeta, new ModelResourceLocation("arsmagica2:essence_" + entry.getName(), "inventory"));
			effMeta++;
		}
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

	}
	
	private static void registerTexture(Item item) {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, new IgnoreMetadataRenderer( new ModelResourceLocation(item.getRegistryName(), "inventory")));
	}
}
