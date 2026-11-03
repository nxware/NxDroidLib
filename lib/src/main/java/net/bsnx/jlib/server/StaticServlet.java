package net.bsnx.jlib.server;

import net.bsnx.jlib.net.Mime;

/**
 * Servlet um statische Daten auszugeben
 * 
 * @author Bjoern R. Salgert
 * @date 25.05.2011
 */
public class StaticServlet implements IServlet {

	private String content;
	
	/**
	 * Erzeugt ein Servet das einfach "Hallo Welt" ausgibt.
	 */
	public StaticServlet() {
		this("Hallo Welt");
	}
	
	/**
	 * Erzeugt ein Servet das $content mit dem MimeTyp html ausgibt.
	 * 
	 * @param content
	 */
	public StaticServlet(String content) {
		this.content = content;
	}
	
	@Override
	public void service(IServletRequest req, IServletResponse res) {
		res.setContentType(Mime.HTML);
		res.getWriter().write(content);
	}

}
