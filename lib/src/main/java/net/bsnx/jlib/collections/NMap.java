package net.bsnx.jlib.collections;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class NMap<K, V> extends HashMap<K, V> implements Iterable<V> {

	public static NMap<String, String> Add(String k, String v) {
		NMap<String, String> result = new NMap<String, String>();
		result.put(k,v);
		return result;
	}

	private static final long serialVersionUID = 1L;
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (K key : keySet()) {
			sb.append(key.toString() + "=" + get(key) + " ");
		}
		return sb.toString();
	}

	public String toQueryStr() {
		StringBuilder sb = new StringBuilder();
		for (K key : keySet()) {
			sb.append(key.toString() + "=" + get(key) + "&");
		}
		return sb.toString();
	}

	public NMap<K, V> add(K k, V v) {
		this.put(k, v);
		return this;
	}
	
	public Set<K> keys() {
		return keySet();
	}
	
	public Lst<K> keyList() {
		return new Lst<K>(keys());
	}
	
	public V getOpt(K key, V defaultVal) {
		V val = get(key);
		return val==null ? defaultVal : val;		
	}

	@Override
	public Iterator<V> iterator() {
		return values().iterator();
	}

}
