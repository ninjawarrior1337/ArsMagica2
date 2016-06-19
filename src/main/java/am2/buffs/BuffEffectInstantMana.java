package am2.buffs;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;

public class BuffEffectInstantMana extends BuffEffect{

	public BuffEffectInstantMana(Potion buffID, int duration, int amplifier){
		super(buffID, duration, amplifier);
	}

	@Override
	public void applyEffect(EntityLivingBase entityliving){

	}

	@Override
	public void stopEffect(EntityLivingBase entityliving){

	}

	@Override
	protected String spellBuffName(){
		return "Instant Mana";
	}

}
