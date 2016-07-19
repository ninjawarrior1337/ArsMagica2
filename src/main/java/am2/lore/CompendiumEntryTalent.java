package am2.lore;

import am2.api.skill.Skill;

public class CompendiumEntryTalent extends CompendiumEntrySkill {

	public CompendiumEntryTalent(String id, Skill talent, String... related){
		super(CompendiumEntryTypes.instance.TALENT, id, talent, related);
	}

}
