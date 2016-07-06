package am2.defs;

import java.util.Iterator;
import java.util.Map.Entry;

import am2.affinity.Affinity;
import am2.api.AffinityRegistry;
import am2.armor.ItemMagitechGoggles;
import am2.items.ItemAffinityTome;
import am2.items.ItemArcaneCompendium;
import am2.items.ItemArsMagica2;
import am2.items.ItemChalk;
import am2.items.ItemCrystalWrench;
import am2.items.ItemEssence;
import am2.items.ItemOre;
import am2.items.ItemRune;
import am2.items.ItemSpellComponent;
import am2.items.SpellBase;
import am2.items.rendering.SpellRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemDefs {
	public static final Item spellParchment = new ItemArsMagica2().registerAndName("spell_parchment");
	public static final Item affinityTome = new ItemAffinityTome().registerAndName("tome");
	public static final Item itemOre = new ItemOre().registerAndName("itemOre");
	public static final Item essence = new ItemEssence().registerAndName("essence");
	public static final Item rune = new ItemRune().registerAndName("rune");
	
	public static final Item mobFocus = new ItemArsMagica2().registerAndName("mobFocus");
	public static final Item lesserFocus = new ItemArsMagica2().registerAndName("lesserFocus");
	public static final Item standardFocus = new ItemArsMagica2().registerAndName("standardFocus");
	public static final Item manaFocus = new ItemArsMagica2().registerAndName("manaFocus");
	public static final Item greaterFocus = new ItemArsMagica2().registerAndName("greaterFocus");
	public static final Item chargeFocus = new ItemArsMagica2().registerAndName("chargeFocus");
	public static final Item itemFocus = new ItemArsMagica2().registerAndName("itemFocus");
	public static final Item playerFocus = new ItemArsMagica2().registerAndName("playerFocus");
	public static final Item creatureFocus = new ItemArsMagica2().registerAndName("creatureFocus");
	public static final Item arcaneCompendium = new ItemArcaneCompendium().registerAndName("arcaneCompendium");
	public static final Item crystalWrench = new ItemCrystalWrench().registerAndName("crystalWrench");
	public static final Item magitechGoggles = new ItemMagitechGoggles(0).registerAndName("magitechGoggles");
	
	// PlaceHolder items
	public static final Item spell_component = new ItemSpellComponent().registerAndName("spellComponent");
	public static final Item etherium = new ItemArsMagica2().registerAndName("etherium").setCreativeTab(null);
	
	public static final Item blankRune = new ItemArsMagica2().registerAndName("blankRune");
	public static final Item chalk = new ItemChalk().registerAndName("chalk");
	public static final Item spellStaffMagitech = null;
	public static final Item flickerFocus = null;
	public static final Item flickerJar = null;
	public static final Item evilBook = null;
	public static final Item wardingCandle = null;
	public static final Item liquidEssenceBottle = null;
	public static final Item bindingCatalyst = null;
	public static final Item inscriptionUpgrade = null;

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
		
		registerTexture(arcaneCompendium);
		registerTexture(blankRune);
		
		registerTexture(crystalWrench);
		registerTexture(magitechGoggles);
		registerTexture(etherium);
		registerTexture(chalk);
		
		RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
		renderItem.getItemModelMesher().register(GameRegistry.findRegistry(Item.class).getValue(new ResourceLocation("arsmagica2", "spell_parchment")), 0, new ModelResourceLocation("arsmagica2:spell_parchment", "inventory"));
		Item essence = GameRegistry.findRegistry(Item.class).getValue(new ResourceLocation("arsmagica2", "essence"));
		Iterator<Entry<String, Affinity>> iter = AffinityRegistry.getAffinityMap().entrySet().iterator();
		int effMeta = 0;
		for (int i = 0; i < AffinityRegistry.getAffinityMap().size(); i++) {
			if (!iter.hasNext())
				break;
			Entry<String, Affinity> entry = iter.next();
			ModelBakery.registerItemVariants(affinityTome, new ModelResourceLocation("arsmagica2:affinity_tome_" + entry.getValue().getName(), "inventory"));
			renderItem.getItemModelMesher().register(affinityTome, i, new ModelResourceLocation("arsmagica2:affinity_tome_" + entry.getValue().getName(), "inventory"));
			if (entry.getValue().equals(AffinityRegistry.getAffinityFromName("none"))) {
				continue;
			}
			ModelBakery.registerItemVariants(essence, new ModelResourceLocation("arsmagica2:essence_" + entry.getValue().getName(), "inventory"));
			renderItem.getItemModelMesher().register(essence, effMeta, new ModelResourceLocation("arsmagica2:essence_" + entry.getValue().getName(), "inventory"));
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
		renderItem.getItemModelMesher().register(spell, new SpellRenderer());

	}
	
	private static void registerTexture(Item item) {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
	}
}
