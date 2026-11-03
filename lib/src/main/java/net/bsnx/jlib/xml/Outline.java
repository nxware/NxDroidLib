package net.bsnx.jlib.xml;

import net.bsnx.i.Titled;

import java.util.ArrayList;
import java.util.List;

/**
 * Ein OPML Outline Element
 * 
 * @link http://www.bsnx.net/w/xml/opml
 * @category OPML
 * 
 * @author Bjoern R. Salgert
 * @date 21.07.2011
 */
public class Outline implements Titled {

	private NElement elem;
	
	public Outline(NElement elem) {
		this.elem = elem;
	}

	/**
	 * Liefert den Titel der Outline zurueck
	 * 
	 * (es kann auch nur einen Text geben)
	 */
	@Override
	public String getTitle() {
		return elem.attr("title");
	}
	
	public String getText() {
		return elem.attr("text");
	}
	
	public String getType() {
		return elem.attr("type");
	}
	
	public String getUrl() {
		if (elem.containsAttr("url")) {
			return elem.attr("url");
		} else if (elem.containsAttr("xmlUrl")) {
			return elem.attr("xmlUrl");
		} else {
			throw new RuntimeException("Outline enth�lt kein URL-Attribut.");
		}
	}
	
	public String getXmlUrl() {
		if (elem.containsAttr("xmlUrl")) {
			return elem.attr("xmlUrl");
		} else if (elem.containsAttr("url")) {
			return elem.attr("url");
		} else {
			throw new RuntimeException("Outline enth�lt kein XML-URL-Attribut.");
		}
	}
	
	public Iterable<Outline> getOutlines() {
		List<Outline> res = new ArrayList<Outline>();
		for (NElement e : elem.iter("outline")) {
			res.add(new Outline(e));
		}
		return res;
	}
	
	public String attr(String name) {
		return elem.attr(name);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("<outline"+XML.attrs(
			XML.attr("title", getTitle()),
			XML.attr("url", getUrl()),
			XML.attr("type", getType())
		)+">");
		for (Outline outline : getOutlines()) {
			sb.append(outline.toString());
		}
		sb.append("</outline>");
		return sb.toString();
	}
	
	
	
}
