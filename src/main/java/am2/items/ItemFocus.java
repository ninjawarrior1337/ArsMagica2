package am2.items;

public abstract class ItemFocus extends ItemArsMagica{
	
	public ItemFocus() {
		super();
	}
	
	public abstract Object[] getRecipeItems();

	public abstract String getInGameName();
}
