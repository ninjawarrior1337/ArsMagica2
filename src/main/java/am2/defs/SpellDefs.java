package am2.defs;

import static am2.spell.SpellModifiers.BOUNCE;
import static am2.spell.SpellModifiers.BUFF_POWER;
import static am2.spell.SpellModifiers.DAMAGE;
import static am2.spell.SpellModifiers.DURATION;
import static am2.spell.SpellModifiers.GRAVITY;
import static am2.spell.SpellModifiers.HEALING;
import static am2.spell.SpellModifiers.PIERCING;
import static am2.spell.SpellModifiers.PROCS;
import static am2.spell.SpellModifiers.RADIUS;
import static am2.spell.SpellModifiers.RANGE;
import static am2.spell.SpellModifiers.SPEED;
import static am2.spell.SpellModifiers.TARGET_NONSOLID_BLOCKS;
import static am2.spell.SpellModifiers.VELOCITY_ADDED;

import java.util.EnumSet;

import am2.api.SpellRegistry;
import am2.spell.SpellShape;
import am2.spell.component.Accelerate;
import am2.spell.component.Appropriation;
import am2.spell.component.AstralDistortion;
import am2.spell.component.Attract;
import am2.spell.component.BanishRain;
import am2.spell.component.Blind;
import am2.spell.component.Blink;
import am2.spell.component.Blizzard;
import am2.spell.component.Charm;
import am2.spell.component.ChronoAnchor;
import am2.spell.component.CreateWater;
import am2.spell.component.Daylight;
import am2.spell.component.Dig;
import am2.spell.component.Disarm;
import am2.spell.component.Dispel;
import am2.spell.component.DivineIntervention;
import am2.spell.component.Drought;
import am2.spell.component.Drown;
import am2.spell.component.EnderIntervention;
import am2.spell.component.Entangle;
import am2.spell.component.FallingStar;
import am2.spell.component.FireDamage;
import am2.spell.component.FireRain;
import am2.spell.component.Flight;
import am2.spell.component.Fling;
import am2.spell.component.Forge;
import am2.spell.component.Freeze;
import am2.spell.component.FrostDamage;
import am2.spell.component.Fury;
import am2.spell.component.GravityWell;
import am2.spell.component.Grow;
import am2.spell.component.HarvestPlants;
import am2.spell.component.Haste;
import am2.spell.component.Heal;
import am2.spell.component.Ignition;
import am2.spell.component.Invisiblity;
import am2.spell.component.Knockback;
import am2.spell.component.Leap;
import am2.spell.component.Levitation;
import am2.spell.component.LifeDrain;
import am2.spell.component.LifeTap;
import am2.spell.component.Light;
import am2.spell.component.LightningDamage;
import am2.spell.component.MagicDamage;
import am2.spell.component.ManaDrain;
import am2.spell.component.ManaLink;
import am2.spell.component.ManaShield;
import am2.spell.component.Mark;
import am2.spell.component.Moonrise;
import am2.spell.component.NightVision;
import am2.spell.component.PhysicalDamage;
import am2.spell.component.PlaceBlock;
import am2.spell.component.Plant;
import am2.spell.component.Plow;
import am2.spell.component.RandomTeleport;
import am2.spell.component.Recall;
import am2.spell.component.Reflect;
import am2.spell.component.Regeneration;
import am2.spell.component.Repel;
import am2.spell.component.Rift;
import am2.spell.component.Shield;
import am2.spell.component.Shrink;
import am2.spell.component.Silence;
import am2.spell.component.Slow;
import am2.spell.component.Slowfall;
import am2.spell.component.Storm;
import am2.spell.component.Summon;
import am2.spell.component.SwiftSwim;
import am2.spell.component.Telekinesis;
import am2.spell.component.Transplace;
import am2.spell.component.TrueSight;
import am2.spell.component.WaterBreathing;
import am2.spell.component.WateryGrave;
import am2.spell.component.WizardsAutumn;
import am2.spell.modifier.Bounce;
import am2.spell.modifier.BuffPower;
import am2.spell.modifier.Colour;
import am2.spell.modifier.Damage;
import am2.spell.modifier.Dismembering;
import am2.spell.modifier.Duration;
import am2.spell.modifier.FeatherTouch;
import am2.spell.modifier.Gravity;
import am2.spell.modifier.Healing;
import am2.spell.modifier.Lunar;
import am2.spell.modifier.MiningPower;
import am2.spell.modifier.Piercing;
import am2.spell.modifier.Prosperity;
import am2.spell.modifier.Radius;
import am2.spell.modifier.Range;
import am2.spell.modifier.RuneProcs;
import am2.spell.modifier.Solar;
import am2.spell.modifier.Speed;
import am2.spell.modifier.TargetNonSolidBlocks;
import am2.spell.modifier.VelocityAdded;
import am2.spell.shape.AoE;
import am2.spell.shape.Beam;
import am2.spell.shape.Binding;
import am2.spell.shape.Chain;
import am2.spell.shape.Channel;
import am2.spell.shape.Contingency_Death;
import am2.spell.shape.Contingency_Fall;
import am2.spell.shape.Contingency_Fire;
import am2.spell.shape.Contingency_Health;
import am2.spell.shape.Contingency_Hit;
import am2.spell.shape.MissingShape;
import am2.spell.shape.Projectile;
import am2.spell.shape.Rune;
import am2.spell.shape.Self;
import am2.spell.shape.Touch;
import am2.spell.shape.Wall;
import am2.spell.shape.Wave;
import am2.spell.shape.Zone;
import net.minecraft.util.ResourceLocation;

