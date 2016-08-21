package am2.spell.component;

import java.util.Random;
import java.util.Set;

import com.google.common.collect.Sets;

import am2.ArsMagica2;
import am2.api.affinity.Affinity;
import am2.api.blocks.MultiblockStructureDefinition;
import am2.api.extensions.IEntityExtension;
import am2.api.spell.SpellComponent;
import am2.defs.ItemDefs;
import am2.defs.PotionEffectsDefs;
import am2.extensions.EntityExtension;
import am2.particles.AMParticle;
import am2.particles.ParticleExpandingCollapsingRingAtPoint;
import am2.rituals.IRitualInteraction;
import am2.rituals.RitualShapeHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

@SuppressWarnings("deprecation")
public class Recall extends SpellComponent implements IRitualInteraction{

	@Override
	public boolean applyEffectBlock(ItemStack stack, World world, BlockPos pos, EnumFacing blockFace, double impactX, double impactY, double impactZ, EntityLivingBase caster){
		return false;
	}

	@Override
	public boolean applyEffectEntity(ItemStack stack, World world, EntityLivingBase caster, Entity target){

		if (!(target instanceof EntityLivingBase)){
			return false;
		}

		if (caster.isPotionActive(PotionEffectsDefs.astralDistortion) || ((EntityLivingBase)target).isPotionActive(PotionEffectsDefs.astralDistortion)){
			if (caster instanceof EntityPlayer)
				((EntityPlayer)caster).addChatMessage(new TextComponentString(I18n.translateToLocal("am2.tooltip.cantTeleport")));
			return false;
		}

//		int x = (int)Math.floor(target.posX);
//		int y = (int)Math.floor(target.posY);
//		int z = (int)Math.floor(target.posZ);

//		ItemStack[] ritualRunes = RitualShapeHelper.instance.checkForRitual(this, world, x, y, z, false);
//		if (ritualRunes != null){
//			return handleRitualReagents(ritualRunes, world, x, y, z, caster, target);
//		}

		IEntityExtension casterProperties = EntityExtension.For(caster);
		if (casterProperties.getMarkDimensionID() == -512){
			if (caster instanceof EntityPlayer && !world.isRemote)
				((EntityPlayer)caster).addChatMessage(new TextComponentString(I18n.translateToLocal("am2.tooltip.noMark")));
			return false;
		}else if (casterProperties.getMarkDimensionID() != caster.dimension){
			if (caster instanceof EntityPlayer && !world.isRemote)
				((EntityPlayer)caster).addChatMessage(new TextComponentString(I18n.translateToLocal("am2.tooltip.diffDimMark")));
			return false;
		}
		if (!world.isRemote){
			((EntityLivingBase)target).setPositionAndUpdate(casterProperties.getMarkX(), casterProperties.getMarkY(), casterProperties.getMarkZ());
		}
		return true;
	}

//	private boolean handleRitualReagents(ItemStack[] ritualRunes, World world, int x, int y, int z, EntityLivingBase caster, Entity target){
//
//		boolean hasVinteumDust = false;
//		for (ItemStack stack : ritualRunes){
//			if (stack.getItem() == ItemDefs.itemOre && stack.getItemDamage() == ItemOre.META_VINTEUM){
//				hasVinteumDust = true;
//				break;
//			}
//		}
//
//		if (!hasVinteumDust && ritualRunes.length == 3){
//			//TODO Gateways
////			long key = KeystoneUtilities.instance.getKeyFromRunes(ritualRunes);
////			AMVector3 vector = AMCore.proxy.blocks.getNextKeystonePortalLocation(world, x, y, z, false, key);
////			if (vector == null || vector.equals(new AMVector3(x, y, z))){
////				if (caster instanceof EntityPlayer && !world.isRemote)
////					((EntityPlayer)caster).addChatMessage(new TextComponentString(I18n.translateToLocal("am2.tooltip.noMatchingGate")));
////				return false;
////			}else{
////				RitualShapeHelper.instance.consumeRitualReagents(this, world, x, y, z);
////				RitualShapeHelper.instance.consumeRitualShape(this, world, x, y, z);
////				((EntityLivingBase)target).setPositionAndUpdate(vector.x, vector.y - target.height, vector.z);
////				return true;
////			}
//		}else if (hasVinteumDust){
//			ArrayList<ItemStack> copy = new ArrayList<ItemStack>();
//			for (ItemStack stack : ritualRunes){
//				if (stack.getItem() == ItemDefs.rune && stack.getItemDamage() <= 16){
//					copy.add(stack);
//				}
//			}
//			ItemStack[] newRunes = copy.toArray(new ItemStack[copy.size()]);
//			long key = KeystoneUtilities.instance.getKeyFromRunes(newRunes);
//			EntityPlayer player = EntityUtilities.getPlayerForCombo(world, (int)key);
//			if (player == null){
//				if (caster instanceof EntityPlayer && !world.isRemote)
//					((EntityPlayer)caster).addChatMessage(new TextComponentString("am2.tooltip.noMatchingPlayer"));
//				return false;
//			}else if (player == caster){
//				if (caster instanceof EntityPlayer && !world.isRemote)
//					((EntityPlayer)caster).addChatMessage(new TextComponentString("am2.tooltip.cantSummonSelf"));
//				return false;
//			}else{
//				RitualShapeHelper.instance.consumeRitualReagents(this, world, x, y, z);
//				if (target.worldObj.provider.getDimension() != caster.worldObj.provider.getDimension()){
//					DimensionUtilities.doDimensionTransfer(player, caster.worldObj.provider.getDimension());
//				}
//				((EntityLivingBase)target).setPositionAndUpdate(x, y, z);
//				return true;
//			}
//		}
//		return false;
//	}

