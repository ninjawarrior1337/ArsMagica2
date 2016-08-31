package am2.lore;

import am2.gui.GuiArcaneCompendium;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CompendiumEntryItem extends CompendiumEntry{

	int lowerMetaRange, upperMetaRange;
	Item item;

	public CompendiumEntryItem(String id, Item item, int lowerMeta, int upperMeta, String... related){
		super(CompendiumEntryTypes.instance.ITEM, id, related);
		this.lowerMetaRange = lowerMeta;
		this.upperMetaRange = upperMeta;
		this.item = item;
	}
	
	public CompendiumEntryItem(String id, Item item, int meta, String... related) {
		this(id, item, meta, meta + 1, related);
	}
	
	public CompendiumEntryItem(String id, Item item, String... related) {
		this(id, item, 0, related);
	}

	public boolean hasMetaItems(){
		return (lowerMetaRange - upperMetaRange) > 1;
	}

	public ItemStack[] getMetaItems(Item item){
		ItemStack[] items = new ItemStack[upperMetaRange - lowerMetaRange];
		for (int i = lowerMetaRange; i < upperMetaRange; ++i){
			items[i - lowerMetaRange] = new ItemStack(item, 1, i);
		}
		return items;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public GuiArcaneCompendium getCompendiumGui(){
		return new GuiArcaneCompendium(id, item, lowerMetaRange);
	}

	@Override
	public ItemStack getRepresentStack(){
		return new ItemStack(item, 1, lowerMetaRange);
	}
}
