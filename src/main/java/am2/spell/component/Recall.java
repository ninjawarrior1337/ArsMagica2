package am2.spell.component;

import java.util.Random;
import java.util.Set;

import com.google.common.collect.Sets;

import am2.affinity.Affinity;
import am2.api.extensions.IEntityExtension;
import am2.defs.SkillDefs;
import am2.extensions.EntityExtension;
import am2.spell.IComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

@SuppressWarnings("deprecation")
public class Recall implements IComponent{

	@Override
	public Object[] getRecipe() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void encodeBasicData(NBTTagCompound tag, Object[] recipe) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean applyEffectBlock(ItemStack stack, World world,
			BlockPos blockPos, EnumFacing blockFace, double impactX,
			double impactY, double impactZ, EntityLivingBase caster) {
		return false;
	}

	@Override
	public boolean applyEffectEntity(ItemStack stack, World world,
			EntityLivingBase caster, Entity target) {
		IEntityExtension ext = EntityExtension.For(caster);
		if (ext.getMarkDimensionID() == -512) {
			if (caster instanceof EntityPlayer) {
				((EntityPlayer)caster).addChatMessage(new TextComponentString(I18n.translateToLocal("tooltip.nomarkset")));
			}
			return false;
		}
		if (ext.getMarkDimensionID() != target.worldObj.provider.getDimension()) {
			if (caster instanceof EntityPlayer) {
				((EntityPlayer)caster).addChatMessage(new TextComponentString(I18n.translateToLocal("tooltip.markwrongdim")));
			}
			return false;	
		}
		target.setPosition(ext.getMarkX(), ext.getMarkY(), ext.getMarkZ());
		return false;
	}

	@Override
	public float manaCost(EntityLivingBase caster) {
		// TODO Auto-generated method stub
		return 500;
	}

	@Override
	public ItemStack[] reagents(EntityLivingBase caster) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void spawnParticles(World world, double x, double y, double z,
			EntityLivingBase caster, Entity target, Random rand,
			int colorModifier) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Set<Affinity> getAffinity() {
		return Sets.newHashSet(SkillDefs.ARCANE);
	}

	@Override
	public float getAffinityShift(Affinity affinity) {
		return 0.1F;
	}

}
