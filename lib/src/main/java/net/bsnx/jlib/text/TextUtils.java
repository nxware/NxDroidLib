package net.bsnx.jlib.text;

import net.bsnx.jlib.collections.Collections;
import net.bsnx.jlib.collections.StringList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 
 * @author Bjoern R. Salgert
 * @date 06.06.2011
 */
public class TextUtils {

	public static void main(String[] args) {
		
		System.out.println(getTextBetween("[<!--{#}-->]", "<!--", "-->"));
		
		System.out.println(Arrays.toString(splitInTwo("a1:b_2", ':')));
		System.out.println(removeEscape("'123'"));
		
		List<String> list = parseToPairs("text/html encoding=UTF-8");
		System.out.println(Arrays.toString(list.toArray()));
		
		Map<String, String> map = parseToMap("a=b ze:'de'");
		System.out.println(Collections.toString(map));
		
		list = parseToList("1 2 3 4 5 6 7 8 9", " ");
		System.out.println(Arrays.toString(list.toArray()));
		list = parseToList("1 2 \"3 3\" 4 5 6 7 8 9", " ");
		System.out.println(Arrays.toString(list.toArray()));
	}
	
	/**
	 * Parset einen String al la filename='Eine Doku' length=1234
	 * is seine Bestandteile also
	 * String[] {"filename='Eine Doku.pdf'", "length=1234"}
	 */
	public static List<String> parseToPairs(String s) {
		List<String> res = new ArrayList<String>();
		if (s == null || s.length()==0) return res;
		int state = s.charAt(0)==' ' ? 0:1;
		char escape = 0;
		StringBuilder buf = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			switch (state) {
			case 0: // Im Leerraum
				if (c != ' ') {
					state = 1;
					buf.append(c);
				}
				break;
			case 1:
				if (c == ' ') {
					state = 0;
					res.add(buf.toString());
					buf = new StringBuilder();
				} else if (c == '\'' || c == '"') {
					escape = c;
					state = 2;
					buf.append(c);
				} else {
					buf.append(c);
				}
				break;
			case 2: // in Escape Sequenz // TODO support fuer escaping " or '
				if (c == escape) {
					state = 0;
					buf.append(c);
					res.add(buf.toString());
					buf = new StringBuilder();
				} else {
					buf.append(c);
				}
				break;
			}
		}
		if (buf.length() > 0) {
			res.add(buf.toString());
		}
		return res;
	}
	
	public static StringList parseToList(String s, String seperator) {
		TokenStream stream = new TokenStream(s);
		StringList res = new StringList();
		while (!stream.isEof()) {//System.out.println(stream);
			String item;
			if (stream.isOnTop("\"")) {
				item = stream.readStringLiteral('"');
			} else {
				item = stream.readTil(seperator);
			}
			stream.readWhile(seperator);
			res.add(item);
		}
		return res;
	}
	
	public static StringList parseToList(String s, String seperator, char...optinalEnclosers) {
		TokenStream stream = new TokenStream(s);
		StringList res = new StringList();
		LOOP : while (!stream.isEof()) {//System.out.println(stream);
			stream.readWhiteSpaces();
			String item;
			for (char enclose : optinalEnclosers) {
				if (stream.isOnTop("" + enclose)) {
					item = stream.readStringLiteral(enclose);
					stream.readWhile(seperator);
					res.add(item);
					continue LOOP;
				}
			}
			item = stream.readTil(seperator);
			stream.readWhile(seperator);
			res.add(item);
		}
		return res;
	}
	
	// {{{ parseToMap
	
	/**
	 * Parst einen Attribut-String z.B "a='b' length=10" in eine Map
	 * 
	 * @param s
	 * @return
	 */
	public static Map<String,String> parseToMap(String s) {
		return parseToMap(parseToPairs(s));
	}
	
	public static Map<String,String> parseToMap(List<String> pairs) {
		Map<String,String> res = new HashMap<String, String>();
		for (String s : pairs) {
			if (s.endsWith(";")) {
				s = s.substring(0, s.length()-1);
			}
			String[] parts;
			// Trennzeichen sind = und :
			int index_gleich = s.indexOf('=');
			int index_doppelpkt = s.indexOf(':');
			if (index_gleich == -1 && index_doppelpkt == -1) {
				continue;
			} else if (index_gleich!=-1 && (index_gleich < index_doppelpkt || index_doppelpkt==-1)) {
				parts = splitInTwo(s, '=');
			} else {
				parts = splitInTwo(s, ':');
			}
			res.put(parts[0], removeEscape(parts[1]));
		}
		return res;
	}
	
	// }}}
	
	// {{{ removeEscape(...)
	
	public static String removeEscape(String s) {
		if (s.startsWith("\"") && s.endsWith("\"")) {
			return removeEscape(s, '"');
		} else if (s.startsWith("'") && s.endsWith("'")) {
			return removeEscape(s, '\'');
		} else {
			return s;
		}
	}
	
	public static String removeEscape(String s, char c) {
		return s.substring(1, s.length()-1);
	}
	
	// }}}
	
	/**
	 * Teilt einen String $s beim ersten vorkommen von $c in zwei Teile
	 * 
	 * @param s	String der geteilt werden soll
	 * @param c	Zeichen das in $s vorkommt
	 * @return	Array der L�nge 2
	 */
	public static String[] splitInTwo(String s, char c) {
		int i = s.indexOf(c);
		String a = s.substring(0, i);
		String b = s.substring(i+1);
		return new String[] { a, b };
	}
	
	/**
	 * Entfernt Numerische Ziffern aus einen String
	 * 
	 * @param s
	 * @return String der keine numerischen Zeichen enth�lt
	 */
	public static String removeNumberChars(String s) {
		StringBuilder sb = new StringBuilder(s.length()); // StringBuilder da keine syncronisierung n�tig
		for (int i = 0; i < s.length(); i++) {	// s.length() O(1)
			char c = s.charAt(i);				// s.cahrAt() O(1)
			if (!Character.isDigit(c)) {
				sb.append(c);
			}
		}
		return sb.toString();
		/*return s.
			replace("0", "").
			replace("1", "").
			replace("2", "").
			replace("3", "").
			replace("4", "").
			replace("5", "").
			replace("6", "").
			replace("7", "").
			replace("8", "").
			replace("9", "")
		;*/
	}
	
	public static String removeNonNumberChars(String s) {
		StringBuilder sb = new StringBuilder(s.length()); // StringBuilder da keine syncronisierung n�tig
		for (int i = 0; i < s.length(); i++) {	// s.length() O(1)
			char c = s.charAt(i);				// s.cahrAt() O(1)
			if (Character.isDigit(c)) {
				sb.append(c);
			}
		}
		return sb.toString();
	}
	
	public static String fillToLength(String str, char fillChar, int resultStringLength) {
		if (str == null) {
			str = "";
		}
		String res = str.toString();
		while (res.length()<resultStringLength) {
			res += ""+fillChar; 
		}
		return res;
	}
	
	public static String cutToLength(String s, int length) {
		if (s.length()>length) {
			return s.substring(0, length);
		} else {
			return s;
		}
	}
	
	public static String getTextBetween(String text, String begin, String end) {
		return text.substring(text.indexOf(begin)+begin.length(), text.indexOf(end));
	}
	
	public static StringList toLines(String s) {
		// http://stackoverflow.com/questions/1096621/how-to-read-a-string-line-per-line
		StringList result = new StringList();
		BufferedReader reader = new BufferedReader(new StringReader(s));
		try  {
			String line = reader.readLine();
			while (line != null) {
			    result.add(line);
			    line = reader.readLine();
			}
		} catch (IOException exc) {
		    // quit
		}
		return result;
	}
	
	/**
	 * Mit Umschlie�enden Anf�hrungszeichen
	 * 
	 * @param s
	 * @return
	 */
	public static String toJavaLiteral(String s) {
		return "\"" + s.replace("\"", "\\\"") + '"';
	}
	
	public static String toSqlLiteral(String s) {
		return "'"+s.replace("'", "''")+"'";
	}
	
	public static String toJavaLiteral(Object o) {
		if (o instanceof String) {
			return toJavaLiteral(o.toString());
		} else if (o == null) {
			return "null";
		} else {
			return o.toString();
		}
	}

	public static String trimLeft(String arg, char c) {
		String a = "" + arg;
		try {
			while (a.charAt(0)==c) {
				a = a.substring(1);
			}
			return a;
		} catch (Exception e) {
			return arg;
		}
	}

	public static String substr(String s, int pos, int length) {
		if (s.length() >= pos + length) {
			return s.substring(pos, pos+length);
		} else {
			return s.substring(pos, s.length());
		}
	}
	
	public static String escapeForRegEx(String s) {
	  if (s == null) {
	    throw new IllegalArgumentException("String s cannot be null");
	  }
	  int len = s.length();
	  if (len == 0) {
	    return "";
	  }
	  final StringBuilder sb = new StringBuilder(len * 2);
	  for (int i = 0; i < len; i++){
	    char c = s.charAt(i);
	    if ("[](){}.*+?$^|#\\".indexOf(c) != -1) {
	      sb.append("\\");
	    }
	    sb.append(c);
	  }
	  return sb.toString();
	}
	
	public static boolean like(final String str, final String expr) {
		final String regex = escapeForRegEx(expr).replace("_", ".").replace("%", ".*?");
		final Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		return p.matcher(str).matches();
	}
	
}
