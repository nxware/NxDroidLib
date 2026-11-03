package net.bsnx.jlib.text.html;

import net.bsnx.jlib.net.Mime;
import net.bsnx.jlib.xml.XML;

import java.util.ArrayList;
import java.util.List;

public class Form implements HtmlAble {

	private String action;
	private String method;
	private String enctype;
	private List<Object> controls = new ArrayList<Object>();

	public Form(String action, String method) {
		this.action = action;
		this.method = method;
	}
	
	public Form setEnctype(String mime) {
		this.enctype = mime;
		return this;
	}
	
	public Form enableFileUploads() {
		return setEnctype(Mime.FILE_UPLOAD);
	}
	
	public void add(String ctrl) {
		controls.add(ctrl);
	}
	
	public Form addFileUpload(String name) {
		enableFileUploads();
		if (method.equals("GET")) method = "POST";
		add("<input type=\"file\" "+XML.attr("name", name)+" />");
		return this;
	}
	
	public void addSubmit() {
		add("<input type=\"submit\" name=\"submit\" value=\"Senden\" />");
	}

	@Override
	public String toHtml() {
		if (method == null) {
			method = "POST";
		}
		StringBuilder sb = new StringBuilder();
		sb.append("<form ");
		sb.append(XML.attr("action", action));
		sb.append(" " + XML.attr("method", method));
		if (enctype != null) sb.append(" " + XML.attr("enctype", enctype));
		sb.append(">\n");
		for (Object ctrl : controls) {
			sb.append(ctrl.toString());
		}
		sb.append("</form>\n");
		return sb.toString();
	}

	public void addTextBox(String name) {
		add("<input type=\"text\" name=\""+name+"\" value=\"\" />");
	}
	
}
