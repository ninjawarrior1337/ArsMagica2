package am2.lore;

import java.util.ArrayList;
import java.util.EnumSet;

import am2.api.SkillRegistry;
import am2.api.SpellRegistry;
import am2.api.SpellRegistry.SpellData;
import am2.defs.ItemDefs;
import am2.gui.GuiArcaneCompendium;
import am2.items.ItemSpellComponent;
import am2.skill.Skill;
import am2.spell.IModifier;
import am2.spell.SpellModifiers;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CompendiumEntrySpellPart extends CompendiumEntry{

	private Skill skill;
	private EnumSet<SpellModifiers> mods;

	public CompendiumEntrySpellPart(CompendiumEntryType type, String id, Skill skill, EnumSet<SpellModifiers> mods, String... related){
		super(type, id, related);
		this.skill = skill;
		this.mods = mods;
	}
	@Override
	@SideOnly(Side.CLIENT)
	public GuiArcaneCompendium getCompendiumGui(){
		return new GuiArcaneCompendium(id, skill);
	}
	
	public ArrayList<ItemStack> getModifiers() {
		ArrayList<ItemStack> ret = new ArrayList<>();
		for (SpellData<IModifier> skill : SpellRegistry.getModifierMap().values()) {
			if (skill.part == null) continue;
			for (SpellModifiers mod : mods) {
				if (skill.part.getAspectsModified().contains(mod)) {
					ret.add(new ItemStack(ItemDefs.spell_component, 1, ItemSpellComponent.getIdFor(SkillRegistry.getSkillFromName(skill.id))));
					break;
				}
			}
		}
		return ret;
	}

	@Override
	public ItemStack getRepresentStack(){
		if (skill != null){
			return new ItemStack(ItemDefs.spell_component, 1, ItemSpellComponent.getIdFor(skill));
		}
		return null;
	}

}
