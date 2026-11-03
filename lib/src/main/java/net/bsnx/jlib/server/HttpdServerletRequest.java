package net.bsnx.jlib.server;

import net.bsnx.jlib.net.MultiPartFormDataReader;
import net.bsnx.jlib.net.QueryString;

public class HttpdServerletRequest implements IServletRequest, IMultipartProvider {

	HttpHeadLine mHeadLine;
	
	String mPath;
	
	byte[] mBody = null;
	
	void setHeadLine(HttpHeadLine line) {
		mHeadLine = line;
		mPath = line.getPath();
	}
	
	void setBody(byte[] val) {
		mBody = val;
	}

	
	@Override
	public String getProtocol() {
		return mHeadLine.getProtocol();
	}

	@Override
	public String getPathTranslated() {
		return mPath;
	}

	@Override
	public String getMethod() {
		return mHeadLine.getMethod();
	}
	
	public byte[] getBody() {
		return mBody;
	}

	@Override
	public String getUrl() {
		return "http://localhost" + mHeadLine.getFullPath();
	}
	
	@Override
	public String getQueryString() {
		return QueryString.FromUrlString(getUrl()).toString();
	}
	
	// }}}
	
	MultiPartFormDataReader multipart = null;
	
	public MultiPartFormDataReader getMultipart() {
		return multipart;
	}
	
	public void setMultipart(MultiPartFormDataReader val) {
		multipart = val;
	}

}