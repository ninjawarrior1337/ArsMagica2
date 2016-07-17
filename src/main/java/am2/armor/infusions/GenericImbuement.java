package am2.armor.infusions;

import java.util.EnumSet;
import java.util.UUID;

import am2.api.items.armor.IArmorImbuement;
import am2.api.items.armor.ImbuementApplicationTypes;
import am2.api.items.armor.ImbuementTiers;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class GenericImbuement implements IArmorImbuement{

	private String id = "";
	private int iconIndex = 0;
	private ImbuementTiers tier;
	private EntityEquipmentSlot[] validSlots;

	public static final UUID imbuedHasteID = UUID.fromString("3b51a94c-8866-470b-8b69-e1d5cb50a72f");
	public static final AttributeModifier imbuedHaste = new AttributeModifier(imbuedHasteID, "Imbued Haste", 0.2, 2);

	public static final String manaRegen = "mn_reg";
	public static final String burnoutReduction = "bn_red";
	public static final String flickerLure = "fl_lure";
	public static final String magicXP = "mg_xp";
	public static final String pinpointOres = "pp_ore";
	public static final String magitechGoggleIntegration = "mg_gog";
	public static final String thaumcraftNodeReveal = "tc_nrv";
	public static final String stepAssist = "step_up";
	public static final String runSpeed = "run_spd";
	public static final String soulbound = "soulbnd";

	public GenericImbuement(String id, int IIconIndex, ImbuementTiers tier, EntityEquipmentSlot[] validSlots){
		this.id = id;
		this.iconIndex = IIconIndex;
		this.tier = tier;
		this.validSlots = validSlots;
	}

	public static void registerAll(){
		//all armors
		ImbuementRegistry.instance.registerImbuement(new GenericImbuement(manaRegen, 0, ImbuementTiers.FIRST, new EntityEquipmentSlot[]{EntityEquipmentSlot.FEET, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.HEAD}));
		ImbuementRegistry.instance.registerImbuement(new GenericImbuement(burnoutReduction, 1, ImbuementTiers.FIRST, new EntityEquipmentSlot[]{EntityEquipmentSlot.FEET, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.HEAD}));
		ImbuementRegistry.instance.registerImbuement(new GenericImbuement(soulbound, 31, ImbuementTiers.FIRST, new EntityEquipmentSlot[]{EntityEquipmentSlot.FEET, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.HEAD}));

		//chest
		ImbuementRegistry.instance.registerImbuement(new GenericImbuement(flickerLure, 3, ImbuementTiers.FIRST, new EntityEquipmentSlot[]{EntityEquipmentSlot.CHEST}));
		ImbuementRegistry.instance.registerImbuement(new GenericImbuement(magicXP, 15, ImbuementTiers.FOURTH, new EntityEquipmentSlot[]{EntityEquipmentSlot.CHEST}));

		//HEADet
		ImbuementRegistry.instance.registerImbuement(new GenericImbuement(pinpointOres, 16, ImbuementTiers.FIRST, new EntityEquipmentSlot[]{EntityEquipmentSlot.HEAD}));
		ImbuementRegistry.instance.registerImbuement(new GenericImbuement(magitechGoggleIntegration, 17, ImbuementTiers.FIRST, new EntityEquipmentSlot[]{EntityEquipmentSlot.HEAD}));
		ImbuementRegistry.instance.registerImbuement(new GenericImbuement(thaumcraftNodeReveal, 2, ImbuementTiers.FOURTH, new EntityEquipmentSlot[]{EntityEquipmentSlot.HEAD}));

		//legs
		ImbuementRegistry.instance.registerImbuement(new GenericImbuement(stepAssist, 21, ImbuementTiers.FIRST, new EntityEquipmentSlot[]{EntityEquipmentSlot.LEGS}));

		//FEET
		ImbuementRegistry.instance.registerImbuement(new GenericImbuement(runSpeed, 26, ImbuementTiers.FIRST, new EntityEquipmentSlot[]{EntityEquipmentSlot.FEET}));
	}

	@Override
	public String getID(){
		return id;
	}

	@Override
	public int getIconIndex(){
		return iconIndex;
	}

	@Override
	public ImbuementTiers getTier(){
		return tier;
	}

	@Override
	public EnumSet<ImbuementApplicationTypes> getApplicationTypes(){
		return EnumSet.of(ImbuementApplicationTypes.NONE);
	}

	@Override
	public boolean applyEffect(EntityPlayer player, World world, ItemStack stack, ImbuementApplicationTypes matchedType, Object... params){
		return false;
	}

	@Override
	public EntityEquipmentSlot[] getValidSlots(){
		return validSlots;
	}

	@Override
	public boolean canApplyOnCooldown(){
		return true;
	}

	@Override
	public int getCooldown(){
		return 0;
	}

	@Override
	public int getArmorDamage(){
		return 0;
	}
}
