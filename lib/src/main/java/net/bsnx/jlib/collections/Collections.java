package net.bsnx.jlib.collections;

import net.bsnx.i.Function;
import net.bsnx.i.Procedure2A;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Statische Klasse zum Umgang mit Collections
 * 
 * @author Bjoern R. Salgert
 * @date 09.08.2011
 */
public class Collections {

	public static String implode(Iterable<?> items, String glue) {
		StringBuilder sb = new StringBuilder();
		Iterator<?> iter = items.iterator();
		if (!iter.hasNext()) return "";
		Object obj = iter.next();
		sb.append(obj.toString());
		while (true) {
			if (iter.hasNext()) {
				sb.append(glue);
			} else break;
			obj = iter.next();
			sb.append(obj.toString());
			
		}
		
		return sb.toString();
	}
	
	public static String implode(Object[] list, String glue) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < list.length; i++) {
			sb.append(list[i]);
			if (i != list.length-1) {
				sb.append(glue);
			} 
		}
		return sb.toString();
	}
	
	public static String implode(String glue, Object[] list, int offset, int length) {
		StringBuilder sb = new StringBuilder();
		for (int i = offset; i < offset + length; i++) {
			sb.append(list[i]);
			if (i != offset + length-1) {
				sb.append(glue);
			} 
		}
		return sb.toString();
	}
	
	public static <T> List<T> toList(T[] items) {
		List<T> res = new ArrayList<T>();
		for (T e : items) {
			res.add(e);
		}
		return res;
	}
	
	public static CharSequence[] toCharSequenceArray(Iterable<String> strings) {
		List<CharSequence> list = new ArrayList<CharSequence>();
		for (String string : strings) {
			list.add(string);
		}
		CharSequence[] res = new CharSequence[list.size()];
		list.toArray(res);
		return res;
	}
	
	public static <T> List<T> map(List<T> input, Function<T, T> func) {
		List<T> res = new ArrayList<T>(input.size());
		for (T item : input) {
			res.add(func.execute(item));
		}
		return res;
	}
	
	public static <T, TRes> List<TRes> convert(List<T> input, Function<T, TRes> func) {
		List<TRes> res = new ArrayList<TRes>(input.size());
		for (T item : input) {
			res.add(func.execute(item));
		}
		return res;
	}
	
	public static <T> List<String> mapS(List<T> input, Function<String, String> func) {
		List<String> res = new ArrayList<String>(input.size());
		for (T item : input) {
			res.add(func.execute(item.toString()));
		}
		return res;
	}
	
	/**
	 * 
	 * @param lst
	 * @param filterFunc True -> Item soll in die Ausgabe
	 * @return
	 */
	public static <T> List<T> filter(List<T> lst, Function<T,Boolean> filterFunc) {
		List<T> res = new ArrayList<T>();
		for (T t : lst) {
			if (filterFunc.execute(t)) {
				res.add(t);
			}
		}		
		return res;
	}
	
	public static <T> List<T> take(int count, List<T> list) {
		List<T> res = new ArrayList<T>();
		int i = 0;
		for (T t : list) {
			if (i >= count || i>=list.size()) {
				break;
			}
			res.add(t);
			i++;
		}		
		return res;
	}
	
	public static <T, U> String toString(Map<T,U> map) {
		StringBuilder sb = new StringBuilder();
		for (Entry<T, U> e : map.entrySet()) {
			sb.append(e.getKey().toString());
			sb.append(": ");
			sb.append(e.getValue().toString());
			sb.append("\n");
		}
		return sb.toString();
	}
	
	public static <T> String toString(Iterable<T> items) {
		StringBuilder sb = new StringBuilder();
		for (T item : items) {
			sb.append(item.toString());
			sb.append("\n");
		}
		return sb.toString();
	}
	
	public static <T> List<String> toStringList(List<T> lst, Function<T,String> func) {
		if (lst == null) return new ArrayList<String>();
		List<String> res = new ArrayList<String>(lst.size());
		for (T item : lst) {
			res.add(func.execute(item));
		}
		return res;
	}
	
	public static Map<String, String> buildMap(KV...entries) {
		Map<String, String> res = new HashMap<String, String>();
		for (KV entry : entries) {
			res.put(entry.getKey(), entry.getValue());
		}
		return res;
		
	}
	
	/**
	 * Liefert die Schnittmenge beider Listen zurueck.
	 * D.h. jedes Element in $res ist auch in $a und $b enthalten
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static <T> List<T> getIntersection(Collection<T> a, Collection<T> b) {
		List<T> res = new ArrayList<T>();
		for (T t : a) {
			if (b.contains(t)) {
				res.add(t);
			}
		}
		return res;
	}
	
	/**
	 * Liefert alle Elemente zur�ck die in $a aber nicht in $b enthalten sind.
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static <T> List<T> getDiff(Collection<T> a, Collection<T> b) {
		List<T> res = new ArrayList<T>();
		res.addAll(a);
		res.removeAll(b);
		return res;
	}
	
	public static <T> void intervalize(List<T> list, Procedure2A<T, T> cb) {
		for (int i = 0; i < list.size()-1; i++) {
			cb.execute(list.get(i), list.get(i +1));
		}
	}
	
	public static <T> boolean containsAny(Collection<T> collection, Collection<T> toTest) {
		for (T t : toTest) {
			if (collection.contains(t)) {
				return true;
			}
		}
		return false;
	}
	
	public static <T> boolean containsAny(Collection<T> collection, T[] toTest) {
		for (T t : toTest) {
			if (collection.contains(t)) {
				return true;
			}
		}
		return false;
	}
	
	public static <T> List<T> intersect(Collection<T> collection, T[] toTest) {
		List<T> res = new ArrayList<T>();
		for (T t : toTest) {
			if (collection.contains(t)) {
				res.add(t);
			}
		}
		return res;
	}
	
}
