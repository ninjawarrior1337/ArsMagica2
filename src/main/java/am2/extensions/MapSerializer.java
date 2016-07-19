package am2.extensions;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializer;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public abstract class MapSerializer<K, V> implements DataSerializer<HashMap<K, V>> {

	@Override
	public void write(PacketBuffer buf, HashMap<K, V> value) {
		String str = "";
		for (Entry<K, V> entry : value.entrySet()) {
			str += entry.getKey().toString() + "\0" + entry.getValue().toString() + "\0";
		}
		ByteBufUtils.writeUTF8String(buf, str);
		//buf.writeString(str);
	}

	@Override
	public HashMap<K, V> read(PacketBuffer buf) throws IOException {
		HashMap<K, V> map = new HashMap<K, V>();
		String str = ByteBufUtils.readUTF8String(buf);//buf.readStringFromBuffer(Integer.MAX_VALUE);
		String[] list = str.split("\0");
		boolean key = true;
		String strKey = "";
		for (String li : list) {
			if (key) {
				strKey = li;
			} else {
				K kInst = getKeyInstanceFromString(strKey);
				V vInst = getValueInstanceFromString(li);
				map.put(kInst, vInst);
			}
			key = !key;
		}
		return map;
	}
	
	public abstract V getValueInstanceFromString(String str);
	public abstract K getKeyInstanceFromString(String str);
	
	@Override
	public DataParameter<HashMap<K, V>> createKey(int id) {
		return new DataParameter<HashMap<K,V>>(id, this);
	}

}
