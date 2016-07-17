package am2.affinity;

import am2.affinity.abilities.AbilityPhasing;
import am2.affinity.abilities.AbilityExpandedLungs;
import am2.affinity.abilities.AbilityFluidity;
import am2.affinity.abilities.AbilityLavaFreeze;
import am2.affinity.abilities.AbilityLightningStep;
import am2.affinity.abilities.AbilityNightVision;
import am2.affinity.abilities.AbilitySwiftSwim;
import am2.affinity.abilities.AbilityWaterFreeze;
import am2.api.affinity.AbstractAffinityAbility;
import am2.utils.WorldUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

public class AffinityAbilityHelper {
	
	static {
		//AIR
		
		//ARCANE
		
		//EARTH
		
		//ENDER
		GameRegistry.register(new AbilityPhasing());
		GameRegistry.register(new AbilityNightVision());
		
		//FIRE
		
		//ICE
		GameRegistry.register(new AbilityLavaFreeze());
		GameRegistry.register(new AbilityWaterFreeze());
		
		//LIFE
		
		//WATER
		GameRegistry.register(new AbilityExpandedLungs());
		GameRegistry.register(new AbilityFluidity());
		GameRegistry.register(new AbilitySwiftSwim());
		
		//NATURE
		
		//LIGHTNING
		GameRegistry.register(new AbilityLightningStep());
	}
	
	
	@SubscribeEvent
	public void onKeyInput(InputEvent.KeyInputEvent event) {
		for (AbstractAffinityAbility ability : GameRegistry.findRegistry(AbstractAffinityAbility.class).getValues()) {
			if (ability.getKey() != null && ability.getKey().isPressed()) {
				EntityPlayer player = Minecraft.getMinecraft().thePlayer;
				if (ability.canApply(player)) {
					WorldUtils.runSided(Side.CLIENT, ability.createRunnable(player));
					player = FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld().getPlayerEntityByUUID(player.getUniqueID());
					WorldUtils.runSided(Side.SERVER, ability.createRunnable(player));
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onPlayerTick(LivingUpdateEvent event) {
		if (event.getEntityLiving() instanceof EntityPlayer) {
			for (AbstractAffinityAbility ability : GameRegistry.findRegistry(AbstractAffinityAbility.class).getValues()) {
				if (ability.getKey() == null) {
					if (ability.canApply((EntityPlayer) event.getEntityLiving()))
						ability.applyTick((EntityPlayer) event.getEntityLiving());
					else
						ability.removeEffects((EntityPlayer) event.getEntityLiving());
				}
			}
		}
	}
}
