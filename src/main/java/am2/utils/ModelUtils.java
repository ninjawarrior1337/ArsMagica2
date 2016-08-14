package am2.utils;

import java.lang.reflect.Type;
import java.util.Map;

import javax.vecmath.Vector3f;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraftforge.client.model.SimpleModelState;
import net.minecraftforge.common.model.IModelPart;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelUtils {
	
	public static final Type mapType = new TypeToken<Map<String, String>>() {}.getType();
	public static final Gson GSON = new GsonBuilder().registerTypeAdapter(mapType, ModelTextureDeserializer.INSTANCE).create();
	public static final IModelState DEFAULT_ITEM_STATE;
	public static final IModelState DEFAULT_BLOCK_STATE;

	static {
	      ImmutableMap.Builder<IModelPart, TRSRTransformation> builder = ImmutableMap.builder();
	      builder.put(ItemCameraTransforms.TransformType.GROUND, get(0, 2, 0, 0, 0, 0, 0.5f));
	      builder.put(ItemCameraTransforms.TransformType.HEAD, get(0, 13, 7, 0, 180, 0, 1));
	      builder.put(ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, get(0, 3, 1, 0, 0, 0, 0.55f));
	      builder.put(ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, get(0, 3, 1, 0, 0, 0, 0.55f));
	      builder.put(ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND, get(1.13f, 3.2f, 1.13f, 0, -90, 25, 0.68f));
	      builder.put(ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND, get(1.13f, 3.2f, 1.13f, 0, 90, -25, 0.68f));
	      DEFAULT_ITEM_STATE = new SimpleModelState(builder.build());
	      
	      ImmutableMap.Builder<IModelPart, TRSRTransformation> builderBlock = ImmutableMap.builder();
	      builderBlock.put(ItemCameraTransforms.TransformType.GUI, get(0, 0, 0, 30, 225, 0, 0.625f));
	      builderBlock.put(ItemCameraTransforms.TransformType.GROUND, get(0, 3, 0, 0, 0, 0, 0.25f));
	      builderBlock.put(ItemCameraTransforms.TransformType.FIXED, get(0, 0, 0, 0, 0, 0, 0.5f));
	      builderBlock.put(ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, get(0, 2.5f, 0, 75, 45, 0, 0.375f));
	      builderBlock.put(ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, get(0, 2.5f, 0, 75, 225, 0, 0.375f));
	      builderBlock.put(ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND, get(0, 0, 0, 0, 45, 0, 0.40f));
	      builderBlock.put(ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND, get(0, 0, 0, 0, 225, 0, 0.40f));
	      DEFAULT_BLOCK_STATE = new SimpleModelState(builderBlock.build());
	}
	
	private static TRSRTransformation get(float tx, float ty, float tz,
			float ax, float ay, float az, float s) {
		return TRSRTransformation.blockCenterToCorner(new TRSRTransformation(
				new Vector3f(tx / 16, ty / 16, tz / 16), TRSRTransformation
						.quatFromXYZDegrees(new Vector3f(ax, ay, az)),
				new Vector3f(s, s, s), null));
	}
	public static class ModelTextureDeserializer implements JsonDeserializer<Map<String, String>> {

		public static final ModelTextureDeserializer INSTANCE = new ModelTextureDeserializer();

		private static final Gson GSON = new Gson();

		@Override
		public Map<String, String> deserialize(JsonElement json, Type typeOfT,
				JsonDeserializationContext context) throws JsonParseException {

			JsonObject obj = json.getAsJsonObject();
			JsonElement texElem = obj.get("textures");

			if (texElem == null) {
				throw new JsonParseException("Missing textures entry in json");
			}

			return GSON.fromJson(texElem, mapType);
		}
	}


}
