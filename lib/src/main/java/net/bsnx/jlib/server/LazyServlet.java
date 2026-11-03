package net.bsnx.jlib.server;

import net.bsnx.i.ResultFunction;
import net.bsnx.jlib.net.Mime;

/**
 * Servlet dem eine Funktion Uebergeben wird die erst ausgewertet wird wenn das Serverlet aufgerufen wird.
 * 
 * @author Bjoern R. Salgert
 * @date 25.05.2011
 */
public class LazyServlet implements IServlet {

	private ResultFunction<String> content;

	public LazyServlet(ResultFunction<String> content) {
		this.content = content;
	}
	
	@Override
	public void service(IServletRequest req, IServletResponse res) {
		res.setContentType(Mime.HTML);
		res.getWriter().print(content.execute());
	}

}
