package am2.lore;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Set;

import am2.skill.Skill;
import am2.spell.SpellModifiers;
import net.minecraft.item.ItemStack;

public class CompendiumEntrySpellModifier extends CompendiumEntrySpellPart{

	private Set<SpellModifiers> modifies;

	public CompendiumEntrySpellModifier(String id, Skill mod, EnumSet<SpellModifiers> modifies, String... related){
		super(CompendiumEntryTypes.instance.SPELL_MODIFIER, id, mod, modifies, related);
		this.modifies = modifies;
	}
	
	@Override
	public ArrayList<ItemStack> getModifiers() {
		return new ArrayList<>();
	}

	public Set<SpellModifiers> getModifies(){
		return this.modifies;
	}
}
