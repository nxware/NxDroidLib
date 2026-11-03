package net.bsnx.jlib.media;

import net.bsnx.jlib.ShuffleRandom;
import net.bsnx.jlib.collections.Collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Playlist Klasse
 * 
 * 
 * @author Bjoern R. Salgert
 * @date 13.09.2011
 *
 * @param <T>	Typ eines Tracks
 */
public class Playlist<T> implements List<T>, IPlaylist<T> {

	/**
	 * Eigentliche Playlist
	 */
	private List<T> list;
	
	private ShuffleRandom mRandom = new ShuffleRandom();
	
	/**
	 * Liste der bisher gespielten Lieder
	 * 
	 * Index 0:		Als Erstes gespieltes Lied
	 * Index n:		Das Lied zuletzt hinzugefuegte Lied
	 */
	private List<T> history = new ArrayList<T>();
	/**
	 * Position wenn man in der Playlist wieder zurueck geht
	 */
	private int history_pos = 0;
	
	public Playlist() {
		list = new ArrayList<T>();
	}
	
	public void addToHistory(T item) {
		history.add(item);
		history_pos = history.size() - 1;
	}
	
	/**
	 * @see IPlaylist#current()
	 */
	@Override
	public T current() {
		if (history_pos < history.size()) {
			return history.get(history_pos);
		} else {
			//return next();
			return null;
		}
	}
	
	/**
	 * @see IPlaylist#next()
	 */
	@Override
	public T next() {
		return next(true);
	}
	
	public T next(boolean add_to_histroy) {
		T res = null;
		if (history_pos < history.size()) {
			history_pos++;
			if (history_pos == history.size()) {
				res = doNext(add_to_histroy);
			} else {
				res = history.get(history_pos);
			}
		} else {
			res = doNext(add_to_histroy);
		}
		return res;
	}
	
	/**
	 * @see IPlaylist#previous()
	 */
	@Override
	public T previous() {
		T res = null;
		if (history_pos <= history.size()) {
			history_pos--;
			if (history_pos == -1) {
				res = doNext(true);
			} else {
				res = history.get(history_pos);
			}
		} else {
			res = doNext(true);
		}
		return res;
	}
	
	private T doNext(boolean add_to_histroy) {
		return get(mRandom.rand(), add_to_histroy);
	}
	
	public T get(int location, boolean add_to_history) {
		T res =  list.get(location);
		if (add_to_history) {
			addToHistory(res);
		}
		return res;
	}
	
	public void enqueue(T item) {
		history.add(item);
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
	public List<T> subList(int start, int end) {
		return list.subList(start, end);
	}

	@Override
	public Object[] toArray() {
		return list.toArray();
	}

	@Override
	public <U> U[] toArray(U[] array) {
		return list.toArray(array);
	}
	
	// }}}
	
	// {{{ toString()
	
	@Override
	public String toString() {
		return "[" + Collections.implode(list, ", ") + "]";
	}
	
	public enum Format {
		NORMAL,
		HISTORY
	}
	
	public String toString(Format format) {
		switch(format) {
			case HISTORY:
				StringBuilder sb = new StringBuilder();
				sb.append("[ ");
				for	(int i = 0; i < history.size(); i++) {
					if (i==history_pos) sb.append('|');
					sb.append(history.get(i));
					if (i==history_pos) sb.append('|');
					sb.append(' ');
				}
				sb.append("] HistoryPos: ");
				sb.append(history_pos);
				return sb.toString();
			default:
				return toString();
		}
	}
	
	// }}}
	
}
