package am2.defs;

import am2.ArsMagica2;
import am2.affinity.Affinity;
import am2.api.AffinityRegistry;
import am2.api.SkillPointRegistry;
import am2.api.SkillRegistry;
import am2.api.SkillTreeRegistry;
import am2.skill.Skill;
import am2.skill.SkillPoint;
import am2.skill.SkillTree;
import am2.utils.ResourceUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

public class SkillDefs {
	
	public static final SkillTree TREE_OFFENSE = new SkillTree("offense", new ResourceLocation(ArsMagica2.MODID, "textures/occulus/offense.png"), new ResourceLocation(ArsMagica2.MODID, "textures/icons/offense.png"));
	public static final SkillTree TREE_DEFENSE = new SkillTree("defense", new ResourceLocation(ArsMagica2.MODID, "textures/occulus/defense.png"), new ResourceLocation(ArsMagica2.MODID, "textures/icons/defense.png"));
	public static final SkillTree TREE_UTILITY = new SkillTree("utility", new ResourceLocation(ArsMagica2.MODID, "textures/occulus/utility.png"), new ResourceLocation(ArsMagica2.MODID, "textures/icons/utility.png"));
	public static final SkillTree TREE_AFFINITY = new SkillTree("affinity", new ResourceLocation(ArsMagica2.MODID, "textures/occulus/affinity.png"), new ResourceLocation(ArsMagica2.MODID, "textures/icons/affinity.png")).disableRender("affinity");
	public static final SkillTree TREE_TALENT = new SkillTree("talent", new ResourceLocation(ArsMagica2.MODID, "textures/occulus/talent.png"), new ResourceLocation(ArsMagica2.MODID, "textures/icons/talent.png"));
	
	public static final SkillPoint SILVER_POINT = new SkillPoint("Silver", TextFormatting.GRAY, 0x999999, -1, -1).disableRender();
	public static final SkillPoint SKILL_POINT_0 = new SkillPoint("Blue", TextFormatting.BLUE, 0x0000ff, 0, 1);
	public static final SkillPoint SKILL_POINT_1 = new SkillPoint("Green", TextFormatting.GREEN, 0x00ff00, 20, 2);
	public static final SkillPoint SKILL_POINT_2 = new SkillPoint("Red", TextFormatting.RED, 0xff0000, 30, 2);	
	public static final SkillPoint SKILL_POINT_3 = new SkillPoint("Yellow", TextFormatting.YELLOW, 0xffff00, 40, 3);	
	public static final SkillPoint SKILL_POINT_4 = new SkillPoint("Magenta", TextFormatting.LIGHT_PURPLE, 0xff00ff, 50, 3);	
	public static final SkillPoint SKILL_POINT_5 = new SkillPoint("Cyan", TextFormatting.AQUA, 0x00ffff, 60, 2);	
	
