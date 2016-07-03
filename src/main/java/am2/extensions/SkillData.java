package am2.extensions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import am2.api.SkillPointRegistry;
import am2.api.SkillRegistry;
import am2.api.SpellRegistry;
import am2.api.extensions.ISkillData;
import am2.defs.SkillDefs;
import am2.skill.Skill;
import am2.skill.SkillPoint;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class SkillData implements ISkillData, ICapabilityProvider, ICapabilitySerializable<NBTBase> {
	
	private EntityPlayer player;
	public static final ResourceLocation ID = new ResourceLocation("arsmagica2:SkillData");
	
	@CapabilityInject(value=ISkillData.class)
	public static Capability<ISkillData> INSTANCE = null;
	
	public static ISkillData For(EntityLivingBase living) {
		return living.getCapability(INSTANCE, null);
	}
	
	public HashMap<Skill, Boolean> getSkills() {
		return player.getDataManager().get(DataDefinitions.SKILL);
	}
	
	public boolean hasSkill (String name) {
		Boolean bool = player.getDataManager().get(DataDefinitions.SKILL).get(SkillRegistry.getSkillFromName(name));
		return bool == null ? false : bool;
	}
	
	public void unlockSkill (String name) {
		HashMap<Skill, Boolean> map = player.getDataManager().get(DataDefinitions.SKILL);
		map.put(SkillRegistry.getSkillFromName(name), true);
		player.getDataManager().set(DataDefinitions.SKILL, map);
	}
	
	public HashMap<SkillPoint, Integer> getSkillPoints() {
		return player.getDataManager().get(DataDefinitions.POINT_TIER);
	}
	
	public int getSkillPoint(SkillPoint skill) {
		return player.getDataManager().get(DataDefinitions.POINT_TIER).get(skill);
	}
	
	public void setSkillPoint(SkillPoint point, int num) {
		HashMap<SkillPoint, Integer> map = player.getDataManager().get(DataDefinitions.POINT_TIER);
		map.put(point, num);
		player.getDataManager().set(DataDefinitions.POINT_TIER, map);
	}

	public void init(EntityPlayer entity) {
		this.player = entity;
		HashMap<Skill, Boolean> skillMap = new HashMap<Skill, Boolean>();
		HashMap<SkillPoint, Integer> pointMap = new HashMap<SkillPoint, Integer>();
		for (Skill aff : SkillRegistry.getSkillMap().values()) {
			skillMap.put(aff, false);
		}
		for (SkillPoint aff : SkillPointRegistry.getSkillPointMap().values()) {
			pointMap.put(aff, 0);
		}
		pointMap.put(SkillDefs.SKILL_POINT_0, 3);
		player.getDataManager().register(DataDefinitions.SKILL, skillMap);
		player.getDataManager().register(DataDefinitions.POINT_TIER, pointMap);
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == INSTANCE;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == INSTANCE)
			return (T) this;
		return null;
	}
	
	@Override
	public NBTBase serializeNBT() {
		return new ISkillData.Storage().writeNBT(INSTANCE, this, null);
	}

	@Override
	public void deserializeNBT(NBTBase nbt) {
		new ISkillData.Storage().readNBT(INSTANCE, this, null, nbt);
	}

	@Override
	public boolean canLearn(String name) {
		if (SkillRegistry.getSkillFromName(name) == null) return false;
		for (String skill : SkillRegistry.getSkillFromName(name).getParents()) {
			Skill s = SkillRegistry.getSkillFromName(skill);
			if (s == null) continue;
			if (hasSkill(skill)) continue;
			return false;
		}
		return true;
	}

	@Override
	public ArrayList<String> getKnownShapes() {
		ArrayList<String> out = new ArrayList<>();
		for (Entry<Skill, Boolean> entry : getSkills().entrySet()) {
			if (entry.getValue() && SpellRegistry.getShapeFromName(entry.getKey().getID()) != null)
				out.add(entry.getKey().getID());
		}
		return out;
	}

	@Override
	public ArrayList<String> getKnownComponents() {
		ArrayList<String> out = new ArrayList<>();
		for (Entry<Skill, Boolean> entry : getSkills().entrySet()) {
			if (entry.getValue() && SpellRegistry.getComponentFromName(entry.getKey().getID()) != null)
				out.add(entry.getKey().getID());
		}
		return out;
	}

	@Override
	public ArrayList<String> getKnownModifiers() {
		ArrayList<String> out = new ArrayList<>();
		for (Entry<Skill, Boolean> entry : getSkills().entrySet()) {
			if (entry.getValue() && SpellRegistry.getModifierFromName(entry.getKey().getID()) != null)
				out.add(entry.getKey().getID());
		}
		return out;
	}

}
