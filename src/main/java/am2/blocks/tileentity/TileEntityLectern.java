package am2.blocks.tileentity;

import java.util.ArrayList;
import java.util.Random;

import am2.ArsMagica2;
import am2.defs.ItemDefs;
import am2.packet.AMDataWriter;
import am2.packet.AMNetHandler;
import am2.packet.AMPacketIDs;
import am2.particles.AMParticle;
import am2.particles.ParticleFadeOut;
import am2.particles.ParticleMoveOnHeading;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntityEnchantmentTable;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.Loader;

public class TileEntityLectern extends TileEntityEnchantmentTable implements ITickable{
	private ItemStack stack;
	private ItemStack tooltipStack;
	private boolean needsBook;
	private boolean overPowered;
	public int particleAge;
	public int particleMaxAge = 150;
	private boolean increasing = true;
	private Random rand = new Random();

	public TileEntityLectern(){
	}

	public void resetParticleAge(){
		particleAge = 0;
		increasing = true;
	}

	public ItemStack getTooltipStack(){
		return tooltipStack;
	}

	public void setTooltipStack(ItemStack stack){
		this.tooltipStack = stack;
	}

	@Override
	public void update(){
		if (worldObj.isRemote){
			updateBookRender();
			if (tooltipStack != null){
				AMParticle particle = (AMParticle)ArsMagica2.proxy.particleManager.spawn(worldObj, "sparkle", pos.getX() + 0.5 + ((worldObj.rand.nextDouble() * 0.2) - 0.1), pos.getY() + 1, pos.getZ() + 0.5 + ((worldObj.rand.nextDouble() * 0.2) - 0.1));
				if (particle != null){
					particle.AddParticleController(new ParticleMoveOnHeading(particle, worldObj.rand.nextDouble() * 360, -45 - worldObj.rand.nextInt(90), 0.05f, 1, false));
					particle.AddParticleController(new ParticleFadeOut(particle, 2, false).setFadeSpeed(0.05f).setKillParticleOnFinish(true));
					particle.setIgnoreMaxAge(true);
					if (getOverpowered()){
						particle.setRGBColorF(1.0f, 0.2f, 0.2f);
					}
				}
			}
		}
	}

	private void updateBookRender(){
        this.bookSpreadPrev = this.bookSpread;
        this.bookRotationPrev = this.bookRotation;
        EntityPlayer entityplayer = this.worldObj.getClosestPlayer((double)((float)this.pos.getX() + 0.5F), (double)((float)this.pos.getY() + 0.5F), (double)((float)this.pos.getZ() + 0.5F), 3.0D, false);

        if (entityplayer != null)
        {
            double d0 = entityplayer.posX - (double)((float)this.pos.getX() + 0.5F);
            double d1 = entityplayer.posZ - (double)((float)this.pos.getZ() + 0.5F);
            this.tRot = (float)MathHelper.atan2(d1, d0);
            this.bookSpread += 0.1F;

            if (this.bookSpread < 0.5F || rand.nextInt(40) == 0)
            {
                float f1 = this.flipT;

                while (true)
                {
                    this.flipT += (float)(rand .nextInt(4) - rand.nextInt(4));

                    if (f1 != this.flipT)
                    {
                        break;
                    }
                }
            }
        }
        else
        {
            this.tRot += 0.02F;
            this.bookSpread -= 0.1F;
        }

        while (this.bookRotation >= (float)Math.PI)
        {
            this.bookRotation -= ((float)Math.PI * 2F);
        }

        while (this.bookRotation < -(float)Math.PI)
        {
            this.bookRotation += ((float)Math.PI * 2F);
        }

        while (this.tRot >= (float)Math.PI)
        {
            this.tRot -= ((float)Math.PI * 2F);
        }

        while (this.tRot < -(float)Math.PI)
        {
            this.tRot += ((float)Math.PI * 2F);
        }

        float f2;

        for (f2 = this.tRot - this.bookRotation; f2 >= (float)Math.PI; f2 -= ((float)Math.PI * 2F))
        {
            ;
        }

        while (f2 < -(float)Math.PI)
        {
            f2 += ((float)Math.PI * 2F);
        }

        this.bookRotation += f2 * 0.4F;
        this.bookSpread = MathHelper.clamp_float(this.bookSpread, 0.0F, 1.0F);
        ++this.tickCount;
        this.pageFlipPrev = this.pageFlip;
        float f = (this.flipT - this.pageFlip) * 0.4F;
        float f3 = 0.2F;
        f = MathHelper.clamp_float(f, -f3, f3);
        this.flipA += (f - this.flipA) * 0.9F;
        this.pageFlip += this.flipA;
//		particleAge++;
//		if (increasing){
//			particleMaxAge += 2;
//			if (particleMaxAge - particleAge > 120)
//				increasing = false;
//		}else{
//			if (particleMaxAge - particleAge < 5)
//				increasing = true;
//		}
//
//		this.bookSpreadPrev = this.bookSpread;
//		this.bookRotationPrev = this.bookRotation;
//
//		this.field_145930_m += 0.1F;
//
//		if (this.field_145930_m < 0.5F || worldObj.rand.nextInt(40) == 0){
//			float f1 = this.field_145932_k;
//
//			do{
//				this.field_145932_k += (float)(worldObj.rand.nextInt(4) - worldObj.rand.nextInt(4));
//			}
//			while (f1 == this.field_145932_k);
//		}
//
//		while (this.bookRotation >= (float)Math.PI){
//			this.bookRotation -= ((float)Math.PI * 2F);
//		}
//
//		while (this.bookRotation < -(float)Math.PI){
//			this.bookRotation += ((float)Math.PI * 2F);
//		}
//
//		while (this.field_145924_q >= (float)Math.PI){
//			this.field_145924_q -= ((float)Math.PI * 2F);
//		}
//
//		while (this.field_145924_q < -(float)Math.PI){
//			this.field_145924_q += ((float)Math.PI * 2F);
//		}
//
//		float f2;
//
//		for (f2 = this.field_145924_q - this.bookRotation; f2 >= (float)Math.PI; f2 -= ((float)Math.PI * 2F)){
//			;
//		}
//
//		while (f2 < -(float)Math.PI){
//			f2 += ((float)Math.PI * 2F);
//		}
//
//		this.bookRotation += f2 * 0.4F;
//
//		if (this.field_145930_m < 0.0F){
//			this.field_145930_m = 0.0F;
//		}
//
//		if (this.field_145930_m > 1.0F){
//			this.field_145930_m = 1.0F;
//		}
//
//		++this.field_145926_a;
//		this.field_145931_j = this.field_145933_i;
//		float f = (this.field_145932_k - this.field_145933_i) * 0.4F;
//		float f3 = 0.2F;
//
//		if (f < -f3){
//			f = -f3;
//		}
//
//		if (f > f3){
//			f = f3;
//		}
//
//		this.field_145929_l += (f - this.field_145929_l) * 0.9F;
//		this.field_145933_i += this.field_145929_l;
//
		//TODO:
			/*this.bookSpreadPrev = this.bookSpread;
		this.bookRotationPrev = this.bookRotation2;
		this.bookSpread += 0.1F;

		if (this.bookSpread < 0.5F || rand.nextInt(40) == 0)
		{
			float f = this.field_70373_d;

			do
			{
				this.field_70373_d += rand.nextInt(4) - rand.nextInt(4);
			}
			while (f == this.field_70373_d);
		}

		float f1;

		for (f1 = this.bookRotation - this.bookRotation2; f1 >= (float)Math.PI; f1 -= ((float)Math.PI * 2F))
		{
			;
		}

		while (f1 < -(float)Math.PI)
		{
			f1 += ((float)Math.PI * 2F);
		}

		this.bookRotation2 += f1 * 0.4F;

		if (this.bookSpread < 0.0F)
		{
			this.bookSpread = 0.0F;
		}

		if (this.bookSpread > 1.0F)
		{
			this.bookSpread = 1.0F;
		}

		++this.tickCount;
		this.pageFlipPrev = this.pageFlip;
		float f2 = (this.field_70373_d - this.pageFlip) * 0.4F;
		float f3 = 0.02F;

		if (f2 < -f3)
		{
			f2 = -f3;
		}

		if (f2 > f3)
		{
			f2 = f3;
		}

		this.field_70374_e += (f2 - this.field_70374_e) * 1.1F;
		this.pageFlip += this.field_70374_e;*/
	}
	
