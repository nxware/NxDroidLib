package net.bsnx.jlib.xml;

import net.bsnx.i.Titled;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * OPML Dokument
 * 
 * @link http://www.bsnx.net/w/xml/opml
 * @category OPML
 * 
 * @author Bjoern R. Salgert
 * @date 21.07.2011
 */
public class OpmlDocument implements Titled, Iterable<Outline> {

	public static void main(String[] args) {
		OpmlDocument doc = new OpmlDocument();
		doc.addOutline("bsnx.net", "http://www.bsnx.net/feed.rss");
		
		System.out.println(doc);
		System.out.println(doc.getOutlines().iterator().next().toString());
	}
	
	private Document doc;
	
	private NElement head;
	
	private NElement body;
	
	/**
	 * Erzeugt ein neues leeres OPML-Dokument
	 */
	public OpmlDocument() {
		doc = XML.create("opml");
		Element root = doc.getDocumentElement();
		root.setAttribute("version", "1.1");
		NElement r = new NElement(root);
		head = r.elem("head");
		body = r.elem("body");
	}
	
	public OpmlDocument(String xmlstr) {
		doc = XML.parse(xmlstr);
		Element root = doc.getDocumentElement();
		NElement r = new NElement(root);
		head = r.elem("head");
		body = r.elem("body");
	}
	
	public Iterable<Outline> getOutlines() {
		List<Outline> res = new ArrayList<Outline>();
		for (NElement elem : body.iter("outline")) {
			res.add(new Outline(elem));
		}
		return res;
	}
	
	/**
	 * Fuegt einen neue Outline ins Dokument ein
	 * 
	 * @param title
	 * @param url
	 * @return
	 */
	public Outline addOutline(String title, String url) {
		Element eOutline = doc.createElement("outline");
		body.append(eOutline);
		eOutline.setAttribute("title", title);
		eOutline.setAttribute("url", url);
		return new Outline(new NElement(eOutline));
	}
	
	@Override
	public String toString() {
		return XML.toString(doc);
	}

	/**
	 * Liefert den Titel des Dokumentes zur�ck.
	 * 
	 * (Der Titel wird im head-Bereich des Dokumentes in einem titel-Element gespeichert)
	 */
	@Override
	public String getTitle() {
		if (head.contains("title")) {
			return head.elem("title").val();
		} else {
			return "";
		}
	}

	@Override
	public Iterator<Outline> iterator() {
		return getOutlines().iterator();
	}
	
}
