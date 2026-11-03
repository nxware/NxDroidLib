package net.bsnx.jlib.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;


public class NElement {
	
	private Element e;
	
	public NElement(Element e) {
		this.e = e;
	}
	
	public NElement(String xmlstr) {
		Document doc;
		try {
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse((new InputSource(new StringReader(xmlstr))));
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		e = doc.getDocumentElement();
	}
	
	/**
	 * Setzt ein Attribut mit einem Wert
	 * 
	 * @param name
	 * @param val
	 * @return this
	 */
	public NElement attr(String name, String val) {
		e.setAttribute(name, val);
		return this;
	}
	
	public String attr(String name) {
		return e.getAttribute(name);
	}
	
	public List<NElement> iter(String tag_name) {
		List<NElement> res = new ArrayList<NElement>();
		NodeList nodes = e.getElementsByTagName(tag_name); 
		for(int i = 0; i < nodes.getLength(); i++) {
			res.add(new NElement((Element) nodes.item(i)));
		}
		return res;
	}
	
	/**
	 * Liefert den Wert|Inhalt eines Kind Elementes zurueck, oder einen leeren String wenn das KindElement nicht existiert
	 * 
	 * @param name
	 * @return
	 */
	public String val(String name) {
		String res = "";
		try {
			return ((Element)e.getElementsByTagName(name).item(0)).getTextContent().trim();
		} catch (Exception e) {}
		return res;
	}
	
	public String val() {
		return e.getTextContent().trim();
	}
	
	/**
	 * Liefert ein Element zur�ck oder erstellt es.
	 * 
	 * @param name
	 * @return
	 */
	public NElement elem(String name) {
		try {
			Element dom = (Element)e.getElementsByTagName(name).item(0);
			if (dom == null) {
				dom = e.getOwnerDocument().createElement(name);
				e.appendChild(dom);
			}
			return new NElement(dom);
		} catch (Exception ex) {
			Element newElem = e.getOwnerDocument().createElement(name);
			e.appendChild(newElem);
			return new NElement(newElem);
		}
	}

	public NElement add(String tagName) {
		return new NElement(XML.insert(e, tagName));
	}
	
	public void add(String name, String value) {
		XML.insert(e, name, value);
	}

	public int intVal(String name) {
		return Integer.parseInt(val(name));
	}
	
	public boolean contains(String elementName) {
		return e.getElementsByTagName(elementName).getLength()>0;
	}
	
	public boolean containsAttr(String attributeName) {
		return e.hasAttribute(attributeName);
	}
	
	void append(Element child) {
		e.appendChild(child);
	}

	public String name() {
		return e.getTagName();
	}
	
	public List<NElement> children() {
		List<NElement> res = new ArrayList<NElement>();
		NodeList nodes = e.getChildNodes(); 
		for(int i = 0; i < nodes.getLength(); i++) {
			if (nodes.item(i) instanceof Element) {
				res.add(new NElement((Element) nodes.item(i)));
			}
		}
		return res;
	}
	
	@Override
	public String toString() {
		DOMImplementationLS lsImpl = (DOMImplementationLS)e.getOwnerDocument().getImplementation().getFeature("LS", "3.0");
	    LSSerializer lsSerializer = lsImpl.createLSSerializer();
	    StringBuilder sb = new StringBuilder();
	    sb.append(lsSerializer.writeToString(e));
	    return sb.toString(); 
	}

	public String xpathValue(String xpathQuery) {
		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();
		XPathExpression expr;
		try {
			expr = xpath.compile(xpathQuery);
			return expr.evaluate(e, XPathConstants.STRING).toString();
		} catch (XPathExpressionException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	public List<NElement> xpathItems(String xpathQuery) {
		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();
		XPathExpression expr;
		try {
			expr = xpath.compile(xpathQuery);
			NodeList res = (NodeList)expr.evaluate(e, XPathConstants.NODESET);
			return toList(res);
		} catch (XPathExpressionException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	private static List<NElement> toList(NodeList lst) {
		List<NElement> res = new ArrayList<NElement>();
		for (int i = 0; i < lst.getLength(); i++) {
			if (lst.item(i) instanceof Element) {
				res.add(new NElement((Element)lst.item(i)));
			}
		}
		return res;
	}

}