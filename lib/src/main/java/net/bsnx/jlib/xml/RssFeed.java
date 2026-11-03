package net.bsnx.jlib.xml;

import net.bsnx.i.Titled;
import net.bsnx.jlib.NetUtils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;

/**
 * @link http://de.wikipedia.org/wiki/RSS
 * @category RSS
 * @author Bjoern R. Salgert
 * @date 21.07.2011
 */
public class RssFeed implements Iterable<RssItem>, Titled {

	public static final String MIME = "application/rss+xml";
	
	private Document doc;
	
	private Element channel;
	
	private List<RssItem> items = new ArrayList<RssItem>();
	
	public RssFeed(String xmlstr) {
		try {
			doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse((new InputSource(new StringReader(xmlstr))));
			Element e = doc.getDocumentElement();
			channel = (Element) e.getElementsByTagName("channel").item(0);
			NodeList list = channel.getElementsByTagName("item");
			for (int i = 0; i < list.getLength(); i++) {
				Element item = (Element) list.item(i);
				items.add(new RssItem(item));
			}
		} catch (Exception e) {
			throw new RuntimeException("RssFeed Error on " + xmlstr, e);
		}
	}
	
	public RssFeed(URL url) {
		this(NetUtils.wget(url));
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (RssItem item : items) {
			sb.append(item);
			sb.append("\n");
		}
		return sb.toString();
	}
	
	public List<RssItem> getItems() {
		return items;
	}

	@Override
	public Iterator<RssItem> iterator() {
		return items.iterator();
	}
	
	/**
	 * Liefert den Inhalt eines Elements im Bereich des Channels zurueck
	 * 
	 * @param key
	 * @return
	 */
	public String getMetaValue(String key) {
		try {
			return channel.getElementsByTagName(key).item(0).getTextContent().trim();
		} catch(Exception e) {
			return "";
		}
	}

	@Override
	public String getTitle() {
		return getMetaValue("title");
	}
	
}
