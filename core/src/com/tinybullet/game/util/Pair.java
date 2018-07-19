package com.tinybullet.game.util;

import java.io.Serializable;

public class Pair<K, V> implements Cloneable, Serializable {
	public K key;
	public V value;
	
	public Pair() {
	}

	public Pair(K key, V value) {
		this.key = key;
		this.value = value;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Pair)) {
			return false;
		}

		Pair<?, ?> pair = (Pair<?, ?>) obj;
		return key.equals(pair.key) && value.equals(pair.value);
	}
	
	public K getKey() {
		return key;
	}

	public void setKey(K key) {
		this.key = key;
	}

	public V getValue() {
		return value;
	}

	public void setValue(V value) {
		this.value = value;
	}

	@Override
	public int hashCode() {
		return key.hashCode() + value.hashCode();
	}

	@Override
	public String toString() {
		return "(" + key.toString() + ", " + value.toString() + ")";
	}
	
	public Pair<K, V> clone() {
		return new Pair<K, V>(key, value);
	}
}
