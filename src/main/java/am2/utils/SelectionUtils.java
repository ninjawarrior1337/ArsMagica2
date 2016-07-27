package am2.utils;

import java.util.ArrayList;

import com.mojang.authlib.GameProfile;

import am2.LogHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class SelectionUtils {
	
	public static int[] getRuneSet(EntityPlayer player) {
		GameProfile profile = player.getGameProfile();
		int runeCombo = profile.getName().hashCode() & 0xfffff;
		int numRunes = 0;
		for (int i = 0; i <= 16; ++i){
			int bit = 1 << i;
			if ((runeCombo & bit) == bit){
				numRunes++;
			}
		}
		int currentAdded = 0;
		int[] runes = new int[numRunes];
		for (int i = 0; i <= 16; ++i) {
			int bit = 1 << i;
			if ((runeCombo & bit) == bit) {
				runes[currentAdded] = i;
				currentAdded++;
			}
		}
		return runes;
	}
	
	public static ArrayList<EntityPlayer> getPlayersForRuneSet (int[] runes) {
		MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
		GameProfile[] profiles = server.getPlayerList().getAllProfiles();
		for (GameProfile profile : profiles) {
			EntityPlayer player = server.getEntityWorld().getPlayerEntityByUUID(profile.getId());
			if (player == null) {
				LogHelper.error("Missing player " + profile.getName());
				continue;
			}
			
		}
		
		
		return new ArrayList<>();
	}
}
