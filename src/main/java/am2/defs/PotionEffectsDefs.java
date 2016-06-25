package am2.defs;

import static am2.ArsMagica2.LOGGER;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import am2.buffs.BuffEffect;
import am2.buffs.BuffEffectAgility;
import am2.buffs.BuffEffectAstralDistortion;
import am2.buffs.BuffEffectBurnoutReduction;
import am2.buffs.BuffEffectCharmed;
import am2.buffs.BuffEffectClarity;
import am2.buffs.BuffEffectEntangled;
import am2.buffs.BuffEffectFlight;
import am2.buffs.BuffEffectFrostSlowed;
import am2.buffs.BuffEffectFury;
import am2.buffs.BuffEffectGravityWell;
import am2.buffs.BuffEffectHaste;
import am2.buffs.BuffEffectIllumination;
import am2.buffs.BuffEffectInstantMana;
import am2.buffs.BuffEffectLeap;
import am2.buffs.BuffEffectLevitation;
import am2.buffs.BuffEffectMagicShield;
import am2.buffs.BuffEffectManaRegen;
import am2.buffs.BuffEffectManaShield;
import am2.buffs.BuffEffectRegeneration;
import am2.buffs.BuffEffectScrambleSynapses;
import am2.buffs.BuffEffectShield;
import am2.buffs.BuffEffectShrink;
import am2.buffs.BuffEffectSilence;
import am2.buffs.BuffEffectSlowfall;
import am2.buffs.BuffEffectSpellReflect;
import am2.buffs.BuffEffectSwiftSwim;
import am2.buffs.BuffEffectTemporalAnchor;
import am2.buffs.BuffEffectTrueSight;
import am2.buffs.BuffEffectWaterBreathing;
import am2.buffs.BuffEffectWateryGrave;
import am2.buffs.BuffMaxManaIncrease;

public class PotionEffectsDefs {
	
	public static final HashMap<Potion, Class<? extends BuffEffect>> classForId = new HashMap<Potion, Class<? extends BuffEffect>>();
	
	public static Potion agility;
	public static Potion astralDistortion;
	public static Potion burnoutReduction;
	public static Potion charme;
	public static Potion clarity;
	public static Potion entangle;
	public static Potion flight;
	public static Potion frostSlow;
	public static Potion fury;
	public static Potion gravityWell;
	public static Potion haste;
	public static Potion illumination;
	public static Potion instantMana;
	public static Potion leap;
	public static Potion levitation;
	public static Potion magicShield;
	public static Potion manaRegen;
	public static Potion manaShield;
	public static Potion regeneration;
	public static Potion scrambleSynapses;
	public static Potion shield;
	public static Potion shrink;
	public static Potion silence;
	public static Potion slowfall;
	public static Potion spellReflect;
	public static Potion swiftSwim;
	public static Potion temporalAnchor;
	public static Potion trueSight;
	public static Potion waterBreathing;
	public static Potion wateryGrave;
	public static Potion manaBoost;
	
	public static final int default_buff_duration = 600;
	
