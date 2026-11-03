package net.bsnx.jlib.collections;

/**
 * Einfache Schluessel-Wert Klasse
 * 
 * @author Salgert
 *
 */
public class KV {

	public KV(String key, String value) {
		this.key = key;
		this.value = value;
	}
	
	public KV(String key, int value) {
		this.key = key;
		this.value = ""+value;
	}

	private String key; 
	private String value;
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String toHtmlAttr() {
		return key +  "=\"" + value + "\"";
	}
	
	
}