public class SpellDefs {
	public static final SpellShape MISSING_SHAPE = new MissingShape();
	
	public static void init() {
		SpellRegistry.registerSpellShape("null", null, null, MISSING_SHAPE, null, 0, 0, null);
		
		SpellRegistry.registerSpellModifier("colour", getModifierTexture("Colour"), SkillDefs.SKILL_POINT_0, new Colour(), SkillDefs.TREE_TALENT, 230, 75);

		handleOffenseTree();
		handleDefenseTree();
		handleUtilityTree();
	}
	
	public static void handleOffenseTree() {
		SpellRegistry.registerSpellShape("projectile", getShapeTexture("Projectile"), SkillDefs.SKILL_POINT_0, new Projectile(), SkillDefs.TREE_OFFENSE, 300, 45, EnumSet.of(SPEED, GRAVITY, BOUNCE, PIERCING, DURATION, TARGET_NONSOLID_BLOCKS));
		SpellRegistry.registerSpellComponent("physical_damage", getComponentTexture("PhysicalDamage"), SkillDefs.SKILL_POINT_0, new PhysicalDamage(), SkillDefs.TREE_OFFENSE, 300, 90, EnumSet.of(DAMAGE), "arsmagica2:projectile");
		SpellRegistry.registerSpellModifier("gravity", getModifierTexture("Gravity"), SkillDefs.SKILL_POINT_0, new Gravity(), SkillDefs.TREE_OFFENSE, 255, 70, "arsmagica2:projectile");
		SpellRegistry.registerSpellModifier("bounce", getModifierTexture("Bounce"), SkillDefs.SKILL_POINT_0, new Bounce(), SkillDefs.TREE_OFFENSE, 345, 70, "arsmagica2:projectile");
		
		SpellRegistry.registerSpellComponent("fire_damage", getComponentTexture("FireDamage"), SkillDefs.SKILL_POINT_0, new FireDamage(), SkillDefs.TREE_OFFENSE, 210, 135, EnumSet.of(DAMAGE), "arsmagica2:physical_damage");
		SpellRegistry.registerSpellComponent("lightning_damage", getComponentTexture("LightningDamage"), SkillDefs.SKILL_POINT_0, new LightningDamage(), SkillDefs.TREE_OFFENSE, 255, 135, EnumSet.of(DAMAGE), "arsmagica2:fire_damage");
		SpellRegistry.registerSpellComponent("ignition", getComponentTexture("Ignition"), SkillDefs.SKILL_POINT_1, new Ignition(), SkillDefs.TREE_OFFENSE, 165, 135, EnumSet.of(DURATION), "arsmagica2:fire_damage");
		SpellRegistry.registerSpellComponent("forge", getComponentTexture("Forge"), SkillDefs.SKILL_POINT_1, new Forge(), SkillDefs.TREE_OFFENSE, 120, 135, null, "arsmagica2:ignition");	
		
		SpellRegistry.registerSpellComponent("magic_damage", getComponentTexture("MagicDamage"), SkillDefs.SKILL_POINT_0, new MagicDamage(), SkillDefs.TREE_OFFENSE, 390, 135, EnumSet.of(DAMAGE), "arsmagica2:physical_damage");
		SpellRegistry.registerSpellComponent("frost_damage", getComponentTexture("FrostDamage"), SkillDefs.SKILL_POINT_0, new FrostDamage(), SkillDefs.TREE_OFFENSE, 345, 135, EnumSet.of(DAMAGE, BUFF_POWER), "arsmagica2:magic_damage");
		
		SpellRegistry.registerSpellComponent("drown", getComponentTexture("Drown"), SkillDefs.SKILL_POINT_0, new Drown(), SkillDefs.TREE_OFFENSE, 435, 135, EnumSet.of(DAMAGE), "arsmagica2:magic_damage");
		
		SpellRegistry.registerSpellComponent("blind", getComponentTexture("Blind"), SkillDefs.SKILL_POINT_1, new Blind(), SkillDefs.TREE_OFFENSE, 233, 180, EnumSet.of(BUFF_POWER, DURATION), "arsmagica2:fire_damage", "arsmagica2:lightning_damage");
		SpellRegistry.registerSpellShape("aoe", getShapeTexture("AoE"), SkillDefs.SKILL_POINT_1, new AoE(), SkillDefs.TREE_OFFENSE, 300, 180, EnumSet.of(GRAVITY, RADIUS), "arsmagica2:frost_damage", "arsmagica2:physical_damage", "arsmagica2:fire_damage", "arsmagica2:lightning_damage", "arsmagica2:magic_damage");
		SpellRegistry.registerSpellComponent("freeze", getComponentTexture("Freeze"), SkillDefs.SKILL_POINT_1, new Freeze(), SkillDefs.TREE_OFFENSE, 345, 180, EnumSet.of(BUFF_POWER, DURATION), "arsmagica2:frost_damage");
		SpellRegistry.registerSpellComponent("knockback", getComponentTexture("Knockback"), SkillDefs.SKILL_POINT_1, new Knockback(), SkillDefs.TREE_OFFENSE, 390, 180, EnumSet.of(VELOCITY_ADDED), "arsmagica2:magic_damage");
		
		SpellRegistry.registerSpellShape("contingency_fire", getShapeTexture("Contingency_Fire"), SkillDefs.SKILL_POINT_1, new Contingency_Fire(), SkillDefs.TREE_OFFENSE, 165, 190, null, "arsmagica2:ignition");
		SpellRegistry.registerSpellModifier("solar", getModifierTexture("Solar"), SkillDefs.SKILL_POINT_2, new Solar(), SkillDefs.TREE_OFFENSE, 210, 255, "arsmagica2:blind");
		
		SpellRegistry.registerSpellComponent("storm", getComponentTexture("Storm"), SkillDefs.SKILL_POINT_2, new Storm(), SkillDefs.TREE_OFFENSE, 255, 225, null, "arsmagica2:lightning_damage");
		SpellRegistry.registerSpellComponent("astral_distortion", getComponentTexture("AstralDistortion"), SkillDefs.SKILL_POINT_1, new AstralDistortion(), SkillDefs.TREE_OFFENSE, 367, 215, EnumSet.of(BUFF_POWER, DURATION), "arsmagica2:magic_damage", "arsmagica2:frost_damage");
		SpellRegistry.registerSpellComponent("silence", getComponentTexture("Silence"), SkillDefs.SKILL_POINT_2, new Silence(), SkillDefs.TREE_OFFENSE, 345, 245, null, "arsmagica2:astral_distortion");
		
		SpellRegistry.registerSpellComponent("fling", getComponentTexture("Fling"), SkillDefs.SKILL_POINT_1, new Fling(), SkillDefs.TREE_OFFENSE, 390, 245, EnumSet.of(VELOCITY_ADDED), "arsmagica2:knockback");
		SpellRegistry.registerSpellModifier("velocity_added", getModifierTexture("VelocityAdded"), SkillDefs.SKILL_POINT_2, new VelocityAdded(), SkillDefs.TREE_OFFENSE, 390, 290, "arsmagica2:fling");
		SpellRegistry.registerSpellComponent("watery_grave", getComponentTexture("WateryGrave"), SkillDefs.SKILL_POINT_1, new WateryGrave(), SkillDefs.TREE_OFFENSE, 435, 245, EnumSet.of(BUFF_POWER, DURATION), "arsmagica2:drown");
		
		SpellRegistry.registerSpellModifier("piercing", getModifierTexture("Piercing"), SkillDefs.SKILL_POINT_2, new Piercing(), SkillDefs.TREE_OFFENSE, 323, 215, "arsmagica2:freeze");
		
		SpellRegistry.registerSpellShape("beam", getShapeTexture("Beam"), SkillDefs.SKILL_POINT_2, new Beam(), SkillDefs.TREE_OFFENSE, 300, 270, EnumSet.of(RANGE, TARGET_NONSOLID_BLOCKS), "arsmagica2:aoe");	
		SpellRegistry.registerSpellModifier("damage", getModifierTexture("Damage"), SkillDefs.SKILL_POINT_2, new Damage(), SkillDefs.TREE_OFFENSE, 300, 315, "arsmagica2:beam");
		SpellRegistry.registerSpellComponent("fury", getComponentTexture("Fury"), SkillDefs.SKILL_POINT_2, new Fury(), SkillDefs.TREE_OFFENSE, 255, 315, EnumSet.of(BUFF_POWER, DURATION), "arsmagica2:beam", "arsmagica2:storm");
		SpellRegistry.registerSpellShape("wave", getShapeTexture("Wave"), SkillDefs.SKILL_POINT_2, new Wave(), SkillDefs.TREE_OFFENSE, 367, 315, EnumSet.of(RADIUS, DURATION, SPEED, GRAVITY, PIERCING), "arsmagica2:beam", "arsmagica2:fling");	
		
		SpellRegistry.registerSpellComponent("blizzard", getComponentTexture("Blizzard"), SkillDefs.SILVER_POINT, new Blizzard(), SkillDefs.TREE_OFFENSE, 75, 45, EnumSet.of(RADIUS, DAMAGE, DURATION));
		SpellRegistry.registerSpellComponent("falling_star", getComponentTexture("FallingStar"), SkillDefs.SILVER_POINT, new FallingStar(), SkillDefs.TREE_OFFENSE, 75, 90, EnumSet.of(DAMAGE));
		SpellRegistry.registerSpellComponent("fire_rain", getComponentTexture("FireRain"), SkillDefs.SILVER_POINT, new FireRain(), SkillDefs.TREE_OFFENSE, 75, 135, EnumSet.of(RADIUS, DAMAGE, DURATION));
		SpellRegistry.registerSpellModifier("dismembering", getModifierTexture("Dismembering"), SkillDefs.SILVER_POINT, new Dismembering(), SkillDefs.TREE_OFFENSE, 75, 180);
	}
	
