package am2.defs;

import am2.api.SpellRegistry;
import am2.spell.IShape;
import am2.spell.component.Accelerate;
import am2.spell.component.Appropriation;
import am2.spell.component.AstralDistortion;
import am2.spell.component.Attract;
import am2.spell.component.BanishRain;
import am2.spell.component.Blind;
import am2.spell.component.Blizzard;
import am2.spell.component.ChronoAnchor;
import am2.spell.component.Dig;
import am2.spell.component.Dispel;
import am2.spell.component.Drown;
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
import am2.spell.component.Haste;
import am2.spell.component.Heal;
import am2.spell.component.Ignition;
import am2.spell.component.Knockback;
import am2.spell.component.Leap;
import am2.spell.component.Levitation;
import am2.spell.component.LifeDrain;
import am2.spell.component.LifeTap;
import am2.spell.component.LightningDamage;
import am2.spell.component.MagicDamage;
import am2.spell.component.ManaDrain;
import am2.spell.component.ManaLink;
import am2.spell.component.ManaShield;
import am2.spell.component.Mark;
import am2.spell.component.PhysicalDamage;
import am2.spell.component.Recall;
import am2.spell.component.Reflect;
import am2.spell.component.Regeneration;
import am2.spell.component.Repel;
import am2.spell.component.Rift;
import am2.spell.component.ScrambleSynapses;
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
import am2.spell.modifier.Gravity;
import am2.spell.modifier.Healing;
import am2.spell.modifier.Piercing;
import am2.spell.modifier.RuneProcs;
import am2.spell.modifier.Solar;
import am2.spell.modifier.Speed;
import am2.spell.modifier.VelocityAdded;
import am2.spell.shape.AoE;
import am2.spell.shape.Beam;
import am2.spell.shape.Contingency_Death;
import am2.spell.shape.Contingency_Fall;
import am2.spell.shape.Contingency_Fire;
import am2.spell.shape.Contingency_Health;
import am2.spell.shape.Contingency_Hit;
import am2.spell.shape.MissingShape;
import am2.spell.shape.Projectile;
import am2.spell.shape.Rune;
import am2.spell.shape.Self;
import am2.spell.shape.Wall;
import am2.spell.shape.Wave;
import am2.spell.shape.Zone;
import net.minecraft.util.ResourceLocation;

public class SpellDefs {
	public static final IShape MISSING_SHAPE = new MissingShape();
	
	public static void init() {
		SpellRegistry.registerSpellShape("null", null, null, MISSING_SHAPE, null, 0, 0);
		
		SpellRegistry.registerSpellModifier("colour", getModifierTexture("Colour"), SkillDefs.SKILL_POINT_0, new Colour(), SkillDefs.TREE_TALENT, 230, 75);

		handleOffenseTree();
		handleDefenseTree();
		
		
		SpellRegistry.registerSpellShape("contingency_death", getShapeTexture("Contingency_Death"), SkillDefs.SKILL_POINT_0, new Contingency_Death(), SkillDefs.TREE_DEFENSE, 0, 0);
		
		SpellRegistry.registerSpellComponent("attract", getComponentTexture("Attract"), SkillDefs.SKILL_POINT_0, new Attract(), SkillDefs.TREE_UTILITY, 4, 4);
		SpellRegistry.registerSpellComponent("banish_rain", getComponentTexture("BanishRain"), SkillDefs.SKILL_POINT_0, new BanishRain(), SkillDefs.TREE_UTILITY, 4, 4);
		SpellRegistry.registerSpellComponent("dig", getComponentTexture("Dig"), SkillDefs.SKILL_POINT_0, new Dig(), SkillDefs.TREE_UTILITY, 0, 1);
				
		SpellRegistry.registerSpellComponent("mark", getComponentTexture("Mark"), SkillDefs.SKILL_POINT_1, new Mark(), SkillDefs.TREE_UTILITY, 0, 2);
		
		
		SpellRegistry.registerSpellComponent("recall", getComponentTexture("Recall"), SkillDefs.SKILL_POINT_1, new Recall(), SkillDefs.TREE_UTILITY, 0, 3);
		SpellRegistry.registerSpellComponent("rift", getComponentTexture("Rift"), SkillDefs.SKILL_POINT_1, new Rift(), SkillDefs.TREE_UTILITY, 0, 3);
		SpellRegistry.registerSpellComponent("scramble_synapses", getComponentTexture("ScrambleSynapses"), SkillDefs.SKILL_POINT_1, new ScrambleSynapses(), SkillDefs.TREE_UTILITY, 0, 3);
		SpellRegistry.registerSpellComponent("telekinesis", getComponentTexture("Telekinesis"), SkillDefs.SKILL_POINT_1, new Telekinesis(), SkillDefs.TREE_UTILITY, 0, 3);
		SpellRegistry.registerSpellComponent("transplace", getComponentTexture("Transplace"), SkillDefs.SKILL_POINT_1, new Transplace(), SkillDefs.TREE_UTILITY, 0, 3);
		SpellRegistry.registerSpellComponent("true_sight", getComponentTexture("TrueSight"), SkillDefs.SKILL_POINT_1, new TrueSight(), SkillDefs.TREE_UTILITY, 0, 3);
		SpellRegistry.registerSpellComponent("water_breathing", getComponentTexture("WaterBreathing"), SkillDefs.SKILL_POINT_1, new WaterBreathing(), SkillDefs.TREE_UTILITY, 0, 3);
		SpellRegistry.registerSpellComponent("wizards_autumn", getComponentTexture("WizardsAutumn"), SkillDefs.SKILL_POINT_1, new WizardsAutumn(), SkillDefs.TREE_UTILITY, 0, 3);
		
	}
	
