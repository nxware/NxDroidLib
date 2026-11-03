package net.bsnx.jlib.ui;

import net.bsnx.data.reflection.IDataObjectReflection;
import net.bsnx.data.reflection.IProperty;
import net.bsnx.jlib.text.html.Form;
import net.bsnx.jlib.xml.XML;

public class DataForm extends Form {

	private IDataObjectReflection data;

	public DataForm(String action, String method, IDataObjectReflection data) {
		super(action, method);
		this.data = data;
		process();
		addSubmit();
	}
	
	protected void process() {
		for (IProperty	p : data.getProperties()) {
			doProperty(p);
		}
	}
	
	protected void doProperty(IProperty p) {
		String n = p.getName();
		String ctrl = "<div class=\"form-group\">\n" +
				"<label for=\"form_"+n+"\">"+n+"</label>\n"+
				"<input type=\"text\" "+XML.attr("value", p.getValue().toString())+" class=\"form-control\" id=\"form_"+n+"\" name=\""+n+"\" placeholder=\"\">"+
    	"</div>";
		add(ctrl);
	}
	
	

}
