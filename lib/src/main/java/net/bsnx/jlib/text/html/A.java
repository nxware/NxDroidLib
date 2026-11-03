package net.bsnx.jlib.text.html;

import net.bsnx.jlib.collections.KV;

public class A implements HtmlAble {

	private String href;
	private String content;
	private String attrs = "";

	public A(String href, String content) {
		this.href = href;
		this.content = content;
	}
	
	public A(String href, String content, KV...attr) {
		this.href = href;
		this.content = content;
		for (KV kv : attr) {
			attrs += " " + kv.toHtmlAttr();
		}
	}
	
	@Override
	public String toHtml() {
		return "<a href=\""+href+"\""+attrs+">" + content + "</a>";
	}
	
	@Override
	public String toString() {
		return toHtml();
	}

}