	public static void handleOffenseTree() {
		SpellRegistry.registerSpellShape("projectile", getShapeTexture("Projectile"), SkillDefs.SKILL_POINT_0, new Projectile(), SkillDefs.TREE_OFFENSE, 300, 45);
		SpellRegistry.registerSpellComponent("physical_damage", getComponentTexture("PhysicalDamage"), SkillDefs.SKILL_POINT_0, new PhysicalDamage(), SkillDefs.TREE_OFFENSE, 300, 90, "projectile");
		SpellRegistry.registerSpellModifier("gravity", getModifierTexture("Gravity"), SkillDefs.SKILL_POINT_0, new Gravity(), SkillDefs.TREE_OFFENSE, 255, 70, "projectile");
		SpellRegistry.registerSpellModifier("bounce", getModifierTexture("Bounce"), SkillDefs.SKILL_POINT_0, new Bounce(), SkillDefs.TREE_OFFENSE, 345, 70, "projectile");
		
		SpellRegistry.registerSpellComponent("fire_damage", getComponentTexture("FireDamage"), SkillDefs.SKILL_POINT_0, new FireDamage(), SkillDefs.TREE_OFFENSE, 210, 135, "physical_damage");
		SpellRegistry.registerSpellComponent("lightning_damage", getComponentTexture("LightningDamage"), SkillDefs.SKILL_POINT_0, new LightningDamage(), SkillDefs.TREE_OFFENSE, 255, 135, "fire_damage");
		SpellRegistry.registerSpellComponent("ignition", getComponentTexture("Ignition"), SkillDefs.SKILL_POINT_1, new Ignition(), SkillDefs.TREE_OFFENSE, 165, 135, "fire_damage");
		SpellRegistry.registerSpellComponent("forge", getComponentTexture("Forge"), SkillDefs.SKILL_POINT_1, new Forge(), SkillDefs.TREE_OFFENSE, 120, 135, "ignition");	
		
		SpellRegistry.registerSpellComponent("magic_damage", getComponentTexture("MagicDamage"), SkillDefs.SKILL_POINT_0, new MagicDamage(), SkillDefs.TREE_OFFENSE, 390, 135, "physical_damage");
		SpellRegistry.registerSpellComponent("frost_damage", getComponentTexture("FrostDamage"), SkillDefs.SKILL_POINT_0, new FrostDamage(), SkillDefs.TREE_OFFENSE, 345, 135, "magic_damage");
		
		SpellRegistry.registerSpellComponent("drown", getComponentTexture("Drown"), SkillDefs.SKILL_POINT_0, new Drown(), SkillDefs.TREE_OFFENSE, 435, 135, "magic_damage");
		
		SpellRegistry.registerSpellComponent("blind", getComponentTexture("Blind"), SkillDefs.SKILL_POINT_1, new Blind(), SkillDefs.TREE_OFFENSE, 233, 180, "fire_damage", "lightning_damage");
		SpellRegistry.registerSpellShape("aoe", getShapeTexture("AoE"), SkillDefs.SKILL_POINT_1, new AoE(), SkillDefs.TREE_OFFENSE, 300, 180, "frost_damage", "physical_damage", "fire_damage", "lightning_damage", "magic_damage");
		SpellRegistry.registerSpellComponent("freeze", getComponentTexture("Freeze"), SkillDefs.SKILL_POINT_1, new Freeze(), SkillDefs.TREE_OFFENSE, 345, 180, "frost_damage");
		SpellRegistry.registerSpellComponent("knockback", getComponentTexture("Knockback"), SkillDefs.SKILL_POINT_1, new Knockback(), SkillDefs.TREE_OFFENSE, 390, 180, "magic_damage");
		
		SpellRegistry.registerSpellShape("contingency_fire", getShapeTexture("Contingency_Fire"), SkillDefs.SKILL_POINT_1, new Contingency_Fire(), SkillDefs.TREE_OFFENSE, 165, 190, "ignition");
		SpellRegistry.registerSpellModifier("solar", getModifierTexture("Solar"), SkillDefs.SKILL_POINT_2, new Solar(), SkillDefs.TREE_OFFENSE, 210, 255, "blind");
		
		SpellRegistry.registerSpellComponent("storm", getComponentTexture("Storm"), SkillDefs.SKILL_POINT_2, new Storm(), SkillDefs.TREE_OFFENSE, 255, 225, "lightning_damage");
		SpellRegistry.registerSpellComponent("astral_distortion", getComponentTexture("AstralDistortion"), SkillDefs.SKILL_POINT_1, new AstralDistortion(), SkillDefs.TREE_OFFENSE, 367, 215, "magic_damage", "frost_damage");
		SpellRegistry.registerSpellComponent("silence", getComponentTexture("Silence"), SkillDefs.SKILL_POINT_2, new Silence(), SkillDefs.TREE_OFFENSE, 345, 245, "astral_distortion");
		
		SpellRegistry.registerSpellComponent("fling", getComponentTexture("Fling"), SkillDefs.SKILL_POINT_1, new Fling(), SkillDefs.TREE_OFFENSE, 390, 245, "knockback");
		SpellRegistry.registerSpellModifier("velocity_added", getModifierTexture("VelocityAdded"), SkillDefs.SKILL_POINT_2, new VelocityAdded(), SkillDefs.TREE_OFFENSE, 390, 290, "fling");
		SpellRegistry.registerSpellComponent("watery_grave", getComponentTexture("WateryGrave"), SkillDefs.SKILL_POINT_1, new WateryGrave(), SkillDefs.TREE_OFFENSE, 435, 245, "drown");
		
		SpellRegistry.registerSpellModifier("piercing", getModifierTexture("Piercing"), SkillDefs.SKILL_POINT_2, new Piercing(), SkillDefs.TREE_OFFENSE, 323, 215, "freeze");
		
		SpellRegistry.registerSpellShape("beam", getShapeTexture("Beam"), SkillDefs.SKILL_POINT_2, new Beam(), SkillDefs.TREE_OFFENSE, 300, 270, "aoe");	
		SpellRegistry.registerSpellModifier("damage", getModifierTexture("Damage"), SkillDefs.SKILL_POINT_2, new Damage(), SkillDefs.TREE_OFFENSE, 300, 315, "beam");
		SpellRegistry.registerSpellComponent("fury", getComponentTexture("Fury"), SkillDefs.SKILL_POINT_2, new Fury(), SkillDefs.TREE_OFFENSE, 255, 315, "beam", "storm");
		SpellRegistry.registerSpellShape("wave", getShapeTexture("Wave"), SkillDefs.SKILL_POINT_2, new Wave(), SkillDefs.TREE_OFFENSE, 367, 315, "beam", "fling");	
		
		SpellRegistry.registerSpellComponent("blizzard", getComponentTexture("Blizzard"), SkillDefs.SILVER_POINT, new Blizzard(), SkillDefs.TREE_OFFENSE, 75, 45);
		SpellRegistry.registerSpellComponent("falling_star", getComponentTexture("FallingStar"), SkillDefs.SILVER_POINT, new FallingStar(), SkillDefs.TREE_OFFENSE, 75, 90);
		SpellRegistry.registerSpellComponent("fire_rain", getComponentTexture("FireRain"), SkillDefs.SILVER_POINT, new FireRain(), SkillDefs.TREE_OFFENSE, 75, 135);
		SpellRegistry.registerSpellModifier("dismembering", getModifierTexture("Dismembering"), SkillDefs.SILVER_POINT, new Dismembering(), SkillDefs.TREE_OFFENSE, 75, 180);
	}
	
