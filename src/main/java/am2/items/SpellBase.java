package am2.items;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import am2.api.SpellRegistry;
import am2.extensions.EntityExtension;
import am2.extensions.SkillData;
import am2.spell.IShape;
import am2.spell.ISpellPart;
import am2.spell.component.Blizzard;
import am2.spell.component.FireRain;
import am2.spell.component.PhysicalDamage;
import am2.spell.shape.MissingShape;
import am2.spell.shape.Projectile;
import am2.spell.shape.Touch;
import am2.spell.shape.Wave;
import am2.utils.EntityUtils;
import am2.utils.KeyValuePair;
import am2.utils.SpellUtils;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SpellBase extends ItemSpellBase{

	public SpellBase(){
		super();
		this.setMaxDamage(0);
	}
	
	@Override
	public SpellBase registerAndName(String name) {
		// TODO Auto-generated method stub
		return (SpellBase) super.registerAndName(name);
	}
	
	@Override
	public EnumAction getItemUseAction(ItemStack par1ItemStack){
		return EnumAction.BLOCK;
	}

	@Override
	public boolean getShareTag(){
		return true;
	}

	@Override
	public String getItemStackDisplayName(ItemStack par1ItemStack){
		if (par1ItemStack.getTagCompound() == null) return "\247bMalformed Spell";
		IShape shape = SpellUtils.getShapeForStage(par1ItemStack, 0);
		if (shape instanceof MissingShape){
			return "Unnamed Spell";
		}
		String clsName = shape.getClass().getName();
		return clsName.substring(clsName.lastIndexOf('.') + 1) + " Spell";
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean par4){

		if (!stack.hasTagCompound()) return;
		
		int manaCost = SpellUtils.getManaCost(stack);
		manaCost *= 1F + (float)((float)EntityExtension.For(player).getCurrentBurnout() / (float)EntityExtension.For(player).getMaxBurnout());
		list.add("Mana Cost : " + manaCost);
	}

	@Override
	public int getMaxItemUseDuration(ItemStack par1ItemStack){
		return 72000;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack stack, World world, EntityPlayer caster, EnumHand hand){
		caster.setActiveHand(hand);
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
	}

	@Override
	public boolean hasEffect(ItemStack par1ItemStack){
		return false;
	}
	
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world,
			EntityLivingBase player, int timeLeft) {
		IShape shape = SpellUtils.getShapeForStage(stack, 0);
		if (shape != null){
			if (!shape.isChanneled())
				System.out.println(SpellUtils.applyStackStage(stack, player, null, player.posX, player.posY, player.posZ, EnumFacing.UP, world, true, true, 0));
			if (world.isRemote && shape.isChanneled()){
				//SoundHelper.instance.stopSound(shape.getSoundForAffinity(SpellUtils.instance.mainAffinityFor(stack), stack, null));
			}
		}
	}
	
	@Override
	public void onUsingTick(ItemStack stack, EntityLivingBase caster, int count) {
		IShape shape = SpellUtils.getShapeForStage(stack, 0);
		if (shape.isChanneled())
			SpellUtils.applyStackStage(stack, caster, caster, caster.posX, caster.posY, caster.posZ, EnumFacing.UP, caster.worldObj, true, true, count - 1);
		super.onUsingTick(stack, caster, count);
	}
	
	@Override
	public RayTraceResult getMovingObjectPosition(EntityLivingBase caster, World world, double range, boolean includeEntities, boolean targetWater){
		RayTraceResult entityPos = null;
		if (includeEntities){
			Entity pointedEntity = EntityUtils.getPointedEntity(world, caster, range, 1.0f, false);
			if (pointedEntity != null){
				entityPos = new RayTraceResult(pointedEntity);
			}
		}

		float factor = 1.0F;
		float interpPitch = caster.prevRotationPitch + (caster.rotationPitch - caster.prevRotationPitch) * factor;
		float interpYaw = caster.prevRotationYaw + (caster.rotationYaw - caster.prevRotationYaw) * factor;
		double interpPosX = caster.prevPosX + (caster.posX - caster.prevPosX) * factor;
		double interpPosY = caster.prevPosY + (caster.posY - caster.prevPosY) * factor + caster.getEyeHeight();
		double interpPosZ = caster.prevPosZ + (caster.posZ - caster.prevPosZ) * factor;
		Vec3d vec3 = new Vec3d(interpPosX, interpPosY, interpPosZ);
		float offsetYawCos = MathHelper.cos(-interpYaw * 0.017453292F - (float)Math.PI);
		float offsetYawSin = MathHelper.sin(-interpYaw * 0.017453292F - (float)Math.PI);
		float offsetPitchCos = -MathHelper.cos(-interpPitch * 0.017453292F);
		float offsetPitchSin = MathHelper.sin(-interpPitch * 0.017453292F);
		float finalXOffset = offsetYawSin * offsetPitchCos;
		float finalZOffset = offsetYawCos * offsetPitchCos;
		Vec3d targetVector = vec3.addVector(finalXOffset * range, offsetPitchSin * range, finalZOffset * range);
		RayTraceResult mop = world.rayTraceBlocks(vec3, targetVector, targetWater);

		if (entityPos != null && mop != null){
			if (mop.hitVec.distanceTo(new RayTraceResult(caster).hitVec) < entityPos.hitVec.distanceTo(new RayTraceResult(caster).hitVec)){
				return mop;
			}else{
				return entityPos;
			}
		}

		return entityPos != null ? entityPos : mop;

	}
	@Override
	public void getSubItems(Item itemIn, CreativeTabs tab,
			List<ItemStack> subItems) {
		subItems.add(SpellUtils.createSpellStack_old(new Random().nextInt(405), SpellRegistry.getShapeFromName("projectile"), SpellRegistry.getComponentFromName("dig")));
		subItems.add(SpellUtils.createSpellStack_old(new Random().nextInt(405), SpellRegistry.getShapeFromName("projectile"), SpellRegistry.getComponentFromName("physical_damage")));
		subItems.add(SpellUtils.createSpellStack_old(new Random().nextInt(405), SpellRegistry.getShapeFromName("self"), SpellRegistry.getComponentFromName("physical_damage")));
		subItems.add(SpellUtils.createSpellStack_old(new Random().nextInt(405), SpellRegistry.getShapeFromName("projectile"), SpellRegistry.getComponentFromName("dig"), SpellRegistry.getShapeFromName("self"), SpellRegistry.getComponentFromName("physical_damage")));
		subItems.add(SpellUtils.createSpellStack_old(new Random().nextInt(405), SpellRegistry.getShapeFromName("projectile"), SpellRegistry.getComponentFromName("mark"), SpellRegistry.getShapeFromName("self"), SpellRegistry.getComponentFromName("recall")));
		subItems.add(SpellUtils.createSpellStack_old(new Random().nextInt(405), SpellRegistry.getShapeFromName("contingency_death"), SpellRegistry.getShapeFromName("self"), SpellRegistry.getComponentFromName("chronoAnchor")));
		subItems.add(SpellUtils.createSpellStack_old(new Random().nextInt(405), SpellRegistry.getShapeFromName("self"), SpellRegistry.getComponentFromName("heal")));
		subItems.add(SpellUtils.createSpellStack_old(new Random().nextInt(405), SpellRegistry.getShapeFromName("projectile"), SpellRegistry.getComponentFromName("rift")));
		subItems.add(SpellUtils.createSpellStack_old(new Random().nextInt(405), SpellRegistry.getShapeFromName("self"), SpellRegistry.getComponentFromName("slowfall"), SpellRegistry.getComponentFromName("heal")));
		subItems.add(SpellUtils.createSpellStack_old(new Random().nextInt(405), SpellRegistry.getShapeFromName("projectile"), SpellRegistry.getShapeFromName("zone"), SpellRegistry.getComponentFromName("lightning_damage")));
		subItems.add(SpellUtils.createSpellStack_old(new Random().nextInt(405), SpellRegistry.getShapeFromName("projectile"), SpellRegistry.getShapeFromName("zone"), SpellRegistry.getComponentFromName("lightning_damage")));
		subItems.add(SpellUtils.createSpellStack_old(new Random().nextInt(405), SpellRegistry.getShapeFromName("projectile"), SpellRegistry.getComponentFromName("falling_star"), SpellRegistry.getModifierFromName("damage"), SpellRegistry.getModifierFromName("damage"), SpellRegistry.getModifierFromName("damage"), SpellRegistry.getModifierFromName("damage"), SpellRegistry.getModifierFromName("damage")).setStackDisplayName("Falling Star"));
		subItems.add(SpellUtils.createSpellStack_old(new Random().nextInt(405), SpellRegistry.getShapeFromName("projectile"), SpellRegistry.getComponentFromName("blizzard")).setStackDisplayName("Blizzard"));
		subItems.add(SpellUtils.createSpellStack_old(new Random().nextInt(405), SpellRegistry.getShapeFromName("projectile"), SpellRegistry.getComponentFromName("fire_rain")).setStackDisplayName("Fire Rain"));
		
		KeyValuePair<ArrayList<ISpellPart>, NBTTagCompound> pair = new KeyValuePair<>(Lists.newArrayList(new Projectile(), new Wave(), new Touch()), new NBTTagCompound());
		subItems.add(SpellUtils.createSpellStack(Lists.newArrayList(pair), Lists.newArrayList(new PhysicalDamage()), new NBTTagCompound()));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onUpdate(ItemStack stack, World world, Entity entity, int par4, boolean par5){
		super.onUpdate(stack, world, entity, par4, par5);
		if (entity instanceof EntityPlayerSP && ((EntityPlayerSP)entity).getActiveHand() != null){
			EntityPlayerSP player = (EntityPlayerSP)entity;
			ItemStack usingItem = player.getHeldItem(player.getActiveHand());
			if (usingItem != null && usingItem.getItem() == this){
				if (SkillData.For(player).hasSkill("spellMovement")){
					player.movementInput.moveForward *= 2.5F;
					player.movementInput.moveStrafe *= 2.5F;
				}
			}
		}
	}
}
