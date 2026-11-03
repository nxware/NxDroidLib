package net.bsnx.jlib.net;

import net.bsnx.jlib.Functions;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Verwaltet Http Header in einer Liste
 * 
 * @author Bjoern R. Salgert
 * @date 08.06.2011
 */
public class HeaderCollection {

	public static void main(String[] args) {
		
		HeaderCollection c = new HeaderCollection();
		
		c.add("Content-Type: a");
		
	}
	
	private Map<String,HeaderValue> map = new HashMap<String, HeaderValue>();
	
	public HeaderCollection() {}
	
	public HeaderCollection(BufferedReader br) {
		this();
		try {
			String line = br.readLine();
			while (!Functions.empty(line)) {
				add(line);
				line = br.readLine();
			}
		} catch (IOException e) {
			throw new RuntimeException("Fehler beim lesen der Headerwerte.", e);
		}
	}
	
	/**
	 * Fuegt eine Zeile zur Collection hinzu.
	 * 
	 * @param line	Zeile z.B. "Content-Type: multipart/form-data"
	 */
	public void add(String line) {
		HeaderValue entry = new HeaderValue(line);
		map.put(entry.getKey(), entry);
	}
	
	public String getContentType() {
		String c = getFullContentType();
		if (c.indexOf(',')==-1 && c.indexOf(';')==-1) {
			return c;
		} else if (c.indexOf(';')!=-1) {
			return c.substring(0, c.indexOf(';'));
		} else {
			return c.substring(0, c.indexOf(','));
		}
	}
	
	public int getContentLength() {
		return Integer.parseInt(map.get("Content-Length").getValue());
	}
	
	public String getFullContentType() {
		if (map.containsKey("Content-Type")) {
			return map.get("Content-Type").getValue();
		} else if (map.containsKey("content-type")) {
			return map.get("content-type").getValue();
		} else {
			throw new RuntimeException("HeaderCollection enth�lt keinen Eintrag f�r Content-Type"); 
		}
	}
	
	public String getBoundary() {
		String c = getFullContentType();
		String[] array = c.split("=");
		return array[array.length-1];
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Entry<String, HeaderValue> entry : map.entrySet()) {
			sb.append(entry.getKey() + " " + entry.getValue().getValue() + "\n");
		}
		return sb.toString();
	}
	
	
	
}
