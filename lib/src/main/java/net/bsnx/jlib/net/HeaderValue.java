package net.bsnx.jlib.net;

import net.bsnx.jlib.Functions;
import net.bsnx.jlib.text.TextUtils;

import java.util.Map;

/**
 * Eine Zeile aus einem HTTP Header
 * 
 * @author Bjoern R. Salgert
 * @date 02.06.2011
 */
public class HeaderValue {

	public static void main(String[] args) {
		HeaderValue o = new HeaderValue("Content-Type: text/html; encoding=UTF-8");
		System.out.println(o);
		System.out.println(o.getExtraValue("encoding"));
		
		o = new HeaderValue("Content-Type: text/html; encoding=\"UTF-8\"");
		System.out.println(o);
		System.out.println(o.getExtraValue("encoding"));
		
	}
	
	private String key;
	private String value;

	/**
	 * 
	 * @param line	Header Zeile z.B. "Content-Type: text/html"
	 */
	public HeaderValue(String line) {
		try {
			int index = line.indexOf(':');
			this.key = line.substring(0, index); // Exception wenn index=-1
			this.value = Functions.rtrim(line.substring(index+1).trim(), ";");
		} catch (Exception e) {}
	}

	public String getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}
	
	/**
	 * Liefert die zus�tzlichen Werte eines Headers zur�ck.
	 * BeispielHeaderValue: text/html; encoding=UTF-8
	 * 
	 * @see TextUtils
	 * @return
	 */
	public Map<String, String> getExtras() {
		return TextUtils.parseToMap(value);
	}
	
	public String getExtraValue(String key) {
		Map<String, String> map = getExtras();
		return map.get(key);
	}

	@Override
	public String toString() {
		return key + ": " + value;
	}
	
}
