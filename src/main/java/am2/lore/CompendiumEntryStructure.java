package am2.lore;

import am2.api.IMultiblockStructureController;
import am2.gui.GuiArcaneCompendium;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CompendiumEntryStructure extends CompendiumEntry{

	private Class<? extends TileEntity> controllerClass;

	public CompendiumEntryStructure(String id, Class<? extends TileEntity> controller, String... related){
		super(CompendiumEntryTypes.instance.STRUCTURE, id, related);
		this.controllerClass = controller;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public GuiArcaneCompendium getCompendiumGui(){
		if (this.controllerClass != null){
			if (controllerClass != null && IMultiblockStructureController.class.isAssignableFrom(controllerClass)){
				try{
					TileEntity te = (TileEntity)controllerClass.newInstance();
					return new GuiArcaneCompendium(id, ((IMultiblockStructureController)te).getDefinition(), te);
				}catch (InstantiationException e){
					e.printStackTrace();
				}catch (IllegalAccessException e){
					e.printStackTrace();
				}
			}
		}
		return new GuiArcaneCompendium(id);
	}

	@Override
	public ItemStack getRepresentStack(){
		return null;
	}

}
