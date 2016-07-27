package am2.items;

import java.util.List;

import am2.api.ArsMagicaAPI;
import am2.api.SkillPointRegistry;
import am2.api.skill.SkillPoint;
import am2.extensions.EntityExtension;
import am2.extensions.SkillData;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

@SuppressWarnings("deprecation")
public class ItemInfinityOrb extends ItemArsMagica2 {
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
		SkillPoint point = SkillPointRegistry.getPointForTier(itemStackIn.getItemDamage());
		if (point == null)
			playerIn.addChatMessage(new TextComponentString("Broken Item : Please use a trash bin."));
		itemStackIn = doGiveSkillPoints(playerIn, itemStackIn, point);
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		SkillPoint point = SkillPointRegistry.getPointForTier(stack.getItemDamage());
		if (point == null)
			return "Broken Item : Please use a trash bin.";
		return I18n.translateToLocal("item.infOrb" + point.toString() + ".name");
	}
	
	private ItemStack doGiveSkillPoints(EntityPlayer player, ItemStack stack, SkillPoint type){
		if (EntityExtension.For(player).getCurrentLevel() > 0){
			SkillData.For(player).setSkillPoint(type, SkillData.For(player).getSkillPoint(type) + 1);
			if (player.worldObj.isRemote){
				player.addChatMessage(new TextComponentString(I18n.translateToLocal("am2.tooltip.infOrb" + type.toString())));
			}
			if (!player.capabilities.isCreativeMode)
			stack.stackSize--;
			if (stack.stackSize < 1){
				player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
			}
		}else{
			if (player.worldObj.isRemote){
				int message = player.worldObj.rand.nextInt(10);
				player.addChatMessage(new TextComponentString(I18n.translateToLocal("am2.tooltip.infOrbFail" + message)));
			}
		}
		return stack;
	}
	
	@Override
	public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
		subItems.add(new ItemStack(itemIn, 1, 0));
		subItems.add(new ItemStack(itemIn, 1, 1));
		subItems.add(new ItemStack(itemIn, 1, 2));
		if (ArsMagicaAPI.hasTier4())
			subItems.add(new ItemStack(itemIn, 1, 3));
		if (ArsMagicaAPI.hasTier5())
			subItems.add(new ItemStack(itemIn, 1, 4));
		if (ArsMagicaAPI.hasTier6())
			subItems.add(new ItemStack(itemIn, 1, 5));
	}
}
