package am2.items;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import am2.affinity.Affinity;
import am2.api.AffinityRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
		Iterator<Entry<String, Affinity>> iter = AffinityRegistry.getAffinityMap().entrySet().iterator();
		for (int i = 0; i < AffinityRegistry.getAffinityMap().size(); i++) {
			if (!iter.hasNext())
				break;
			if (iter.next().getValue().equals(AffinityRegistry.getAffinityFromName("none"))) {
				i--;
				continue;
			}
			subItems.add(new ItemStack(this, 1, i));
		}
		//super.getSubItems(itemIn, tab, subItems);
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		int meta = MathHelper.clamp_int(stack.getItemDamage(), 0, AffinityRegistry.getAffinityMap().size() - 2);
		String name = "";
		int i = 0;
		for (Entry<String, Affinity> entry : AffinityRegistry.getAffinityMap().entrySet()) {
			if (entry.getValue().equals(AffinityRegistry.getAffinityFromName("none"))) {
				//i--;
				continue;
			}
			if (i == meta)
				name += entry.getValue().getLocalizedName();
			i++;
		}
		name += I18n.translateToLocal("item.essence.name");
		return name;
	}
}
