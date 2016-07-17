package am2.affinity.abilities;

import am2.api.affinity.AbstractAffinityAbility;
import am2.api.affinity.Affinity;
import am2.defs.BindingsDefs;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;

public class AbilityEnderTeleport extends AbstractAffinityAbility {

	public AbilityEnderTeleport() {
		super(new ResourceLocation("arsmagica2", "enderteleport"));
	}

	@Override
	protected float getMinimumDepth() {
		return 1.0f;
	}

	@Override
	protected Affinity getAffinity() {
		return Affinity.ENDER;
	}
	
	@Override
	public KeyBinding getKey() {
		return BindingsDefs.ENDER_TP;
	}
	
	@Override
	public void apply(EntityPlayer player) {
		Vec3d playerPos = new Vec3d(player.posX, player.posY + player.getEyeHeight(), player.posZ);
		RayTraceResult result = player.worldObj.rayTraceBlocks(playerPos, playerPos.add(new Vec3d(player.getLookVec().xCoord * 32, player.getLookVec().yCoord * 32, player.getLookVec().zCoord * 32)));
		if (result != null) {
			EnderTeleportEvent event = new EnderTeleportEvent(player, result.hitVec.xCoord, result.hitVec.yCoord, result.hitVec.zCoord, 0.0f);
			if (MinecraftForge.EVENT_BUS.post(event)) return;
			double posY = event.getTargetY();
			while (!player.worldObj.isAirBlock(new BlockPos(event.getTargetX(), posY, event.getTargetZ())))
				posY++;
			player.setPosition(event.getTargetX(), posY, event.getTargetZ());
			player.fallDistance = 0;
		}
	}

}
