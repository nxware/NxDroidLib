package net.bsnx.jlib.server;

import net.bsnx.jlib.net.Mime;

import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;

public abstract class BaseServlet implements IServlet {

	protected void servHtml(IServletResponse res, String html) {
		res.setContentType(Mime.HTML);
		PrintWriter out = res.getWriter();
		out.print(html);
	}
	
	protected void servFile(IServletResponse res, File f, String mime) {
		try {
			if (res instanceof HttpdServletResponse) {
				((HttpdServletResponse)res).readFile(f, mime);
			} else {
				res.setContentType(mime);
				PrintWriter out = res.getWriter();
				FileReader reader = new FileReader(f);
				char[] buf = new char[1024];
				int i = reader.read(buf, 0, 1024);
				do {
					out.write(buf, 0, i);
					i = reader.read(buf, 0, 1024);
				} while (i>0);
				reader.close();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}
