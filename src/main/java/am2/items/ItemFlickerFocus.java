package am2.items;

import java.util.List;

import am2.api.flickers.IFlickerFunctionality;
import am2.blocks.tileentity.flickers.FlickerOperatorRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;

@SuppressWarnings("deprecation")
public class ItemFlickerFocus extends ItemArsMagica2{

	public ItemFlickerFocus(){
		super();
		setHasSubtypes(true);
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack){
		int meta = stack.getItemDamage();
		IFlickerFunctionality operator = FlickerOperatorRegistry.instance.getOperatorForMask(meta);
		if (operator == null)
			return "Trash";
		return String.format(I18n.translateToLocal("item.arsmagica2:FlickerFocusPrefix"), I18n.translateToLocal("item.arsmagica2:" + operator.getClass().getSimpleName() + ".name"));
	}

	@Override
	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List){
		for (int i : FlickerOperatorRegistry.instance.getMasks()){
			par3List.add(new ItemStack(this, 1, i));
		}
	}
}
