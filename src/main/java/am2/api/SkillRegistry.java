package am2.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import am2.ArsMagica2;
import am2.lore.ArcaneCompendium;
import am2.skill.Skill;
import am2.skill.SkillPoint;
import am2.skill.SkillTree;
import net.minecraft.util.ResourceLocation;

public class SkillRegistry {
	private static final HashMap<String, Skill> skillMap = new HashMap<String, Skill>();
	
	public static void registerSkill (String ID, ResourceLocation icon, SkillPoint tier, int posX, int posY, SkillTree tree, String... parents) {
		registerSkill(new Skill(ID, icon, tier, posX, posY, tree, parents));
	}
	
	public static void registerSkill (boolean createEntry, String ID, ResourceLocation icon, SkillPoint tier, int posX, int posY, SkillTree tree, String... parents) {
		registerSkill(createEntry, new Skill(ID, icon, tier, posX, posY, tree, parents));
	}
	
	public static void registerSkill (boolean createEntry, Skill skill) {
		if (skillMap.containsKey(skill.getID().toLowerCase()))
			ArsMagica2.LOGGER.warn("Overwriting skill " + skill.getID().toLowerCase() + ", if this isn't intended, please warn the mod author.");
		skillMap.put(skill.getID().toLowerCase(), skill);
		if (createEntry)
			ArcaneCompendium.AddCompendiumEntry(skill, skill.getID(), null, false);
	}
	
	public static void registerSkill (Skill skill) {
		registerSkill(true, skill);
	}
	
	public static HashMap<String, Skill> getSkillMap() {
		return skillMap;
	}
	
	
	public static Skill getSkillFromName (String name) {
		
		return skillMap.get(name.toLowerCase());
	}
	
	public static ArrayList<Skill> getSkillsForTree (SkillTree tree) {
		ArrayList<Skill> skillList = new ArrayList<Skill>();
		for (Entry<String, Skill> skills : skillMap.entrySet()) {
			Skill skill = skills.getValue();
			if (skill != null && skill.getTree() != null && skill.getTree().equals(tree))
				skillList.add(skill);
		}
		return skillList;
	}
	
}
