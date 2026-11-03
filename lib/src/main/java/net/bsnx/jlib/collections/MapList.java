package net.bsnx.jlib.collections;

public class MapList<K,V> extends NMap<K, Lst<V>> {

	public Lst<V> getList(K key) {
		if (containsKey(key)) {
			return get(key);
		} else {
			Lst<V> res = new Lst<V>();
			put(key, res);
			return res;
		}
	}
	
}