	public static void init () {
		agility = createPotion(new ResourceLocation("arsmagica2:agility"), false, 0x000000, 0, 0, BuffEffectAgility.class);
		astralDistortion = createPotion(new ResourceLocation("arsmagica2:astralDistortion"), true, 0x000000, 0, 4, BuffEffectAstralDistortion.class);
		burnoutReduction = createPotion(new ResourceLocation("arsmagica2:burnoutReduction"), false, 0x000000, 1, 1, BuffEffectBurnoutReduction.class);
		charme = createPotion(new ResourceLocation("arsmagica2:charme"), true, 0x000000, 3, 2, BuffEffectCharmed.class);
		clarity = createPotion(new ResourceLocation("arsmagica2:clarity"), false, 0x000000, 0, 5, BuffEffectClarity.class);
		entangle = createPotion(new ResourceLocation("arsmagica2:entangle"), false, 0x000000, 3, 7, BuffEffectEntangled.class);
		flight = createPotion(new ResourceLocation("arsmagica2:flight"), false, 0x000000, 2, 1, BuffEffectFlight.class);
		frostSlow = createPotion(new ResourceLocation("arsmagica2:frostSlow"), true, 0x000000, 3, 3, BuffEffectFrostSlowed.class);
		fury = createPotion(new ResourceLocation("arsmagica2:fury"), true, 0x000000, 3, 6, BuffEffectFury.class);
		gravityWell = createPotion(new ResourceLocation("arsmagica2:gravityWell"), true, 0x000000, 0, 6, BuffEffectGravityWell.class);
		haste = createPotion(new ResourceLocation("arsmagica2:haste"), false, 0x000000, 2, 3, BuffEffectHaste.class);
		illumination = createPotion(new ResourceLocation("arsmagica2:illumination"), false, 0x000000, 1, 0, BuffEffectIllumination.class);
		instantMana = createPotion(new ResourceLocation("arsmagica2:instantMana"), false, 0x000000, 0, 0, BuffEffectInstantMana.class);
		leap = createPotion(new ResourceLocation("arsmagica2:leap"), false, 0x000000, 0, 2, BuffEffectLeap.class);
		levitation = createPotion(new ResourceLocation("arsmagica2:levitation"), false, 0x000000, 0, 7, BuffEffectLevitation.class);
		magicShield = createPotion(new ResourceLocation("arsmagica2:magicShield"), false, 0x000000, 3, 1, BuffEffectMagicShield.class);
		manaRegen = createPotion(new ResourceLocation("arsmagica2:manaRegen"), false, 0x000000, 3, 5, BuffEffectManaRegen.class);
		manaShield = createPotion(new ResourceLocation("arsmagica2:manaShield"), false, 0x000000, 2, 7, BuffEffectManaShield.class);
		regeneration = createPotion(new ResourceLocation("arsmagica2:regeneration"), false, 0x000000, 2, 6, BuffEffectRegeneration.class);
		scrambleSynapses = createPotion(new ResourceLocation("arsmagica2:scrambleSynapses"), true, 0x000000, 3, 7, BuffEffectScrambleSynapses.class);
		shield = createPotion(new ResourceLocation("arsmagica2:shield"), false, 0x000000, 0, 0, BuffEffectShield.class);
		shrink = createPotion(new ResourceLocation("arsmagica2:shrink"), false, 0x000000, 0, 5, BuffEffectShrink.class);
		silence = createPotion(new ResourceLocation("arsmagica2:silence"), true, 0x000000, 4, 6, BuffEffectSilence.class);
		slowfall = createPotion(new ResourceLocation("arsmagica2:slowfall"), false, 0x000000, 2, 2, BuffEffectSlowfall.class);
		spellReflect = createPotion(new ResourceLocation("arsmagica2:spellReflect"), false, 0x000000, 4, 3, BuffEffectSpellReflect.class);
		swiftSwim = createPotion(new ResourceLocation("arsmagica2:swiftSwim"), false, 0x000000, 4, 7, BuffEffectSwiftSwim.class);
		temporalAnchor = createPotion(new ResourceLocation("arsmagica2:temporalAnchor"), false, 0x000000, 3, 4, BuffEffectTemporalAnchor.class);
		trueSight = createPotion(new ResourceLocation("arsmagica2:trueSight"), false, 0x000000, 2, 4, BuffEffectTrueSight.class);
		waterBreathing = createPotion(new ResourceLocation("arsmagica2:waterBreathing"), false, 0x000000, 2, 0, BuffEffectWaterBreathing.class);
		wateryGrave = createPotion(new ResourceLocation("arsmagica2:wateryGrave"), true, 0x000000, 4, 0, BuffEffectWateryGrave.class);
		manaBoost = createPotion(new ResourceLocation("arsmagica2:manaBoost"), false, 0x000000, 3, 0, BuffMaxManaIncrease.class);
	}
	
	public static Potion createPotion(ResourceLocation loc, boolean isBad, int color, int posX, int posY, Class<? extends BuffEffect> clazz) {
		Potion potion = new AMPotion(isBad, color).setIconIndex(posX, posY).setPotionName(loc.toString());
		GameRegistry.register(potion, loc);
		classForId.put(potion, clazz);
		return potion;
	}
	
	public static PotionEffect getEffect(PotionEffect effect) {
		Class<? extends BuffEffect> clazz = classForId.get(effect.getPotion());
		if (clazz == null) return null;
		if (effect instanceof BuffEffect) return null;
		try {
			Constructor<? extends BuffEffect> constr = clazz.getDeclaredConstructor(int.class, int.class);
			return constr.newInstance(effect.getDuration(), effect.getAmplifier());
		} catch (NoSuchMethodException e) {
			LOGGER.warn("Wrong definition for : " + clazz.getName());
		} catch (SecurityException e) {
			LOGGER.warn("Is this even a thing ? (SecurityException:" + clazz.getName() + ")");
		} catch (InstantiationException e) {
			LOGGER.warn("Could not create : " + clazz.getName());
		} catch (IllegalAccessException e) {
			LOGGER.warn("Could not access : " + clazz.getName());
		} catch (IllegalArgumentException e) {
			LOGGER.warn("Could not create : " + clazz.getName());
		} catch (InvocationTargetException e) {
			LOGGER.warn("InvocationTargetException" + clazz.getName());
		} catch (NullPointerException e) {
			LOGGER.warn("This should never be a thing : NullPointerExecption at " + clazz.getName());
		}
		
		return null;
	}
}
