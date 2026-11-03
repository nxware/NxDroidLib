package net.bsnx.jlib.collections;

import net.bsnx.data.Row;
import net.bsnx.data.RowCursor;
import net.bsnx.jlib.FileSystem;
import net.bsnx.jlib.text.TextUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Eine um String-Spzifische Elemente erweiterte List<String>
 * 
 * @author bjoern.salgert
 */
public class StringList extends ArrayList<String> {

	private static final long serialVersionUID = 1L;
	
	public static StringList fromFile(String path) {
		return (new StringList().addList(FileSystem.readLines(new File(path))));
	}
	
	public static StringList fromText(String text) {
		String[] a = text.split("\n");
		StringList sl = StringList.create();
		sl.addAll(a);
		return sl;
	}
	
	public static StringList create() {
		return new StringList();
	}

	/**
	 * Hauptkonstruktor
	 * 
	 * @category Constructor
	 */
	public StringList() {
		super();
	}
	
	/**
	 * @category Constructor
	 * @param list
	 */
	public StringList(List<String> list) {
		this();
		addList(list);
	}
	
	/**
	 * Konstruktor um direkt ein oder mehrere Element der Liste hinzuzufuegen
	 * @category Constructor
	 * @param item
	 */
	public StringList(String...item) {
		this();
		for (String string : item) {
			add(string);
		}
	}
	
	public StringList addAll(String...strings) {
		for (String string : strings) {
			add(string);
		}
		return this;
	}
	
	/**
	 * Gibt eine Menschenlesbare Liste zur�ck.
	 * Ist die Liste leer wird zur�ck gegeben.
	 * 
	 * @return z.B. "1, 2, 3, 4"
	 */
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
	
	/**
	 * Liefert das erste Element der Liste zur�ck, dass ohne Ber�cksichtigung der
	 * Gro�- und kleinschreibung mit item �bereinstimmt.
	 * 
	 * Wird das Element nicht in der liste gefunden, wird eine 
	 * IllegalArgumentException geworfen.
	 * 
	 * @param item
	 * @return
	 */
	public String findIgnoreCase(String item) {
		for (String s : this) {
			if (s.toLowerCase().equals(item.toLowerCase())) {
				return s;
			}
		}
		throw new IllegalArgumentException("Der String \"" + item + "\" ist ohne ber�cksichtigung der Gro�- und kleinschreibung nicht in der List enthalten.");
	}
	
	public boolean containsIgnoreCase(String item) {
		for (String s : this) {
			if (s.toLowerCase().equals(item.toLowerCase())) {
				return true;
			}
		}
		return false;
	}

	public String[] toStringArray() {
		String[] res = new String[size()];
		for (int i = 0; i < res.length; i++) {
			res[i] = get(i);
		}
		return res;
	}

	public StringList addAll(Iterable<String> strings) {
		if (strings != null) {
			for (String s : strings) {
				add(s);
			}
		}
		return this;
	}
	
	public StringList addList(List<String> strings) {
		if (strings != null) { 
			for (String s : strings) {
				add(s);
			}
		}
		return this;
	}
	
	public StringList sort() {
		Collections.sort(this);
		return this;
	}
	
