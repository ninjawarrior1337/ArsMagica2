package am2.defs;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class AMSounds {
	
	public static final SoundEvent AIR_GUARDIAN_HIT = GameRegistry.register(new SoundEvent(new ResourceLocation("arsmagica2:mob.airguardian.hit")));
	public static final SoundEvent AIR_GUARDIAN_DEATH = GameRegistry.register(new SoundEvent(new ResourceLocation("arsmagica2:mob.airguardian.death")));
	public static final SoundEvent AIR_GUARDIAN_IDLE = GameRegistry.register(new SoundEvent(new ResourceLocation("arsmagica2:mob.airguardian.idle")));
	
	public static final SoundEvent ARCANE_GUARDIAN_HIT = GameRegistry.register(new SoundEvent(new ResourceLocation("arsmagica2:mob.arcaneguardian.hit")));
	public static final SoundEvent ARCANE_GUARDIAN_DEATH = GameRegistry.register(new SoundEvent(new ResourceLocation("arsmagica2:mob.arcaneguardian.death")));
	public static final SoundEvent ARCANE_GUARDIAN_IDLE = GameRegistry.register(new SoundEvent(new ResourceLocation("arsmagica2:mob.arcaneguardian.idle")));
	public static final SoundEvent ARCANE_GUARDIAN_SPELL = GameRegistry.register(new SoundEvent(new ResourceLocation("arsmagica2:mob.arcaneguardian.spell")));
	
	public static final SoundEvent LIGHTNING_GUARDIAN_IDLE = GameRegistry.register(new SoundEvent(new ResourceLocation("arsmagica2:mob.lightningguardian.idle")));
	public static final SoundEvent LIGHTNING_GUARDIAN_ATTACK = GameRegistry.register(new SoundEvent(new ResourceLocation("arsmagica2:mob.lightningguardian.attack")));
	public static final SoundEvent LIGHTNING_GUARDIAN_LIGHTNING_ROD_1 = GameRegistry.register(new SoundEvent(new ResourceLocation("arsmagica2:mob.lightningguardian.lightning_rod_1")));
	public static final SoundEvent LIGHTNING_GUARDIAN_LIGHTNING_ROD_START = GameRegistry.register(new SoundEvent(new ResourceLocation("arsmagica2:mob.lightningguardian.lightning_rod_start")));
	public static final SoundEvent LIGHTNING_GUARDIAN_STATIC = GameRegistry.register(new SoundEvent(new ResourceLocation("arsmagica2:mob.lightningguardian.static")));

	public static final SoundEvent NATURE_GUARDIAN_WHIRL_LOOP = GameRegistry.register(new SoundEvent(new ResourceLocation("arsmagica2:mob.natureguardian.whirlloop")));
	
	public static final SoundEvent LIFE_GUARDIAN_SUMMON = GameRegistry.register(new SoundEvent(new ResourceLocation("arsmagica2:mob.lifeguardian.summon")));
	
	public static final SoundEvent WINTER_GUARDIAN_LAUNCH_ARM = GameRegistry.register(new SoundEvent(new ResourceLocation("arsmagica2:mob.winterguardian.launcharm")));

}
