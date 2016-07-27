package am2.extensions;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.common.base.Optional;

import am2.LogHelper;
import am2.api.ArsMagicaAPI;
import am2.api.SkillPointRegistry;
import am2.api.SkillRegistry;
import am2.api.affinity.Affinity;
import am2.api.skill.Skill;
import am2.api.skill.SkillPoint;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ResourceLocation;

public class DataDefinitions {
	
	public static final MapSerializer<Affinity, Float> AFFINITY_SERIALIZER = new MapSerializer<Affinity, Float>() {
		public Float getValueInstanceFromString(String str) {return Float.valueOf(str);}
		public Affinity getKeyInstanceFromString(String str) {
			LogHelper.info(str);
			return ArsMagicaAPI.getAffinityRegistry().getObject(new ResourceLocation(str));
			}
	};
		
	public static final MapSerializer<SkillPoint, Integer> SKILL_POINT_SERIALIZER = new MapSerializer<SkillPoint, Integer>() {
		public Integer getValueInstanceFromString(String str) {return Integer.valueOf(str);}
		public SkillPoint getKeyInstanceFromString(String str) {return SkillPointRegistry.fromName(str);}
	};
	
	public static final MapSerializer<Skill, Boolean> SKILL_SERIALIZER = new MapSerializer<Skill, Boolean>() {
		public Boolean getValueInstanceFromString(String str) {return Boolean.valueOf(str);}
		public Skill getKeyInstanceFromString(String str) {return SkillRegistry.getSkillFromName(str);}
	};
	
	public static final MapSerializer<String, Integer> COOLDOWN_SERIALIZER = new MapSerializer<String, Integer>() {
		public Integer getValueInstanceFromString(String str) {return Integer.valueOf(str);}
		public String getKeyInstanceFromString(String str) {return str;}
	};
	
	public static final MapSerializer<String, Float> DATA_SERIALIZER_FLOAT = new MapSerializer<String, Float>() {
		public Float getValueInstanceFromString(String str) {return Float.valueOf(str);}
		public String getKeyInstanceFromString(String str) {return str;}
	};
	
	public static final MapSerializer<String, Boolean> DATA_SERIALIZER_BOOLEAN = new MapSerializer<String, Boolean>() {
		public Boolean getValueInstanceFromString(String str) {return Boolean.valueOf(str);}
		public String getKeyInstanceFromString(String str) {return str;}
	};
	
	public static final ArraySerializer<String> STRING_SERIALIZER = new ArraySerializer<String>() {
		public String getKeyInstanceFromString(String str) {return str;}
	};
	
	static {
		DataSerializers.registerSerializer(AFFINITY_SERIALIZER);
		DataSerializers.registerSerializer(SKILL_POINT_SERIALIZER);
		DataSerializers.registerSerializer(SKILL_SERIALIZER);
		DataSerializers.registerSerializer(STRING_SERIALIZER);
		DataSerializers.registerSerializer(COOLDOWN_SERIALIZER);
		DataSerializers.registerSerializer(DATA_SERIALIZER_FLOAT);
		DataSerializers.registerSerializer(DATA_SERIALIZER_BOOLEAN);
		
	}

	static final DataParameter<Float> CURRENT_MANA = EntityDataManager.<Float>createKey(Entity.class, DataSerializers.FLOAT);
	static final DataParameter<Float> CURRENT_MANA_FATIGUE = EntityDataManager.<Float>createKey(Entity.class, DataSerializers.FLOAT);
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
	static final DataParameter<Boolean> DISABLE_GRAVITY = EntityDataManager.<Boolean>createKey(Entity.class, DataSerializers.BOOLEAN);
	static final DataParameter<Float> FALL_PROTECTION = EntityDataManager.<Float>createKey(Entity.class, DataSerializers.FLOAT);
	static final DataParameter<String> CONTENGENCY_TYPE = EntityDataManager.<String>createKey(Entity.class, DataSerializers.STRING);
	
	static final DataParameter<String> AFFINITY = EntityDataManager.createKey(Entity.class, DataSerializers.STRING);
	static final DataParameter<HashMap<Affinity, Float>> AFFINITY_DATA = EntityDataManager.createKey(Entity.class, AFFINITY_SERIALIZER);
	static final DataParameter<HashMap<SkillPoint, Integer>> POINT_TIER = EntityDataManager.createKey(Entity.class, SKILL_POINT_SERIALIZER);
	static final DataParameter<HashMap<Skill, Boolean>> SKILL = EntityDataManager.createKey(Entity.class, SKILL_SERIALIZER);
	
	static final DataParameter<HashMap<String, Integer>> COOLDOWNS = EntityDataManager.createKey(Entity.class, COOLDOWN_SERIALIZER);
	static final DataParameter<HashMap<String, Boolean>> ABILITY_BOOLEAN = EntityDataManager.createKey(Entity.class, DATA_SERIALIZER_BOOLEAN);
	static final DataParameter<HashMap<String, Float>> ABILITY_FLOAT = EntityDataManager.createKey(Entity.class, DATA_SERIALIZER_FLOAT);
	
	static final DataParameter<Boolean> REVERSE_INPUT = EntityDataManager.<Boolean>createKey(Entity.class, DataSerializers.BOOLEAN);
	
	static final DataParameter<Float> SHRINK_PCT = EntityDataManager.<Float>createKey(Entity.class, DataSerializers.FLOAT);
	static final DataParameter<Float> PREV_SHRINK_PCT = EntityDataManager.<Float>createKey(Entity.class, DataSerializers.FLOAT);
	static final DataParameter<Float> FLIP_ROTATION = EntityDataManager.<Float>createKey(Entity.class, DataSerializers.FLOAT);
	static final DataParameter<Float> PREV_FLIP_ROTATION = EntityDataManager.<Float>createKey(Entity.class, DataSerializers.FLOAT);

	public static final DataParameter<ArrayList<String>> COMPENDIUM = EntityDataManager.createKey(Entity.class, STRING_SERIALIZER);	
	public static final DataParameter<ArrayList<String>> CATEGORIES = EntityDataManager.createKey(Entity.class, STRING_SERIALIZER);	
	
	static final DataParameter<Float> DIMINISHING_RETURNS = EntityDataManager.<Float>createKey(Entity.class, DataSerializers.FLOAT);
	
	static final DataParameter<Float> TK_DISTANCE = EntityDataManager.<Float>createKey(Entity.class, DataSerializers.FLOAT);
	
}