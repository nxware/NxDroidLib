package net.bsnx.jlib.xml;

import java.io.OutputStream;
import java.io.PrintWriter;

public class XmlWriter {

	protected PrintWriter out;
	
	public XmlWriter(OutputStream stream) {
		out = new PrintWriter(stream);
	}
	
	public XmlWriter tag(String tagName, String content) {
		out.print("<"+tagName+">" + XML.escapeValue(content) + "</"+tagName+">");
		return this;
	}
	
	public void close() {
		out.close();
	}

}
