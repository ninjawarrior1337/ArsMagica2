package am2.armor.infusions;

import am2.api.items.armor.IArmorImbuement;
import am2.api.items.armor.ImbuementApplicationTypes;
import am2.api.items.armor.ImbuementTiers;
import am2.api.math.AMVector3;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.Method;
import java.util.EnumSet;

public class WaterWalking implements IArmorImbuement{

	@Override
	public String getID(){
		return "wtrwalk";
	}

	@Override
	public int getIconIndex(){
		return 28;
	}

	@Override
	public ImbuementTiers getTier(){
		return ImbuementTiers.FOURTH;
	}

	@Override
	public EnumSet<ImbuementApplicationTypes> getApplicationTypes(){
		return EnumSet.of(ImbuementApplicationTypes.ON_TICK);
	}

	@Override
	public boolean applyEffect(EntityPlayer player, World world, ItemStack stack, ImbuementApplicationTypes matchedType, Object... params){
		Block[] blocks = new Block[4];
		AMVector3[] vectors = new AMVector3[4];
		int posY = (int)Math.floor(player.posY);

		vectors[0] = new AMVector3((int)Math.floor(player.posX), posY, (int)Math.floor(player.posZ));
		vectors[1] = new AMVector3((int)Math.ceil(player.posX), posY, (int)Math.floor(player.posZ));
		vectors[2] = new AMVector3((int)Math.floor(player.posX), posY, (int)Math.ceil(player.posZ));
		vectors[3] = new AMVector3((int)Math.ceil(player.posX), posY, (int)Math.ceil(player.posZ));

		blocks[0] = world.getBlockState(vectors[0].toBlockPos()).getBlock();
		blocks[1] = world.getBlockState(vectors[1].toBlockPos()).getBlock();
		blocks[2] = world.getBlockState(vectors[2].toBlockPos()).getBlock();
		blocks[3] = world.getBlockState(vectors[3].toBlockPos()).getBlock();

		boolean onWater = false;
		int index = 0;
		for (int i = 0; i < 4 && !onWater; ++i){
			onWater |= (blocks[i] == Blocks.FLOWING_WATER || blocks[i] == Blocks.WATER);
			index = i;
		}

		if (!player.isInsideOfMaterial(Material.WATER) && onWater && !player.isSneaking()){
			player.fallDistance = 0;
			player.onGround = true;
			player.isAirBorne = false;
			player.isCollidedVertically = true;
			player.isCollided = true;
			if (player.motionY < 0){
				player.motionY = 0;
			}

			if (player.worldObj.isRemote && player.ticksExisted % 5 == 0 && (Math.abs(player.motionX) > 0.1f || Math.abs(player.motionZ) > 0.1f)){
				//TODO Sound
				//player.playSound("liquid.swim", 0.02f, 1.0F + (player.getRNG().nextFloat() - player.getRNG().nextFloat()) * 0.4F);
				for (float l = 0; l < 5; ++l){
					float f5 = (player.getRNG().nextFloat() * 2.0F - 1.0F) * player.width;
					float f4 = (player.getRNG().nextFloat() * 2.0F - 1.0F) * player.width;
					//TODO Particles
					//player.worldObj.spawnParticle("splash", player.posX + f5, player.posY - player.yOffset, player.posZ + f4, (player.getRNG().nextFloat() - 0.5f) * 0.2f, player.getRNG().nextFloat() * 0.1f, (player.getRNG().nextFloat() - 0.5f) * 0.2f);
				}
			}
		}
		return false;
	}

	private void callFall(EntityPlayer player){
		try{
			Method m = ReflectionHelper.findMethod(Entity.class, player, new String[]{"fall"}, float.class);
			if (m != null){
				m.setAccessible(true);
				m.invoke(player, player.fallDistance);
			}
		}catch (Throwable t){
			t.printStackTrace();
		}
	}

	@Override
	public int[] getValidSlots(){
		return new int[]{ImbuementRegistry.SLOT_BOOTS};
	}

	@Override
	public boolean canApplyOnCooldown(){
		return true;
	}

	@Override
	public int getCooldown(){
		return 0;
	}

	@Override
	public int getArmorDamage(){
		return 0;
	}
}
