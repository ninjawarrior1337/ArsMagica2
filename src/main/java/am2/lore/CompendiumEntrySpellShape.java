package am2.lore;

import java.util.EnumSet;

import am2.api.skill.Skill;
import am2.spell.SpellModifiers;

public class CompendiumEntrySpellShape extends CompendiumEntrySpellPart {

	public CompendiumEntrySpellShape(String id, Skill shape, EnumSet<SpellModifiers> modifiedBy, String... related){
		super(CompendiumEntryTypes.instance.SPELL_SHAPE, id, shape, modifiedBy, related);
	}

}
