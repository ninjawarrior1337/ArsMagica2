package am2.lore;

import am2.gui.GuiArcaneCompendium;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;

public class CompendiumEntryBlock extends CompendiumEntry{

	protected IBlockState state;
	
	public CompendiumEntryBlock(String id, IBlockState state, String... related){
		super(CompendiumEntryTypes.instance.BLOCK, id, related);
		this.state = state;
	}

	@Override
	public GuiArcaneCompendium getCompendiumGui() {
		return new GuiArcaneCompendium(this.id, state);
	}
	
	@Override
	public ItemStack getRepresentStack() {
		return new ItemStack(state.getBlock(), 1, state.getBlock().getMetaFromState(state));
	}
	

//	@Override
//	@SideOnly(Side.CLIENT)
//	protected GuiArcaneCompendium getCompendiumGui(String searchID, int meta){
//
//		if (this.id.equals("stonebricksmooth@3")){
//			return new GuiArcaneCompendium(this.id, Blocks.STONEBRICK, 3);
//		}else if (searchID.equals("wakebloom")){
//			return new GuiArcaneCompendium(searchID, Item.getItemFromBlock(BlockDefs.wakebloom), 0);
//		}
//
//		String[] idSplit = searchID.split(":");
//		if (idSplit.length == 2){
//			Item item = GameRegistry.findItem(idSplit[0], idSplit[1]);
//			if (item != null){
//				if (meta == -1)
//					return new GuiArcaneCompendium(item);
//				else
//					return new GuiArcaneCompendium(searchID + "@" + meta, item, meta);
//			}
//		}else{
//			for (Item item : ArsMagica2.instance.proxy.items.getArsMagicaItems()){
//				if (item.getUnlocalizedName() == null) continue;
//				String itemID = item.getUnlocalizedName().replace("item.", "").replace("arsmagica2:", "");
//				if (itemID.equals(searchID)){
//					if (meta == -1)
//						return new GuiArcaneCompendium(item);
//					else
//						return new GuiArcaneCompendium(searchID + "@" + meta, item, meta);
//				}
//			}
//		}
//
//		if (idSplit.length == 2){
//			Block block = GameRegistry.findBlock(idSplit[0], idSplit[1]);
//			if (block != null){
//				if (meta == -1)
//					return new GuiArcaneCompendium(block);
//				else
//					return new GuiArcaneCompendium(searchID + "@" + meta, block, meta);
//			}
//		}else{
//			for (Block block : ArsMagica2.instance.proxy.blocks.getArsMagicaBlocks()){
//				if (block.getUnlocalizedName() == null) continue;
//				String[] split = searchID.split("@");
//				if (block.getUnlocalizedName().replace("arsmagica2:", "").replace("tile.", "").equals(split[0])){
//					if (meta == -1)
//						return new GuiArcaneCompendium(block);
//					else
//						return new GuiArcaneCompendium(this.id, block, meta);
//				}
//			}
//		}
//
//		return new GuiArcaneCompendium(searchID);
//	}
//
//	@Override
//	protected void parseEx(Node node){
//	}
//
//	@Override
//	public int compareTo(CompendiumEntry arg0){
//		return 0;
//	}
//
//	@Override
//	public ItemStack getRepresentItemStack(String searchID, int meta){
//		if (this.id.equals("stonebricksmooth@3")){
//			return new ItemStack(Blocks.STONEBRICK, 1, 3);
//		}
//
//		String[] idSplit = searchID.split(":");
//		if (idSplit.length == 2){
//			Item item = GameRegistry.findItem(idSplit[0], idSplit[1]);
//			if (item != null){
//				if (meta == -1)
//					return new ItemStack(item);
//				else
//					return new ItemStack(item, 1, meta);
//			}
//		}else{
//			for (Block block : AMCore.instance.proxy.blocks.getArsMagicaBlocks()){
//				if (block.getUnlocalizedName() == null) continue;
//				String[] split = searchID.split("@");
//				if (block.getUnlocalizedName().replace("arsmagica2:", "").replace("tile.", "").equals(split[0])){
//					if (meta == -1)
//						return new ItemStack(block);
//					else
//						return new ItemStack(block, 1, meta);
//				}
//			}
//		}
//
//		if (idSplit.length == 2){
//			Block block = GameRegistry.findBlock(idSplit[0], idSplit[1]);
//			if (block != null){
//				if (meta == -1)
//					return new ItemStack(block);
//				else
//					return new ItemStack(block, 1, meta);
//			}
//		}else{
//			for (Item item : ArsMagica2.instance.proxy.items.getArsMagicaItems()){
//				if (item.getUnlocalizedName() == null) continue;
//				String itemID = item.getUnlocalizedName().replace("item.", "").replace("arsmagica2:", "");
//				if (itemID.equals(searchID)){
//					if (meta == -1)
//						return new ItemStack(item);
//					else
//						return new ItemStack(item, 1, meta);
//				}
//			}
//		}
//		return null;
//	}
}
