package am2.api;

import java.util.HashMap;
import java.util.Map.Entry;

import am2.affinity.Affinity;

public class AffinityRegistry {
	
	private static final HashMap<String, Affinity> affinities = new HashMap<String, Affinity>();
	
	/**
	 * Registers an affinity
	 * 
	 * @param name  Name of the affinity (will be lower cased)
	 * @param color Color to render in the occulus
	 */
	public static void registerAffinity(String name, int color) {
		registerAffinity(new Affinity(name, color));
	}
	
	/**
	 * Registers an affinity
	 * 
	 * @param aff   The affinity you want to register
	 */
	public static void registerAffinity(Affinity aff) {
		affinities.put(aff.getUnlocalisedName().replaceAll("affinity.", ""), aff);
	}
	/**
	 * Get the affinity map for more special searching.
	 * 
	 * @return the affinity registry
	 */
	public static HashMap<String, Affinity> getAffinityMap() {
		return affinities;
	}
	
	/**
	 * 
	 * @param name  Name of the affinity (will be lower cased)
	 * @return the affinity with the corresponding name if the is, none otherwise
	 */
	public static Affinity getAffinityFromName (String name) {
		for (Entry<String, Affinity> entry : affinities.entrySet()) {
			if (entry.getKey().equals(name.toLowerCase()))
				return entry.getValue();
		}
		return affinities.get("none");
	}

	public static Affinity getByID(int itemDamage) {
		int i = 0;
		for (Entry<String, Affinity> entry : affinities.entrySet()) {
			if (i == itemDamage)
				return entry.getValue();
			i++;
		}
		return null;
	}
	
	public static int getIdFor(Affinity aff) {
		int i = 0;
		for (Entry<String, Affinity> entry : affinities.entrySet()) {
			if (entry.getValue() == aff)
				return i;
			i++;
		}
		return -1;
	}

}
