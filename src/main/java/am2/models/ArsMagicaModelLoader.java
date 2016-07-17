package am2.models;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import am2.ArsMagica2;
import am2.api.ArsMagicaAPI;
import am2.api.affinity.Affinity;
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
	public static final List<ResourceLocation> spellIcons = getResourceListing();
	
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
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ModelLoaderRegistry.getMissingModel();
	}
	
	@SubscribeEvent
	public void preStitch(TextureStitchEvent.Pre e) {
		for (Affinity aff : ArsMagicaAPI.getAffinityRegistry().getValues()) {
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
	
	private static final String iconsPath = "/assets/arsmagica2/textures/items/spells/icons/";
	private static final String iconsPrefix = "items/spells/icons/";
	
	private static List<ResourceLocation> getResourceListing() {
		try {
			CodeSource src = ArsMagica2.class.getProtectionDomain().getCodeSource();
			ArrayList<ResourceLocation> toReturn = new ArrayList<ResourceLocation>();
			if (src != null){
				URL jar = src.getLocation();
				if (jar.getProtocol() == "jar"){
					String path = jar.toString().replace("jar:", "").replace("file:", "").replace("!/am2/ArsMagica2.class", "").replace('/', File.separatorChar);
					path = URLDecoder.decode(path, "UTF-8");
					JarFile jarFile = new JarFile(path);
					Enumeration<JarEntry> entries = jarFile.entries();
					while (entries.hasMoreElements()){
						JarEntry entry = entries.nextElement();
						if (entry.getName().startsWith("assets/arsmagica2/textures/items/spells/icons/")){
							String name = entry.getName().replace("assets/arsmagica2/textures/items/spells/icons/", "");
							if (name.equals("")) continue;
							toReturn.add(new ResourceLocation("arsmagica2:" + iconsPrefix + name.replace(".png", "")));
						}
					}
					jarFile.close();
				}else if (jar.getProtocol() == "file"){
					String path = jar.toURI().toString().replace("/am2/ArsMagica2.class", iconsPath).replace("file:/", "").replace("%20", " ").replace("/", "\\");
					File file = new File(path);
					if (file.exists() && file.isDirectory()){
						for (File sub : file.listFiles()){
							toReturn.add(new ResourceLocation("arsmagica2:" + iconsPrefix + sub.getName().replace(".png", "")));
						}
					}
				}
				return toReturn;
			}else{
				return toReturn;
			}
		} catch (IOException | URISyntaxException e) {
			return Lists.newArrayList();
		}
	}
}
