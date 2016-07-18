package am2.handler;

import org.lwjgl.opengl.GL11;

import am2.ArsMagica2;
import am2.api.IBoundItem;
import am2.api.affinity.Affinity;
import am2.api.extensions.IAffinityData;
import am2.api.extensions.IEntityExtension;
import am2.armor.ArmorHelper;
import am2.armor.infusions.GenericImbuement;
import am2.defs.ItemDefs;
import am2.defs.PotionEffectsDefs;
import am2.defs.SkillDefs;
import am2.extensions.AffinityData;
import am2.extensions.EntityExtension;
import am2.extensions.RiftStorage;
import am2.extensions.SkillData;
import am2.lore.ArcaneCompendium;
import am2.spell.ContingencyType;
import am2.utils.EntityUtils;
import am2.utils.SpellUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityHandler {
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onMouseEvent(MouseEvent event){
		event.setCanceled(ArsMagica2.proxy.setMouseDWheel(event.getDwheel()));
	}
	
	@SubscribeEvent
	public void onEntityConstruction(AttachCapabilitiesEvent.Entity event) {
		if (event.getEntity() instanceof EntityLivingBase) {
			EntityExtension ext = new EntityExtension();
			ext.init(event.getEntity());
			event.addCapability(EntityExtension.ID, ext);
			if (event.getEntity() instanceof EntityPlayer) {
				ArcaneCompendium compendium = new ArcaneCompendium();
				AffinityData affData = new AffinityData();
				SkillData skillData = new SkillData();
				RiftStorage storage = new RiftStorage();
				affData.init((EntityPlayer) event.getEntity());
				skillData.init((EntityPlayer) event.getEntity());
				compendium.init((EntityPlayer) event.getEntity());
				event.addCapability(new ResourceLocation("arsmagica2", "Compendium"), compendium);
				event.addCapability(SkillData.ID, skillData);
				event.addCapability(AffinityData.ID, affData);
				event.addCapability(new ResourceLocation("arsmagica2", "RiftStorage"), storage);
			}
		}
	}
	
	@SubscribeEvent
	public void onClonePlayer(PlayerEvent.Clone event) {
		transferCapability(AffinityData.INSTANCE, AffinityData.For(event.getOriginal()), AffinityData.For(event.getEntityPlayer()));
		transferCapability(EntityExtension.INSTANCE, EntityExtension.For(event.getOriginal()), EntityExtension.For(event.getEntityPlayer()));
		transferCapability(SkillData.INSTANCE, SkillData.For(event.getOriginal()), SkillData.For(event.getEntityPlayer()));
		transferCapability(RiftStorage.INSTANCE, RiftStorage.For(event.getOriginal()), RiftStorage.For(event.getEntityPlayer()));
		transferCapability(ArcaneCompendium.INSTANCE, ArcaneCompendium.For(event.getOriginal()), ArcaneCompendium.For(event.getEntityPlayer()));
		
	}
	
	private <T> void transferCapability(Capability<T> capability, T original, T target) {
		capability.getStorage().readNBT(capability, target, null, capability.getStorage().writeNBT(capability, original, null));		
	}
	
	@SubscribeEvent
	public void entityTick (LivingUpdateEvent event) {
		if (event.getEntityLiving() instanceof EntityPlayer) playerTick((EntityPlayer) event.getEntityLiving());
		IEntityExtension ext = EntityExtension.For(event.getEntityLiving());
		ContingencyType type = ext.getContingencyType();
		if (event.getEntityLiving().isBurning() && type == ContingencyType.FIRE) {
			SpellUtils.applyStackStage(ext.getContingencyStack(), event.getEntityLiving(), null, event.getEntity().posX, event.getEntity().posY, event.getEntity().posZ, null, event.getEntityLiving().worldObj, false, true, 0);
			if (ext.getContingencyType() == ContingencyType.FIRE)
				ext.setContingency(ContingencyType.NULL, null);		
		}
		else if (event.getEntityLiving().getHealth() * 4 < event.getEntityLiving().getMaxHealth() && type == ContingencyType.HEALTH) {
			SpellUtils.applyStackStage(ext.getContingencyStack(), event.getEntityLiving(), null, event.getEntity().posX, event.getEntity().posY, event.getEntity().posZ, null, event.getEntityLiving().worldObj, false, true, 0);			
			if (ext.getContingencyType() == ContingencyType.HEALTH) {
				ext.setContingency(ContingencyType.NULL, null);
			}
		}
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void renderTick (RenderGameOverlayEvent event) {
		GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
		GL11.glPushMatrix();
		if (event.getType() == ElementType.CROSSHAIRS)
			ArsMagica2.proxy.renderGameOverlay();
		GL11.glPopMatrix();
		GL11.glPopAttrib();
	}
	
	public void playerTick (EntityPlayer player) {
		//player.addPotionEffect(new BuffEffectTemporalAnchor(200, 0));
		IEntityExtension ext = player.getCapability(EntityExtension.INSTANCE, null);
		IAffinityData affData = player.getCapability(AffinityData.INSTANCE, null);
		float manaMultiplier = 1;
		if (SkillData.For(player).hasSkill(SkillDefs.MANA_REGEN_1.getID()))
			manaMultiplier *= 1.5F;
		if (SkillData.For(player).hasSkill(SkillDefs.MANA_REGEN_2.getID()))
			manaMultiplier *= 1.5F;
		if (SkillData.For(player).hasSkill(SkillDefs.MANA_REGEN_3.getID()))
			manaMultiplier *= 1.5F;
		float manaPerSecond = ext.getMaxMana() / 100F / 20F;
		manaPerSecond *= manaMultiplier;
		float burnoutPerSecond = Math.max(1, ext.getMaxBurnout() / 100F / 20F);
		if (!player.worldObj.isRemote) {// && ext.getCurrentMana() < ext.getMaxMana() && player.ticksExisted % 40 == 0) {
			ext.setCurrentMana(ext.getCurrentMana() + manaPerSecond);
			if (ext.getCurrentMana() > ext.getMaxMana()) {
				ext.setCurrentMana(ext.getMaxMana());
			}
			ext.setCurrentBurnout(ext.getCurrentBurnout() - burnoutPerSecond);
			if (ext.getCurrentBurnout() < 0) {
				ext.setCurrentBurnout(0);
			}
			affData.tickDiminishingReturns();
		}
		if (!player.capabilities.isCreativeMode) {
			for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
				ItemStack stack = player.inventory.getStackInSlot(i);
				if (stack != null && stack.getItem() instanceof IBoundItem) {
					if (ext.hasEnoughtMana(((IBoundItem)stack.getItem()).maintainCost(player, stack)))
						ext.deductMana(((IBoundItem)stack.getItem()).maintainCost(player, stack));
					else 
						stack.getItem().onDroppedByPlayer(stack, player);
				}
			}
		}
		if (ArmorHelper.isInfusionPreset(player.getItemStackFromSlot(EntityEquipmentSlot.LEGS), GenericImbuement.stepAssist)){
			player.stepHeight = 1.0111f;
		}else if (player.stepHeight == 1.0111f){
			player.stepHeight = 0.5f;
		}

		IAttributeInstance attr = player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
		if (ArmorHelper.isInfusionPreset(player.getItemStackFromSlot(EntityEquipmentSlot.FEET), GenericImbuement.runSpeed)){
			if (attr.getModifier(GenericImbuement.imbuedHasteID) == null){
				attr.applyModifier(GenericImbuement.imbuedHaste);
			}
		}else{
			if (attr.getModifier(GenericImbuement.imbuedHasteID) != null){
				attr.removeModifier(GenericImbuement.imbuedHaste);
			}
		}
		float lifeDepth = affData.getAffinityDepth(Affinity.LIFE);
		ext.lowerHealCooldown((int) (Math.max(1, lifeDepth  * 10F)));
		ext.lowerAffinityHealCooldown((int) (Math.max(1, lifeDepth * 10F)));
	}
	
	@SubscribeEvent
	public void entityDeath(LivingDeathEvent e) {
		IEntityExtension ext = EntityExtension.For(e.getEntityLiving());
		ContingencyType type = ext.getContingencyType();
		EntityLivingBase target = null;
		if (e.getSource() != null && e.getSource().getSourceOfDamage() instanceof EntityLivingBase)
			target = e.getSource().getSourceOfDamage() != null ? (EntityLivingBase)e.getSource().getSourceOfDamage() : null;
		if (type == ContingencyType.DEATH) {
			SpellUtils.applyStackStage(ext.getContingencyStack(), e.getEntityLiving(), target, e.getEntity().posX, e.getEntity().posY, e.getEntity().posZ, null, e.getEntityLiving().worldObj, false, true, 0);
			if (ext.getContingencyType() == ContingencyType.DEATH)
				ext.setContingency(ContingencyType.NULL, null);		
		}
	}
	
	@SubscribeEvent
	public void teleportEvent(EnderTeleportEvent e) {
		if (e.getEntityLiving().isPotionActive(PotionEffectsDefs.astralDistortion) || e.getEntity().isDead) {
			e.setCanceled(true);
		}
	}
	
	@SuppressWarnings("deprecation")
	@SubscribeEvent
	public void attackEntity(LivingAttackEvent e) {
		if (e.getEntityLiving() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) e.getEntityLiving();
			ItemStack stack = player.getActiveItemStack();
			if (e.getAmount() > 0.0F && stack != null && EntityUtils.canBlockDamageSource(player, e.getSource()) && stack.getItem() == ItemDefs.BoundShield) {
				if (!EntityExtension.For(player).useMana(e.getAmount() * 10)) {
					stack.getItem().onDroppedByPlayer(stack, player);
				} else if (EntityExtension.For(player).hasEnoughtMana(SpellUtils.getManaCost(stack))) {
					EntityLivingBase target = e.getSource().getEntity() instanceof EntityLivingBase ? (EntityLivingBase)e.getSource().getEntity() : null;
					double posX = target != null ? target.posX : player.posX;
					double posY = target != null ? target.posY : player.posY;
					double posZ = target != null ? target.posZ : player.posZ;
					ItemStack copiedStack = SpellUtils.merge(stack.copy());
					copiedStack.getTagCompound().getCompoundTag("AM2").setInteger("CurrentGroup", SpellUtils.currentStage(stack) + 1);
					copiedStack.setItem(ItemDefs.spell);
					SpellUtils.applyStackStage(copiedStack, player, target, posX, posY, posZ, null, player.worldObj, true, true, 0);
				} else {
					stack.getItem().onDroppedByPlayer(stack, player);
				}
			}
		}
	}
}
