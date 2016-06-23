package am2.handler;

import org.lwjgl.opengl.GL11;

import am2.ArsMagica2;
import am2.affinity.Affinity;
import am2.api.AffinityRegistry;
import am2.api.extensions.IAffinityData;
import am2.api.extensions.IEntityExtension;
import am2.config.AM2Config;
import am2.defs.BindingsDefs;
import am2.defs.BlockDefs;
import am2.defs.IDDefs;
import am2.defs.ItemDefs;
import am2.defs.SkillDefs;
import am2.extensions.AffinityData;
import am2.extensions.EntityExtension;
import am2.extensions.RiftStorage;
import am2.extensions.SkillData;
import am2.lore.ArcaneCompendium;
import am2.packet.MessageBoolean;
import am2.utils.RenderUtils;
import am2.utils.WorldUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
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
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
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
			if (mop != null && mop.typeOfHit.equals(RayTraceResult.Type.BLOCK)) {
				Minecraft.getMinecraft().thePlayer.setPosition(mop.getBlockPos().getX(), mop.getBlockPos().getY() + 2, mop.getBlockPos().getZ());
				FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(new Runnable() {
					
					@Override
					public void run() {
						EntityPlayer player = (EntityPlayer) FMLCommonHandler.instance().getMinecraftServerInstance().getEntityFromUuid(Minecraft.getMinecraft().thePlayer.getUniqueID());
						Vec3d vec = new Vec3d(player.getLookVec().xCoord * 32, player.getLookVec().yCoord * 32, player.getLookVec().zCoord * 32).add(player.getPositionVector());
						RayTraceResult mop = Minecraft.getMinecraft().theWorld.rayTraceBlocks(player.getPositionVector().addVector(0D, 1.2D, 0D), vec);
						player.setPosition(mop.getBlockPos().getX(), mop.getBlockPos().getY() + 2, mop.getBlockPos().getZ());
					}
				});
			}
		}
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
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void renderTick (RenderTickEvent event) {
		int h = Minecraft.getMinecraft().displayHeight / 2;
		int w = Minecraft.getMinecraft().displayWidth / 2;
		if (Minecraft.getMinecraft().theWorld != null && (Minecraft.getMinecraft().inGameHasFocus) && (Minecraft.isGuiEnabled())) {
			Affinity highest = null;
			for (Affinity aff : AffinityRegistry.getAffinityMap().values()) {
				if (aff.equals(SkillDefs.NONE))
					continue;
				if (highest == null || AffinityData.For(Minecraft.getMinecraft().thePlayer).getAffinityDepth(aff) > AffinityData.For(Minecraft.getMinecraft().thePlayer).getAffinityDepth(highest))
					highest = aff;
			}
			int meta = 0;
			for (Affinity aff : AffinityRegistry.getAffinityMap().values()) {
				if (aff.equals(SkillDefs.NONE))
					continue;				
				if (aff.equals(highest))
					break;
				meta++;
			}
			if (!highest.equals(SkillDefs.NONE) && AffinityData.For(Minecraft.getMinecraft().thePlayer).getAffinityDepth(highest) > 0.01) {
				Minecraft.getMinecraft().getRenderItem().renderItemIntoGUI(new ItemStack(ItemDefs.essence, 1, meta), 0, 0);
				Minecraft.getMinecraft().fontRendererObj.drawString("" + Math.floor(AffinityData.For(Minecraft.getMinecraft().thePlayer).getAffinityDepth(highest) * 10000) / 100 + "%", 20, 4, highest.getColor());
				GL11.glColor4f(1, 1, 1, 1);
			}
			Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(ArsMagica2.MODID, "textures/gui/overlay.png"));
			GlStateManager.enableBlend();
			int burnoutBarX = (int) (w/2 - w/2.5);
			int burnoutBarY = h - h/8;
			int manaBarX = (int) (w/2 + w/5);
			int manaBarY = h - h/8;
			//System.out.println(EntityExtension.For(Minecraft.getMinecraft().thePlayer).getMaxMana());
			float burnout = -1F;// * AffinityData.For(Minecraft.getMinecraft().thePlayer).getAffinityDepth(SkillDefs.NATURE);
			float mana = -1F;
			if (EntityExtension.For(Minecraft.getMinecraft().thePlayer).getMaxMana() != 0)
				mana = ((float)EntityExtension.For(Minecraft.getMinecraft().thePlayer).getCurrentMana()) / ((float)EntityExtension.For(Minecraft.getMinecraft().thePlayer).getMaxMana());
			if (EntityExtension.For(Minecraft.getMinecraft().thePlayer).getMaxBurnout() != 0)
				burnout = (float)EntityExtension.For(Minecraft.getMinecraft().thePlayer).getCurrentBurnout() / (float)EntityExtension.For(Minecraft.getMinecraft().thePlayer).getMaxBurnout();
			//System.out.println(mana);
			float f = 1F/256F;
			RenderUtils.drawBox(burnoutBarX + 0.5F, burnoutBarY + 0.5F, (192/2 * burnout), 17 / 2, 0, 1F*f, 1F*f, 193F * burnout * f, 17F * f);
			RenderUtils.drawBox(burnoutBarX, burnoutBarY, 194/2, 19/2, 0F, 0F, 0.07F, 194*f, 0.14F);
			
			RenderUtils.drawBox(manaBarX + 0.5F, manaBarY + 0.5F,(int) (192/2 * mana), 17 / 2, 0, 0F, 0.15F, 193F * mana * f, 0.20F);
			RenderUtils.drawBox(manaBarX, manaBarY, 194/2, 19/2, 0, 0F, 0.2105F, 194*f, 0.28F);
			Minecraft.getMinecraft().fontRendererObj.drawString("Mana : " + (int)EntityExtension.For(Minecraft.getMinecraft().thePlayer).getCurrentMana() + "/" + (int)EntityExtension.For(Minecraft.getMinecraft().thePlayer).getMaxMana(), manaBarX, manaBarY-15, 0x00ffff);
			Minecraft.getMinecraft().fontRendererObj.drawString("Burnout : " + (int)EntityExtension.For(Minecraft.getMinecraft().thePlayer).getCurrentBurnout() + "/" + (int)EntityExtension.For(Minecraft.getMinecraft().thePlayer).getMaxBurnout(), burnoutBarX, burnoutBarY-15, 0xff0000);
			GlStateManager.color(1F, 1F, 1F);
			GlStateManager.disableBlend();
		}
	}
	
	public void playerTick (EntityPlayer player) {
		Minecraft.getMinecraft().mcProfiler.startSection("am2-playertick");
		//player.addPotionEffect(new BuffEffectTemporalAnchor(200, 0));
		IEntityExtension ext = player.getCapability(EntityExtension.INSTANCE, null);
		IAffinityData affData = player.getCapability(AffinityData.INSTANCE, null);
		float natureDepth = affData.getAffinityDepth(SkillDefs.NATURE);
		float lifeDepth = affData.getAffinityDepth(SkillDefs.LIFE);
		float lightningDepth = affData.getAffinityDepth(SkillDefs.LIGHTNING);
		float iceDepth = affData.getAffinityDepth(SkillDefs.ICE);
		
		float manaPerSecond = ext.getMaxMana() / 100;
		float burnoutPerSecond = Math.max(1, ext.getMaxBurnout() / 100);
		if (!player.worldObj.isRemote && player.ticksExisted % 20 == 0) {// && ext.getCurrentMana() < ext.getMaxMana() && player.ticksExisted % 40 == 0) {
			ext.setCurrentMana(ext.getCurrentMana() + manaPerSecond);
			if (ext.getCurrentMana() > ext.getMaxMana()) {
				ext.setCurrentMana(ext.getMaxMana());
			}
			ext.setCurrentBurnout(ext.getCurrentBurnout() - burnoutPerSecond);
			if (ext.getCurrentBurnout() < 0) {
				ext.setCurrentBurnout(0);
			}
		}
		//ext.setCurrentLevel(25);
		AM2Config.maxManaGrowth = 1.15F;
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
		Minecraft.getMinecraft().mcProfiler.endSection();
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
}
