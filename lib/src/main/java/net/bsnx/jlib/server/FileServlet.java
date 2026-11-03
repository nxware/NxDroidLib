package net.bsnx.jlib.server;

import net.bsnx.jlib.net.Mime;

import java.io.File;

/**
 * Servlet das eine Datei ausgibt
 * 
 * @category Servlet
 * @author Bjoern Salgert
 * @date 04.06.2011
 */
public class FileServlet extends BaseServlet {

	private File f;
	
	private String mime;
	
	public FileServlet(File f) {
		this.f = f;
		this.mime = Mime.forFile(f);
	}
	
	public FileServlet(File f, String mime) {
		this.f = f;
		this.mime = mime;
	}
	
	@Override
	public void service(IServletRequest req, IServletResponse res) {
		servFile(res, f, mime);
	}

}
