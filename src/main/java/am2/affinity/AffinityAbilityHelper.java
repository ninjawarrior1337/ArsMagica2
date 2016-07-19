package am2.affinity;

import java.util.Map.Entry;

import am2.affinity.abilities.AbilityColdBlooded;
import am2.affinity.abilities.AbilityExpandedLungs;
import am2.affinity.abilities.AbilityFluidity;
import am2.affinity.abilities.AbilityFulmination;
import am2.affinity.abilities.AbilityLavaFreeze;
import am2.affinity.abilities.AbilityLightningStep;
import am2.affinity.abilities.AbilityNightVision;
import am2.affinity.abilities.AbilityPhasing;
import am2.affinity.abilities.AbilityReflexes;
import am2.affinity.abilities.AbilityRooted;
import am2.affinity.abilities.AbilityShortCircuit;
import am2.affinity.abilities.AbilitySolidBones;
import am2.affinity.abilities.AbilitySwiftSwim;
import am2.affinity.abilities.AbilityThunderPunch;
import am2.affinity.abilities.AbilityWaterFreeze;
import am2.api.affinity.AbstractAffinityAbility;
import am2.api.event.SpellCastEvent;
import am2.extensions.AffinityData;
import am2.utils.WorldUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
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
		GameRegistry.register(new AbilitySolidBones());
		
		//ENDER
		GameRegistry.register(new AbilityPhasing());
		GameRegistry.register(new AbilityNightVision());
		
		//FIRE
		
		//ICE
		GameRegistry.register(new AbilityLavaFreeze());
		GameRegistry.register(new AbilityWaterFreeze());
		GameRegistry.register(new AbilityColdBlooded());
		
		//LIFE
		
		//WATER
		GameRegistry.register(new AbilityExpandedLungs());
		GameRegistry.register(new AbilityFluidity());
		GameRegistry.register(new AbilitySwiftSwim());
		
		//NATURE
		GameRegistry.register(new AbilityRooted());
		
		//LIGHTNING
		GameRegistry.register(new AbilityLightningStep());
		GameRegistry.register(new AbilityReflexes());
		GameRegistry.register(new AbilityFulmination());
		GameRegistry.register(new AbilityShortCircuit());
		GameRegistry.register(new AbilityThunderPunch());
	}
	
	
	@SubscribeEvent
	public void onKeyInput(InputEvent.KeyInputEvent event) {
		for (AbstractAffinityAbility ability : GameRegistry.findRegistry(AbstractAffinityAbility.class).getValues()) {
			if (ability.getKey() != null && ability.getKey().isPressed()) {
				EntityPlayer player = Minecraft.getMinecraft().thePlayer;
				player = FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld().getPlayerEntityByUUID(player.getUniqueID());
				if (ability.canApply(player)) {
					WorldUtils.runSided(Side.CLIENT, ability.createRunnable(Minecraft.getMinecraft().thePlayer));
					WorldUtils.runSided(Side.SERVER, ability.createRunnable(player));
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onPlayerTick(LivingUpdateEvent event) {
		if (event.getEntityLiving() instanceof EntityPlayer) {
			if (!event.getEntityLiving().worldObj.isRemote) {
				for (Entry<String, Integer> entry : AffinityData.For(event.getEntityLiving()).getCooldowns().entrySet()) {
					if (entry.getValue() > 0)
						AffinityData.For(event.getEntityLiving()).addCooldown(entry.getKey(), entry.getValue() - 1);
				}
			}
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
	
	@SubscribeEvent
	public void onPlayerHurt(LivingHurtEvent event) {
		if (event.getEntityLiving() instanceof EntityPlayer) {
			for (AbstractAffinityAbility ability : GameRegistry.findRegistry(AbstractAffinityAbility.class).getValues()) {
				if (ability.getKey() == null) {
					if (ability.canApply((EntityPlayer) event.getEntityLiving()))
						ability.applyHurt((EntityPlayer) event.getEntityLiving(), event, false);
				}
			}
		}
		if (event.getSource().getEntity() != null && event.getSource().getEntity() instanceof EntityPlayer) {
			for (AbstractAffinityAbility ability : GameRegistry.findRegistry(AbstractAffinityAbility.class).getValues()) {
				if (ability.getKey() == null) {
					if (ability.canApply((EntityPlayer) event.getSource().getEntity()))
						ability.applyHurt((EntityPlayer) event.getSource().getEntity(), event, true);
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onPlayerFall(LivingFallEvent event) {
		if (event.getEntityLiving() instanceof EntityPlayer) {
			for (AbstractAffinityAbility ability : GameRegistry.findRegistry(AbstractAffinityAbility.class).getValues()) {
				if (ability.getKey() == null) {
					if (ability.canApply((EntityPlayer) event.getEntityLiving()))
						ability.applyFall((EntityPlayer) event.getEntityLiving(), event);
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onPlayerDeath(LivingDeathEvent event) {
		if (event.getEntityLiving() instanceof EntityPlayer) {
			for (AbstractAffinityAbility ability : GameRegistry.findRegistry(AbstractAffinityAbility.class).getValues()) {
				if (ability.getKey() == null) {
					if (ability.canApply((EntityPlayer) event.getEntityLiving()))
						ability.applyDeath((EntityPlayer) event.getEntityLiving(), event);
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onPlayerJump(LivingJumpEvent event) {
		if (event.getEntityLiving() instanceof EntityPlayer) {
			for (AbstractAffinityAbility ability : GameRegistry.findRegistry(AbstractAffinityAbility.class).getValues()) {
				if (ability.getKey() == null) {
					if (ability.canApply((EntityPlayer) event.getEntityLiving()))
						ability.applyJump((EntityPlayer) event.getEntityLiving(), event);
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onSpellCast(SpellCastEvent.Post event) {
		if (event.entityLiving instanceof EntityPlayer) {
			for (AbstractAffinityAbility ability : GameRegistry.findRegistry(AbstractAffinityAbility.class).getValues()) {
				if (ability.getKey() == null) {
					if (ability.canApply((EntityPlayer) event.entityLiving))
						ability.applySpellCast((EntityPlayer) event.entityLiving, event);
				}
			}
		}
	}
}
