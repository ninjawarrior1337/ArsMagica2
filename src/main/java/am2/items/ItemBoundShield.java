package am2.items;

import am2.api.IBoundItem;
import am2.defs.ItemDefs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemBoundShield extends ItemShield implements IBoundItem {

	public ItemBoundShield() {
		super();
		this.maxStackSize = 1;
		this.setMaxDamage(0);
		this.setCreativeTab(null);
	}

	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
		return false;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onDroppedByPlayer(ItemStack item, EntityPlayer player) {
		item.setItem(ItemDefs.spell);
		return false;
	}

	@Override
	public float maintainCost(EntityPlayer player, ItemStack stack) {
		return normalMaintain;
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		return "item." + getRegistryName().getResourcePath() + ".name";
	}

	public ItemBoundShield registerAndName(String name) {
		this.setUnlocalizedName(name);
		GameRegistry.register(this, new ResourceLocation("arsmagica2", name));
		return this;
	}
	
}
