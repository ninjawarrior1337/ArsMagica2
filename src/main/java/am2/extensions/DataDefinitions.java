package am2.extensions;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.common.base.Optional;

import am2.ArsMagica2;
import am2.affinity.Affinity;
import am2.api.AffinityRegistry;
import am2.api.SkillPointRegistry;
import am2.api.SkillRegistry;
import am2.skill.Skill;
import am2.skill.SkillPoint;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;

public class DataDefinitions {
	
	public static final MapSerializer<Affinity, Float> AFFINITY_SERIALIZER = new MapSerializer<Affinity, Float>() {
		public Float getValueInstanceFromString(String str) {return Float.valueOf(str);}
		public Affinity getKeyInstanceFromString(String str) {return AffinityRegistry.getAffinityFromName(str);}
	};
		
	public static final MapSerializer<SkillPoint, Integer> SKILL_POINT_SERIALIZER = new MapSerializer<SkillPoint, Integer>() {
		public Integer getValueInstanceFromString(String str) {return Integer.valueOf(str);}
		public SkillPoint getKeyInstanceFromString(String str) {return SkillPointRegistry.fromName(str);}
	};
	
	public static final MapSerializer<Skill, Boolean> SKILL_SERIALIZER = new MapSerializer<Skill, Boolean>() {
		public Boolean getValueInstanceFromString(String str) {return Boolean.valueOf(str);}
		public Skill getKeyInstanceFromString(String str) {return SkillRegistry.getSkillFromName(str);}
	};
	
	public static final ArraySerializer<String> STRING_SERIALIZER = new ArraySerializer<String>() {
		public String getKeyInstanceFromString(String str) {
			System.out.println(str);
			return str;
		}
	};
	
	static {
		DataSerializers.registerSerializer(AFFINITY_SERIALIZER);
		DataSerializers.registerSerializer(SKILL_POINT_SERIALIZER);
		DataSerializers.registerSerializer(SKILL_SERIALIZER);
		DataSerializers.registerSerializer(STRING_SERIALIZER);
	}

	static final DataParameter<Float> CURRENT_MANA = EntityDataManager.<Float>createKey(Entity.class, DataSerializers.FLOAT);
	static final DataParameter<Float> MAX_MANA = EntityDataManager.<Float>createKey(Entity.class, DataSerializers.FLOAT);
	static final DataParameter<Float> CURRENT_MANA_FATIGUE = EntityDataManager.<Float>createKey(Entity.class, DataSerializers.FLOAT);
	static final DataParameter<Float> MAX_MANA_FATIGUE = EntityDataManager.<Float>createKey(Entity.class, DataSerializers.FLOAT);
	static final DataParameter<Integer> CURRENT_LEVEL = EntityDataManager.<Integer>createKey(Entity.class, DataSerializers.VARINT);
	static final DataParameter<Float> CURRENT_XP = EntityDataManager.<Float>createKey(Entity.class, DataSerializers.FLOAT);
	static final DataParameter<Integer> CURRENT_SUMMONS = EntityDataManager.<Integer>createKey(Entity.class, DataSerializers.VARINT);
	static final DataParameter<Integer> HEAL_COOLDOWN = EntityDataManager.<Integer>createKey(Entity.class, DataSerializers.VARINT);
	static final DataParameter<Integer> AFFINITY_HEAL_COOLDOWN = EntityDataManager.<Integer>createKey(Entity.class, DataSerializers.VARINT);
	static final DataParameter<Float> MARK_X = EntityDataManager.<Float>createKey(Entity.class, DataSerializers.FLOAT);
	static final DataParameter<Float> MARK_Y = EntityDataManager.<Float>createKey(Entity.class, DataSerializers.FLOAT);
	static final DataParameter<Float> MARK_Z = EntityDataManager.<Float>createKey(Entity.class, DataSerializers.FLOAT);
	static final DataParameter<Integer> MARK_DIMENSION = EntityDataManager.<Integer>createKey(Entity.class, DataSerializers.VARINT);
	static final DataParameter<Optional<ItemStack>> CONTENGENCY_STACK = EntityDataManager.<Optional<ItemStack>>createKey(Entity.class, DataSerializers.OPTIONAL_ITEM_STACK);
	static final DataParameter<Boolean> IS_SHRUNK = EntityDataManager.<Boolean>createKey(Entity.class, DataSerializers.BOOLEAN);
	static final DataParameter<Boolean> IS_INVERTED = EntityDataManager.<Boolean>createKey(Entity.class, DataSerializers.BOOLEAN);
	static final DataParameter<Boolean> HAS_FALL_PROTECTION = EntityDataManager.<Boolean>createKey(Entity.class, DataSerializers.BOOLEAN);
	static final DataParameter<String> CONTENGENCY_TYPE = EntityDataManager.<String>createKey(Entity.class, DataSerializers.STRING);
	
