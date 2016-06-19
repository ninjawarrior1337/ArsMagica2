package am2.defs;

import am2.api.SpellRegistry;
import am2.spell.IShape;
import am2.spell.component.Accelerate;
import am2.spell.component.Appropriation;
import am2.spell.component.AstralDistortion;
import am2.spell.component.Attract;
import am2.spell.component.BanishRain;
import am2.spell.component.Blind;
import am2.spell.component.ChronoAnchor;
import am2.spell.component.Dig;
import am2.spell.component.Drown;
import am2.spell.component.FireDamage;
import am2.spell.component.Forge;
import am2.spell.component.FrostDamage;
import am2.spell.component.Heal;
import am2.spell.component.Ignition;
import am2.spell.component.LightningDamage;
import am2.spell.component.MagicDamage;
import am2.spell.component.Mark;
import am2.spell.component.PhysicalDamage;
import am2.spell.component.Recall;
import am2.spell.component.Reflect;
import am2.spell.component.Regeneration;
import am2.spell.component.Repel;
import am2.spell.component.Rift;
import am2.spell.component.ScrambleSynapses;
import am2.spell.component.Shield;
import am2.spell.component.Silence;
import am2.spell.component.Slow;
import am2.spell.component.Slowfall;
import am2.spell.component.Storm;
import am2.spell.component.SwiftSwim;
import am2.spell.component.Telekinesis;
import am2.spell.component.Transplace;
import am2.spell.component.TrueSight;
import am2.spell.component.WaterBreathing;
import am2.spell.component.WateryGrave;
import am2.spell.component.WizardsAutumn;
import am2.spell.modifier.Bounce;
import am2.spell.modifier.Colour;
import am2.spell.modifier.Gravity;
import am2.spell.shape.AoE;
import am2.spell.shape.Contingency_Death;
import am2.spell.shape.Contingency_Fire;
import am2.spell.shape.MissingShape;
import am2.spell.shape.Projectile;
import am2.spell.shape.Self;
import net.minecraft.util.ResourceLocation;

public class SpellDefs {
	public static final IShape MISSING_SHAPE = new MissingShape();
	
