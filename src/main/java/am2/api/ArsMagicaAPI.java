package am2.api;

import java.util.Map;

import am2.api.affinity.AbstractAffinityAbility;
import am2.api.affinity.Affinity;
import am2.api.items.armor.ArmorImbuement;
import am2.api.skill.Skill;
import am2.spell.AbstractSpellPart;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.registry.FMLControlledNamespacedRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.fml.common.registry.PersistentRegistryManager;

public class ArsMagicaAPI {
	
	private static final FMLControlledNamespacedRegistry<Affinity> AFFINITY_REGISTRY;
	private static final FMLControlledNamespacedRegistry<AbstractAffinityAbility> ABILITY_REGISTRY;
	private static final FMLControlledNamespacedRegistry<ArmorImbuement> IMBUEMENTS_REGISTRY;
	private static final FMLControlledNamespacedRegistry<AbstractSpellPart> SPELL_REGISTRY;
	private static final FMLControlledNamespacedRegistry<Skill> SKILL_REGISTRY;
	
	private static boolean enableTier4 = false;
	private static boolean enableTier5 = false;
	private static boolean enableTier6 = false;
	
	
	static {
		ABILITY_REGISTRY = PersistentRegistryManager.createRegistry(new ResourceLocation("arsmagica2", "affinityabilities"), AbstractAffinityAbility.class, null, 0, Short.MAX_VALUE, false, ObjectCallbacks.ABILITY, ObjectCallbacks.ABILITY, ObjectCallbacks.ABILITY);
		AFFINITY_REGISTRY = PersistentRegistryManager.createRegistry(new ResourceLocation("arsmagica2", "affinities"), Affinity.class, new ResourceLocation("arsmagica2", "none"), 0, Short.MAX_VALUE, false, ObjectCallbacks.AFFINITY, ObjectCallbacks.AFFINITY, ObjectCallbacks.AFFINITY);
		IMBUEMENTS_REGISTRY = PersistentRegistryManager.createRegistry(new ResourceLocation("arsmagica2", "armorimbuments"), ArmorImbuement.class, null, 0, Short.MAX_VALUE, true, ObjectCallbacks.IMBUEMENT, ObjectCallbacks.IMBUEMENT, ObjectCallbacks.IMBUEMENT);
		SPELL_REGISTRY = PersistentRegistryManager.createRegistry(new ResourceLocation("arsmagica2", "spells"), AbstractSpellPart.class, null, 0, Short.MAX_VALUE, true, ObjectCallbacks.SPELL, ObjectCallbacks.SPELL, ObjectCallbacks.SPELL);
		SKILL_REGISTRY = PersistentRegistryManager.createRegistry(new ResourceLocation("arsmagica2", "skills"), Skill.class, null, 0, Short.MAX_VALUE, true, ObjectCallbacks.SKILL, ObjectCallbacks.SKILL, ObjectCallbacks.SKILL);
	}
	
	//Bonus to max mana.  Applied additively.
	public static final IAttribute maxManaBonus = new RangedAttribute(null, "am2.maxManaBonus", 0.0f, 0.0f, Double.MAX_VALUE).setDescription("Mana Bonus").setShouldWatch(true);
	//Bonus to max burnout.  Applied additively.
	public static final IAttribute maxBurnoutBonus = new RangedAttribute(null, "am2.maxBurnoutBonus", 0.0f, 0.0f, Double.MAX_VALUE).setDescription("Burnout Bonus").setShouldWatch(true);
	//Bonus to XP gained.  Applied multiplicatively.
	public static final IAttribute xpGainModifier = new RangedAttribute(null, "am2.xpMultiplier", 1.0f, 0.0f, Double.MAX_VALUE).setDescription("XP Mutiplier").setShouldWatch(true);
	//Bonus to mana regen rate.  Applied multiplicatively.
	public static final IAttribute manaRegenTimeModifier = new RangedAttribute(null, "am2.manaRegenModifier", 1.0f, 0.5f, 2.0f).setDescription("Mana Regen Rate Multiplier").setShouldWatch(true);
	//Bonus to burnout reduction rate.  Applied multiplicatively.
	public static final IAttribute burnoutReductionRate = new RangedAttribute(null, "am2.burnoutReduction", 1.0f, 0.1f, 2.0f).setDescription("Burnout Reduction Rate").setShouldWatch(true);

	
	public static FMLControlledNamespacedRegistry<Affinity> getAffinityRegistry() {return AFFINITY_REGISTRY;};
	public static FMLControlledNamespacedRegistry<AbstractAffinityAbility> getAffinityAbilityRegistry() {return ABILITY_REGISTRY;}
	public static FMLControlledNamespacedRegistry<ArmorImbuement> getArmorImbuementRegistry() {return IMBUEMENTS_REGISTRY;}
	public static FMLControlledNamespacedRegistry<AbstractSpellPart> getSpellRegistry() {return SPELL_REGISTRY;}
	public static FMLControlledNamespacedRegistry<Skill> getSkillRegistry() {return SKILL_REGISTRY;}
	
	/**
	 * Enable Tier 4, call in static{} for change to take effect.
	 */
	public static void enableTier4() {enableTier4 = true;}
	public static boolean hasTier4() {return enableTier4 || hasTier5();}

	/**
	 * Enable Tier 5, call in static{} for change to take effect.
	 */
	public static void enableTier5() {enableTier5 = true;}
	public static boolean hasTier5() {return enableTier5 || hasTier6();}
	
	/**
	 * Enable Tier 6, call in static{} for change to take effect.
	 */
	public static void enableTier6() {enableTier6 = true;}
	public static boolean hasTier6() {return enableTier6 ;}

	public static String getCurrentModId () {
		ModContainer current = Loader.instance().activeModContainer();
		String modid = "arsmagica2";
		if (current != null)
			modid = current.getModId();
		return modid;
	}
	
    private static class ObjectCallbacks<T> implements IForgeRegistry.AddCallback<T>,IForgeRegistry.ClearCallback<T>,IForgeRegistry.CreateCallback<T>
	{
		static final ObjectCallbacks<AbstractSpellPart> SPELL = new SpellCallbacks();
		static final ObjectCallbacks<AbstractAffinityAbility> ABILITY = new ObjectCallbacks<>();
		static final ObjectCallbacks<Affinity> AFFINITY = new ObjectCallbacks<>();
		static final ObjectCallbacks<ArmorImbuement> IMBUEMENT = new ObjectCallbacks<>();
		static final ObjectCallbacks<Skill> SKILL = new ObjectCallbacks<>();

		@Override
		public void onAdd(T ability, int id, Map<ResourceLocation, ?> slaves) {}

		@Override
		public void onClear(Map<ResourceLocation, ?> slaveset) {}

		@Override
		public void onCreate(Map<ResourceLocation, ?> slaveset) {}
	}
    
    private static class SpellCallbacks extends ObjectCallbacks<AbstractSpellPart> {

		@Override
		public void onAdd(AbstractSpellPart ability, int id, Map<ResourceLocation, ?> slaves) {
		}
		
	}
}
