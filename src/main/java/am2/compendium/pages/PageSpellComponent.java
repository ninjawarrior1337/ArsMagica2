package am2.compendium.pages;

import static net.minecraft.client.renderer.texture.TextureMap.LOCATION_BLOCKS_TEXTURE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import am2.api.ArsMagicaAPI;
import am2.api.event.SpellRecipeItemsEvent;
import am2.api.spell.AbstractSpellPart;
import am2.defs.ItemDefs;
import am2.gui.AMGuiHelper;
import am2.texture.SpellIconManager;
import am2.utils.RecipeUtils;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;

public class PageSpellComponent extends CompendiumPage<AbstractSpellPart> {

	private Object[] craftingComponents;
	private float framecount = 0;
	private ItemStack stackTip = null;
	private int tipX;
	private int tipY;
	private static HashMap<Item, Integer> forcedMetas = new HashMap<>();

	public PageSpellComponent(AbstractSpellPart element) {
		super(element);
		getAndAnalyzeRecipe();
	}

	@Override
	protected void renderPage(int posX, int posY, int mouseX, int mouseY) {
		RenderHelper.disableStandardItemLighting();
		int cx = posX + 60;
		int cy = posY + 92;
		framecount += 0.5f;
		stackTip = null;
		RenderRecipe(cx, cy, mouseX, mouseY);
		TextureAtlasSprite icon = SpellIconManager.INSTANCE.getSprite(element.getRegistryName().toString());
		mc.renderEngine.bindTexture(LOCATION_BLOCKS_TEXTURE);
		GlStateManager.color(1, 1, 1, 1);
		if (icon != null)
			AMGuiHelper.DrawIconAtXY(icon, cx, cy, zLevel, 16, 16, false);
		if (mouseX > cx && mouseX < cx + 16){
			if (mouseY > cy && mouseY < cy + 16){
				stackTip = new ItemStack(ItemDefs.spell_component, 1, ArsMagicaAPI.getSkillRegistry().getId(element.getRegistryName()));
				tipX = mouseX;
				tipY = mouseY;
			}
		}
		if (stackTip != null) {
			renderItemToolTip(stackTip, tipX, tipY);
		}
		RenderHelper.enableStandardItemLighting();
	}
	
	private void RenderRecipe(int cx, int cy, int mousex, int mousey){
		if (craftingComponents == null) return;
		float angleStep = (360.0f / craftingComponents.length);
		for (int i = 0; i < craftingComponents.length; ++i){
			float angle = (float)(Math.toRadians((angleStep * i) + framecount % 360));
			float nextangle = (float)(Math.toRadians((angleStep * ((i + 1) % craftingComponents.length)) + framecount % 360));
			float dist = 45;
			int x = (int)Math.round(cx - Math.cos(angle) * dist);
			int y = (int)Math.round(cy - Math.sin(angle) * dist);
			int nextx = (int)Math.round(cx - Math.cos(nextangle) * dist);
			int nexty = (int)Math.round(cy - Math.sin(nextangle) * dist);
			AMGuiHelper.line2d(x + 8, y + 8, cx + 8, cy + 8, zLevel, 0x0000DD);
			AMGuiHelper.gradientline2d(x + 8, y + 8, nextx + 8, nexty + 8, zLevel, 0x0000DD, 0xDD00DD);
			renderCraftingComponent(i, x, y, mousex, mousey);
		}
		return;
	}
	
	@SuppressWarnings("unchecked")
	private void renderCraftingComponent(int index, int sx, int sy, int mousex, int mousey){
		Object craftingComponent = craftingComponents[index];
	
		if (craftingComponent == null) return;
	
		ItemStack stack = null;
	
		if (craftingComponent instanceof ItemStack){
			stack = (ItemStack)craftingComponent;
		}else if (craftingComponent instanceof List){
			if (((List<ItemStack>)craftingComponent).size() == 0)
				return;
			int idx = new Random(AMGuiHelper.instance.getSlowTicker()).nextInt(((List<ItemStack>)craftingComponent).size());
			stack = ((ItemStack)((List<ItemStack>)craftingComponent).get(idx)).copy();
		}
	
		List<ItemStack> oredict = OreDictionary.getOres(stack.getItem().getUnlocalizedName());
		List<ItemStack> alternates = new ArrayList<ItemStack>();
		alternates.addAll(oredict);
	
		if (alternates.size() > 0){
			alternates.add(stack);
			stack = alternates.get(new Random(AMGuiHelper.instance.getSlowTicker()).nextInt(alternates.size()));
		}
		if (forcedMetas .containsKey(stack.getItem()))
			stack = new ItemStack(stack.getItem(), stack.stackSize, forcedMetas.get(stack.getItem()));
	
		try{
			AMGuiHelper.DrawItemAtXY(stack, sx, sy, this.zLevel);
			RenderHelper.disableStandardItemLighting();
		}catch (Throwable t){
			forcedMetas.put(stack.getItem(), 0);
		}
	
		if (mousex > sx && mousex < sx + 16){
			if (mousey > sy && mousey < sy + 16){
				stackTip = stack;
				tipX = mousex;
				tipY = mousey;
			}
		}
	}

	private void getAndAnalyzeRecipe() {
		ArrayList<Object> recipe = new ArrayList<Object>();
		Object[] recipeItems = ((AbstractSpellPart) element).getRecipe();
		SpellRecipeItemsEvent event = new SpellRecipeItemsEvent(element.getRegistryName().toString(), recipeItems);
		MinecraftForge.EVENT_BUS.post(event);
		recipeItems = event.recipeItems;

		if (recipeItems != null) {
			for (int i = 0; i < recipeItems.length; ++i) {
				Object o = recipeItems[i];
				if (o instanceof ItemStack) {
					recipe.add(o);
				} else if (o instanceof Item) {
					recipe.add(new ItemStack((Item) o));
				} else if (o instanceof Block) {
					recipe.add(new ItemStack((Block) o));
				} else if (o instanceof String) {
					if (((String) o).startsWith("E:")) {
						String s = ((String) o);
						try {
							int[] types = RecipeUtils.ParseEssenceIDs(s);
							int type = 0;
							for (int t : types)
								type |= t;
							int amount = (Integer) recipeItems[++i];
							recipe.add(new ItemStack(ItemDefs.etherium, amount, type));
						} catch (Throwable t) {
							continue;
						}
					} else {
						recipe.add(OreDictionary.getOres((String) o));
					}
				}
			}
		}
		craftingComponents = recipe.toArray();
	}
}
