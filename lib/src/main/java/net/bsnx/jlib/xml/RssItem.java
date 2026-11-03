package net.bsnx.jlib.xml;

import net.bsnx.i.Titled;
import net.bsnx.jlib.DateUtils;

import org.w3c.dom.Element;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @link http://podcast.wdr.de/radio/1liveklubbing.xml			Beispiel eines Podcasts
 * 
 * @category RSS
 * @author Bjoern R. Salgert
 * @date 21.07.2011
 */
public class RssItem implements Titled {

	String title;
	String link;
	String guid;
	String description;
	String pubDate;
	String content;
	NElement elem;
	
	public RssItem(Element e) {
		try {
			elem = new NElement(e);
			title = elem.val("title");
			link  = elem.val("link");
			pubDate = elem.val("pubDate");
			description = elem.val("description");
			guid = elem.val("guid");
			content = elem.val("content:encoded");
		} catch (Exception ex) {
			// Schluck das ding!
		}
	}
	
	@Override
	public String toString() {
		return title + " ("+link+")";
	}

	public String getTitle() {
		return title;
	}

	public String getLink() {
		return link;
	}

	public String getGuid() {
		return guid;
	}

	public String getDescription() {
		return description;
	}
	
	public String getContent() {
		return content;
	}
	
	public String getEnclosureUrl() {
		return elem.elem("enclosure").attr("url");
	}
	
	/**
	 * Liefert den Eintrag des pubDate-Elementes als UnixTimestap zurueck
	 * 
	 * Beispiel was in einem pubDate stehen kann "Tue, 29 Jan 2002 22:14:02 -0500"
	 * 
	 * @return
	 */
	public int getTimeStamp() {
		// http://exampledepot.com/egs/java.text/ParseDate.html
		// http://stackoverflow.com/questions/2705548/parse-rss-pubdate-to-date-object-in-java
		SimpleDateFormat formatter = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss Z");
	    try {
			Date date = (Date)formatter.parse(elem.val("pubDate"));
			return (int)(date.getTime()/1000);
		} catch (Exception e) {
			return DateUtils.currentUnixTimeStamp();
		}
	}
	
}
