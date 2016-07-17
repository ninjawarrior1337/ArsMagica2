package am2.api.affinity;

import javax.annotation.Nullable;

import am2.api.extensions.IAffinityData;
import am2.extensions.AffinityData;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;

public abstract class AbstractAffinityAbility extends IForgeRegistryEntry.Impl<AbstractAffinityAbility>{
	
	protected AbstractAffinityAbility(ResourceLocation identifier) {
		this.setRegistryName(identifier);
	}
	
	/**
	 * At which point does this ability enable ?
	 * 
	 * @return a depth.
	 */	
	protected abstract float getMinimumDepth();
	
	/**
	 * At which point does this ability disables ?
	 * 
	 * @return a depth or any value under 0 or over 1 to ignore this.
	 */
	protected float getMaximumDepth() {
		return -1F;
	}
	
	/**
	 * Setting this to null or NONE will make this class useless.
	 * 
	 * @return the ability that is required.
	 */
	protected abstract Affinity getAffinity();
	
	/**
	 * If this Affinity Ability uses a key binding, return it, otherwise just return null
	 * 
	 * @return the key binding that this ability uses, or null.
	 */
	@Nullable
	public KeyBinding getKey() {
		return null;
	}
	
	/**
	 * Checks if the player can use this ability. Most of the time you won't need change this unless you are using toggle {@link KeyBinding}s.
	 * 
	 * @param player : the current player.
	 * @return if the player can use this ability.
	 */
	public boolean canApply(EntityPlayer player) {
		Affinity aff = this.getAffinity();
		if (aff == Affinity.NONE || aff == null)
			return false;
		IAffinityData data = AffinityData.For(player);
		float depth = data.getAffinityDepth(aff);
		if (getMaximumDepth() < 0F || getMaximumDepth() > 1F || getMaximumDepth() < getMinimumDepth())
			return depth >= getMinimumDepth();
		return depth >= getMinimumDepth() && depth <= getMaximumDepth();
	}
	/**
	 * The thing that this ability does
	 * 
	 * @param player : the current player
	 */
	public abstract void apply(EntityPlayer player);
	
	public Runnable createRunnable(EntityPlayer player) {
		return new Apply(player, this);
	}
	
	private static class Apply implements Runnable {
		
		private EntityPlayer player;
		private AbstractAffinityAbility ability;
		
		public Apply(EntityPlayer player, AbstractAffinityAbility ability) {
			this.player = player;
			this.ability = ability;
		}
		
		@Override
		public void run() {
			ability.apply(player);
		}
		
	}
}
