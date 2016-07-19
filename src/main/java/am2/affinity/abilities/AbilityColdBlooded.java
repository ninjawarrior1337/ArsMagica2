package am2.affinity.abilities;

import am2.affinity.AffinityAbilityModifiers;
import am2.api.affinity.AbstractAffinityAbility;
import am2.api.affinity.Affinity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class AbilityColdBlooded extends AbstractAffinityAbility {

	public AbilityColdBlooded() {
		super(new ResourceLocation("arsmagica2", "coldblooded"));
	}

	@Override
	protected float getMinimumDepth() {
		return 0.1f;
	}

	@Override
	protected Affinity getAffinity() {
		return Affinity.ICE;
	}
	
	@Override
	public void applyTick(EntityPlayer player) {
		IAttributeInstance attribute = player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
		AffinityAbilityModifiers.instance.applyOrRemoveModifier(attribute, AffinityAbilityModifiers.iceAffinityColdBlooded, !AffinityAbilityModifiers.instance.isOnIce(player));
	}
	
	@Override
	public void removeEffects(EntityPlayer player) {
		IAttributeInstance attribute = player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
		AffinityAbilityModifiers.instance.applyOrRemoveModifier(attribute, AffinityAbilityModifiers.iceAffinityColdBlooded, false);
	}

}
