package net.bsnx.jlib.text.html;

public class StrTag implements HtmlAble {
	private String str;
	public StrTag(String str) {
		this.str = str;
	}
	@Override
	public String toHtml() {
		return str;
	}
}
