package am2.buffs;

import am2.ArsMagica2;
import am2.defs.PotionEffectsDefs;
import am2.packet.MessageCapabilities;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class BuffEffectLevitation extends BuffEffect{

	public BuffEffectLevitation(int duration, int amplifier){
		super(PotionEffectsDefs.levitation, duration, amplifier);
	}

	@Override
	public void applyEffect(EntityLivingBase entityliving){
		if (entityliving instanceof EntityPlayer){
			((EntityPlayer)entityliving).capabilities.allowFlying = true;
			ArsMagica2.network.sendToServer(new MessageCapabilities((EntityPlayer)entityliving, 1, true));
		}
	}

	@Override
	public void performEffect(EntityLivingBase entityliving){
		if (entityliving instanceof EntityPlayer){
			if (((EntityPlayer)entityliving).capabilities.isFlying){
				float factor = 0.4f;
				entityliving.motionX *= factor;
				entityliving.motionZ *= factor;
				entityliving.motionY *= 0.0001f;
			}
		}
	}

	@Override
	public void stopEffect(EntityLivingBase entityliving){
		if (entityliving instanceof EntityPlayer){
			if (!((EntityPlayer)entityliving).capabilities.isCreativeMode){
				((EntityPlayer)entityliving).capabilities.allowFlying = false;
				((EntityPlayer)entityliving).capabilities.isFlying = false;
				((EntityPlayer)entityliving).fallDistance = 0f;
				ArsMagica2.network.sendToServer(new MessageCapabilities((EntityPlayer)entityliving, 0, false));
				ArsMagica2.network.sendToServer(new MessageCapabilities((EntityPlayer)entityliving, 1, false));
			}
		}
	}

	@Override
	protected String spellBuffName(){
		return "Levitation";
	}

}
