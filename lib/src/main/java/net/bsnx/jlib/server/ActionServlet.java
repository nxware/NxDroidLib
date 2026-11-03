package net.bsnx.jlib.server;

import net.bsnx.i.Callback;
import net.bsnx.jlib.net.Mime;

import java.io.PrintWriter;

public class ActionServlet implements IServlet {

	public enum CallbackParam {
		URL,
		PATH_TRANSLATED,
		QUERY
	}
	
	String content;
	Runnable action;
	Callback<String> callback;
	CallbackParam cb_param = CallbackParam.URL;
	
	public ActionServlet(String content, Runnable action) {
		this.content = content;
		this.action = action;
	}
	
	/**
	 * 
	 * @param content
	 * @param action
	 */
	public ActionServlet(String content, Callback<String> action) {
		this.content = content;
		this.callback = action;
	}
	
	public ActionServlet(String content, Callback<String> action, CallbackParam cb_param) {
		this.content = content;
		this.callback = action;
		this.cb_param = cb_param;
	}
	
	@Override
	public void service(IServletRequest req, IServletResponse res) {
		res.setContentType(Mime.TEXT);
		PrintWriter out = res.getWriter();
		try {
			if (action != null) {
				action.run();
			}
			if (callback != null) {
				switch (cb_param) {
				case URL:
					callback.execute(req.getUrl());
					break;
				case PATH_TRANSLATED:
					callback.execute(req.getPathTranslated());
				case QUERY:
					callback.execute(req.getQueryString());
				}
				
			}
			out.print(content);
		} catch (Exception e) {
			e.printStackTrace(out);
		}
		
	}

}
