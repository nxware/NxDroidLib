package net.bsnx.jlib.text.html;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @see php Html\PropertyGrid
 * 
 * @author Bjoern R. Salgert
 * @date 10.08.2011
 */
public class PropertyGrid implements HtmlAble {

	Map<String,String> map = new HashMap<String, String>();
	
	public void add(String key, String val) {
		map.put(key, val);
	}
	
	@Override
	public String toHtml() {
		StringBuilder sb = new StringBuilder();
		sb.append("<table class=\"grid\">\n");
		boolean even = true;
		for (Entry<String, String> entry : map.entrySet()) {
			even = !even;
			sb.append("<tr class=\""+((even)?"grid_tr1":"grid_tr2")+"\">\n");
			sb.append("<td>");
			sb.append(entry.getKey());
			sb.append("</td>");
			sb.append("<td>");
			sb.append(entry.getValue());
			sb.append("</td>");
			sb.append("</tr>\n");
		}
		sb.append("</table>");
		return sb.toString();
	}

}
