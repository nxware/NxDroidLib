package net.bsnx.jlib.server;

import net.bsnx.jlib.net.UrlPath;
import net.bsnx.jlib.text.html.A;

import java.io.PrintWriter;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Router bzw. Dispatcher Servet
 * 
 * Stellt eine Art Verzeichnis dar.
 * 
 * @author Bjoern R. Salgert
 * @date 25.05.2011
 */
public class RouterServlet implements IServlet {

	private IServlet index;
	private Map<String, IServlet> map;
	private IServlet not_found;

	/**
	 * 
	 * @param index		Der Index wird aufregufen wenn keine URL Uebergeben wurde
	 * @param map		Map fuer die eizelnen Verzeichnisse
	 * @param not_found	Wird aufgerufen wenn in der Map nichts passendes gefunden wurde
	 */
	public RouterServlet(IServlet index, Map<String, IServlet> map, IServlet not_found) {
		this.index = index;
		this.map = map;
		this.not_found = not_found;
		if (this.not_found == null) {
			this.not_found = new StaticServlet("Not Found.");
		}
	}
	
	public RouterServlet(IServlet index, Map<String, IServlet> map) {
		this(index, map, null);
	}
	
	public RouterServlet(final Map<String, IServlet> map) {
		this(new IServlet() {
			public void service(IServletRequest req, IServletResponse res) {
				res.setContentType("text/html");
				PrintWriter out = res.getWriter();
				out.println("<h1>Index of</h1>");
				Set<Entry<String, IServlet>> set = map.entrySet();
				for (Entry<String, IServlet> entry : set) {
					out.println("<div>"+(new A(entry.getKey()+"/", entry.getKey()))+"</div>\n");
				}
			}
		}, map);
	}
	
	@Override
	public void service(IServletRequest req, IServletResponse res) {
		final IServletRequest _req = req;
		final UrlPath path = new UrlPath(req.getPathTranslated());
		if (path.isEmpty()) {
			index.service(req, res);
		} else {
			String first = path.first();
			if (map.containsKey(first)) {
				IServlet servlet = map.get(first);
				servlet.service(new RouterServletRequest(_req, path), res);
			} else {
				not_found.service(req, res);
			}
		}
		
	}

}
