package net.bsnx.jlib.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Liste nur ein Element nur einmal aufnehmen kann.
 * 
 * @author Bjoern R. Salgert
 * @date 28.05.2011
 */
public class UniqueList<T> implements List<T> {

	List<T> list;
	
	public UniqueList() {
		list = new ArrayList<T>();
	}
	
	@Override
	public boolean add(T item) {
		if (!contains(item)) {
			return list.add(item);
		} else {
			return false;
		}
	}

	@Override
	public void add(int pos, T item) {
		add(item);
	}

	@Override
	public boolean addAll(Collection<? extends T> items) {
		boolean res = true;
		for (T t : items) {
			if (!add(t)) res = false;
		}
		return res;
	}

	@Override
	public boolean addAll(int pos, Collection<? extends T> items) {
		return list.addAll(items);
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
	public T get(int index) {
		return list.get(index);
	}

	@Override
	public int indexOf(Object o) {
		return list.indexOf(o);
	}

	@Override
	public boolean isEmpty() {
		return list.isEmpty();
	}

	@Override
	public Iterator<T> iterator() {
		return list.iterator();
	}

	@Override
	public int lastIndexOf(Object o) {
		return list.lastIndexOf(o);
	}

	@Override
	public ListIterator<T> listIterator() {
		return list.listIterator();
	}

	@Override
	public ListIterator<T> listIterator(int i) {
		return list.listIterator(i);
	}

	@Override
	public boolean remove(Object o) {
		return list.remove(o);
	}

	@Override
	public T remove(int index) {
		return list.remove(index);
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
	public T set(int index, T element) {
		return list.set(index, element);
	}

	@Override
	public int size() {
		return list.size();
	}

	@Override
	public List<T> subList(int fromIndex, int toIndex) {
		return list.subList(fromIndex, toIndex);
	}

	@Override
	public Object[] toArray() {
		return list.toArray();
	}

	@Override
	public <U> U[] toArray(U[] a) {
		return list.toArray(a);
	}
	
	@Override
	public String toString() {
		return Arrays.toString(toArray());
	}

}
