package net.bsnx.jlib.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

public class EntrySet implements Set<Entry<String,Object>> {

	private List<Entry<String,Object>> list;
	
	public EntrySet(List<Entry<String,Object>> list) {
		this.list = list;
	}
	
	public EntrySet() {
		this(new ArrayList<Entry<String,Object>>());
	}
	
	@Override
	public boolean add(Entry<String, Object> e) {
		return list.add(e);
	}
	
	public boolean add(String key, Object value) {
		return list.add(new DefaultEntry(key, value));
	}
	
	private static class DefaultEntry implements Entry<String, Object> {

		String k;
		Object v;
		
		public DefaultEntry(String k, Object v) {
			this.k = k;
			this.v = v;
		}
		
		@Override
		public String getKey() {
			return k;
		}

		@Override
		public Object getValue() {
			return v;
		}

		@Override
		public Object setValue(Object value) {
			v = value;
			return v;
		}
	}

	@Override
	public boolean addAll(Collection<? extends Entry<String, Object>> c) {
		return list.addAll(c);
	}

	@Override
	public void clear() {
		list.clear();
	}

	@Override
	public boolean contains(Object o) {
		return list.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return list.containsAll(c);
	}

	@Override
	public boolean isEmpty() {
		return list.isEmpty();
	}

	@Override
	public Iterator<Entry<String, Object>> iterator() {
		return list.iterator();
	}

	@Override
	public boolean remove(Object o) {
		return list.remove(o);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return list.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return list.retainAll(c);
	}

	@Override
	public int size() {
		return list.size();
	}

	@Override
	public Object[] toArray() {
		return list.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return list.toArray(a);
	}

}
