package net.bsnx.jlib.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import java.io.File;
import java.io.FileReader;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Hilfsfunktionen fuer dem Umgang mit XML
 * 
 * @author Bjoern R. Salgert
 * @date 28.05.2011
 */
public class XML {

	public static final String DECLARATION = " ?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
	
	// {{{ insert(...)
	
	public static Element insert(Document doc, Element parent, String tagName) {
		Element res = doc.createElement(tagName);
		parent.appendChild(res);
		return res;
	}
	
	public static Element insert(Element parent, String tagName) {
		Element res = parent.getOwnerDocument().createElement(tagName);
		parent.appendChild(res);
		return res;
	}
	
	public static void insert(Element parent, String name, String value) {
		Element elem = insert(parent, name);
		elem.setTextContent(value);
	}
	
	// }}}
	
	public static Document parse(String xmlstr) {
		try {
			return DocumentBuilderFactory
				.newInstance()
				.newDocumentBuilder()
				.parse((new InputSource(new StringReader(xmlstr))));
		} catch (Exception e) {
			throw new RuntimeException("Fehler beim XML parsen.", e);
		}
	}
	
	public static Document parse(File file) {
		try {
			return DocumentBuilderFactory
				.newInstance()
				.newDocumentBuilder()
				.parse((new InputSource(new FileReader(file))));
		} catch (Exception e) {
			throw new RuntimeException("Fehler beim XML parsen.", e);
		}
	}
	
	/**
	 * Erzeugt ein neues leeres XML-Dokument mit nur einem Wurzel-Element
	 * 
	 * @param rootName
	 * @return
	 */
	public static Document create(String rootName) {
		try {
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			Element root = doc.createElement(rootName);
			doc.appendChild(root);
			return doc;
		} catch (Exception e) {
			throw new RuntimeException("Fehler beim erstellen des HtmlDokumentes", e);
		}
	}
	
	/**
	 * Erzeugt einen XML-String aus einem XML-Dokument
	 * 
	 * @param doc
	 * @return
	 */
	public static String toString(Document doc) {
		try {
			// Seit Android API Level 8
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			StreamResult result = new StreamResult(new StringWriter());
			DOMSource source = new DOMSource(doc);
			transformer.transform(source, result);
	
			return result.getWriter().toString();
		} catch (TransformerException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Ohne f�hrendes Leerzeichen
	 * 
	 * @param key
	 * @param val
	 * @return
	 */
	public static String attr(String key, String val) {
		// TODO escape
		return key + "=\"" + val + "\"";
	}
	
	public static String attrs(String...attr) {
		StringBuilder sb = new StringBuilder();
		for (String str : attr) {
			sb.append(' ');
			sb.append(str);
		}
		return sb.toString();
	}
	
	public static String escapeValue(String s) {
		return s.replace("<", "&lt;").replace(">", "&gt;");
	}
	
}
