package am2.handler;

import org.lwjgl.opengl.GL11;

import am2.ArsMagica2;
import am2.api.IBoundItem;
import am2.api.extensions.IAffinityData;
import am2.api.extensions.IEntityExtension;
import am2.defs.BindingsDefs;
import am2.defs.BlockDefs;
import am2.defs.IDDefs;
import am2.defs.ItemDefs;
import am2.defs.PotionEffectsDefs;
import am2.defs.SkillDefs;
import am2.extensions.AffinityData;
import am2.extensions.EntityExtension;
import am2.extensions.RiftStorage;
import am2.extensions.SkillData;
import am2.lore.ArcaneCompendium;
import am2.packet.MessageBoolean;
import am2.spell.ContingencyType;
import am2.utils.EntityUtils;
import am2.utils.SpellUtils;
import am2.utils.WorldUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityHandler {
		
	@SubscribeEvent
	public void onKeyInput(InputEvent.KeyInputEvent event) {
		if (BindingsDefs.iceBridge.isPressed()) {
			AffinityData.For(Minecraft.getMinecraft().thePlayer).setIceBridgeState(!AffinityData.For(Minecraft.getMinecraft().thePlayer).getIceBridgeState());
			ArsMagica2.network.sendToServer(new MessageBoolean(AffinityData.For(Minecraft.getMinecraft().thePlayer).getIceBridgeState()));
		}
		if (BindingsDefs.enderTP.isPressed() && AffinityData.For(Minecraft.getMinecraft().thePlayer).getAffinityDepth(SkillDefs.ENDER) > 0.8) {
			Vec3d vec = new Vec3d(Minecraft.getMinecraft().thePlayer.getLookVec().xCoord * 32, Minecraft.getMinecraft().thePlayer.getLookVec().yCoord * 32, Minecraft.getMinecraft().thePlayer.getLookVec().zCoord * 32).add(Minecraft.getMinecraft().thePlayer.getPositionVector());
			RayTraceResult mop = Minecraft.getMinecraft().theWorld.rayTraceBlocks(Minecraft.getMinecraft().thePlayer.getPositionVector().addVector(0D, 1.2D, 0D), vec);
			EnderTeleportEvent tp = new EnderTeleportEvent(Minecraft.getMinecraft().thePlayer, mop.hitVec.xCoord, mop.hitVec.yCoord, mop.hitVec.zCoord, 0F);
			if (MinecraftForge.EVENT_BUS.post(tp)) return;
			Minecraft.getMinecraft().thePlayer.setPosition(tp.getTargetX(), tp.getTargetY(), tp.getTargetZ());
			if (mop != null && mop.typeOfHit.equals(RayTraceResult.Type.BLOCK)) {
				Minecraft.getMinecraft().thePlayer.setPosition(mop.getBlockPos().getX(), mop.getBlockPos().getY() + 2, mop.getBlockPos().getZ());
				FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(new Runnable() {
					
					@Override
					public void run() {
						EntityPlayer player = (EntityPlayer) FMLCommonHandler.instance().getMinecraftServerInstance().getEntityFromUuid(Minecraft.getMinecraft().thePlayer.getUniqueID());
						Vec3d vec = new Vec3d(player.getLookVec().xCoord * 32, player.getLookVec().yCoord * 32, player.getLookVec().zCoord * 32).add(player.getPositionVector());
						RayTraceResult mop = Minecraft.getMinecraft().theWorld.rayTraceBlocks(player.getPositionVector().addVector(0D, 1.2D, 0D), vec);
						EnderTeleportEvent tp = new EnderTeleportEvent(Minecraft.getMinecraft().thePlayer, mop.hitVec.xCoord, mop.hitVec.yCoord, mop.hitVec.zCoord, 0F);
						if (MinecraftForge.EVENT_BUS.post(tp)) return;
						Minecraft.getMinecraft().thePlayer.setPosition(tp.getTargetX(), tp.getTargetY(), tp.getTargetZ());
					}
				});
			}
		}
	}
	
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
		float natureDepth = affData.getAffinityDepth(SkillDefs.NATURE);
		float lifeDepth = affData.getAffinityDepth(SkillDefs.LIFE);
		float lightningDepth = affData.getAffinityDepth(SkillDefs.LIGHTNING);
		float iceDepth = affData.getAffinityDepth(SkillDefs.ICE);
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
		//ext.setCurrentLevel(25);
		ext.lowerHealCooldown((int) (Math.max(1, lifeDepth * 10F)));
		ext.lowerAffinityHealCooldown((int) (Math.max(1, lifeDepth * 10F)));
		
		//Nature Affinity
		if (natureDepth >= 0.1F) {
			if (player.worldObj.canBlockSeeSky(player.getPosition()) && ext.getAffinityHealCooldown() == 0 && player.getHealth() != player.getMaxHealth() && player.worldObj.isDaytime() && !player.worldObj.isRaining()) {
				int chance = (int) (natureDepth * 10.0F);
				int output = player.worldObj.rand.nextInt(100);
				//System.out.println("Chances : " + chance + " - Output : " + output);
				if (chance > output) {
					player.heal(natureDepth * 5.0F);
					ext.placeAffinityHealOnCooldown(true);
				} else {
					ext.placeAffinityHealOnCooldown(false);
				}
			}
		}
		AttributeModifier modNatureSpeed = new AttributeModifier(IDDefs.natureSpeed, "Nature Speed", natureDepth / 3.5F, 2);
		if (!player.worldObj.isRemote) {
			if (natureDepth >= 0.5F) {
				if (player.worldObj.getBlockState(player.getPosition().down()).getMaterial().equals(Material.GRASS) ||player.worldObj.getBlockState(player.getPosition().down().down()).getMaterial().equals(Material.GRASS)) {
					player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).removeModifier(modNatureSpeed);
					player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).applyModifier(modNatureSpeed);
					affData.setNatureSpeed(20);
				} else if (affData.getNatureSpeed() <= 0){
					player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).removeModifier(modNatureSpeed);
				} else if (affData.getNatureSpeed() > 0){
					affData.setNatureSpeed(affData.getNatureSpeed() - 1);;
				}
			} else {// if (!player.worldObj.isRemote){
				player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).removeModifier(modNatureSpeed);
			}
		}
		if (natureDepth == 1.0F && player.worldObj.canBlockSeeSky(player.getPosition())) {
			player.addPotionEffect(new PotionEffect(MobEffects.SATURATION, 20));
		}
		
		//Life Affinity
		AttributeModifier modLifeHalth = new AttributeModifier(IDDefs.lifeHealth, "Life Health", ((double)lifeDepth) * 20D, 0);
		if (!player.worldObj.isRemote) {
			if (lifeDepth >= 0.1F) {
				player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).removeModifier(modLifeHalth);	
				player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(modLifeHalth);
			} else if (!player.worldObj.isRemote){
				player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).removeModifier(modLifeHalth);		
			}
		}
		
		//Lightning Affinity
		
		AttributeModifier modLightning = new AttributeModifier(IDDefs.lightningSpeed, "Lightning Speed", lightningDepth / 5F, 2);
		if (!player.worldObj.isRemote) {
			if (lightningDepth >= 0.1F) {
				player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).removeModifier(modLightning);
				player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).applyModifier(modLightning);
			} else {
				player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).removeModifier(modLightning);
			}
		}
		if (lightningDepth >= 0.5f){
			player.stepHeight = 1.014f;
		}
		else if (player.stepHeight == 1.014f){
			player.stepHeight = 0.6f;
		}
		
		//Ice Affinity
		makeIceBridge(player);
		
		AttributeModifier modIceSpeed = new AttributeModifier(IDDefs.iceSpeed, "Ice Speed", iceDepth / 3.5F, 2);
		if (!player.worldObj.isRemote) {
			if (iceDepth >= 0.25F) {
				if (player.worldObj.getBlockState(player.getPosition().down()).getMaterial().equals(Material.ICE) ||player.worldObj.getBlockState(player.getPosition().down().down()).getMaterial().equals(Material.ICE)) {
					player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).removeModifier(modIceSpeed);
					player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).applyModifier(modIceSpeed);
					affData.setIceSpeed(20);
				} else if (affData.getIceSpeed() <= 0){
					player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).removeModifier(modIceSpeed);
				} else if (affData.getIceSpeed() > 0){
					affData.setIceSpeed(affData.getIceSpeed() - 1);
				}
			} else {// if (!player.worldObj.isRemote){
				player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).removeModifier(modIceSpeed);
			}
		}
	}
	
	public void makeIceBridge (EntityPlayer player) {
		IAffinityData affData = player.getCapability(AffinityData.INSTANCE, null);
		float iceDepth = affData.getAffinityDepth(SkillDefs.ICE);
		if (affData.getIceBridgeState() && iceDepth >= 0.5F && !player.worldObj.isRemote && ((!player.isSneaking() && player.onGround) || (player.isSneaking() && !player.onGround))) {
			//System.out.println(player.worldObj.isRemote);
			for (int x = -1; x <= 1; x++) {
				for (int z = -1; z <= 1; z++) {
					Block block = WorldUtils.getBlockAt(player.worldObj, new BlockPos(player.getPosition().getX() + x, player.getPosition().getY() - (player.onGround ? 1 : 2), player.getPosition().getZ() + z));
					if (block.equals(Blocks.WATER) || block.equals(Blocks.FLOWING_WATER))
						player.worldObj.setBlockState(new BlockPos(player.getPosition().getX() + x, player.getPosition().getY() - (player.onGround ? 1 : 2), player.getPosition().getZ() + z), Blocks.ICE.getDefaultState(), 3);
					else if ((block.equals(Blocks.LAVA) || block.equals(Blocks.FLOWING_LAVA)) && iceDepth >= 0.75F) {
						player.worldObj.setBlockState(new BlockPos(player.getPosition().getX() + x, player.getPosition().getY() - (player.onGround ? 1 : 2), player.getPosition().getZ() + z), Blocks.OBSIDIAN.getDefaultState(), 3);
					}
					else if (block.equals(Blocks.AIR) && iceDepth >= 0.90F && player.worldObj.isRaining()) {
						player.worldObj.setBlockState(new BlockPos(player.getPosition().getX() + x, player.getPosition().getY() - (player.onGround ? 1 : 2), player.getPosition().getZ() + z), BlockDefs.frost.getDefaultState(), 3);
						//player.worldObj.markBlockForUpdate(new BlockPos(player.getPosition().getX() + x, player.getPosition().getY() - 1, player.getPosition().getZ() + z));
					}
				}			
			}
		}
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
					EntityLivingBase target = e.getSource().getSourceOfDamage() instanceof EntityLivingBase ? (EntityLivingBase)e.getSource().getSourceOfDamage() : null;
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
