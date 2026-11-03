package net.bsnx.jlib.text;

import net.bsnx.data.Row;
import net.bsnx.data.RowCursor;
import net.bsnx.jlib.collections.StringList;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;


/**
 * Liste von Woertern mit ihrer Anzahl
 * 
 * @author Bjoern R. Salgert
 * @date 16.06.2010
 *
 */
public class WordCountList implements Iterable<WordEntry> {

	private Map<String, Integer> map = new TreeMap<String, Integer>();
	
	public WordCountList() {
		super();
	}
	
	/**
	 * Fuegt ein Wort in die Liste ein, wenn es bereits vorhanden ist wird die Anzahl
	 * des Wortes erhaeht
	 * 
	 * @param word
	 */
	public void add(String word) {
		if (map.containsKey(word)) {
			int i = map.get(word);
			map.remove(word);
			map.put(word, i+1);
		} else {
			map.put(word, 1);
		}
	}
	
	/**
	 * Liefert eine Sortierte Sequenz der W�rter zur�ck
	 * 
	 * @return
	 */
	public Iterable<WordEntry> toSorted() {
		// Neue Collektion erzeugen
		List<WordEntry> res = new ArrayList<WordEntry>();
		// W�rter einf�gen
		for (Entry<String, Integer> e : map.entrySet()) {
			res.add(new WordEntry(e.getKey(), e.getValue()));
		}
		// Sortieren
		Collections.sort(res);
		// In geforderte reihenfolge drehen
		Collections.reverse(res);
		return res;
	}
	
	public int getMaxCount() {
		int max = 0;
		for (Integer e : map.values()) {
			max = Math.max(max, e);
		}
		return max;
	}
	
	/**
	 * 
	 * O(1)
	 * 
	 * @return
	 */
	public int size() {
		return map.size();
	}
	
	public void clear() {
		map.clear();
	}

	/**
	 * F�llt die Lieste von einem Reader
	 * 
	 * @param r
	 */
	public void fillFromReader(Reader r) {
		char[] cbuf = new char[100];
		try {
			while (r.read(cbuf, 0, 100) != -1) {
				String word = new String(cbuf);
				add(word.trim());
				cbuf = new char[100];
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public StringList getCountList(int count) {
		StringList res = new StringList();
		for (WordEntry itm : this) {
			if (itm.getCount()==count) {
				res.add(itm.getWord());
			}
		}
		return res;
	}

	@Override
	public Iterator<WordEntry> iterator() {
		return toSorted().iterator();
	}
	
	public RowCursor toRows(String wordName, String countName) {
		RowCursor res = new RowCursor();
		for (String s : map.keySet()) {
			Row row = new Row();
			row.add(wordName, s);
			row.add(countName, map.get(s));
			res.add(row);
		}
		return res;
	}
	
}
