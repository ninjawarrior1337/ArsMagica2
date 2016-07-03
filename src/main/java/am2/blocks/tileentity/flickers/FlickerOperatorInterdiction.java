package am2.blocks.tileentity.flickers;

import am2.affinity.Affinity;
import am2.api.AffinityRegistry;
import am2.api.flickers.IFlickerController;
import am2.blocks.BlockInvisibleUtility.EnumInvisibleType;
import am2.defs.ItemDefs;
import am2.defs.SkillDefs;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FlickerOperatorInterdiction extends FlickerOperatorContainment{

	@Override
	public boolean DoOperation(World worldObj, IFlickerController<?> habitat, boolean powered, Affinity[] flickers){
		if (worldObj.isRemote)
			return true;

		boolean hasEnderAugment = false;
		for (Affinity aff : flickers){
			if (aff == SkillDefs.ENDER){
				hasEnderAugment = true;
				break;
			}
		}

		int lastRadius = getLastRadius(habitat);
		int calcRadius = calculateRadius(flickers);

		if (lastRadius != calcRadius){
			RemoveOperator(worldObj, habitat, powered, flickers);
		}
		
		BlockPos habitatPos = ((TileEntity)habitat).getPos();
		
		for (int i = 0; i < calcRadius * 2 + 1; ++i){
			if (hasEnderAugment){
				//-x
				setUtilityBlock(worldObj, habitatPos.add(-calcRadius, 0, -calcRadius + i), EnumInvisibleType.COLLISION_ALL);
				//+x
				setUtilityBlock(worldObj, habitatPos.add(calcRadius + 1, 0, -calcRadius + i), EnumInvisibleType.COLLISION_ALL);
				//-z
				setUtilityBlock(worldObj, habitatPos.add(-calcRadius + i, 0, -calcRadius), EnumInvisibleType.COLLISION_ALL);
				//+z
				setUtilityBlock(worldObj, habitatPos.add(calcRadius + 1 - i, 0, calcRadius + 1), EnumInvisibleType.COLLISION_ALL);
			}else{
				//-x
				setUtilityBlock(worldObj, habitatPos.add(-calcRadius, 0, -calcRadius + i), EnumInvisibleType.COLLISION_ALL);
				//+x
				setUtilityBlock(worldObj, habitatPos.add(calcRadius + 1, 0, -calcRadius + i), EnumInvisibleType.COLLISION_ALL);
				//-z
				setUtilityBlock(worldObj, habitatPos.add(-calcRadius + i, 0, -calcRadius), EnumInvisibleType.COLLISION_ALL);
				//+z
				setUtilityBlock(worldObj, habitatPos.add(calcRadius + 1 - i, 0, calcRadius + 1), EnumInvisibleType.COLLISION_ALL);
			}
		}

		setLastRadius(habitat, calcRadius);

		return true;
	}

	@Override
	public void RemoveOperator(World worldObj, IFlickerController<?> habitat, boolean powered){
		int radius = 6;
		BlockPos habitatPos = ((TileEntity)habitat).getPos();

		for (int i = 0; i < radius * 2 + 1; ++i){
			//-x
			clearUtilityBlock(worldObj, habitatPos.add(-radius, 0, -radius+i));
			//+x
			clearUtilityBlock(worldObj, habitatPos.add(radius+1, 0, -radius+i));
			//-z
			clearUtilityBlock(worldObj, habitatPos.add(-radius+i, 0, -radius));
			//+z
			clearUtilityBlock(worldObj, habitatPos.add(radius+1-i, 0, +radius + 1));
		}
	}

	@Override
	public Object[] getRecipe(){
		return new Object[]{
				"FWF",
				"ARN",
				"IWI",
				Character.valueOf('F'), "fence",
				Character.valueOf('W'), Blocks.COBBLESTONE_WALL,
				Character.valueOf('A'), new ItemStack(ItemDefs.flickerJar, 1, AffinityRegistry.getIdFor(SkillDefs.ARCANE)),
				Character.valueOf('R'), new ItemStack(ItemDefs.rune, 1, EnumDyeColor.PURPLE.getDyeDamage()),
				Character.valueOf('N'), new ItemStack(ItemDefs.flickerJar, 1, AffinityRegistry.getIdFor(SkillDefs.AIR)),
				Character.valueOf('I'), Blocks.IRON_BARS

		};
	}
}
