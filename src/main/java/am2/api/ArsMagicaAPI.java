package am2.api;

import java.util.Map;

import am2.api.affinity.AbstractAffinityAbility;
import am2.api.affinity.Affinity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.FMLControlledNamespacedRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.fml.common.registry.PersistentRegistryManager;

public class ArsMagicaAPI {
	private static final int MIN_AFFINITY_ID = 0;
	private static final int MAX_AFFINITY_ID = Short.MAX_VALUE;
	
	private static final FMLControlledNamespacedRegistry<Affinity> AFFINITY_REGISTRY;
	private static final FMLControlledNamespacedRegistry<AbstractAffinityAbility> ABILITY_REGISTRY;

	static {
		ABILITY_REGISTRY = PersistentRegistryManager.createRegistry(new ResourceLocation("arsmagica2", "affinityabilities"), AbstractAffinityAbility.class, null, MIN_AFFINITY_ID, MAX_AFFINITY_ID, false, AffinityAbilityCallbacks.INSTANCE, AffinityAbilityCallbacks.INSTANCE, AffinityAbilityCallbacks.INSTANCE);
		AFFINITY_REGISTRY = PersistentRegistryManager.createRegistry(new ResourceLocation("arsmagica2", "affinities"), Affinity.class, new ResourceLocation("arsmagica2", "none"), MIN_AFFINITY_ID, MAX_AFFINITY_ID, false, AffinityCallbacks.INSTANCE, AffinityCallbacks.INSTANCE, AffinityCallbacks.INSTANCE);
	}
	
	public static FMLControlledNamespacedRegistry<Affinity> getAffinityRegistry() {return AFFINITY_REGISTRY;};
	public static FMLControlledNamespacedRegistry<AbstractAffinityAbility> getAffinityAbilityRegistry() {return ABILITY_REGISTRY;}
	
    private static class AffinityAbilityCallbacks implements IForgeRegistry.AddCallback<AbstractAffinityAbility>,IForgeRegistry.ClearCallback<AbstractAffinityAbility>,IForgeRegistry.CreateCallback<AbstractAffinityAbility>
	{
		static final AffinityAbilityCallbacks INSTANCE = new AffinityAbilityCallbacks();

		@Override
		public void onAdd(AbstractAffinityAbility ability, int id, Map<ResourceLocation, ?> slaves) {}

		@Override
		public void onClear(Map<ResourceLocation, ?> slaveset) {}

		@Override
		public void onCreate(Map<ResourceLocation, ?> slaveset) {}
	}
    
    private static class AffinityCallbacks implements IForgeRegistry.AddCallback<Affinity>,IForgeRegistry.ClearCallback<Affinity>,IForgeRegistry.CreateCallback<Affinity>
	{
		static final AffinityCallbacks INSTANCE = new AffinityCallbacks();

		@Override
		public void onAdd(Affinity aff, int id, Map<ResourceLocation, ?> slaves) {}

		@Override
		public void onClear(Map<ResourceLocation, ?> slaveset) {}

		@Override
		public void onCreate(Map<ResourceLocation, ?> slaveset) {}
	}
}
