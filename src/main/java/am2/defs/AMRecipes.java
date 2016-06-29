package am2.defs;

import am2.ObeliskFuelHelper;
import am2.items.ItemOre;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class AMRecipes {
	public static void addRecipes() {
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack (BlockDefs.magicWall, 16), new Object[] {
			"VSV",
			'V', new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_VINTEUM),
			'S', "stone"
		}));
		GameRegistry.addRecipe(new ShapedOreRecipe(BlockDefs.altar, new Object[] {
			"V",
			"S",
			'V', new ItemStack(ItemDefs.itemOre, 1, ItemOre.META_VINTEUM),
			'S', "stone"
		}));
		
		ObeliskFuelHelper.instance.registerFuelType(new ItemStack(ItemDefs.itemOre, 0, ItemOre.META_VINTEUM), 200);
	}
}
