package am2.lore;

import java.util.EnumSet;
import java.util.Set;

import am2.skill.Skill;
import am2.spell.SpellModifiers;

public class CompendiumEntrySpellComponent extends CompendiumEntrySpellPart{
	private Set<SpellModifiers> modifiedBy;

	public CompendiumEntrySpellComponent(String id, Skill skill, EnumSet<SpellModifiers> modifiedBy, String... related){
		super(CompendiumEntryTypes.instance.SPELL_COMPONENT, id, skill, modifiedBy, related);
		this.modifiedBy = modifiedBy;
	}

	public Set<SpellModifiers> getModifiedBy(){
		return modifiedBy;
	}


}
