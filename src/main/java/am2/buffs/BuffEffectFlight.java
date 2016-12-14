package am2.buffs;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import am2.ArsMagica2;
import am2.defs.PotionEffectsDefs;
import am2.packet.MessageCapabilities;

public class BuffEffectFlight extends BuffEffect{

	public BuffEffectFlight(int duration, int amplifier){
		super(PotionEffectsDefs.flight, duration, amplifier);
	}

	@Override
	public void applyEffect(EntityLivingBase entityliving){

	}

	@Override
	public void performEffect(EntityLivingBase entityliving){
		if (entityliving instanceof EntityPlayerMP){
			((EntityPlayer)entityliving).capabilities.allowFlying = true;
			((EntityPlayer)entityliving).capabilities.isFlying = true;
			if (getDuration() % 20 == 0)
				ArsMagica2.network.sendToServer(new MessageCapabilities((EntityPlayer) entityliving, 1, true));
		}
	}

	@Override
	public void stopEffect(EntityLivingBase entityliving){
		if (entityliving instanceof EntityPlayerMP){
			if (!((EntityPlayer)entityliving).capabilities.isCreativeMode){
				((EntityPlayer)entityliving).capabilities.allowFlying = false;
				((EntityPlayer)entityliving).capabilities.isFlying = false;
				((EntityPlayer)entityliving).fallDistance = 0f;
				ArsMagica2.network.sendToServer(new MessageCapabilities((EntityPlayer) entityliving, 0, false));
				ArsMagica2.network.sendToServer(new MessageCapabilities((EntityPlayer) entityliving, 1, false));
			}
		}
	}

	@Override
	protected String spellBuffName(){
		return "Flight";
	}

}
