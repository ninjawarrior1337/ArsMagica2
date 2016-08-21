package am2.items;

import java.util.List;

import am2.ArsMagica2;
import am2.blocks.BlockInvisibleUtility;
import am2.defs.BlockDefs;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;

@SuppressWarnings("deprecation")
public class ItemCandle extends ItemArsMagica{

	private static final int radius = 10;
	private static final int short_radius = 5;
	private static final float immediate_radius = 2.5f;

	public ItemCandle(){
		super();
		setMaxStackSize(1);
		setMaxDamage(18000); //15 minutes (20 * 60 * 15)
	}
	
	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		return super.onItemUse(stack, playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ);
	}
	
//	@Override
//	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ){
//
//		if (!stack.hasTagCompound() || !stack.getTagCompound().hasKey("search_block")){
//			Block block = world.getBlock(x, y, z);
//			if (player.isSneaking() && block != null && block.getBlockHardness(world, x, y, z) > 0f && world.getTileEntity(x, y, z) == null){
//				if (!world.isRemote){
//					setSearchBlock(world.getBlock(x, y, z), world.getBlockMetadata(x, y, z), stack);
//					world.setBlockToAir(x, y, z);
//				}else{
//					AMParticle particle = (AMParticle)AMCore.proxy.particleManager.spawn(world, "radiant", x + 0.5, y + 0.5, z + 0.5);
//					if (particle != null){
//						particle.AddParticleController(new ParticleHoldPosition(particle, 20, 1, false));
//						particle.setRGBColorF(0, 0.5f, 1);
//					}
//				}
//				return true;
//			}
//		}
//
//		if (!world.isRemote){
//
//			if (stack.hasTagCompound() && stack.getTagCompound().hasKey("search_block")){
//				player.addChatMessage(new ChatComponentText(I18n.translateToLocal("am2.tooltip.candlecantplace")));
//				return false;
//			}
//
//			switch (side){
//			case 0:
//				y--;
//				break;
//			case 1:
//				y++;
//				break;
//			case 2:
//				z--;
//				break;
//			case 3:
//				z++;
//				break;
//			case 4:
//				x--;
//				break;
//			case 5:
//				x++;
//				break;
//			}
//
//			Block block = world.getBlock(x, y, z);
//			if (block == null || block.isReplaceable(world, x, y, z)){
//				int newMeta = (int)Math.ceil(stack.getItemDamage() / 1200);
//				world.setBlock(x, y, z, Blockdef.candle, newMeta, 2);
//				if (!player.capabilities.isCreativeMode)
//					player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
//			}
//			return true;
//		}
//		return false;
//	}

	public void setSearchBlock(Block block, int meta, ItemStack item){
		if (!item.hasTagCompound())
			item.setTagCompound(new NBTTagCompound());

		setFlameColor(item, 0, 1, 0);
		item.getTagCompound().setInteger("search_block", Block.getIdFromBlock(block));
		item.getTagCompound().setInteger("search_meta", meta);
	}

	private void setFlameColor(ItemStack stack, float r, float g, float b){
		if (!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());

		stack.getTagCompound().setFloat("flame_red", r);
		stack.getTagCompound().setFloat("flame_green", g);
		stack.getTagCompound().setFloat("flame_blue", b);
	}

	public void search(EntityPlayer player, ItemStack stack, World world, BlockPos pos, Block block, int meta){

		boolean found = false;

		for (int i = -radius; i <= radius; ++i){
			for (int j = -1; j <= 1; ++j){
				for (int k = -radius; k <= radius; ++k){
					Block f_block = world.getBlockState(pos.add(i, j, k)).getBlock();
					int f_meta = f_block.getMetaFromState(world.getBlockState(pos.add(i, j, k)));

					if (block == f_block && (meta == Short.MAX_VALUE || meta == f_meta)){
						if (Math.abs(i) <= immediate_radius && Math.abs(k) <= immediate_radius){// && player.getCurrentArmor(3) != null && ArmorHelper.isInfusionPreset(player.getCurrentArmor(3), GenericImbuement.pinpointOres)){
							setFlameColor(stack, 0, 0, 0);
						}else if (Math.abs(i) <= short_radius && Math.abs(k) <= short_radius){
							setFlameColor(stack, 1, 0, 0);
							return;
						}else{
							setFlameColor(stack, 0, 0.5f, 1f);
							found = true;
						}
					}
				}
			}
		}

		if (!found)
			setFlameColor(stack, 0, 1, 0);
	}

	@Override
	public boolean getShareTag(){
		return true;
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int indexInInventory, boolean isCurrentlyHeld){
		if (isCurrentlyHeld && entity instanceof EntityPlayer){
			if (!world.isRemote && stack.hasTagCompound() && stack.getItemDamage() % 40 == 0){
				search((EntityPlayer)entity, stack, world,
						entity.getPosition(),
						Block.getBlockById(stack.getTagCompound().getInteger("search_block")),
						stack.getTagCompound().getInteger("search_meta"));
			}
			stack.damageItem(1, (EntityPlayer)entity);
			if (!world.isRemote && stack.getItemDamage() >= this.getMaxDamage())
				((EntityPlayer)entity).inventory.setInventorySlotContents(indexInInventory, null);
			if (!world.isRemote && ArsMagica2.config.candlesAreRovingLights() &&
					world.isAirBlock(entity.getPosition()) &&
					world.getLightFor(EnumSkyBlock.BLOCK, entity.getPosition()) < 14){
				world.setBlockState(entity.getPosition(), BlockDefs.invisibleUtility.getDefaultState().withProperty(BlockInvisibleUtility.TYPE, BlockInvisibleUtility.EnumInvisibleType.HIGH_ILLUMINATED), 2);
			}
		}
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack){
		String name = I18n.translateToLocal("item.arsmagica2:warding_candle.name");
		if (stack.hasTagCompound() && stack.getTagCompound().hasKey("search_block")){
			ItemStack blockStack = new ItemStack(Block.getBlockById(stack.getTagCompound().getInteger("search_block")), 1, stack.getTagCompound().getInteger("search_meta"));
			Item tempItem = blockStack.getItem();
			if(tempItem == null){
				name += " (" + stack.getTagCompound().getInteger("search_block") + ":" + stack.getTagCompound().getInteger("search_meta") + ")";
			}
			else{
				name += " (" + blockStack.getDisplayName() + ")";
			}
		}else{
			name += " (" + I18n.translateToLocal("am2.tooltip.unattuned") + ")";
		}

		return name;
	}

	@Override
	public boolean getHasSubtypes(){
		return true;
	}

	@Override
	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List<ItemStack> par3List){
		ItemStack unattuned = new ItemStack(this, 1, 0);
		par3List.add(unattuned);
	}
}

