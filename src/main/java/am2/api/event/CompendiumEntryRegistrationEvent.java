package am2.api.event;

import java.util.Set;

import am2.lore.CompendiumEntry;
import am2.spell.SpellModifiers;
import net.minecraftforge.fml.common.eventhandler.Event;

public class CompendiumEntryRegistrationEvent extends Event {
	
	public Object entryItem;
	public String id;
	public Set<SpellModifiers> mods;
	public String[] relatedKeys;
	private CompendiumEntry entry;
	
	public CompendiumEntryRegistrationEvent(Object entryItem, String id, Set<SpellModifiers> mods, String... relatedKeys) {
		this.entryItem = entryItem;
		this.id = id;
		this.mods = mods;
		this.relatedKeys = relatedKeys;
	}
	
	public void setEntry(CompendiumEntry entry) {
		this.entry = entry;
	}
	
	public CompendiumEntry getEntry() {
		return entry;
	}
}
