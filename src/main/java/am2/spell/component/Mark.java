package am2.spell.component;

import java.util.Random;
import java.util.Set;

import com.google.common.collect.Sets;

import am2.ArsMagica2;
import am2.api.affinity.Affinity;
import am2.api.spell.SpellComponent;
import am2.defs.ItemDefs;
import am2.extensions.EntityExtension;
import am2.particles.AMParticle;
import am2.particles.ParticleConverge;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Mark extends SpellComponent{

	@Override
	public boolean applyEffectBlock(ItemStack stack, World world, BlockPos pos, EnumFacing blockFace, double impactX, double impactY, double impactZ, EntityLivingBase caster){
		EntityExtension.For(caster).setMark(impactX, impactY, impactZ, caster.worldObj.provider.getDimension());
		return true;
	}

	@Override
	public boolean applyEffectEntity(ItemStack stack, World world, EntityLivingBase caster, Entity target){
		if (!(target instanceof EntityLivingBase)){
			return false;
		}
		/*if (ExtendedProperties.For(caster).getMarkSet()){
			ExtendedProperties.For(caster).setNoMarkLocation();
			if (caster instanceof EntityPlayer && world.isRemote){
				((EntityPlayer)caster).addChatMessage("Mark Cleared");
			}
		}else{*/
		EntityExtension.For(caster).setMark(target.posX, target.posY, target.posZ, caster.worldObj.provider.getDimension());
		/*if (caster instanceof EntityPlayer && world.isRemote){
			((EntityPlayer)caster).addChatMessage("Mark Set");
		}*/
		//}
		return true;
	}

	@Override
	public float manaCost(EntityLivingBase caster){
		return 5;
	}

	@Override
	public ItemStack[] reagents(EntityLivingBase caster){
		return null;
	}

	@Override
	public void spawnParticles(World world, double x, double y, double z, EntityLivingBase caster, Entity target, Random rand, int colorModifier){
		int offset = 1;

		SetupParticle(world, caster.posX - 0.5f, caster.posY + offset, caster.posZ, 0.2, 0, colorModifier);
		SetupParticle(world, caster.posX + 0.5f, caster.posY + offset, caster.posZ, -0.2, 0, colorModifier);
		SetupParticle(world, caster.posX, caster.posY + offset, caster.posZ - 0.5f, 0, 0.2, colorModifier);
		SetupParticle(world, caster.posX, caster.posY + offset, caster.posZ + 0.5f, 0, -0.2, colorModifier);
	}

	private void SetupParticle(World world, double x, double y, double z, double motionx, double motionz, int colorModifier){
		AMParticle effect = (AMParticle)ArsMagica2.proxy.particleManager.spawn(world, "symbols", x, y, z);
		if (effect != null){
			effect.AddParticleController(new ParticleConverge(effect, motionx, -0.1, motionz, 1, true));
			effect.setMaxAge(40);
			effect.setIgnoreMaxAge(false);
			effect.setParticleScale(0.1f);
			if (colorModifier > -1){
				effect.setRGBColorF(((colorModifier >> 16) & 0xFF) / 255.0f, ((colorModifier >> 8) & 0xFF) / 255.0f, (colorModifier & 0xFF) / 255.0f);
			}
		}
	}

	@Override
	public Set<Affinity> getAffinity(){
		return Sets.newHashSet(Affinity.NONE);
	}

	@Override
	public Object[] getRecipe(){
		return new Object[]{
				new ItemStack(ItemDefs.rune, 1, EnumDyeColor.RED.getDyeDamage()),
				new ItemStack(Items.MAP, 1, Short.MAX_VALUE)
		};
	}

	@Override
	public float getAffinityShift(Affinity affinity){
		return 0;
	}
	@Override
	public void encodeBasicData(NBTTagCompound tag, Object[] recipe) {
		// TODO Auto-generated method stub
		
	}
}
