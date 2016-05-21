package am2.armor.infusions;

import am2.api.items.armor.IArmorImbuement;
import am2.api.items.armor.ImbuementApplicationTypes;
import am2.api.items.armor.ImbuementTiers;
import am2.blocks.BlocksCommonProxy;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

import java.util.EnumSet;

public class Lightstep implements IArmorImbuement{

	@Override
	public String getID(){
		return "lightstep";
	}

	@Override
	public int getIconIndex(){
		return 30;
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

		if (world.isRemote)
			return false;

		if (player.isSneaking())
			return false;
		int ll = world.getLightFor(EnumSkyBlock.BLOCK, player.getPosition().up());
		if (ll < 7 && world.isAirBlock(player.getPosition().up())){
			world.setBlockState(player.getPosition().up(), BlocksCommonProxy.blockMageTorch.getDefaultState());
			return true;
		}
		return false;
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
		return 1;
	}
}
