package am2.items;

import java.util.HashMap;
import java.util.List;

import am2.api.ArsMagicaAPI;
import am2.api.affinity.Affinity;
import am2.extensions.AffinityData;
import am2.utils.AffinityShiftUtils;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

@SuppressWarnings("deprecation")
public class ItemAffinityTome extends ItemArsMagica2 {

	
	public ItemAffinityTome() {
		setHasSubtypes(true);
		setMaxDamage(1);
		setMaxDamage(0);
	}
	
	@Override
	public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
		for (int i = 0; i < ArsMagicaAPI.getAffinityRegistry().getValues().size(); i++) {
			subItems.add(new ItemStack(itemIn, 1, i));
		}
	}
	
	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (worldIn.isRemote)
			return EnumActionResult.PASS;
		Affinity aff = ArsMagicaAPI.getAffinityRegistry().getObjectById(stack.getItemDamage());
		HashMap<Affinity, Float> map = AffinityShiftUtils.finalize(aff, 0.2F, AffinityShiftUtils.shiftAffinity(aff, 0.2F, AffinityData.For(playerIn).getAffinities()));
		stack.stackSize--;
		AffinityShiftUtils.setAffinityData(map, playerIn, false);
		return EnumActionResult.PASS;
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		Affinity aff = ArsMagicaAPI.getAffinityRegistry().getObjectById(stack.getItemDamage());
		return I18n.translateToLocal("item.tome.name") + aff.getLocalizedName();
	}
	
	@Override
	public boolean hasEffect(ItemStack stack) {
		return true;
	}
}
