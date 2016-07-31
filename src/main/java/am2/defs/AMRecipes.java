package am2.defs;

import am2.LogHelper;
import am2.ObeliskFuelHelper;
import am2.api.flickers.IFlickerFunctionality;
import am2.blocks.tileentity.flickers.FlickerOperatorRegistry;
import am2.items.ItemOre;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fluids.UniversalBucket;
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
		ObeliskFuelHelper.instance.registerFuelType(UniversalBucket.getFilledBucket(ForgeModContainer.getInstance().universalBucket, BlockDefs.liquid_essence), 2000);
		for (int i : FlickerOperatorRegistry.instance.getMasks()) {
			IFlickerFunctionality func = FlickerOperatorRegistry.instance.getOperatorForMask(i);
			if (func != null) {
				Object[] recipeItems = func.getRecipe();
				if (recipeItems != null) {
					GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ItemDefs.flickerFocus, 1, i), recipeItems));
				} else {
					LogHelper.info("Flicker operator %s was registered with no recipe.  It is un-craftable.  This may have been intentional.",func.getClass().getSimpleName());
				}
			}
		}
	}
}
