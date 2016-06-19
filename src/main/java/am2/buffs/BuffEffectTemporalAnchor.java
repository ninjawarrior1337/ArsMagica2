package am2.buffs;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import am2.defs.PotionEffectsDefs;
import am2.extensions.EntityExtension;

public class BuffEffectTemporalAnchor extends BuffEffect{

	private double x;
	private double y;
	private double z;
	private float rotationPitch;
	private float rotationYaw;
	private float rotationYawHead;

	private float mana;
	private float health;

	public BuffEffectTemporalAnchor(int duration, int amplifier){
		super(PotionEffectsDefs.temporalAnchor, duration, amplifier);
	}

	@Override
	public void applyEffect(EntityLivingBase entityliving){
		//store values from the entity
		x = entityliving.posX;
		y = entityliving.posY;
		z = entityliving.posZ;
		rotationPitch = entityliving.rotationPitch;
		rotationYaw = entityliving.rotationYaw;
		rotationYawHead = entityliving.rotationYawHead;
				
		health = entityliving.getHealth();
		mana = entityliving.getCapability(EntityExtension.INSTANCE, null).getCurrentMana();
	}

	@Override
	public void stopEffect(EntityLivingBase entityliving){
		entityliving.setPositionAndUpdate(x, y, z);
		entityliving.setAngles(rotationYaw, rotationPitch);
		entityliving.rotationYawHead = rotationYawHead;
		entityliving.getCapability(EntityExtension.INSTANCE, null).setCurrentMana(mana);

		entityliving.setHealth(health);
		entityliving.fallDistance = 0;
	}

	@Override
	protected String spellBuffName(){
		return "Temporal Anchor";
	}
	
	@Override
	public NBTTagCompound writeCustomPotionEffectToNBT(NBTTagCompound nbt) {
		nbt.setDouble("X", x);
		nbt.setDouble("Y", y);
		nbt.setDouble("Z", z);
		nbt.setFloat("RotationPitch", rotationPitch);
		nbt.setFloat("RotationYaw", rotationYaw);
		nbt.setFloat("RotationYawHead", rotationYawHead);
		nbt.setFloat("Mana", mana);
		nbt.setFloat("Health", health);

		return super.writeCustomPotionEffectToNBT(nbt);
	}
	
	public static BuffEffectTemporalAnchor readCustomPotionEffectFromNBT(NBTTagCompound nbt) {
		BuffEffectTemporalAnchor tmp = (BuffEffectTemporalAnchor)PotionEffect.readCustomPotionEffectFromNBT(nbt);
		tmp.x = nbt.getDouble("X");
		tmp.y = nbt.getDouble("Y");
		tmp.z = nbt.getDouble("Z");
		tmp.rotationPitch = nbt.getFloat("RotationPitch");
		tmp.rotationYaw = nbt.getFloat("RotationYaw");
		tmp.rotationYawHead = nbt.getFloat("RotationYawHead");
		tmp.health = nbt.getFloat("Health");
		tmp.mana = nbt.getInteger("Mana");
		return tmp;
	}
	
	
}
