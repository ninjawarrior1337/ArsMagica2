package am2.lore;

import am2.defs.ItemDefs;
import am2.gui.GuiArcaneCompendium;
import am2.items.ItemSpellComponent;
import am2.skill.Skill;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CompendiumEntrySkill extends CompendiumEntry{

	private Skill skill;

	public CompendiumEntrySkill(CompendiumEntryType type, String id, Skill skill, String... related){
		super(type, id, related);
		this.skill = skill;
	}
	@Override
	@SideOnly(Side.CLIENT)
	public GuiArcaneCompendium getCompendiumGui(){
		return new GuiArcaneCompendium(id, skill);
	}

	@Override
	public ItemStack getRepresentStack(){
		if (skill != null){
			return new ItemStack(ItemDefs.spell_component, 1, ItemSpellComponent.getIdFor(skill));
		}
		return null;
	}

}
