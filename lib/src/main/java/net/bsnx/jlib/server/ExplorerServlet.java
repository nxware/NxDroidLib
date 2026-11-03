package net.bsnx.jlib.server;

import net.bsnx.jlib.FileSystem;
import net.bsnx.jlib.net.Mime;
import net.bsnx.jlib.net.MultiPartFormDataReader;
import net.bsnx.jlib.text.html.A;
import net.bsnx.jlib.text.html.Form;

import java.io.File;

/**
 * DirectionaryListing Servlet
 * 
 * @author Bjoern R. Salgert
 * @date
 */
public class ExplorerServlet extends BaseServlet {

	private File root;

	/**
	 * 
	 * @param root	Root fuer die Verzeichnisliste
	 */
	public ExplorerServlet(File root) {
		this.root = root;
	}
	
	private File build(String path) {
		path = FileSystem.removeAllTrailingPathDelimiters(path);
		return new File(root.getAbsolutePath() + "/" + path);
	}
	
	@Override
	public void service(IServletRequest req, IServletResponse res) {
		String path = req.getPathTranslated();
		File f = build(path);
		if (req instanceof IMultipartProvider) {
			IMultipartProvider hsr = (IMultipartProvider)req;
			if (hsr.getMultipart() != null) {
				MultiPartFormDataReader reader = hsr.getMultipart();
				//String new_path = reader.get("path");
				reader.saveToFile(new File(f.getAbsolutePath() + "/" + reader.getFilename()));
				servHtml(res, "Saved.");
				System.out.println("Datei gespeichert.");
				return;
			}
		}
		if ("POST".equalsIgnoreCase(req.getMethod())) {
			servHtml(res, "Hier die Weitere POST verarbeitung.");
			return;
		}
		if (f.exists()) {
			if (f.isDirectory()) {
				StringBuilder sb = new StringBuilder();
				sb.append("<h1>Index of "+path+"</h1>");
				String[] list = f.list();
				for (String item : list) {
					sb.append(new A(item + "/", item));
					sb.append("<br>");
				}
				Form form = new Form("", "POST");
				form.addFileUpload("f");
				form.addTextBox("path");
				form.addSubmit();
				sb.append(form.toHtml());
				servHtml(res, sb.toString());
			} else {
				servFile(res, f, Mime.forFile(f));
			}
		} else {
			servHtml(res, "404 File not found.");
		}
	}

}
