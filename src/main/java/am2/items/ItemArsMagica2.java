package am2.items;

import am2.defs.CreativeTabsDefs;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemArsMagica2 extends Item{
	
	public ItemArsMagica2() {
		setCreativeTab(CreativeTabsDefs.tabAM2Items);
		setMaxDamage(0);
	}
	
	public ItemArsMagica2 registerAndName(String name) {
		this.setUnlocalizedName(name);
		GameRegistry.register(this, new ResourceLocation("arsmagica2", name));
		return this;
	}
}