	public static void init() {
		SpellRegistry.registerSpellShape("null", null, null, MISSING_SHAPE, null, 0, 0);
		//Offense Tree
		/*
		RegisterPart(SkillManager.instance.getSkill("Solar"), 210, 225, SkillTrees.Offense, SkillPointTypes.RED, SkillManager.instance.getSkill("Blind"));

		RegisterPart(SkillManager.instance.getSkill("Storm"), 255, 225, SkillTrees.Offense, SkillPointTypes.RED, SkillManager.instance.getSkill("LightningDamage"));
		RegisterPart(SkillManager.instance.getSkill("AstralDistortion"), 367, 215, SkillTrees.Offense, SkillPointTypes.GREEN, SkillManager.instance.getSkill("MagicDamage"), SkillManager.instance.getSkill("FrostDamage"));
		RegisterPart(SkillManager.instance.getSkill("Silence"), 345, 245, SkillTrees.Offense, SkillPointTypes.RED, SkillManager.instance.getSkill("AstralDistortion"));

		RegisterPart(SkillManager.instance.getSkill("Fling"), 390, 245, SkillTrees.Offense, SkillPointTypes.GREEN, SkillManager.instance.getSkill("Knockback"));
		RegisterPart(SkillManager.instance.getSkill("VelocityAdded"), 390, 290, SkillTrees.Offense, SkillPointTypes.RED, SkillManager.instance.getSkill("Fling"));
		RegisterPart(SkillManager.instance.getSkill("WateryGrave"), 435, 245, SkillTrees.Offense, SkillPointTypes.GREEN, SkillManager.instance.getSkill("Drown"));

		RegisterPart(SkillManager.instance.getSkill("Piercing"), 323, 215, SkillTrees.Offense, SkillPointTypes.RED, SkillManager.instance.getSkill("Freeze"));

		RegisterPart(SkillManager.instance.getSkill("Beam"), 300, 270, SkillTrees.Offense, SkillPointTypes.RED, SkillManager.instance.getSkill("AoE"));
		RegisterPart(SkillManager.instance.getSkill("Damage"), 300, 315, SkillTrees.Offense, SkillPointTypes.RED, SkillManager.instance.getSkill("Beam"));
		RegisterPart(SkillManager.instance.getSkill("Fury"), 255, 315, SkillTrees.Offense, SkillPointTypes.RED, SkillManager.instance.getSkill("Beam"), SkillManager.instance.getSkill("Storm"));
		RegisterPart(SkillManager.instance.getSkill("Wave"), 367, 315, SkillTrees.Offense, SkillPointTypes.RED, SkillManager.instance.getSkill("Beam"), SkillManager.instance.getSkill("Fling"));

		RegisterPart(SkillManager.instance.getSkill("Blizzard"), 75, 45, SkillTrees.Offense, SkillPointTypes.SILVER);
		RegisterPart(SkillManager.instance.getSkill("FallingStar"), 75, 90, SkillTrees.Offense, SkillPointTypes.SILVER);
		RegisterPart(SkillManager.instance.getSkill("FireRain"), 75, 135, SkillTrees.Offense, SkillPointTypes.SILVER);
		RegisterPart(SkillManager.instance.getSkill("Dismembering"), 75, 180, SkillTrees.Offense, SkillPointTypes.SILVER);
		 */
		
		SpellRegistry.registerSpellModifier("colour", getModifierTexture("Colour"), SkillDefs.SKILL_POINT_0, new Colour(), SkillDefs.TREE_TALENT, 230, 75);

		
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
		//TODO SpellRegistry.registerSpellComponent("freeze", getComponentTexture("Freeze"), SkillDefs.SKILL_POINT_1, new Freeze(), SkillDefs.TREE_OFFENSE, 345, 180, "frost_damage");
		//TODO SpellRegistry.registerSpellComponent("knockback", getComponentTexture("Knockback"), SkillDefs.SKILL_POINT_1, new Knockback(), SkillDefs.TREE_OFFENSE, 345, 180, "magic_damage");
		
		
		SpellRegistry.registerSpellShape("contingency_fire", getShapeTexture("Contingency_Fire"), SkillDefs.SKILL_POINT_1, new Contingency_Fire(), SkillDefs.TREE_OFFENSE, 165, 190, "ignition");
		
		SpellRegistry.registerSpellShape("self", getShapeTexture("Self"), SkillDefs.SKILL_POINT_0, new Self(), SkillDefs.TREE_DEFENSE, 0, 0);
		SpellRegistry.registerSpellShape("contingency_death", getShapeTexture("Contingency_Death"), SkillDefs.SKILL_POINT_0, new Contingency_Death(), SkillDefs.TREE_DEFENSE, 0, 0);
		
		SpellRegistry.registerSpellComponent("accelerate", getComponentTexture("Accelerate"), SkillDefs.SKILL_POINT_0, new Accelerate(), SkillDefs.TREE_UTILITY, 4, 4);
		SpellRegistry.registerSpellComponent("appropriation", getComponentTexture("Appropriation"), SkillDefs.SKILL_POINT_0, new Appropriation(), SkillDefs.TREE_UTILITY, 4, 4);
		SpellRegistry.registerSpellComponent("astral_distortion", getComponentTexture("AstralDistortion"), SkillDefs.SKILL_POINT_0, new AstralDistortion(), SkillDefs.TREE_UTILITY, 4, 4);
		SpellRegistry.registerSpellComponent("attract", getComponentTexture("Attract"), SkillDefs.SKILL_POINT_0, new Attract(), SkillDefs.TREE_UTILITY, 4, 4);
		SpellRegistry.registerSpellComponent("banish_rain", getComponentTexture("BanishRain"), SkillDefs.SKILL_POINT_0, new BanishRain(), SkillDefs.TREE_UTILITY, 4, 4);
		SpellRegistry.registerSpellComponent("blind", getComponentTexture("Blind"), SkillDefs.SKILL_POINT_0, new Blind(), SkillDefs.TREE_UTILITY, 4, 4);
		SpellRegistry.registerSpellComponent("chrono_anchor", getComponentTexture("ChronoAnchor"), SkillDefs.SKILL_POINT_0, new ChronoAnchor(), SkillDefs.TREE_UTILITY, 4, 4);
		SpellRegistry.registerSpellComponent("dig", getComponentTexture("Dig"), SkillDefs.SKILL_POINT_0, new Dig(), SkillDefs.TREE_UTILITY, 0, 1);
		
		
		SpellRegistry.registerSpellComponent("heal", getComponentTexture("Heal"), SkillDefs.SKILL_POINT_0, new Heal(), SkillDefs.TREE_UTILITY, 4, 4);
		
		SpellRegistry.registerSpellComponent("mark", getComponentTexture("Mark"), SkillDefs.SKILL_POINT_1, new Mark(), SkillDefs.TREE_UTILITY, 0, 2);
		
		
		SpellRegistry.registerSpellComponent("recall", getComponentTexture("Recall"), SkillDefs.SKILL_POINT_1, new Recall(), SkillDefs.TREE_UTILITY, 0, 3);
		SpellRegistry.registerSpellComponent("reflect", getComponentTexture("Reflect"), SkillDefs.SKILL_POINT_1, new Reflect(), SkillDefs.TREE_UTILITY, 0, 3);
		SpellRegistry.registerSpellComponent("regeneration", getComponentTexture("Regeneration"), SkillDefs.SKILL_POINT_1, new Regeneration(), SkillDefs.TREE_UTILITY, 0, 3);
		SpellRegistry.registerSpellComponent("repel", getComponentTexture("Repel"), SkillDefs.SKILL_POINT_1, new Repel(), SkillDefs.TREE_UTILITY, 0, 3);
		SpellRegistry.registerSpellComponent("rift", getComponentTexture("Rift"), SkillDefs.SKILL_POINT_1, new Rift(), SkillDefs.TREE_UTILITY, 0, 3);
		SpellRegistry.registerSpellComponent("scramble_synapses", getComponentTexture("ScrambleSynapses"), SkillDefs.SKILL_POINT_1, new ScrambleSynapses(), SkillDefs.TREE_UTILITY, 0, 3);
		SpellRegistry.registerSpellComponent("shield", getComponentTexture("Shield"), SkillDefs.SKILL_POINT_1, new Shield(), SkillDefs.TREE_UTILITY, 0, 3);
		SpellRegistry.registerSpellComponent("silence", getComponentTexture("Silence"), SkillDefs.SKILL_POINT_1, new Silence(), SkillDefs.TREE_UTILITY, 0, 3);
		SpellRegistry.registerSpellComponent("slow", getComponentTexture("Slow"), SkillDefs.SKILL_POINT_1, new Slow(), SkillDefs.TREE_UTILITY, 0, 3);
		SpellRegistry.registerSpellComponent("slowfall", getComponentTexture("Slowfall"), SkillDefs.SKILL_POINT_1, new Slowfall(), SkillDefs.TREE_UTILITY, 0, 3);
		SpellRegistry.registerSpellComponent("storm", getComponentTexture("Storm"), SkillDefs.SKILL_POINT_1, new Storm(), SkillDefs.TREE_UTILITY, 0, 3);
		//TODO SpellRegistry.registerSpellComponent("summon", getComponentTexture("Summon"), SkillDefs.SKILL_POINT_1, new Summon(), SkillDefs.TREE_UTILITY, 0, 3);
		SpellRegistry.registerSpellComponent("swiftswim", getComponentTexture("SwiftSwim"), SkillDefs.SKILL_POINT_1, new SwiftSwim(), SkillDefs.TREE_UTILITY, 0, 3);
		SpellRegistry.registerSpellComponent("telekinesis", getComponentTexture("Telekinesis"), SkillDefs.SKILL_POINT_1, new Telekinesis(), SkillDefs.TREE_UTILITY, 0, 3);
		SpellRegistry.registerSpellComponent("transplace", getComponentTexture("Transplace"), SkillDefs.SKILL_POINT_1, new Transplace(), SkillDefs.TREE_UTILITY, 0, 3);
		SpellRegistry.registerSpellComponent("true_sight", getComponentTexture("TrueSight"), SkillDefs.SKILL_POINT_1, new TrueSight(), SkillDefs.TREE_UTILITY, 0, 3);
		SpellRegistry.registerSpellComponent("water_breathing", getComponentTexture("WaterBreathing"), SkillDefs.SKILL_POINT_1, new WaterBreathing(), SkillDefs.TREE_UTILITY, 0, 3);
		SpellRegistry.registerSpellComponent("watery_grave", getComponentTexture("WateryGrave"), SkillDefs.SKILL_POINT_1, new WateryGrave(), SkillDefs.TREE_UTILITY, 0, 3);
		SpellRegistry.registerSpellComponent("wizards_autumn", getComponentTexture("WizardsAutumn"), SkillDefs.SKILL_POINT_1, new WizardsAutumn(), SkillDefs.TREE_UTILITY, 0, 3);
		
	}
	/*
	 * 	public void init(){

		//offense tree
		offenseTree.clear();
		RegisterPart(SkillManager.instance.getSkill("Projectile"), 300, 45, SkillTrees.Offense, SkillPointTypes.BLUE);
		RegisterPart(SkillManager.instance.getSkill("PhysicalDamage"), 300, 90, SkillTrees.Offense, SkillPointTypes.BLUE, SkillManager.instance.getSkill("Projectile"));
		RegisterPart(SkillManager.instance.getSkill("Gravity"), 255, 70, SkillTrees.Offense, SkillPointTypes.BLUE, SkillManager.instance.getSkill("Projectile"));
		RegisterPart(SkillManager.instance.getSkill("Bounce"), 345, 70, SkillTrees.Offense, SkillPointTypes.BLUE, SkillManager.instance.getSkill("Projectile"));

		RegisterPart(SkillManager.instance.getSkill("FireDamage"), 210, 135, SkillTrees.Offense, SkillPointTypes.BLUE, SkillManager.instance.getSkill("PhysicalDamage"));
		RegisterPart(SkillManager.instance.getSkill("LightningDamage"), 255, 135, SkillTrees.Offense, SkillPointTypes.BLUE, SkillManager.instance.getSkill("FireDamage"));
		RegisterPart(SkillManager.instance.getSkill("Ignition"), 165, 135, SkillTrees.Offense, SkillPointTypes.GREEN, SkillManager.instance.getSkill("FireDamage"));
		RegisterPart(SkillManager.instance.getSkill("Forge"), 120, 135, SkillTrees.Offense, SkillPointTypes.GREEN, SkillManager.instance.getSkill("Ignition"));

		RegisterPart(SkillManager.instance.getSkill("Contingency_Fire"), 165, 180, SkillTrees.Offense, SkillPointTypes.GREEN, SkillManager.instance.getSkill("Ignition"));

		RegisterPart(SkillManager.instance.getSkill("MagicDamage"), 390, 135, SkillTrees.Offense, SkillPointTypes.BLUE, SkillManager.instance.getSkill("PhysicalDamage"));
		RegisterPart(SkillManager.instance.getSkill("FrostDamage"), 345, 135, SkillTrees.Offense, SkillPointTypes.BLUE, SkillManager.instance.getSkill("MagicDamage"));

		RegisterPart(SkillManager.instance.getSkill("Drown"), 435, 135, SkillTrees.Offense, SkillPointTypes.BLUE, SkillManager.instance.getSkill("MagicDamage"));

		RegisterPart(SkillManager.instance.getSkill("Blind"), 233, 180, SkillTrees.Offense, SkillPointTypes.GREEN, SkillManager.instance.getSkill("FireDamage"), SkillManager.instance.getSkill("LightningDamage"));
		RegisterPart(SkillManager.instance.getSkill("AoE"), 300, 180, SkillTrees.Offense, SkillPointTypes.GREEN, SkillManager.instance.getSkill("PhysicalDamage"), SkillManager.instance.getSkill("FrostDamage"), SkillManager.instance.getSkill("FireDamage"), SkillManager.instance.getSkill("LightningDamage"), SkillManager.instance.getSkill("MagicDamage"));
		RegisterPart(SkillManager.instance.getSkill("Freeze"), 345, 180, SkillTrees.Offense, SkillPointTypes.GREEN, SkillManager.instance.getSkill("FrostDamage"));
		RegisterPart(SkillManager.instance.getSkill("Knockback"), 390, 180, SkillTrees.Offense, SkillPointTypes.GREEN, SkillManager.instance.getSkill("MagicDamage"));

		RegisterPart(SkillManager.instance.getSkill("Solar"), 210, 225, SkillTrees.Offense, SkillPointTypes.RED, SkillManager.instance.getSkill("Blind"));

		RegisterPart(SkillManager.instance.getSkill("Storm"), 255, 225, SkillTrees.Offense, SkillPointTypes.RED, SkillManager.instance.getSkill("LightningDamage"));
		RegisterPart(SkillManager.instance.getSkill("AstralDistortion"), 367, 215, SkillTrees.Offense, SkillPointTypes.GREEN, SkillManager.instance.getSkill("MagicDamage"), SkillManager.instance.getSkill("FrostDamage"));
		RegisterPart(SkillManager.instance.getSkill("Silence"), 345, 245, SkillTrees.Offense, SkillPointTypes.RED, SkillManager.instance.getSkill("AstralDistortion"));

		RegisterPart(SkillManager.instance.getSkill("Fling"), 390, 245, SkillTrees.Offense, SkillPointTypes.GREEN, SkillManager.instance.getSkill("Knockback"));
		RegisterPart(SkillManager.instance.getSkill("VelocityAdded"), 390, 290, SkillTrees.Offense, SkillPointTypes.RED, SkillManager.instance.getSkill("Fling"));
		RegisterPart(SkillManager.instance.getSkill("WateryGrave"), 435, 245, SkillTrees.Offense, SkillPointTypes.GREEN, SkillManager.instance.getSkill("Drown"));

		RegisterPart(SkillManager.instance.getSkill("Piercing"), 323, 215, SkillTrees.Offense, SkillPointTypes.RED, SkillManager.instance.getSkill("Freeze"));

		RegisterPart(SkillManager.instance.getSkill("Beam"), 300, 270, SkillTrees.Offense, SkillPointTypes.RED, SkillManager.instance.getSkill("AoE"));
		RegisterPart(SkillManager.instance.getSkill("Damage"), 300, 315, SkillTrees.Offense, SkillPointTypes.RED, SkillManager.instance.getSkill("Beam"));
		RegisterPart(SkillManager.instance.getSkill("Fury"), 255, 315, SkillTrees.Offense, SkillPointTypes.RED, SkillManager.instance.getSkill("Beam"), SkillManager.instance.getSkill("Storm"));
		RegisterPart(SkillManager.instance.getSkill("Wave"), 367, 315, SkillTrees.Offense, SkillPointTypes.RED, SkillManager.instance.getSkill("Beam"), SkillManager.instance.getSkill("Fling"));

		RegisterPart(SkillManager.instance.getSkill("Blizzard"), 75, 45, SkillTrees.Offense, SkillPointTypes.SILVER);
		RegisterPart(SkillManager.instance.getSkill("FallingStar"), 75, 90, SkillTrees.Offense, SkillPointTypes.SILVER);
		RegisterPart(SkillManager.instance.getSkill("FireRain"), 75, 135, SkillTrees.Offense, SkillPointTypes.SILVER);
		RegisterPart(SkillManager.instance.getSkill("Dismembering"), 75, 180, SkillTrees.Offense, SkillPointTypes.SILVER);

		//defense tree
		defenseTree.clear();
		RegisterPart(SkillManager.instance.getSkill("Self"), 267, 45, SkillTrees.Defense, SkillPointTypes.BLUE);

		RegisterPart(SkillManager.instance.getSkill("Leap"), 222, 90, SkillTrees.Defense, SkillPointTypes.BLUE, SkillManager.instance.getSkill("Self"));
		RegisterPart(SkillManager.instance.getSkill("Regeneration"), 357, 90, SkillTrees.Defense, SkillPointTypes.BLUE, SkillManager.instance.getSkill("Self"));

		RegisterPart(SkillManager.instance.getSkill("Shrink"), 402, 90, SkillTrees.Defense, SkillPointTypes.BLUE, SkillManager.instance.getSkill("Regeneration"));
		RegisterPart(SkillManager.instance.getSkill("Slowfall"), 222, 135, SkillTrees.Defense, SkillPointTypes.BLUE, SkillManager.instance.getSkill("Leap"));
		RegisterPart(SkillManager.instance.getSkill("Heal"), 357, 135, SkillTrees.Defense, SkillPointTypes.BLUE, SkillManager.instance.getSkill("Regeneration"));
		RegisterPart(SkillManager.instance.getSkill("LifeTap"), 312, 135, SkillTrees.Defense, SkillPointTypes.GREEN, SkillManager.instance.getSkill("Heal"));
		RegisterPart(SkillManager.instance.getSkill("Healing"), 402, 135, SkillTrees.Defense, SkillPointTypes.RED, SkillManager.instance.getSkill("Heal"));

		RegisterPart(SkillManager.instance.getSkill("Summon"), 267, 135, SkillTrees.Defense, SkillPointTypes.GREEN, SkillManager.instance.getSkill("LifeTap"));
		RegisterPart(SkillManager.instance.getSkill("Contingency_Damage"), 447, 180, SkillTrees.Defense, SkillPointTypes.GREEN, SkillManager.instance.getSkill("Healing"));

		RegisterPart(SkillManager.instance.getSkill("Haste"), 177, 155, SkillTrees.Defense, SkillPointTypes.BLUE, SkillManager.instance.getSkill("Slowfall"));
		RegisterPart(SkillManager.instance.getSkill("Slow"), 132, 155, SkillTrees.Defense, SkillPointTypes.BLUE, SkillManager.instance.getSkill("Slowfall"));

		RegisterPart(SkillManager.instance.getSkill("GravityWell"), 222, 180, SkillTrees.Defense, SkillPointTypes.GREEN, SkillManager.instance.getSkill("Slowfall"));
		RegisterPart(SkillManager.instance.getSkill("LifeDrain"), 312, 180, SkillTrees.Defense, SkillPointTypes.GREEN, SkillManager.instance.getSkill("LifeTap"));
		RegisterPart(SkillManager.instance.getSkill("Dispel"), 357, 180, SkillTrees.Defense, SkillPointTypes.GREEN, SkillManager.instance.getSkill("Heal"));

		RegisterPart(SkillManager.instance.getSkill("Contingency_Fall"), 267, 180, SkillTrees.Defense, SkillPointTypes.GREEN, SkillManager.instance.getSkill("GravityWell"));

		RegisterPart(SkillManager.instance.getSkill("SwiftSwim"), 177, 200, SkillTrees.Defense, SkillPointTypes.BLUE, SkillManager.instance.getSkill("Haste"));
		RegisterPart(SkillManager.instance.getSkill("Repel"), 132, 200, SkillTrees.Defense, SkillPointTypes.GREEN, SkillManager.instance.getSkill("Slow"));

		RegisterPart(SkillManager.instance.getSkill("Levitate"), 222, 225, SkillTrees.Defense, SkillPointTypes.GREEN, SkillManager.instance.getSkill("GravityWell"));
		RegisterPart(SkillManager.instance.getSkill("ManaDrain"), 312, 225, SkillTrees.Defense, SkillPointTypes.GREEN, SkillManager.instance.getSkill("LifeDrain"));
		RegisterPart(SkillManager.instance.getSkill("Zone"), 357, 225, SkillTrees.Defense, SkillPointTypes.RED, SkillManager.instance.getSkill("Dispel"));

		RegisterPart(SkillManager.instance.getSkill("Wall"), 87, 200, SkillTrees.Defense, SkillPointTypes.GREEN, SkillManager.instance.getSkill("Repel"));
		RegisterPart(SkillManager.instance.getSkill("Accelerate"), 177, 245, SkillTrees.Defense, SkillPointTypes.GREEN, SkillManager.instance.getSkill("SwiftSwim"));
		RegisterPart(SkillManager.instance.getSkill("Entangle"), 132, 245, SkillTrees.Defense, SkillPointTypes.GREEN, SkillManager.instance.getSkill("Repel"));
		RegisterPart(SkillManager.instance.getSkill("Appropriation"), 87, 245, SkillTrees.Defense, SkillPointTypes.RED, SkillManager.instance.getSkill("Entangle"));

		RegisterPart(SkillManager.instance.getSkill("Flight"), 222, 270, SkillTrees.Defense, SkillPointTypes.RED, SkillManager.instance.getSkill("Levitate"));
		RegisterPart(SkillManager.instance.getSkill("Shield"), 357, 270, SkillTrees.Defense, SkillPointTypes.BLUE, SkillManager.instance.getSkill("Zone"));

		RegisterPart(SkillManager.instance.getSkill("Contingency_Health"), 402, 270, SkillTrees.Defense, SkillPointTypes.RED, SkillManager.instance.getSkill("Shield"));

		RegisterPart(SkillManager.instance.getSkill("Rune"), 157, 315, SkillTrees.Defense, SkillPointTypes.GREEN, SkillManager.instance.getSkill("Accelerate"), SkillManager.instance.getSkill("Entangle"));

		RegisterPart(SkillManager.instance.getSkill("RuneProcs"), 157, 360, SkillTrees.Defense, SkillPointTypes.GREEN, SkillManager.instance.getSkill("Rune"));

		RegisterPart(SkillManager.instance.getSkill("Speed"), 202, 315, SkillTrees.Defense, SkillPointTypes.RED, SkillManager.instance.getSkill("Accelerate"), SkillManager.instance.getSkill("Flight"));

		RegisterPart(SkillManager.instance.getSkill("Reflect"), 357, 315, SkillTrees.Defense, SkillPointTypes.RED, SkillManager.instance.getSkill("Shield"));
		RegisterPart(SkillManager.instance.getSkill("ChronoAnchor"), 312, 315, SkillTrees.Defense, SkillPointTypes.RED, SkillManager.instance.getSkill("Reflect"));

		RegisterPart(SkillManager.instance.getSkill("Duration"), 312, 360, SkillTrees.Defense, SkillPointTypes.RED, SkillManager.instance.getSkill("ChronoAnchor"));

		RegisterPart(SkillManager.instance.getSkill("ManaLink"), 30, 45, SkillTrees.Defense, SkillPointTypes.SILVER);
		RegisterPart(SkillManager.instance.getSkill("ManaShield"), 30, 90, SkillTrees.Defense, SkillPointTypes.SILVER);
		RegisterPart(SkillManager.instance.getSkill("BuffPower"), 30, 135, SkillTrees.Defense, SkillPointTypes.SILVER);

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

		//talent tree
		RegisterPart(SkillManager.instance.getSkill("ManaRegenI"), 275, 75, SkillTrees.Talents, SkillPointTypes.BLUE);
		RegisterPart(SkillManager.instance.getSkill("Colour"), 230, 75, SkillTrees.Talents, SkillPointTypes.BLUE);

		RegisterPart(SkillManager.instance.getSkill("AffinityGains"), 365, 120, SkillTrees.Talents, SkillPointTypes.BLUE, SkillManager.instance.getSkill("ManaRegenI"));
		RegisterPart(SkillManager.instance.getSkill("ManaRegenII"), 275, 120, SkillTrees.Talents, SkillPointTypes.GREEN, SkillManager.instance.getSkill("ManaRegenI"));
		RegisterPart(SkillManager.instance.getSkill("SpellMotion"), 230, 120, SkillTrees.Talents, SkillPointTypes.GREEN, SkillManager.instance.getSkill("ManaRegenII"));
		RegisterPart(SkillManager.instance.getSkill("AugmentedCasting"), 230, 165, SkillTrees.Talents, SkillPointTypes.RED, SkillManager.instance.getSkill("SpellMotion"));
		RegisterPart(SkillManager.instance.getSkill("ManaRegenIII"), 275, 165, SkillTrees.Talents, SkillPointTypes.RED, SkillManager.instance.getSkill("ManaRegenII"));

		RegisterPart(SkillManager.instance.getSkill("ExtraSummon"), 230, 210, SkillTrees.Talents, SkillPointTypes.RED, SkillManager.instance.getSkill("AugmentedCasting"));

		RegisterPart(SkillManager.instance.getSkill("MageBandI"), 320, 120, SkillTrees.Talents, SkillPointTypes.GREEN, SkillManager.instance.getSkill("ManaRegenI"));
		RegisterPart(SkillManager.instance.getSkill("MageBandII"), 320, 165, SkillTrees.Talents, SkillPointTypes.RED, SkillManager.instance.getSkill("MageBandI"));

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
