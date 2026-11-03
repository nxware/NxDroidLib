package net.bsnx.jlib.text.html;

import net.bsnx.i.Titled;

import java.util.ArrayList;
import java.util.List;

public class UL implements HtmlAble {

	List<Object> items = new ArrayList<Object>();
	
	public UL() {}
	
	public UL(Object...items) {
		for (Object obj : items) {
			this.items.add(obj);
		}
	}
	
	public <T> UL add(Iterable<T> items) {
		for (T obj : items) {
			this.items.add(obj);
		}
		return this;
	}
	
	@Override
	public String toHtml() {
		StringBuilder sb = new StringBuilder();
		sb.append("<ul>\n");
		for (Object item : items) {
			sb.append("  <li>");
			if (item instanceof Titled) {
				sb.append(((Titled)item).getTitle());
			} else {
				sb.append(item);
			}
			sb.append("</li>\n");
		}
		sb.append("</ul>\n");
		return sb.toString();
	}
	
}
