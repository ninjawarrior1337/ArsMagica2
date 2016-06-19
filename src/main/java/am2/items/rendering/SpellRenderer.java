package am2.items.rendering;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.google.common.collect.Lists;

import am2.ArsMagica2;
import am2.defs.ItemDefs;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class SpellRenderer implements ItemMeshDefinition {
	
	public static final List<ResourceLocation> resources = getResourceListing();
	private static final String iconsPath = "/assets/arsmagica2/models/item/spells/icons/";
	private static final String iconsPrefix = "spells/icons/";
	private List<ModelResourceLocation> locations = Lists.newArrayList();
	
	public SpellRenderer() {
			List<ResourceLocation> resources = getResourceListing();
			for (ResourceLocation resource : resources) {
				ArsMagica2.LOGGER.info("Loading " + resource);
				locations.add(new ModelResourceLocation(resource, "inventory"));
				ModelBakery.registerItemVariants(ItemDefs.spell, new ModelResourceLocation(resource, "inventory"));
			}
			ArsMagica2.LOGGER.info("Sucessfully Loaded " + locations.size() + " Spell Icons");
	}
	
	public static List<ResourceLocation> getResourceListing() {
		try {
			CodeSource src = ArsMagica2.class.getProtectionDomain().getCodeSource();
			ArrayList<ResourceLocation> toReturn = new ArrayList<ResourceLocation>();
			if (src != null){
				URL jar = src.getLocation();
				if (jar.getProtocol() == "jar"){
					String path = jar.toString().replace("jar:", "").replace("file:", "").replace("!/am2/ArsMagica2.class", "").replace('/', File.separatorChar);
					path = URLDecoder.decode(path, "UTF-8");
					ArsMagica2.LOGGER.debug(path);
					JarFile jarFile = new JarFile(path);
					Enumeration<JarEntry> entries = jarFile.entries();
					while (entries.hasMoreElements()){
						JarEntry entry = entries.nextElement();
						if (entry.getName().startsWith("assets/arsmagica2/models/item/spells/icons/")){
							String name = entry.getName().replace("assets/arsmagica2/models/item/spells/icons/", "");
							if (name.equals("")) continue;
							toReturn.add(new ResourceLocation("arsmagica2:" + iconsPrefix + name.replace(".json", "")));
						}
					}
					jarFile.close();
				}else if (jar.getProtocol() == "file"){
					String path = jar.toURI().toString().replace("/am2/ArsMagica2.class", iconsPath).replace("file:/", "").replace("%20", " ").replace("/", "\\");
					File file = new File(path);
					if (file.exists() && file.isDirectory()){
						for (File sub : file.listFiles()){
							toReturn.add(new ResourceLocation("arsmagica2:" + iconsPrefix + sub.getName().replace(".json", "")));
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

	
	@Override
	public ModelResourceLocation getModelLocation(ItemStack stack) {
		return locations.get(MathHelper.clamp_int(stack.getItemDamage(), 0, locations.size() - 1));
	}

}
