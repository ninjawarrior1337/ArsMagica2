package am2.utils;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.potion.Potion;

public class RecipeUtils {
//	public static ItemStack parsePotionMeta(String potionDefinition){
//		String[] potionSections = potionDefinition.split("&");
//
//		int potionMeta = 0;
//		ItemStack potion = new ItemStack(Items.POTIONITEM);
//
//		for (String s : potionSections){
//			s = s.trim();
//
//			if (s.contains("+")) continue;
//
//			boolean bitSet = true;
//			for (char c : s.toCharArray()){
//				if (c == '!'){
//					bitSet = false;
//					continue;
//				}
//				if (Character.isDigit(c)){
//					int value = Character.getNumericValue(c);
//					Potion.REGISTRY.getObjectById(value);
//					if (bitSet){
//						potionMeta = setBit(potionMeta, value);
//					}else{
//						potionMeta = clearBit(potionMeta, value);
//					}
//					bitSet = true;
//					continue;
//				}
//			}
//
//		}
//
//		return potionMeta;
//	}
	
	public static IRecipe getRecipeFor(ItemStack item){

		if (item == null || item.getItem() == null) return null;

		try{
			List<IRecipe> list = CraftingManager.getInstance().getRecipeList();
			ArrayList<IRecipe> possibleRecipes = new ArrayList<>();
			for (IRecipe recipe : list){
				ItemStack output = ((IRecipe)recipe).getRecipeOutput();
				if (output == null) continue;
				if (output.getItem() == item.getItem() && (output.getItemDamage() == Short.MAX_VALUE || output.getItemDamage() == item.getItemDamage())){
					possibleRecipes.add(recipe);
				}
			}

			if (possibleRecipes.size() > 0){
				for (Object recipe : possibleRecipes){
					if (((IRecipe)recipe).getRecipeOutput().getItemDamage() == item.getItemDamage()){
						return (IRecipe)recipe;
					}
				}
				return (IRecipe)possibleRecipes.get(0);
			}
		}catch (Throwable t){

		}

		return null;
	}
}
