package net.bsnx.jlib.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Les und schreibares HtmlDokument
 * 
 * @author Bjoern R. Salgert
 * @date 29.05.2011
 */
public class HtmlDocument {

	private Document doc;
	
	private Element root;
	
	private NElement html;
	private NElement head;
	private NElement body;
	
	public HtmlDocument() {
		createNew();
	}
	
	public HtmlDocument(File f) {
		if (!f.exists()) {
			createNew();
			return;
		}
		try {
			doc = DocumentBuilderFactory
				.newInstance()
				.newDocumentBuilder()
				.parse(f);
			root = doc.getDocumentElement();
			html = new NElement(root);
			head = html.elem("head");
			body = html.elem("body");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private void createNew() {
		try {
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			root = doc.createElement("html");
			doc.appendChild(root);
			html = new NElement(root);
			head = html.elem("head");
			body = html.elem("body");
		} catch (Exception e) {
			throw new RuntimeException("Fehler beim erstellen des HtmlDokumentes", e);
		}
	}
	
	public boolean containsMetaKey(String name) {
		for (NElement elem : head.iter("meta")) {
			if (elem.attr(name).equals("property")) {
				return true;
			}
		}
		return false;
	}
	
	public String meta(String name) {
		for (NElement elem : head.iter("meta")) {
			if (elem.attr("property").equals(name)) {
				return elem.attr("content"); 
			}
		}
		return "";
	}
	
	public HtmlDocument meta(String name, String val) {
		if (containsMetaKey(name)) {
			for (NElement elem : head.iter("meta")) {
				if (elem.attr(name).equals("property")) {
					elem.attr("content", val); 
				}
			}
		} else {
			head.add("meta").attr("property", name).attr("content", val);
		}
		return this;
	}
	
	public final NElement getBody() {
		return body;
	}
	
	public final NElement getHead() {
		return head;
	}
	
	@Override
	public String toString() {
		return XML.toString(doc);
	}
	
	public void saveToFile(File f) {
		try {
			FileWriter writer = new FileWriter(f);
			writer.append(toString());
			writer.close();
		} catch (IOException e) {
			throw new RuntimeException("Fehler beim Speichern des HtmlDokumentes", e);
		}
	}
	
}
