package am2.api.event;

import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.Event;

public class RenderingItemEvent extends Event {
	
	private final ItemStack stack;
	private final TransformType cameraTransformType;
	
	public RenderingItemEvent(ItemStack stack, TransformType cameraTransformType) {
		this.stack = stack;
		this.cameraTransformType = cameraTransformType;
	}
	
	public ItemStack getStack() {
		return stack;
	}
	
	public TransformType getCameraTransformType() {
		return cameraTransformType;
	}
}
