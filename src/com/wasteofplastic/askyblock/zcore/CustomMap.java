package com.wasteofplastic.askyblock.zcore;

import java.util.HashMap;

public class CustomMap<K, V> extends HashMap<K, V> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * One-to-one relationship, you can return the first matched key
	 *
	 * @param map
	 * @param value
	 * @return key
	 */
	public K getKeyByValue(V value) {
		for (Entry<K, V> entry : this.entrySet())
			if (value.equals(entry.getValue()))
				return entry.getKey();
		return null;
	}

}
