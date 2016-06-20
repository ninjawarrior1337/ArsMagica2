package am2.proxy;

import static am2.defs.IDDefs.OCCULUS_GUI_ID;
import static am2.defs.IDDefs.RIFT_GUI_ID;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.common.collect.ImmutableMap;

import am2.ArsMagica2;
import am2.container.ContainerRiftStorage;
import am2.entity.EntityRiftStorage;
import am2.entity.EntitySpellEffect;
import am2.entity.EntitySpellProjectile;
import am2.extensions.RiftStorage;
import am2.particles.ParticleManagerServer;
import am2.proxy.tick.ServerTickHandler;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.registry.EntityRegistry;


public class CommonProxy implements IGuiHandler{

	public ParticleManagerServer particleManager;
	private ServerTickHandler serverTickHandler;
	private HashMap<EntityLivingBase, ArrayList<PotionEffect>> deferredPotionEffects;
	private HashMap<EntityLivingBase, Integer> deferredDimensionTransfers;
	public ArrayList<Item> items;


	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		System.out.println("Running");
		switch (ID) {
		case OCCULUS_GUI_ID: return null;
		case RIFT_GUI_ID: return new ContainerRiftStorage(player, RiftStorage.For(player));
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		return null;
	}

	public void preInit() {
		serverTickHandler = new ServerTickHandler();
		MinecraftForge.EVENT_BUS.register(serverTickHandler);
		particleManager = new ParticleManagerServer();
		EntityRegistry.registerModEntity(EntitySpellProjectile.class, "SpellProjectile", 0, ArsMagica2.instance, 80, 1, false);
		EntityRegistry.registerModEntity(EntityRiftStorage.class, "RiftStorage", 1, ArsMagica2.instance, 80, 1, false);
		EntityRegistry.registerModEntity(EntitySpellEffect.class, "SpellEffect", 2, ArsMagica2.instance, 80, 1, false);
	}
	
	public void addDeferredTargetSet(EntityLiving ent, EntityLivingBase target){
		serverTickHandler.addDeferredTarget(ent, target);
	}
	
	public ImmutableMap<EntityLivingBase, ArrayList<PotionEffect>> getDeferredPotionEffects(){
		return ImmutableMap.copyOf(deferredPotionEffects);
	}
	
	public void clearDeferredPotionEffects(){
		deferredPotionEffects.clear();
	}
	
	public void clearDeferredDimensionTransfers(){
		deferredDimensionTransfers.clear();
	}

	public ImmutableMap<EntityLivingBase, Integer> getDeferredDimensionTransfers(){
		return ImmutableMap.copyOf(deferredDimensionTransfers);
	}

	public void unlockCompendiumEntry(String id) {
		
	}

	public void unlockCompendiumCategory(String id) {
		
	}


}
