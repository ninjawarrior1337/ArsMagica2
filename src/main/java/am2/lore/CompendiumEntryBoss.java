package am2.lore;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.w3c.dom.Node;

import am2.gui.GuiArcaneCompendium;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CompendiumEntryBoss extends CompendiumEntry{

	public CompendiumEntryBoss(){
		super(CompendiumEntryTypes.instance.BOSS);
	}

	@Override
	protected void parseEx(Node node){
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected GuiArcaneCompendium getCompendiumGui(String searchID, int meta){
		String modEntityID = "arsmagica2." + searchID;
		Class<? extends Entity> entityClass = (Class<? extends Entity>)EntityList.NAME_TO_CLASS.get(modEntityID);
		if (entityClass != null){
			try{
				Constructor<? extends Entity> ctor = entityClass.getConstructor(World.class);
				return new GuiArcaneCompendium((Entity)ctor.newInstance(Minecraft.getMinecraft().theWorld));
			}catch (InstantiationException e){
				e.printStackTrace();
			}catch (IllegalAccessException e){
				e.printStackTrace();
			}catch (NoSuchMethodException e){
				e.printStackTrace();
			}catch (SecurityException e){
				e.printStackTrace();
			}catch (IllegalArgumentException e){
				e.printStackTrace();
			}catch (InvocationTargetException e){
				e.printStackTrace();
			}
		}
		return new GuiArcaneCompendium(searchID);
	}


	@Override
	public ItemStack getRepresentItemStack(String searchID, int meta){
		return null;
	}

}
