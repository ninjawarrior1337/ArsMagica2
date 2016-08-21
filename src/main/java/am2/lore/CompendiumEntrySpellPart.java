package am2.lore;

import java.util.ArrayList;
import java.util.EnumSet;

import am2.api.ArsMagicaAPI;
import am2.api.skill.Skill;
import am2.api.spell.AbstractSpellPart;
import am2.api.spell.SpellModifier;
import am2.api.spell.SpellModifiers;
import am2.defs.ItemDefs;
import am2.gui.GuiArcaneCompendium;
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
		return new GuiArcaneCompendium(id, skill, getModifiers());
	}
	
	@Override
	public String[] getRelatedItems() {
		ArrayList<String> str = new ArrayList<>();
		for (Skill sk : ArsMagicaAPI.getSkillRegistry().getValues()) {
			for (String parent : sk.getParents()) {
				if (parent.equals(skill.getID())) {
					str.add(sk.getID());
				}
			}
		}
		String[] array = new String[str.size() + related.length];
		for (int i = 0; i < related.length; i++)
			array[i] = related[i];
		for (int i = 0; i < str.size(); i++)
			array[i + related.length] = str.get(i);
		return super.getRelatedItems();
	}
	
	public ArrayList<ItemStack> getModifiers() {
		ArrayList<ItemStack> ret = new ArrayList<>();
		if (mods != null) {
			for (AbstractSpellPart part : ArsMagicaAPI.getSpellRegistry().getValues()) {
				if (part == null || !(part instanceof SpellModifier)) continue;
				SpellModifier modifier = (SpellModifier)part;
				for (SpellModifiers mod : mods) {
					boolean shouldSkip = false;
					for (SpellModifiers mod2 : modifier.getAspectsModified()) {
						if (mod2.equals(mod)) {
							ItemStack stack = new ItemStack(ItemDefs.spell_component, 1, ArsMagicaAPI.getSkillRegistry().getId(part.getRegistryName()));
							if (!ret.contains(stack))
								ret.add(new ItemStack(ItemDefs.spell_component, 1, ArsMagicaAPI.getSkillRegistry().getId(part.getRegistryName())));
							shouldSkip = true;
							break;
						}
					}
					if (shouldSkip)
						break;
				}
			}
		}
		return ret;
	}

	@Override
	public ItemStack getRepresentStack(){
		if (skill != null){
			return new ItemStack(ItemDefs.spell_component, 1, ArsMagicaAPI.getSkillRegistry().getId(skill));
		}
		return null;
	}

}
