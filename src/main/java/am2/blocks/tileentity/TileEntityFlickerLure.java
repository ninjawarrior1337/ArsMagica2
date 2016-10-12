package am2.blocks.tileentity;

import am2.entity.EntityFlicker;
import am2.power.PowerNodeRegistry;
import am2.power.PowerTypes;

public class TileEntityFlickerLure extends TileEntityAMPower{

	public TileEntityFlickerLure(){
		super(200);
	}

	@Override
	public boolean canRelayPower(PowerTypes type){
		return false;
	}

	@Override
	public int getChargeRate(){
		return 5;
	}

	@Override
	public void update(){
		super.update();

		if (worldObj.isRemote)
			return;

		if (worldObj.isBlockIndirectlyGettingPowered(pos) > 0){
			if (worldObj.rand.nextDouble() < 0.05f && PowerNodeRegistry.For(worldObj).checkPower(this, 20)){
				EntityFlicker flicker = new EntityFlicker(worldObj);
				flicker.setPosition(pos.getX() + 0.5f, pos.getY() + 1.5f, pos.getZ() + 0.5f);
				worldObj.spawnEntityInWorld(flicker);
				PowerNodeRegistry.For(worldObj).consumePower(this, PowerNodeRegistry.For(worldObj).getHighestPowerType(this), 20);
			}
		}
	}
}
