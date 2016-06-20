package am2.lore;

import am2.ArsMagica2;
import am2.api.SkillRegistry;
import am2.defs.SkillDefs;
import am2.event.PlayerMagicLevelChangeEvent;
import am2.event.SkillLearnedEvent;
import am2.event.SpellCastEvent;
import am2.extensions.EntityExtension;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry.EntityRegistration;

/**
 * This class should handle compendium unlocks wherever possible through events.
 * If it is not possible, then all calls should use the static utility methods here.
 *
 * @author Mithion
 */
public class CompendiumUnlockHandler{
	/**
	 * This is a catch all method - it's genericized to attempt to unlock a compendium entry for anything AM2 based that the player picks up
	 *
	 * @param event
	 */
	@SubscribeEvent
	public void onPlayerPickupItem(EntityItemPickupEvent event){
	}

	/**
	 * Any magic level based unlocks should go in here
	 *
	 * @param event
	 */
	@SubscribeEvent
	public void onPlayerMagicLevelChange(PlayerMagicLevelChangeEvent event){
		if (event.getEntity().worldObj.isRemote && event.getEntity() == Minecraft.getMinecraft().thePlayer){
			if (event.getLevel() >= 5){
				ArcaneCompendium.instance.unlockCategory("talents");
				//ArcaneCompendium.instance.unlockEntry("dungeonsAndExploring");
				ArcaneCompendium.instance.unlockEntry("enchantments");
			}
			if (event.getLevel() >= 10){
				ArcaneCompendium.instance.unlockEntry("armorMage");
				ArcaneCompendium.instance.unlockEntry("playerjournal");
			}
			if (event.getLevel() >= 15){
				ArcaneCompendium.instance.unlockEntry("BossWaterGuardian");
				ArcaneCompendium.instance.unlockEntry("BossEarthGuardian");
				ArcaneCompendium.instance.unlockEntry("rituals");
				ArcaneCompendium.instance.unlockEntry("inlays");
				ArcaneCompendium.instance.unlockEntry("inlays_structure");
			}
			if (event.getLevel() >= 20){
				ArcaneCompendium.instance.unlockEntry("armorBattlemage");
			}
			if (event.getLevel() >= 25){
				ArcaneCompendium.instance.unlockEntry("BossAirGuardian");
				ArcaneCompendium.instance.unlockEntry("BossArcaneGuardian");
				ArcaneCompendium.instance.unlockEntry("BossLifeGuardian");
			}
			if (event.getLevel() >= 35){
				ArcaneCompendium.instance.unlockEntry("BossNatureGuardian");
				ArcaneCompendium.instance.unlockEntry("BossWinterGuardian");
				ArcaneCompendium.instance.unlockEntry("BossFireGuardian");
				ArcaneCompendium.instance.unlockEntry("BossLightningGuardian");
				ArcaneCompendium.instance.unlockEntry("BossEnderGuardian");
			}
		}
	}

	/**
	 * This should handle all mobs and the Astral Barrier
	 *
	 * @param event
	 */
	@SubscribeEvent
	public void onEntityDeath(LivingDeathEvent event){
		if (event.getEntityLiving().worldObj.isRemote && event.getSource().getSourceOfDamage() instanceof EntityPlayer){
			if (event.getEntity() instanceof EntityEnderman){
				ArcaneCompendium.instance.unlockEntry("blockastralbarrier");
			}else{
				EntityRegistration reg = EntityRegistry.instance().lookupModSpawn(event.getEntityLiving().getClass(), true);
				if (reg != null && reg.getContainer().matches(ArsMagica2.instance)){
					String id = reg.getEntityName();
					ArcaneCompendium.instance.unlockEntry(id);
				}
			}
		}
	}


	/**
	 * Any skill-based unlocks should go in here
	 *
	 * @param event
	 */
	@SubscribeEvent
	public void onSkillLearned(SkillLearnedEvent event){
		if (event.getEntityPlayer() == Minecraft.getMinecraft().thePlayer){
			if (event.getSkill().equals(SkillRegistry.getSkillFromName("summon"))){
				ArcaneCompendium.instance.unlockEntry("crystal_phylactery");
				ArcaneCompendium.instance.unlockEntry("summoner");
			} else if (event.getSkill().equals(SkillRegistry.getSkillFromName("true_sight"))){
				ArcaneCompendium.instance.unlockEntry("illusionBlocks");
			} else if (event.getSkill().getPoint().equals(SkillDefs.SILVER_POINT)){
				ArcaneCompendium.instance.unlockEntry("silver_skills");
			}
		}
	}

	/**
	 * Any spell-based unlocks should go here (eg, low mana based unlocks, affinity, etc.)
	 *
	 * @param event
	 */
	@SubscribeEvent
	public void onSpellCast(SpellCastEvent.Pre event){
		if (event.entityLiving == Minecraft.getMinecraft().thePlayer){
			ArcaneCompendium.instance.unlockEntry("unlockingPowers");
			ArcaneCompendium.instance.unlockEntry("affinity");
			if (EntityExtension.For(event.entityLiving).getCurrentMana() < EntityExtension.For(event.entityLiving).getMaxMana() / 2)
				ArcaneCompendium.instance.unlockEntry("mana_potion");
		}
	}

	/**
	 * This is another genericized method, which attempts to unlock any entry for something the player crafts
	 */
	@SubscribeEvent
	public void onCrafting(ItemCraftedEvent event){
		if (event.player.worldObj.isRemote){
			ArcaneCompendium.instance.unlockRelatedItems(event.crafting);
		}
	}

	/**
	 * Helper method (auto-proxied) that will unlock a compendium entry.  If the entry is found to be a category, it wil be unlocked instead.
	 *
	 * @param id The ID used to identify the entry to unlock.
	 */
	public static void unlockEntry(String id){
		ArsMagica2.proxy.unlockCompendiumEntry(id);
	}

	/**
	 * Helper method (auto-proxied) that will unlock a compendium category.
	 *
	 * @param id The ID used to identify the entry to unlock.
	 */
	public static void unlockCategory(String id){
		ArsMagica2.proxy.unlockCompendiumCategory(id);
	}
}
