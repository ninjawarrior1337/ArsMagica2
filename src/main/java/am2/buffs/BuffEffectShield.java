package am2.buffs;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;

public class BuffEffectShield extends BuffEffect{

	//private List<AMParticle> particles;
//	private int maxParticles = 50;

	public BuffEffectShield(Potion buffID, int duration, int amplifier){
		super(buffID, duration, amplifier);
		//particles = new ArrayList<AMParticle>();
	}

//	public void AddParticle(AMParticle particle){
//		if (particles.size() < maxParticles){
//			particles.add(particle);
//		}
//	}

	@Override
	public void applyEffect(EntityLivingBase entityliving){
	}

	@Override
	public void stopEffect(EntityLivingBase entityliving){
//		PotionEffectsDefs.buffEnding(this.getPotionID());
	}

	@Override
	protected String spellBuffName(){
		return "Magic Shield";
	}
}
