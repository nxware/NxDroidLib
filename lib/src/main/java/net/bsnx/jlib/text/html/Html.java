package net.bsnx.jlib.text.html;

import java.util.ArrayList;
import java.util.List;

/**
 * @todo Titled ? 
 * 
 * @author Bjoern R. Salgert
 * @date 22.05.2011
 */
public class Html implements HtmlAble {
	
	private String head;
	private List<Object> body = new ArrayList<Object>();
	
	public Html() {
		this("", "");
	}
	
	public Html(String head, String body) {
		this.head = head;
		this.body.add(body);
	}
	
	public Html title(String val) {
		head += H.tag("title", "", val);
		return this;
	}
	
	public Html inlineStyle(String css) {
		head += H.tag("style", "", css);
		return this;
	}
	
	public Html js(String jsUrl) {
		head += H.tag("script", " src=\""+jsUrl+"\"", "");
		return this;
	}
	
	public Html append(String val) {
		this.body.add(val);
		return this;
	}
	
	public void println(String s) {
		append(s);
	}
	
	public void div(String content, String attr) {
		append(H.tag("div", attr, content));
	}
	
	public void pre(String content) {
		append(H.tag("pre", "", content));
	}
	
	public void img(String src) {
		append(H.img(src));
	}
	
	@Override
	public String toHtml() {
		StringBuilder sb = new StringBuilder();
		sb.append("<html>\n");
		sb.append("<head>\n");
		sb.append(head);
		sb.append("</head>\n");
		sb.append("<body>\n");
		for (Object obj : body) {
			if (obj instanceof HtmlAble) {
				sb.append(((HtmlAble)obj).toHtml());
			} else {
				sb.append(obj.toString());
			}
		}
		sb.append("</body>\n");
		sb.append("</html>\n");
		return sb.toString();
	}

}
