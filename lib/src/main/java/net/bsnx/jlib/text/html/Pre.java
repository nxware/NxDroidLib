package net.bsnx.jlib.text.html;

public class Pre implements HtmlAble {

	private String content;
	
	public Pre(String content) {
		this.content = content;
	}

	@Override
	public String toHtml() {
		return "<pre>"+content+"</pre>";
	}
	
	@Override
	public String toString() {
		return toHtml();
	}
	
}
