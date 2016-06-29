package am2.models;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;

import am2.affinity.Affinity;
import am2.api.AffinityRegistry;
import am2.items.rendering.SpellModel;
import am2.utils.ResourceUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.LoaderState;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ArsMagicaModelLoader implements ICustomModelLoader {
	
	public static final HashMap<Affinity, TextureAtlasSprite> sprites = new HashMap<>();
	public static final HashMap<String, TextureAtlasSprite> particles = new HashMap<>();
	
	@Override
	public void onResourceManagerReload(IResourceManager resourceManager) {
		sprites.clear();
		SpellModel.handRender = null;
		
	}

	@Override
	public boolean accepts(ResourceLocation modelLocation) {
		return modelLocation.toString().contains("spells/icons");
	}

	@SuppressWarnings("unchecked")
	@Override
	public IModel loadModel(ResourceLocation modelLocation) throws Exception {
		if (!Loader.instance().hasReachedState(LoaderState.POSTINITIALIZATION)) {
			return ModelLoaderRegistry.getMissingModel();
		}
		try {
		    ImmutableList.Builder<ResourceLocation> builder = ImmutableList.builder();
		    IResource iresource =
	        Minecraft.getMinecraft().getResourceManager()
	                 .getResource(new ResourceLocation(modelLocation.getResourceDomain(), modelLocation.getResourcePath() + ".json"));
		    Reader reader = new InputStreamReader(iresource.getInputStream(), Charsets.UTF_8);
		    for (String s : ((Map<String,String>) ResourceUtils.GSON.fromJson(reader, ResourceUtils.mapType)).values()) {
				builder.add(new ResourceLocation(s));
			}
			IModel model = new SpellModel(builder.build());
			return model;
		} catch (IOException e) {}
		return ModelLoaderRegistry.getMissingModel();
	}
	
	@SubscribeEvent
	public void preStitch(TextureStitchEvent.Pre e) {
		for (Affinity aff : AffinityRegistry.getAffinityMap().values()) {
			e.getMap().registerSprite(new ResourceLocation("arsmagica2", "items/particles/" + aff.getName().toLowerCase() + "_hand"));
			sprites.put(aff, e.getMap().getTextureExtry(new ResourceLocation("arsmagica2", "items/particles/" + aff.getName().toLowerCase() + "_hand").toString()));
		}
		registerParticle(e.getMap(), "arcane");
		registerParticle(e.getMap(), "beam");
		registerParticle(e.getMap(), "beam1");
		registerParticle(e.getMap(), "beam2");
		registerParticle(e.getMap(), "clock");
		registerParticle(e.getMap(), "ember");
		registerParticle(e.getMap(), "explosion_2");
		registerParticle(e.getMap(), "ghost");
		registerParticle(e.getMap(), "heart");
		registerParticle(e.getMap(), "leaf");
		registerParticle(e.getMap(), "lens_flare");
		registerParticle(e.getMap(), "lights");
		registerParticle(e.getMap(), "plant");
		registerParticle(e.getMap(), "pulse");
		registerParticle(e.getMap(), "rock");
		registerParticle(e.getMap(), "rotating_rings");
		registerParticle(e.getMap(), "smoke");
		registerParticle(e.getMap(), "sparkle");
		registerParticle(e.getMap(), "sparkle2");
		registerParticle(e.getMap(), "water_ball");
		registerParticle(e.getMap(), "wind");
		registerParticle(e.getMap(), "witchwood_leaf");
		e.getMap().registerSprite(new ResourceLocation("arsmagica2:blocks/custom/obelisk"));
		e.getMap().registerSprite(new ResourceLocation("arsmagica2:blocks/custom/obelisk_active"));
		e.getMap().registerSprite(new ResourceLocation("arsmagica2:blocks/custom/obelisk_active_highpower"));
		e.getMap().registerSprite(new ResourceLocation("arsmagica2:blocks/custom/obelisk_runes"));
		e.getMap().registerSprite(new ResourceLocation("arsmagica2:blocks/custom/celestial_prism"));
	}
	
	private void registerParticle(TextureMap map, String name) {
		map.registerSprite(new ResourceLocation("arsmagica2", "items/particles/" + name));
		particles.put(name, map.getTextureExtry(new ResourceLocation("arsmagica2", "items/particles/" + name).toString()));
	}
}
