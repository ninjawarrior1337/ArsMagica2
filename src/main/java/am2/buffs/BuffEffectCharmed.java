package am2.buffs;

import net.minecraft.entity.EntityLivingBase;
import am2.defs.PotionEffectsDefs;

public class BuffEffectCharmed extends BuffEffect{


	public static final int CHARM_TO_PLAYER = 1;
	public static final int CHARM_TO_MONSTER = 2;

//	private EntityLivingBase charmer;

	public BuffEffectCharmed(int duration, int amplifier){
		super(PotionEffectsDefs.charme, duration, amplifier);
	}

//	public void setCharmer(EntityLivingBase entity){
//		charmer = entity;
//	}

	@Override
	public void applyEffect(EntityLivingBase entityliving) {
		//TODO Summons
//		if (getAmplifier() + 1 == CHARM_TO_PLAYER && entityliving instanceof EntityCreature && charmer instanceof EntityPlayer){
//			EntityUtilities.makeSummon_PlayerFaction((EntityCreature)entityliving, (EntityPlayer)charmer, true);
//		}else if (getAmplifier() + 1 == CHARM_TO_MONSTER && entityliving instanceof EntityCreature){
//			EntityUtilities.makeSummon_MonsterFaction((EntityCreature)entityliving, true);
//		}
//		EntityUtilities.setOwner(entityliving, charmer);
//		EntityUtilities.setSummonDuration(entityliving, -1);
	}

	@Override
	public void stopEffect(EntityLivingBase entityliving){
//		if (entityliving instanceof EntityCreature){
//			EntityUtilities.revertAI((EntityCreature)entityliving);
//		}
	}

	@Override
	protected String spellBuffName(){
		return "Charmed";
	}

}
