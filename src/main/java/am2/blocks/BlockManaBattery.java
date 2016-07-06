package am2.blocks;

import java.util.ArrayList;
import java.util.List;

import am2.ArsMagica2;
import am2.blocks.tileentity.TileEntityManaBattery;
import am2.entity.EntityDummyCaster;
import am2.power.PowerNodeRegistry;
import am2.power.PowerTypes;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockManaBattery extends BlockAMPowered{

	public BlockManaBattery(){
		super(Material.IRON);
		this.setHardness(2.0f);
		this.setResistance(2.0f);
	}
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ){
		if (!super.onBlockActivated(worldIn, pos, state, playerIn, hand, heldItem, side, hitX, hitY, hitZ))
			return true;

		if (worldIn.isRemote){
			TileEntityManaBattery te = getTileEntity(worldIn, pos);
			if (te != null){
				if (ArsMagica2.config.colourblindMode()){
					playerIn.addChatMessage(new TextComponentString(String.format("Charge Level: %.2f %% [%s]", PowerNodeRegistry.For(worldIn).getPower(te, te.getPowerType()) / te.getCapacity() * 100, getColorNameFromPowerType(te.getPowerType()))));
				}else{
					playerIn.addChatMessage(new TextComponentString(String.format("Charge Level: %s%.2f \u00A7f%%", te.getPowerType().getChatColor(), PowerNodeRegistry.For(worldIn).getPower(te, te.getPowerType()) / te.getCapacity() * 100)));
				}
			}
		}

		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World var1, int i){
		return new TileEntityManaBattery();
	}

	private TileEntityManaBattery getTileEntity(World world, BlockPos pos){
		TileEntity te = world.getTileEntity(pos);
		if (te != null && te instanceof TileEntityManaBattery){
			return (TileEntityManaBattery)te;
		}
		return null;
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		if (stack != null){
			TileEntityManaBattery te = getTileEntity(worldIn, pos);
			if (stack.getTagCompound() != null){
				if (stack.getTagCompound().hasKey("mana_battery_charge") && stack.getTagCompound().hasKey("mana_battery_powertype"))
					PowerNodeRegistry.For(worldIn).setPower(te, PowerTypes.getByID(stack.getTagCompound().getInteger("mana_battery_powertype")), stack.getTagCompound().getFloat("mana_battery_charge"));
				else
					te.setPowerType(PowerTypes.NONE, false);
			}

		}
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
	}
	
	@Override
	public void onBlockExploded(World world, BlockPos pos, Explosion explosion) {
		destroy(world, pos);
		super.onBlockExploded(world, pos, explosion);
	}
	
	@Override
	public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te, ItemStack stack) {
		destroy(worldIn, pos);
	}
	
	@Override
	public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
		destroy(worldIn, pos);
		super.onBlockHarvested(worldIn, pos, state, player);
	}

	private void destroy(World world, BlockPos pos){
		TileEntityManaBattery te = getTileEntity(world, pos);
		if (te != null && !world.isRemote){
			float f = world.rand.nextFloat() * 0.8F + 0.1F;
			float f1 = world.rand.nextFloat() * 0.8F + 0.1F;
			float f2 = world.rand.nextFloat() * 0.8F + 0.1F;
			int dmg = (int)((PowerNodeRegistry.For(world).getPower(te, te.getPowerType()) / te.getCapacity()) * 100);
			if (dmg == 0) dmg = 1;
			ItemStack stack = new ItemStack(this);
			stack.damageItem(stack.getMaxDamage() - dmg, new EntityDummyCaster(world));
			stack.setTagCompound(new NBTTagCompound());
			stack.getTagCompound().setFloat("mana_battery_charge", PowerNodeRegistry.For(world).getPower(te, te.getPowerType()));
			stack.getTagCompound().setInteger("mana_battery_powertype", te.getPowerType().ID());

			if (!stack.getTagCompound().hasKey("Lore"))
				stack.getTagCompound().setTag("Lore", new NBTTagList());

			NBTTagList tagList = new NBTTagList();
			PowerTypes powerType = te.getPowerType();
			float amt = PowerNodeRegistry.For(world).getPower(te, powerType);
			tagList.appendTag(new NBTTagString(String.format("Contains %.2f %s%s etherium", amt, powerType.getChatColor(), powerType.name())));
			stack.getTagCompound().setTag("Lore", tagList);

			EntityItem entityitem = new EntityItem(world, pos.getX() + f, pos.getY() + f1, pos.getZ() + f2, stack);
			float f3 = 0.05F;
			entityitem.motionX = (float)world.rand.nextGaussian() * f3;
			entityitem.motionY = (float)world.rand.nextGaussian() * f3 + 0.2F;
			entityitem.motionZ = (float)world.rand.nextGaussian() * f3;
			world.spawnEntityInWorld(entityitem);
		}
	}
	
	@Override
	public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos) {
		TileEntityManaBattery batt = getTileEntity(worldIn, pos);
		if (batt == null)
			return 0;

		//can simply use getHighest, as batteries can only have *one* type. 
		//the only time they have more, is when they are at zero, but then it doesn't matter
		//as all power types are zero.
		//Once they get power a single time, they lock to that power type.
		float pct = PowerNodeRegistry.For(worldIn).getHighestPower(batt) / batt.getCapacity();

		return (int)Math.floor(15.0f * pct);
	}
	
	@Override
	public boolean hasComparatorInputOverride(IBlockState state) {
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, List par3List){
		ItemStack stack = new ItemStack(this);
		stack.setTagCompound(new NBTTagCompound());
		stack.getTagCompound().setFloat("mana_battery_charge", new TileEntityManaBattery().getCapacity());

		par3List.add(stack);
	}

//	@Override
//	public int colorMultiplier(IBlockAccess blockAccess, int x, int y, int z){
//		TileEntity te = blockAccess.getTileEntity(x, y, z);
//		if (te instanceof TileEntityManaBattery){
//			TileEntityManaBattery battery = (TileEntityManaBattery)te;
//			if (battery.getPowerType() == PowerTypes.DARK)
//				return 0x850e0e;
//			else if (battery.getPowerType() == PowerTypes.LIGHT)
//				return 0x61cfc3;
//			else if (battery.getPowerType() == PowerTypes.NEUTRAL)
//				return 0x2683d2;
//			else
//				return 0xFFFFFF;
//		}
//		return 0xFFFFFF;
//	}
	
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		return new ArrayList<>();
	}
}