	public static void handleDefenseTree () {
		//defense tree
		SpellRegistry.registerSpellShape("self", getShapeTexture("Self"), SkillDefs.SKILL_POINT_0, new Self(), SkillDefs.TREE_DEFENSE, 267, 45);

		SpellRegistry.registerSpellComponent("leap", getComponentTexture("Leap"), SkillDefs.SKILL_POINT_0, new Leap(), SkillDefs.TREE_DEFENSE, 222, 90, "self");
		SpellRegistry.registerSpellComponent("regeneration", getComponentTexture("Regeneration"), SkillDefs.SKILL_POINT_0, new Regeneration(), SkillDefs.TREE_DEFENSE, 357, 90, "self");

		SpellRegistry.registerSpellComponent("shrink", getComponentTexture("Shrink"), SkillDefs.SKILL_POINT_0, new Shrink(), SkillDefs.TREE_DEFENSE, 402, 90, "regeneration");
		SpellRegistry.registerSpellComponent("slowfall", getComponentTexture("Slowfall"), SkillDefs.SKILL_POINT_0, new Slowfall(), SkillDefs.TREE_DEFENSE, 222, 135, "leap");
		SpellRegistry.registerSpellComponent("heal", getComponentTexture("Heal"), SkillDefs.SKILL_POINT_0, new Heal(), SkillDefs.TREE_DEFENSE, 357, 135, "regeneration");
		SpellRegistry.registerSpellComponent("life_tap", getComponentTexture("LifeTap"), SkillDefs.SKILL_POINT_1, new LifeTap(), SkillDefs.TREE_DEFENSE, 312, 135, "heal");
		SpellRegistry.registerSpellModifier("healing", getModifierTexture("Healing"), SkillDefs.SKILL_POINT_2, new Healing(), SkillDefs.TREE_DEFENSE, 402, 135, "heal");

		SpellRegistry.registerSpellComponent("summon", getComponentTexture("Summon"), SkillDefs.SKILL_POINT_1, new Summon(), SkillDefs.TREE_DEFENSE, 267, 135, "life_tap");
		SpellRegistry.registerSpellShape("contingency_damage", getShapeTexture("Contingency_Damage"), SkillDefs.SKILL_POINT_1, new Contingency_Hit(), SkillDefs.TREE_DEFENSE, 447, 180, "healing");

		SpellRegistry.registerSpellComponent("haste", getComponentTexture("Haste"), SkillDefs.SKILL_POINT_0, new Haste(), SkillDefs.TREE_DEFENSE, 177, 155, "slowfall");
		SpellRegistry.registerSpellComponent("slow", getComponentTexture("Slow"), SkillDefs.SKILL_POINT_0, new Slow(), SkillDefs.TREE_DEFENSE, 132, 155, "slowfall");

		SpellRegistry.registerSpellComponent("gravity_well", getComponentTexture("GravityWell"), SkillDefs.SKILL_POINT_1, new GravityWell(), SkillDefs.TREE_DEFENSE, 222, 180, "slowfall");
		SpellRegistry.registerSpellComponent("life_drain", getComponentTexture("LifeDrain"), SkillDefs.SKILL_POINT_1, new LifeDrain(), SkillDefs.TREE_DEFENSE, 312, 180, "life_tap");
		SpellRegistry.registerSpellComponent("dispel", getComponentTexture("Dispel"), SkillDefs.SKILL_POINT_1, new Dispel(), SkillDefs.TREE_DEFENSE, 357, 180, "Heal");

		SpellRegistry.registerSpellShape("contingency_fall", getShapeTexture("Contingency_Fall"), SkillDefs.SKILL_POINT_1, new Contingency_Fall(), SkillDefs.TREE_DEFENSE, 267, 180, "gravity_well");

		SpellRegistry.registerSpellComponent("swift_swim", getComponentTexture("SwiftSwim"), SkillDefs.SKILL_POINT_0, new SwiftSwim(), SkillDefs.TREE_DEFENSE, 177, 200, "haste");
		SpellRegistry.registerSpellComponent("repel", getComponentTexture("Repel"), SkillDefs.SKILL_POINT_1, new Repel(), SkillDefs.TREE_DEFENSE, 132, 200, "slow");

		SpellRegistry.registerSpellComponent("levitate", getComponentTexture("Levitate"), SkillDefs.SKILL_POINT_1, new Levitation(), SkillDefs.TREE_DEFENSE, 222, 225, "gravity_well");
		SpellRegistry.registerSpellComponent("mana_drain", getComponentTexture("ManaDrain"), SkillDefs.SKILL_POINT_1, new ManaDrain(), SkillDefs.TREE_DEFENSE, 312, 225, "life_drain");
		SpellRegistry.registerSpellShape("zone", getShapeTexture("Zone"), SkillDefs.SKILL_POINT_2, new Zone(), SkillDefs.TREE_DEFENSE, 357, 225, "dispel");

		SpellRegistry.registerSpellShape("wall", getShapeTexture("Wall"), SkillDefs.SKILL_POINT_1, new Wall(), SkillDefs.TREE_DEFENSE, 87, 200, "repel");
		SpellRegistry.registerSpellComponent("accelerate", getComponentTexture("Accelerate"), SkillDefs.SKILL_POINT_1, new Accelerate(), SkillDefs.TREE_DEFENSE, 177, 245, "swift_swim");
		SpellRegistry.registerSpellComponent("entangle", getComponentTexture("Entangle"), SkillDefs.SKILL_POINT_1, new Entangle(), SkillDefs.TREE_DEFENSE, 132, 245, "repel");
		SpellRegistry.registerSpellComponent("appropriation", getComponentTexture("Appropriation"), SkillDefs.SKILL_POINT_2, new Appropriation(), SkillDefs.TREE_DEFENSE, 87, 245, "entangle");

		SpellRegistry.registerSpellComponent("flight", getComponentTexture("Flight"), SkillDefs.SKILL_POINT_2, new Flight(), SkillDefs.TREE_DEFENSE, 222, 270, "levitate");
		SpellRegistry.registerSpellComponent("shield", getComponentTexture("Shield"), SkillDefs.SKILL_POINT_0, new Shield(), SkillDefs.TREE_DEFENSE, 357, 270, "zone");

		SpellRegistry.registerSpellShape("contingency_health", getShapeTexture("Contingency_Health"), SkillDefs.SKILL_POINT_2, new Contingency_Health(), SkillDefs.TREE_DEFENSE, 402, 270, "shield");

		SpellRegistry.registerSpellShape("rune", getShapeTexture("Rune"), SkillDefs.SKILL_POINT_1, new Rune(), SkillDefs.TREE_DEFENSE, 157, 315, "accelerate", "entangle");

		SpellRegistry.registerSpellModifier("rune_procs", getModifierTexture("RuneProcs"), SkillDefs.SKILL_POINT_1, new RuneProcs(), SkillDefs.TREE_DEFENSE, 157, 360, "rune");

		SpellRegistry.registerSpellModifier("speed", getModifierTexture("Speed"), SkillDefs.SKILL_POINT_2, new Speed(), SkillDefs.TREE_DEFENSE, 202, 315, "accelerate", "flight");

		SpellRegistry.registerSpellComponent("reflect", getComponentTexture("Reflect"), SkillDefs.SKILL_POINT_2, new Reflect(), SkillDefs.TREE_DEFENSE, 357, 315, "shield");
		SpellRegistry.registerSpellComponent("chrono_anchor", getComponentTexture("ChronoAnchor"), SkillDefs.SKILL_POINT_2, new ChronoAnchor(), SkillDefs.TREE_DEFENSE, 312, 315, "reflect");

		SpellRegistry.registerSpellModifier("duration", getModifierTexture("Duration"), SkillDefs.SKILL_POINT_2, new Duration(), SkillDefs.TREE_DEFENSE, 312, 360, "chrono_anchor");

		SpellRegistry.registerSpellComponent("mana_link", getComponentTexture("ManaLink"), SkillDefs.SILVER_POINT, new ManaLink(), SkillDefs.TREE_DEFENSE, 30, 45);
		SpellRegistry.registerSpellComponent("mana_shield", getComponentTexture("ManaShield"), SkillDefs.SILVER_POINT, new ManaShield(), SkillDefs.TREE_DEFENSE, 30, 90);
		SpellRegistry.registerSpellModifier("buff_power", getModifierTexture("BuffPower"), SkillDefs.SILVER_POINT, new BuffPower(), SkillDefs.TREE_DEFENSE, 30, 135);
		
	}/*
		//utility tree
		utilityTree.clear();
		RegisterPart(SkillManager.instance.getSkill("Touch"), 275, 75, SkillTrees.Utility, SkillPointTypes.BLUE);

		RegisterPart(SkillManager.instance.getSkill("Dig"), 275, 120, SkillTrees.Utility, SkillPointTypes.BLUE, SkillManager.instance.getSkill("Touch"));
		RegisterPart(SkillManager.instance.getSkill("WizardsAutumn"), 315, 120, SkillTrees.Utility, SkillPointTypes.BLUE, SkillManager.instance.getSkill("Dig"));
		RegisterPart(SkillManager.instance.getSkill("TargetNonSolid"), 230, 75, SkillTrees.Utility, SkillPointTypes.BLUE, SkillManager.instance.getSkill("Touch"));

		RegisterPart(SkillManager.instance.getSkill("PlaceBlock"), 185, 93, SkillTrees.Utility, SkillPointTypes.BLUE, SkillManager.instance.getSkill("Dig"));
		RegisterPart(SkillManager.instance.getSkill("FeatherTouch"), 230, 137, SkillTrees.Utility, SkillPointTypes.BLUE, SkillManager.instance.getSkill("Dig"));
		RegisterPart(SkillManager.instance.getSkill("MiningPower"), 185, 137, SkillTrees.Utility, SkillPointTypes.GREEN, SkillManager.instance.getSkill("FeatherTouch"));

		RegisterPart(SkillManager.instance.getSkill("Light"), 275, 165, SkillTrees.Utility, SkillPointTypes.BLUE, SkillManager.instance.getSkill("Dig"));
		RegisterPart(SkillManager.instance.getSkill("NightVision"), 185, 165, SkillTrees.Utility, SkillPointTypes.BLUE, SkillManager.instance.getSkill("Light"));

		RegisterPart(SkillManager.instance.getSkill("Binding"), 275, 210, SkillTrees.Utility, SkillPointTypes.BLUE, SkillManager.instance.getSkill("Light"));
		RegisterPart(SkillManager.instance.getSkill("Disarm"), 230, 210, SkillTrees.Utility, SkillPointTypes.BLUE, SkillManager.instance.getSkill("Binding"));
		RegisterPart(SkillManager.instance.getSkill("Charm"), 315, 235, SkillTrees.Utility, SkillPointTypes.BLUE, SkillManager.instance.getSkill("Binding"));
		RegisterPart(SkillManager.instance.getSkill("TrueSight"), 185, 210, SkillTrees.Utility, SkillPointTypes.BLUE, SkillManager.instance.getSkill("NightVision"));

		RegisterPart(SkillManager.instance.getSkill("Lunar"), 145, 210, SkillTrees.Utility, SkillPointTypes.RED, SkillManager.instance.getSkill("TrueSight"));

		RegisterPart(SkillManager.instance.getSkill("HarvestPlants"), 365, 120, SkillTrees.Utility, SkillPointTypes.GREEN, SkillManager.instance.getSkill("Binding"));
		RegisterPart(SkillManager.instance.getSkill("Plow"), 365, 165, SkillTrees.Utility, SkillPointTypes.BLUE, SkillManager.instance.getSkill("Binding"));
		RegisterPart(SkillManager.instance.getSkill("Plant"), 365, 210, SkillTrees.Utility, SkillPointTypes.BLUE, SkillManager.instance.getSkill("Binding"));
		RegisterPart(SkillManager.instance.getSkill("CreateWater"), 365, 255, SkillTrees.Utility, SkillPointTypes.GREEN, SkillManager.instance.getSkill("Binding"));
		RegisterPart(SkillManager.instance.getSkill("Drought"), 365, 300, SkillTrees.Utility, SkillPointTypes.GREEN, SkillManager.instance.getSkill("Binding"));

		RegisterPart(SkillManager.instance.getSkill("BanishRain"), 365, 345, SkillTrees.Utility, SkillPointTypes.GREEN, SkillManager.instance.getSkill("Drought"));
		RegisterPart(SkillManager.instance.getSkill("WaterBreathing"), 410, 345, SkillTrees.Utility, SkillPointTypes.BLUE, SkillManager.instance.getSkill("Drought"));

		RegisterPart(SkillManager.instance.getSkill("Grow"), 410, 210, SkillTrees.Utility, SkillPointTypes.RED, SkillManager.instance.getSkill("Drought"), SkillManager.instance.getSkill("CreateWater"), SkillManager.instance.getSkill("Plant"), SkillManager.instance.getSkill("Plow"), SkillManager.instance.getSkill("HarvestPlants"));

		RegisterPart(SkillManager.instance.getSkill("Chain"), 455, 210, SkillTrees.Utility, SkillPointTypes.RED, SkillManager.instance.getSkill("Grow"));

		RegisterPart(SkillManager.instance.getSkill("Rift"), 275, 255, SkillTrees.Utility, SkillPointTypes.GREEN, SkillManager.instance.getSkill("Binding"));
		RegisterPart(SkillManager.instance.getSkill("Invisibility"), 185, 255, SkillTrees.Utility, SkillPointTypes.GREEN, SkillManager.instance.getSkill("TrueSight"));

		RegisterPart(SkillManager.instance.getSkill("RandomTeleport"), 185, 300, SkillTrees.Utility, SkillPointTypes.BLUE, SkillManager.instance.getSkill("Invisibility"));
		RegisterPart(SkillManager.instance.getSkill("Attract"), 245, 300, SkillTrees.Utility, SkillPointTypes.GREEN, SkillManager.instance.getSkill("Rift"));
		RegisterPart(SkillManager.instance.getSkill("Telekinesis"), 305, 300, SkillTrees.Utility, SkillPointTypes.GREEN, SkillManager.instance.getSkill("Rift"));

		RegisterPart(SkillManager.instance.getSkill("Blink"), 185, 345, SkillTrees.Utility, SkillPointTypes.GREEN, SkillManager.instance.getSkill("RandomTeleport"));
		RegisterPart(SkillManager.instance.getSkill("Range"), 140, 345, SkillTrees.Utility, SkillPointTypes.RED, SkillManager.instance.getSkill("Blink"));
		RegisterPart(SkillManager.instance.getSkill("Channel"), 275, 345, SkillTrees.Utility, SkillPointTypes.GREEN, SkillManager.instance.getSkill("Attract"), SkillManager.instance.getSkill("Telekinesis"));

		RegisterPart(SkillManager.instance.getSkill("Radius"), 275, 390, SkillTrees.Utility, SkillPointTypes.RED, SkillManager.instance.getSkill("Channel"));
		RegisterPart(SkillManager.instance.getSkill("Transplace"), 185, 390, SkillTrees.Utility, SkillPointTypes.BLUE, SkillManager.instance.getSkill("Blink"));

		RegisterPart(SkillManager.instance.getSkill("Mark"), 155, 435, SkillTrees.Utility, SkillPointTypes.GREEN, SkillManager.instance.getSkill("Transplace"));
		RegisterPart(SkillManager.instance.getSkill("Recall"), 215, 435, SkillTrees.Utility, SkillPointTypes.GREEN, SkillManager.instance.getSkill("Transplace"));

		RegisterPart(SkillManager.instance.getSkill("DivineIntervention"), 172, 480, SkillTrees.Utility, SkillPointTypes.RED, SkillManager.instance.getSkill("Recall"), SkillManager.instance.getSkill("Mark"));
		RegisterPart(SkillManager.instance.getSkill("EnderIntervention"), 198, 480, SkillTrees.Utility, SkillPointTypes.RED, SkillManager.instance.getSkill("Recall"), SkillManager.instance.getSkill("Mark"));

		RegisterPart(SkillManager.instance.getSkill("Contingency_Death"), 198, 524, SkillTrees.Utility, SkillPointTypes.RED, SkillManager.instance.getSkill("EnderIntervention"));

		RegisterPart(SkillManager.instance.getSkill("Daylight"), 75, 45, SkillTrees.Utility, SkillPointTypes.SILVER);
		RegisterPart(SkillManager.instance.getSkill("Moonrise"), 75, 90, SkillTrees.Utility, SkillPointTypes.SILVER);
		RegisterPart(SkillManager.instance.getSkill("Prosperity"), 75, 135, SkillTrees.Utility, SkillPointTypes.SILVER);

		calculateHighestOverallTier();

		checkAllPartIDs(SkillManager.instance.getAllShapes());
		checkAllPartIDs(SkillManager.instance.getAllComponents());
		checkAllPartIDs(SkillManager.instance.getAllModifiers());
		checkAllPartIDs(SkillManager.instance.getAllTalents());

		AMCore.skillConfig.save();
	}
	 */
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