	public ItemStack getStack(){
		return stack;
	}

	public boolean setStack(ItemStack stack){
		if (stack == null || getValidItems().contains(stack.getItem())){
			this.stack = stack;
			if (!this.worldObj.isRemote){
				AMDataWriter writer = new AMDataWriter();
				writer.add(pos.getX());
				writer.add(pos.getY());
				writer.add(pos.getZ());
				if (stack == null){
					writer.add(false);
				}else{
					writer.add(true);
					writer.add(stack);
				}
				AMNetHandler.INSTANCE.sendPacketToAllClientsNear(worldObj.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 32, AMPacketIDs.LECTERN_DATA, writer.generate());
			}
			return true;
		}
		return false;
	}

	public boolean hasStack(){
		return stack != null;
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket(){
		NBTTagCompound compound = new NBTTagCompound();
		writeToNBT(compound);
		SPacketUpdateTileEntity packet = new SPacketUpdateTileEntity(pos, getBlockMetadata(), compound);
		return packet;
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt){
		this.readFromNBT(pkt.getNbtCompound());
	}

	private ArrayList<Item> getValidItems(){
		ArrayList<Item> validItems = new ArrayList<Item>();

		validItems.add(Items.WRITTEN_BOOK);
		validItems.add(ItemDefs.arcaneCompendium);

//		if (Loader.isModLoaded("Thaumcraft")){
//			ItemStack item = thaumcraft.api.ItemApi.getItem("itemThaumonomicon", 0);
//			if (item != null){
//				validItems.add(item.getItem());
//			}
//		}

		return validItems;
	}

	@Override
	public void readFromNBT(NBTTagCompound comp){
		super.readFromNBT(comp);
		if (comp.hasKey("placedBook")){
			NBTTagCompound bewk = comp.getCompoundTag("placedBook");
			stack = ItemStack.loadItemStackFromNBT(bewk);
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound comp){
		super.writeToNBT(comp);
		if (stack != null){
			NBTTagCompound bewk = new NBTTagCompound();
			stack.writeToNBT(bewk);
			comp.setTag("placedBook", bewk);
		}
		return comp;
	}

	public void setNeedsBook(boolean b){
		this.needsBook = b;
	}

	public boolean getNeedsBook(){
		return this.needsBook;
	}

	public void setOverpowered(boolean b){
		this.overPowered = b;
	}

	public boolean getOverpowered(){
		return this.overPowered;
	}

}
