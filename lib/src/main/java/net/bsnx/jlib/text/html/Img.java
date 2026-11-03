package net.bsnx.jlib.text.html;

public class Img extends Tag {

	public Img() {
		super("img");
		isSelfClosing = true;
	}
	
	public Img src(String href) {
		attr("src", href);
		return this;
	}
	
	public Img alt(String href) {
		attr("alt", href);
		return this;
	}

}
