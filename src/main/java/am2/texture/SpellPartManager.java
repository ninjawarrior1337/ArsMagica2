package am2.texture;

import java.util.HashMap;

import am2.api.SkillRegistry;
import am2.skill.Skill;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SpellPartManager {
	
	public static final SpellPartManager INSTANCE = new SpellPartManager();
	
	private final HashMap<String, TextureAtlasSprite> sprites;
	
	private SpellPartManager() {
		this.sprites = new HashMap<>();
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@SubscribeEvent
	public void init (TextureStitchEvent.Pre e) {
		sprites.clear();
		for (Skill skill : SkillRegistry.getSkillMap().values()) {
			if (skill.getIcon() != null)
				sprites.put(skill.getID(), e.getMap().registerSprite(skill.getIcon()));
		}
	}
	
	public TextureAtlasSprite getSprite(String name) {
		return sprites.get(name);
	}
	
}
