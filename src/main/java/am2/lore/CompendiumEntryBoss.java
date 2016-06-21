package am2.lore;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import am2.gui.GuiArcaneCompendium;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CompendiumEntryBoss extends CompendiumEntry{
	
	protected Class<? extends Entity> clazz;
	
	public CompendiumEntryBoss(String id, Class<? extends Entity> clazz, String... related){
		super(CompendiumEntryTypes.instance.BOSS, id, related);
		this.clazz = clazz;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public GuiArcaneCompendium getCompendiumGui(){
		if (clazz != null){
			try{
				Constructor<? extends Entity> ctor = clazz.getConstructor(World.class);
				return new GuiArcaneCompendium(id, (Entity)ctor.newInstance(Minecraft.getMinecraft().theWorld));
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
		return new GuiArcaneCompendium(id);
	}


	@Override
	public ItemStack getRepresentStack(){
		return null;
	}

}
