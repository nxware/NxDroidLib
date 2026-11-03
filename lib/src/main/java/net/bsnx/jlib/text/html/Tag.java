package net.bsnx.jlib.text.html;

import net.bsnx.jlib.collections.KV;

import java.util.ArrayList;
import java.util.List;

public class Tag implements HtmlAble {
	
	public Tag(String name, HtmlAble...tag) {
		this.name = name;
		add(tag);
	}

	protected String name = "div";
	
	protected boolean isSelfClosing = false;
	
	protected List<HtmlAble> tags = new ArrayList<HtmlAble>();
	protected List<KV> attrs = new ArrayList<KV>();
	
	public Tag add(HtmlAble...tag) {
		for (HtmlAble t : tag) {
			this.tags.add(t);
		}
		return this;
	}
	
	public Tag attr(String name, String value) {
		attrs.add(new KV(name, value));
		return this;
	}
	
	protected String getAttrStr() {
		StringBuilder sb = new StringBuilder();
		for (KV kv : attrs) {
			sb.append(" " + kv.toHtmlAttr());
		}
		return sb.toString();
	}
	
	@Override
	public String toHtml() {
		StringBuilder sb = new StringBuilder();
		sb.append("<" + name + getAttrStr());
		if (isSelfClosing) {
			sb.append("/");
		}
		sb.append(">");
		for (HtmlAble t : tags) {
			sb.append(t.toHtml());
		}
		if (!isSelfClosing) {
			sb.append("</" + name + ">");
		}
		return sb.toString();
	}
	
	public boolean containsAttr(String name) {
		for (KV kv : attrs) {
			if (kv.getKey().equals(name)) {
				return true;
			}
		}
		return false;
	}
	
	public void deleteAttr(String name) {
		for (KV kv : attrs) {
			if (kv.getKey().equals(name)) {
				attrs.remove(kv);
				break;
			}
		}
	}
	
	public Tag id(String id) {
		if (containsAttr("id")) {
			deleteAttr("id");
		}
		attr("id", id);
		return this;
	}

}
