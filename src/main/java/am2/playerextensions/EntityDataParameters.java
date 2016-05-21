package am2.playerextensions;

import am2.spell.components.Mark;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializer;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;

import java.io.IOException;

/**
 * Created by Edwin on 21/05/2016.
 */
public class EntityDataParameters {

    public static final DataSerializer<MarkData> MARK_DATA_SERIALIZER = new MarkSerializer();

    static {
        DataSerializers.registerSerializer(MARK_DATA_SERIALIZER);
    }

    public static final DataParameter<Float> CURRENT_MANA = EntityDataManager.createKey(EntityLivingBase.class, DataSerializers.FLOAT);
    public static final DataParameter<Float> MAX_MANA = EntityDataManager.createKey(EntityLivingBase.class, DataSerializers.FLOAT);
    public static final DataParameter<Float> CURRENT_MANA_FATIGUE = EntityDataManager.createKey(EntityLivingBase.class, DataSerializers.FLOAT);
    public static final DataParameter<Float> MAX_MANA_FATIGUE = EntityDataManager.createKey(EntityLivingBase.class, DataSerializers.FLOAT);
    public static final DataParameter<Integer> MAGIC_LEVEL = EntityDataManager.createKey(EntityLivingBase.class, DataSerializers.VARINT);
    public static final DataParameter<Float> MAGIC_XP = EntityDataManager.createKey(EntityLivingBase.class, DataSerializers.FLOAT);
    public static final DataParameter<MarkData> MARK_DATA = EntityDataManager.createKey(EntityLivingBase.class, MARK_DATA_SERIALIZER);
    public static final DataParameter<Integer> HEAL_COOLDOWN = EntityDataManager.createKey(EntityLivingBase.class, DataSerializers.VARINT);

    public static class MarkData {

        public double posX;
        public double posY;
        public double posZ;
        public int dimension;

        public MarkData(double posX, double posY, double posZ, int dimension) {
            this.posX = posX;
            this.posY = posY;
            this.posZ = posZ;
            this.dimension = dimension;
        }
    }

    public static class MarkSerializer implements DataSerializer<MarkData> {

        @Override
        public void write(PacketBuffer buf, MarkData value) {
            buf.writeString(value.posX + "\0" + value.posY + "\0" + value.posZ + "\0" + value.dimension);
        }

        @Override
        public MarkData read(PacketBuffer buf) throws IOException {
            String[] strings = buf.readStringFromBuffer(32767).split("\0");
            return new MarkData(Double.valueOf(strings[0]), Double.valueOf(strings[1]), Double.valueOf(strings[2]), Integer.valueOf(strings[3]));
        }

        @Override
        public DataParameter<MarkData> createKey(int id) {
            return new DataParameter(id, this);
        }
    }
}
