package am2.particles;

import java.util.HashMap;
import java.util.Random;

import am2.affinity.Affinity;
import am2.defs.SkillDefs;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AMParticleIcons{

	public static final AMParticleIcons instance = new AMParticleIcons();

	private final HashMap<String, TextureAtlasSprite> icons;
	private final HashMap<String, TextureAtlasSprite> hiddenIcons;
	private static final Random rand = new Random();

	private AMParticleIcons(){
		icons = new HashMap<String, TextureAtlasSprite>();
		hiddenIcons = new HashMap<String, TextureAtlasSprite>();
		MinecraftForge.EVENT_BUS.register(this);
	}

	public int numParticles(){
		return icons.size();
	}
	
	@SubscribeEvent
	public void init(TextureStitchEvent.Pre e){
		icons.clear();
		TextureMap textureMap = e.getMap();

		loadAndInitIcon("arcane", "arcane", textureMap);
		loadAndInitIcon("clock", "clock", textureMap);
		loadAndInitIcon("ember", "ember", textureMap);
		loadAndInitIcon("explosion_2", "explosion_2", textureMap);
		loadAndInitIcon("ghost", "ghost", textureMap);
		loadAndInitIcon("heart", "heart", textureMap);
		loadAndInitIcon("leaf", "leaf", textureMap);
		loadAndInitIcon("lens_flare", "lens_flare", textureMap);
		loadAndInitIcon("lights", "lights", textureMap);
		loadAndInitIcon("plant", "plant", textureMap);
		loadAndInitIcon("pulse", "pulse", textureMap);
		loadAndInitIcon("rock", "rock", textureMap);
		loadAndInitIcon("rotating_rings", "rotating_rings", textureMap);
		loadAndInitIcon("smoke", "smoke", textureMap);
		loadAndInitIcon("sparkle", "sparkle", textureMap);
		loadAndInitIcon("sparkle2", "sparkle2", textureMap);
		loadAndInitIcon("water_ball", "water_ball", textureMap);
		loadAndInitIcon("wind", "wind", textureMap);

		loadAndInitIcon("air_hand", "air_hand", textureMap);
		loadAndInitIcon("arcane_hand", "arcane_hand", textureMap);
		loadAndInitIcon("earth_hand", "earth_hand", textureMap);
		loadAndInitIcon("ender_hand", "ender_hand", textureMap);
		loadAndInitIcon("fire_hand", "fire_hand", textureMap);
		loadAndInitIcon("ice_hand", "ice_hand", textureMap);
		loadAndInitIcon("life_hand", "life_hand", textureMap);
		loadAndInitIcon("lightning_hand", "lightning_hand", textureMap);
		loadAndInitIcon("nature_hand", "nature_hand", textureMap);
		loadAndInitIcon("none_hand", "none_hand", textureMap);
		loadAndInitIcon("water_hand", "water_hand", textureMap);

		loadAndInitIcon("beam", "beam", textureMap, false);
		loadAndInitIcon("beam1", "beam1", textureMap, false);
		loadAndInitIcon("beam2", "beam2", textureMap, false);

		for (int i = 1; i <= 28; ++i){
			loadAndInitIcon("Symbols" + i, "symbols/Symbols" + i, textureMap, false);
		}

		for (int i = 1; i <= 9; ++i){
			loadAndInitIcon("snowflake" + i, "snowflakes/snowflake" + i, textureMap, false);
		}

		icons.put("symbols", null);
		icons.put("snowflakes", null);

		AMParticle.particleTypes = icons.keySet().toArray(new String[icons.size() + 1]);
		AMParticle.particleTypes[AMParticle.particleTypes.length - 1] = "radiant";
	}

	private void loadAndInitIcon(String name, String TextureAtlasSpritePath, TextureMap register){
		loadAndInitIcon(name, TextureAtlasSpritePath, register, true);
	}

	private void loadAndInitIcon(String name, String TextureAtlasSpritePath, TextureMap register, boolean registerName){
		register.registerSprite(new ResourceLocation("arsmagica2:items/particles/" + TextureAtlasSpritePath));
		TextureAtlasSprite icon = register.getTextureExtry(new ResourceLocation("arsmagica2:items/particles/" + TextureAtlasSpritePath).toString());
		if (registerName){
			if (icon != null){
				icons.put(name, icon);
			}
		}else{
			if (icon != null){
				hiddenIcons.put(name, icon);
			}
		}
	}

	public void RegisterIcon(String name, TextureAtlasSprite TextureAtlasSprite){
		icons.put(name, TextureAtlasSprite);
	}

	public TextureAtlasSprite getIconByName(String name){
		TextureAtlasSprite icon = null;
		if (name.equals("symbols")){
			icon = hiddenIcons.get("Symbols" + (rand.nextInt(28) + 1));
		}else if (name.equals("snowflakes")){
			icon = hiddenIcons.get("snowflake" + (rand.nextInt(9) + 1));
		}
		icon = icons.get(name);
		if (icon == null) return icons.get("lights");
		return icon;
	}

	public TextureAtlasSprite getHiddenIconByName(String name){
		TextureAtlasSprite icon = hiddenIcons.get(name);
		if (icon == null) return icons.get("lights");
		return icon;
	}

	public String getParticleForAffinity(Affinity aff){
//		switch (aff){
//		case AIR:
//			return "wind";
//		case ARCANE:
//			return "arcane";
//		case EARTH:
//			return "rock";
//		case ENDER:
//			return "pulse";
//		case FIRE:
//			return "explosion_2";
//		case ICE:
//			return "ember";
//		case LIFE:
//			return "sparkle";
//		case LIGHTNING:
//			return "lightning_hand";
//		case NATURE:
//			return "plant";
//		case WATER:
//			return "water_ball";
//		case NONE:
//		default:
//			return "lens_flare";
//
//		}
		if (aff.equals(SkillDefs.AIR)) return "wind";
		if (aff.equals(SkillDefs.ARCANE)) return "arcane";
		if (aff.equals(SkillDefs.EARTH)) return "rock";
		if (aff.equals(SkillDefs.ENDER)) return "pulse";
		if (aff.equals(SkillDefs.FIRE)) return "explosion_2";
		if (aff.equals(SkillDefs.ICE)) return "ember";
		if (aff.equals(SkillDefs.LIFE)) return "sparkle";
		if (aff.equals(SkillDefs.LIGHTNING)) return "lightning_hand";
		if (aff.equals(SkillDefs.NATURE)) return "plant";
		if (aff.equals(SkillDefs.WATER)) return "water_ball";
		if (aff.equals(SkillDefs.NONE)) return "lens_flare";
		return "lens_flare";
	}

	public String getSecondaryParticleForAffinity(Affinity aff){
//		switch (aff){
//		case AIR:
//			return "air_hand";
//		case ARCANE:
//			return "symbols";
//		case EARTH:
//			return "earth_hand";
//		case ENDER:
//			return "ghost";
//		case FIRE:
//			return "smoke";
//		case ICE:
//			return "snowflakes";
//		case LIFE:
//			return "sparkle2";
//		case LIGHTNING:
//			return "lightning_hand";
//		case NATURE:
//			return "leaf";
//		case WATER:
//			return "water_hand";
//		case NONE:
//		default:
//			return "lights";
//
//		}
		
		if (aff.equals(SkillDefs.AIR)) return "air_hand";
		if (aff.equals(SkillDefs.ARCANE)) return "symbols";
		if (aff.equals(SkillDefs.EARTH)) return "earth_hand";
		if (aff.equals(SkillDefs.ENDER)) return "ghost";
		if (aff.equals(SkillDefs.FIRE)) return "smoke";
		if (aff.equals(SkillDefs.ICE)) return "snowflakes";
		if (aff.equals(SkillDefs.LIFE)) return "sparkle2";
		if (aff.equals(SkillDefs.LIGHTNING)) return "lightning_hand";
		if (aff.equals(SkillDefs.NATURE)) return "leaf";
		if (aff.equals(SkillDefs.WATER)) return "water_hand";
		if (aff.equals(SkillDefs.NONE)) return "lights";
		return "lights";

	}
}
