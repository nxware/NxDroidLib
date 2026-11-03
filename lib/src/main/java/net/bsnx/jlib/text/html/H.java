package net.bsnx.jlib.text.html;

import net.bsnx.Php;

public class H {
	
	public static boolean ALLOW_SELF_CLOSING = true;
	
	public static void main(String[] args){}
	
	public static String img(String src) {
		return "<img src=\""+src+"\" />\n";
	}

	public static String tag(String tagName, String attr, String content) {
		return "<"+tagName+attr(attr)+">"+content+"</"+tagName+">";
	}
	
	public static String attr(String name, String value) {
		return " " + name + '"' + value + '"';
	}
	
	public static String attr(String attrs) {
		if (!"".equals(attrs)) {
			return " " + attrs;
		} else {
			return "";
		}
	}
	
	public static String attrs(String[] attrs) {
		return " "+ Php.implode(" ", attrs);
	}
	
	public static String iframe(String src, int width, int height) {
		return H.tag("iframe", " src=\""+src+"\" width=\""+width+"\" height=\""+height+"\"", "");
	}

	public static String a(String content, String href) {
		return tag("a", " href=\""+href+"\"", content);
	}

	public static String ul(String...item) {
		StringBuilder sb = new StringBuilder();
		sb.append("<ul>");
		for (String s : item) {
			sb.append(H.tag("li", "", s));
		}
		sb.append("</ul>");
		return sb.toString();
	}

	public static String script(String js) {
		if (js.startsWith("http")) {
			return tag("script", "src=\""+js+"\"", "");
		} else {
			return tag("script", "", js);
		}
	}
	
	public static String css(String css) {
		return "<link rel=\"stylesheet\" type=\"text/css\" media=\"all\" "+
			   " href=\""+css+"\" />";
	}

	public static String div(String content) {
		return tag("div", "", content);
	}
	
	public static String pre(String content) {
		return tag("pre", "", content);
	}
	
	public static String textarea(String name, String text, String...attrs) {
		return tag("textarea", " name=\""+name+"\"" + attrs(attrs), text);
	}
}