	@Override
	public float manaCost(EntityLivingBase caster){
		return 500;
	}
	@Override
	public ItemStack[] reagents(EntityLivingBase caster){
		return null;
	}

	@Override
	public void spawnParticles(World world, double x, double y, double z, EntityLivingBase caster, Entity target, Random rand, int colorModifier){
		for (int i = 0; i < 25; ++i){
			AMParticle particle = (AMParticle)ArsMagica2.proxy.particleManager.spawn(world, "arcane", x, y - 1, z);
			if (particle != null){
				particle.addRandomOffset(1, 0, 1);
				particle.AddParticleController(new ParticleExpandingCollapsingRingAtPoint(particle, x, y - 1, z, 0.1, 3, 0.3, 1, false).setCollapseOnce());
				particle.setMaxAge(20);
				particle.setParticleScale(0.2f);
				if (colorModifier > -1){
					particle.setRGBColorF(((colorModifier >> 16) & 0xFF) / 255.0f, ((colorModifier >> 8) & 0xFF) / 255.0f, (colorModifier & 0xFF) / 255.0f);
				}
			}
		}
	}

	@Override
	public Set<Affinity> getAffinity(){
		return Sets.newHashSet(Affinity.ARCANE);
	}

	@Override
	public Object[] getRecipe(){
		return new Object[]{
				new ItemStack(ItemDefs.rune, 1, EnumDyeColor.ORANGE.getDyeDamage()),
				Items.COMPASS,
				new ItemStack(Items.MAP, 1, Short.MAX_VALUE),
				Items.ENDER_PEARL
		};
	}

	@Override
	public float getAffinityShift(Affinity affinity){
		return 0.1f;
	}

	@Override
	public MultiblockStructureDefinition getRitualShape(){
		return RitualShapeHelper.instance.ringedCross;
	}

	@Override
	public ItemStack[] getReagents(){
		return new ItemStack[]{
				new ItemStack(ItemDefs.rune, 1, OreDictionary.WILDCARD_VALUE),
				new ItemStack(ItemDefs.rune, 1, OreDictionary.WILDCARD_VALUE),
				new ItemStack(ItemDefs.rune, 1, OreDictionary.WILDCARD_VALUE)
		};
	}

	@Override
	public int getReagentSearchRadius(){
		return RitualShapeHelper.instance.ringedCross.getWidth();
	}

	@Override
	public void encodeBasicData(NBTTagCompound tag, Object[] recipe) {
		// TODO Auto-generated method stub
		
	}
}
