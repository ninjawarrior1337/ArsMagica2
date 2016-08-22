package am2.blocks.tileentity;

import am2.entity.EntityDummyCaster;
import am2.extensions.EntityExtension;
import am2.utils.DummyEntityPlayer;
import am2.utils.SpellUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

public class TileEntityGroundRuneSpell extends TileEntity implements ITickable{
	private ItemStack spellStack;
	private EntityPlayer caster;
	private String placedByName;

	private int numTriggers = 1;
	private boolean isPermanent = false;

	public TileEntityGroundRuneSpell(){
		
	}

	public void setSpellStack(ItemStack spellStack){
		this.spellStack = spellStack.copy();
	}
	
	public ItemStack getSpellStack() {
		return spellStack;
	}

	public void setNumTriggers(int triggers){
		this.numTriggers = triggers;
	}

	public int getNumTriggers(){
		return this.numTriggers;
	}

	public void setPermanent(boolean permanent){
		this.isPermanent = permanent;
	}

	public boolean getPermanent(){
		return this.isPermanent;
	}

	private void prepForActivate(){
		if (placedByName != null)
			caster = worldObj.getPlayerEntityByName(placedByName);
		if (caster == null){
			caster = DummyEntityPlayer.fromEntityLiving(new EntityDummyCaster(worldObj));
			EntityExtension.For(caster).setMagicLevelWithMana(99);
		}
	}
	
	public boolean canApply(EntityLivingBase entity) {
		if (spellStack == null) return false;
		prepForActivate();
		if (entity.getName().equals(placedByName)) return false;
		return true;
	}

	public boolean applySpellEffect(EntityLivingBase target){
		if (spellStack == null) return false;
		if (!canApply(target)) return false;
		prepForActivate();
		SpellUtils.applyStackStage(spellStack, caster, target, target.posX, target.posY, target.posZ, null, worldObj, false, false, 0);
		return true;
	}

	public void setPlacedBy(EntityLivingBase caster){
		if (caster instanceof EntityPlayer) this.placedByName = ((EntityPlayer)caster).getName();
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound){
		compound.setString("placedByName", placedByName);
		compound.setTag("spellStack", spellStack.writeToNBT(new NBTTagCompound()));
		compound.setInteger("numTrigger", numTriggers);
		compound.setBoolean("permanent", isPermanent);
		return compound;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound){
		placedByName = compound.getString("placedByName");
		spellStack = ItemStack.loadItemStackFromNBT(compound.getCompoundTag("spellStack"));
		numTriggers = compound.getInteger("numTrigger");
		isPermanent = compound.getBoolean("permanent");
	}
	
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(pos, 0, writeToNBT(new NBTTagCompound()));
	}
	
	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		readFromNBT(pkt.getNbtCompound());
	}

	@Override
	public void update() {
		worldObj.markAndNotifyBlock(pos, worldObj.getChunkFromBlockCoords(pos), worldObj.getBlockState(pos), worldObj.getBlockState(pos), 2);
	}
}
