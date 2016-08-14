package am2.affinity.abilities;

import am2.api.affinity.AbstractToggledAffinityAbility;
import am2.api.affinity.Affinity;
import am2.extensions.AffinityData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;

public class AbilityNightVision extends AbstractToggledAffinityAbility {

	public AbilityNightVision() {
		super(new ResourceLocation("arsmagica2", "nightvision"));
	}

	@Override
	protected boolean isEnabled(EntityPlayer player) {
		return AffinityData.For(player).getAbilityBoolean(AffinityData.NIGHT_VISION);
	}

	@Override
	public float getMinimumDepth() {
		return 0.75f;
	}

	@Override
	public Affinity getAffinity() {
		return Affinity.ENDER;
	}

	@Override
	public void applyTick(EntityPlayer player) {
		if (!player.worldObj.isRemote && (!player.isPotionActive(Potion.getPotionFromResourceLocation("night_vision")) || player.getActivePotionEffect(Potion.getPotionFromResourceLocation("night_vision")).getDuration() <= 220)){
			player.addPotionEffect(new PotionEffect(Potion.getPotionFromResourceLocation("night_vision"), 300, 1));
		}
	}

}
