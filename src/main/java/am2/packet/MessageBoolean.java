package am2.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import am2.extensions.AffinityData;

public class MessageBoolean implements IMessage {
	
	private boolean bool;
	
	public MessageBoolean() {
	}
	
	public MessageBoolean(boolean bool) {
		this.bool = bool;
	}
	
	
	@Override
	public void fromBytes(ByteBuf buf) {
		bool = buf.readBoolean();
	}
	
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeBoolean(bool);		
	}
	
	public static class IceBridgeHandler implements IMessageHandler<MessageBoolean, IMessage> {
		public IceBridgeHandler() {
			
		}
		
		
		@Override
		public IMessage onMessage(final MessageBoolean message, final MessageContext ctx) {
			((WorldServer)ctx.getServerHandler().playerEntity.worldObj).addScheduledTask(new Runnable () {
			//Minecraft.getMinecraft().addScheduledTask(new Runnable() {
				@Override
				public void run() {
					EntityPlayer p = ctx.getServerHandler().playerEntity;
					if (p == null)
						return;
					AffinityData.For(p).setIceBridgeState(message.bool);
				}
			});
			return null;
		}
	}

}
