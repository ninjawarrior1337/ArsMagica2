package am2.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import am2.ArsMagica2;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class RenderFactory<T extends Entity> implements IRenderFactory<T> {
	
	private Constructor<Render<? super T>> constructor;
	
	public RenderFactory(Class<Render<? super T>> clazz) {
		try {
			this.constructor = clazz.getConstructor(RenderManager.class);
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public Render<? super T> createRenderFor(RenderManager manager) {
		try {
			Render<? super T> render = constructor.newInstance(manager);
			ArsMagica2.LOGGER.info("Successfully created instance for : " + constructor.getDeclaringClass().getName());
			return render;
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
			return null;
		}
	}

}
