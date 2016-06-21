package am2.lore;

import java.util.EnumSet;
import java.util.Set;

import am2.skill.Skill;
import am2.spell.SpellModifiers;

public class CompendiumEntrySpellShape extends CompendiumEntrySpellPart {

	private Set<SpellModifiers> modifiedBy;

	public CompendiumEntrySpellShape(String id, Skill shape, EnumSet<SpellModifiers> modifiedBy, String... related){
		super(CompendiumEntryTypes.instance.SPELL_SHAPE, id, shape, modifiedBy, related);
		this.modifiedBy = modifiedBy;
	}

	public Set<SpellModifiers> getModifiedBy(){
		return modifiedBy;
	}

}
