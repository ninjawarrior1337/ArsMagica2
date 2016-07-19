package am2.defs;

import am2.ArsMagica2;
import am2.api.ArsMagicaAPI;
import am2.api.SkillPointRegistry;
import am2.api.SkillRegistry;
import am2.api.SkillTreeRegistry;
import am2.api.skill.Skill;
import am2.api.skill.SkillPoint;
import am2.api.skill.SkillTree;
import am2.utils.ResourceUtils;
import net.minecraft.util.ResourceLocation;

public class SkillDefs {
	
	public static final SkillTree TREE_OFFENSE = new SkillTree("offense", new ResourceLocation(ArsMagica2.MODID, "textures/occulus/offense.png"), new ResourceLocation(ArsMagica2.MODID, "textures/icons/offense.png"));
	public static final SkillTree TREE_DEFENSE = new SkillTree("defense", new ResourceLocation(ArsMagica2.MODID, "textures/occulus/defense.png"), new ResourceLocation(ArsMagica2.MODID, "textures/icons/defense.png"));
	public static final SkillTree TREE_UTILITY = new SkillTree("utility", new ResourceLocation(ArsMagica2.MODID, "textures/occulus/utility.png"), new ResourceLocation(ArsMagica2.MODID, "textures/icons/utility.png"));
	public static final SkillTree TREE_AFFINITY = new SkillTree("affinity", new ResourceLocation(ArsMagica2.MODID, "textures/occulus/affinity.png"), new ResourceLocation(ArsMagica2.MODID, "textures/icons/affinity.png")).disableRender("affinity");
	public static final SkillTree TREE_TALENT = new SkillTree("talent", new ResourceLocation(ArsMagica2.MODID, "textures/occulus/talent.png"), new ResourceLocation(ArsMagica2.MODID, "textures/icons/talent.png"));
		
	public static final Skill MANA_REGEN_1 = new Skill("manaRegen1", ResourceUtils.getSkillIcon("ManaRegenI"), SkillPoint.SKILL_POINT_1, 275, 75, TREE_TALENT);
	public static final Skill MANA_REGEN_2 = new Skill("manaRegen2", ResourceUtils.getSkillIcon("ManaRegenII"), SkillPoint.SKILL_POINT_2, 275, 120, TREE_TALENT, "arsmagica2:manaRegen1");
	public static final Skill MANA_REGEN_3 = new Skill("manaRegen3", ResourceUtils.getSkillIcon("ManaRegenIII"), SkillPoint.SKILL_POINT_3, 275, 165, TREE_TALENT, "arsmagica2:manaRegen2");
	public static final Skill MAGE_POSSE_1 = new Skill("magePosse1", ResourceUtils.getSkillIcon("MageBandI"), SkillPoint.SKILL_POINT_2, 320, 120, TREE_TALENT, "arsmagica2:manaRegen2");
	public static final Skill MAGE_POSSE_2 = new Skill("magePosse2", ResourceUtils.getSkillIcon("MageBandII"), SkillPoint.SKILL_POINT_3, 320, 165, TREE_TALENT, "arsmagica2:magePosse1");
	public static final Skill SPELL_MOTION = new Skill("spellMotion", ResourceUtils.getSkillIcon("SpellMotion"), SkillPoint.SKILL_POINT_2, 230, 120, TREE_TALENT, "arsmagica2:manaRegen2");
	public static final Skill AUGMENTED_CASTING = new Skill("augmentedCasting", ResourceUtils.getSkillIcon("AugmentedCasting"), SkillPoint.SKILL_POINT_3, 230, 165, TREE_TALENT, "arsmagica2:spellMotion");
	public static final Skill AFFINITY_GAINS = new Skill("affinityGains", ResourceUtils.getSkillIcon("AffinityGains"), SkillPoint.SKILL_POINT_1, 365, 120, TREE_TALENT, "arsmagica2:manaRegen1");
	public static final Skill EXTRA_SUMMONS = new Skill("extraSummons", ResourceUtils.getSkillIcon("ExtraSummon"), SkillPoint.SKILL_POINT_3, 230, 210, TREE_TALENT, "arsmagica2:augmentedCasting");
	
	public static void init() {
		SkillTreeRegistry.registerSkillTree(TREE_OFFENSE);
		SkillTreeRegistry.registerSkillTree(TREE_DEFENSE);
		SkillTreeRegistry.registerSkillTree(TREE_UTILITY);
		SkillTreeRegistry.registerSkillTree(TREE_AFFINITY);
		SkillTreeRegistry.registerSkillTree(TREE_TALENT);
		
		SkillPointRegistry.registerSkillPoint(-1, SkillPoint.SILVER_POINT);
		SkillPointRegistry.registerSkillPoint(0, SkillPoint.SKILL_POINT_1);
		SkillPointRegistry.registerSkillPoint(1, SkillPoint.SKILL_POINT_2);
		SkillPointRegistry.registerSkillPoint(2, SkillPoint.SKILL_POINT_3);
		if (ArsMagicaAPI.hasTier4() || ArsMagicaAPI.hasTier5() || ArsMagicaAPI.hasTier6()) SkillPointRegistry.registerSkillPoint(3, SkillPoint.SKILL_POINT_4);
		if (ArsMagicaAPI.hasTier5() || ArsMagicaAPI.hasTier6()) SkillPointRegistry.registerSkillPoint(4, SkillPoint.SKILL_POINT_5);
		if (ArsMagicaAPI.hasTier6()) SkillPointRegistry.registerSkillPoint(5, SkillPoint.SKILL_POINT_6);
		
		SkillRegistry.registerSkill(MANA_REGEN_1);
		SkillRegistry.registerSkill(MANA_REGEN_2);
		SkillRegistry.registerSkill(MANA_REGEN_3);
		SkillRegistry.registerSkill(MAGE_POSSE_1);
		SkillRegistry.registerSkill(MAGE_POSSE_2);
		SkillRegistry.registerSkill(SPELL_MOTION);
		SkillRegistry.registerSkill(AUGMENTED_CASTING);
		SkillRegistry.registerSkill(AFFINITY_GAINS);
		SkillRegistry.registerSkill(EXTRA_SUMMONS);
	}
	
}
