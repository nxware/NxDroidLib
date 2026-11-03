package net.bsnx.jlib.net;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * Stellt einen QueryString dar wie er in einer URL vorkommt.
 * 
 * @author Bjoern R. Salgert
 * @date 22.05.2011
 */
public class QueryString {

	public static QueryString FromUrlString(String url) {
		try {
			return new QueryString(new URI(url));
		} catch (URISyntaxException e) {
			throw new RuntimeException("Fehlerhafte URL um daraus einen QueryString zu extrahieren", e);
		}
	}
	
	private Map<String, String> map = new HashMap<String, String>();
	
	private String orginal;
	
	/**
	 * @param s	Ein QueryString (das Was Hinter dem Fragezeichen steht)
	 */
	public QueryString(String s) {
		if (s == null) return;
		this.orginal = s;
		String buffer = "";
		String key = "";
		int state = 0;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			switch (state) {
			case 0:
				if (c=='=') {
					key = buffer.toString();
					buffer = "";
					state = 1;
				} else if (c=='&') {
					
				} else {
					buffer += ""+c;
				}
				break;
			case 1:
				if (c=='&') {
					map.put(key, buffer);
					state = 0;
					key = "";
					buffer = "";
				} else {
					buffer += ""+c;
				}
				break;
			}
		}
		if (!buffer.equals("")) {
			map.put(key, buffer);
		}
	}
	
	public QueryString(URI uri) {
		this(uri.getQuery());
	}
	
	public String get(String key) {
		return map.get(key);
	}
	
	public String get(String key, String defaultValue) {
		if (map.containsKey(key)) {
			return map.get(key);
		} else {
			return defaultValue;
		}
	}
	
	public int getInt(String key) {
		return Integer.parseInt(get(key));
	}

	@Override
	public String toString() {
		return orginal;
	}
	
}
