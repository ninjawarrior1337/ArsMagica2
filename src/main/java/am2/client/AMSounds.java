package am2.client;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class AMSounds {
	
	public static final SoundEvent MANA_SHIELD_BLOCK = new SoundEvent(new ResourceLocation("arsmagica2", "misc.event.mana_shield_block"));
	public static final SoundEvent MAGIC_LEVEL_UP = new SoundEvent(new ResourceLocation("arsmagica2", "misc.event.magic_level_up"));
	
	public static void registerSounds() {
		GameRegistry.register(MANA_SHIELD_BLOCK, new ResourceLocation("arsmagica2", "misc.event.mana_shield_block"));
		GameRegistry.register(MAGIC_LEVEL_UP, new ResourceLocation("arsmagica2", "misc.event.magic_level_up"));
	}
}
