package am2.texture;

import java.util.HashMap;

import am2.api.SkillRegistry;
import am2.gui.AMGuiIcons;
import am2.lore.CompendiumEntryTypes;
import am2.skill.Skill;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SpellIconManager {
	
	public static final SpellIconManager INSTANCE = new SpellIconManager();
	
	private final HashMap<String, TextureAtlasSprite> sprites;
	
	private SpellIconManager() {
		this.sprites = new HashMap<>();
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@SubscribeEvent
	public void init (TextureStitchEvent.Pre e) {
		sprites.clear();
		AMGuiIcons.instance.init(e.getMap());
		for (Skill skill : SkillRegistry.getSkillMap().values()) {
			if (skill.getIcon() != null)
				sprites.put(skill.getID(), e.getMap().registerSprite(skill.getIcon()));
		}
		CompendiumEntryTypes.instance.initTextures();
		sprites.put("CasterRuneSide", e.getMap().registerSprite(new ResourceLocation("arsmagica2:blocks/CasterRuneSide")));
		sprites.put("RuneStone", e.getMap().registerSprite(new ResourceLocation("arsmagica2:blocks/RuneStone")));
	}
	
	public TextureAtlasSprite getSprite(String name) {
		return sprites.get(name);
	}
	
}
