package am2.items;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import am2.api.ArsMagicaAPI;
import am2.api.affinity.Affinity;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.translation.I18n;

@SuppressWarnings("deprecation")
public class ItemEssence extends ItemArsMagica2 {
	
	public ItemEssence() {
		super();
		hasSubtypes = true;
		setMaxDamage(0);
	}
	
	@Override
	public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
		Iterator<Entry<ResourceLocation, Affinity>> iter = ArsMagicaAPI.getAffinityRegistry().getEntries().iterator();
		for (int i = 0; i < ArsMagicaAPI.getAffinityRegistry().getValues().size(); i++) {
			if (!iter.hasNext())
				break;
			if (iter.next().getValue().equals(Affinity.NONE)) {
				i--;
				continue;
			}
			subItems.add(new ItemStack(this, 1, i));
		}
		//super.getSubItems(itemIn, tab, subItems);
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		int meta = MathHelper.clamp_int(stack.getItemDamage(), 0, ArsMagicaAPI.getAffinityRegistry().getValues().size() - 2);
		String name = "";
		int i = 0;
		for (Affinity aff : ArsMagicaAPI.getAffinityRegistry().getValues()) {
			if (aff.equals(Affinity.NONE)) {
				//i--;
				continue;
			}
			if (i == meta)
				name += aff.getLocalizedName();
			i++;
		}
		name += I18n.translateToLocal("item.essence.name");
		return name;
	}
}
