package am2.proxy.tick;

import java.util.HashMap;

import am2.packet.AMDataWriter;
import am2.packet.AMNetHandler;
import am2.packet.AMPacketIDs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ServerTickHandler{

	//private boolean firstTick = true;
	public static HashMap<EntityLiving, EntityLivingBase> targetsToSet = new HashMap<EntityLiving, EntityLivingBase>();

	public static String lastWorldName;

	private void gameTick_Start(){
//
//		if (MinecraftServer.getServer().getFolderName() != lastWorldName){
//			lastWorldName = MinecraftServer.getServer().getFolderName();
//			firstTick = true;
//		}
//
//		if (firstTick){
//			ItemsCommonProxy.crystalPhylactery.getSpawnableEntities(MinecraftServer.getServer().worldServers[0]);
//			firstTick = false;
//		}
//
//		AMCore.proxy.itemFrameWatcher.checkWatchedFrames();
	}

	private void gameTick_End(){
//		BossSpawnHelper.instance.tick();
//		MeteorSpawnHelper.instance.tick();
//		EntityItemWatcher.instance.tick();
	}
	
	@SubscribeEvent
	public void onServerTick(TickEvent.ServerTickEvent event){
		if (event.phase == TickEvent.Phase.START){
			gameTick_Start();
		}else if (event.phase == TickEvent.Phase.END){
			gameTick_End();
		}
	}

	@SubscribeEvent
	public void onWorldTick(TickEvent.WorldTickEvent event){
//		System.out.println(event.side);
//		if (AMCore.config.retroactiveWorldgen())
//			RetroactiveWorldgenerator.instance.continueRetrogen(event.world);
//
//		applyDeferredPotionEffects();
//		if (event.phase == TickEvent.Phase.END){
//			applyDeferredDimensionTransfers();
//		}
	}

//	private void applyDeferredPotionEffects(){
//		for (EntityLivingBase ent : ArsMagica2.proxy.getDeferredPotionEffects().keySet()){
//			ArrayList<PotionEffect> potions = ArsMagica2.proxy.getDeferredPotionEffects().get(ent);
//			for (PotionEffect effect : potions)
//				ent.addPotionEffect(effect);
//		}
//
//		ArsMagica2.proxy.clearDeferredPotionEffects();
//	}

//	private void applyDeferredDimensionTransfers(){
//		for (EntityLivingBase ent : ArsMagica2.proxy.getDeferredDimensionTransfers().keySet()){
//			DimensionUtilities.doDimensionTransfer(ent, ArsMagica2.proxy.getDeferredDimensionTransfers().get(ent));
//		}
//
//		ArsMagica2.proxy.clearDeferredDimensionTransfers();
//	}

//	private void applyDeferredTargetSets(){
//		Iterator<Entry<EntityLiving, EntityLivingBase>> it = targetsToSet.entrySet().iterator();
//		while (it.hasNext()){
//			Entry<EntityLiving, EntityLivingBase> entry = it.next();
//			if (entry.getKey() != null && !entry.getKey().isDead)
//				entry.getKey().setAttackTarget(entry.getValue());
//			it.remove();
//		}
//	}

	public void addDeferredTarget(EntityLiving ent, EntityLivingBase target){
		targetsToSet.put(ent, target);
	}

	public void blackoutArmorPiece(EntityPlayerMP player, EntityEquipmentSlot slot, int cooldown){
		AMNetHandler.INSTANCE.sendPacketToClientPlayer(player, AMPacketIDs.FLASH_ARMOR_PIECE, new AMDataWriter().add(slot.getIndex()).add(cooldown).generate());
	}

}
