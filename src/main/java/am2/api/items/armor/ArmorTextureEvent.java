package am2.api.items.armor;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraftforge.fml.common.eventhandler.Event;


public class ArmorTextureEvent extends Event{
	public final EntityEquipmentSlot slot;
	public final int renderIndex;

	public String texture;

	public ArmorTextureEvent(EntityEquipmentSlot slot, int renderIndex){
		this.slot = slot;
		this.renderIndex = renderIndex;
	}
}