	static final DataParameter<String> AFFINITY = EntityDataManager.createKey(Entity.class, DataSerializers.STRING);
	static final DataParameter<HashMap<Affinity, Float>> AFFINITY_DATA = EntityDataManager.createKey(Entity.class, AFFINITY_SERIALIZER);
	static final DataParameter<HashMap<SkillPoint, Integer>> POINT_TIER = EntityDataManager.createKey(Entity.class, SKILL_POINT_SERIALIZER);
	static final DataParameter<HashMap<Skill, Boolean>> SKILL = EntityDataManager.createKey(Entity.class, SKILL_SERIALIZER);	
	static final DataParameter<Boolean> ICE_BRIDGE_STATE = EntityDataManager.createKey(Entity.class, DataSerializers.BOOLEAN);
	static final DataParameter<Integer> ICE_SPEED = EntityDataManager.createKey(Entity.class, DataSerializers.VARINT);
	static final DataParameter<Integer> NATURE_SPEED = EntityDataManager.createKey(Entity.class, DataSerializers.VARINT);

	static final DataParameter<Boolean> REVERSE_INPUT = EntityDataManager.<Boolean>createKey(Entity.class, DataSerializers.BOOLEAN);
	static final DataParameter<Boolean> IS_FLIPPED = EntityDataManager.<Boolean>createKey(Entity.class, DataSerializers.BOOLEAN);
	
	static final DataParameter<Float> SHRINK_PCT = EntityDataManager.<Float>createKey(Entity.class, DataSerializers.FLOAT);
	static final DataParameter<Float> PREV_SHRINK_PCT = EntityDataManager.<Float>createKey(Entity.class, DataSerializers.FLOAT);
	static final DataParameter<Float> FLIP_ROTATION = EntityDataManager.<Float>createKey(Entity.class, DataSerializers.FLOAT);
	static final DataParameter<Float> PREV_FLIP_ROTATION = EntityDataManager.<Float>createKey(Entity.class, DataSerializers.FLOAT);

	public static final DataParameter<ArrayList<String>> COMPENDIUM = EntityDataManager.createKey(Entity.class, STRING_SERIALIZER);	
	public static final DataParameter<ArrayList<String>> CATEGORIES = EntityDataManager.createKey(Entity.class, STRING_SERIALIZER);	

	static {
		ArsMagica2.LOGGER.info("Current Mana ID : " + CURRENT_MANA.getId());
		ArsMagica2.LOGGER.info("Max Mana ID : " + MAX_MANA.getId());
		ArsMagica2.LOGGER.info("Current Mana Fatigue ID : " + CURRENT_MANA_FATIGUE.getId());
		ArsMagica2.LOGGER.info("Max Mana Fatigue ID : " + MAX_MANA_FATIGUE.getId());
		ArsMagica2.LOGGER.info("Current Level ID : " + CURRENT_LEVEL.getId());
		ArsMagica2.LOGGER.info("Current XP ID : " + CURRENT_XP.getId());
		ArsMagica2.LOGGER.info("Current Summons ID : " + CURRENT_SUMMONS.getId());
		ArsMagica2.LOGGER.info("Heal Cooldown ID : " + HEAL_COOLDOWN.getId());
		ArsMagica2.LOGGER.info("Affinity Heal Cooldown ID : " + AFFINITY_HEAL_COOLDOWN.getId());
		ArsMagica2.LOGGER.info("Mark X ID : " + MARK_X.getId());
		ArsMagica2.LOGGER.info("Mark Y ID : " + MARK_Y.getId());
		ArsMagica2.LOGGER.info("Mark Z ID : " + MARK_Z.getId());
		ArsMagica2.LOGGER.info("Mark Dimension ID : " + MARK_DIMENSION.getId());
		ArsMagica2.LOGGER.info("Contengency Stack ID : " + CONTENGENCY_STACK.getId());
		ArsMagica2.LOGGER.info("Shrink ID : " + IS_SHRUNK.getId());
		ArsMagica2.LOGGER.info("Inverted ID : " + IS_INVERTED.getId());
		ArsMagica2.LOGGER.info("Has Fall Protection ID : " + HAS_FALL_PROTECTION.getId());
		ArsMagica2.LOGGER.info("Contengency Type ID : " + CONTENGENCY_TYPE.getId());
		ArsMagica2.LOGGER.info("Affinity ID : " + AFFINITY.getId());
		ArsMagica2.LOGGER.info("Affinity Data ID : " + AFFINITY_DATA.getId());
		ArsMagica2.LOGGER.info("Point Tier ID : " + POINT_TIER.getId());
		ArsMagica2.LOGGER.info("Skill ID : " + SKILL.getId());
		ArsMagica2.LOGGER.info("Ice Bridge State ID : " + ICE_BRIDGE_STATE.getId());
		ArsMagica2.LOGGER.info("Ice Speed ID : " + ICE_SPEED.getId());
		ArsMagica2.LOGGER.info("Nature Speed ID : " + NATURE_SPEED.getId());
	}
	
}