	public static final Skill MANA_REGEN_1 = new Skill("manaRegen1", ResourceUtils.getSkillIcon("ManaRegenI"), SKILL_POINT_0, 275, 75, TREE_TALENT);
	public static final Skill MANA_REGEN_2 = new Skill("manaRegen2", ResourceUtils.getSkillIcon("ManaRegenII"), SKILL_POINT_1, 275, 120, TREE_TALENT, "manaRegen1");
	public static final Skill MANA_REGEN_3 = new Skill("manaRegen3", ResourceUtils.getSkillIcon("ManaRegenIII"), SKILL_POINT_2, 275, 165, TREE_TALENT, "manaRegen2");
	public static final Skill MAGE_POSSE_1 = new Skill("magePosse1", ResourceUtils.getSkillIcon("MageBandI"), SKILL_POINT_1, 320, 120, TREE_TALENT, "manaRegen2");
	public static final Skill MAGE_POSSE_2 = new Skill("magePosse2", ResourceUtils.getSkillIcon("MageBandII"), SKILL_POINT_2, 320, 165, TREE_TALENT, "magePosse1");
	public static final Skill SPELL_MOTION = new Skill("spellMotion", ResourceUtils.getSkillIcon("SpellMotion"), SKILL_POINT_1, 230, 120, TREE_TALENT, "manaRegen2");
	public static final Skill AUGMENTED_CASTING = new Skill("augmentedCasting", ResourceUtils.getSkillIcon("AugmentedCasting"), SKILL_POINT_2, 230, 165, TREE_TALENT, "spellMotion");
	public static final Skill AFFINITY_GAINS = new Skill("affinityGains", ResourceUtils.getSkillIcon("AffinityGains"), SKILL_POINT_0, 365, 120, TREE_TALENT, "manaRegen1");
	public static final Skill EXTRA_SUMMONS = new Skill("extraSummons", ResourceUtils.getSkillIcon("ExtraSummon"), SKILL_POINT_2, 230, 210, TREE_TALENT, "augmentedCasting");
	
	
	public static final Affinity NONE = new Affinity("none", 0xFFFFFF);
	public static final Affinity ARCANE = new Affinity("arcane", 0xb935cd);
	public static final Affinity WATER = new Affinity("water", 0x0b5cef);
	public static final Affinity FIRE = new Affinity("fire", 0xef260b);
	public static final Affinity EARTH = new Affinity("earth", 0x61330b);
	public static final Affinity AIR = new Affinity("air", 0x777777);
	public static final Affinity LIGHTNING = new Affinity("lightning", 0xdece19);
	public static final Affinity ICE = new Affinity("ice", 0xd3e8fc);
	public static final Affinity NATURE = new Affinity("nature", 0x228718);
	public static final Affinity LIFE = new Affinity("life", 0x34e122);
	public static final Affinity ENDER = new Affinity("ender", 0x3f043d);

	
	public static void init() {
		SkillTreeRegistry.registerSkillTree(TREE_OFFENSE);
		SkillTreeRegistry.registerSkillTree(TREE_DEFENSE);
		SkillTreeRegistry.registerSkillTree(TREE_UTILITY);
		SkillTreeRegistry.registerSkillTree(TREE_AFFINITY);
		SkillTreeRegistry.registerSkillTree(TREE_TALENT);
		
		SkillPointRegistry.registerSkillPoint(-1, SILVER_POINT);
		SkillPointRegistry.registerSkillPoint(0, SKILL_POINT_0);
		SkillPointRegistry.registerSkillPoint(1, SKILL_POINT_1);
		SkillPointRegistry.registerSkillPoint(2, SKILL_POINT_2);
		
		SkillRegistry.registerSkill(MANA_REGEN_1);
		SkillRegistry.registerSkill(MANA_REGEN_2);
		SkillRegistry.registerSkill(MANA_REGEN_3);
		SkillRegistry.registerSkill(MAGE_POSSE_1);
		SkillRegistry.registerSkill(MAGE_POSSE_2);
		SkillRegistry.registerSkill(SPELL_MOTION);
		SkillRegistry.registerSkill(AUGMENTED_CASTING);
		SkillRegistry.registerSkill(AFFINITY_GAINS);
		SkillRegistry.registerSkill(EXTRA_SUMMONS);
		
		AffinityRegistry.registerAffinity(NONE);
		AffinityRegistry.registerAffinity(ARCANE);
		AffinityRegistry.registerAffinity(WATER);
		AffinityRegistry.registerAffinity(FIRE);
		AffinityRegistry.registerAffinity(EARTH);
		AffinityRegistry.registerAffinity(AIR);
		AffinityRegistry.registerAffinity(LIGHTNING);
		AffinityRegistry.registerAffinity(ICE);
		AffinityRegistry.registerAffinity(NATURE);
		AffinityRegistry.registerAffinity(LIFE);
		AffinityRegistry.registerAffinity(ENDER);
	}
	
}