	public static void handleDefenseTree () {
		//defense tree
		SpellRegistry.registerSpellShape("self", getShapeTexture("Self"), SkillDefs.SKILL_POINT_0, new Self(), SkillDefs.TREE_DEFENSE, 267, 45, null);

		SpellRegistry.registerSpellComponent("leap", getComponentTexture("Leap"), SkillDefs.SKILL_POINT_0, new Leap(), SkillDefs.TREE_DEFENSE, 222, 90, EnumSet.of(BUFF_POWER, DURATION), "arsmagica2:self");
		SpellRegistry.registerSpellComponent("regeneration", getComponentTexture("Regeneration"), SkillDefs.SKILL_POINT_0, new Regeneration(), SkillDefs.TREE_DEFENSE, 357, 90, EnumSet.of(BUFF_POWER, DURATION), "arsmagica2:self");

		SpellRegistry.registerSpellComponent("shrink", getComponentTexture("Shrink"), SkillDefs.SKILL_POINT_0, new Shrink(), SkillDefs.TREE_DEFENSE, 402, 90, EnumSet.of(BUFF_POWER, DURATION), "arsmagica2:regeneration");
		SpellRegistry.registerSpellComponent("slowfall", getComponentTexture("Slowfall"), SkillDefs.SKILL_POINT_0, new Slowfall(), SkillDefs.TREE_DEFENSE, 222, 135, EnumSet.of(BUFF_POWER, DURATION), "arsmagica2:leap");
		SpellRegistry.registerSpellComponent("heal", getComponentTexture("Heal"), SkillDefs.SKILL_POINT_0, new Heal(), SkillDefs.TREE_DEFENSE, 357, 135, EnumSet.of(HEALING), "arsmagica2:regeneration");
		SpellRegistry.registerSpellComponent("life_tap", getComponentTexture("LifeTap"), SkillDefs.SKILL_POINT_1, new LifeTap(), SkillDefs.TREE_DEFENSE, 312, 135, EnumSet.of(DAMAGE), "arsmagica2:heal");
		SpellRegistry.registerSpellModifier("healing", getModifierTexture("Healing"), SkillDefs.SKILL_POINT_2, new Healing(), SkillDefs.TREE_DEFENSE, 402, 135, "arsmagica2:heal");

		SpellRegistry.registerSpellComponent("summon", getComponentTexture("Summon"), SkillDefs.SKILL_POINT_1, new Summon(), SkillDefs.TREE_DEFENSE, 267, 135, EnumSet.of(DURATION), "arsmagica2:life_tap");
		SpellRegistry.registerSpellShape("contingency_damage", getShapeTexture("Contingency_Damage"), SkillDefs.SKILL_POINT_1, new Contingency_Hit(), SkillDefs.TREE_DEFENSE, 447, 180, null, "arsmagica2:healing");

		SpellRegistry.registerSpellComponent("haste", getComponentTexture("Haste"), SkillDefs.SKILL_POINT_0, new Haste(), SkillDefs.TREE_DEFENSE, 177, 155, EnumSet.of(BUFF_POWER, DURATION), "arsmagica2:slowfall");
		SpellRegistry.registerSpellComponent("slow", getComponentTexture("Slow"), SkillDefs.SKILL_POINT_0, new Slow(), SkillDefs.TREE_DEFENSE, 132, 155, EnumSet.of(BUFF_POWER, DURATION), "arsmagica2:slowfall");

		SpellRegistry.registerSpellComponent("gravity_well", getComponentTexture("GravityWell"), SkillDefs.SKILL_POINT_1, new GravityWell(), SkillDefs.TREE_DEFENSE, 222, 180, EnumSet.of(BUFF_POWER, DURATION), "arsmagica2:slowfall");
		SpellRegistry.registerSpellComponent("life_drain", getComponentTexture("LifeDrain"), SkillDefs.SKILL_POINT_1, new LifeDrain(), SkillDefs.TREE_DEFENSE, 312, 180, EnumSet.of(DAMAGE), "arsmagica2:life_tap");
		SpellRegistry.registerSpellComponent("dispel", getComponentTexture("Dispel"), SkillDefs.SKILL_POINT_1, new Dispel(), SkillDefs.TREE_DEFENSE, 357, 180, null, "arsmagica2:heal");

		SpellRegistry.registerSpellShape("contingency_fall", getShapeTexture("Contingency_Fall"), SkillDefs.SKILL_POINT_1, new Contingency_Fall(), SkillDefs.TREE_DEFENSE, 267, 180, null, "arsmagica2:gravity_well");

		SpellRegistry.registerSpellComponent("swift_swim", getComponentTexture("SwiftSwim"), SkillDefs.SKILL_POINT_0, new SwiftSwim(), SkillDefs.TREE_DEFENSE, 177, 200, EnumSet.of(BUFF_POWER, DURATION), "arsmagica2:haste");
		SpellRegistry.registerSpellComponent("repel", getComponentTexture("Repel"), SkillDefs.SKILL_POINT_1, new Repel(), SkillDefs.TREE_DEFENSE, 132, 200, null, "arsmagica2:slow");

		SpellRegistry.registerSpellComponent("levitate", getComponentTexture("Levitate"), SkillDefs.SKILL_POINT_1, new Levitation(), SkillDefs.TREE_DEFENSE, 222, 225, EnumSet.of(BUFF_POWER, DURATION), "arsmagica2:gravity_well");
		SpellRegistry.registerSpellComponent("mana_drain", getComponentTexture("ManaDrain"), SkillDefs.SKILL_POINT_1, new ManaDrain(), SkillDefs.TREE_DEFENSE, 312, 225, null, "arsmagica2:life_drain");
		SpellRegistry.registerSpellShape("zone", getShapeTexture("Zone"), SkillDefs.SKILL_POINT_2, new Zone(), SkillDefs.TREE_DEFENSE, 357, 225, EnumSet.of(RADIUS, GRAVITY, DURATION), "arsmagica2:dispel");

		SpellRegistry.registerSpellShape("wall", getShapeTexture("Wall"), SkillDefs.SKILL_POINT_1, new Wall(), SkillDefs.TREE_DEFENSE, 87, 200, EnumSet.of(DURATION, RADIUS, GRAVITY), "arsmagica2:repel");
		SpellRegistry.registerSpellComponent("accelerate", getComponentTexture("Accelerate"), SkillDefs.SKILL_POINT_1, new Accelerate(), SkillDefs.TREE_DEFENSE, 177, 245, null, "arsmagica2:swift_swim");
		SpellRegistry.registerSpellComponent("entangle", getComponentTexture("Entangle"), SkillDefs.SKILL_POINT_1, new Entangle(), SkillDefs.TREE_DEFENSE, 132, 245, EnumSet.of(BUFF_POWER, DURATION), "arsmagica2:repel");
		SpellRegistry.registerSpellComponent("appropriation", getComponentTexture("Appropriation"), SkillDefs.SKILL_POINT_2, new Appropriation(), SkillDefs.TREE_DEFENSE, 87, 245, null, "arsmagica2:entangle");

		SpellRegistry.registerSpellComponent("flight", getComponentTexture("Flight"), SkillDefs.SKILL_POINT_2, new Flight(), SkillDefs.TREE_DEFENSE, 222, 270, EnumSet.of(BUFF_POWER, DURATION), "arsmagica2:levitate");
		SpellRegistry.registerSpellComponent("shield", getComponentTexture("Shield"), SkillDefs.SKILL_POINT_0, new Shield(), SkillDefs.TREE_DEFENSE, 357, 270, EnumSet.of(BUFF_POWER, DURATION), "arsmagica2:zone");

		SpellRegistry.registerSpellShape("contingency_health", getShapeTexture("Contingency_Health"), SkillDefs.SKILL_POINT_2, new Contingency_Health(), SkillDefs.TREE_DEFENSE, 402, 270, null, "arsmagica2:shield");

		SpellRegistry.registerSpellShape("rune", getShapeTexture("Rune"), SkillDefs.SKILL_POINT_1, new Rune(), SkillDefs.TREE_DEFENSE, 157, 315, EnumSet.of(PROCS, TARGET_NONSOLID_BLOCKS), "arsmagica2:accelerate", "arsmagica2:entangle");

		SpellRegistry.registerSpellModifier("rune_procs", getModifierTexture("RuneProcs"), SkillDefs.SKILL_POINT_1, new RuneProcs(), SkillDefs.TREE_DEFENSE, 157, 360, "arsmagica2:rune");

		SpellRegistry.registerSpellModifier("speed", getModifierTexture("Speed"), SkillDefs.SKILL_POINT_2, new Speed(), SkillDefs.TREE_DEFENSE, 202, 315, "arsmagica2:accelerate", "arsmagica2:flight");

		SpellRegistry.registerSpellComponent("reflect", getComponentTexture("Reflect"), SkillDefs.SKILL_POINT_2, new Reflect(), SkillDefs.TREE_DEFENSE, 357, 315, EnumSet.of(BUFF_POWER, DURATION), "arsmagica2:shield");
		SpellRegistry.registerSpellComponent("chrono_anchor", getComponentTexture("ChronoAnchor"), SkillDefs.SKILL_POINT_2, new ChronoAnchor(), SkillDefs.TREE_DEFENSE, 312, 315, EnumSet.of(BUFF_POWER, DURATION), "arsmagica2:reflect");

		SpellRegistry.registerSpellModifier("duration", getModifierTexture("Duration"), SkillDefs.SKILL_POINT_2, new Duration(), SkillDefs.TREE_DEFENSE, 312, 360, "arsmagica2:chrono_anchor");

		SpellRegistry.registerSpellComponent("mana_link", getComponentTexture("ManaLink"), SkillDefs.SILVER_POINT, new ManaLink(), SkillDefs.TREE_DEFENSE, 30, 45, null);
		SpellRegistry.registerSpellComponent("mana_shield", getComponentTexture("ManaShield"), SkillDefs.SILVER_POINT, new ManaShield(), SkillDefs.TREE_DEFENSE, 30, 90, EnumSet.of(BUFF_POWER, DURATION));
		SpellRegistry.registerSpellModifier("buff_power", getModifierTexture("BuffPower"), SkillDefs.SILVER_POINT, new BuffPower(), SkillDefs.TREE_DEFENSE, 30, 135);
		
	}
	
