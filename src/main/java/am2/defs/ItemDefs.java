package am2.defs;

import java.util.Iterator;
import java.util.Map.Entry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import am2.affinity.Affinity;
import am2.api.AffinityRegistry;
import am2.items.ItemAffinityTome;
import am2.items.ItemArsMagica2;
import am2.items.ItemEssence;
import am2.items.ItemOre;
import am2.items.ItemRune;
import am2.items.SpellBase;
import am2.items.rendering.SpellRenderer;

public class ItemDefs {
	public static final Item spellParchement = new ItemArsMagica2().registerAndName("spell_parchment");
	public static final Item affinityTome = new ItemAffinityTome().registerAndName("tome");
	public static final Item itemOre = new ItemOre().registerAndName("itemOre");
	public static final Item essence = new ItemEssence().registerAndName("essence");
	public static final Item rune = new ItemRune().registerAndName("rune");
	
	public static final Item mobFocus = new ItemArsMagica2().registerAndName("mobFocus");
	public static final Item lesserFocus = new ItemArsMagica2().registerAndName("lesserFocuse");
	public static final Item standardFocus = new ItemArsMagica2().registerAndName("standardFocus");
	public static final Item manaFocus = new ItemArsMagica2().registerAndName("manaFocus");
	
	public static SpellBase spell = new SpellBase().registerAndName("spell");
	
	public static void init () {
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
			ModelBakery.registerItemVariants(itemOre, new ModelResourceLocation("arsmagica2:rune_" + EnumDyeColor.byDyeDamage(i).getName().toLowerCase(), "inventory"));
			renderItem.getItemModelMesher().register(rune, i, new ModelResourceLocation("arsmagica2:rune_" + EnumDyeColor.byDyeDamage(i).getName().toLowerCase(), "inventory"));
		}
		renderItem.getItemModelMesher().register(spell, new SpellRenderer());
	}
}
