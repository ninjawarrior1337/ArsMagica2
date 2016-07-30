package am2.handler;

import am2.api.event.OBJQuadEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BakingHandler {
	
	@SubscribeEvent
	public void objBake (OBJQuadEvent event) {
		if (event.materialName.equalsIgnoreCase("Colored")) {
			event.tintIndex = 1;
		}
	}
	
}
