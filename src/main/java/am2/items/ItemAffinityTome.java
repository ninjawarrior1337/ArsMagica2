package am2.items;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import am2.affinity.Affinity;
import am2.api.AffinityRegistry;
import am2.extensions.AffinityData;
import am2.utils.AffinityShiftUtils;

@SuppressWarnings("deprecation")
public class ItemAffinityTome extends ItemArsMagica2 {

	
	public ItemAffinityTome() {
		setHasSubtypes(true);
		setMaxDamage(1);
		setMaxDamage(0);
	}
	
	@Override
	public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
		for (int i = 0; i < AffinityRegistry.getAffinityMap().size(); i++) {
			subItems.add(new ItemStack(itemIn, 1, i));
		}
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn,
			World worldIn, EntityPlayer playerIn, EnumHand hand) {
		if (worldIn.isRemote)
			return super.onItemRightClick(itemStackIn, worldIn, playerIn, hand);
		String affName = "none";
		int i = 0;
		for (Entry<String, Affinity> entry : AffinityRegistry.getAffinityMap().entrySet()) {
			if (i == itemStackIn.getItemDamage())
				affName = entry.getKey();
			i++;
		}
		HashMap<Affinity, Float> map = AffinityShiftUtils.finalize(AffinityRegistry.getAffinityFromName(affName), 0.2F, AffinityShiftUtils.shiftAffinity(AffinityRegistry.getAffinityFromName(affName), 0.2F, AffinityData.For(playerIn).getAffinities()));
		ItemStack newStack = itemStackIn.copy();
		newStack.stackSize--;
		AffinityShiftUtils.setAffinityData(map, playerIn, false);
		return super.onItemRightClick(newStack, worldIn, playerIn, hand);
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		Affinity aff = AffinityRegistry.getAffinityFromName("none");
		int i = 0;
		for (Entry<String, Affinity> entry : AffinityRegistry.getAffinityMap().entrySet()) {
			if (i == stack.getItemDamage())
				aff = entry.getValue();
			i++;
		}
		return I18n.translateToLocal("item.tome.name") + aff.getLocalizedName();
	}
	
	@Override
	public boolean hasEffect(ItemStack stack) {
		return true;
	}
}
