package am2.items;

import java.util.List;

import am2.api.SpellRegistry;
import am2.api.SpellRegistry.SpellData;
import am2.spell.IComponent;
import am2.spell.IModifier;
import am2.spell.IShape;
import am2.spell.ISpellPart;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemSpellComponent extends ItemArsMagica2 {
	
	public ItemSpellComponent() {
		setCreativeTab(null);
	}
	
	@Override
	public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
		int i = 0;
		for (SpellData<IShape> shape : SpellRegistry.getShapeMap().values()) {
			if (shape == null) continue;
			subItems.add(new ItemStack(this, 1, i));
			i++;
		}
		for (SpellData<IComponent> component : SpellRegistry.getComponentMap().values()) {
			if (component == null) continue;
			subItems.add(new ItemStack(this, 1, i));
			i++;
		}
		for (SpellData<IModifier> modifier : SpellRegistry.getModifierMap().values()) {
			if (modifier == null) continue;
			subItems.add(new ItemStack(this, 1, i));
			i++;
		}
	}
	
	public static int getIdFor(ISpellPart part) {
		int i = 0;
		for (SpellData<IShape> shape : SpellRegistry.getShapeMap().values()) {
			if (shape == null) continue;
			if (shape.part.equals(part)) return i;
			i++;
		}
		for (SpellData<IComponent> component : SpellRegistry.getComponentMap().values()) {
			if (component == null) continue;
			if (component.part.equals(part)) return i;
			i++;
		}
		for (SpellData<IModifier> modifier : SpellRegistry.getModifierMap().values()) {
			if (modifier == null) continue;
			if (modifier.part.equals(part)) return i;
			i++;
		}
		return i;
	}
	
	public static SpellData<? extends ISpellPart> getPartFor(int id) {
		int i = 0;
		for (SpellData<IShape> shape : SpellRegistry.getShapeMap().values()) {
			if (shape == null) continue;
			if (i == id) return shape;
			i++;
		}
		for (SpellData<IComponent> component : SpellRegistry.getComponentMap().values()) {
			if (component == null) continue;
			if (i == id) return component;
			i++;
		}
		for (SpellData<IModifier> modifier : SpellRegistry.getModifierMap().values()) {
			if (modifier == null) continue;
			if (i == id) return modifier;
			i++;
		}
		return null;
	}
	
}
