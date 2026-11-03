package net.bsnx.jlib.media;

import net.bsnx.jlib.ShuffleRandom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class PlaylistRandomQueued<T> implements IPlaylist<T>, List<T> {
	
	private List<T> list = new ArrayList<T>();
	private List<T> prev = new ArrayList<T>();
	private T current;
	private List<T> next = new ArrayList<T>();
	
	private ShuffleRandom mRandom = new ShuffleRandom();
	
	private T calcRandom() {
		return get(mRandom.rand());
	}
	
	/**
	 * O(1)
	 * 
	 * @category IPlaylist
	 */
	@Override
	public T current() {
		return current;
	}

	/**
	 * @category IPlaylist
	 */
	@Override
	public T next() {
		if (current != null) {
			prev.add(current);
		}
		if (next.isEmpty()) {
			current = calcRandom();
		} else {
			current = next.get(0);
			next.remove(0);
		}
		return current;
	}
	
	/**
	 * @category IPlaylist
	 */
	@Override
	public T previous() {
		if (current != null) {
			next.add(0, current);
		}
		if (prev.isEmpty()) {
			current = calcRandom();
		} else {
			current = prev.get(prev.size()-1);
			prev.remove(prev.size()-1);
		}
		return current;
	}
	
	public void enqueue(T item) {
		next.add(item);
	}
	
	// {{{ List

	@Override
	public boolean add(T item) {
		boolean res = list.add(item);
		mRandom.setMax(list.size());
		return res;
	}

	@Override
	public void add(int location, T item) {
		list.add(location, item);
		mRandom.setMax(list.size());
	}

	@Override
	public boolean addAll(Collection<? extends T> collection) {
		boolean res = list.addAll(collection);
		mRandom.setMax(list.size());
		return res;
	}

	@Override
	public boolean addAll(int pos, Collection<? extends T> collection) {
		boolean res = list.addAll(pos, collection);
		mRandom.setMax(list.size());
		return res;
	}

	@Override
	public void clear() {
		list.clear();
		// mRandom ?
	}

	@Override
	public boolean contains(Object object) {
		return list.contains(object);
	}

	@Override
	public boolean containsAll(Collection<?> collection) {
		return list.containsAll(collection);
	}

	/**
	 * Was soll passieren wenn man ein Item aufrufen, ab in die History damit ?
	 */
	@Override
	public T get(int location) {
		return list.get(location);
	}

	@Override
	public int indexOf(Object object) {
		return list.indexOf(object);
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
	public int lastIndexOf(Object object) {
		return list.lastIndexOf(object);
	}

	@Override
	public ListIterator<T> listIterator() {
		return list.listIterator();
	}

	@Override
	public ListIterator<T> listIterator(int location) {
		return list.listIterator(location);
	}

	@Override
	public T remove(int location) {
		T res = list.remove(location);
		mRandom.setMax(list.size());
		return res;
	}

	@Override
	public boolean remove(Object object) {
		boolean res = list.remove(object);
		mRandom.setMax(list.size());
		return res;
	}

	@Override
	public boolean removeAll(Collection<?> collection) {
		boolean res = list.removeAll(collection);
		mRandom.setMax(list.size());
		return res;
	}

	@Override
	public boolean retainAll(Collection<?> collection) {
		boolean res = list.retainAll(collection);
		mRandom.setMax(list.size());
		return res;
	}

	@Override
	public T set(int location, T object) {
		return list.set(location, object);
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
	
	// }}}

}
