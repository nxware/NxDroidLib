package net.bsnx.jlib.collections;

import net.bsnx.i.Function;
import net.bsnx.i.Getter;

import java.util.Collection;
import java.util.Comparator;

public class Categories<T, V> implements Runnable {

	private Collection<T> items;
	private Getter<T, V> getter;
	private Function<V,V> reduce;
	
	private Lst<V> cats = new Lst<V>();
	private NMap<V, Lst<T>> map = new NMap<V, Lst<T>>();

	/**
	 * 
	 * @param items
	 * @param getter
	 * @param reduce (null fuer Identitaetsfunktion)
	 */
	public Categories(Collection<T> items, Getter<T, V> getter, Function<V,V> reduce) {
		this.items = items;
		this.getter = getter;
		this.reduce = reduce;
		if (this.reduce == null) {
			this.reduce = new Function<V, V>() {
				@Override public V execute(V item) {
					return item;
				}
			};
		}
	}

	@Override
	public void run() {
		Lst<T> list = new Lst<T>();
		list.addAll(items);
		if (!list.isEmpty()) {
			BoolList check = new BoolList(list.size());
			V current = null;
			for (int i = 0; i < list.length(); i++) {
				if (!check.is(i)) {
					Lst<T> cat = new Lst<T>();
					cat.add(list.get(i));
					current = reduce.execute(getter.getValue(list.get(i)));
					check.set(i, true); 
					for (int j = i+1; j < list.length(); j++) {
						if (!check.is(j)) {
							if (current.equals(reduce.execute(getter.getValue(list.get(j))))) {
								check.set(j, true);
								cat.add(list.get(j));
							}
						}
					}
					map.put(current, cat);
					cats.add(current);
				}
			}
		}
	}

	public void println() {
		System.out.println(toString());
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (V v : cats) {
			sb.append(v.toString() + ": " + map.get(v) + "\n");
		}
		return sb.toString();
	}
	
	public int getCategoryCount() {
		return cats.size();
	}
	
	public Lst<V> getCatList() {
		return cats;
	}
	
	public NMap<V, Lst<T>> getMap() {
		return map;
	}
	
	public void sort(Comparator<T> cmp) {
		for (V v : cats) {
			map.get(v).sort(cmp);
		}
	}
	
	public void orderBy(final Comparator<V> cmp) {
		for (V v : cats) {
			map.get(v).sort(new Comparator<T>() {
				@Override public int compare(T a, T b) {
					return cmp.compare(getter.getValue(a), getter.getValue(b));
				}
			});
		}
	}
	
}
