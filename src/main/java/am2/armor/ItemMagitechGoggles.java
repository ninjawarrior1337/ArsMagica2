package am2.armor;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

public class ItemMagitechGoggles extends AMArmor{

	public ItemMagitechGoggles(int renderIndex){
		super(ArmorMaterial.LEATHER, ArsMagicaArmorMaterial.UNIQUE, renderIndex, EntityEquipmentSlot.HEAD);
	}
	@Override
	public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot){
		return 2;
	}

	@Override
	public int GetDamageReduction(){
		return 2;
	}
}
