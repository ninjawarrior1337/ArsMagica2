package am2;

import am2.api.events.ManaCostEvent;
import am2.api.spell.enums.Affinity;
import am2.api.spell.enums.BuffPowerLevel;
import am2.api.spell.enums.ContingencyTypes;
import am2.armor.ArmorHelper;
import am2.armor.infusions.GenericImbuement;
import am2.blocks.BlocksCommonProxy;
import am2.blocks.tileentities.TileEntityAstralBarrier;
import am2.bosses.BossSpawnHelper;
import am2.buffs.BuffEffectTemporalAnchor;
import am2.buffs.BuffList;
import am2.buffs.BuffStatModifiers;
import am2.damage.DamageSources;
import am2.entities.EntityFlicker;
import am2.items.ItemsCommonProxy;
import am2.network.AMNetHandler;
import am2.playerextensions.AffinityData;
import am2.playerextensions.ExtendedProperties;
import am2.playerextensions.SkillData;
import am2.utility.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.event.brewing.PotionBrewEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class AMEventHandler {

    @SubscribeEvent
    public void onPotionBrewed(PotionBrewEvent brewEvent) {
        for (int i = 0; i < brewEvent.getLength(); i++) {
            ItemStack stack = brewEvent.getItem(i);
            if (stack == null) continue;
            if (stack.getItem() instanceof ItemPotion) {
                ItemPotion ptn = ((ItemPotion) stack.getItem());
                List<PotionEffect> fx = PotionUtils.getEffectsFromStack(stack);
                if (fx == null) return;
                for (PotionEffect pe : fx) {
                    if (pe.getPotion() == BuffList.greaterManaPotion) {
                        stack = InventoryUtilities.replaceItem(stack, ItemsCommonProxy.greaterManaPotion);
                        break;
                    } else if (pe.getPotion() == BuffList.epicManaPotion) {
                        stack = InventoryUtilities.replaceItem(stack, ItemsCommonProxy.epicManaPotion);
                        break;
                    } else if (pe.getPotion() == BuffList.legendaryManaPotion) {
                        stack = InventoryUtilities.replaceItem(stack, ItemsCommonProxy.legendaryManaPotion);
                        break;
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onEndermanTeleport(EnderTeleportEvent event) {
        EntityLivingBase ent = event.getEntityLiving();


        ArrayList<Long> keystoneKeys = KeystoneUtilities.instance.GetKeysInInvenory(ent);
        TileEntityAstralBarrier blockingBarrier = DimensionUtilities.GetBlockingAstralBarrier(event.getEntityLiving().worldObj, (int) event.getTargetX(), (int) event.getTargetY(), (int) event.getTargetZ(), keystoneKeys);

        if (ent.isPotionActive(BuffList.astralDistortion) || blockingBarrier != null) {
            event.setCanceled(true);
            if (blockingBarrier != null) {
                blockingBarrier.onEntityBlocked(ent);
            }
            return;
        }

        if (!ent.worldObj.isRemote && ent instanceof EntityEnderman && ent.worldObj.rand.nextDouble() < 0.01f) {
            EntityFlicker flicker = new EntityFlicker(ent.worldObj);
            flicker.setPosition(ent.posX, ent.posY, ent.posZ);
            flicker.setFlickerType(Affinity.ENDER);
            ent.worldObj.spawnEntityInWorld(flicker);
        }
    }

    @SubscribeEvent
    public void onEntityConstructing(EntityEvent.EntityConstructing event) {
        /*if (event.getEntity() instanceof EntityLivingBase) {
            event.getEntity().registerExtendedProperties(ExtendedProperties.identifier, new ExtendedProperties());
            ((EntityLivingBase) event.getEntity()).getAttributeMap().registerAttribute(ArsMagicaApi.maxManaBonus);
            ((EntityLivingBase) event.getEntity()).getAttributeMap().registerAttribute(ArsMagicaApi.maxBurnoutBonus);
            ((EntityLivingBase) event.getEntity()).getAttributeMap().registerAttribute(ArsMagicaApi.xpGainModifier);
            ((EntityLivingBase) event.getEntity()).getAttributeMap().registerAttribute(ArsMagicaApi.burnoutReductionRate);
            ((EntityLivingBase) event.getEntity()).getAttributeMap().registerAttribute(ArsMagicaApi.manaRegenTimeModifier);

            if (event.getEntity() instanceof EntityPlayer) {
                event.getEntity().registerExtendedProperties(RiftStorage.identifier, new RiftStorage());
                event.getEntity().registerExtendedProperties(AffinityData.identifier, new AffinityData());
                event.getEntity().registerExtendedProperties(SkillData.identifier, new SkillData((EntityPlayer) event.getEntity()));
            }
        } else if (event.getEntity() instanceof EntityItemFrame) {
            AMCore.proxy.itemFrameWatcher.startWatchingFrame((EntityItemFrame) event.getEntity());
        }*/
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onEntityDeath(LivingDeathEvent event) {
        EntityLivingBase soonToBeDead = event.getEntityLiving();
        if (soonToBeDead.isPotionActive(BuffList.temporalAnchor)) {
            event.setCanceled(true);
            PotionEffect pe = soonToBeDead.getActivePotionEffect(BuffList.temporalAnchor);
            if (pe instanceof BuffEffectTemporalAnchor) {
                BuffEffectTemporalAnchor buff = (BuffEffectTemporalAnchor) pe;
                buff.stopEffect(soonToBeDead);
            }
            soonToBeDead.removePotionEffect(BuffList.temporalAnchor);
            return;
        }

        if (ExtendedProperties.For(soonToBeDead).getContingencyType() == ContingencyTypes.DEATH) {
            ExtendedProperties.For(soonToBeDead).procContingency();
        }

        if (soonToBeDead instanceof EntityPlayer) {
            AMCore.proxy.playerTracker.onPlayerDeath((EntityPlayer) soonToBeDead);
        } else if (soonToBeDead instanceof EntityCreature) {
            if (!EntityUtilities.isSummon(soonToBeDead) && EntityUtilities.isAIEnabled((EntityCreature) soonToBeDead) && event.getSource().getSourceOfDamage() instanceof EntityPlayer) {
                EntityUtilities.handleCrystalPhialAdd((EntityCreature) soonToBeDead, (EntityPlayer) event.getSource().getSourceOfDamage());
            }
        }

        if (EntityUtilities.isSummon(soonToBeDead)) {
            ReflectionHelper.setPrivateValue(EntityLivingBase.class, soonToBeDead, 0, "field_70718_bc", "recentlyHit");
            int ownerID = EntityUtilities.getOwner(soonToBeDead);
            Entity e = soonToBeDead.worldObj.getEntityByID(ownerID);
            if (e != null & e instanceof EntityLivingBase) {
                ExtendedProperties.For((EntityLivingBase) e).removeSummon();
            }
        }

        if (soonToBeDead instanceof EntityVillager && ((EntityVillager) soonToBeDead).isChild()) {
            BossSpawnHelper.instance.onVillagerChildKilled((EntityVillager) soonToBeDead);
        }
    }

    @SubscribeEvent
    public void onPlayerGetAchievement(AchievementEvent event) {
        if (!event.getEntityPlayer().worldObj.isRemote && event.getAchievement() == AchievementList.THE_END2) {
            AMCore.instance.proxy.playerTracker.storeExtendedPropertiesForRespawn(event.getEntityPlayer());
            // AMCore.instance.proxy.playerTracker.storeSoulboundItemsForRespawn(event.getEntityPlayer());
        }
    }

    @SubscribeEvent
    public void onLivingDrops(LivingDropsEvent event) {
        if (EntityUtilities.isSummon(event.getEntityLiving()) && !(event.getEntityLiving() instanceof EntityHorse)) {
            event.setCanceled(true);
        }
        if (event.getSource() == DamageSources.darkNexus) {
            event.setCanceled(true);
        }
        if (!event.getEntityLiving().worldObj.isRemote && event.getEntityLiving() instanceof EntityPig && event.getEntityLiving().getRNG().nextDouble() < 0.3f) {
            EntityItem animalFat = new EntityItem(event.getEntityLiving().worldObj);
            ItemStack stack = new ItemStack(ItemsCommonProxy.itemOre, 1, ItemsCommonProxy.itemOre.META_ANIMALFAT);
            animalFat.setPosition(event.getEntity().posX, event.getEntity().posY, event.getEntity().posZ);
            animalFat.setEntityItemStack(stack);
            event.getDrops().add(animalFat);
        }
    }

    @SubscribeEvent
    public void onEntityJump(LivingEvent.LivingJumpEvent event) {
        if (event.getEntityLiving().isPotionActive(BuffList.agility)) {
            event.getEntityLiving().motionY *= 1.5f;
        }
        if (event.getEntityLiving().isPotionActive(BuffList.leap)) {

            Entity velocityTarget = event.getEntityLiving();

            if (event.getEntityLiving().getRidingEntity() != null) {
                if (event.getEntityLiving().getRidingEntity() instanceof EntityMinecart) {
                    event.getEntityLiving().getRidingEntity().setPosition(event.getEntityLiving().getRidingEntity().posX, event.getEntityLiving().getRidingEntity().posY + 1.5, event.getEntityLiving().getRidingEntity().posZ);
                }
                velocityTarget = event.getEntityLiving().getRidingEntity();
            }

            double yVelocity = 0;
            double xVelocity = 0;
            double zVelocity = 0;

            Vec3d vec = event.getEntityLiving().getLookVec().normalize();
            switch (event.getEntityLiving().getActivePotionEffect(BuffList.leap).getAmplifier() + 1) {
                case BuffPowerLevel.Low:
                    yVelocity = 0.4;
                    xVelocity = velocityTarget.motionX * 1.08 * Math.abs(vec.xCoord);
                    zVelocity = velocityTarget.motionZ * 1.08 * Math.abs(vec.zCoord);
                    break;
                case BuffPowerLevel.Medium:
                    yVelocity = 0.7;
                    xVelocity = velocityTarget.motionX * 1.25 * Math.abs(vec.xCoord);
                    zVelocity = velocityTarget.motionZ * 1.25 * Math.abs(vec.zCoord);
                    break;
                case BuffPowerLevel.High:
                    yVelocity = 1;
                    xVelocity = velocityTarget.motionX * 1.75 * Math.abs(vec.xCoord);
                    zVelocity = velocityTarget.motionZ * 1.75 * Math.abs(vec.zCoord);
                    break;
                default:
                    break;
            }

            float maxHorizontalVelocity = 1.45f;

            if (event.getEntityLiving().getRidingEntity() != null && (event.getEntityLiving().getRidingEntity() instanceof EntityMinecart || event.getEntityLiving().getRidingEntity() instanceof EntityBoat) || event.getEntityLiving().isPotionActive(BuffList.haste)) {
                maxHorizontalVelocity += 25;
                xVelocity *= 2.5;
                zVelocity *= 2.5;
            }

            if (xVelocity > maxHorizontalVelocity) {
                xVelocity = maxHorizontalVelocity;
            } else if (xVelocity < -maxHorizontalVelocity) {
                xVelocity = -maxHorizontalVelocity;
            }

            if (zVelocity > maxHorizontalVelocity) {
                zVelocity = maxHorizontalVelocity;
            } else if (zVelocity < -maxHorizontalVelocity) {
                zVelocity = -maxHorizontalVelocity;
            }

            if (ExtendedProperties.For(event.getEntityLiving()).getIsFlipped()) {
                yVelocity *= -1;
            }

            velocityTarget.addVelocity(xVelocity, yVelocity, zVelocity);
        }
        if (event.getEntityLiving().isPotionActive(BuffList.entangled)) {
            event.getEntityLiving().motionY = 0;
        }

        if (event.getEntityLiving() instanceof EntityPlayer) {
            ItemStack boots = ((EntityPlayer) event.getEntityLiving()).inventory.armorInventory[0];
            if (boots != null && boots.getItem() == ItemsCommonProxy.enderBoots && event.getEntityLiving().isSneaking()) {
                ExtendedProperties.For(event.getEntityLiving()).toggleFlipped();
            }

            if (ExtendedProperties.For(event.getEntityLiving()).getFlipRotation() > 0)
                ((EntityPlayer) event.getEntityLiving()).addVelocity(0, -2 * event.getEntityLiving().motionY, 0);
        }
    }

    @SubscribeEvent
    public void onEntityFall(LivingFallEvent event) {

        EntityLivingBase ent = event.getEntityLiving();
        float f = event.getDistance();
        ent.isAirBorne = false;

        //slowfall buff
        if (ent.isPotionActive(BuffList.slowfall) || ent.isPotionActive(BuffList.shrink) || (ent instanceof EntityPlayer && AffinityData.For(ent).getAffinityDepth(Affinity.NATURE) == 1.0f)) {
            event.setCanceled(true);
            ent.fallDistance = 0;
            return;
        }

        //gravity well
        if (ent.isPotionActive(BuffList.gravityWell)) {
            ent.fallDistance *= 1.5f;
        }

        //fall protection stat
        f -= ExtendedProperties.For(ent).getFallProtection();
        ExtendedProperties.For(ent).setFallProtection(0);
        if (f <= 0) {
            ent.fallDistance = 0;
            event.setCanceled(true);
            return;
        }
    }

    @SubscribeEvent
    public void onEntityLiving(LivingEvent.LivingUpdateEvent event) {

        EntityLivingBase ent = event.getEntityLiving();

        World world = ent.worldObj;

        BuffStatModifiers.instance.applyStatModifiersBasedOnBuffs(ent);

        ExtendedProperties extendedProperties;
        extendedProperties = ExtendedProperties.For(ent);
        extendedProperties.handleSpecialSyncData();
        extendedProperties.manaBurnoutTick();

        //archmage armor effects & infusion
        if (ent instanceof EntityPlayer) {

            if (ent.worldObj.isRemote) {
                int divisor = extendedProperties.getAuraDelay() > 0 ? extendedProperties.getAuraDelay() : 1;
                if (ent.ticksExisted % divisor == 0)
                    AMCore.instance.proxy.particleManager.spawnAuraParticles(ent);
                AMCore.proxy.setViewSettings();
            }

            ArmorHelper.HandleArmorInfusion((EntityPlayer) ent);
            ArmorHelper.HandleArmorEffects((EntityPlayer) ent, world);

            extendedProperties.flipTick();

            if (extendedProperties.getIsFlipped()) {
                if (((EntityPlayer) ent).motionY < 2)
                    ((EntityPlayer) ent).motionY += 0.15f;

                double posY = ent.posY + ent.height;
                if (!world.isRemote)
                    posY += ent.getEyeHeight();
                if (world.rayTraceBlocks(new Vec3d(ent.posX, posY, ent.posZ), new Vec3d(ent.posX, posY + 1, ent.posZ), true) != null) {
                    if (!ent.onGround) {
                        if (ent.fallDistance > 0) {
                            try {
                                Method m = ReflectionHelper.findMethod(Entity.class, ent, new String[]{"func_70069_a", "fall"}, float.class);
                                m.setAccessible(true);
                                m.invoke(ent, ent.fallDistance);
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }
                            ent.fallDistance = 0;
                        }
                    }
                    ent.onGround = true;
                } else {
                    if (ent.motionY > 0) {
                        ent.fallDistance += ent.posY - ent.prevPosY;
                    }
                    ent.onGround = false;
                }
            }

            if (ArmorHelper.isInfusionPreset(((EntityPlayer) ent).getItemStackFromSlot(EntityEquipmentSlot.LEGS), GenericImbuement.stepAssist)) {
                ent.stepHeight = 1.0111f;
            } else if (ent.stepHeight == 1.0111f) {
                ent.stepHeight = 0.5f;
            }

            IAttributeInstance attr = ent.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
            if (ArmorHelper.isInfusionPreset(((EntityPlayer) ent).getItemStackFromSlot(EntityEquipmentSlot.FEET), GenericImbuement.runSpeed)) {
                if (attr.getModifier(GenericImbuement.imbuedHasteID) == null) {
                    attr.applyModifier(GenericImbuement.imbuedHaste);
                }
            } else {
                if (attr.getModifier(GenericImbuement.imbuedHasteID) != null) {
                    attr.removeModifier(GenericImbuement.imbuedHaste);
                }
            }
        }

        if (!ent.onGround && ent.fallDistance >= 4f && extendedProperties.getContingencyType() == ContingencyTypes.FALL && extendedProperties.getContingencyEffect() != null) {
            int distanceToGround = MathUtilities.getDistanceToGround(ent, world);
            if (distanceToGround < -8 * ent.motionY) {
                extendedProperties.procContingency();
            }
        }
        if (extendedProperties.getContingencyType() == ContingencyTypes.ON_FIRE && ent.isBurning()) {
            extendedProperties.procContingency();
        }

        if (!ent.worldObj.isRemote && ent.ticksExisted % 200 == 0) {
            extendedProperties.setSyncAuras();
        }

        //buff particles
        //if (ent.worldObj.isRemote)
        //	AMCore.instance.proxy.particleManager.spawnBuffParticles(ent);

        //data sync
        extendedProperties.handleExtendedPropertySync();

        if (ent instanceof EntityPlayer) {
            AffinityData.For(ent).handleExtendedPropertySync();
            SkillData.For((EntityPlayer) ent).handleExtendedPropertySync();

            if (ent.isPotionActive(BuffList.flight) || ent.isPotionActive(BuffList.levitation) || ((EntityPlayer) ent).capabilities.isCreativeMode) {
                extendedProperties.hadFlight = true;
                if (ent.isPotionActive(BuffList.levitation)) {
                    if (((EntityPlayer) ent).capabilities.isFlying) {
                        float factor = 0.4f;
                        ent.motionX *= factor;
                        ent.motionZ *= factor;
                        ent.motionY *= 0.0001f;
                    }
                }
            } else if (extendedProperties.hadFlight) {
                ((EntityPlayer) ent).capabilities.allowFlying = false;
                ((EntityPlayer) ent).capabilities.isFlying = false;
                extendedProperties.hadFlight = false;
            }
        }

        if (ent.isPotionActive(BuffList.agility)) {
            ent.stepHeight = 1.01f;
        } else if (ent.stepHeight == 1.01f) {
            ent.stepHeight = 0.5f;
        }

        if (!ent.worldObj.isRemote && EntityUtilities.isSummon(ent) && !EntityUtilities.isTileSpawnedAndValid(ent)) {
            int owner = EntityUtilities.getOwner(ent);
            Entity ownerEnt = ent.worldObj.getEntityByID(owner);
            if (!EntityUtilities.decrementSummonDuration(ent)) {
                ent.attackEntityFrom(DamageSources.unsummon, 5000);
            }
            if (owner == -1 || ownerEnt == null || ownerEnt.isDead || ownerEnt.getDistanceSqToEntity(ent) > 900) {
                if (ent instanceof EntityLiving && !((EntityLiving) ent).getCustomNameTag().equals("")) {
                    EntityUtilities.setOwner(ent, null);
                    EntityUtilities.setSummonDuration(ent, -1);
                    EntityUtilities.revertAI((EntityCreature) ent);
                } else {
                    ent.attackEntityFrom(DamageSources.unsummon, 5000);
                }
            }
        }

        //leap buff
        if (event.getEntityLiving().isPotionActive(BuffList.leap)) {
            int amplifier = event.getEntityLiving().getActivePotionEffect(BuffList.leap).getAmplifier() + 1;

            switch (amplifier) {
                case BuffPowerLevel.Low:
                    extendedProperties.setFallProtection(8);
                    break;
                case BuffPowerLevel.Medium:
                    extendedProperties.setFallProtection(20);
                    break;
                case BuffPowerLevel.High:
                    extendedProperties.setFallProtection(45);
                    break;
                default:
                    break;
            }
        }

        if (event.getEntityLiving().isPotionActive(BuffList.gravityWell)) {
            if (event.getEntityLiving().motionY < 0 && event.getEntityLiving().motionY > -3f) {
                event.getEntityLiving().motionY *= 1.59999999999999998D;
            }
        }


        //slowfall/shrink buff
        // (isSneaking calls DataWatcher which are slow, so we test it late)
        if (event.getEntityLiving().isPotionActive(BuffList.slowfall)
                || event.getEntityLiving().isPotionActive(BuffList.shrink)
                || (ent instanceof EntityPlayer && AffinityData.For(ent).getAffinityDepth(Affinity.NATURE) == 1.0f && !ent.isSneaking())) {
            if (!event.getEntityLiving().onGround && event.getEntityLiving().motionY < 0.0D) {
                event.getEntityLiving().motionY *= 0.79999999999999998D;
            }
        }

        //watery grave
        if (event.getEntityLiving().isPotionActive(BuffList.wateryGrave)) {
            if (event.getEntityLiving().isInWater()) {
                double pullVel = -0.5f;
                pullVel *= (event.getEntityLiving().getActivePotionEffect(BuffList.wateryGrave).getAmplifier() + 1);
                if (event.getEntityLiving().motionY > pullVel)
                    event.getEntityLiving().motionY -= 0.1;
            }
        }

        //mana link pfx
        if (ent.worldObj.isRemote)
            extendedProperties.spawnManaLinkParticles();

        if (ent.ticksExisted % 20 == 0)
            extendedProperties.cleanupManaLinks();

        if (world.isRemote) {
            AMCore.proxy.sendLocalMovementData(ent);
        }
    }

    @SubscribeEvent
    public void onBucketFill(FillBucketEvent event) {
        ItemStack result = attemptFill(event.getWorld(), event.getTarget());

        if (result != null) {
            //TODO yep I love useless lines
            //event.result = result;
            event.setResult(Event.Result.ALLOW);
        }
    }

    private ItemStack attemptFill(World world, RayTraceResult p) {
        IBlockState block = world.getBlockState(p.getBlockPos());

        if (block == BlocksCommonProxy.liquidEssence) {
            if (block.getBlock().getMetaFromState(block) == 0) // Check that it is a source block
            {
                world.setBlockToAir(p.getBlockPos());// Remove the fluid block

                return new ItemStack(ItemsCommonProxy.itemAMBucket);
            }
        }

        return null;
    }

    @SubscribeEvent
    public void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        if (event.getTarget() instanceof EntityItemFrame) {
            AMCore.proxy.itemFrameWatcher.startWatchingFrame((EntityItemFrame) event.getTarget());
        }
    }

    @SubscribeEvent
    public void onPlayerTossItem(ItemTossEvent event) {
        if (!event.getEntityItem().worldObj.isRemote)
            EntityItemWatcher.instance.addWatchedItem(event.getEntityItem());
    }

    @SubscribeEvent
    public void onEntityAttacked(LivingAttackEvent event) {
        if (event.getSource().isFireDamage() && event.getEntityLiving() instanceof EntityPlayer && ((EntityPlayer) event.getEntityLiving()).inventory.armorInventory[3] != null && ((EntityPlayer) event.getEntityLiving()).inventory.armorInventory[3].getItem() == ItemsCommonProxy.fireEars) {
            event.setCanceled(true);
            return;
        }

        if (event.getEntityLiving().isPotionActive(BuffList.manaShield)) {
            if (ExtendedProperties.For(event.getEntityLiving()).getCurrentMana() >= event.getAmount() * 250f) {
                ExtendedProperties.For(event.getEntityLiving()).deductMana(event.getAmount() * 100f);
                ExtendedProperties.For(event.getEntityLiving()).forceSync();
                for (int i = 0; i < Math.min(event.getAmount(), 5 * AMCore.config.getGFXLevel()); ++i)
                    AMCore.proxy.particleManager.BoltFromPointToPoint(event.getEntityLiving().worldObj,
                            event.getEntityLiving().posX,
                            event.getEntityLiving().posY + event.getEntityLiving().worldObj.rand.nextFloat() * event.getEntityLiving().getEyeHeight(),
                            event.getEntityLiving().posZ,
                            event.getEntityLiving().posX - 1 + event.getEntityLiving().worldObj.rand.nextFloat() * 2,
                            event.getEntityLiving().posY - 1 + event.getEntityLiving().worldObj.rand.nextFloat() * 2,
                            event.getEntityLiving().posZ - 1 + event.getEntityLiving().worldObj.rand.nextFloat() * 2, 6, -1);
                //TODO Sound stuff again
                //event.getEntityLiving().worldObj.playSoundAtEntity(event.getEntityLiving, "arsmagica2:misc.event.mana_shield_block", 1.0f, event.getEntityLiving().worldObj.rand.nextFloat() + 0.5f);
                event.setCanceled(true);
                return;
            }
        }
    }

    @SubscribeEvent
    public void onEntityHurt(LivingHurtEvent event) {

        if (event.getSource().isFireDamage() && event.getEntityLiving() instanceof EntityPlayer && ((EntityPlayer) event.getEntityLiving()).inventory.armorInventory[3] != null && ((EntityPlayer) event.getEntityLiving()).inventory.armorInventory[3].getItem() == ItemsCommonProxy.fireEars) {
            event.setCanceled(true);
            return;
        }

        if (event.getEntityLiving().isPotionActive(BuffList.magicShield))
            event.setAmount(event.getAmount() * 0.25f);

        if (event.getEntityLiving().isPotionActive(BuffList.manaShield)) {
            float manaToTake = Math.min(ExtendedProperties.For(event.getEntityLiving()).getCurrentMana(), event.getAmount() * 250f);
            event.setAmount(event.getAmount() - manaToTake / 250f);
            ExtendedProperties.For(event.getEntityLiving()).deductMana(manaToTake);
            ExtendedProperties.For(event.getEntityLiving()).forceSync();
            for (int i = 0; i < Math.min(event.getAmount(), 5 * AMCore.config.getGFXLevel()); ++i)
                AMCore.proxy.particleManager.BoltFromPointToPoint(event.getEntityLiving().worldObj,
                        event.getEntityLiving().posX,
                        event.getEntityLiving().posY + event.getEntityLiving().worldObj.rand.nextFloat() * event.getEntityLiving().getEyeHeight(),
                        event.getEntityLiving().posZ,
                        event.getEntityLiving().posX - 1 + event.getEntityLiving().worldObj.rand.nextFloat() * 2,
                        event.getEntityLiving().posY + event.getEntityLiving().getEyeHeight() - 1 + event.getEntityLiving().worldObj.rand.nextFloat() * 2,
                        event.getEntityLiving().posZ - 1 + event.getEntityLiving().worldObj.rand.nextFloat() * 2, 6, -1);
            //TODO Sound stuff again
            //event.getEntityLiving().worldObj.playSoundAtEntity(event.getEntityLiving(), "arsmagica2:misc.event.mana_shield_block", 1.0f, event.getEntityLiving().worldObj.rand.nextFloat() + 0.5f);
            if (event.getAmount() <= 0) {
                event.setCanceled(true);
                return;
            }
        }

        Entity entitySource = event.getSource().getSourceOfDamage();
        if (entitySource instanceof EntityPlayer
                && ((EntityPlayer) entitySource).inventory.armorInventory[2] != null
                && ((EntityPlayer) entitySource).inventory.armorInventory[2].getItem() == ItemsCommonProxy.earthGuardianArmor
                && ((EntityPlayer) entitySource).getItemStackFromSlot(EntityEquipmentSlot.MAINHAND) == null) {
            event.setAmount(event.getAmount() + 4);

            double deltaZ = event.getEntityLiving().posZ - entitySource.posZ;
            double deltaX = event.getEntityLiving().posX - entitySource.posX;
            double angle = Math.atan2(deltaZ, deltaX);
            double speed = ((EntityPlayer) entitySource).isSprinting() ? 3 : 2;
            double vertSpeed = ((EntityPlayer) entitySource).isSprinting() ? 0.5 : 0.325;

            if (event.getEntityLiving() instanceof EntityPlayer) {
                AMNetHandler.INSTANCE.sendVelocityAddPacket(event.getEntityLiving().worldObj, event.getEntityLiving(), speed * Math.cos(angle), vertSpeed, speed * Math.sin(angle));
            } else {
                event.getEntityLiving().motionX += (speed * Math.cos(angle));
                event.getEntityLiving().motionZ += (speed * Math.sin(angle));
                event.getEntityLiving().motionY += vertSpeed;
            }
            //TODO Sounds
            //event.getEntityLiving().worldObj.playSoundAtEntity(event.getEntityLiving, "arsmagica2:spell.cast.earth", 0.4f, event.getEntityLiving().worldObj.rand.nextFloat() * 0.1F + 0.9F);
        }

        ExtendedProperties extendedProperties = ExtendedProperties.For(event.getEntityLiving());
        EntityLivingBase ent = event.getEntityLiving();
        if (extendedProperties.getContingencyType() == ContingencyTypes.DAMAGE_TAKEN) {
            extendedProperties.procContingency();
        }
        if (extendedProperties.getContingencyType() == ContingencyTypes.HEALTH_LOW && ent.getHealth() <= ent.getMaxHealth() / 3) {
            extendedProperties.procContingency();
        }

        if (ent.isPotionActive(BuffList.fury))
            event.setAmount(event.getAmount() / 2);

        if (entitySource instanceof EntityLivingBase
                && ((EntityLivingBase) entitySource).isPotionActive(BuffList.shrink))
            event.setAmount(event.getAmount() / 2);
    }

    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof EntityLivingBase && ((EntityLivingBase) event.getEntity()).isPotionActive(BuffList.temporalAnchor)) {
            ((EntityLivingBase) event.getEntity()).removePotionEffect(BuffList.temporalAnchor);
        }
    }

    @SubscribeEvent
    public void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        EntityPlayer player = event.getEntityPlayer();
        if (player.isPotionActive(BuffList.fury))
            event.setNewSpeed(event.getOriginalSpeed() * 5);
    }

    @SubscribeEvent
    public void onManaCost(ManaCostEvent event) {
        if (event.caster.getHeldItem(EnumHand.MAIN_HAND) != null && event.caster.getHeldItem(EnumHand.MAIN_HAND).getItem() == ItemsCommonProxy.arcaneSpellbook) {
            event.manaCost *= 0.75f;
            event.burnout *= 0.4f;
        }
    }

    @SubscribeEvent
    public void onPlayerPickupItem(EntityItemPickupEvent event) {
        if (event.getEntityPlayer() == null)
            return;

        if (!event.getEntityPlayer().worldObj.isRemote && ExtendedProperties.For(event.getEntityPlayer()).getMagicLevel() <= 0 && event.getItem().getEntityItem().getItem() == ItemsCommonProxy.arcaneCompendium) {
            event.getEntityPlayer().addChatMessage(new TextComponentString("You have unlocked the secrets of the arcane!"));
            AMNetHandler.INSTANCE.sendCompendiumUnlockPacket((EntityPlayerMP) event.getEntityPlayer(), "shapes", true);
            AMNetHandler.INSTANCE.sendCompendiumUnlockPacket((EntityPlayerMP) event.getEntityPlayer(), "components", true);
            AMNetHandler.INSTANCE.sendCompendiumUnlockPacket((EntityPlayerMP) event.getEntityPlayer(), "modifiers", true);
            ExtendedProperties.For(event.getEntityPlayer()).setMagicLevelWithMana(1);
            ExtendedProperties.For(event.getEntityPlayer()).forceSync();
            return;
        }

        if (event.getItem().getEntityItem().getItem() == ItemsCommonProxy.spell) {
            if (event.getEntityPlayer().worldObj.isRemote) {
                AMNetHandler.INSTANCE.sendCompendiumUnlockPacket((EntityPlayerMP) event.getEntityPlayer(), "spell_book", false);
            }
        } else {
            Item item = event.getItem().getEntityItem().getItem();
            int meta = event.getItem().getEntityItem().getItemDamage();

            if (event.getEntityPlayer().worldObj.isRemote &&
                    item.getUnlocalizedName() != null && (
                    AMCore.proxy.items.getArsMagicaItems().contains(item)) ||
                    (item instanceof ItemBlock && AMCore.proxy.blocks.getArsMagicaBlocks().contains(((ItemBlock) item).block))) {
                AMNetHandler.INSTANCE.sendCompendiumUnlockPacket((EntityPlayerMP) event.getEntityPlayer(), item.getUnlocalizedName().replace("item.", "").replace("arsmagica2:", "").replace("tile.", "") + ((meta > -1) ? "@" + meta : ""), false);
            }
        }
    }
}