	public String implode(String glue) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < size(); i++) {
			sb.append(get(i));
			if (i != size()-1) { // Wenn NICHT das letzte Element verarbeitet wird...
				sb.append(glue);
			}
		}
		return sb.toString();
	}
	
	/**
	 * Ohne Seiteneffekte
	 * 
	 * @return
	 */
	public StringList trim() {
		StringList res = new StringList();
		for (String s : this) {
			res.add(s.trim());
		}
		return res;
	}
	
	public StringList prependEach(String prefix) {
		StringList res = new StringList();
		for (String s : this) {
			res.add(prefix + s);
		}
		return res;
	}
	
	public StringList appendEach(String postfix) {
		StringList res = new StringList();
		for (String s : this) {
			res.add(s + postfix);
		}
		return res;
	}
	
	public int getMaxLength() {
		int res = 0;
		for (String s : this) {
			res = Math.max(res, s.length());
		}
		return res;
	}
	
	public void fillToLength(int length, char fillChar) {
		for (int i = 0; i < size(); i++) {
			set(i, TextUtils.fillToLength(get(i), fillChar, length));
		}
	}
	
	public void appendHorizontal(StringList s, int offset, boolean fillEmpty) {
		int c = size() - (offset + s.size());
		int maxLength = getMaxLength();
		if (c > 0) {
			for (int i = 0; i < c; i++) {
				if(fillEmpty) {
					add(TextUtils.fillToLength("", ' ', maxLength));
				} else {
					add("");
				}
			}
		}
		for (int i = 0; i < s.size(); i++) {
			set(offset + i, get(offset+i) + s.get(i));
		}
	}
	
	public void update(int line, int col, String text) {
		if (get(line).length() > col + text.length()) {
			String add = get(line).substring(col + text.length(), get(line).length()-1);
			set(line, TextUtils.substr(get(line), 0, col) + text + add);
		} else {
			set(line, TextUtils.substr(get(line), 0, col) + text);
		}
	}
	
	public StringList copy() {
		StringList res = create();
		res.addList(this);
		return res;
	}
	
	/**        +-+
	 * a       |a|
	 * b   ->  |b|
	 * c       |c|
	 *         +-+
	 * 
	 * @return
	 */
	public StringList toTextBox() {
		int len = getMaxLength();
		StringList res = copy();
		res.fillToLength(len, ' ');
		res = res.prependEach("|").appendEach("|");
		res.add(0, "+"+TextUtils.fillToLength("", '-',len)+"+");
		res.add("+"+TextUtils.fillToLength("", '-',len)+"+");
		return res;
	}
	
	public StringList replace(String oldStr, String newStr) {
		StringList res = new StringList();
		for (String s : this) {
			res.add(s.replace(oldStr, newStr));
		}
		return res;
	}
	
	public StringList teeStdOut() {
		System.out.println(toString());
		return this;
	}
	
	public RowCursor toRows(final String filedName) {
		RowCursor res = new RowCursor();
		for (String s : this) {
			Row row = new Row();
			row.add(filedName, s);
			res.add(row);
		}
		return res;
	}
	
	public Lst<StringList> parseTextBoxes() {
		Lst<StringList> res = new Lst<StringList>();
		boolean inBox = false;
		StringList item = null;
		if (size()>3) {
			for (int i = 0; i < size()-1; i++) {
				if (!inBox) {
					if (get(i).startsWith("+-") && get(i+1).startsWith("|")) {
						inBox = true;
						item = new StringList();
						item.add(get(i));
					} 
				} else /* !inBox */ {
					if (get(i).startsWith("+-") && !get(i+1).startsWith("|")) {
						inBox = false;
						res.add(item);
						item.add(get(i));
						item = null;
					} else {
						item.add(get(i));
					}
				}
			}
		}
		if (item != null) {
			res.add(item);
		}
		return res;
	}

	public void println() {
		for (String s : this) {
			System.out.println(s);
		}
	}
	
	public StringList push(String s) {
		add(s);
		return this;
	}
	
	public StringList toJava() {
		StringList res = new StringList();
		res.add("StringList.create()");
		for (String s : this) {
			res.add(".push("+TextUtils.toJavaLiteral(s)+")");
		}
		return res;
	}
	
	public StringList qLike(String expr) {
		StringList res = new StringList();
		for (String s : this) {
			if (TextUtils.like(s, expr)) {
				res.add(s);
			}
		}
		return res;
	}
	
	public StringList qNotLike(String expr) {
		StringList res = new StringList();
		for (String s : this) {
			if (!TextUtils.like(s, expr)) {
				res.add(s);
			}
		}
		return res;
	}
	
}