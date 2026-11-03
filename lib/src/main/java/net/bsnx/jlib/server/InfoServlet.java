package net.bsnx.jlib.server;

import net.bsnx.jlib.net.Mime;
import net.bsnx.jlib.text.html.PropertyGrid;

public class InfoServlet implements IServlet {

	@Override
	public void service(IServletRequest req, IServletResponse res) {
		res.setContentType(Mime.HTML);
		PropertyGrid grid = new PropertyGrid();
		
		grid.add("req.getMethod()", req.getMethod());
		grid.add("getPathTranslated()", req.getPathTranslated());
		grid.add("req.getUrl()", req.getUrl());
		
		res.getWriter().write(grid.toHtml());
		
	}
	
}
