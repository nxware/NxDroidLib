package net.bsnx.jlib.server;

import net.bsnx.jlib.net.MultiPartFormDataReader;
import net.bsnx.jlib.net.UrlPath;

public class RouterServletRequest implements IServletRequest, IMultipartProvider {

	private IServletRequest _req;
	private UrlPath path;
	
	public RouterServletRequest(IServletRequest req, UrlPath path) {
		_req = req;
		this.path = path;
	}
	
	public String getProtocol() {
		return _req.getProtocol();
	}
	public String getPathTranslated() {
		return path.extract().toString();
	}
	public String getMethod() {
		return _req.getMethod();
	}
	@Override
	public String getUrl() {
		return _req.getUrl();
	}
	@Override
	public String getQueryString() {
		return _req.getQueryString();
	}

	@Override
	public MultiPartFormDataReader getMultipart() {
		if (_req instanceof IMultipartProvider) {
			return ((IMultipartProvider)_req).getMultipart();
		} else {
			return null;
		}
	}
	
}
