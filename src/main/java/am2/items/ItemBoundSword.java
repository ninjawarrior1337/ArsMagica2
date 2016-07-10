package am2.items;

import am2.ArsMagica2;
import am2.api.IBoundItem;
import am2.defs.ItemDefs;
import am2.utils.SpellUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemBoundSword extends ItemSword implements IBoundItem {

	public ItemBoundSword() {
		super(ItemDefs.BOUND);
		this.maxStackSize = 1;
		this.setMaxDamage(0);
		this.setCreativeTab(CreativeTabs.COMBAT);
	}

	public float getStrVsBlock(ItemStack stack, IBlockState state) {
		Block block = state.getBlock();

		if (block == Blocks.WEB) {
			return 15.0F;
		} else {
			Material material = state.getMaterial();
			return material != Material.PLANTS && material != Material.VINE && material != Material.CORAL && material != Material.LEAVES && material != Material.GOURD ? 1.0F : 1.5F;
		}
	}

	@SuppressWarnings("deprecation")
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
		if (!stack.hasTagCompound())
			return true;
		ItemStack copiedStack = SpellUtils.merge(stack.copy());
		copiedStack.getTagCompound().getCompoundTag("AM2").setInteger("CurrentGroup", SpellUtils.currentStage(stack) + 1);
		copiedStack.setItem(ItemDefs.spell);
		int hurtResist = target.hurtResistantTime;
		target.hurtResistantTime = 0;
		SpellUtils.applyStackStage(copiedStack, attacker, target, target.posX, target.posY, target.posZ, null, attacker.worldObj, true, true, 0);
		target.hurtResistantTime = hurtResist;
		return true;
	}

	public boolean canHarvestBlock(IBlockState blockIn) {
		return blockIn.getBlock() == Blocks.WEB || blockIn.getBlock().getHarvestTool(blockIn).equalsIgnoreCase("sword");
	}

	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
		return false;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onDroppedByPlayer(ItemStack item, EntityPlayer player) {
		item.setItem(ItemDefs.spell);
		return false;
	}

	@Override
	public float maintainCost(EntityPlayer player, ItemStack stack) {
		return normalMaintain;
	}

	public ItemSword registerAndName(String name) {
		this.setUnlocalizedName(name);
		GameRegistry.register(this, new ResourceLocation("arsmagica2", name));
		ArsMagica2.proxy.items.add(this);
		return this;
	}

}
