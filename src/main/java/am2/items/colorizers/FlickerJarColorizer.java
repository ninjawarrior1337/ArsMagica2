package am2.items.colorizers;

import am2.api.ArsMagicaAPI;
import am2.api.affinity.Affinity;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;

public class FlickerJarColorizer implements IItemColor{

	@Override
	public int getColorFromItemstack(ItemStack stack, int tintIndex) {
		int meta = stack.getItemDamage();
		Affinity aff = ArsMagicaAPI.getAffinityRegistry().getObjectById(meta);
		return aff.getColor();
	}

}
