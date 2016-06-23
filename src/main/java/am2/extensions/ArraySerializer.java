package am2.extensions;

import java.io.IOException;
import java.util.ArrayList;

import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializer;

public abstract class ArraySerializer<K> implements DataSerializer<ArrayList<K>> {

	@Override
	public void write(PacketBuffer buf, ArrayList<K> value) {
		String str = "";
		for (K entry : value) {
			str += entry.toString() + "\0";
		}
		buf.writeString(str);
	}

	@Override
	public ArrayList<K> read(PacketBuffer buf) throws IOException {
		ArrayList<K> map = new ArrayList<K>();
		String str = buf.readStringFromBuffer(Integer.MAX_VALUE);
		String[] list = str.split("\0");
		for (String li : list) {
			map.add(getKeyInstanceFromString(li));
		}
		return map;
	}
	
	public abstract K getKeyInstanceFromString(String str);
	
	@Override
	public DataParameter<ArrayList<K>> createKey(int id) {
		return new DataParameter<ArrayList<K>>(id, this);
	}

}
