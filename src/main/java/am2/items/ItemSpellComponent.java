package am2.items;

import java.util.List;

import am2.api.SkillRegistry;
import am2.skill.Skill;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemSpellComponent extends ItemArsMagica2 {
	
	public ItemSpellComponent() {
		setCreativeTab(null);
	}
	
	@Override
	public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
	}
	
	public static int getIdFor(Skill part) {
		int i = 0;
		for (Skill skill : SkillRegistry.getSkillMap().values()) {
			if (skill.equals(part)) return i;
			i++;
		}
		return i;
	}
	
	public static Skill getPartFor(int id) {
		int i = 0;
		for (Skill skill : SkillRegistry.getSkillMap().values()) {
			if (i == id) return skill;
			i++;
		}
		return null;
	}
	
}