	public static void handleUtilityTree () {
		//utility tree
		SpellRegistry.registerSpellShape("touch", getShapeTexture("Touch"), SkillDefs.SKILL_POINT_0, new Touch(), SkillDefs.TREE_UTILITY, 275, 75, null);

		SpellRegistry.registerSpellComponent("dig", getComponentTexture("Dig"), SkillDefs.SKILL_POINT_0, new Dig(), SkillDefs.TREE_UTILITY, 275, 120, null, "arsmagica2:touch");
		SpellRegistry.registerSpellComponent("wizards_autumn", getComponentTexture("WizardsAutumn"), SkillDefs.SKILL_POINT_0, new WizardsAutumn(), SkillDefs.TREE_UTILITY, 315, 120, null, "arsmagica2:dig");
		SpellRegistry.registerSpellModifier("target_non_solid", getModifierTexture("TargetNonSolid"), SkillDefs.SKILL_POINT_0, new TargetNonSolidBlocks(), SkillDefs.TREE_UTILITY, 230, 75, "arsmagica2:touch");

		SpellRegistry.registerSpellComponent("place_block", getComponentTexture("PlaceBlock"), SkillDefs.SKILL_POINT_0, new PlaceBlock(), SkillDefs.TREE_UTILITY, 185, 93, null, "arsmagica2:dig");
		SpellRegistry.registerSpellModifier("feather_touch", getModifierTexture("FeatherTouch"), SkillDefs.SKILL_POINT_0, new FeatherTouch(), SkillDefs.TREE_UTILITY, 230, 137, "arsmagica2:dig");
		SpellRegistry.registerSpellModifier("mining_power", getModifierTexture("MiningPower"), SkillDefs.SKILL_POINT_1, new MiningPower(), SkillDefs.TREE_UTILITY, 185, 137, "arsmagica2:feather_touch");

		SpellRegistry.registerSpellComponent("light", getComponentTexture("Light"), SkillDefs.SKILL_POINT_0, new Light(), SkillDefs.TREE_UTILITY, 275, 165, null, "arsmagica2:dig");
		SpellRegistry.registerSpellComponent("night_vision", getComponentTexture("NightVision"), SkillDefs.SKILL_POINT_0, new NightVision(), SkillDefs.TREE_UTILITY, 185, 165, null, "arsmagica2:light");

		SpellRegistry.registerSpellShape("binding", getShapeTexture("Binding"), SkillDefs.SKILL_POINT_0, new Binding(), SkillDefs.TREE_UTILITY, 275, 210, null, "arsmagica2:light");
		SpellRegistry.registerSpellComponent("disarm", getComponentTexture("Disarm"), SkillDefs.SKILL_POINT_0, new Disarm(), SkillDefs.TREE_UTILITY, 230, 210, null, "arsmagica2:binding");
		SpellRegistry.registerSpellComponent("charm", getComponentTexture("Charm"), SkillDefs.SKILL_POINT_0, new Charm(), SkillDefs.TREE_UTILITY, 315, 235, null, "arsmagica2:binding");
		SpellRegistry.registerSpellComponent("true_sight", getComponentTexture("TrueSight"), SkillDefs.SKILL_POINT_0, new TrueSight(), SkillDefs.TREE_UTILITY, 185, 210, null, "arsmagica2:night_vision");

		SpellRegistry.registerSpellModifier("lunar", getModifierTexture("Lunar"), SkillDefs.SKILL_POINT_2, new Lunar(), SkillDefs.TREE_UTILITY, 145, 210, "arsmagica2:true_sight");

		SpellRegistry.registerSpellComponent("harvest_plants", getComponentTexture("HarvestPlants"), SkillDefs.SKILL_POINT_1, new HarvestPlants(), SkillDefs.TREE_UTILITY, 365, 120, null, "arsmagica2:binding");
		SpellRegistry.registerSpellComponent("plow", getComponentTexture("Plow"), SkillDefs.SKILL_POINT_0, new Plow(), SkillDefs.TREE_UTILITY, 365, 165, null, "arsmagica2:binding");
		SpellRegistry.registerSpellComponent("plant", getComponentTexture("Plant"), SkillDefs.SKILL_POINT_0, new Plant(), SkillDefs.TREE_UTILITY, 365, 210, null, "arsmagica2:binding");
		SpellRegistry.registerSpellComponent("create_water", getComponentTexture("CreateWater"), SkillDefs.SKILL_POINT_1, new CreateWater(), SkillDefs.TREE_UTILITY, 365, 255, null, "arsmagica2:binding");
		SpellRegistry.registerSpellComponent("drought", getComponentTexture("Drought"), SkillDefs.SKILL_POINT_1, new Drought(), SkillDefs.TREE_UTILITY, 365, 300, null, "arsmagica2:binding");

		SpellRegistry.registerSpellComponent("banish_rain", getComponentTexture("BanishRain"), SkillDefs.SKILL_POINT_1, new BanishRain(), SkillDefs.TREE_UTILITY, 365, 345, null, "arsmagica2:drought");
		SpellRegistry.registerSpellComponent("water_breathing", getComponentTexture("WaterBreathing"), SkillDefs.SKILL_POINT_0, new WaterBreathing(), SkillDefs.TREE_UTILITY, 410, 345, null, "arsmagica2:drought");

		SpellRegistry.registerSpellComponent("grow", getComponentTexture("Grow"), SkillDefs.SKILL_POINT_2, new Grow(), SkillDefs.TREE_UTILITY, 410, 210, null, "arsmagica2:drought", "arsmagica2:create_water", "arsmagica2:plant", "arsmagica2:plow", "arsmagica2:harvest_plants");

		SpellRegistry.registerSpellShape("chain", getShapeTexture("Chain"), SkillDefs.SKILL_POINT_2, new Chain(), SkillDefs.TREE_UTILITY, 455, 210, null, "arsmagica2:grow");

		SpellRegistry.registerSpellComponent("rift", getComponentTexture("Rift"), SkillDefs.SKILL_POINT_1, new Rift(), SkillDefs.TREE_UTILITY, 275, 255, null, "arsmagica2:binding");
		SpellRegistry.registerSpellComponent("invisibility", getComponentTexture("Invisibility"), SkillDefs.SKILL_POINT_1, new Invisiblity(), SkillDefs.TREE_UTILITY, 185, 255, null, "arsmagica2:true_sight");

		SpellRegistry.registerSpellComponent("random_teleport", getComponentTexture("RandomTeleport"), SkillDefs.SKILL_POINT_0, new RandomTeleport(), SkillDefs.TREE_UTILITY, 185, 300, null, "arsmagica2:invisibility");
		SpellRegistry.registerSpellComponent("attract", getComponentTexture("Attract"), SkillDefs.SKILL_POINT_1, new Attract(), SkillDefs.TREE_UTILITY, 245, 300, null, "arsmagica2:rift");
		SpellRegistry.registerSpellComponent("telekinesis", getComponentTexture("Telekinesis"), SkillDefs.SKILL_POINT_1, new Telekinesis(), SkillDefs.TREE_UTILITY, 305, 300, null, "arsmagica2:rift");

		SpellRegistry.registerSpellComponent("blink", getComponentTexture("Blink"), SkillDefs.SKILL_POINT_1, new Blink(), SkillDefs.TREE_UTILITY, 185, 345, null, "arsmagica2:random_teleport");
		SpellRegistry.registerSpellModifier("range", getModifierTexture("Range"), SkillDefs.SKILL_POINT_2, new Range(), SkillDefs.TREE_UTILITY, 140, 345, "arsmagica2:blink");
		SpellRegistry.registerSpellShape("channel", getShapeTexture("Channel"), SkillDefs.SKILL_POINT_1, new Channel(), SkillDefs.TREE_UTILITY, 275, 345, null, "arsmagica2:attract", "arsmagica2:telekinesis");

		SpellRegistry.registerSpellModifier("radius", getModifierTexture("Radius"), SkillDefs.SKILL_POINT_2, new Radius(), SkillDefs.TREE_UTILITY, 275, 390, "arsmagica2:channel");
		SpellRegistry.registerSpellComponent("transplace", getComponentTexture("Transplace"), SkillDefs.SKILL_POINT_0, new Transplace(), SkillDefs.TREE_UTILITY, 185, 390, null, "arsmagica2:blink");

		SpellRegistry.registerSpellComponent("mark", getComponentTexture("Mark"), SkillDefs.SKILL_POINT_1, new Mark(), SkillDefs.TREE_UTILITY, 155, 435, null, "arsmagica2:transplace");
		SpellRegistry.registerSpellComponent("recall", getComponentTexture("Recall"), SkillDefs.SKILL_POINT_1, new Recall(), SkillDefs.TREE_UTILITY, 215, 435, null, "arsmagica2:transplace");

		SpellRegistry.registerSpellComponent("divine_intervention", getComponentTexture("DivineIntervention"), SkillDefs.SKILL_POINT_2, new DivineIntervention(), SkillDefs.TREE_UTILITY, 172, 480, null, "arsmagica2:recall", "arsmagica2:mark");
		SpellRegistry.registerSpellComponent("ender_intervention", getComponentTexture("EnderIntervention"), SkillDefs.SKILL_POINT_2, new EnderIntervention(), SkillDefs.TREE_UTILITY, 198, 480, null, "arsmagica2:recall", "arsmagica2:mark");

		SpellRegistry.registerSpellShape("contingency_death", getShapeTexture("Contingency_Death"), SkillDefs.SKILL_POINT_2, new Contingency_Death(), SkillDefs.TREE_UTILITY, 198, 524, null, "arsmagica2:ender_intervention");

		SpellRegistry.registerSpellComponent("daylight", getComponentTexture("Daylight"), SkillDefs.SILVER_POINT, new Daylight(), SkillDefs.TREE_UTILITY, 75, 45, null);
		SpellRegistry.registerSpellComponent("moonrise", getComponentTexture("Moonrise"), SkillDefs.SILVER_POINT, new Moonrise(), SkillDefs.TREE_UTILITY, 75, 90, null);
		SpellRegistry.registerSpellModifier("prosperity", getModifierTexture("Prosperity"), SkillDefs.SILVER_POINT, new Prosperity(), SkillDefs.TREE_UTILITY, 75, 135);
	}
	
	private static ResourceLocation getComponentTexture(String name) {
		return new ResourceLocation("arsmagica2", "items/spells/components/" + name);
	}
	
	private static ResourceLocation getShapeTexture(String name) {
		return new ResourceLocation("arsmagica2", "items/spells/shapes/" + name);		
	}
	
	private static ResourceLocation getModifierTexture(String name) {
		return new ResourceLocation("arsmagica2", "items/spells/modifiers/" + name);		
	}
}
