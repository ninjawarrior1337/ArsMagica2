package am2.lore;

import java.util.EnumSet;

import am2.skill.Skill;
import am2.spell.SpellModifiers;

public class CompendiumEntrySpellComponent extends CompendiumEntrySpellPart{

	public CompendiumEntrySpellComponent(String id, Skill skill, EnumSet<SpellModifiers> modifiedBy, String... related){
		super(CompendiumEntryTypes.instance.SPELL_COMPONENT, id, skill, modifiedBy, related);
	}

}
