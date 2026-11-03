package net.bsnx.jlib.collections;

import net.bsnx.data.Row;
import net.bsnx.data.RowCursor;
import net.bsnx.i.ContainsAble;
import net.bsnx.i.Countable;
import net.bsnx.i.Getter;
import net.bsnx.i.Named;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class Lst<T> extends ArrayList<T> implements ContainsAble<T>, Countable {

	private static final long serialVersionUID = 1L;

	public Lst() {}
	
	public Lst(T...array) {
		for (T item : array) {
			add(item);
		}
	}

	public Lst(Collection<T> keys) {
		for (T item : keys) {
			add(item);
		}
	}

	public boolean isEmpty() {
		return size() == 0;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < size(); i++) {
			sb.append(get(i));
			if (i != size()-1) { // Wenn NICHT das letzte Element verarbeitet wird...
				sb.append(", ");
			}
		}
		return sb.toString();
	}
	
	public String toLinesStr() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < size(); i++) {
			sb.append(get(i));
			if (i != size()-1) { // Wenn NICHT das letzte Element verarbeitet wird...
				sb.append("\n");
			}
		}
		return sb.toString();
	}
	
	@SuppressWarnings("unchecked")
	public <U> Lst<U> findTypes(Class<U> type) {
		Lst<U> res = new Lst<U>();
		for (T item : this) {
			if (type.isInstance(item)) {
				res.add((U)item);
			}
		}
		return res;
	}
	
	public Lst<T> copy() {
		Lst<T> res = new Lst<T>();
		res.addAll(this);
		return res;
	}

	public int length() {
		return size();
	}

	public T first() {
		return get(0);
	}
	
	public <V> void orderBy(final Getter<T, V> getter, final Comparator<V> cmp) {
		sort(new Comparator<T>() {
			@Override public int compare(T a, T b) {
				return cmp.compare(getter.getValue(a), getter.getValue(b));
			}
		});
	}
	
	public <V> Lst<T> qEquals(Getter<T, V> field, V value) {
		Lst<T> res = new Lst<T>();
		for (T item : res) {
			if (field.getValue(item).equals(value)) {
				res.add(item);
			}
		}
		return res;
	}
	
	public <V> Lst<T> qNotEquals(Getter<T, V> field, V value) {
		Lst<T> res = new Lst<T>();
		for (T item : res) {
			if (!field.getValue(item).equals(value)) {
				res.add(item);
			}
		}
		return res;
	}

	@Override
	public boolean containsElement(T item) {
		return contains(item);
	}
	
	public <V extends ContainsAble<Object>> Lst<T> qPropertyContains(Getter<T, V> field, V needle) {
		Lst<T> res = new Lst<T>();
		for (T item : res) {
			if (field.getValue(item).containsElement(needle)) {
				res.add(item);
			}
		}
		return res;
	}
	
	public <V extends List<Object>> Lst<T> qListPropertyContains(Getter<T, V> field, V needle) {
		Lst<T> res = new Lst<T>();
		for (T item : res) {
			if (field.getValue(item).contains(needle)) {
				res.add(item);
			}
		}
		return res;
	}
	
	public <V> Lst<T> qArrayPropertyContains(Getter<T, V[]> field, V needle) {
		Lst<T> res = new Lst<T>();
		for (T item : this) {
			V[] array = field.getValue(item);
			for (V arrayItem : array) {
				if (arrayItem.equals(needle)) {
					res.add(item);
					break;
				}
			}
		}
		return res;
	}

	public void println() {
		for (T item : this) {
			System.out.println(item);
		}
	}
	
	public RowCursor toRows(String field) {
		RowCursor res = new RowCursor();
		for (T item : this) {
			Row r = res.addRow().add(field, item);
			if (item instanceof Named) {
				r.add(Named.COL, ((Named)item).getName());
			}
		}
		return res;
	}

	@Override
	public int count() {
		return count();
	}
	
}
