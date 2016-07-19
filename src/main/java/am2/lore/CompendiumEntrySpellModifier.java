package am2.lore;

import java.util.EnumSet;

import am2.api.skill.Skill;
import am2.spell.SpellModifiers;

public class CompendiumEntrySpellModifier extends CompendiumEntrySpellPart{

	public CompendiumEntrySpellModifier(String id, Skill mod, EnumSet<SpellModifiers> modifies, String... related){
		super(CompendiumEntryTypes.instance.SPELL_MODIFIER, id, mod, modifies, related);
	}
}
