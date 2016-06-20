package am2.config;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class AM2Config extends Configuration{
	
	public static int maxLevel = 99;
	public static int manaDefault = 100;
	public static float maxManaGrowth = 1.10F;
	public static float XPNeedDefault = 21F;
	public static float XPNeedGrowth = 1.10F;
	public static int healCooldown = 200;
	public static double frictionCoef = 0.8;
	
	public static final float MANA_BURNOUT_RATIO = 0.38F;
	
	public AM2Config(File file) {
		super(file);
		maxLevel = this.getInt("MaxLevel", "magic", maxLevel, 0, 1000, "Maximum Mage Level");
		maxManaGrowth = this.getFloat("MaxManaGrowth", "magic", maxManaGrowth, 1F, 100F, "Mana Multiplicator per level");
		manaDefault = this.getInt("MaxManaGrowth", "magic", manaDefault, 1, 1000, "Default mana at level 1");
		XPNeedDefault = this.getFloat("XPNeedDefault", "magic", XPNeedDefault, 1, 1000, "Default XP requirement");
		XPNeedGrowth = this.getFloat("XPNeedGrowth", "magic", XPNeedGrowth, 1F, 100F, "XP Multiplicator per level");
		healCooldown = this.getInt("HealCooldown", "magic", healCooldown, 1, 1000, "Heal Cooldown");
	}
	
	public int readPotionId (String name, int defaultId) {
		return this.getInt(name, "potion", defaultId, 0, 256, "ID for " + name);
	}

	public boolean FullGFX() {
		return false;
	}

	public boolean LowGFX() {
		return false;
	}

	public int getGFXLevel() {
		return 1;
	}

	public float getDamageMultiplier() {
		return 1f;
	}

	public boolean forgeSmeltsVillagers() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean NoGFX() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean moonstoneMeteorsDestroyTerrain() {
		// TODO Auto-generated method stub
		return false;
	}
	
	
}
