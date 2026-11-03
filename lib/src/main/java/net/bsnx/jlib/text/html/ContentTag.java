package net.bsnx.jlib.text.html;

public class ContentTag extends Tag {

	public ContentTag(String name, HtmlAble[] tag) {
		super(name, tag);
	}
	
	public ContentTag(String name) {
		super(name);
	}

	public void println(final String str) {
		add(new StrTag(str));
	}
	
	public void js(final String str) {
		println("<script>"+str+"</script>");
	}
	
	public ContentTag button(String title, String href) {
		add(new StrTag("<a href=\""+href+"\"><button>"+title+"</button></a>"));
		return this;
	}

}
