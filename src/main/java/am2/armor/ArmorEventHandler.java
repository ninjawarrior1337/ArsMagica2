package am2.armor;

import am2.AMCore;
import am2.api.items.armor.ArmorTextureEvent;
import am2.api.items.armor.IArmorImbuement;
import am2.api.items.armor.ImbuementApplicationTypes;
import am2.playerextensions.ExtendedProperties;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ArmorEventHandler{

	@SubscribeEvent
	public void onEntityLiving(LivingUpdateEvent event){
		if (!(event.getEntityLiving() instanceof EntityPlayer))
			return;

		doInfusions(ImbuementApplicationTypes.ON_TICK, event, (EntityPlayer)event.getEntityLiving());
	}

	@SubscribeEvent
	public void onEntityHurt(LivingHurtEvent event){
		if (!(event.getEntityLiving() instanceof EntityPlayer))
			return;

		doInfusions(ImbuementApplicationTypes.ON_HIT, event, (EntityPlayer)event.getEntityLiving());

		if (event.getEntityLiving() instanceof EntityPlayer)
			doXPInfusion((EntityPlayer)event.getEntityLiving(), 0.01f, Math.max(0.05f, Math.min(event.getAmount(), 5)));
	}

	@SubscribeEvent
	public void onEntityJump(LivingJumpEvent event){
		if (!(event.getEntityLiving() instanceof EntityPlayer))
			return;

		doInfusions(ImbuementApplicationTypes.ON_JUMP, event, (EntityPlayer)event.getEntityLiving());
	}

	@SubscribeEvent
	public void onMiningSpeed(BreakSpeed event){
		doInfusions(ImbuementApplicationTypes.ON_MINING_SPEED, event, (EntityPlayer)event.getEntityPlayer());
	}

	@SubscribeEvent
	public void onEntityDeath(LivingDeathEvent event){
		if (event.getSource().getSourceOfDamage() instanceof EntityPlayer)
			doXPInfusion((EntityPlayer)event.getSource().getSourceOfDamage(), 1, Math.min(20, event.getEntityLiving().getMaxHealth()));

		if (!(event.getEntityLiving() instanceof EntityPlayer))
			return;

		doInfusions(ImbuementApplicationTypes.ON_DEATH, event, (EntityPlayer)event.getEntityLiving());
	}

	private void doInfusions(ImbuementApplicationTypes type, Event event, EntityPlayer player){
		ExtendedProperties props = ExtendedProperties.For(player);

		for (EntityEquipmentSlot slot : EntityEquipmentSlot.values()) {
			if (!slot.getSlotType().equals(EntityEquipmentSlot.Type.ARMOR))
				continue;
			IArmorImbuement[] infusions = ArmorHelper.getInfusionsOnArmor(player, slot);
			int cd = props.armorProcCooldowns[slot.getIndex()];
			for (IArmorImbuement inf : infusions){
				if (inf == null)
					continue;
				if (inf.getApplicationTypes().contains(type)){
					if (cd == 0 || inf.canApplyOnCooldown()){
						if (inf.applyEffect(player, player.worldObj, player.getItemStackFromSlot(slot), type, event)){
							if (inf.getCooldown() > 0){
								if (props.armorProcCooldowns[slot.getIndex()] < inf.getCooldown()){
									props.armorProcCooldowns[slot.getIndex()] = inf.getCooldown();
									if (player instanceof EntityPlayerMP)
										AMCore.proxy.blackoutArmorPiece((EntityPlayerMP)player, slot, inf.getCooldown());
								}
							}
						}
					}
				}
			}
		}
	}

	private void doXPInfusion(EntityPlayer player, float xpMin, float xpMax){
		float amt = (float)((player.worldObj.rand.nextFloat() * xpMin + (xpMax - xpMin)) * AMCore.config.getArmorXPInfusionFactor());
		ArmorHelper.addXPToArmor(amt, player);
	}

	@SubscribeEvent
	public void onArmorTexture(ArmorTextureEvent event){
		if (event.renderIndex == ArmorHelper.getArmorRenderIndex("mage")){
			if (event.slot == 2){
				event.texture = "arsmagica2:textures/models/mage_2.png";
			}else{
				event.texture = "arsmagica2:textures/models/mage_1.png";
			}
		}else if (event.renderIndex == ArmorHelper.getArmorRenderIndex("battlemage")){
			if (event.slot == 2){
				event.texture = "arsmagica2:textures/models/battlemage_2.png";
			}else{
				event.texture = "arsmagica2:textures/models/battlemage_1.png";
			}
		}else if (event.renderIndex == ArmorHelper.getArmorRenderIndex("archmage")){
			if (event.slot == 2){
				event.texture = "arsmagica2:textures/models/archmage_2.png";
			}else{
				event.texture = "arsmagica2:textures/models/archmage_1.png";
			}
		}else if (event.renderIndex == ArmorHelper.getArmorRenderIndex("ender")){
			event.texture = "arsmagica2:textures/models/ender_1.png";
		}else if (event.renderIndex == ArmorHelper.getArmorRenderIndex("magitech")){
			event.texture = "arsmagica2:textures/models/magitech_1.png";
		}
	}
}
