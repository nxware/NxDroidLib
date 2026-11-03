package net.bsnx.jlib.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Eine Liste die keine Wiederhohlungen zulaesst
 * 
 * @author bjoern.salgert
 * @param <T>
 */
public class DistinctList<T> extends ArrayList<T> {
	
	private static final long serialVersionUID = -5463912906832450069L;

	public DistinctList() {}
	
	public DistinctList(T[] array) {
		for (T item : array) {
			add(item);
		}
	}

	/**
	 * @see ArrayList#add(int, Object)
	 */
	@Override
	public void add(int index, T element) {
		super.add(element);
	}

	/**
	 * @see ArrayList#add(Object)
	 */
	@Override
	public boolean add(T e) {
		if (!contains(e)) {
			return super.add(e);
		} else {
			return false;
		}
	}
	
	@Override
	public boolean addAll(Collection<? extends T> c) {
		for (T item : c) {
			add(item);
		}
		return true;
	}

	@Override
	public boolean addAll(int index, Collection<? extends T> c) {
		return super.addAll(c);
	}

	@Override
	public String toString() {
		return Arrays.toString(this.toArray());
	}
	
	public DistinctList<T> copy() {
		DistinctList<T> res = new DistinctList<T>();
		res.addAll(this);
		return res;
	}
	
	public boolean isSetEquals(Collection<T> c) {
		return containsAll(c) && c.size() == size();
	}
	
}