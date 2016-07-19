package am2.api.event;

import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.fml.common.eventhandler.Event;

public class EventPotionAdded extends Event{
	
	public Potion id;
	public int duration, amplifier;
	public boolean ambient, showParticules;
	public PotionEffect effect;

	public EventPotionAdded(Potion id, int duration, int amplifier, boolean ambient, boolean showParticules) {
		this.id = id;
		this.duration = duration;
		this.amplifier = amplifier;
		this.ambient = ambient;
		this.showParticules = showParticules;
		this.effect = new PotionEffect(id, duration, amplifier, ambient, showParticules);
	}
	
	public EventPotionAdded(PotionEffect effect) {
		this.effect = effect;
		id = effect.getPotion();
		duration = effect.getDuration();
		amplifier = effect.getAmplifier();
		showParticules = effect.doesShowParticles();
		ambient = effect.getIsAmbient();
	}
	
	public PotionEffect getEffect() {
		return effect;
	}

}
