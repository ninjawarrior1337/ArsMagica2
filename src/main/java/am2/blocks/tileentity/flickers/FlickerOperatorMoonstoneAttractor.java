package am2.blocks.tileentity.flickers;

import java.util.ArrayList;

import am2.affinity.Affinity;
import am2.api.AffinityRegistry;
import am2.api.flickers.IFlickerController;
import am2.api.flickers.IFlickerFunctionality;
import am2.api.math.AMVector3;
import am2.blocks.BlockArsMagicaOre;
import am2.defs.BlockDefs;
import am2.defs.ItemDefs;
import am2.defs.SkillDefs;
import am2.utils.AffinityShiftUtils;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class FlickerOperatorMoonstoneAttractor implements IFlickerFunctionality{

	private static final ArrayList<AMVector3> attractors = new ArrayList<AMVector3>();

	public static AMVector3 getMeteorAttractor(AMVector3 target){
		for (AMVector3 attractor : attractors.toArray(new AMVector3[attractors.size()])){
			if (attractor.distanceSqTo(target) <= 16384)
				return attractor.copy();
		}
		return null;
	}

	@Override
	public boolean RequiresPower(){
		return true;
	}

	@Override
	public int PowerPerOperation(){
		return 10;
	}

	@Override
	public boolean DoOperation(World worldObj, IFlickerController<?> habitat, boolean powered){
		AMVector3 vec = new AMVector3((TileEntity)habitat);
		if (powered){
			if (!attractors.contains(vec)){
				attractors.add(vec);
			}
			return true;
		}else{
			attractors.remove(vec);
		}
		return false;
	}

	@Override
	public boolean DoOperation(World worldObj, IFlickerController<?> habitat, boolean powered, Affinity[] flickers){
		return DoOperation(worldObj, habitat, powered);
	}

	@Override
	public void RemoveOperator(World worldObj, IFlickerController<?> habitat, boolean powered){
		AMVector3 vec = new AMVector3((TileEntity)habitat);
		attractors.remove(vec);
	}

	@Override
	public int TimeBetweenOperation(boolean powered, Affinity[] flickers){
		return 100;
	}

	@Override
	public void RemoveOperator(World worldObj, IFlickerController<?> habitat, boolean powered, Affinity[] flickers){
		RemoveOperator(worldObj, habitat, powered);
	}

	@Override
	public Object[] getRecipe(){
		return new Object[]{
				"RLR",
				"AME",
				"I T",
				Character.valueOf('R'), new ItemStack(ItemDefs.rune, 1, EnumDyeColor.ORANGE.getDyeDamage()),
				Character.valueOf('L'), new ItemStack(ItemDefs.flickerJar, 1, AffinityRegistry.getIdFor(SkillDefs.LIGHTNING)),
				Character.valueOf('A'), new ItemStack(ItemDefs.flickerJar, 1, AffinityRegistry.getIdFor(SkillDefs.ARCANE)),
				Character.valueOf('E'), new ItemStack(ItemDefs.flickerJar, 1, AffinityRegistry.getIdFor(SkillDefs.EARTH)),
				Character.valueOf('M'), new ItemStack(BlockDefs.ores, 1, BlockArsMagicaOre.EnumOreType.MOONSTONE.ordinal()),
				Character.valueOf('I'), AffinityShiftUtils.getEssenceForAffinity(SkillDefs.AIR),
				Character.valueOf('T'), AffinityShiftUtils.getEssenceForAffinity(SkillDefs.EARTH)
		};
	}

}
