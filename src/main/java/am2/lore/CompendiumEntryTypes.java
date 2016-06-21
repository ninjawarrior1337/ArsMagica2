package am2.lore;

import am2.defs.ItemDefs;
import am2.defs.SkillDefs;
import am2.gui.AMGuiIcons;
import am2.texture.SpellIconManager;
import am2.utils.AffinityShiftUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;

public class CompendiumEntryTypes{

	public final CompendiumEntryType GUIDE = new CompendiumEntryType("guides", "guide");
	public final CompendiumEntryType MECHANIC = new CompendiumEntryType("mechanics", "mechanic");
	public final CompendiumEntryType ITEM = new CompendiumEntryType("items", "item");
	public final CompendiumEntryType BLOCK = new CompendiumEntryType("blocks", "block");
	public final CompendiumEntryType SPELL_SHAPE = new CompendiumEntryType("shapes", "shape");
	public final CompendiumEntryType SPELL_COMPONENT = new CompendiumEntryType("components", "component");
	public final CompendiumEntryType SPELL_MODIFIER = new CompendiumEntryType("modifiers", "modifier");
	public final CompendiumEntryType TALENT = new CompendiumEntryType("talents", "talent");
	public final CompendiumEntryType MOB = new CompendiumEntryType("mobs", "mob");
	public final CompendiumEntryType STRUCTURE = new CompendiumEntryType("structures", "structure");
	public final CompendiumEntryType RITUAL = new CompendiumEntryType("structures", "ritual");
	public final CompendiumEntryType BOSS = new CompendiumEntryType("bosses", "boss");

	public static final CompendiumEntryTypes instance = new CompendiumEntryTypes();
	private boolean initialized = false;

	public boolean hasInitialized(){
		return initialized;
	}

	public static CompendiumEntryType[] categoryList(){
		return new CompendiumEntryType[]{
				instance.GUIDE,
				instance.MECHANIC,
				instance.ITEM,
				instance.BLOCK,
				instance.SPELL_SHAPE,
				instance.SPELL_COMPONENT,
				instance.SPELL_MODIFIER,
				instance.TALENT,
				instance.MOB,
				instance.STRUCTURE,
				instance.BOSS
		};
	}

	private static CompendiumEntryType[] allValues(){
		return new CompendiumEntryType[]{
				instance.GUIDE,
				instance.MECHANIC,
				instance.ITEM,
				instance.BLOCK,
				instance.SPELL_SHAPE,
				instance.SPELL_COMPONENT,
				instance.SPELL_MODIFIER,
				instance.TALENT,
				instance.MOB,
				instance.STRUCTURE,
				instance.RITUAL,
				instance.BOSS
		};
	}

	public void initTextures(){
		ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();
		GUIDE.setRepresentIcon(mesher.getParticleIcon(ItemDefs.arcaneCompendium));
		MECHANIC.setRepresentIcon(mesher.getParticleIcon(ItemDefs.magitechGoggles));
		ITEM.setRepresentIcon(mesher.getParticleIcon(ItemDefs.essence, AffinityShiftUtils.getEssenceForAffinity(SkillDefs.ICE).getItemDamage()));
		BLOCK.setRepresentIcon(mesher.getParticleIcon(ItemDefs.crystalWrench));
		SPELL_SHAPE.setRepresentIcon(SpellIconManager.INSTANCE.getSprite("Binding"));
		SPELL_COMPONENT.setRepresentIcon(SpellIconManager.INSTANCE.getSprite("LifeTap"));
		SPELL_MODIFIER.setRepresentIcon(SpellIconManager.INSTANCE.getSprite("VelocityAdded"));
		TALENT.setRepresentIcon(SpellIconManager.INSTANCE.getSprite("AugmentedCasting"));
		MOB.setRepresentIcon(AMGuiIcons.fatigueIcon);
		STRUCTURE.setRepresentIcon(AMGuiIcons.gatewayPortal);
		RITUAL.setRepresentIcon(AMGuiIcons.gatewayPortal);
		BOSS.setRepresentIcon(AMGuiIcons.evilBook);

		initialized = true;
	}

	public static CompendiumEntryType getForSection(String category, String node){
		for (CompendiumEntryType type : CompendiumEntryTypes.allValues()){
			if (type.getCategoryName().equals(category) && type.getNodeName().equals(node)){
				return type;
			}
		}
		return null;
	}
}
