package am2.items;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import am2.defs.CreativeTabsDefs;

public class ItemArsMagica2 extends Item{
	
	public ItemArsMagica2() {
		setCreativeTab(CreativeTabsDefs.tabAM2Items);
	}
	
	public ItemArsMagica2 registerAndName(String name) {
		this.setUnlocalizedName(name);
		GameRegistry.register(this, new ResourceLocation("arsmagica2", name));
		return this;
	}
	
}
