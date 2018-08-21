package com.tinybullet.game.util;

import java.util.*;

public class MapUtil {

	public static <K, V> void orderByValue(LinkedHashMap<K, V> map, Comparator<? super V> comparator) {
		List<Map.Entry<K, V>> entries = new ArrayList<>(map.entrySet());
		map.clear();
		entries.stream().sorted(Comparator.comparing(Map.Entry::getValue, comparator))
				.forEachOrdered(e -> map.put(e.getKey(), e.getValue()));
	}
}
